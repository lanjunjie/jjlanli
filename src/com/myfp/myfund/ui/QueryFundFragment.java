package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MySelectFund;

import com.myfp.myfund.ui.view.FixColumnListViewTwo;
import com.myfp.myfund.ui.view.ListItemHorizontalScrollView;
import com.myfp.myfund.ui.view.FixColumnListViewTwo.OnLoadMoreListener;
import com.myfp.myfund.ui.view.FixColumnListViewTwo.OnRefreshListener;

/**
 * 基金查询Fragment
 * 
 * @author pengchong.jia
 * 
 */
public class QueryFundFragment extends BaseFragment implements
		OnDataReceivedListener2 {

	List<MySelectFund> mList = new ArrayList<MySelectFund>();
	FixColumnListViewTwo mColumnScrollListView;
	ListItemHorizontalScrollView mHeaderView;
	private int index;
	private QueryFundActivity activity;
	private View view;
	private List<MySelectFund> funds, tempList;
	private String userName;
	private TextView tv_title_unit, tv_title_total, tv_title_day,
			tv_title_month, tv_title_week, tv_title_season, tv_title_halfYear,
			tv_title_year,tv_title_toyear,zixuan;
	private ImageView img_title_unit, img_title_total, img_title_day,
			img_title_month, img_title_week, img_title_season,
			img_title_halfYear, img_title_year,img_title_toyear;

	private LinearLayout layout_total, layout_week, layout_month, layout_season,
			layout_halfYear, layout_year,layout_toyear;
	private ImageView istrue;
	private int pageNum = 1;

	public static QueryFundFragment instantiation(int position) {
		QueryFundFragment fragment = new QueryFundFragment();
		Bundle args = new Bundle();
		args.putInt("index", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (QueryFundActivity) getActivity();
		activity.showProgressDialog("正在加载");
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		userName = App.getContext().getUserName();
		funds = new ArrayList<MySelectFund>();

		RequestParams params = new RequestParams(getActivity());
		params.put("UserName", userName);
		params.put("fundtype", index);
		params.put("pagenum", 1);
		activity.execApi(ApiType.GET_FUNDLISTTWO, params, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_query_fundtwo, null, false);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}


		layout_total = (LinearLayout) view.findViewById(R.id.layout_title_total);
		layout_week = (LinearLayout) view.findViewById(R.id.layout_title_week);
		layout_month = (LinearLayout) view
				.findViewById(R.id.layout_title_month);
		layout_season = (LinearLayout) view
				.findViewById(R.id.layout_title_season);
		layout_halfYear = (LinearLayout) view
				.findViewById(R.id.layout_title_halfYear);
		layout_year = (LinearLayout) view.findViewById(R.id.layout_title_year);
		layout_toyear = (LinearLayout) view.findViewById(R.id.layout_title_toyear);

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
		tv_title_toyear = (TextView) view.findViewById(R.id.tv_query_title_toyear);
              
		tv_title_unit = (TextView) view.findViewById(R.id.tv_query_title_unit);
		tv_title_total = (TextView) view
				.findViewById(R.id.tv_query_title_total);
		if (index == 7) {
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

		mColumnScrollListView = (FixColumnListViewTwo) view
				.findViewById(R.id.listView_fundSelect_list1);
	
		mHeaderView = (ListItemHorizontalScrollView) view
				.findViewById(R.id.item_scroll_title1);
		mHeaderView.setListView(mColumnScrollListView);
		mColumnScrollListView.addHeaderScrollView(mHeaderView);

		mColumnScrollListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				pageNum = 1;
				RequestParams params = new RequestParams(getActivity());
				params.put("UserName", App.getContext().getUserName());
				params.put("fundtype", index);
				params.put("pagenum", pageNum);
				activity.execApi(ApiType.GET_FUNDLISTTWO, params,
						QueryFundFragment.this);
			}
		});

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
			// 今年涨幅
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
	public void onResume() {
		super.onResume();
		/*
		String uName = App.getContext().getUserName();
		if (!TextUtils.isEmpty(uName)) {
			userName = uName;
			RequestParams params = new RequestParams(getActivity());
			params.put("UserName", userName);
			params.put("fundtype", index);
			params.put("pagenum", 1);
			activity.execApi(ApiType.GET_FUNDLIST, params, this);
			
		}*/
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onReceiveData(ApiType api, RequestParams params, String json) {
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_FUNDLISTTWO) {
			tempList = JSON.parseArray(json, MySelectFund.class);
			
			if (params.get("pagenum").equals("1")) {
				mColumnScrollListView.setList(tempList, userName, activity,
						index);
				mColumnScrollListView.layoutListView();
				mColumnScrollListView.onRefreshComplete();
				funds.addAll(tempList);
			} else {

				funds.addAll(tempList);
				mColumnScrollListView.add2Footer(tempList);
				// mColumnScrollListView.layoutListView();
				// mColumnScrollListView.setSelection((pageNum - 1) * 15 - 5);
				mColumnScrollListView.onLoadMoreComplete();
			}
			mColumnScrollListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							if (position > 0) {
								MySelectFund fund = funds.get(position - 1);
								Intent intent = new Intent(getActivity(),
										DetailActivity.class);
								intent.putExtra("fundName", fund.getFundName()
										.trim());
								intent.putExtra("fundCode", fund.getFundCode()
										.trim());
								startActivity(intent);
							}
						}
					});
			mColumnScrollListView.setOnLoadListener(new OnLoadMoreListener() {

				@Override
				public void onLoadMore() {
					// TODO 加载更多
					pageNum++;
					RequestParams more = new RequestParams(getActivity());
					more.put("UserName", App.getContext().getUserName());
					more.put("fundtype", index);
					more.put("pagenum", pageNum);
					activity.execApi(ApiType.GET_FUNDLISTTWO, more,
							QueryFundFragment.this);
				}
			});
			activity.disMissDialog();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void ListSort(List userlist, boolean isSort, SortType type) {
		if (type == SortType.UnitEquity) {
			ComparatorUnitEquity comparator = new ComparatorUnitEquity();
			Collections.sort(userlist, comparator);
			//
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
			//
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

		mColumnScrollListView.repData(funds);
		mColumnScrollListView.setSelection(0);
	}
}

class ComparatorUnitEquity implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.UnitEquity.compareTo(MySelectFund1.UnitEquity);
		return flag;
	}
}

class ComparatorTotalEquity implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.TotalEquity
				.compareTo(MySelectFund1.TotalEquity);
		return flag;
	}
}

class ComparatorDayBenefit implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.DayBenefit.compareTo(MySelectFund1.DayBenefit);
		return flag;
	}
}

class ComparatorOneWeekRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.OneWeekRedound
				.compareTo(MySelectFund1.OneWeekRedound);
		return flag;
	}
}

class ComparatorOneMonthRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.OneMonthRedound
				.compareTo(MySelectFund1.OneMonthRedound);
		return flag;
	}
}

class ComparatorThreeMonthRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.ThreeMonthRedound
				.compareTo(MySelectFund1.ThreeMonthRedound);
		return flag;
	}
}

class ComparatorSixMonthRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.SixMonthRedound
				.compareTo(MySelectFund1.SixMonthRedound);
		return flag;
	}
}

class ComparatorOneyearRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.OneyearRedound
				.compareTo(MySelectFund1.OneyearRedound);
		return flag;
	}
}

class ComparatorThisYearRedound implements Comparator {
	public int compare(Object arg0, Object arg1) {
		MySelectFund MySelectFund0 = (MySelectFund) arg0;
		MySelectFund MySelectFund1 = (MySelectFund) arg1;
		int flag = MySelectFund0.ThisYearRedound
				.compareTo(MySelectFund1.ThisYearRedound);
		return flag;
	}
}

enum SortType {
	UnitEquity, TotalEquity, DayBenefit, DealDate, OneWeekRedound, OneMonthRedound, ThreeMonthRedound, SixMonthRedound, OneyearRedound, ThisYearRedound,
}
