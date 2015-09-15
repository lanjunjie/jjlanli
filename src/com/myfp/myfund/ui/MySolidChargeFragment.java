package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import u.aly.co;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
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
import com.myfp.myfund.api.beans.ShowDetailsResult;
import com.myfp.myfund.api.beans.SolidChargeResult;
import com.myfp.myfund.ui.MyShowDetailsFragment.ShowDetailsAdapter;

public class MySolidChargeFragment extends BaseFragment{
	
	private MyFixationActivity activity;
	//private CouponActivity activity;
	private ListView list_defend_view;
	private LinearLayout tishi;
	private List<SolidChargeResult> results;
	private View view;
	private TextView te_yc;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//activity=(CouponActivity)getActivity();
		 activity = (MyFixationActivity) getActivity();
		 Intent intent = activity.getIntent();
			String idcard = intent.getStringExtra("idcard");
			RequestParams params=new RequestParams(activity);
			System.out.println("params我的详情-=-==-=-==-=<>"+params);
		
			params.put("sdzjnumber".trim(), idcard.trim());
			activity.execApi(ApiType.GET_SOLIDCHARGE,params,this);
			activity.showProgressDialog("正在加载");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.fragment_solidcharge, null);
			te_yc = (TextView) view.findViewById(R.id.te_yc);
			te_yc.setVisibility(view.GONE);
			list_defend_view = (ListView) view.findViewById(R.id.list_solid_view);
		return view;
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);


		if (json.equals("[]")) {
			te_yc.setVisibility(View.VISIBLE);
			te_yc.setText("您还没有购买固收产品!");
			activity.disMissDialog();
			return;
		}
		results = JSON.parseArray(json, SolidChargeResult.class);
		
			list_defend_view.setAdapter(new MySolidChargeAdapter(results));
			activity.disMissDialog();
		
		}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class MySolidChargeAdapter extends BaseAdapter{
		List<SolidChargeResult> mList;
		private SolidChargeResult res;
		
		public MySolidChargeAdapter(List<SolidChargeResult> list){
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
			ViewHolder holder = null;
			

			if (convertView==null) {
				holder=new ViewHolder();
					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_defend, null);
					
					System.out.println("convertView=-=-=-=>"+convertView);
					holder.t_name_view = (TextView) convertView.findViewById(R.id.t_name_view);
					holder.t_purchasing_date = (TextView) convertView.findViewById(R.id.t_purchasing_date);
					holder.t_monetary = (TextView) convertView.findViewById(R.id.t_monetary);
					holder.t_interest = (TextView) convertView.findViewById(R.id.t_interest);
					holder.t_expiration_stime= (TextView) convertView.findViewById(R.id.t_expiration_stime);
					holder.t_deadline = (TextView) convertView.findViewById(R.id.t_deadline);
					holder.t_hua_shou = (TextView) convertView.findViewById(R.id.t_hua_shou);
					convertView.setTag(holder);
				
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			res = mList.get(position);
			if (res!=null) {
				if (res.getSName()==null) {
					
					holder.t_name_view.setText("--");
				}else {
					holder.t_name_view.setText(res.getSName());
				}
				if (res.getDtransaction()==null) {
					holder.t_purchasing_date.setText("--");
				}else {
					holder.t_purchasing_date.setText(res.getDtransaction());
					
				}
				if (res.getFnetmoney()==null) {
					holder.t_monetary.setText("--");

				}else {
					
					holder.t_monetary.setText(res.getFnetmoney());
				}
				if (res.getStartdate()==null) {
					holder.t_interest.setText("--");
				}else {
					holder.t_interest.setText(res.getStartdate());
				}
				if (res.getDEstimateEnd()==null) {
					holder.t_expiration_stime.setText("--");

				}else {
					holder.t_expiration_stime.setText(res.getDEstimateEnd());
					
				}
				if (res.getTerm()==null) {
					holder.t_deadline.setText("--");

				}else {
					
					holder.t_deadline.setText(res.getTerm());
				}
				if (res.getSmodel()==null) {
					holder.t_hua_shou.setText("--");

				}else {
					
					holder.t_hua_shou.setText(res.getSmodel());
				}
				}
			return convertView;
			
		}
		class ViewHolder{
			TextView t_name_view,t_purchasing_date,t_monetary,t_interest,t_expiration_stime,t_deadline,t_hua_shou;
		}
		
	}

}
