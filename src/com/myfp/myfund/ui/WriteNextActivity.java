package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;
import com.myfp.myfund.utils.IDcard;

public class WriteNextActivity extends BaseActivity {
	private EditText et_writeifor_realname, et_writeifor_idcard,
			et_writeifor_phonenum, et_checknum;
	private String captcha;
	
	private TimeCountButton bt_checkNum;
	ByteArrayInputStream tInputStringStream = null;
	static WriteNextActivity instance = null;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_yanzheng);
		instance=this;
		// instance = this;
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("填写信息");
		et_writeifor_realname = (EditText) findViewById(R.id.et_writeifor_realname);
		et_writeifor_idcard = (EditText) findViewById(R.id.et_writeifor_idcard);
		et_writeifor_phonenum = (EditText) findViewById(R.id.et_writeifor_phonenum);
		et_checknum = (EditText) findViewAddListener(R.id.etchecknum);
		bt_checkNum = (TimeCountButton) findViewAddListener(R.id.bt_getchecknum);
		// mobile = et_writeifor_phonenum.getText().toString();
		// checknum = et_checknum.getText().toString();
		findViewAddListener(R.id.bt_enextstep);
		findViewAddListener(R.id.bt_getchecknum);
	}
      
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_enextstep:
			nextStep();
			break;
		case R.id.bt_getchecknum:
			
			// 获取验证码
			if (et_writeifor_phonenum.getText().toString().length() == 0) {
				showToast("请输入手机号！");

			} else if (et_writeifor_phonenum.getText().toString().length() != 11) {
				showToast("手机号码格式不正确!");

			} else {
				RequestParams params = new RequestParams(this);
				params.put("mobileno", et_writeifor_phonenum.getText().toString());
				execApi(ApiType.GET_FINDMOBILENO, params);	
				
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 获取验证码
	 */
	private void getCheckNum() {

		bt_checkNum.TimeStart();
		// TODO 获取验证码
		captcha = Math.round(Math.random() * 8999 + 1000) + "";

		RequestParams params = new RequestParams(this);
		params.put("phone", et_writeifor_phonenum.getText().toString());
		params.put("code", captcha.trim());
		params.put("tempid", "dct_msg2".trim());
		execApi(ApiType.GET_CHECK_CODETWO, params);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_CHECK_CODETWO) {
				int result = array.getJSONObject(0).getInt("returnValue");
				// com.alibaba.fastjson.JSONArray result =
				// JSON.parseArray(json).getJSONArray(0).getInt;
				switch (result) {
				case 0:
					showToast("发送成功!");
					break;
				case -4:
					showToast("手机号码格式不正确!");
					break;
				case -9:
					showToast("发送失败，请重新发送!");
					break;
				default:
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (api == ApiType.GET_FINDMOBILENO) {

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
										System.out.println("1111111111111111"+jsonObj.getString("msg"));
										if (jsonObj.getString("msg")
												.equals("1")) {
											
											showToast("手机号已注册！");
										
										} else {
										
											getCheckNum();
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

	void nextStep() {
		if (!(et_writeifor_realname.getText().toString().length() == 0)) {
			if (et_writeifor_realname.getText().toString()
					.matches("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {
				if (!(et_writeifor_idcard.getText().toString().length() == 0)) {

					ByteArrayInputStream is;

					try {
						is = new ByteArrayInputStream(et_writeifor_idcard
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
							if (!(et_writeifor_phonenum.getText().toString()
									.length() == 0)) {
								if (et_writeifor_phonenum.getText().toString()
										.length() == 11) {
									if (!TextUtils.isEmpty(et_checknum
											.getText().toString())) {
										if (et_checknum.getText().toString()
												.equals(captcha)) {

											Intent intent = new Intent(
													WriteNextActivity.this,
													WriteInforActivity.class);
											Bundle bundle = new Bundle();
											bundle.putString("RealName",
													et_writeifor_realname
															.getText()
															.toString());
											bundle.putString("IDCard",
													et_writeifor_idcard
															.getText()
															.toString());
											bundle.putString("PhoneNum",
													et_writeifor_phonenum
															.getText()
															.toString());
											intent.putExtras(bundle);
											startActivity(intent);
										} else {
											showToast("验证码不正确!");
										}
									} else {
										showToast("请输入验证码!");

									}
								} else {
									showToast("手机号码格式不正确");
									return;
								}

							} else {
								showToast("手机号码为空");
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
}
