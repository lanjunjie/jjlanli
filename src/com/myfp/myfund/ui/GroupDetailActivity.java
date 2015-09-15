package com.myfp.myfund.ui;

import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
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
import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.GroupChar;
import com.myfp.myfund.api.beans.GroupPercent;
import com.myfp.myfund.utils.ImageCacheManager;

public class GroupDetailActivity extends BaseActivity {

	private String fundName, recomtype, fundCode;
	private ListView listView_percent;
	private TextView tv_sumPercent, tv_charHelp, tv_earnHelp, tv_riskHelp,
			tv_charDataField, tv_earnDataField, tv_charTitle,
			tv_chartTitle_unit, tv_chartTitle_total, tv_chartData_unit,
			tv_chartData_total, tv_chartData_date;
	private List<GroupPercent> datas;
	private NetworkImageView img_banner;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String baseUrl = "http://www.myfund.com";
	private LinearLayout layout_name, layout_earnings, layout_retrace,
			layout_risk;
	private RelativeLayout char_layout;
	private LineChart xychart;
	private List<GroupChar> groupChars;
	private String buyUrl;
	private Button bt_buy;
	private String groupFlag;
	private long[] dx;
	private double[] tdy, udy;
	private double[] datax;
	private String[] x;
	private int mEventEndX;
	private int mEventEndY;
	private int mEventStartX;
	private int mEventStartY;
	private LinearLayout l;
	private ScrollView mScrollView;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_groupdetail);

		showProgressDialog("正在拼命加载");

		Intent intent = getIntent();
		recomtype = intent.getStringExtra("recomtype");
		fundName = intent.getStringExtra("fundName");
		fundCode = intent.getStringExtra("fundCode");

		RequestParams params = new RequestParams(this);
		params.put("recomtype", recomtype);
		execApi(ApiType.GET_RECOM_FUND_PERCENT, params);
		execApi(ApiType.GET_RECOM_FUND_DETAIL, params);
		execApi(ApiType.GET_GROUP_CHAR, params);
		execApi(ApiType.GET_SAME_EARNINGS, params);

		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {
		setTitle(fundName+"("+fundCode+")");
		
		mScrollView=(ScrollView)findViewById(R.id.scrollView1);
		listView_percent = (ListView) findViewById(R.id.listView_groupDetail_percent);
		tv_sumPercent = (TextView) findViewById(R.id.textView_groupDetail_sumPersent);
		tv_charHelp = (TextView) findViewById(R.id.textView_groupDetail_charHelp);
		tv_earnHelp = (TextView) findViewById(R.id.textView_groupDetail_earnHelp);
		tv_riskHelp = (TextView) findViewById(R.id.textView_groupDetail_riskHelp);
		tv_charDataField = (TextView) findViewById(R.id.textView_groupDetail_charDataField);
		tv_earnDataField = (TextView) findViewById(R.id.textView_groupDetail_earnDataField);
		tv_charTitle = (TextView) findViewById(R.id.textView_groupDetail_charTitle);

		bt_buy = (Button) findViewAddListener(R.id.button_groupDetail_buy);
		findViewAddListener(R.id.bt_groupDetail_telConsult);
		img_banner = (NetworkImageView) findViewById(R.id.img_privateFund_banner);

		char_layout = (RelativeLayout) findViewById(R.id.layout_group_char);
		layout_name = (LinearLayout) findViewById(R.id.layout_group_fundName);
		layout_earnings = (LinearLayout) findViewById(R.id.layout_group_oneYearRedound);
		layout_retrace = (LinearLayout) findViewById(R.id.layout_group_maxRetrace);
		layout_risk = (LinearLayout) findViewById(R.id.layout_group_riskBeneRatio);

		tv_chartTitle_unit = (TextView) findViewById(R.id.tv_groupChart_title_unit);
		tv_chartTitle_total = (TextView) findViewById(R.id.tv_groupChart_title_total);

		tv_chartData_unit = (TextView) findViewById(R.id.tv_groupChart_unit);
		tv_chartData_total = (TextView) findViewById(R.id.tv_groupChart_total);
		tv_chartData_date = (TextView) findViewById(R.id.tv_groupChart_date);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.button_groupDetail_buy:
			Intent intent = new Intent(this, DealActivity.class);
			intent.putExtra("GroupBuyURL", buyUrl);
			startActivity(intent);
			break;
		case R.id.bt_groupDetail_telConsult:
			Intent orderTel = new Intent();
			orderTel.setAction(Intent.ACTION_DIAL);
			orderTel.setData(Uri.parse("tel:400-888-6661"));
			startActivity(orderTel);
			break;
		default:
			break;
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

		PercentAdapter listAdapter = (PercentAdapter) listView.getAdapter();
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

	class PercentAdapter extends BaseAdapter {

		private List<GroupPercent> list;

		public PercentAdapter(List<GroupPercent> list) {
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
			convertView = LayoutInflater.from(GroupDetailActivity.this)
					.inflate(R.layout.item_fundgroup_percent, null, false);
			TextView tv_sign = (TextView) convertView
					.findViewById(R.id.textView_sign);
			TextView tv_fundName = (TextView) convertView
					.findViewById(R.id.textView_fundName);
			TextView tv_percent = (TextView) convertView
					.findViewById(R.id.textView_percent);

			tv_sign.setText(list.get(position).getFundCode());
			tv_fundName.setText(list.get(position).getFundName());
			tv_percent.setText(list.get(position).getInvestPercent());
			return convertView;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_RECOM_FUND_PERCENT) {
			datas = JSON.parseArray(json, GroupPercent.class);
			listView_percent.setAdapter(new PercentAdapter(datas));
			setListViewHeightBasedOnChildren(listView_percent);
		} else if (api == ApiType.GET_RECOM_FUND_DETAIL) {
			try {
				JSONArray array = new JSONArray(json);
				JSONObject obj = array.getJSONObject(0);
				String percent = obj.getString("OneYearEarn").trim();
				tv_sumPercent
						.setText(percent.substring(0, percent.length() - 1));
				tv_charHelp.setText("      " + obj.getString("TrendChartHelp"));
				tv_earnHelp.setText("      "
						+ obj.getString("SameEarnFundHelp"));
				tv_riskHelp.setText("      " + obj.getString("RiskEarnHelp"));
				tv_charDataField.setText(obj.getString("DataField"));
				tv_earnDataField.setText(obj.getString("DataField"));
				tv_charTitle.setText(obj.getString("CombTitle2"));

				groupFlag = obj.getString("GroupFundFlag");

				buyUrl = obj.getString("GroupBuyURL");
				if (!obj.getString("IsOpenToBuy").equals("1")) {
					bt_buy.setBackgroundColor(Color.GRAY);
					bt_buy.setClickable(false);
				}

				// img_banner.setDefaultImageResId(R.drawable.ic_launcher);
				// img_banner.setErrorImageResId(R.drawable.ic_launcher);
				img_banner.setImageUrl(baseUrl
						+ obj.getString("Banner").substring(1), imageLoader);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (api == ApiType.GET_GROUP_CHAR) {
			groupChars = JSON.parseArray(json, GroupChar.class);

			tv_chartTitle_unit.setText(groupChars.get(0).getContrastName());
			tv_chartTitle_total.setText(groupChars.get(0).getCombName());

			x = new String[groupChars.size()];
			dx = new long[groupChars.size()];
			datax = new double[groupChars.size()];
			udy = new double[groupChars.size()];
			tdy = new double[groupChars.size()];

			for (int i = 0; i < groupChars.size(); i++) {
				GroupChar gchar = groupChars.get(i);
				String dealdate = gchar.getDate();

				// Date data =StringToDate(dealdate);
				// long time=data.getTime();

				// java.util.Date date = null;
				// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				// try {
				// date = df.parse(dealdate);
				// } catch (ParseException e) {
				// e.printStackTrace();
				// }
				x[i] = dealdate;
				dx[i] = i;// date.getTime();// .getDay();
				double contrast = gchar.getCombContrast();
				udy[i] = contrast;

				double totalequity = gchar.getCombIndex();
				tdy[i] = totalequity;
			}
			addACE(x, dx, udy, tdy);
			disMissDialog();
		} else if (api == ApiType.GET_SAME_EARNINGS) {
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < 3; i++) {
					JSONObject obj = array.getJSONObject(i);

					TextView tv_name = (TextView) layout_name
							.getChildAt(i * 2 + 2);
					tv_name.setText(obj.getString("FundName"));

					TextView tv_earnings = (TextView) layout_earnings
							.getChildAt(i * 2 + 2);
					tv_earnings.setText(obj.getString("OneYearRedound"));

					TextView tv_retrace = (TextView) layout_retrace
							.getChildAt(i * 2 + 2);
					tv_retrace.setText(obj.getString("MaxRetrace"));

					TextView tv_risk = (TextView) layout_risk
							.getChildAt(i * 2 + 2);
					tv_risk.setText(obj.getString("RiskBeneRatio"));

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加一个表格到程序中
	 */
	private void addACE(String[] xTitle, long[] x, double[] y1, double[] y2) {
		char_layout.removeAllViews();
		// long[] data1 = q;// {21,15,8,0,17};
		// double[] data2 = eq;// {11,5,18,10,7};
		int[] colors = new int[] { Color.BLUE, Color.RED, Color.CYAN,
				Color.YELLOW, Color.WHITE, Color.WHITE, Color.YELLOW };

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.clear();
		XYSeries xy1 = new XYSeries(groupChars.get(0).getContrastName());

		for (int i = 0; i < x.length; i++) {
			xy1.add(x[i], y1[i]);
		}
		dataset.addSeries(xy1);

		XYSeries xy2 = new XYSeries(groupChars.get(0).getCombName());
		for (int i = 0; i < x.length; i++) {
			xy2.add(x[i], y2[i]);
		}
		dataset.addSeries(xy2);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setClickEnabled(true); // 是否可以点击
		renderer.setSelectableBuffer(30); // 点击区域的大小
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		//
		// renderer.setGridColor(colors[4]);//设置网格颜色
		// renderer.setAxesColor(colors[4]);//设置坐标轴颜色
		renderer.setBackgroundColor(colors[4]);
		renderer.setMarginsColor(colors[4]);
		// renderer.setApplyBackgroundColor(false);

		// renderer.setClickEnabled(false);//设置了之后，不可再拖动
		renderer.setZoomButtonsVisible(false);// 设置伸缩按钮是否可见
		renderer.setZoomEnabled(false);
		renderer.setExternalZoomEnabled(false);
		renderer.setInScroll(true);

		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);

		for (int i = 0; i < x.length;) {
			renderer.addXTextLabel(x[i], xTitle[i]);
			i += x.length / 11;
		}

		renderer.setPanEnabled(false, false);

		for (int i = 0; i < 2; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStrokeWidth(1);
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
		GraphicalView graph = new GraphicalView(GroupDetailActivity.this,
				xychart);
		graph.setOnTouchListener(chartViewOnTouchListener);
		char_layout.addView(graph, -1, -1);
		graph.setOnClickListener(gOCL);
		l = new LinearLayout(this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				1, RelativeLayout.LayoutParams.FILL_PARENT);
		l.setLayoutParams(layoutParams);
		l.setBackgroundColor(colors[1]);
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
			xychart.toScreenPoint(point);

			// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// Date date = new Date((long) point[0]);
			// String curDate = df.format(date);

		}
	};

	private OnTouchListener chartViewOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mScrollView.requestDisallowInterceptTouchEvent(false);
			} else {
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
					} else {
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
						// if (childText.getVisibility() != View.VISIBLE)
						// childText.setVisibility(View.VISIBLE);
						// 滑动 显示的数值
						// childText.setText("净值:"+tdy[j]+"\r\n日期:"+x[j]);
						// childText.setTextSize(10);
						// childText.requestLayout();

						tv_chartData_unit.setText(udy[j] + "%");
						if (tdy != null) {
							tv_chartData_total.setText(tdy[j] + "%");
						}
						tv_chartData_date.setText(x[j]);
					}
				}
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						1, RelativeLayout.LayoutParams.MATCH_PARENT);
				layoutParams.leftMargin = mEventEndX;
				l.setLayoutParams(layoutParams);

				RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams1.topMargin = mEventEndY / 3;
				layoutParams1.leftMargin = mEventEndX + 10;
				// childText.setLayoutParams(layoutParams1);

				char_layout.requestLayout();
				break;
			}
			return false;
		}
	};

	public void isShow(boolean show) {
		if (show) {
			l.setVisibility(View.VISIBLE);
		} else {
			l.setVisibility(View.GONE);
		}
	}
}
