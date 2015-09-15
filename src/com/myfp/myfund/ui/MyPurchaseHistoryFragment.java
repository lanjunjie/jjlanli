package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.PurchaseHistoryResult;
import com.myfp.myfund.api.beans.SolidChargeResult;
import com.myfp.myfund.ui.MySolidChargeFragment.MySolidChargeAdapter;
import com.myfp.myfund.ui.MySolidChargeFragment.MySolidChargeAdapter.ViewHolder;

public class MyPurchaseHistoryFragment extends BaseFragment{

	private MyFixationActivity activity;
	private ListView list_purchase_history;
	private View view;
	private TextView tishi1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 activity = (MyFixationActivity) getActivity();
		 Intent intent = activity.getIntent();
			String idcard = intent.getStringExtra("idcard");
			RequestParams params=new RequestParams(activity);
			params.put("sdzjnumber".trim(), idcard.trim());
			activity.execApi(ApiType.GET_PURCHASEHISTORY,params,this);
			activity.showProgressDialog("正在加载");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.activity_purchase_history, null);
		list_purchase_history = (ListView) view.findViewById(R.id.list_purchase_history);
		tishi1=(TextView) view.findViewById(R.id.tishi1);
		tishi1.setVisibility(view.GONE);  
		return view;
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json.equals("[]")) {
			tishi1.setVisibility(view.VISIBLE);
			tishi1.setText("您还没有购买固收产品。");
			activity.disMissDialog();
			return;
		}
		List<PurchaseHistoryResult> results = JSON.parseArray(json, PurchaseHistoryResult.class);
		
			list_purchase_history.setAdapter(new MyPurchaseHistoryAdapter(results));

		activity.disMissDialog();
		
		}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class MyPurchaseHistoryAdapter extends BaseAdapter{
		List<PurchaseHistoryResult> mList;
		public MyPurchaseHistoryAdapter(List<PurchaseHistoryResult> list){
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
				if (mList.size()==0) {
					holder=new ViewHolder();
					convertView=LayoutInflater.from(getActivity()).inflate(R.layout.activity_no_data, null);
					holder.no_dete=(TextView)convertView.findViewById(R.id.no_dete);
				}else {

				if (convertView==null) {
					holder=new ViewHolder();
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_purchase_historyitem, null);
					holder.t_product_name_view = (TextView) convertView.findViewById(R.id.t_product_name_view);
					holder.t_purchasing_date_view = (TextView) convertView.findViewById(R.id.t_purchasing_date_view);
					holder.t_amount_view = (TextView) convertView.findViewById(R.id.t_amount_view);
					holder.t_state_view= (TextView) convertView.findViewById(R.id.t_state_view);
					convertView.setTag(holder);
				}else {
					holder = (ViewHolder) convertView.getTag();
				}
				PurchaseHistoryResult res = mList.get(position);
					if (res.getSName()==null) {
						holder.t_product_name_view.setText("--");
					}else {
						holder.t_product_name_view.setText(res.getSName());
					}
					if (res.getDtransaction()==null) {
						holder.t_purchasing_date_view.setText("--");

					}else {
						holder.t_purchasing_date_view.setText(res.getDtransaction());
						
					}
					if (res.getFnetmoney()==null) {
						holder.t_amount_view.setText("--");
					}else {
						holder.t_amount_view.setText(res.getFnetmoney());
						
					}
					if (res.getOperation()==null) {
						holder.t_state_view.setText("--");

					}else {
						
						holder.t_state_view.setText(res.getOperation());
					}
				
				
			return convertView;
				}
				holder.no_dete.setText("您没有此项数据！");
			return parent;
		}
				
		class ViewHolder{
			TextView no_dete;
			TextView t_product_name_view,t_purchasing_date_view,t_amount_view,t_state_view;
			
		}
		
	}


}
