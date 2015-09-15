package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.IDcard;

public class ShortcutStatusActivity extends BaseActivity{

	private EditText shortcut_dredgede_name;
	private EditText shortcut_dredgede_idcard;
	ByteArrayInputStream tInputStringStream = null;
	private EditText shortcut_dredgede_code;
	public static ShortcutStatusActivity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_status);
		instance=this;
		
	}

	@Override
	protected void initViews() {
		setTitle("请完善身份信息");
		shortcut_dredgede_name = (EditText) findViewById(R.id.shortcut_dredgede_name);
		shortcut_dredgede_idcard = (EditText) findViewById(R.id.shortcut_dredgede_idcard);
		shortcut_dredgede_code = (EditText) findViewById(R.id.shortcut_dredgede_code);
		findViewAddListener(R.id.shortcut_dredgede_next);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_dredgede_next:
			verifyNameID();
			break;

		default:
			break;
		}
		
	}
	void verifyNameID(){
		if (!(shortcut_dredgede_name.getText().toString().length() == 0)) {
			if (shortcut_dredgede_name.getText().toString()
					.matches("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {
				if (!(shortcut_dredgede_idcard.getText().toString().length() == 0)) {

					ByteArrayInputStream is;

					try {
						is = new ByteArrayInputStream(shortcut_dredgede_idcard
								.getText().toString().getBytes("ISO-8859-1"));
						BufferedReader consoleReader = new BufferedReader(
								new InputStreamReader(is));
						String idNum = null;
						try {
							idNum = consoleReader.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
//						Pattern idNumPattern = Pattern
//								.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
						// 通过Pattern获得Matcher
//						Matcher idNumMatcher = idNumPattern.matcher(idNum);
						String ii = IDcard.IDCardValidate(idNum);
							
					if(ii==""){
						
						RequestParams params = new RequestParams(
								this);

						params.put(
								"certificateno",
								shortcut_dredgede_idcard.getText().toString());
						params.put(
								"depositacct",
								"");
						params.put(
								"certificatetype",
								"0");

						execApi(ApiType.GET_OPENACCOUNTSTATUS,
								params);
						
						
						} else {
							showToast(ii);
//							showToast("身份证号格式不不正确");
							return;
						}
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					showToast("身份证号为空");
					return;
				}
			} else {
				showToast("姓名格式不不正确");
				return;
			}

		} else {
			showToast("姓名为空");
			return;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力！");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_OPENACCOUNTSTATUS) {

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
										System.out.println("<><><><><><><><><>"+ xmlReturn);
										try {
											JSONObject jsonObj = new JSONObject(
													xmlReturn);
											if (jsonObj.getString("openstat")
													.equals("w")) {
												disMissDialog();
												//showToast(jsonObj
												//		.getString("backmsg"));
												Intent intent=new Intent(this, ShortcutDealPassAcitvity.class);
												intent.putExtra("Phone", getIntent().getStringExtra("Phone"));
												intent.putExtra("SDName", shortcut_dredgede_name.getText().toString());
												intent.putExtra("SDIdCard", shortcut_dredgede_idcard.getText().toString());
												intent.putExtra("Code", shortcut_dredgede_code.getText().toString());
												startActivity(intent);
												
												
											} else {
												disMissDialog();
												showToast(jsonObj
														.getString("backmsg"));
												return;
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

			}

		}
	}

}
