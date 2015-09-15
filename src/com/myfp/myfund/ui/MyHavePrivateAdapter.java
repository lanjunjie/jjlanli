package com.myfp.myfund.ui;

import java.util.List;

import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.MyHoldAPrivateResutl;
import com.myfp.myfund.ui.MyPrivateProductsActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyHavePrivateAdapter extends BaseAdapter{
	List<MyHoldAPrivateResutl> mList;
	MyPrivateProductsActivity activity;
	public MyHavePrivateAdapter(List<MyHoldAPrivateResutl> list,MyPrivateProductsActivity activity){
		this.mList=list;
		this.activity=activity;
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.activity_have_placement, null);
			holder.te_have_name = (TextView) convertView.findViewById(R.id.te_have_name);
			holder.te_have_currentvalue = (TextView) convertView.findViewById(R.id.te_have_currentvalue);
			holder.te_have_cost= (TextView) convertView.findViewById(R.id.te_have_cost);
			holder.te_have_holdf = (TextView) convertView.findViewById(R.id.te_have_holdf);
			holder.te_have_earnings = (TextView) convertView.findViewById(R.id.te_have_earnings);
			holder.te_have_market = (TextView) convertView.findViewById(R.id.te_have_market);
			holder.te_have_yield = (TextView) convertView.findViewById(R.id.te_have_yield);
			holder.te_have_xtone = (TextView) convertView.findViewById(R.id.te_have_xtone);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyHoldAPrivateResutl res = mList.get(position);
		holder.te_have_name.setText(res.getSName());
		holder.te_have_currentvalue.setText(res.getCurrnav());
		holder.te_have_cost.setText(res.getCostAmount());
		holder.te_have_holdf.setText(res.getAmountvol());
		holder.te_have_earnings.setText(res.getFloatprofit());
		holder.te_have_market.setText(res.getShiZhi());
		holder.te_have_yield.setText(res.getRate()+"%");
		holder.te_have_xtone.setText(res.getOpentime());
		return convertView;
	}
	class ViewHolder{
		TextView te_have_name,
		te_have_currentvalue,
		te_have_cost,
		te_have_holdf,
		te_have_earnings,
		te_have_market,
		te_have_yield,
		te_have_xtone;
	}
	
}

