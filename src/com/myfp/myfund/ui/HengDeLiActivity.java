package com.myfp.myfund.ui;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;

public class HengDeLiActivity extends BaseActivity {
	
	private WebView webView;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_hengdeli);
	}

	@Override
	protected void initViews() {
		setTitle("恒得利");
		//html5页面
		webView = (WebView) findViewById(R.id.webView_hengdeli);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.loadUrl("file:///android_asset/hdl.html");
	}

	@Override
	protected void onViewClick(View v) {

	}

}
