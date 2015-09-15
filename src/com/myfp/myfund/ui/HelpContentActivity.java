package com.myfp.myfund.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.HelpList;

public class HelpContentActivity extends BaseActivity {
	
	private String title;
	private HelpList help;
	private TextView tv_title,tv_answer,tv_time,tv_more;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_help_content);
		
		title = getIntent().getStringExtra("Title");
		try {
			title = URLEncoder.encode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		RequestParams params = new RequestParams(this);
		params.put("Title", title);
		execApi(ApiType.GET_HELP_CONTENT1, params);
	}

	@Override
	protected void initViews() {
		setTitle("使用帮助");
		
		tv_title = (TextView) findViewById(R.id.tv_helpContent_title);
		tv_answer = (TextView) findViewById(R.id.tv_helpContent_answer);
		tv_time = (TextView) findViewById(R.id.tv_helpContent_time);
		tv_more  = (TextView) findViewById(R.id.tv_helpContent_more);
		tv_more.setVisibility(View.GONE);
	}

	@Override
	protected void onViewClick(View v) {

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		help = JSON.parseArray(json, HelpList.class).get(0);
		tv_title.setText(help.getTitle());
		tv_answer.setText(help.getAnsContent());
		tv_time.setText(help.getAddDate());
	}
	
}
