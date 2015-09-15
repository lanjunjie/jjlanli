package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.GetFundReview;

public class FundCommentListActivity extends BaseActivity {
	
	private ListView listView_commentList;
	private String fundCode,fundName;
	private List<GetFundReview> reviews;
	private TextView tv_publish;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fund_comment);
		showProgressDialog("正在加载");
		fundCode = getIntent().getStringExtra("FundCode");
		fundName = getIntent().getStringExtra("FundName");

		RequestParams params = new RequestParams(this);
		params.put("fundcode", fundCode);
		execApi(ApiType.GET_FUND_REVIEW, params);
		
	}

	@Override
	protected void onStart() {

		RequestParams params = new RequestParams(this);
		params.put("fundcode", fundCode);
		execApi(ApiType.GET_FUND_REVIEW, params);
		
		super.onStart();
	}
	@Override
	protected void initViews() {
		setTitle("基金评论");
		
		listView_commentList = (ListView) findViewById(R.id.listView_fundComment_list);
		tv_publish = (TextView) findViewAddListener(R.id.tv_text_main_publish);
		tv_publish.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_text_main_publish:
			Intent intent = new Intent(this,CommentActivity.class);
			intent.putExtra("FundCode", fundCode);
			intent.putExtra("FundName", fundName);
			startActivity(intent);
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
		if (api == ApiType.GET_FUND_REVIEW) {
			reviews = JSON.parseArray(json, GetFundReview.class);
			listView_commentList.setAdapter(new ReviewListAdapter(reviews));
		}
		disMissDialog();
	}

	class ReviewListAdapter extends BaseAdapter {

		private List<GetFundReview> list;

		public ReviewListAdapter(List<GetFundReview> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater
					.from(FundCommentListActivity.this)
					.inflate(R.layout.item_review_list, null, false);
			TextView tv_userName = (TextView) convertView
					.findViewById(R.id.textView_review_username);
			TextView tv_sendTime = (TextView) convertView
					.findViewById(R.id.textView_review_sendTime);
			TextView tv_fundName = (TextView) convertView
					.findViewById(R.id.textView_review_fundName);
			TextView tv_fundCode = (TextView) convertView
					.findViewById(R.id.textView_review_fundCode);
			TextView tv_content = (TextView) convertView
					.findViewById(R.id.textView_review_content);

			GetFundReview review = list.get(position);
			if(review.getUserName().length()==11){
				tv_userName.setText(review.getUserName().replace(review.getUserName().substring(3, 7), "****"));
			}else{
				tv_userName.setText(review.getUserName());
			}
			
			tv_sendTime.setText(review.getSendTime());
			tv_fundName.setText(review.getFundName());
			tv_fundCode.setText("("+review.getFundCode()+")");
			tv_content.setText(review.getFundReview());

			return convertView;
		}

	}
}
