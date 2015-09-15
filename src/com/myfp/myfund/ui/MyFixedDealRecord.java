package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MyHoldAPrivateResutl;
import com.myfp.myfund.api.beans.PrivatePurchaseRecordsResult;
import com.myfp.myfund.api.beans.PurchaseHistoryResult;
import com.myfp.myfund.ui.MyHoldAPrivateFragment.MyHoldAPrivateAdapter;
import com.myfp.myfund.ui.MyHoldAPrivateFragment.MyHoldAPrivateAdapter.ViewHolder;

public class MyFixedDealRecord extends BaseActivity{

	
	private ListView list_fixed_view;
	private LinearLayout tishi1;
	ByteArrayInputStream tInputStringStream = null;
	LayoutInflater layoutInflater;


	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fixed_listview);
		 Intent intent = getIntent();
			String idcard = intent.getStringExtra("idcard");
			RequestParams params=new RequestParams(this);
			params.put("sdzjnumber".trim(), idcard.trim());
			execApi(ApiType.GET_PURCHASEHISTORY,params,this);
			showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		
		list_fixed_view = (ListView)findViewById(R.id.list_fixed_view);
		tishi1=(LinearLayout)findViewById(R.id.tishi1);
		tishi1.setVisibility(View.GONE);
		
	}
	

	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json.equals("[]")) {
			tishi1.setVisibility(View.VISIBLE);
		
			disMissDialog();
			return;
		}

		 List<PurchaseHistoryResult> results = JSON.parseArray(json, PurchaseHistoryResult.class);
		 list_fixed_view.setAdapter(new MyPrivatePurchaseRecordsAdapter(results));
			disMissDialog();
		
		
		}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class MyPrivatePurchaseRecordsAdapter extends BaseAdapter{
		List<PurchaseHistoryResult> mList;
		public MyPrivatePurchaseRecordsAdapter(List<PurchaseHistoryResult> list){
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
				System.out.println("44444444");
				holder=new ViewHolder();
				convertView = LayoutInflater.from(MyFixedDealRecord.this).inflate(R.layout.fragment_private_fixed, null);
				holder.te_name_fixed = (TextView) convertView.findViewById(R.id.te_name_fixed);
				
				holder.te_open_day_fixed = (TextView) convertView.findViewById(R.id.te_open_day_fixed);
				holder.te_floating_profit_fixed= (TextView) convertView.findViewById(R.id.te_floating_profit_fixed);

				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			PurchaseHistoryResult res = mList.get(position);
			holder.te_name_fixed.setText(res.getSName());

			holder.te_open_day_fixed.setText(res.getDtransaction());
			holder.te_floating_profit_fixed.setText(res.getFnetmoney());

			return convertView;
		}
		class ViewHolder{
			TextView te_name_fixed,te_open_day_fixed,te_floating_profit_fixed;
		}
		
	}


}
