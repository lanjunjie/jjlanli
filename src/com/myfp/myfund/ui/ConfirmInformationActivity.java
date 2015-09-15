package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;

public class ConfirmInformationActivity extends BaseActivity {
	EditText et_paperscode, et_realname, et_phonenum, et_checknumcode;
	ByteArrayInputStream tInputStringStream = null;
	private String custno, random;
	private TimeCountButton bt_get_checknumcode;
	private Button bt_forget_comfirm;
	public static ConfirmInformationActivity instance = null;
	private String tar;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_confirminformation);
		tar = getIntent().getStringExtra("tar");
		System.out.println("tar------->"+tar);
		instance = this;
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("验证信息");
		et_paperscode = (EditText) findViewById(R.id.et_paperscode);
		et_realname = (EditText) findViewById(R.id.et_realname);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		et_checknumcode = (EditText) findViewById(R.id.et_checknumcode);
		bt_get_checknumcode = (TimeCountButton) findViewAddListener(R.id.bt_get_checknumcode);
		bt_forget_comfirm = (Button) findViewById(R.id.bt_forget_comfirm);

		findViewAddListener(R.id.bt_forget_comfirm);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_get_checknumcode:
			// 获取验证码
			if (tar.equals("true")) {
				checkType();
			}else if(tar.equals("false")){
				checkInputType();
				
			}
			et_checknumcode.setVisibility(View.VISIBLE);
			bt_forget_comfirm.setVisibility(View.VISIBLE);

			break;
		case R.id.bt_forget_comfirm:
			// 下一步
			if (et_checknumcode.getText().toString().equals("")) {
				showToast("验证码错误！！");
				return;

			} else {
				if (et_checknumcode.getText().toString().trim()
						.equals(random)) {

					Intent intent = new Intent(ConfirmInformationActivity.this,
							DealResetPassWordActivity.class);
					intent.putExtra("CheckNumCode", et_checknumcode.getText()
							.toString());
					intent.putExtra("PapersCode", et_paperscode.getText()
							.toString());
					intent.putExtra("CertificateType", "0");
					startActivity(intent);
				} else {
					showToast("验证码错误！！");
					return;
				}
			}

			break;

		default:
			break;
		}

	}

	// 得到验证码
	void getCheckNum() {

		bt_get_checknumcode.TimeStart();
		// TODO 获取验证码
		// captcha = Math.round(Math.random() * 8999 + 1000) + "";

		RequestParams params = new RequestParams(this);
		params.put("certificateno", et_paperscode.getText().toString());
		// params.put(RequestParams.MOBILE, username);
		params.put("custname", et_realname.getText().toString());
		params.put("certificatetype", "0");
		params.put("mobileno", et_phonenum.getText().toString());
		execApi(ApiType.GET_CHECKINFO, params);

	}

	// 判断输入信息格式
	void checkInputType() {
		if (et_paperscode.getText().toString().length() == 0) {
			showToast("证件号码不能为空！");
			return;

		} else if (!App.getContext().getIdCard().equals(et_paperscode.getText().toString())) {
			showToast("您输入的身份证号与本人的不匹配，请重新输入！");
			return;
		}else{
			if (et_realname.getText().toString().length() == 0) {
				showToast("姓名不能为空！");
				return;
			} else {
				if (et_phonenum.getText().toString().length() == 0) {
					showToast("用户不存在或者手机号输入错误");
					return;
				} else {
					ByteArrayInputStream is;
					try {
						is = new ByteArrayInputStream(et_paperscode.getText()
								.toString().getBytes("ISO-8859-1"));
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
						Pattern idNumPattern = Pattern
								.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
						// 通过Pattern获得Matcher
						Matcher idNumMatcher = idNumPattern.matcher(idNum);
						if (idNumMatcher.matches()) {

							if (et_realname
									.getText()
									.toString()
									.matches(
											"[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {

								if (et_phonenum
										.getText()
										.toString()
										.matches(
												"^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$")) {

									getCheckNum();

								} else {
									showToast("手机号码格式不正确！");
									return;
								}

							} else {
								showToast("姓名格式不正确！");
								return;
							}
						} else {

							showToast("证件号码格式不正确！");
							return;
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}
	
	void checkType() {
		if (et_paperscode.getText().toString().length() == 0) {
			showToast("证件号码不能为空！");
			return;

		}else{
			if (et_realname.getText().toString().length() == 0) {
				showToast("姓名不能为空！");
				return;
			} else {
				if (et_phonenum.getText().toString().length() == 0) {
					showToast("手机号码不能为空！");
					return;
				} else {
					ByteArrayInputStream is;
					try {
						is = new ByteArrayInputStream(et_paperscode.getText()
								.toString().getBytes("ISO-8859-1"));
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
						Pattern idNumPattern = Pattern
								.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
						// 通过Pattern获得Matcher
						Matcher idNumMatcher = idNumPattern.matcher(idNum);
						if (idNumMatcher.matches()) {

							if (et_realname
									.getText()
									.toString()
									.matches(
											"[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {

								if (et_phonenum
										.getText()
										.toString()
										.matches(
												"^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$")) {

									getCheckNum();

								} else {
									showToast("手机号码格式不正确！");
									return;
								}

							} else {
								showToast("姓名格式不正确！");
								return;
							}
						} else {

							showToast("证件号码格式不正确！");
							return;
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_CHECKINFO) {

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
											if (xmlReturn.contains("custno")) {

												custno = jsonObj
														.getString("custno");
												execApi(ApiType.GET_RANDOMBUM,
														null);

											} else {
												disMissDialog();
												showToast(jsonObj
														.getString("msg"));
												return;
											}
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

			} else if (api == ApiType.GET_RANDOMBUM) {

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
											random = jsonObj.getString("msg");
											if (!(random.length() == 0)) {
												et_paperscode.setEnabled(false);
												et_realname.setEnabled(false);
												et_phonenum.setEnabled(false);
												RequestParams params = new RequestParams(
														this);
												params.put("mobileno",
														et_phonenum.getText()
																.toString());
												// params.put(RequestParams.MOBILE,
												// username);
												params.put("saveInfo", null);
												params.put("custno", custno);
												params.put("Random", random);
												execApi(ApiType.GET_SENDMSG,
														params);
											} else {
												disMissDialog();
												showToast("获取验证码失败！！");
												return;
											}
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

			} else if (api == ApiType.GET_CHECKINFO) {

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
