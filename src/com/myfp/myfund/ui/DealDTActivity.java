package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DTSearchResult;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.HoldDetail;
import com.myfp.myfund.ui.view.AutoListView;
import com.myfp.myfund.ui.view.AutoListView.OnLoadListener;
import com.myfp.myfund.ui.view.AutoListView.OnRefreshListener;

/**
 * 定投
 * */
public class DealDTActivity extends BaseActivity {

	private String customrisklevel;
	EditText et_dt_search;
	Button bt_dt_search;
	ByteArrayInputStream tInputStringStream = null;
	ListView list_dtlist;
	public static DealDTActivity instance = null;
	int startindex;
	private  List<DTSearchResult> results;
	private String sessionId;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealdt);
		instance = this;
		Intent intent = getIntent();
		sessionId = intent.getStringExtra("sessionId");
		customrisklevel = intent.getStringExtra("CustomRiskLevel");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("定期定额基金买入");
		et_dt_search = (EditText) findViewById(R.id.et_dt_search);
		list_dtlist = (ListView) findViewById(R.id.list_dt_list);
		findViewAddListener(R.id.bt_dt_search);
		

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_dt_search:

			RequestParams params = new RequestParams(this);
			params.put("sessionId", sessionId);
			params.put("condition", et_dt_search.getText().toString());
			params.put("Pagesize", "");
			params.put("Startindex", "");
			execApi(ApiType.GET_DTSEARCHTWO, params);
			showProgressDialog("正在搜索");
			break;

		default:
			break;
		}

	}

	class DTSearchAdapter extends BaseAdapter {

		private List<DTSearchResult> list;

		public DTSearchAdapter(List<DTSearchResult> list) {
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
			ViewHolder holder;

			convertView = LayoutInflater.from(DealDTActivity.this).inflate(
					R.layout.item_dealdt, null, false);
			holder = new ViewHolder();

			holder.tv_dt_fundName = (TextView) convertView
					.findViewById(R.id.tv_dt_fundname);
			holder.tv_dt_fundCode = (TextView) convertView
					.findViewById(R.id.tv_dt_fundcode);
			holder.tv_dt_fundType = (TextView) convertView
					.findViewById(R.id.tv_dt_fundtype);
			holder.bt_deal_dt = (Button) convertView
					.findViewById(R.id.bt_deal_dt);
			holder.tv_dt_fundequity = (TextView) convertView
					.findViewById(R.id.tv_dt_fundequity);

			convertView.setTag(holder);

			final DTSearchResult res = list.get(position);
			holder.tv_dt_fundName.setText(res.getFundname());
			holder.tv_dt_fundCode.setText(res.getFundcode());
			if (res.getFundtype().equals("2")) {
				holder.tv_dt_fundType.setText("货币型");
				holder.tv_dt_fundequity.setText(String.format("%.2f",
						Double.parseDouble(res.getGrowthrate())));
			} else {
				if (res.getFundtype().equals("0")) {
					holder.tv_dt_fundType.setText("股票型");
				} else if (res.getFundtype().equals("1")) {
					holder.tv_dt_fundType.setText("债券型");
				} else if (res.getFundtype().equals("3")) {
					holder.tv_dt_fundType.setText("混合型");
				} else if (res.getFundtype().equals("4")) {
					holder.tv_dt_fundType.setText("专户基金");
				} else if (res.getFundtype().equals("5")) {
					holder.tv_dt_fundType.setText("指数型");
				} else if (res.getFundtype().equals("6")) {
					holder.tv_dt_fundType.setText("QDII型");
				}
				holder.tv_dt_fundequity.setText(String.format("%.2f",
						Double.parseDouble(res.getTotalnav())));
			}
			if ("1".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			}else if ("2".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			} else if ("3".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			} else if ("4".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			}  else if ("9".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			} else if ("a".equals(res.getStatus())) {
				holder.bt_deal_dt.setEnabled(false);
				holder.bt_deal_dt.setBackgroundColor(Color.GRAY);
			} else {

				holder.bt_deal_dt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// if("2".equals(res.getStatus()) ||
						// "3".equals(res.getStatus())
						// || "4".equals(res.getStatus())
						// || "5".equals(res.getStatus())
						// || "9".equals(res.getStatus())
						// || "a".equals(res.getStatus())){
						// showToast("该基金暂时不能申购！！");
						// return;
						// }
						Intent intent = new Intent(DealDTActivity.this,
								DTApplyActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("sessionId", sessionId);
						//bundle.putString("IDCard", encodeIdCard);
//						bundle.putString("PassWord", encodePassWord);
						bundle.putSerializable("DTSearchResult", res);
						bundle.putString("CustomRiskLevel", customrisklevel);
						// intent.putExtra("IDCard", encodeIdCard);
						// intent.putExtra("PassWord", encodePassWord);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_dt_fundName, tv_dt_fundCode, tv_dt_fundType,
					tv_dt_fundequity;
			Button bt_deal_dt;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_DTSEARCHTWO) {
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
									
									results = JSON.parseArray(xmlReturn,
											DTSearchResult.class);

									if (results.size() == 0) {
										showToast("没有符合条件的基金！");
										disMissDialog();
										return;
									}
									list_dtlist.setAdapter(new DTSearchAdapter(
											results));
									list_dtlist.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent, View view,
												int position, long itemId) {
											// TODO Auto-generated method stub
											DTSearchResult res = results.get(position);
											Intent intent = new Intent(
													DealDTActivity.this,
													DetailActivity.class);
											intent.putExtra("fundName", res
													.getFundname().trim());
											intent.putExtra("fundCode", res
													.getFundcode().trim());
											intent.putExtra("sessionId", sessionId);
											startActivity(intent);
										}
									});

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
				disMissDialog();

			}

		}
	}

}
