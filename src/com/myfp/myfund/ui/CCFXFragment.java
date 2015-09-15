package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.myfp.myfund.api.beans.AssetAlloction;
import com.myfp.myfund.api.beans.BondTop;
import com.myfp.myfund.api.beans.StockTop;
import com.myfp.myfund.ui.view.CustomProgressView;

/**
 * 持仓分析Fragment
 * 
 * @author pengchongjia
 * 
 */
public class CCFXFragment extends BaseFragment {

	private CustomProgressView cpview;
	private DetailActivity activity;
	private String fundCode;
	private TextView tv_stock_totalValue, tv_stock_totalRatio,
			tv_bond_totalValue, tv_bond_totalRatio, tv_time, tv_value_stock,
			tv_value_currency, tv_value_bond, tv_value_other;
	private ListView lv_stock, lv_bond;
	private List<AssetAlloction> assets;
	private List<StockTop> stocks;
	private List<BondTop> bonds;
	private List<Map<String, String>> top10 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> top5 = new ArrayList<Map<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (DetailActivity) getActivity();
		activity.showProgressDialog("正在加载");
		fundCode = activity.getFundCode();

		RequestParams params = new RequestParams(activity);
		params.put("InputFundValue", fundCode);
		activity.execApi(ApiType.GET_ASSET_ALLOCTION, params, this);
		activity.execApi(ApiType.GET_STOCK_TOP10, params, this);
		activity.execApi(ApiType.GET_BOND_TOP5, params, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_detail_ccfx, null, false);

		cpview = (CustomProgressView) view.findViewById(R.id.cpview);
		tv_time = (TextView) view.findViewById(R.id.tv_proportionDate);
		tv_stock_totalValue = (TextView) view
				.findViewById(R.id.tv_stock_totalMarketValue);
		tv_stock_totalRatio = (TextView) view
				.findViewById(R.id.tv_stock_totalRatio);
		tv_bond_totalValue = (TextView) view
				.findViewById(R.id.tv_bond_totalMarketValue);
		tv_bond_totalRatio = (TextView) view
				.findViewById(R.id.tv_bond_totalRatio);

		tv_value_stock = (TextView) view.findViewById(R.id.tv_value_stock);
		tv_value_currency = (TextView) view
				.findViewById(R.id.tv_value_currency);
		tv_value_bond = (TextView) view.findViewById(R.id.tv_value_bond);
		tv_value_other = (TextView) view.findViewById(R.id.tv_value_other);

		lv_bond = (ListView) view.findViewById(R.id.lv_bond_top5);
		lv_stock = (ListView) view.findViewById(R.id.lv_stock_top10);

		return view;
	}

	@Override
	protected void onViewClick(View v) {

	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToastLong("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_ASSET_ALLOCTION) {
			assets = JSON.parseArray(json, AssetAlloction.class);
			if (assets.size()==0) {
				tv_time.setText("--");
				tv_value_stock.setText("--");
				tv_value_currency.setText("--");
				tv_value_bond.setText("--");
				tv_value_other.setText("--");
			}else {
				System.out.println("assets.get(0)--------------->"+assets.get(0).getProportionDate());
				tv_time.setText(assets.get(0).getProportionDate());
				tv_value_stock.setText(assets.get(0).getProportion()+"%");
				tv_value_currency.setText(assets.get(2).getProportion()+"%");
				tv_value_bond.setText(assets.get(1).getProportion()+"%");
				tv_value_other.setText(assets.get(3).getProportion()+"%");
				cpview.setProportion(
						(int) (Float.parseFloat(assets.get(0).getProportion()) * 100),
						(int) (Float.parseFloat(assets.get(2).getProportion()) * 100),
						(int) (Float.parseFloat(assets.get(1).getProportion()) * 100),
						(int) (Float.parseFloat(assets.get(3).getProportion()) * 100));
			}
			
		} else if (api == ApiType.GET_STOCK_TOP10 && !activity.isFinishing()) {
			stocks = JSON.parseArray(json, StockTop.class);
			System.out.println("stocks-=-=-=>"+stocks);
			if (stocks.size()==0) {
				tv_stock_totalValue.setText("--");
				tv_stock_totalRatio.setText("--");
			}else {
				for (int i = 0; i < stocks.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", stocks.get(i).getSholding2());
					map.put("code", stocks.get(i).getESymbol());
					map.put("value", stocks.get(i).getSholding6());
					map.put("ratio", stocks.get(i).getSholding7() + "%");
					top10.add(map);
				}
				
			}
			lv_stock.setAdapter(new TopAdapter(top10));
			setListViewHeightBasedOnChildren(lv_stock);
			if (stocks.equals("")) {
				tv_stock_totalValue.setText("--");
				tv_stock_totalRatio.setText("--");
			}else {
				tv_stock_totalValue.setText(stocks.get(stocks.size() - 1)
						.getSholding6());
				tv_stock_totalRatio.setText(stocks.get(stocks.size() - 1)
						.getSholding7() + "%");
			}
		} else if (api == ApiType.GET_BOND_TOP5 && !activity.isFinishing()) {
			bonds = JSON.parseArray(json, BondTop.class);
			System.out.println("bonds=-=-=-=>"+bonds);
			if (bonds.size()==0) {
				tv_bond_totalValue.setText("--");
				tv_bond_totalRatio.setText("--");
			}else {
				
				for (int i = 0; i < bonds.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", bonds.get(i).getBSholding1());
					map.put("code", bonds.get(i).getBSymbol());
					map.put("value", bonds.get(i).getBSholding2());
					map.put("ratio", bonds.get(i).getBSholding4() + "%");
					top5.add(map);
					
				}
			}
			lv_bond.setAdapter(new TopAdapter(top5));
			setListViewHeightBasedOnChildren(lv_bond);
			if (bonds.equals("")) {
				tv_bond_totalValue.setText("--");
				tv_bond_totalRatio.setText("--");
			}else {
				tv_bond_totalValue.setText(bonds.get(bonds.size() - 1)
						.getBSholding2());
				tv_bond_totalRatio.setText(bonds.get(bonds.size() - 1)
						.getBSholding4() + "%");
			}
		}
		activity.disMissDialog();
	}

	class TopAdapter extends BaseAdapter {

		private List<Map<String, String>> list;

		public TopAdapter(List<Map<String, String>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			System.out.println(list.size() + "............");
			return list.size() - 1;
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
			convertView = View.inflate(getActivity(),
					R.layout.item_stock_top10_list, null);
			Map<String, String> map = list.get(position);
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_ccfx_fundName);
			TextView tv_code = (TextView) convertView
					.findViewById(R.id.tv_ccfx_fundCode);
			TextView tv_value = (TextView) convertView
					.findViewById(R.id.tv_ccfx_value);
			TextView tv_ratio = (TextView) convertView
					.findViewById(R.id.tv_ccfx_percent);
			if (map.equals("")) {
				tv_name.setText("--");
			}else {
				tv_name.setText(map.get("name"));
			}
			if (map.equals("")) {
				tv_code.setText("--");
			}else {
				tv_code.setText(map.get("code"));
			}
			if (map.equals("")) {
				tv_value.setText("--");
			}else {
				tv_value.setText(map.get("value"));
			}
			if (map.equals("")) {
				tv_ratio.setText("--");
			}else {
				tv_ratio.setText(map.get("ratio")); 
			}

			return convertView;
		}

	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		TopAdapter listAdapter = (TopAdapter) listView.getAdapter();
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
}
