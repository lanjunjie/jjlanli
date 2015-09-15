package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MemberInformationResult;

public class MyMemberInformationFragment extends BaseFragment{
	
	private ListView list_Member;
	private MyPropertyActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Intent intent = activity.getIntent();
		String idcard = intent.getStringExtra("idcard");
		RequestParams params=new RequestParams(activity);
		params.put("idcard", idcard.trim());
		activity.execApi(ApiType.GET_PURCHASEDETAILS, params);
	}
	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_applicant_details, null);
		list_Member = (ListView) view.findViewById(R.id.list_Member);
		
		return view;
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("获取数据失败");
			activity.disMissDialog();
			return;
		}
		 List<MemberInformationResult> list = JSON.parseArray(json, MemberInformationResult.class);
		list_Member.setAdapter(new MemberInformationAdapter(list));
	}
	class MemberInformationAdapter extends BaseAdapter{
		List<MemberInformationResult> mList;
		public MemberInformationAdapter(List<MemberInformationResult> list){
			this.mList=list;
		}
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_applicant_detailsitem,null);
				holder.te_fund_name=(TextView) convertView.findViewById(R.id.te_fund_name);
				holder.te_purchase_date = (TextView) convertView.findViewById(R.id.te_purchase_date);
				holder.te_Amount = (TextView) convertView.findViewById(R.id.te_Amount);
				holder.te_buy_charge = (TextView) convertView.findViewById(R.id.te_buy_charge);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			MemberInformationResult res = mList.get(position);
			holder.te_fund_name.setText(res.getDctVipTypeName());
			holder.te_purchase_date.setText(res.getDctVipBeginDate());
			holder.te_Amount.setText(res.getAlreadyReturnApplyCost());
			holder.te_buy_charge.setText(res.getCanReturnApplyCost());
			return convertView;
		}
		class ViewHolder{
			TextView te_fund_name,te_purchase_date,te_Amount,te_buy_charge;
		}
		
	}

}
