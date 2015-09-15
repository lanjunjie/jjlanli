package com.myfp.myfund.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import cn.jpush.android.api.JPushInterface;

import com.myfp.myfund.App;
import com.myfp.myfund.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 最开始的欢迎页面
 * @author Max.Zhao
 *
 */
public class WelcomeActivity extends Activity {

	private boolean isFirst;
	private Context mContext;
	private final  String mPageName = "WelcomeActivity";
	
	private String imgPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/myfund"
			+ File.separator
 			+ "MyFund.jpg"; // 二维码地址
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startActivity(new Intent(WelcomeActivity.this,
						GuideActivity.class));
				WelcomeActivity.this.finish();
				break;
			case 2: 
				startActivity(new Intent(WelcomeActivity.this,
						MyActivityGroup.class));
				WelcomeActivity.this.finish();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		mContext = this;	
		MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
		
		MobclickAgent.updateOnlineConfig(this);
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String path = "http://223.203.189.182:8484/Service/DemoService.svc/myfundCountForAndroid";
				// 新建HttpGet对象
				HttpGet httpGet = new HttpGet(path);
				// 获取HttpClient对象
				HttpClient httpClient = new DefaultHttpClient();
				// 获取HttpResponse实例
				HttpResponse httpResp;
				try {
					httpResp = httpClient.execute(httpGet);
					// 判断是够请求成功
					if (httpResp.getStatusLine().getStatusCode() == 200) {
						// 获取返回的数据
						String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
						System.out.println("HttpGet方式请求成功，返回数据如下：");
						System.out.println(result);
					} else {
						System.out.println("HttpGet方式请求失败");
					}
				} catch (ClientProtocolException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {  
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
			}
			
		}.start();

		initQRCode();
		
		SharedPreferences sPreferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		isFirst = sPreferences.getBoolean("isFirst", true);
  
		Editor editor = sPreferences.edit();
		editor.putBoolean("mainIsStart", false);
		editor.commit();
		
		App.getContext().setFirst(isFirst); 

		if (isFirst) {
			handler.sendEmptyMessageDelayed(1, 3000);
		} else {
			handler.sendEmptyMessageDelayed(2, 3000);
		}
	}
	
	private void initQRCode() {
		try {
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/myfund");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			InputStream is = getAssets().open("myfund.png");
			BitmapFactory.Options options = new BitmapFactory.Options(); 
			options.inSampleSize = 1;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
			
			
			File file = new File(imgPath);

			if (!file.exists()) {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);

				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		JPushInterface.onPause(mContext);
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
		
	}
	
}
