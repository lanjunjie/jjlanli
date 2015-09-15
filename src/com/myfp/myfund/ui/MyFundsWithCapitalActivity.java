package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class MyFundsWithCapitalActivity extends BaseActivity{

	private TextView tev_leveraged;
	private TextView tev_investment;
	private TextView tev_borrowingbalance;
	private TextView tev_loanterm;
	private TextView tev_warningline;
	private TextView tev_stoploss;
	//private TextView but_mymatch;
	private TextView tev_webv;
	private String idcard;
	private String xmlReturn;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_funds_with_capital);
		Intent intent = getIntent();
		idcard = intent.getStringExtra("idcard");
		RequestParams params=new RequestParams(this);
		params.put("idcard", idcard);
		execApi(ApiType.FUNDSWITH_CAPITAL, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		setTitle("配资服务");
		tev_leveraged = (TextView) findViewById(R.id.tev_leveraged);
		tev_investment = (TextView) findViewById(R.id.tev_investment);
	//	tev_borrowingbalance = (TextView) findViewById(R.id.tev_borrowingbalance);
		tev_loanterm = (TextView) findViewById(R.id.tev_loanterm);
		tev_warningline = (TextView) findViewById(R.id.tev_warningline);
		tev_stoploss = (TextView) findViewById(R.id.tev_stoploss);
		//but_mymatch = (TextView) findViewById(R.id.but_mymatch);
		tev_webv = (TextView) findViewById(R.id.tev_webv);
		findViewAddListener(R.id.but_mymatch);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.but_mymatch:
			Intent intent =new Intent(this, PaySuccessActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json==null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api==ApiType.FUNDSWITH_CAPITAL) {
			if (json!=null&&!json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event!= XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								try {
									xmlReturn = parser.nextText();
									org.json.JSONArray jsonArray=new org.json.JSONArray(xmlReturn);
									System.out.println("jsonArray---------->"+jsonArray);
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject object = jsonArray.getJSONObject(i);
										String data = object.getString("dbegin").split(" ")[0];
										String dbegin = data.replaceAll("-", "/");
									tev_leveraged.setText(dbegin);
									String sp = object.getString("dend").split(" ")[0];
									String dend = sp.replaceAll("-", "/");
									//.substring(0, object.getString("guaranteemon").indexOf("."))
									tev_investment.setText(dend);
									//tev_borrowingbalance.setText(object.getString("borrowmoney").substring(0, object.getString("borrowmoney").indexOf(".")));
									tev_loanterm.setText(object.getString("earingrate").substring(0, object.getString("earingrate").indexOf("."))+"%");
									tev_warningline.setText(object.getString("benefitrate").substring(0, object.getString("benefitrate").indexOf("."))+"%");
									tev_stoploss.setText(object.getString("stoplimit").substring(0, object.getString("stoplimit").indexOf(".")));
									
									
									}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
							}
							
							
							break;

						default:
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
			disMissDialog();
		}
	}

}
