package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.DESEncrypt;

public class BankSmallMoneyActivity extends BaseActivity{
	TextView tv_banksmallmoney_banktype,tv_banksmallmoney_bankidnum;
	Bundle bundle;
	public static BankSmallMoneyActivity instance = null;
	private String jsonstr,encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_banksmallmoney);
		instance = this;
		bundle = getIntent().getExtras();
		DESEncrypt desEpt = new DESEncrypt();
		try {
			encodePassWord = desEpt.encrypt(bundle.getString("LoginPass"));
//			encodePassWord = java.net.URLEncoder.encode(encodePassWord);
			System.out.println(encodePassWord);
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
			json.put("depositprov", bundle.getString("Province"));
			json.put("depositacctname",bundle.getString("RealName"));
			json.put("ismainback", "");
			json.put("officetelno", "");
			json.put("smallflag", "1");
			json.put("fax", "");
			json.put("nickname", "");
			json.put("bankname", bundle.getString("AccountAddress"));
			json.put("tpasswd", encodePassWord);
			json.put("annualincome", "");
			json.put("educationlevel", "");
			json.put("postcode", "");
			json.put("minorid", "");
			json.put("transactorname", "");
			json.put("familyname", "");
			json.put("country", "156");
			json.put("custfullname", bundle.getString("RealName"));
			json.put("depositacct", bundle.getString("BankCardId").toString());
			json.put("email", bundle.getString("Email"));
			json.put("delivertype", "1");
			json.put("signflag", "1");
			json.put("paycenterid",bundle.getString("PayCenterId"));
			json.put("lpasswd", encodePassWord);
			json.put("certificatetype", "0");
			json.put("sex", "");
			json.put("channelname", bundle.getString("BankType"));
			json.put("referral",bundle.getString("ServeCode") );
			json.put("telno", "");
			json.put("hometelno", "");
			json.put("depositcity", bundle.getString("City"));
			json.put("vocationcode", "");
			json.put("channelid", bundle.getString("ChannelId"));
			json.put("ismainpay", "");
			json.put("referralcityname", "");
			json.put("deliverway", "00000000");
			json.put("_", "");
			json.put("transactorcertno", "");
			json.put("custname", bundle.getString("RealName"));
			json.put("certificateno", bundle.getString("IDCard"));
			json.put("transactorvalidate", "");
			json.put("firstname", "");
			json.put("shsecuritiesaccountid", "");
			json.put("vailddate", "20591231");
			json.put("mobileno",bundle.getString("PhoneNum"));
			json.put("investorsbirthday",bundle.getString("IDCard").substring(6, 14));
			json.put("address", "未填写");
			json.put("transactorcertrefer", "");
			json.put("transactortel", "");
			json.put("custmanagerid", "");
			json.put("szsecuritiesaccountid", "");
			 
			jsonstr = json.toString();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("银行卡绑定");
		tv_banksmallmoney_banktype = (TextView)findViewById(R.id.tv_banksmallmoney_banktype);
		tv_banksmallmoney_bankidnum = (TextView)findViewById(R.id.tv_banksmallmoney_bankidnum);
		tv_banksmallmoney_banktype.setText(bundle.getString("BankType"));
		tv_banksmallmoney_bankidnum.setText(bundle.getString("BankCardId"));
		findViewAddListener(R.id.bt_banksmallmoney_getsmallmoney);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_banksmallmoney_getsmallmoney:
//			Intent intent = new Intent(BankSmallMoneyActivity.this,OpenAccountSuccessActivity.class);
//			startActivity(intent);	showProgressDialog("正在确认...");

			RequestParams params = new RequestParams(
					this);
			params.put("paramMap",java.net.URLEncoder.encode(jsonstr));
			execApi(ApiType.GET_OPENACCOUNT,
					params);
			showProgressDialog("正在加载");
			
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
			if (api == ApiType.GET_OPENACCOUNT) {

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
													
													RequestParams params = new RequestParams(
															this);
													params.put("depositacctprov",bundle.getString("Province"));
													params.put("certificateno",bundle.getString("IDCard"));
													params.put("depositacct",bundle.getString("BankCardId"));
													params.put("subbankno","8867");
													params.put("depositacctcity",bundle.getString("City"));
													params.put("depositacctname",bundle.getString("RealName"));
													params.put("channelid",bundle.getString("ChannelId"));
													params.put("certificatetype","0");
													execApi(ApiType.GET_SMALLMONEY,params);
													
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

			}else if (api == ApiType.GET_SMALLMONEY) {

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
												jsonObj = new JSONObject(xmlReturn);
												if(jsonObj.getString("code").equals("0000")){
													disMissDialog();
													showToast("小额打款成功");
													Intent intent = new Intent(BankSmallMoneyActivity.this,
															OpenAccountSuccessActivity.class);
													
													startActivity(intent);
													finish();
													
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
