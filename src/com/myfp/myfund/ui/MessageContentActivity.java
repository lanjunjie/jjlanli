package com.myfp.myfund.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MessageContent;

public class MessageContentActivity extends BaseActivity {
	
	private String title;
	private MessageContent msg;
	private TextView tv_title,tv_content,tv_time;
	private String url;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_help_content);
		showProgressDialog("正在加载");
		title = getIntent().getStringExtra("Title");
		try {
			title = URLEncoder.encode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		RequestParams params = new RequestParams(this);
		params.put("Title", title);
		execApi(ApiType.GET_NEWS_CONTENT, params);
	}

	@Override
	protected void initViews() {
		setTitle("消息中心");
		
		tv_title = (TextView) findViewById(R.id.tv_helpContent_title);
		tv_content = (TextView) findViewById(R.id.tv_helpContent_answer);
		tv_time = (TextView) findViewById(R.id.tv_helpContent_time);
		findViewAddListener(R.id.tv_helpContent_more);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_helpContent_more:
			Intent banner = new Intent(this,ShowWebActivity.class);
			banner.putExtra("Url", url);
			startActivity(banner);
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		msg = JSON.parseArray(json, MessageContent.class).get(0);
		tv_title.setText(msg.getTitle());
		tv_content.setText(msg.getContent());
		tv_time.setText(msg.getAddDate());
		url = msg.getMsgDetailURL();
		disMissDialog();
	}
	
}
