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
import com.myfp.myfund.ui.MyHoldAPrivateFragment.MyHoldAPrivateAdapter;
import com.myfp.myfund.ui.MyHoldAPrivateFragment.MyHoldAPrivateAdapter.ViewHolder;

public class MyPrivatePurchaseRecordsFragment extends BaseFragment{

	private MyPrivateProductsActivity activity;
	private ListView list_defend_view;
	private TextView tishi;
	ByteArrayInputStream tInputStringStream = null;
	LayoutInflater layoutInflater;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 activity = (MyPrivateProductsActivity) getActivity();
		 Intent intent = activity.getIntent();
			String idcard = intent.getStringExtra("idcard");
			RequestParams params=new RequestParams(activity);
			params.put("sdzjnumber".trim(), idcard.trim());
			activity.execApi(ApiType.GET_PRIVATEEQUITYTOBUY,params,this);
			activity.showProgressDialog("正在加载");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.activity_defend_listview, null);
			list_defend_view = (ListView) view.findViewById(R.id.list_defend_view);
		tishi=(TextView) view.findViewById(R.id.tishi);
		tishi.setVisibility(view.GONE);
		return view;
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json.equals("[]")) {
			tishi.setVisibility(view.VISIBLE);
			tishi.setText("您还没有购买私募产品。");
			activity.disMissDialog();
			return;
		}

		 List<PrivatePurchaseRecordsResult> results = JSON.parseArray(json, PrivatePurchaseRecordsResult.class);
			list_defend_view.setAdapter(new MyPrivatePurchaseRecordsAdapter(results));
			activity.disMissDialog();
		
		
		}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class MyPrivatePurchaseRecordsAdapter extends BaseAdapter{
		List<PrivatePurchaseRecordsResult> mList;
		public MyPrivatePurchaseRecordsAdapter(List<PrivatePurchaseRecordsResult> list){
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_private_equity, null);
				holder.te_name_xtone = (TextView) convertView.findViewById(R.id.te_name_xtone);
				
				//holder.te_purchasing_date_xtone = (TextView) convertView.findViewById(R.id.te_purchasing_date_xtone);
				holder.te_latest_net_xtone = (TextView) convertView.findViewById(R.id.te_latest_net_xtone);
				holder.te_open_day_xtone = (TextView) convertView.findViewById(R.id.te_open_day_xtone);
				holder.te_floating_profit_xtone= (TextView) convertView.findViewById(R.id.te_floating_profit_xtone);
				holder.te_yield_xtone = (TextView) convertView.findViewById(R.id.te_yield_xtone);
				holder.te_purchase_xtone = (TextView) convertView.findViewById(R.id.te_purchase_xtone);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			PrivatePurchaseRecordsResult res = mList.get(position);
			holder.te_name_xtone.setText(res.getSName());
			holder.te_purchasing_date_xtone.setText(res.getDtransaction());
			holder.te_latest_net_xtone.setText(res.getOperation());
			holder.te_open_day_xtone.setText(res.getStartdate());
			holder.te_floating_profit_xtone.setText(res.getFnetmoney());
			holder.te_yield_xtone.setText(res.getAmountvol());
			holder.te_purchase_xtone.setText(res.getUnitPrice());
			return convertView;
		}
		class ViewHolder{
			TextView te_name_xtone,te_purchasing_date_xtone,te_latest_net_xtone,te_open_day_xtone,te_floating_profit_xtone,te_yield_xtone,te_purchase_xtone;
		}
		
	}
	

}
