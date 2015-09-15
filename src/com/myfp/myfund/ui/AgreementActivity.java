package com.myfp.myfund.ui;

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

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.ui.DealActivity.LoadHttpsChrome;

public class AgreementActivity extends BaseActivity {
	private WebView webView;
	private String url;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_agreement);
		showProgressDialog("正在加载");
		Intent intent = getIntent();
		url = intent.getStringExtra("URL");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("协议");
		webView = (WebView) findViewById(R.id.webView_agreement);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		showProgressDialog("正在加载");
		webView.loadUrl(url);

		webView.clearCache(true);
		webView.destroyDrawingCache();

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
			showProgressDialog("正在加载");
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

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
