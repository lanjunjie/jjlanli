package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DTSearchResult;
import com.myfp.myfund.api.beans.DealSearchResult;

public class DealBuyActivity extends BaseActivity {
	private String encodeIdCard, encodePassWord;
	EditText et_deal_search;
	Button bt_deal_search;
	ByteArrayInputStream tInputStringStream = null;
	private List<DealSearchResult> results;
	ListView list_deallist;
	public static DealBuyActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealbuy);
		instance = this;
		Intent intent = getIntent();
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金购买");
		et_deal_search = (EditText) findViewById(R.id.et_deal_search);
		list_deallist = (ListView) findViewById(R.id.list_deal_list);
		findViewAddListener(R.id.bt_deal_search);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_deal_search:
			RequestParams params = new RequestParams(this);
			params.put("condition", et_deal_search.getText().toString());
			params.put("sessionId", getIntent().getStringExtra("sessionId"));
			params.put("fundType", null);
			params.put("company", null);
			execApi(ApiType.GET_DEALSEARCHONETWO, params);
			showProgressDialog("正在搜索");
			break;

		default:
			break;
		}
	}

	class DealSearchAdapter extends BaseAdapter {

		private List<DealSearchResult> list;

		public DealSearchAdapter(List<DealSearchResult> list) {
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
			
				convertView = LayoutInflater.from(DealBuyActivity.this)
						.inflate(R.layout.item_deallist, null, false);
				holder = new ViewHolder();

				holder.tv_deal_fundName = (TextView) convertView
						.findViewById(R.id.tv_deal_fundname);
				holder.tv_deal_fundCode = (TextView) convertView
						.findViewById(R.id.tv_deal_fundcode);
				holder.tv_deal_fundType = (TextView) convertView
						.findViewById(R.id.tv_deal_fundtype);
				holder.bt_deal_buy = (Button) convertView
						.findViewById(R.id.bt_deal_buy);
				holder.tv_deal_fundequity = (TextView) convertView
						.findViewById(R.id.tv_deal_fundequity);

				convertView.setTag(holder);
			
			final DealSearchResult res = list.get(position);
			holder.tv_deal_fundName.setText(res.getFundName());
			holder.tv_deal_fundCode.setText(res.getFundCode());
			if (res.getFundType().equals("2")) {
				holder.tv_deal_fundType.setText("货币型");
				holder.tv_deal_fundequity.setText(String.format("%.2f",
						Double.parseDouble(res.getNav())));
			} else {
				if (res.getFundType().equals("0")) {
					holder.tv_deal_fundType.setText("股票型");
				} else if (res.getFundType().equals("1")) {
					holder.tv_deal_fundType.setText("债券型");
				} else if (res.getFundType().equals("3")) {
					holder.tv_deal_fundType.setText("混合型");
				} else if (res.getFundType().equals("4")) {
					holder.tv_deal_fundType.setText("专户基金");
				} else if (res.getFundType().equals("5")) {
					holder.tv_deal_fundType.setText("指数型");
				} else if (res.getFundType().equals("6")) {
					holder.tv_deal_fundType.setText("QDII型");
				}
				holder.tv_deal_fundequity.setText(String.format("%.2f",
						Double.parseDouble(res.getNav())));
			}

			if ("2".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else if ("3".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else if ("4".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else if ("5".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else if ("9".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else if ("a".equals(res.getStatus())) {
				holder.bt_deal_buy.setEnabled(false);
				holder.bt_deal_buy.setBackgroundColor(Color.GRAY);
			} else {
				if("1".equals(res.getStatus())){
					holder.bt_deal_buy.setText("认购");
				}
				holder.bt_deal_buy.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						if("2".equals(res.getStatus()) || "3".equals(res.getStatus())
//								|| "4".equals(res.getStatus())
//								|| "5".equals(res.getStatus())
//								|| "9".equals(res.getStatus())
//								|| "a".equals(res.getStatus())){
//							showToast("该基金暂时不能申购！！");
//							return;
//						}
						Intent intent = new Intent(DealBuyActivity.this,
								DealApplyActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("sessionId", getIntent().getStringExtra("sessionId"));
						bundle.putString("IDCard", encodeIdCard);
						bundle.putString("PassWord", encodePassWord);
						System.out.println("encodePassWord==============>"+encodePassWord);
						bundle.putSerializable("DealSearchResult", res);
						// intent.putExtra("IDCard", encodeIdCard);
						intent.putExtra("PassWord", encodePassWord);
						intent.putExtras(bundle);
						startActivity(intent);
						
					}
				});
			}	

			return convertView;
		}

		class ViewHolder {
			TextView tv_deal_fundName, tv_deal_fundCode, tv_deal_fundType,
					tv_deal_fundequity;
			Button bt_deal_buy;
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
										DealSearchResult.class);
								if (results.size() == 0) {
									showToast("没有符合条件的基金！");
									disMissDialog();
									return;
								}

								list_deallist.setAdapter(new DealSearchAdapter(
										results));
								list_deallist.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long itemId) {
										// TODO Auto-generated method stub
										DealSearchResult res = results.get(position);
										Intent intent = new Intent(
												DealBuyActivity.this,
												DetailActivity.class);
										intent.putExtra("sessionId", getIntent().getStringExtra("sessionId"));
										intent.putExtra("fundName", res
												.getFundName().trim());
										intent.putExtra("fundCode", res
												.getFundCode().trim());
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
