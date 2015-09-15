package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.Tuijian;
import com.myfp.myfund.utils.ImageCacheManager;

public class RcommendmainActivity extends BaseActivity {
	private ListView recommend;
	private List<Tuijian> list;
	private List<String> bannerUrl = new ArrayList<String>();
	private String iamgeurl;
	private List<String> bannerDetail = new ArrayList<String>();
	private RequestQueue requestQueue;
	private ImageLoader loader;
	private View left;
	private long lastBack;
	private SharedPreferences sPreferences;
	private String sessionid;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_rcommendmian);
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.TYPE, 1);
		execApi(ApiType.GET_TUIJIANBANNER, params);
		requestQueue = Volley.newRequestQueue(this);
		loader = new ImageLoader(requestQueue, ImageCacheManager.getInstance());
		sPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
		sessionid = App.getContext().getSessionid();
		
	}

	@Override
	protected void initViews() {
		setTitle("展恒推荐");
		recommend = (ListView) findViewById(R.id.recommendlv);
		findViewAddListener(R.id.rcommend_img3);
		left=findViewById(R.id.ll_top_layout_left_view);
		left.setVisibility(View.GONE);
//		findViewAddListener(R.id.recommendlv);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.recommendlv:
			
			break;
		
		case R.id.rcommend_img3:
			Intent intent3= new Intent(this,RcommendActivity.class);
			intent3.putExtra("sessionId", sessionid);
			System.out.println("sessionid2"+sessionid);
			startActivity(intent3);
			break;
		default:
			break;
		}

	}
		@Override
		public void onReceiveData(ApiType api, String json) {
			if (json == null) {
				disMissDialog();
				showToast("获取失败!");
				return;
			}
			if(api==ApiType.GET_TUIJIANBANNER){

					final List<Tuijian> res = JSON.parseArray(json, Tuijian.class);
					recommend.setAdapter(new MyAdapter(this, res));
					setListViewHeightBasedOnChildren(recommend);
					recommend.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							
							Tuijian results = res.get(position);
							Intent intent=new Intent();
							intent.putExtra("sessionId", sessionid);
							intent.putExtra("Url", results.getBannerURL());
							intent.setClass(RcommendmainActivity.this, ShowWebActivity.class);
							
							startActivity(intent);
						}
					});
			
			}
		}

		private void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
		return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
		View listItem = listAdapter.getView(i, null, listView);
		listItem.measure(0, 0);
		totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
		+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		}
	class MyAdapter extends BaseAdapter {
		private List<Tuijian> list;
		private LayoutInflater mInflater;
		
		public MyAdapter(Context context,List<Tuijian> mlist){
			this.list=mlist;
			this.mInflater=LayoutInflater.from(context);
			
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
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder =new ViewHolder();
				if(convertView==null){
					convertView=mInflater.inflate(R.layout.item_layout_rcommend, null);
					holder.image=(NetworkImageView) convertView.findViewById(R.id.imagerecmmend);
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				Tuijian res = list.get(position);
				String quanPic = res.getBannerPic();
				String 	url = quanPic.substring(1);
				iamgeurl = "http://www.myfund.com" + url;
				System.out.println("imageuri"+iamgeurl);
				holder.image.setImageUrl(iamgeurl, loader);
//			image.setImageUrl(iamgeurl,loader);
			return convertView;
		}
			class ViewHolder{
				public NetworkImageView image;
			}
	}
	@Override
	public void onBackPressed() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastBack > 2000) {
			lastBack = curTime;
			showToast("再按一次退出展恒基金网");
		} else {
			String listStr = JSON.toJSONString(App.getContext().userList);
			Editor editor = sPreferences.edit();
			editor.putString("UserList", listStr);
			editor.commit();
			finish();
			
		}
	}
}
