package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cn.jpush.android.util.ac;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.Get5unitEquity;
import com.myfp.myfund.api.beans.GetFundDetailInfo;
import com.myfp.myfund.api.beans.GetTotalChar;
import com.myfp.myfund.api.beans.GetUnitChar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 基金走势
 * 
 * @author Max.Zhao
 * 
 */
public class JJZSFragment extends BaseFragment {

	private static final String TAG = "JJZSFragment";
	private TextView[] arr_types;
	private TextView[] arr_times;
	private int fundDetailInfo = 1; // 判断是单位净值还是累计收益
	private int timeType = 1; // 月、季、半年、年
	private TextView tv_fundType, tv_unitEquity, tv_dealData, tv_totalEquity,
			tv_dayBenefit, tv_more, tv_title_benefit, tv_title_unit,
			tv_title_total,tv_list_benefit, tv_list_unit,
			tv_list_total,tv_chart_unit,tv_chart_total,tv_chartTitle_unit,tv_chartTitle_total,tv_chartData_unit,tv_chartData_total,tv_chartData_date;
	private Button bt_buy, bt_comment;
	private ListView listView_5unitEquity;
	private DetailActivity activity;
	private List<GetFundDetailInfo> fundDetailInfos;
	private List<Get5unitEquity> fiveUnitEquities;
	private List<GetUnitChar> unitCharList;
	private List<GetTotalChar> totalCharList;
	private String fundCode, fundName;
	private RelativeLayout char_layout;
	private LineChart xychart;
	private GraphicalView graph;
	private LinearLayout l;
	private TextView childText;
	private int mEventEndX;
	private int mEventEndY;
	private int mEventStartX;
	private int mEventStartY;
	private long[] dx;
	private double[] tdy,udy;
	private double[] datax;
	private String[] x;
	private String fundType;
	private String isHuoBi;
	private ScrollView mScrollView;
	private String encodePassWord, idCard;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (DetailActivity) getActivity();
		fundCode = activity.getFundCode();
		fundName = activity.getFundName();
		sessionId =App.getContext().getSessionid();
		mobile = App.getContext().getMobile();
		activity.showProgressDialog("正在加载");
		//得到详情页传过来的参数fundcode，再调用详情页接口
		RequestParams params = new RequestParams(activity);
		params.put("InputFundValue", fundCode);
		activity.execApi(ApiType.GET_FUND_DETAIL_INFO, params, this);
		//得到详情页传过来的参数fundcode和count，再调用近五日净值接口
		RequestParams history_5 = new RequestParams(activity);
		history_5.put("InputFundValue", fundCode);
		history_5.put("count", 10);
		activity.execApi(ApiType.GET_FIVE_UNIT_EQUITY, history_5, this);

		//
		refreshChar(fundDetailInfo, timeType);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_detail_jjzs, null, false);
		mScrollView=(ScrollView)view.findViewById(R.id.scrollView1);
		tv_fundType = (TextView) view
				.findViewById(R.id.textView_jjzsFragment_type);
		tv_unitEquity = (TextView) view
				.findViewById(R.id.textView_jjzsFragment_unitEquity);
		tv_dealData = (TextView) view
				.findViewById(R.id.textView_jjzsFragment_dealData);
		tv_totalEquity = (TextView) view
				.findViewById(R.id.textView_jjzsFragment_totalEquity);
		tv_dayBenefit = (TextView) view
				.findViewById(R.id.textView_jjzsFragment_dayBenefit);
		bt_buy = (Button) view.findViewById(R.id.button_jjzsFragment_buy);
		bt_comment = (Button) view
				.findViewById(R.id.button_jjzsFragment_comment);
		tv_title_benefit = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_title_benefit);
		tv_title_unit = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_title_unit);
		tv_title_total = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_title_total);
		
		tv_chart_unit = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_chart_unit);
		tv_chart_total = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_chart_total);
		
		tv_list_benefit = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_history_dayBenefit);
		tv_list_unit = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_history_unit);
		tv_list_total = (TextView) view
				.findViewById(R.id.tv_jjzsFragment_history_total);
		
		tv_chartTitle_unit = (TextView) view
				.findViewById(R.id.tv_chart_title_unit);
		tv_chartTitle_total = (TextView) view
				.findViewById(R.id.tv_chart_title_total);
		
		tv_chartData_unit = (TextView) view
				.findViewById(R.id.tv_chart_unit);
		tv_chartData_total = (TextView) view
				.findViewById(R.id.tv_chart_total);
		tv_chartData_date = (TextView) view
				.findViewById(R.id.tv_chart_date);

		if (fundDetailInfo == 1) {
			tv_chartTitle_total.setVisibility(View.GONE);
			tv_chartData_total.setVisibility(View.GONE);
		}
		
		tv_more = (TextView) view.findViewById(R.id.tv_jjzsFragment_more);
		bt_buy.setOnClickListener(this);
		bt_comment.setOnClickListener(this);
		tv_more.setOnClickListener(this);
		listView_5unitEquity = (ListView) view
				.findViewById(R.id.listView_jjzsFragment_5UnitEquity_list);

		char_layout = (RelativeLayout) view.findViewById(R.id.char_layout);
		
		initTimeTab(view);

		initEquityTab(view);

		return view;
	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
	

	private void initEquityTab(View view) {
		LinearLayout layoutType = (LinearLayout) view
				.findViewById(R.id.layout_jjzsFragment_type);
		arr_types = new TextView[2];
		for (int i = 0; i < arr_types.length; i++) {
			TextView textView_type = (TextView) layoutType.getChildAt(i);
			arr_types[i] = textView_type;
			arr_types[i].setEnabled(true);
			arr_types[i].setTag(i);
			arr_types[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					dx = null;
					tdy = null;
					udy = null;
					datax = null;
					x = null;
					
					fundDetailInfo = (Integer) v.getTag() + 1;
					for (int j = 0; j < arr_types.length; j++) {
						arr_types[j].setEnabled(true);
					}
					
					if (fundDetailInfo == 1) {
						tv_chartTitle_unit.setVisibility(View.VISIBLE);
						tv_chartData_unit.setVisibility(View.VISIBLE);
						tv_chartTitle_unit.setText("单位净值");
						tv_chartTitle_total.setVisibility(View.GONE);
						tv_chartData_total.setVisibility(View.GONE);
					}else if(fundDetailInfo == 2){
						if ("True".equals(isHuoBi)) {
							tv_chartTitle_unit.setVisibility(View.GONE);
							tv_chartData_unit.setVisibility(View.GONE);
						}
						
						tv_chartTitle_unit.setText("上证指数:");
						tv_chartTitle_total.setVisibility(View.VISIBLE);
						tv_chartData_total.setVisibility(View.VISIBLE);
					}
					
					arr_types[(Integer) v.getTag()].setEnabled(false);
					timeType = 1;
					for (int j = 0; j < arr_times.length; j++) {
						arr_times[j].setEnabled(true);
					}
					arr_times[0].setEnabled(false);

					refreshChar(fundDetailInfo, timeType);
				}
			});
			arr_types[0].setEnabled(false);

		}
	}
//加载月，季，半年，一年标签
	private void initTimeTab(View view) {
		LinearLayout layoutTime = (LinearLayout) view
				.findViewById(R.id.layout_jjzsFragment_time);
		arr_times = new TextView[4];
		for (int i = 0; i < arr_times.length; i++) {
			TextView textView_time = (TextView) layoutTime.getChildAt(i * 2);
			arr_times[i] = textView_time;
			arr_times[i].setEnabled(true);
			arr_times[i].setTag(i);
			arr_times[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					dx = null;
					tdy = null;
					udy = null;
					datax = null;
					x = null;
					
					timeType = (Integer) v.getTag() + 1;
					for (int j = 0; j < arr_times.length; j++) {
						arr_times[j].setEnabled(true);
					}
					arr_times[(Integer) v.getTag()].setEnabled(false);

					refreshChar(fundDetailInfo, timeType);
				}
			});
			arr_times[0].setEnabled(false);
		}
	}
//刷新净值
	private void refreshChar(int fundDetailInfo, int timeType) {
		RequestParams params_char = new RequestParams(activity);
		params_char.put("inputFundValue", fundCode);
		params_char.put("UnityDate", timeType);
		if (fundDetailInfo == 1) {
			activity.execApi(ApiType.GET_UNIT_CHAR, params_char,
					JJZSFragment.this);
		} else if (fundDetailInfo == 2) {
			activity.execApi(ApiType.GET_TOTAL_CHAR, params_char,
					JJZSFragment.this);
		}
		activity.showProgressDialog("正在加载");
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.button_jjzsFragment_buy:
			// 购买
			if ( App.getContext().getUserName()==null) {
				Intent intent = new Intent(activity,
						MyMeansActivity.class);
				
				intent.putExtra("gms", "true");
				startActivity(intent);
				return;
			}else{
			
			RequestParams pms=new RequestParams(activity);
			pms.put("mobileno", mobile);
			activity.execApi(ApiType.GET_FINDMOBILENO, pms,new OnDataReceivedListener() {
				
				@Override
				public void onReceiveData(ApiType api, String json) {
					if (json != null && !json.equals("")) {
						tInputStringStream = new ByteArrayInputStream(json.getBytes());
						XmlPullParser parser = Xml.newPullParser();
						try {
							parser.setInput(tInputStringStream, "UTF-8");
							int event = parser.getEventType();
							while (event != XmlPullParser.END_DOCUMENT) {
								Log.i("start_document", "start_document");
								switch (event) {
								case XmlPullParser.START_TAG:
									if ("return".equals(parser.getName())) {
										try {
											String xmlReturn = parser.nextText();
											try {
												JSONObject object=new JSONObject(xmlReturn);
												String msg = object.getString("msg");
												System.out.println("msg1------------->"+msg);
												if (msg.equals("0")) {
													
													Intent intent=new Intent(activity, ShortcutStatusActivity.class);
													intent.putExtra("Phone", mobile);
													System.out.println("Phone3次开户流程------->"+mobile);
													startActivity(intent);
													
												}else if (msg.equals("1")) {
													dealBuy();
												}
												
											} catch (Exception e) {
												e.printStackTrace();
											}
											
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									
									break;
									
								}
								try {
									event = parser.next();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						}
						
					}
					
				}
			});
			}
			
			
//			Intent intent = new Intent(activity, DealActivity.class);
//			intent.putExtra("FundCode", fundCode);
//			activity.startActivity(intent);
			break;
		case R.id.button_jjzsFragment_comment:
			// 评论
			Intent comment = new Intent(getActivity(),
					FundCommentListActivity.class);
			comment.putExtra("FundCode", fundCode);
			comment.putExtra("FundName", fundName);
			activity.startActivity(comment);
			break;
		case R.id.tv_jjzsFragment_more:
			// 历史
			Intent history = new Intent(getActivity(),
					HistoryEquityActivity.class);
			history.putExtra("FundName", fundName);
			history.putExtra("InputFundValue", fundCode);
			history.putExtra("FundType", isHuoBi);
			activity.startActivity(history);
			break;
		default:
			break;
		}
	}
	
	
	void dealBuy(){
		encodePassWord = App.getContext().getEncodePassWord();
		idCard = App.getContext().getIdCard();
		//	if (sessionId==null) {
		//		Intent intent=new Intent(activity, MyMeansActivity.class);
		//		startActivity(intent);
		//	}else {
			activity.showProgressDialog("正在加载");
			RequestParams params = new RequestParams(activity.getApplicationContext());
			//params.put("id", idCard);
			params.put("sessionId", sessionId);
			params.put("passwd", encodePassWord);
			params.put("condition", fundCode);
			params.put("fundType", null);
			params.put("company", null);
			activity.execApi(ApiType.GET_DEALSEARCHONETWO, params,
					new OnDataReceivedListener() {

						@Override
						public void onReceiveData(ApiType api,
								String json) {
							System.out.println("json==========>"+json);
							if (json == null) {
								activity.showToast("请求失败");
								activity.disMissDialog();
								return;
							}
							if (json != null && !json.equals("")) {
								tInputStringStream = new ByteArrayInputStream(json.getBytes());
								XmlPullParser parser = Xml.newPullParser();
								try {
									parser.setInput(tInputStringStream, "UTF-8");
									int event = parser.getEventType();
									while (event != XmlPullParser.END_DOCUMENT) {
										Log.i("start_document", "start_document");
										switch (event) {
										case XmlPullParser.START_TAG:
											if ("return".equals(parser.getName())) {
												String xmlReturn;
												try {
													xmlReturn = parser.nextText();
													System.out.println("<><><><><><><><><>"
															+ xmlReturn);
													List<DealSearchResult> list;
													list = JSON.parseArray(xmlReturn,DealSearchResult.class);
													System.out.println("list===========---------->"+list);
													DealSearchResult res = list.get(0);
													Intent intent = new Intent(activity.getApplicationContext(),
															DealApplyActivity.class);
													Bundle bundle = new Bundle();
													bundle.putString("IDCard", idCard);
													bundle.putString("sessionId", sessionId);
													bundle.putString("PassWord", encodePassWord);
													bundle.putSerializable("DealSearchResult", res);
													// intent.putExtra("IDCard", encodeIdCard);
													// intent.putExtra("PassWord", encodePassWord);
													intent.putExtras(bundle);
													activity.startActivity(intent);
													
											

												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

											}
											break;

										}
										try {
											event = parser.next();

										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
									try {
										tInputStringStream.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (XmlPullParserException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								activity.disMissDialog();
							}


						}
					});
		//	}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_FUND_DETAIL_INFO) {
			fundDetailInfos = JSON.parseArray(json, GetFundDetailInfo.class);
			GetFundDetailInfo info = fundDetailInfos.get(0);

			fundType = info.getFundType();
			tv_fundType.setText(fundType);
			tv_unitEquity.setText(info.getUnitEquity());
			tv_dealData.setText("(" + info.getDealDate() + ")");
			tv_totalEquity.setText(info.getTotalEquity());
			isHuoBi = info.getIsHuobi();
			if (isHuoBi.equals("True")) {
				tv_title_benefit.setText("费率");
				tv_title_unit.setText("万份收益");
				tv_title_total.setText("七日年化");
				tv_totalEquity.setText(info.getTotalEquity()+"%");
				
				tv_chart_unit.setText("万份收益");
				tv_chart_total.setText("七日年化");
				
				tv_list_benefit.setText("费率");
				tv_list_benefit.setVisibility(View.GONE);
				tv_list_unit.setText("万份收益");
				tv_list_total.setText("七日年化");
				
				tv_chartTitle_unit.setText("万份收益:");
				tv_chartTitle_total.setText("七日年化:");
				
			}else {
				tv_title_benefit.setText("日涨跌幅");
				tv_title_unit.setText("单位净值");
				tv_title_total.setText("累计收益");
				
				tv_chart_unit.setText("单位净值");
				tv_chart_total.setText("累计收益");
				
				tv_list_benefit.setText("日涨跌幅");
				tv_list_unit.setText("单位净值");
				tv_list_total.setText("累计收益");
				
				tv_chartTitle_unit.setText("单位净值:");
				tv_chartTitle_total.setText("累计收益率:");
			}

			if (info.getIsOpenToBuy().equals("2")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("3")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("4")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("5")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("7")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("8")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}else if (info.getIsOpenToBuy().equals("9")) {
				bt_buy.setClickable(false);
				bt_buy.setText("购买");
				bt_buy.setBackgroundColor(Color.GRAY);
			}
			if (info != null && !TextUtils.isEmpty(info.getDayBenefit())) {
				tv_dayBenefit.setText(info.getDayBenefit() + "%");
				if (info.getDayBenefit().substring(0, 1).equals("-")) {
					tv_dayBenefit.setTextColor(Color.rgb(1, 153, 1));
				} else {
					tv_dayBenefit.setTextColor(Color.RED);
				}
			} else {
				tv_dayBenefit.setText("0");
			}
			
		} else if (api == ApiType.GET_FIVE_UNIT_EQUITY && !activity.isFinishing()) {
			fiveUnitEquities = JSON.parseArray(json, Get5unitEquity.class);
			listView_5unitEquity.setAdapter(new FiveUnitEquityAdapter(
					fiveUnitEquities));
			setListViewHeightBasedOnChildren(listView_5unitEquity);
			
		} else if (api == ApiType.GET_UNIT_CHAR) {
			
			unitCharList = JSON.parseArray(json, GetUnitChar.class);
			x = new String[unitCharList.size()];
			dx = new long[unitCharList.size()];
			datax = new double[dx.length];
			udy = new double[unitCharList.size()];
			for (int i = 0; i < unitCharList.size(); i++) {
				GetUnitChar uchar = unitCharList.get(i);
				String dealdate = uchar.DealDate;

				// Date data =StringToDate(dealdate);
				// long time=data.getTime();

//				java.util.Date date = null;
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				try {
//					date = df.parse(dealdate);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
				x[i] = dealdate;// date.getTime();// .getDay();
				dx[i] = i;
				double unitequity = uchar.UnitEquity;
				udy[i] = unitequity;
			}
			addACE(x,dx, udy, null);
			activity.disMissDialog();
		} else if (api == ApiType.GET_TOTAL_CHAR) {
			totalCharList = JSON.parseArray(json, GetTotalChar.class);
			
			x = new String[totalCharList.size()];
			dx = new long[totalCharList.size()];
			datax = new double[dx.length];
			tdy = new double[totalCharList.size()];
			udy = new double[totalCharList.size()];
			for (int i = 0; i < totalCharList.size(); i++) {
				GetTotalChar tchar = totalCharList.get(i);
				String dealdate = tchar.getDealDate();

				// Date data =StringToDate(dealdate);
				// long time=data.getTime();

//				java.util.Date date = null;
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				try {
//					date = df.parse(dealdate);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				d[i] = date.getTime();// .getDay();
				
				x[i] = dealdate;
				dx[i] = i;
				double pChg = tchar.getPchg();
				udy[i] = pChg;

				double totalequity = tchar.getTotalEquity();
				tdy[i] = totalequity;
			}
			if ("True".equals(isHuoBi)) {
				addACE(x,dx, null, tdy);
			} else {
				addACE(x,dx, udy, tdy);
			}
			activity.disMissDialog();
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

		FiveUnitEquityAdapter listAdapter = (FiveUnitEquityAdapter) listView
				.getAdapter();
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

	class FiveUnitEquityAdapter extends BaseAdapter {

		private List<Get5unitEquity> list;

		public FiveUnitEquityAdapter(List<Get5unitEquity> list) {
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
					R.layout.item_jjzsfragment_unitequity, null, false);
			TextView tv_data_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_data);
			TextView tv_dayBenefit_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_dayBenefit);
			TextView tv_unitEquity_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_unitEquity);
			TextView tv_totalEquity_item = (TextView) convertView
					.findViewById(R.id.tv_jjzsList_totalEquity);

			if ("True".equals(isHuoBi)) {
				tv_dayBenefit_item.setVisibility(View.GONE);
			}
			
			Get5unitEquity equity = list.get(position);
			tv_data_item.setText(equity.getDealDate());
			tv_unitEquity_item.setText(equity.getUnitEquity());
			tv_totalEquity_item.setText(equity.getTotalEquity());
			if ("True".equals(isHuoBi)) {
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

	/**
	 * 添加一个表格到程序中
	 */
	private void addACE(String[] xTitle, long[] x, double[] y1, double[] y2) {
		
		char_layout.removeAllViews();

		// long[] data1 = q;// {21,15,8,0,17};
		// double[] data2 = eq;// {11,5,18,10,7};
		int[] colors = new int[] { Color.RED, Color.BLUE, Color.CYAN,
				Color.YELLOW, Color.WHITE, Color.WHITE, Color.YELLOW };

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.clear();
		if (y1 != null) {
			XYSeries xy1;
			if ("True".equals(isHuoBi)) {
				xy1 = new XYSeries("万份收益");
			} else {
				if (y2 == null) {
					xy1 = new XYSeries("单位净值");
				}else {
					xy1 = new XYSeries("上证指数");
				}
			}
			for (int i = 0; i < x.length; i++) {
				xy1.add(x[i], y1[i]);
			}
			dataset.addSeries(xy1);
			
		}

		if (y2 != null) {
			XYSeries xy2;
			if (isHuoBi.equals("True")) {
				xy2 = new XYSeries("七日年化");
			} else {
				xy2 = new XYSeries("累计收益率");
			}
			for (int i = 0; i < x.length; i++) {
				xy2.add(x[i], y2[i]);
			}
			dataset.addSeries(xy2);
		}
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setClickEnabled(true); // 是否可以点击
		renderer.setSelectableBuffer(30); // 点击区域的大小
		renderer.setXLabels(0);
		renderer.setYLabels(8);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		
		// renderer.setGridColor(colors[4]);//设置网格颜色
//		 renderer.setAxesColor(colors[0]);//设置坐标轴颜色
		
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0,Color.BLACK);
		
		renderer.setBackgroundColor(colors[4]);
		renderer.setMarginsColor(colors[4]);
		// renderer.setApplyBackgroundColor(false);

		// renderer.setClickEnabled(false);//设置了之后，不可再拖动
		renderer.setZoomButtonsVisible(false);// 设置伸缩按钮是否可见
		renderer.setZoomEnabled(false);
		renderer.setExternalZoomEnabled(false);
		renderer.setInScroll(true);
		
		if (x.length > 10) {
			for (int i = 0; i < x.length; ) {
				renderer.addXTextLabel(x[i], xTitle[i]); 
				i+=Math.round(x.length/11.0);
			}
		}else {
			for (int i = 0; i < x.length; i++) {
				renderer.addXTextLabel(x[i], xTitle[i]); 
			}
		}

		renderer.setPanEnabled(false, false);
		
		for (int i = 0; i < 2; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStrokeWidth(5);
			r.setPointStyle(PointStyle.POINT);
			r.setFillPoints(true);
			r.setLineWidth(1);
			if (i == 0) {
				r.setFillBelowLine(true);
				r.setFillBelowLineColor(colors[5]);
			}
			renderer.addSeriesRenderer(r);
		}
		// 构建图表
		xychart = new LineChart(dataset, renderer);
		 graph = new GraphicalView(getActivity(), xychart);
		graph.setOnTouchListener(chartViewOnTouchListener);
		//mScrollView.setChildView(graph);
		char_layout.addView(graph, -1, -1);
		graph.setOnClickListener(gOCL);
		l = new LinearLayout(activity);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				1, RelativeLayout.LayoutParams.FILL_PARENT);
		l.setLayoutParams(layoutParams);
		l.setBackgroundColor(colors[0]);

//		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		childText.setLayoutParams(layoutParams1);
//		childText.setTextColor(colors[0]);
		// childText.setVisibility(View.GONE);

		char_layout.addView(l);
	}

	/**
	 * 监听器，用来处理点被点击的事件
	 */
	OnClickListener gOCL = new OnClickListener() {

		@Override
		public void onClick(View v) {
			GraphicalView gv = (GraphicalView) v;
			// 将view转换为可以监听的GraphicalView
			SeriesSelection ss = gv.getCurrentSeriesAndPoint();
			// 获得被点击的系列和点
			if (ss == null)
				return;
			double[] point = new double[] { ss.getXValue(), ss.getValue() };
			// 获得当前被点击点的X位置和Y数值
			final double[] dest = xychart.toScreenPoint(point);
			// 获得当前被点击点的坐标位置
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = new Date((long) point[0]);
//			String curDate = df.format(date);
		}
	};
	
	
	
	
	private OnTouchListener chartViewOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP){  
                mScrollView.requestDisallowInterceptTouchEvent(false);  
            }else{  
            	mScrollView.requestDisallowInterceptTouchEvent(true);  
            }  
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mEventStartX = (int) event.getX();
				mEventStartY = (int) event.getY();

				for (int i = 0; i < dx.length; i++) {
					double[] point;
					if (udy == null) {
						point = new double[] { dx[i], tdy[i] };
					}else {
						point = new double[] { dx[i], udy[i] };
					}
					final double[] dest = xychart.toScreenPoint(point);
					datax[i] = dest[0];
				}
				break;
			case MotionEvent.ACTION_UP:
				isShow(false);
				break;
			case MotionEvent.ACTION_MOVE:
				isShow(true);
				mEventEndX = (int) event.getX();
				mEventEndY = (int) event.getY();

				for (int j = 0; j < datax.length; j++) {
					if (mEventEndX == (int) datax[j]) {
//						if (childText.getVisibility() != View.VISIBLE)
//							childText.setVisibility(View.VISIBLE);
						//滑动 显示的数值
//						childText.setText("净值:"+tdy[j]+"\r\n日期:"+x[j]);
//						childText.setTextSize(10);
						// childText.requestLayout();
						
						if (tdy != null) {
							tv_chartData_unit.setText(udy[j]+"%");
							tv_chartData_total.setText(tdy[j]+"%");
						}else {
							tv_chartData_unit.setText(udy[j]+"");
						}
						tv_chartData_date.setText(x[j]+"");
					}
				}
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						1, RelativeLayout.LayoutParams.MATCH_PARENT);
				layoutParams.leftMargin = mEventEndX;
				l.setLayoutParams(layoutParams);

//				RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				layoutParams1.topMargin = mEventEndY / 3;
//				layoutParams1.leftMargin = mEventEndX + 10;
//				childText.setLayoutParams(layoutParams1);

				char_layout.requestLayout();
				break;
			}
			
			//mScrollView.setChildView("");
			return false;
		}
	};
	private String sessionId;
	private String mobile;

	public void isShow(boolean show) {
		if (show) {
			l.setVisibility(View.VISIBLE);
		} else {
			l.setVisibility(View.GONE);
		}
	}
}
