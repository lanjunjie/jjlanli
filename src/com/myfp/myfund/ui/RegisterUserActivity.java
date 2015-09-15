package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
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
import com.myfp.myfund.ui.view.TimeCountButton;

public class RegisterUserActivity extends BaseActivity{

	private EditText new_register_phone;
	private EditText new_register_code;
	private EditText new_register_password;
	private EditText new_register_nextpassword;
	private CheckBox fund_readed;
	private TextView tv_fund_clause;
	private TimeCountButton new_register_gaincode;
	private String captcha;
	private String register_phone;
	private String register_code;
	private String register_password;
	private String register_nextpassword;
	private String code;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_activity_register);
		
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		new_register_phone = (EditText) findViewById(R.id.new_register_phone);
		new_register_code = (EditText) findViewById(R.id.new_register_code);
		new_register_password = (EditText) findViewById(R.id.new_register_password);
		new_register_nextpassword = (EditText) findViewById(R.id.new_register_nextpassword);
		new_register_gaincode = (TimeCountButton) findViewAddListener(R.id.new_register_gaincode);
		findViewAddListener(R.id.new_register_affirm);
		fund_readed = (CheckBox) findViewById(R.id.cb_fund_readed);
		fund_readed.setChecked(true);
		tv_fund_clause = (TextView) findViewAddListener(R.id.tv_fund_clause);
		tv_fund_clause.setText(Html.fromHtml("<u>服务条款</u>"));
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_register_gaincode:
			// 获取验证码
		if (new_register_phone.getText().toString().length() == 0) {
				showToast("请输入手机号！");

		} else if (new_register_phone.getText().toString().length() != 11) {
				showToast("手机号码格式不正确!");

		} else {
			RequestParams params = new RequestParams(this);
			params.put("mobileno", new_register_phone.getText().toString());
			execApi(ApiType.GET_FINDMOBILENO, params);	
							
						}
			
			break;
		case R.id.new_register_affirm:
			regist();
			break;
		case R.id.tv_fund_clause:
			//服务条款
			startActivity(ClauseActivity.class);
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * 获取验证码
	 */
	private void getCheckNum() {
		if(!checkVal(2)){
			return;
		}
		
		new_register_gaincode.TimeStart();
		//TODO 获取验证码
		captcha = Math.round(Math.random()*8999+1000)+"";
		
		RequestParams params = new RequestParams(this);
		params.put("phone", register_phone.trim());
		params.put("code", captcha.trim());
		params.put("tempid","dct_msg2".trim());
		execApi(ApiType.GET_CHECK_CODETWO, params);
	}

	/**
	 * 注册
	 */
	private void regist() {
		if(!checkVal(1)){
			return;
		}
		//TODO 调用注册接口
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.MOBILE, register_phone);
		params.put(RequestParams.PASSWORD, register_password);
		params.put(RequestParams.RegistFro, "AN_APP");
		execApi(ApiType.GET_POINTREGISTERTWO, params);
		
	}

	private boolean checkVal(int type) {
		
		register_phone = new_register_phone.getText().toString();
		register_code = new_register_code.getText().toString();
		register_password = new_register_password.getText().toString();
		register_nextpassword = new_register_nextpassword.getText().toString();
		
		if(type == 1){
			
			//注册
			if (TextUtils.isEmpty(register_phone) || TextUtils.isEmpty(register_code)
					|| TextUtils.isEmpty(register_password) || TextUtils.isEmpty(register_nextpassword)) {
				showToast("请将资料填写完整!");
				return false;
			}
			
			if(!register_password.equals(register_nextpassword)){
				showToast("两次密码不一致!");
				return false;
			}
		
			if(TextUtils.isEmpty(register_code)){
				showToast("请输入验证码!");
				return false;
			}
			if (!register_code.equals(captcha)) {
				showToast("验证码不正确!");
				return false;
			}
			if (!fund_readed.isChecked()) {
				showToast("请先阅读并同意相关服务条款!");
				return false;
			}
		}else if(type == 2){
			//获取验证码
			if (TextUtils.isEmpty(register_phone)) {
				showToast("手机号码不能为空!");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力！");
			return;
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
	
			try {
				if (api == ApiType.GET_CHECK_CODETWO) {
					JSONArray array = new JSONArray(json);
					int result = array.getJSONObject(0).getInt("returnValue");
					//com.alibaba.fastjson.JSONArray result = JSON.parseArray(json).getJSONArray(0).getInt;
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
				}else if (api == ApiType.GET_POINTREGISTERTWO) {
					JSONObject object=new JSONObject(json);
					code = object.getString("Code");
					System.out.println("code----------->"+code);
					String Hint = object.getString("Hint");
					String Info = object.getString("Info");
					if (code.equals("0000")) {
						Intent intent=new Intent(this, ResgisterSuccessActivity.class);
						intent.putExtra("Phone", register_phone);
						startActivity(intent);
					}else if (code.equals("200")) {
						showToastLong("该手机号已经绑定过其他用户");
					}else if (code.equals("500")) {
						showToastLong("注册失败");
					}
					
				}	
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

}
