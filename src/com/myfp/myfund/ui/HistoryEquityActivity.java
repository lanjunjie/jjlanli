package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.GetHistoryEquity;

public class HistoryEquityActivity extends BaseActivity {

	private ListView listView_historyEquity;
	private String inputFundValue,fundType,fundName;
	private List<GetHistoryEquity> histories;
	private TextView tv_unit,tv_total,tv_benefit;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_history_equity);
		showProgressDialog("正在加载");
		Intent intent = getIntent();
		inputFundValue = intent.getStringExtra("InputFundValue");
		fundType = intent.getStringExtra("FundType");
		fundName = intent.getStringExtra("FundName");
		
		RequestParams params = new RequestParams(this);
		params.put("InputFundValue", inputFundValue);
		execApi(ApiType.GET_HISTORY_EQUITY, params);
	}

	@Override
	protected void initViews() {
		setTitle(fundName);

		listView_historyEquity = (ListView) findViewById(R.id.listView_historyEquity_list);
		
		tv_unit = (TextView) findViewById(R.id.tv_history_unit);
		tv_total = (TextView) findViewById(R.id.tv_history_total);
		tv_benefit = (TextView) findViewById(R.id.tv_history_benefit);
		
		if ("True".equals(fundType)) {
			tv_unit.setText("万份收益");
			tv_total.setText("七日年化");
			tv_benefit.setText("费率");
			tv_benefit.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onViewClick(View v) {

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_HISTORY_EQUITY) {
			histories = JSON.parseArray(json, GetHistoryEquity.class);
			listView_historyEquity.setAdapter(new HistoryEquityAdapter(
					histories));
		}
		disMissDialog();
	}

	class HistoryEquityAdapter extends BaseAdapter {

		private List<GetHistoryEquity> list;

		public HistoryEquityAdapter(List<GetHistoryEquity> list) {
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
			convertView = LayoutInflater
					.from(HistoryEquityActivity.this)
					.inflate(R.layout.item_jjzsfragment_unitequity, null, false);
			TextView tv_data_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_data);
			TextView tv_dayBenefit_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_dayBenefit);
			TextView tv_unitEquity_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_unitEquity);
			TextView tv_totalEquity_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_totalEquity);
			
			if ("True".equals(fundType)) {
				tv_dayBenefit_item.setVisibility(View.GONE);
			}

			GetHistoryEquity equity = list.get(position);
			tv_data_item.setText(equity.getDealDate());
			tv_unitEquity_item.setText(equity.getUnitEquity());
			tv_totalEquity_item.setText(equity.getTotalEquity());
			if ("True".equals(fundType)) {
				tv_totalEquity_item.setText(equity.getTotalEquity()+"%");
			}
			tv_dayBenefit_item.setText(equity.getDayBenefit() + "%");
			if (equity.getDayBenefit().substring(0, 1).equals("-")) {
				tv_dayBenefit_item.setTextColor(Color.rgb(1, 153, 1));
			} else {
				tv_dayBenefit_item.setTextColor(Color.RED);
			}

			return convertView;
		}

	}
}
