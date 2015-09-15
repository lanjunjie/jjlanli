package com.myfp.myfund.ui;

import android.view.View;
import android.webkit.WebView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

/**
 * 服务条款显示页面
 * @author pengchongjia
 *
 */
public class ClauseActivity extends BaseActivity {
	
	private WebView wv_clause;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_clause);
	}

	@Override
	protected void initViews() {
		setTitle("服务条款");
		
		wv_clause = (WebView) findViewById(R.id.webView_clause);
		
		wv_clause.loadUrl("file:///android_asset/terms.html");
	}

	@Override
	protected void onViewClick(View v) {

	}

}
