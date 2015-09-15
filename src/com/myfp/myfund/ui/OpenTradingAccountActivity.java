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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.IDcard;

public class OpenTradingAccountActivity extends BaseActivity{

	private EditText new_dredgede_name;
	private EditText new_dredgede_idcard;
	private EditText new_dredgede_paddword;
	private EditText new_dredgede_nextpassword;
	private EditText new_dredgede_code;
	private String dredgede_name;
	private String dredgede_idcard;
	private String dredgede_paddword;
	private String dredgede_nextpassword;
	private String dredgede_code;
	private String phone;
	private CheckBox dredgede_readed;
	private TextView dredgede_clause;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_dredgedeal_account);
		Intent intent = getIntent();
		phone = intent.getStringExtra("Phone");
	}

	@Override
	protected void initViews() {
		setTitle("开通交易帐户");
		new_dredgede_name = (EditText) findViewById(R.id.new_dredgede_name);
		new_dredgede_idcard = (EditText) findViewById(R.id.new_dredgede_idcard);
		new_dredgede_paddword = (EditText) findViewById(R.id.new_dredgede_paddword);
		new_dredgede_nextpassword = (EditText) findViewById(R.id.new_dredgede_nextpassword);
		new_dredgede_code = (EditText) findViewById(R.id.new_dredgede_code);
		dredgede_readed = (CheckBox) findViewById(R.id.new_new_dredgede_readed);
		dredgede_readed.setChecked(true);
		dredgede_clause = (TextView) findViewById(R.id.new_dredgede_clause);
		dredgede_clause.setText(Html.fromHtml("<u>服务条款</u>"));
		findViewAddListener(R.id.new_dredgede_next);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_dredgede_next:
			nextStep();
			
			break;

		default:
			break;
		}
	}
	void nextStep() {
		if (!(new_dredgede_name.getText().toString().length() == 0)) {
			if (new_dredgede_name.getText().toString()
					.matches("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {
				if (!(new_dredgede_idcard.getText().toString().length() == 0)) {

					ByteArrayInputStream is;

					try {
						is = new ByteArrayInputStream(new_dredgede_idcard
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
							if (!(new_dredgede_paddword
											.getText().toString()
											.length() == 0)) {
										if (new_dredgede_paddword
												.getText().toString()
												.length() > 12
												|| new_dredgede_paddword
														.getText()
														.toString()
														.length() < 6) {
											showToast("密码长度为6~8位");
											return;
										} else {
											if (!(new_dredgede_nextpassword
													.getText()
													.toString()
													.length() == 0)) {
												if (new_dredgede_nextpassword
														.getText()
														.toString()
														.length() > 12
														|| new_dredgede_nextpassword
																.getText()
																.toString()
																.length() < 6) {
													showToast("确认密码长度为6~8位");
													return;
												} else {
													if (new_dredgede_paddword
															.getText()
															.toString()
															.equals(new_dredgede_nextpassword
																	.getText()
																	.toString())) {
														if (!(new_dredgede_code
																.getText()
																.toString()
																.length() == 0)) {
															if (dredgede_readed.isChecked()==false) {
																showToast("请先阅读并同意相关服务条款!");
																return ;
															}else {
																
																RequestParams params = new RequestParams(
																		this);

																params.put(
																		"certificateno",
																		new_dredgede_idcard.getText().toString());
																params.put(
																		"depositacct",
																		"");
																params.put(
																		"certificatetype",
																		"0");

																execApi(ApiType.GET_OPENACCOUNTSTATUS,
																		params);
																
															}
														} else {
															showToast("服务代码为空");
															return;
														}

													} else {
														showToast("两次密码输入不一致");
														return;
													}
												}

											} else {
												showToast("确认密码为空");
												return;
											}
										}
									} else {
										showToast("密码为空");
										return;
									}
						
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
										System.out.println("<><><><><><><><><>"
												+ xmlReturn);
										try {
											JSONObject jsonObj = new JSONObject(
													xmlReturn);
											if (jsonObj.getString("openstat")
													.equals("w")) {
												disMissDialog();
												showToast(jsonObj
														.getString("backmsg"));

												Intent intent = new Intent(OpenTradingAccountActivity.this,NewBindingBankActivity.class);
												Bundle bundle = new Bundle();
												
												bundle.putString("Name",
														new_dredgede_name
														.getText()
														.toString());
												bundle.putString("IDCard",
														new_dredgede_idcard
														.getText()
														.toString());
												bundle.putString("PaddWord", new_dredgede_paddword.getText().toString());
												bundle.putString("NextPassWord", new_dredgede_nextpassword.getText().toString());
												bundle.putString("Code", new_dredgede_code.getText().toString());
												bundle.putString("Phone", phone);
												intent.putExtras(bundle);
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
