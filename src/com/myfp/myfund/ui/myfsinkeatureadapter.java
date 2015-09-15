package com.myfp.myfund.ui;

import java.util.List;

import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.SolidChargeResult;
import com.myfp.myfund.ui.MySolidChargeFragment.MySolidChargeAdapter.ViewHolder;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class myfsinkeatureadapter extends BaseAdapter {
	List<SolidChargeResult> mList;
	private SolidChargeResult res;
	MyFixationActivity activity;
	
	public myfsinkeatureadapter(List<SolidChargeResult> list,MyFixationActivity activity){
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
		ViewHolder holder = null;
		

		if (convertView==null) {
			holder=new ViewHolder();
				convertView = LayoutInflater.from(activity).inflate(R.layout.item_sink_eature, null);
				
				System.out.println("convertView=-=-=-=>"+convertView);
				holder.tex_sinkeaturt_titl = (TextView) convertView.findViewById(R.id.tex_sinkeaturt_titl);
				holder.tex_sinkeaturt_qi = (TextView) convertView.findViewById(R.id.tex_sinkeaturt_qi);
				holder.tex_sinkeaturt_money = (TextView) convertView.findViewById(R.id.tex_sinkeaturt_money);
				holder.tex_sinkeaturt_rise = (TextView) convertView.findViewById(R.id.tex_sinkeaturt_rise);
				holder.tex_sinkeaturt_age= (TextView) convertView.findViewById(R.id.tex_sinkeaturt_age);
				holder.tex_sinkeaturt_arrive = (TextView) convertView.findViewById(R.id.tex_sinkeaturt_arrive);
				convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		res = mList.get(position);
		holder.tex_sinkeaturt_titl.setText(res.getSName());
		holder.tex_sinkeaturt_qi.setText(res.getTerm());
		holder.tex_sinkeaturt_money.setText(res.getFnetmoney());
		holder.tex_sinkeaturt_rise.setText(res.getStartdate());
		holder.tex_sinkeaturt_age.setText(res.getSmodel());
		holder.tex_sinkeaturt_arrive.setText(res.getDEstimateEnd());
		
		return convertView;
		
	}
	class ViewHolder{
		TextView tex_sinkeaturt_titl,tex_sinkeaturt_qi,tex_sinkeaturt_money,tex_sinkeaturt_rise,tex_sinkeaturt_age,tex_sinkeaturt_arrive;
	}
	

}
