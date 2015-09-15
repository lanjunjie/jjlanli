package com.myfp.myfund.ui;


import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;

public class AboutUsActivity extends BaseActivity {

	private WebView wv_aboutUs;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_about_us);
	}
//
	@Override
	protected void initViews() {
		setTitle("关于展恒");
		
		wv_aboutUs = (WebView) findViewById(R.id.webView_about_us);
		WebSettings webSettings = wv_aboutUs.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wv_aboutUs.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		wv_aboutUs.loadUrl("file:///android_asset/about.html");
	}

	@Override
	protected void onViewClick(View v) {

	}

}
