package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.myfp.myfund.api.beans.PurchaseHistoryResult;
import com.myfp.myfund.api.beans.TobeUsedResult;
import com.myfp.myfund.ui.MyPurchaseHistoryFragment.MyPurchaseHistoryAdapter;
import com.myfp.myfund.ui.MyPurchaseHistoryFragment.MyPurchaseHistoryAdapter.ViewHolder;

public class TobeusedFragment extends BaseFragment{

	private CouponActivity activity;
	private ListView list_coupon_view;
	private String username;
	private LinearLayout youhuiquan;
	private View view;
	@Override
	protected void onViewClick(View v) {
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		activity = (CouponActivity) getActivity();
		Intent intent = activity.getIntent();
		 username = intent.getStringExtra("userName");
		RequestParams params=new RequestParams(activity);
		params.put("username", username);
		params.put("Status", "0");
		activity.execApi(ApiType.GET_DISCUNTCOUPON,params,this);
		activity.showProgressDialog("正在加载");
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.activity_couponlistview, null);
		youhuiquan=(LinearLayout) view.findViewById(R.id.youhuiquan);
		youhuiquan.setVisibility(view.GONE);
			
		list_coupon_view = (ListView) view.findViewById(R.id.list_coupon_view);
		
		return view;
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json.equals("[]")) {
			youhuiquan.setVisibility(view.VISIBLE);
			activity.disMissDialog();
			return;
		}
		final List<TobeUsedResult> results = JSON.parseArray(json, TobeUsedResult.class);
		System.out.println("results===================>"+results);
			
				
				list_coupon_view.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TobeUsedResult res = results.get(position);
						Intent intent=new Intent(activity, UponGoodspassActivity.class);
						intent.putExtra("Amount", res.getAmount());
						intent.putExtra("ID", res.getId());
						intent.putExtra("userName", username);
						startActivity(intent);
					}
				});
				
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
					convertView = LayoutInflater.from(activity).inflate(R.layout.fragment_tobeused, null);
					holder.te_stay_money = (TextView) convertView.findViewById(R.id.te_stay_money);
					holder.te_stay_date = (TextView) convertView.findViewById(R.id.te_stay_date);
					convertView.setTag(holder);
				}else {
					holder= (ViewHolder) convertView.getTag();
				}
				TobeUsedResult res = mList.get(position);
				holder.te_stay_money.setText("￥"+res.getAmount());
				holder.te_stay_date.setText(res.getStartTermination());
				
			
			return convertView;
		
		}
		class ViewHolder{
			TextView te_stay_money,te_stay_date;
		}
		
	}

}
