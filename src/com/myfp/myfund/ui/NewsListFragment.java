package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.NewsListResult;


/**
 * 资讯列表
 * @author Max.Zhao
 *
 */
public class NewsListFragment extends BaseFragment {
	
	private int index;
	private ListView listView_newsList;
	private FundNewsActivity activity;
//	private String[][] text_titles = {{"最新资讯","展恒快讯","基金公告","活动公告"},{"专题文章","宏观策略","经济策略","投资策略"}};
	private TextView[] subhead;
	private ApiType url;
	private int newstype;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		
		activity =  (FundNewsActivity) getActivity();

		switch (index) {
		case 0:
			newstype=1;
			url = ApiType.GET_FUNDNEWS;
			break;
		case 1:
			newstype=2;
			url = ApiType.GET_FUNDNEWS;
//			url = ApiType.GET_RESEARCHNEWS;
			break;
		}
		
//		RequestParams params = new RequestParams(activity);
//		params.put("newstype", 1);
//		activity.execApi(url, params,this );
//		activity.showProgressDialog("正在加载");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_newslist, null, false);
		
		
		listView_newsList = (ListView) view.findViewById(R.id.listView_newsList);
		RequestParams params = new RequestParams(activity);
		params.put("newstype",newstype);
		activity.execApi(url, params,NewsListFragment.this );
		activity.showProgressDialog("正在加载");
		return view;
	}
	

	
	class NewsListAdapter extends BaseAdapter{
		
		private List<NewsListResult> data;
		
		public NewsListAdapter(List<NewsListResult> data) {
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
			NewsListResult nlResult = data.get(position);
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_fundnews_list, null, false);
				holder = new ViewHolder();
				holder.textView_title = (TextView) convertView.findViewById(R.id.textView_newslist_title);
				holder.textView_time = (TextView) convertView.findViewById(R.id.textView_newslist_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView_title.setText(nlResult.getTitle());
			String[] time = nlResult.getAddDate().split(" ");
			holder.textView_time.setText(time[0]);
			return convertView;
		}
		
		class ViewHolder{
			TextView textView_title,textView_time;
		}
		
	}

	@Override
	protected void onViewClick(View v) {
		
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("获取数据失败");
			activity.disMissDialog();
			return;
		}
		
		final List<NewsListResult> datas = JSON.parseArray(json, NewsListResult.class);
		
		listView_newsList.setAdapter(new NewsListAdapter(datas));
		listView_newsList.setOnItemClickListener(new OnItemClickListener() {
			
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long itemId) {
				//通过position从类表中得到当前基金名字
				String title = datas.get(position).getId();
//				int newsType = newstype+index*4;
				Intent intent = new Intent(getActivity(),NewsContentActivity.class);
				intent.putExtra("id", title);
				intent.putExtra("newstype", newstype);
				startActivity(intent);
			}
		});
		activity.disMissDialog();
	}
}
