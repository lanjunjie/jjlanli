package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.myfp.myfund.ui.TheDetailsActivity.LoadHttpsChrome;
import com.myfp.myfund.ui.TheDetailsActivity.LoadHttpsView;
import com.myfp.myfund.utils.DataCleanManager;

public class FinancingWebActivity extends BaseActivity{

	private WebView webView;
	private String url, imageID, image, detail;
	private String baseURL = "http://www.myfund.com";
	@SuppressLint("SdCardPath")
	private String filepath = "/data/data/com.myfp.myfund/cache/webviewCacheChromium";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_diancaitong);
		Intent intent = getIntent();
		imageID = intent.getStringExtra("ImageID");
		showProgressDialog("正在加载");
		RequestParams params = new RequestParams(this);
		params.put("imageId", imageID);
		execApi(ApiType.GET_MAINPART, params);
		
	}

	
	@Override
	protected void initViews() {
		setTitle("了解基金配资详情");

		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.share_bt);
		
		webView = (WebView) findViewById(R.id.webView_dct_show);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		//webView.loadUrl("file:///android_asset/dct.html");
	}
	
	class LoadHttpsView extends WebViewClient {
		
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true ;
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			view.loadUrl(url);
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
		//分享
		case R.id.ll_top_layout_right_view:
			showShare();
			break;

		default:
			break;
		}
	}
	
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("获取失败!");
			disMissDialog();
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObj = array.getJSONObject(i);
				System.out.println("jsonObj--------------====================>"+jsonObj);
				url = jsonObj.getString("PageURL"); 
				System.out.println("url+======================>"+url);
				image = jsonObj.getString("ImageURL").substring(0);
				System.out.println("image----------------------------->"+image);
				detail = jsonObj.getString("Description");
				DataCleanManager.cleanCustomCache(filepath);
				System.out.println("filepath=-=-=-=-=-=-=-=-=-=->"+filepath);
				webView.loadUrl(url);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(detail+url);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + File.separator + "QRCode.jpg");
		oks.setImageUrl(baseURL + image);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("展恒基金网");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);

		// 启动分享GUI
		oks.show(this);
	}
}
