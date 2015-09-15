package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.FundProfile;

/**
 * 基金概况
 * 
 * @author Max.Zhao
 * 
 */
public class JJGKFragment extends BaseFragment implements
		OnDataReceivedListener2 {

	private DetailActivity activity;
	private String fundCode;
	private TextView tv_fundName, tv_fundCode, tv_createTime, tv_fundType,
			tv_fundScope, tv_quarterEquity, tv_mgInfo,tv_manager,tv_supervise,tv_trustor;
	private List<FundProfile> profiles;
	private RelativeLayout layout_type1, layout_type2, layout_type3,
			layout_type4, layout_type5;
	private TextView tv_type6, tv_type7, tv_type8, tv_type9;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (DetailActivity) getActivity();
		activity.showProgressDialog("正在加载");
		fundCode = activity.getFundCode();
		RequestParams params = new RequestParams(activity);
		params.put("InputFundValue", fundCode);
		activity.execApi(ApiType.GET_FUND_PROFILE, params, this);

		for (int i = 1; i < 10; i++) {
			RequestParams rate = new RequestParams(activity);
			rate.put("fundcode", fundCode);
			rate.put("type", i);
			activity.execApi(ApiType.GET_FUND_RATE, rate, this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_detail_jjgk, null, false);
		tv_fundName = (TextView) view
				.findViewById(R.id.textView_detailFragment_fundName);
		tv_fundCode = (TextView) view
				.findViewById(R.id.textView_detailFragment_fundCode);
		tv_createTime = (TextView) view
				.findViewById(R.id.textView_detailFragment_createTime);
		tv_fundType = (TextView) view
				.findViewById(R.id.textView_detailFragment_type);
		tv_fundScope = (TextView) view
				.findViewById(R.id.textView_detailFragment_totalShare);
		tv_quarterEquity = (TextView) view
				.findViewById(R.id.textView_detailFragment_totalAsset);
		tv_mgInfo = (TextView) view
				.findViewById(R.id.textView_detailFragment_mgIntro);
		tv_manager = (TextView) view
				.findViewById(R.id.textView_detailFragment_manager);
		tv_supervise = (TextView) view
				.findViewById(R.id.textView_detailFragment_supervise);
		tv_trustor = (TextView) view
				.findViewById(R.id.textView_detailFragment_trustor);

		layout_type1 = (RelativeLayout) view
				.findViewById(R.id.layout_detail_rate_type1);
		layout_type2 = (RelativeLayout) view
				.findViewById(R.id.layout_detail_rate_type2);
		layout_type3 = (RelativeLayout) view
				.findViewById(R.id.layout_detail_rate_type3);
		layout_type4 = (RelativeLayout) view
				.findViewById(R.id.layout_detail_rate_type4);
		layout_type5 = (RelativeLayout) view
				.findViewById(R.id.layout_detail_rate_type5);

		tv_type6 = (TextView) view.findViewById(R.id.tv_detail_rate_type6);
		tv_type7 = (TextView) view.findViewById(R.id.tv_detail_rate_type7);
		tv_type8 = (TextView) view.findViewById(R.id.tv_detail_rate_type8);
		tv_type9 = (TextView) view.findViewById(R.id.tv_detail_rate_type9);

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
	public void onReceiveData(ApiType api, RequestParams params, String json) {

		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_FUND_PROFILE) {
			profiles = JSON.parseArray(json, FundProfile.class);
			tv_fundName.setText(profiles.get(0).getFundName());
			tv_fundCode.setText(profiles.get(0).getFundCode());
			tv_createTime.setText(profiles.get(0).getStartDate());
			tv_fundType.setText(profiles.get(0).getFundType());
			tv_fundScope.setText(profiles.get(0).getFundScope()+"亿份");
			tv_quarterEquity.setText(profiles.get(0).getQuarterEquity()+"亿元");
			tv_mgInfo.setText(profiles.get(0).getFundMG12());
			tv_manager.setText(profiles.get(0).getFundManager());
			tv_supervise.setText(profiles.get(0).getFundSupervise());
			tv_trustor.setText(profiles.get(0).getFundTrustor());
		} else if (api == ApiType.GET_FUND_RATE) {
			try {
				JSONArray array = new JSONArray(json);
				if (params.get("type").equals("1")) {
					if (array.length() == 0) {
						layout_type1.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < 4; i++) {
							JSONObject obj = array.getJSONObject(i);
							LinearLayout layout_range = (LinearLayout) layout_type1
									.getChildAt(0);
							LinearLayout layout_limit = (LinearLayout) layout_type1
									.getChildAt(1);
							TextView tv_range = (TextView) layout_range
									.getChildAt(i);
							TextView tv_limit = (TextView) layout_limit
									.getChildAt(i);

							tv_range.setText(obj.getString("Chgrf9"));
							tv_limit.setText(obj.getString("Chgrf7").substring(
									2));
						}
					}
				}
				if (params.get("type").equals("2")) {
					if (array.length() == 0) {
						layout_type2.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < 4; i++) {
							JSONObject obj = array.getJSONObject(i);
							LinearLayout layout_range = (LinearLayout) layout_type2
									.getChildAt(0);
							LinearLayout layout_limit = (LinearLayout) layout_type2
									.getChildAt(1);
							TextView tv_range = (TextView) layout_range
									.getChildAt(i);
							TextView tv_limit = (TextView) layout_limit
									.getChildAt(i);

							tv_range.setText(obj.getString("Chgrf13"));
							tv_limit.setText(obj.getString("Chgrf7").substring(
									2));
						}
					}
				}
				if (params.get("type").equals("3")) {
					if (array.length() == 0) {
						layout_type3.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < 4; i++) {
							JSONObject obj = array.getJSONObject(i);
							LinearLayout layout_range = (LinearLayout) layout_type3
									.getChildAt(0);
							LinearLayout layout_limit = (LinearLayout) layout_type3
									.getChildAt(1);
							TextView tv_range = (TextView) layout_range
									.getChildAt(i);
							TextView tv_limit = (TextView) layout_limit
									.getChildAt(i);

							tv_range.setText(obj.getString("Chgrf9"));
							tv_limit.setText(obj.getString("Chgrf7").substring(
									2));
						}
					}
				}
				if (params.get("type").equals("4")) {
					if (array.length() == 0) {
						layout_type4.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < 4; i++) {
							JSONObject obj = array.getJSONObject(i);
							LinearLayout layout_range = (LinearLayout) layout_type4
									.getChildAt(0);
							LinearLayout layout_limit = (LinearLayout) layout_type4
									.getChildAt(1);
							TextView tv_range = (TextView) layout_range
									.getChildAt(i);
							TextView tv_limit = (TextView) layout_limit
									.getChildAt(i);

							tv_range.setText(obj.getString("Chgrf13"));
							tv_limit.setText(obj.getString("Chgrf7").substring(
									2));
						}
					}
				}
				if (params.get("type").equals("5")) {
					if (array.length() == 0) {
						layout_type5.setVisibility(View.GONE);
					} else {
						for (int i = 0; i < 2; i++) {
							JSONObject obj = array.getJSONObject(i);
							LinearLayout layout_range = (LinearLayout) layout_type5
									.getChildAt(0);
							LinearLayout layout_limit = (LinearLayout) layout_type5
									.getChildAt(1);
							TextView tv_range = (TextView) layout_range
									.getChildAt(i);
							TextView tv_limit = (TextView) layout_limit
									.getChildAt(i);

							tv_range.setText(obj.getString("Chgrf13"));
							tv_limit.setText(obj.getString("Chgrf7").substring(
									2));
						}
					}
				}
				if (params.get("type").equals("6")) {

					JSONObject obj = array.getJSONObject(0);
					tv_type6.setText(obj.getString("Chgrf7").substring(2));

				}
				if (params.get("type").equals("7")) {

					JSONObject obj = array.getJSONObject(0);
					tv_type7.setText(obj.getString("Chgrf7").substring(2));

				}
				if (params.get("type").equals("8")) {

					JSONObject obj = array.getJSONObject(0);
					tv_type8.setText(obj.getString("Chgrf7").substring(2));

				}
				if (params.get("type").equals("9")) {

					JSONObject obj = array.getJSONObject(0);
					tv_type9.setText(obj.getString("Chgrf7").substring(2));

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		activity.disMissDialog();
	}
}
