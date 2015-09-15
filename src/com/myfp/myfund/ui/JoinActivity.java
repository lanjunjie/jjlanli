package com.myfp.myfund.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

@SuppressLint("SetJavaScriptEnabled")
public class JoinActivity extends BaseActivity{
	private WebView webView;
	private TextView textview;
	private static final String TAG = "JoinActivity";
	private String  joinUrl;
	private String url;

	// private static String url = "http://192.168.1.123:8080/appweb";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_join);
		
		showProgressDialog("正在加载");
		
		Intent intent = getIntent();
		joinUrl = intent.getStringExtra("JoinURL");

		if (joinUrl != null) {
			url = joinUrl;
		} 
	}

	
	@Override
	protected void initViews() {
		setTitle("点财通会员");
		webView = (WebView) findViewById(R.id.webView_join);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.loadUrl(url);
		
		webView.clearCache(true);
		webView.destroyDrawingCache();
		
	
	}

	@Override
	protected void onViewClick(View v) {
		
	}

	class LoadHttpsView extends WebViewClient {
		
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			Log.i(TAG, "current " + url);
			view.loadUrl(url);
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			webView.setVisibility(View.GONE);
			showToast("无法连接到网络");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i(TAG, "current >>>>>>>>>>>>>>>>>>> " + url);
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.i(TAG, "current<<<<<<<<<<<<<<<<<<<< " + url);
			disMissDialog();
		}

	}

	class LoadHttpsChrome extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			Log.i(TAG, "current------------------------------------------------------ " + url);
/*			if(newProgress==1){
			showProgressDialog("正在加载中。。。。");
			}else if(newProgress==99){
				disMissDialog();
			}
			if (newProgress >= 90) {
			}*/
			super.onProgressChanged(view, newProgress);
		}
		
		

		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();
		}
	}

}
