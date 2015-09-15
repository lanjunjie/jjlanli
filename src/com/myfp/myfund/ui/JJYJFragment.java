package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.Achievements;
import com.myfp.myfund.api.beans.Risk;

/**
 * 基金业绩
 * 
 * @author Max.Zhao
 * 
 */
public class JJYJFragment extends BaseFragment {

	private DetailActivity activity;
	private String fundCode;
	private ListView lv_achievements, lv_risk;
	private List<Achievements> alist;
	private List<Risk> rList;
	private LinearLayout ll_jjyj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (DetailActivity) getActivity();
		activity.showProgressDialog("正在加载");
		fundCode = activity.getFundCode();
		RequestParams params = new RequestParams(activity);
		params.put("InputFundValue", fundCode);
		activity.execApi(ApiType.GET_FUND_ACHIEVEMENTS, params, this);
		activity.execApi(ApiType.GET_FUND_RISK, params, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_detail_jjyj, null, false);

		lv_achievements = (ListView) view
				.findViewById(R.id.lv_jjyjFragment_achievements);
		lv_risk = (ListView) view.findViewById(R.id.lv_jjyjFragment_risk);
		ll_jjyj = (LinearLayout)view.findViewById(R.id.ll_jjyj);
		ll_jjyj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), RiskIndicatorActivity.class);
				startActivity(intent);
			}
		});
        
		return view;
	}

	@Override
	protected void onViewClick(View v) {
		

	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_FUND_ACHIEVEMENTS && !activity.isFinishing()) {
			alist = JSON.parseArray(json, Achievements.class);
			AchievementsAdapter aAdapter = new AchievementsAdapter(alist);
			lv_achievements.setAdapter(aAdapter);
			setListViewHeightBasedOnChildren(lv_achievements, aAdapter);
			activity.disMissDialog();
		} else if (api == ApiType.GET_FUND_RISK && !activity.isFinishing()) {
			rList = JSON.parseArray(json, Risk.class);
			RiskAdapter rAdapter = new RiskAdapter(rList);
			lv_risk.setAdapter(rAdapter);
			setListViewHeightBasedOnChildren(lv_risk, rAdapter);
		}
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView,
			ListAdapter listAdapter) {
		if (listView == null)
			return;

		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	class AchievementsAdapter extends BaseAdapter {

		private List<Achievements> list;

		public AchievementsAdapter(List<Achievements> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_jjyjfragment_achievements_list, null, false);
			TextView tv_time = (TextView) convertView
					.findViewById(R.id.tv_achievements_time);
			TextView tv_return = (TextView) convertView
					.findViewById(R.id.tv_achievements_return);
			TextView tv_ranking = (TextView) convertView
					.findViewById(R.id.tv_achievements_ranking);

			tv_time.setText(list.get(position).getPerformanceDate());
			if ("-".equals(list.get(position).getRateOfReturn().substring(0, 1))) {
				tv_return.setTextColor(Color.rgb(4, 151, 1));
			}
			tv_return.setText(list.get(position).getRateOfReturn());

			tv_ranking.setText(list.get(position).getSimilarRankings1());
			return convertView;
		}
	}

	class RiskAdapter extends BaseAdapter {

		private List<Risk> list;

		public RiskAdapter(List<Risk> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_jjyjfragment_risk_list, null, false);
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_risk_name);
			TextView tv_param = (TextView) convertView
					.findViewById(R.id.tv_risk_param);
			TextView tv_level = (TextView) convertView
					.findViewById(R.id.tv_risk_level);

			tv_name.setText(list.get(position).getIndexName());
			tv_param.setText(list.get(position).getParameterValues());
			tv_level.setText(list.get(position).getEvaluation());
			return convertView;
		}
	}
}
