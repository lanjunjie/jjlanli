package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.DESEncrypt;

public class NewAffirmMessage extends BaseActivity{

	private Bundle bundle;
	private String encodePassWord;
	private String jsonstr;
	private TextView message_name;
	private TextView message_idcard;
	private TextView message_bindingbank;
	private TextView message_bankcard;
	private TextView message_accountpoint;
	ByteArrayInputStream tInputStringStream = null;
	private String newprovinces;
	private String phone;
	private String code;
	private String provinces;
	private String idCard;
	private String name;
	private String channelid;
	private String custno;
	private String moneyaccount;
	private String depositacct;
	private String certificatetype;
	private String sessionId;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_affirm_message);
		bundle = getIntent().getExtras();
		newprovinces = bundle.getString("NewProvinces");
		
		code = bundle.getString("Code");
		provinces = bundle.getString("NewProvinces");
		idCard = bundle.getString("IDCard");
		name = bundle.getString("Name");
		channelid = bundle.getString("ChanneLid");
		System.out.println("手机号4------>"+phone);
		System.out.println("provinces------>"+provinces);
		SharedPreferences sPreferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		if (bundle.getString("Phone")!=null) {
			phone = bundle.getString("Phone");
		}else if (sPreferences.getString("Phone", null)!=null) {
			phone = sPreferences.getString("Phone", null);
		}
		
		
		System.out.println("newprovinces===>"+newprovinces);
		DESEncrypt desEpt = new DESEncrypt();
		try {
			encodePassWord = desEpt.encrypt(bundle.getString("PaddWord"));
//			encodePassWord = java.net.URLEncoder.encode(encodePassWord);
			System.out.println("encodePassWord============>"+encodePassWord);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		encodePassWord = java.net.URLEncoder
//				.encode(encodePassWord);
		
		JSONObject json = new JSONObject();
		try {
			
			json.put("referralprovincename", "");
			json.put("transactorcerttype", "");
			json.put("minorflag", "");
			json.put("referralmobile", "");
			json.put("depositprov", provinces);
			json.put("depositacctname",bundle.getString("Name"));
			json.put("ismainback", "");
			json.put("officetelno", "");
			json.put("smallflag", "1");
			json.put("fax", "");
			json.put("nickname", "");
			json.put("bankname", bundle.getString("NewOpeningbank"));
			json.put("tpasswd", encodePassWord);
			json.put("annualincome", "");
			json.put("educationlevel", "");
			json.put("postcode", "");
			json.put("minorid", "");
			json.put("transactorname", "");
			json.put("familyname", "");
			json.put("country", "156");
			json.put("custfullname", bundle.getString("Name"));
			json.put("depositacct", bundle.getString("NewImportcard").toString());
			json.put("email", "");
			json.put("delivertype", "1");
			json.put("signflag", "1");
			json.put("paycenterid",bundle.getString("PayCenterId"));
			json.put("lpasswd", encodePassWord);
			json.put("certificatetype", "0");
			json.put("sex", "");
			json.put("channelname", bundle.getString("NewDBK"));
			json.put("referral",code);
			json.put("telno", "");
			json.put("hometelno", "");
			json.put("depositcity", bundle.getString("NewCity"));
			json.put("vocationcode", "");
			json.put("channelid", bundle.getString("ChanneLid"));
			json.put("ismainpay", "");
			json.put("referralcityname", "");
			json.put("deliverway", "00000000");
			json.put("_", "");
			json.put("transactorcertno", "");
			json.put("custname", bundle.getString("Name"));
			json.put("certificateno", bundle.getString("IDCard"));
			json.put("transactorvalidate", "");
			json.put("firstname", "");
			json.put("shsecuritiesaccountid", "");
			json.put("vailddate", "20591231");
			//手机号通过接口加载
			System.out.println("手机号5---->"+phone);
			json.put("mobileno",phone);
			json.put("investorsbirthday",bundle.getString("IDCard").substring(6, 14));
			json.put("address", "未填写");
			json.put("transactorcertrefer", "");
			json.put("transactortel", "");
			json.put("custmanagerid", "");
			json.put("szsecuritiesaccountid", "");
			 
			jsonstr = json.toString();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initViews() {
		setTitle("信息确认");
		message_name = (TextView) findViewById(R.id.new_message_name);
		message_idcard = (TextView) findViewById(R.id.new_message_idcard);
		message_bindingbank = (TextView) findViewById(R.id.new_message_bindingbank);
		message_bankcard = (TextView) findViewById(R.id.new_message_bankcard);
		message_accountpoint = (TextView) findViewById(R.id.new_message_accountpoint);
		findViewAddListener(R.id.butt_verify_on);
		message_name.setText(bundle.getString("Name"));
		message_idcard.setText(bundle.getString("IDCard"));
		message_bindingbank.setText(bundle.getString("NewDBK"));
		message_bankcard.setText(bundle.getString("NewImportcard"));
		message_accountpoint.setText(bundle.getString("NewOpeningbank"));
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.butt_verify_on:

			RequestParams params = new RequestParams(this);
			params.put("paramMap",java.net.URLEncoder.encode(jsonstr));
			execApi(ApiType.GET_OPENACCOUNT2,params);
			RequestParams pam=new RequestParams(this);
			pam.put("DisplayName", bundle.getString("Name"));
			pam.put("Mobile", bundle.getString("Phone"));
			pam.put("IDcard", bundle.getString("IDCard"));
			execApi(ApiType.GET_UPDATECRM, pam);
			showProgressDialog("正在加载");
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
			if (api == ApiType.GET_OPENACCOUNT2) {

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
										System.out.println("<><><><><><><><><>"
												+ xmlReturn);
										if(xmlReturn.contains("appsheetserialno")){
											JSONObject jsonObj;
											try {
												jsonObj = new JSONObject(
														xmlReturn);
												if(jsonObj.getString("appsheetserialno").length()==0){
													disMissDialog();
													showToast("客户签约失败");
													return;
												}else{
													RequestParams params = new RequestParams(this);
													params.put("depositacctprov",bundle.getString("NewProvinces"));
													params.put("certificateno",bundle.getString("IDCard"));
													params.put("depositacct",bundle.getString("NewImportcard"));
													params.put("subbankno","8867");
													params.put("depositacctcity",bundle.getString("NewCity"));
													params.put("depositacctname",bundle.getString("Name"));
													params.put("channelid",bundle.getString("ChanneLid"));
													params.put("certificatetype","0");
													execApi(ApiType.GET_SMALLMONEY2,params);
												
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}else{
											JSONObject jsonObj;
											try {
												jsonObj = new JSONObject(
														xmlReturn);
												disMissDialog();
												showToast(jsonObj.getString("msg"));
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											return;
											
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
			else if (api == ApiType.GET_SMALLMONEY2) {

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
										if(xmlReturn.contains("code")){
											JSONObject jsonObj;
											try {
												jsonObj = new JSONObject(
														xmlReturn);
												if(jsonObj.getString("code").equals("0000")){
													disMissDialog();
													showToast("小额打款成功");
													RequestParams pms=new RequestParams(this);
													pms.put("DisplayName", name);
													pms.put("IDcard", idCard);
													pms.put("Mobile", phone);
													execApi(ApiType.GET_UPDATEMESSAGE, pms);
													RequestParams pas=new RequestParams(this);
													pas.put("idcard", idCard);
													execApi(ApiType.GET_STEPVERIFICATION, pas, new OnDataReceivedListener() {
														
														private String customRiskLevel;
														private String depositacctname;
														private String totalfundmarketvalue;
														private String countfund;

														@Override
														public void onReceiveData(ApiType api, String json) {
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
																					JSONObject jsonObj;
																					try {
																						jsonObj = new JSONObject(
																									xmlReturn);
																						System.out.println("object====>"+jsonObj);
																						custno = jsonObj.getString("custno");
																						System.out.println("custno=====>"+custno);
																						moneyaccount = jsonObj.getString("moneyaccount");
																						System.out.println("moneyaccount----->"+moneyaccount);
																						depositacct = jsonObj.getString("depositacct");
																						System.out.println("depositacct----->"+depositacct);
																						certificatetype = jsonObj.getString("certificatetype");
																						System.out.println("certificatetype----->"+certificatetype);
																						depositacctname = jsonObj.getString("depositacctname");
																						totalfundmarketvalue = jsonObj.getString("totalfundmarketvalue");
																						countfund = jsonObj.getString("countfund");
																						sessionId = jsonObj.getString("sessionid");
																						customRiskLevel = jsonObj.getString("risklevel");
																						Intent intent = new Intent(NewAffirmMessage.this,NewParagRaphActivity.class);
																						intent.putExtra("IdCard", idCard);
																						intent.putExtra("Name", name);
																						intent.putExtra("Custno", custno);
																						intent.putExtra("Channelid", channelid);
																						intent.putExtra("MoneyAccount", moneyaccount);
																						intent.putExtra("Depositacct", depositacct);
																						intent.putExtra("CertificateType", certificatetype);
																						intent.putExtra("sessionid", sessionId);
																						intent.putExtra("CustomRiskLevel", customRiskLevel);
																						intent.putExtra("DepositacctName", depositacctname);
																						intent.putExtra("TotalFundMarketValue", totalfundmarketvalue);
																						intent.putExtra("CountFund", countfund);
																						startActivity(intent);
																						
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
														}
													});
													
													
												
													
												}else{
													
													disMissDialog();
													showToast(jsonObj.getString("msg"));
													return;
													
													
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}else{
											
											disMissDialog();
											showToast("小额失败");
											return;
											
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

			
		}
		}

}
