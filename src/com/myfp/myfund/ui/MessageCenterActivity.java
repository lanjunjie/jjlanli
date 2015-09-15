package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MessageList;

public class MessageCenterActivity extends BaseActivity {

	private ListView listView_msg;
	private List<MessageList> msgs;
	private String userName;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_messagecenter);

		userName = App.getContext().getUserName();

		RequestParams params = new RequestParams(MessageCenterActivity.this);
		params.put("UserName", userName);
		execApi(ApiType.GET_NEWS_LIST, params);
	}

	@Override
	protected void initViews() {
		setTitle("消息中心");

		listView_msg = (ListView) findViewById(R.id.listView_messageCenter_list);
	}

	@Override
	protected void onViewClick(View v) {

	}

	@Override
	protected void onResume() {
		super.onResume();

		// RequestParams params = new RequestParams(MessageCenterActivity.this);
		// params.put("UserName", App.getContext().getUserName());
		// execApi(ApiType.GET_NEWS_LIST, params);
	}

	class UsingHelpAdapter extends BaseAdapter {

		private List<MessageList> list;

		public UsingHelpAdapter(List<MessageList> list) {
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

			MessageList msg = list.get(position);
			convertView = LayoutInflater.from(MessageCenterActivity.this)
					.inflate(R.layout.item_msgcenter_list, null, false);
			TextView tv_title = (TextView) convertView
					.findViewById(R.id.textView_msgCenter_title);
			TextView tv_time = (TextView) convertView
					.findViewById(R.id.textView_msgCenter_time);
			ImageView img_flag = (ImageView) convertView
					.findViewById(R.id.imageView_msgCenter_isReaded);

			convertView.setTag(img_flag);

			tv_title.setText(msg.getTitle());
			tv_time.setText(msg.getAddDate());
			if (msg.getIsReaded() != null && msg.getIsReaded().equals("1")) {
				img_flag.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		if (api == ApiType.GET_NEWS_LIST) {

			msgs = JSON.parseArray(json, MessageList.class);
			listView_msg.setAdapter(new UsingHelpAdapter(msgs));
			listView_msg.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String title = msgs.get(position).getTitle();
					Intent intent = new Intent(MessageCenterActivity.this,
							MessageContentActivity.class);
					intent.putExtra("Title", title);
					startActivity(intent);

					ImageView img = (ImageView) view.getTag();
					img.setVisibility(View.INVISIBLE);

					RequestParams params = new RequestParams(
							MessageCenterActivity.this);
					params.put("UserName", userName);
					params.put("Id", msgs.get(position).getId());
					execApi(ApiType.MSG_IS_READ, params);
				}
			});
		}
	}

}
