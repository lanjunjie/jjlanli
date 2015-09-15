package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MySelectFund;
import com.myfp.myfund.ui.view.FixColumnListView;
import com.myfp.myfund.ui.view.ListItemHorizontalScrollView;

/**
 * 基金自选Fragment
 * 
 * @author pengchong.jia
 * 
 */
public class FundSelectFragment extends BaseFragment {

	List<MySelectFund> mList = new ArrayList<MySelectFund>();
	FixColumnListView mOptionalScrollListView;
	ListItemHorizontalScrollView mHeaderView;
	private int index, _index;
	private FundSelectActivity activity;
	private String userName;
	private List<MySelectFund> funds;
	private LinearLayout layout_total, layout_week, layout_month, layout_season,
			layout_halfYear, layout_year, layout_toyear;

	private TextView tv_title_unit, tv_title_total, tv_title_day,
			tv_title_month, tv_title_week, tv_title_season, tv_title_halfYear,
			tv_title_year, tv_title_toyear;

	private ImageView img_title_unit, img_title_total, img_title_day,
			img_title_month, img_title_week, img_title_season,
			img_title_halfYear, img_title_year, img_title_toyear;

	public static FundSelectFragment instantiation(int position) {
		FundSelectFragment fragment = new FundSelectFragment();
		Bundle args = new Bundle();
		args.putInt("index", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (FundSelectActivity) getActivity();
		activity.showProgressDialog("正在加载");
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
		userName = sPreferences.getString("UserName", null);
	//	activity.getSharedPreferences("Setting", activity.MODE_PRIVATE);
		RequestParams params = new RequestParams(getActivity());
		switch (index) {
		case 0:
			_index = 0;
			break;
		case 1:
			_index = 7;
			break;
		case 2:
			_index = 13;
			break;
		default:
			break;
		}
		params.put("type", _index);
		params.put("UserName", userName);
		activity.execApi(ApiType.GET_FUND_OPTIONAL, params, this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_query_fund, null, false);

		layout_total = (LinearLayout) view.findViewById(R.id.layout_title_total);
		layout_week = (LinearLayout) view.findViewById(R.id.layout_title_week);
		layout_month = (LinearLayout) view
				.findViewById(R.id.layout_title_month);
		layout_season = (LinearLayout) view
				.findViewById(R.id.layout_title_season);
		layout_halfYear = (LinearLayout) view
				.findViewById(R.id.layout_title_halfYear);
		layout_year = (LinearLayout) view.findViewById(R.id.layout_title_year);
		layout_toyear = (LinearLayout) view
				.findViewById(R.id.layout_title_toyear);

		img_title_day = (ImageView) view.findViewById(R.id.img_query_title_day);
		img_title_week = (ImageView) view
				.findViewById(R.id.img_query_title_week);
		img_title_month = (ImageView) view
				.findViewById(R.id.img_query_title_month);
		img_title_season = (ImageView) view
				.findViewById(R.id.img_query_title_season);
		img_title_halfYear = (ImageView) view
				.findViewById(R.id.img_query_title_halfYear);
		img_title_year = (ImageView) view
				.findViewById(R.id.img_query_title_year);
		img_title_toyear = (ImageView) view
				.findViewById(R.id.img_query_title_toyear);

		img_title_unit = (ImageView) view
				.findViewById(R.id.img_query_title_unit);
		img_title_total = (ImageView) view
				.findViewById(R.id.img_query_title_total);

		tv_title_day = (TextView) view.findViewById(R.id.tv_query_title_day);
		tv_title_week = (TextView) view.findViewById(R.id.tv_query_title_week);
		tv_title_month = (TextView) view
				.findViewById(R.id.tv_query_title_month);
		tv_title_season = (TextView) view
				.findViewById(R.id.tv_query_title_season);
		tv_title_halfYear = (TextView) view
				.findViewById(R.id.tv_query_title_halfYear);
		tv_title_year = (TextView) view.findViewById(R.id.tv_query_title_year);
		tv_title_toyear = (TextView) view
				.findViewById(R.id.tv_query_title_toyear);

		tv_title_unit = (TextView) view.findViewById(R.id.tv_query_title_unit);
		tv_title_total = (TextView) view
				.findViewById(R.id.tv_query_title_total);
		if (_index == 7) {
			tv_title_unit.setText("万份收益");
			tv_title_day.setText("七日年化");

			layout_total.setVisibility(View.GONE);
			layout_week.setVisibility(View.GONE);
			layout_month.setVisibility(View.GONE);
			layout_season.setVisibility(View.GONE);
			layout_halfYear.setVisibility(View.GONE);
			layout_year.setVisibility(View.GONE);
			layout_toyear.setVisibility(View.GONE);
		}

		tv_title_unit.setOnClickListener(this);
		tv_title_total.setOnClickListener(this);
		tv_title_day.setOnClickListener(this);
		tv_title_week.setOnClickListener(this);
		tv_title_month.setOnClickListener(this);
		tv_title_season.setOnClickListener(this);
		tv_title_halfYear.setOnClickListener(this);
		tv_title_year.setOnClickListener(this);
		tv_title_toyear.setOnClickListener(this);

		mOptionalScrollListView = (FixColumnListView) view
				.findViewById(R.id.listView_fundSelect_list);
		mHeaderView = (ListItemHorizontalScrollView) view
				.findViewById(R.id.item_scroll_title);
		mHeaderView.setListView(mOptionalScrollListView);
		mOptionalScrollListView.addHeaderScrollView(mHeaderView);

		return view;
	}

	boolean boo_unit = false;
	boolean boo_total = false;
	boolean boo_day = false;
	boolean boo_week = false;
	boolean boo_month = false;
	boolean boo_season = false;
	boolean boo_halfYear = false;
	boolean boo_year = false;
	boolean boo_toyear = false;

	@Override
	protected void onViewClick(View v) {
		if (funds.size() == 0 || funds == null)
			return;
		switch (v.getId()) {
		case R.id.tv_query_title_unit:
			// 单位净值
			// activity.showToast("1");
			// if (img_title_unit.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.VISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_unit, SortType.UnitEquity);
			if (boo_unit) {
				boo_unit = false;
				img_title_unit.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_unit = true;
				img_title_unit.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_total:
			// 累计收益
			// activity.showToast("2");
			// if (img_title_total.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.VISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_total, SortType.TotalEquity);
			if (boo_total) {
				boo_total = false;
				img_title_total.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_total = true;
				img_title_total.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_day:
			// 日涨幅
			// activity.showToast("3");
			// if (img_title_day.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.VISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_day, SortType.DayBenefit);
			if (boo_day) {
				boo_day = false;
				img_title_day.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_day = true;
				img_title_day.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_week:
			// 周涨幅
			// activity.showToast("4");
			// if (img_title_week.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.VISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_week, SortType.OneWeekRedound);
			if (boo_week) {
				boo_week = false;
				img_title_week.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_week = true;
				img_title_week.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_month:
			// 月涨幅
			// activity.showToast("5");
			// if (img_title_month.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.VISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_month, SortType.OneMonthRedound);
			if (boo_month) {
				boo_month = false;
				img_title_month.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_month = true;
				img_title_month.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_season:
			// 季涨幅
			// activity.showToast("6");
			// if (img_title_season.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.VISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_season, SortType.ThreeMonthRedound);
			if (boo_season) {
				boo_season = false;
				img_title_season.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_season = true;
				img_title_season.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_halfYear:
			// 半年涨幅
			// activity.showToast("7");
			// if (img_title_halfYear.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.VISIBLE);
			// img_title_year.setVisibility(View.INVISIBLE);
			// }
			ListSort(funds, boo_halfYear, SortType.SixMonthRedound);
			if (boo_halfYear) {
				boo_halfYear = false;
				img_title_halfYear.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_halfYear = true;
				img_title_halfYear
						.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_year:
			// 年涨幅
			// activity.showToast("8");
			// if (img_title_year.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.VISIBLE);
			// }
			ListSort(funds, boo_year, SortType.OneyearRedound);
			if (boo_year) {
				boo_year = false;
				img_title_year.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_year = true;
				img_title_year.setBackgroundResource(R.drawable.arrows_down);
			}
			break;
		case R.id.tv_query_title_toyear:
			// 年涨幅
			// activity.showToast("8");
			// if (img_title_year.getVisibility() == View.INVISIBLE) {
			// img_title_unit.setVisibility(View.INVISIBLE);
			// img_title_total.setVisibility(View.INVISIBLE);
			// img_title_day.setVisibility(View.INVISIBLE);
			// img_title_week.setVisibility(View.INVISIBLE);
			// img_title_month.setVisibility(View.INVISIBLE);
			// img_title_season.setVisibility(View.INVISIBLE);
			// img_title_halfYear.setVisibility(View.INVISIBLE);
			// img_title_year.setVisibility(View.VISIBLE);
			// }
			ListSort(funds, boo_toyear, SortType.ThisYearRedound);
			if (boo_toyear) {
				boo_toyear = false;
				img_title_toyear.setBackgroundResource(R.drawable.arrows_up);
			} else {
				boo_toyear = true;
				img_title_toyear.setBackgroundResource(R.drawable.arrows_down);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		funds = JSON.parseArray(json, MySelectFund.class);
		mOptionalScrollListView.setList(funds, userName, activity, _index);
		mOptionalScrollListView.layoutListView();
		mOptionalScrollListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				MySelectFund fund = funds.get(position - 1);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("sessionId", activity.getIntent().getStringExtra("sessionId"));
				intent.putExtra("fundName", fund.getFundName().trim());
				intent.putExtra("fundCode", fund.getFundCode().trim());
				startActivity(intent);
			}
		});
		activity.disMissDialog();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ListSort(List userlist, boolean isSort, SortType type) {
		if (type == SortType.UnitEquity) {
			ComparatorUnitEquity comparator = new ComparatorUnitEquity();
			Collections.sort(userlist, comparator);
			// System.out.println(">>>>>>>>>>>>单位净值>>>>>>>>>>>>>");
			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.UnitEquity.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.UnitEquity.equals(uss.UnitEquity)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.TotalEquity) {
			ComparatorTotalEquity comparator = new ComparatorTotalEquity();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.TotalEquity.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.TotalEquity.equals(uss.TotalEquity)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.DayBenefit) {
			ComparatorDayBenefit comparator = new ComparatorDayBenefit();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.DayBenefit.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.DayBenefit.equals(uss.DayBenefit)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.OneWeekRedound) {
			ComparatorOneWeekRedound comparator = new ComparatorOneWeekRedound();
			Collections.sort(userlist, comparator);
			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.OneWeekRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.OneWeekRedound.equals(uss.OneWeekRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.OneMonthRedound) {
			ComparatorOneMonthRedound comparator = new ComparatorOneMonthRedound();
			Collections.sort(userlist, comparator);
			//
			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.OneMonthRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.OneMonthRedound.equals(uss.OneMonthRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.ThreeMonthRedound) {
			ComparatorThreeMonthRedound comparator = new ComparatorThreeMonthRedound();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.ThreeMonthRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.ThreeMonthRedound.equals(uss.ThreeMonthRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.SixMonthRedound) {
			ComparatorSixMonthRedound comparator = new ComparatorSixMonthRedound();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.SixMonthRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.SixMonthRedound.equals(uss.SixMonthRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.OneyearRedound) {
			ComparatorOneyearRedound comparator = new ComparatorOneyearRedound();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.OneyearRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.OneyearRedound.equals(uss.OneyearRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		} else if (type == SortType.ThisYearRedound) {
			ComparatorThisYearRedound comparator = new ComparatorThisYearRedound();
			Collections.sort(userlist, comparator);

			// List ulist = new ArrayList();
			// for (int i = 0; i < userlist.size(); i++) {
			// MySelectFund us = (MySelectFund) userlist.get(i);
			// if (us.ThisYearRedound.substring(0, 1).equals("-")) {
			// ulist.add(userlist.get(i));
			// }
			// }
			// for (int i = 0; i < ulist.size(); i++) {
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// for (int j = 0; j < userlist.size(); j++) {
			// MySelectFund uss = (MySelectFund) userlist.get(j);
			// if (us.ThisYearRedound.equals(uss.ThisYearRedound)) {
			// userlist.remove(j);
			// break;
			// }
			// }
			// }
			// //
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			// // if(isSort)//正序或者倒叙 集合变换
			// // Collections.reverse(ulist);
			// //
			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			// for (int i = 0; i < ulist.size(); i++) {
			// userlist.add(0, ulist.get(i));
			// MySelectFund us = (MySelectFund) ulist.get(i);
			// }
		}
		if (isSort) {
			Collections.reverse(userlist);
		}
		// for (int i = 0; i < tempList.size(); i++) {
		// Log.e("sort :", tempList.get(i).FundName);
		// }

		System.out.println(">>>>>>>排序>>>>>>>>>>" + funds);
		// mColumnScrollListView.setList(funds, userName, activity);
		// mColumnScrollListView.layoutListView();
		// mColumnScrollListView.onRefreshComplete();
		List<MySelectFund> tempList = new ArrayList<MySelectFund>();
		tempList.addAll(funds);

		mOptionalScrollListView.repData(tempList);
		mOptionalScrollListView.setSelection(0);
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

}
