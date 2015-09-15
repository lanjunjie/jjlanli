package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
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
import com.myfp.myfund.api.beans.MyHoldAPrivateResutl;

public class MyHoldAPrivateFragment extends BaseFragment{
	
	private MyPrivateProductsActivity activity;
	private ListView list_defend_view;
	private TextView tishi;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 activity = (MyPrivateProductsActivity) getActivity();
		 Intent intent = activity.getIntent();
			String idcard = intent.getStringExtra("idcard");
			RequestParams params=new RequestParams(activity);
			params.put("sdzjnumber".trim(), idcard.trim());
			activity.execApi(ApiType.GET_HOLDAPRIVATE,params,this);
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
		List<MyHoldAPrivateResutl> results = JSON.parseArray(json, MyHoldAPrivateResutl.class);
		list_defend_view.setAdapter(new MyHoldAPrivateAdapter(results));
		
		activity.disMissDialog();
		
		}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class MyHoldAPrivateAdapter extends BaseAdapter{
		List<MyHoldAPrivateResutl> mList;
		public MyHoldAPrivateAdapter(List<MyHoldAPrivateResutl> list){
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_holda_private, null);
				holder.te_na_xtone = (TextView) convertView.findViewById(R.id.te_na_xtone);
				holder.te_purchasing_xtone = (TextView) convertView.findViewById(R.id.te_purchasing_xtone);
				holder.te_latest_xtone= (TextView) convertView.findViewById(R.id.te_latest_xtone);
				holder.te_open_xtone = (TextView) convertView.findViewById(R.id.te_open_xtone);
				holder.te_open_xt = (TextView) convertView.findViewById(R.id.te_open_xt);
				holder.te_floating_xtone = (TextView) convertView.findViewById(R.id.te_floating_xtone);
				holder.te_yie_xtone = (TextView) convertView.findViewById(R.id.te_yie_xtone);
				holder.te_pur_xtone = (TextView) convertView.findViewById(R.id.te_pur_xtone);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			MyHoldAPrivateResutl res = mList.get(position);
			holder.te_na_xtone.setText(res.getSName());
			holder.te_purchasing_xtone.setText(res.getAmountvol());
			holder.te_latest_xtone.setText(res.getCostAmount());
			holder.te_open_xtone.setText(res.getCurrnav());
			holder.te_open_xt.setText("("+res.getNavdate()+")");
			holder.te_floating_xtone.setText(res.getOpentime());
			holder.te_yie_xtone.setText(res.getShiZhi());
			holder.te_pur_xtone.setText(res.getRate());
			return convertView;
		}
		class ViewHolder{
			TextView te_na_xtone,
			te_purchasing_xtone,
			te_latest_xtone,
			te_open_xtone,
			te_open_xt,
			te_floating_xtone,
			te_yie_xtone,
			te_pur_xtone;
		}
		
	}

}
