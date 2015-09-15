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

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class NewParagRaphActivity extends BaseActivity{

	private EditText hit_money;
	private Intent intent;
	ByteArrayInputStream tInputStringStream = null;
	private String sessionid;
	@Override
	protected void setContentView() {
		setContentView(R.layout.new_paragraph_hit);
		intent = getIntent();
		sessionid = intent.getStringExtra("sessionid");
		System.out.println("sessionid1================>"+sessionid);
		
	}

	@Override
	protected void initViews() {
		setTitle("小额打款验证");
		hit_money = (EditText) findViewById(R.id.new_hit_money);
		findViewAddListener(R.id.new_finish_proving);
		findViewAddListener(R.id.new_later_proving);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_finish_proving:
			if (!(hit_money.getText().toString().trim().length() == 0)) {
				RequestParams params = new RequestParams(this);
				params.put("amount", hit_money.getText().toString()
						.trim());
				//params.put(RequestParams.MOBILE, username);
				params.put("certificateno",
						intent.getStringExtra("IdCard"));
				params.put("moneyaccount",
						intent.getStringExtra("MoneyAccount"));
				params.put("depositacct", intent.getStringExtra("Depositacct"));
				params.put("custno", intent.getStringExtra("Custno"));
				params.put("depositacctname",
						intent.getStringExtra("Name"));
				params.put("channelid", intent.getStringExtra("Channelid"));
				params.put("certificatetype","0");
				execApi(ApiType.GET_CHKSMALLMONEY, params);
				showProgressDialog("正在加载");

			} else {
				showToast("打款金额为空！");
				return;
			}
			break;
		case R.id.new_later_proving:
			Intent intent=new Intent(this, MyActivityGroup.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("");
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
												showToast(jsonObj.getString("msg"));
												if(intent.getStringExtra("CustomRiskLevel").equals("0")){
													RequestParams params = new RequestParams(this);
													params.put("sessionId", getIntent().getStringExtra("sessionid"));
													params.put("papercode", "001");
													//params.put("pointList",java.net.URLEncoder.encode(pointList));
													params.put("pointList",java.net.URLEncoder.encode("2|2|2|2|2|2|2|2|2|2|2|2|2|2"));
													params.put("iscontinue", "1");
													//params.put("answer",java.net.URLEncoder.encode(answer));
													params.put("answer",java.net.URLEncoder.encode("undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined"));
													params.put("invtp", "1");
													execApi(ApiType.GET_RISKSUBMITTWO, params, new OnDataReceivedListener() {
														
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
																				String xmlReturn;
																				try {
																					xmlReturn = parser.nextText();
																					System.out.println("<><><><><><><><><>"
																							+ xmlReturn);
																					if (xmlReturn.contains("custrisk")) {
																						try {
																							JSONObject jsonObj = new JSONObject(
																									xmlReturn);
																							showToast("提交成功");
																							Intent intent1 = new Intent(NewParagRaphActivity.this,
																									MyActivityGroup.class);
																							intent1.putExtra(
																									"IDCard",
																									intent.getStringExtra("IdCard"));
																							intent1.putExtra(
																									"CustomRiskLevel",
																									intent.getStringExtra("CustomRiskLevel"));
																							
																							intent1.putExtra(
																									"DepositacctName",
																									intent.getStringExtra("DepositacctName"));
																							
																							intent1.putExtra(
																									"TotalFundMarketValue",
																									intent.getStringExtra("TotalFundMarketValue"));
																							intent1.putExtra("sessionId", sessionid);		
																							intent1.putExtra(
																									"CountFund",
																									intent.getStringExtra("CountFund"));
																							
																							intent1.putExtra("Flag", "5");
																							
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
																									"IDCard",
																									intent.getStringExtra("IDCard"));
																							editor.commit();
																							finish();
																						} catch (JSONException e) {
																							// TODO Auto-generated catch block
																							e.printStackTrace();
																						}

																					} else {
																						disMissDialog();
																						showToast("提交失败，请重新提交");
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
														}
													});
													
												}
												
											} else if(jsonObj.getString("code")
													.equals("-490226002")){
												disMissDialog();
												showToast(jsonObj.getString("msg"));
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
								e.printStackTrace();
							}
						}
						try {
							tInputStringStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					}

				}
				disMissDialog();

			}

		}
	}

}
