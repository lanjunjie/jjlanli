package com.myfp.myfund.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.FeatureWebActivity.LoadHttpsChrome;
import com.myfp.myfund.ui.FeatureWebActivity.LoadHttpsView;
import com.myfp.myfund.utils.DataCleanManager;

public class SomeMoneyWebActivity extends BaseActivity{
	private String userName;
	private String quanurl;
	private String image,detail;
	private WebView webView;
	private String filepath = "/data/data/com.myfp.myfund/cache/webviewCacheChromium";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_show_web);
		showProgressDialog("正在加载");
		Intent intent = getIntent();
		quanurl = intent.getStringExtra("QuanURL");
		image = intent.getStringExtra("QuanPic");
		detail = intent.getStringExtra("QuanDetail");
		execApi(ApiType.GET_LUNSTAMPS, null);
	}
	@Override
	protected void initViews() {
		setTitle("活动页面");
		//if (image!=null||detail!=null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_right_view);
			layout.setVisibility(View.VISIBLE);
			ImageView img = (ImageView) findViewAddListener(R.id.iv_mainactivity_top_right);
			img.setImageResource(R.drawable.share_bt);
	//}
		webView = (WebView) findViewById(R.id.webView_deal_show);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		DataCleanManager.cleanCustomCache(filepath);
		webView.loadUrl(quanurl);
		
	}
	class LoadHttpsView extends WebViewClient {
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			view.loadUrl(quanurl);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			webView.setVisibility(View.GONE);
			showToast("无法连接到网络");
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			disMissDialog();
			super.onPageFinished(view, url);
		}

	}

	class LoadHttpsChrome extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			if (newProgress >= 90) {
			}
			super.onProgressChanged(view, newProgress);
		}
		
		

		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();
		}
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_mainactivity_top_right:
			//SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
			//userName = preferences.getString("UserName", null);
			//RequestParams params = new RequestParams(this);
			//params.put(RequestParams.USERNAME, userName);
			//System.out.println("params+++++++"+params);
			//execApi(ApiType.GET_SHARELINKS, params);
			showShare();
		
			
		}
	}
/*	@Override
	public void onReceiveData(ApiType api, String json) {
		if(json==null){
			showToast("请求失败!");
			disMissDialog();
			return;
		}
		if(api==ApiType.GET_SHARELINKS){
		
			JSONObject obj;
			try {
				obj = new JSONObject(json);
				System.out.println("jsonArray---------->"+obj);
				
					int code=Integer.parseInt(obj.getString("Code"));
					String  hint=obj.getString("Hint");
					Boolean info=obj.getBoolean("Info");
					System.out.println("code++++"+code+"hint"+hint+"info"+info);
					// 启动分享GUI	
					//showShare();

					//if(code==0000){
						
					//	showToast("分享到朋友圈，优惠券将增加50元！");
				//	}
		
		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		// TODO Auto-generated method stub
//		super.onReceiveData(api, json);
		}	
	}  */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(quanurl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(detail+quanurl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + File.separator + "QRCode.jpg");
		oks.setImageUrl(image);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(quanurl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("活动页面");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(quanurl);

		// 启动分享GUI
		oks.show(this);
	}

	
}
