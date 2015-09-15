package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class SmallMoneyActivity extends BaseActivity {
	TextView tv_smallmoney_date;
	EditText et_smallmoney_jine;
	ByteArrayInputStream tInputStringStream = null;
	Intent intent;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_smallmoney);
		intent = getIntent();

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("小额打款");
		tv_smallmoney_date = (TextView) findViewById(R.id.tv_smallmoney_date);
		et_smallmoney_jine = (EditText) findViewById(R.id.et_smallmoney_jine);
		findViewAddListener(R.id.bt_smallmoney_confirm);
		String date = intent.getStringExtra("BusiDate");
		tv_smallmoney_date.setText(date.substring(0, 4) + "年"
				+ date.substring(4, 6) + "月" + date.substring(6, date.length())
				+ "日");

	}
	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_smallmoney_confirm:
			if (!(et_smallmoney_jine.getText().toString().trim().length() == 0)) {
				RequestParams params = new RequestParams(this);
				params.put("amount", et_smallmoney_jine.getText().toString()
						.trim());
				// params.put(RequestParams.MOBILE, username);
				params.put("certificateno",
						intent.getStringExtra("Certificateno"));
				params.put("moneyaccount",
						intent.getStringExtra("MoneyAccount"));
				params.put("depositacct", intent.getStringExtra("Depositacct"));
				params.put("custno", intent.getStringExtra("Custno"));
				params.put("depositacctname",intent.getStringExtra("DepositacctName"));
				params.put("channelid", intent.getStringExtra("ChannelId"));
				params.put("certificatetype",
						intent.getStringExtra("CertificateType"));
				execApi(ApiType.GET_CHKSMALLMONEY, params);
				showProgressDialog("正在加载");

			} else {
				showToast("打款金额为空！");
				return;
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {

			if (api == ApiType.GET_CHKSMALLMONEY) {

				if (json != null && !json.equals("")) {
					tInputStringStream = new ByteArrayInputStream(
							json.getBytes());
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
										System.out.println("<><><><><><><><><>"
												+ xmlReturn);
										try {
											JSONObject jsonObj = new JSONObject(
													xmlReturn);
											if (jsonObj.getString("code")
													.equals("0000")) {
												disMissDialog();
												showToast(jsonObj
														.getString("msg"));
												if(intent.getStringExtra("CustomRiskLevel").equals("0")){
													Intent intent2 = new Intent(SmallMoneyActivity.this,
															RiskAssessmentActivity.class);
													intent2.putExtra(
															"IDCard",
															intent.getStringExtra("IDCard"));
													intent2.putExtra(
															"PassWord",
															intent.getStringExtra("PassWord"));
													
													intent2.putExtra(
															"DepositacctName",
															intent.getStringExtra("DepositacctName"));
															
													intent2.putExtra(
															"TotalFundMarketValue",
															intent.getStringExtra("TotalFundMarketValue"));
															
													intent2.putExtra(
															"CountFund",
															intent.getStringExtra("CountFund"));
													startActivity(intent2);
													finish();
												}else{
													Intent intent1 = new Intent(
															SmallMoneyActivity.this,
															DealInforActivity.class);
													intent1.putExtra(
															"IDCard",
															intent.getStringExtra("IDCard"));
													intent1.putExtra(
															"PassWord",
															intent.getStringExtra("PassWord"));
													intent1.putExtra(
															"CustomRiskLevel",
															intent.getStringExtra("CustomRiskLevel"));
														
													intent1.putExtra(
															"DepositacctName",
															intent.getStringExtra("DepositacctName"));
															
													intent1.putExtra(
															"TotalFundMarketValue",
															intent.getStringExtra("TotalFundMarketValue"));
															
													intent1.putExtra(
															"CountFund",
															intent.getStringExtra("CountFund"));
															
													startActivity(intent1);
													App.getContext()
															.setEncodePassWord(
																	intent.getStringExtra("PassWord"));
													App.getContext()
															.setIdCard(
																	intent.getStringExtra("IDCard"));

													SharedPreferences sPreferences = getSharedPreferences(
															"Setting", MODE_PRIVATE);
													Editor editor = sPreferences
															.edit();
													editor.putString(
															"EncodePassWord",
															intent.getStringExtra("PassWord"));
													editor.putString(
															"IDCard",
															intent.getStringExtra("IDCard"));
													editor.commit();
													finish();
													
												}
												
											} else if(jsonObj.getString("code")
													.equals("-490226002")){
												disMissDialog();
												showToast(jsonObj
														.getString("msg"));
												return;
											}else{
												disMissDialog();
												showToast("3次验证失败，账户已被锁定，请联系客服4008886661");
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

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

				}
				disMissDialog();

			}

		}
	}

}
