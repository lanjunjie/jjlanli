package com.myfp.myfund.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;



public class PushedDetailActivity extends BaseActivity {
	private WebView webView;
	private String url,detailURL;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pusheddetail);
		
		Intent intent = getIntent();
		detailURL = intent.getStringExtra("DetailURL");
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("查看详情");

		webView = (WebView) findViewById(R.id.pushed_searchDetail);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		webView.loadUrl(detailURL);
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
			disMissDialog();
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
		// TODO Auto-generated method stub

	}

}
