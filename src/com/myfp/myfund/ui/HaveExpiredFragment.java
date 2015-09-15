package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.TobeUsedResult;
import com.myfp.myfund.ui.HasbeenusedFragment.TobeUsedAdapter;
import com.myfp.myfund.ui.HasbeenusedFragment.TobeUsedAdapter.ViewHolder;

public class HaveExpiredFragment extends BaseFragment{
	private CouponActivity activity;
	private ListView list_coupon_view;
	private LinearLayout youhuiquan;
	private View view;
	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		activity = (CouponActivity) getActivity();
		Intent intent = activity.getIntent();
		String username = intent.getStringExtra("userName");
		RequestParams params=new RequestParams(activity);
		params.put("username", username);
		params.put("Status", "2");
		activity.execApi(ApiType.GET_DISCUNTCOUPON,params,this);
		activity.showProgressDialog("正在加载");
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_couponlistview, null);
		list_coupon_view = (ListView) view.findViewById(R.id.list_coupon_view);
		youhuiquan=(LinearLayout) view.findViewById(R.id.youhuiquan);
		youhuiquan.setVisibility(view.GONE);
		return view;
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json.equals("[]")) {
			activity.showToastLong("您还没有此记录");
			activity.disMissDialog();
			return;
		}
		List<TobeUsedResult> results = JSON.parseArray(json, TobeUsedResult.class);
		
		list_coupon_view.setAdapter(new TobeUsedAdapter(results));

		activity.disMissDialog();
		
		}
	class TobeUsedAdapter extends BaseAdapter{
		List<TobeUsedResult> mList;
		private TobeUsedAdapter(List<TobeUsedResult> list){
			this.mList=list;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			

			if (convertView==null) {
				holder=new ViewHolder();
				convertView = LayoutInflater.from(activity).inflate(R.layout.fragment_havexpired, null);
				holder.te_pastdue_money = (TextView) convertView.findViewById(R.id.te_pastdue_money);
				holder.te_pastdue_date = (TextView) convertView.findViewById(R.id.te_pastdue_date);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			TobeUsedResult res = mList.get(position);
				holder.te_pastdue_money.setText("￥"+res.getAmount());
				holder.te_pastdue_date.setText(res.getStartTermination());
				
		return convertView;
		}
		class ViewHolder{
			TextView te_pastdue_money,te_pastdue_date;
		}
		
	}
}
