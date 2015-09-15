package com.myfp.myfund.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;

public class QustionActivity extends BaseActivity {

	private WebView webView;
	private String url, name;
	private String baseURL = "http://m.myfund.com";
	@SuppressLint("SdCardPath")
	private String hycqustion = "/Hyc/chyc_question.html";
	private String hdlqustion = "/hdl/chdl_question.html";
	private String hdlhetong = "/Ccenter/chdl_xieyi.html";
	private String hychetong = "/Hyc/xieyi.html";
	public static QustionActivity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_qustion);
		instance = this;
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		name = intent.getStringExtra("name");
		if (type.equals("5")) {
			url = baseURL + hdlqustion;
		} else if (type.equals("6")) {
			url = baseURL + hycqustion;
		} else if (type.equals("15")) {
			url = baseURL + hdlhetong;
		} else if (type.equals("16")) {
			url = baseURL + hychetong;
		}

		System.out.println(url);

	}

	@Override
	protected void initViews() {
		setTitle(name);

		webView = (WebView) findViewById(R.id.webView_clause1);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.loadUrl(url);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
