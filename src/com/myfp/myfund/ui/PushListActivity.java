package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.PushInfo;


public class PushListActivity extends BaseActivity {

	private ListView listView_push;
	private List<PushInfo> pushList;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.pushlist_activity);
		showProgressDialog("正在加载");

		execApi(ApiType.GET_JPUSH_LIST, null);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("最新信息");
		listView_push = (ListView) findViewById(R.id.push_list);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class PushListAdapter extends BaseAdapter {

		private List<PushInfo> data;

		public PushListAdapter(List<PushInfo> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			PushInfo pi = data.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(PushListActivity.this)
						.inflate(R.layout.item_pushlist, null, false);
				holder = new ViewHolder();
				holder.tv_title = (TextView)convertView.findViewById(R.id.push_title);
				holder.tv_addDate = (TextView) convertView.findViewById(R.id.push_addDate);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_title.setText(pi.getTitle().trim());
			holder.tv_addDate.setText(pi.getPushTime());
			// holder.imageView_logo.setDefaultImageResId(R.drawable.ic_launcher);
			// holder.imageView_logo.setErrorImageResId(R.drawable.ic_launcher);
			return convertView;
		}

		class ViewHolder {

			TextView tv_title, tv_addDate;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_JPUSH_LIST) {
			pushList = JSON.parseArray(json, PushInfo.class);
			listView_push.setAdapter(new PushListAdapter(pushList));
			listView_push.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long itemId) {
					Intent intent = new Intent(PushListActivity.this,
							PushedActivity.class);
					intent.putExtra("PushKey", pushList.get(position)
							.getPushKey());
					startActivity(intent);
				}
			});
			disMissDialog();
		}
	}

}
