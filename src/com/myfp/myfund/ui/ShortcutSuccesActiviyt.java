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
import android.view.View.OnClickListener;
import android.widget.Button;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class ShortcutSuccesActiviyt extends BaseActivity{
	
	ByteArrayInputStream tInputStringStream = null;
	private String sessionid;
	private String certificateno,moneyaccount,customRiskLevel,countfund
	,depositacctname,channelid,depositacct,custno,totalfundmarketvalue,custname,certificatetype;
	private Button shortcut_butt_up;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_succes);
		RequestParams pam=new RequestParams(this);
		pam.put("DisplayName", getIntent().getStringExtra("DisplayName"));
		pam.put("Mobile", getIntent().getStringExtra("Mobile"));
		pam.put("IDcard", getIntent().getStringExtra("IDcard"));
		execApi(ApiType.GET_UPDATECRM, pam);
		RequestParams pms=new RequestParams(this);
		pms.put("idcard", getIntent().getStringExtra("IDcard"));
		execApi(ApiType.GET_STEPVERIFICATION, pms);
		
		
		
		
	}

	@Override
	protected void initViews() {
		setTitle("开户成功");
		shortcut_butt_up = (Button) findViewById(R.id.shortcut_butt_up);
		
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_butt_up:
			Intent intent=new Intent(this, MyActivityGroup.class);
			System.out.println("111111111111111111111111111111111111");
			intent.putExtra("IDCard", getIntent().getStringExtra("IDcard"));
			System.out.println("IDCard---->"+getIntent().getStringExtra("IDcard"));
			intent.putExtra("DisplayName", depositacctname);
			System.out.println("DisplayName---->"+depositacctname);
			intent.putExtra("UserName", getIntent().getStringExtra("DisplayName"));
			System.out.println("UserName---->"+getIntent().getStringExtra("DisplayName"));
			intent.putExtra("sessionid", sessionid);
			intent.putExtra("Mobile", getIntent().getStringExtra("Mobile"));
			System.out.println("Mobile---->"+getIntent().getStringExtra("Mobile"));
			intent.putExtra("CustomRiskLevel",customRiskLevel);
			System.out.println("customRiskLevel---->"+customRiskLevel);
			intent.putExtra("DepositacctName",depositacctname);
			System.out.println("depositacctname---->"+depositacctname);
			intent.putExtra("TotalFundMarketValue",totalfundmarketvalue);
			System.out.println("totalfundmarketvalue---->"+totalfundmarketvalue);
			intent.putExtra("CountFund",countfund);
			System.out.println("countfund---->"+countfund);
			intent.putExtra("certificateno", certificateno);
			System.out.println("certificateno---->"+certificateno);
			intent.putExtra("depositacct", depositacct);
			System.out.println("depositacct---->"+depositacct);
			intent.putExtra("certificatetype", certificatetype);
			System.out.println("countfund---->"+certificatetype);
			intent.putExtra("Moneyaccount", moneyaccount);
			System.out.println("countfund---->"+moneyaccount);
			intent.putExtra("Custno", custno);
			System.out.println("custno---->"+custno);
			intent.putExtra("channelid", channelid);
			System.out.println("channelid---->"+channelid);
			intent.putExtra("Flag", "5");
			
			System.out.println("2222222222222222222222222222222222222222");
			startActivity(intent);
			finish();
			ShortcutObligateActivity.instance.finish();
			ShortcutBindingActivity.instance.finish();
			ShortcutDealPassAcitvity.instance.finish();
			ShortcutStatusActivity.instance.finish();
			ShortcutResgisterActivity.instance.finish();
			ShortcutLoginPassActivity.instance.finish();
			ShortcutVerifyActivity.instance.finish();
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if ("".equals(json)||json==null) {
			showToast("网络不给力");
			return;
		}
		if (api==ApiType.GET_STEPVERIFICATION) {
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
									JSONObject jsonObj = new JSONObject(xmlReturn);
									sessionid = jsonObj.getString("sessionid");
									System.out.println("sessionid1------------>"+sessionid);
									 certificateno = jsonObj.getString("certificateno");
									 moneyaccount = jsonObj.getString("moneyaccount");
									  customRiskLevel = jsonObj.getString("risklevel");
									 countfund = jsonObj.getString("countfund");
									 depositacctname = jsonObj.getString("depositacctname");
									 channelid = jsonObj.getString("channelid");
									 depositacct = jsonObj.getString("depositacct");
									
									  custno = jsonObj.getString("custno");
									 totalfundmarketvalue = jsonObj.getString("totalfundmarketvalue");
									  custname = jsonObj.getString("custname");
									 certificatetype = jsonObj.getString("certificatetype");
									App.getContext().setIdCard(getIntent().getStringExtra("IDcard"));
									App.getContext().setUserName(getIntent().getStringExtra("DisplayName"));
									App.getContext().setDepositacctName(getIntent().getStringExtra("DisplayName"));
									App.getContext().setMobile(getIntent().getStringExtra("Mobile"));
									App.getContext().setSessionid(sessionid);
									RequestParams params = new RequestParams(this);
									params.put("sessionId", sessionid);
									params.put("papercode", "001");
									//params.put("pointList",java.net.URLEncoder.encode(pointList));
									params.put("pointList",java.net.URLEncoder.encode("2|2|2|2|2|2|2|2|2|2|2|2|2|2"));
									params.put("iscontinue", "1");
									//params.put("answer",java.net.URLEncoder.encode(answer));
									params.put("answer",java.net.URLEncoder.encode("undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined"));
									params.put("invtp", "1");
									execApi(ApiType.GET_RISKSUBMITTWO, params);

								} catch (Exception e) {
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
			
			
		}else if (api==ApiType.GET_RISKSUBMITTWO) {
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
											shortcut_butt_up.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													Intent intent=new Intent(ShortcutSuccesActiviyt.this, MyActivityGroup.class);
													System.out.println("111111111111111111111111111111111111");
													intent.putExtra("IDCard", getIntent().getStringExtra("IDcard"));
													System.out.println("IDCard---->"+getIntent().getStringExtra("IDcard"));
													intent.putExtra("DisplayName", depositacctname);
													System.out.println("DisplayName---->"+depositacctname);
													intent.putExtra("UserName", getIntent().getStringExtra("DisplayName"));
													System.out.println("UserName---->"+getIntent().getStringExtra("DisplayName"));
													intent.putExtra("sessionid", sessionid);
													intent.putExtra("Mobile", getIntent().getStringExtra("Mobile"));
													System.out.println("Mobile---->"+getIntent().getStringExtra("Mobile"));
													intent.putExtra("CustomRiskLevel",customRiskLevel);
													System.out.println("customRiskLevel---->"+customRiskLevel);
													intent.putExtra("DepositacctName",depositacctname);
													System.out.println("depositacctname---->"+depositacctname);
													intent.putExtra("TotalFundMarketValue",totalfundmarketvalue);
													System.out.println("totalfundmarketvalue---->"+totalfundmarketvalue);
													intent.putExtra("CountFund",countfund);
													System.out.println("countfund---->"+countfund);
													intent.putExtra("certificateno", certificateno);
													System.out.println("certificateno---->"+certificateno);
													intent.putExtra("depositacct", depositacct);
													System.out.println("depositacct---->"+depositacct);
													intent.putExtra("certificatetype", certificatetype);
													System.out.println("countfund---->"+certificatetype);
													intent.putExtra("Moneyaccount", moneyaccount);
													System.out.println("countfund---->"+moneyaccount);
													intent.putExtra("Custno", custno);
													System.out.println("custno---->"+custno);
													intent.putExtra("channelid", channelid);
													System.out.println("channelid---->"+channelid);
													intent.putExtra("Flag", "5");
													startActivity(intent);
													finish();
												}
											});
											
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
		
	}

}
