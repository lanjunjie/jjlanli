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
import com.myfp.myfund.api.beans.HelpList;

public class UsingHelpActivity extends BaseActivity {
	
	private ListView usingHelp;
	private List<HelpList> helps;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_usinghelp);
		
		execApi(ApiType.GET_HELP_CONTENT, null);
	}

	@Override
	protected void initViews() {
		setTitle("使用帮助");
		
		usingHelp = (ListView) findViewById(R.id.listView_setting_usingHelp);
	}

	@Override
	protected void onViewClick(View v) {

	}

	class UsingHelpAdapter extends BaseAdapter{

		private List<HelpList> list;
		
		public UsingHelpAdapter(List<HelpList> list){
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
			
			HelpList help = list.get(position);
			convertView = LayoutInflater.from(UsingHelpActivity.this).inflate(R.layout.item_usinghelp_list, null, false);
			TextView num = (TextView) convertView.findViewById(R.id.textView_usingHelpList_num);
			TextView question = (TextView) convertView.findViewById(R.id.textView_usingHelpList_question);
			TextView answer = (TextView) convertView.findViewById(R.id.textView_usingHelpList_answer);
			num.setText(position+1+"");
			question.setText(help.getTitle());
			answer.setText(help.getAnsContent());
			return convertView;
		}
		
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		helps = JSON.parseArray(json, HelpList.class);
		usingHelp.setAdapter(new UsingHelpAdapter(helps));
		usingHelp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title = helps.get(position).getTitle();
					Intent intent = new Intent(UsingHelpActivity.this, HelpContentActivity.class);
					intent.putExtra("Title", title);
					startActivity(intent);
			}
		});
	}
}
