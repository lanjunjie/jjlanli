package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class AccountInforActivity extends BaseActivity {
	TextView tv_accountinfor_realname, tv_accountinfor_username,
			tv_accountinfor_paperstype, tv_accountinfor_paperscode,
			tv_accountinfor_effectivedate, tv_accountinfor_postalcode,
			tv_accountinfor_postaladdress, tv_accountinfor_sendtype,
			tv_accountinfor_phonenum, tv_accountinfor_email;
	ByteArrayInputStream tInputStringStream = null;
	private String encodeIdCard, encodePassWord;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_accountinfor);

		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");

		RequestParams params = new RequestParams(this);
		params.put("id", encodeIdCard);
		// params.put(RequestParams.MOBILE, username);
		params.put("passwd", encodePassWord);
		execApi(ApiType.GET_ACCOUNTINFOR, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("账户基本信息");
		tv_accountinfor_realname = (TextView) findViewById(R.id.tv_accountinfor_realname);
		tv_accountinfor_paperstype = (TextView) findViewById(R.id.tv_accountinfor_paperstype);
		tv_accountinfor_paperscode = (TextView) findViewById(R.id.tv_accountinfor_paperscode);
		tv_accountinfor_effectivedate = (TextView) findViewById(R.id.tv_accountinfor_effectivedate);
		tv_accountinfor_postalcode = (TextView) findViewById(R.id.tv_accountinfor_postalcode);
		tv_accountinfor_postaladdress = (TextView) findViewById(R.id.tv_accountinfor_postaladdress);
		tv_accountinfor_sendtype = (TextView) findViewById(R.id.tv_accountinfor_sendtype);
		tv_accountinfor_phonenum = (TextView) findViewById(R.id.tv_accountinfor_phonenum);
		tv_accountinfor_email = (TextView) findViewById(R.id.tv_accountinfor_email);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_ACCOUNTINFOR) {

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
											tv_accountinfor_realname
													.setText(jsonObj
															.getString("custfullname"));
											tv_accountinfor_paperstype
													.setText("身份证");
											if (encodeIdCard.length() == 15) {
												tv_accountinfor_paperscode
														.setText(jsonObj
																.getString("certId15vByConfusion"));
											} else if (encodeIdCard.length() == 18) {
												tv_accountinfor_paperscode
														.setText(jsonObj
																.getString("certId18vByConfusion"));
											}
											tv_accountinfor_effectivedate
													.setText(jsonObj
															.getString("vailddate"));
											tv_accountinfor_postalcode
													.setText(jsonObj
															.getString("postcode"));
											tv_accountinfor_postaladdress
													.setText(jsonObj
															.getString("address"));
											if (jsonObj
													.getString("delivertype")
													.equals("1")) {
												tv_accountinfor_sendtype
														.setText("不寄送");
											} else if (jsonObj.getString(
													"delivertype").equals("2")) {
												tv_accountinfor_sendtype
														.setText("按月");
											} else if (jsonObj.getString(
													"delivertype").equals("3")) {
												tv_accountinfor_sendtype
														.setText("按季");
											} else if (jsonObj.getString(
													"delivertype").equals("4")) {
												tv_accountinfor_sendtype
														.setText("半年");
											} else if (jsonObj.getString(
													"delivertype").equals("5")) {
												tv_accountinfor_sendtype
														.setText("一年");
											}

											tv_accountinfor_phonenum
													.setText(jsonObj
															.getString("mobileno"));
											tv_accountinfor_email
													.setText(jsonObj
															.getString("email"));

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// String returnResult =
										// jsonObj.getString("loginflag");

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
