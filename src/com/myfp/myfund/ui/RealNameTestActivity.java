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
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class RealNameTestActivity extends BaseActivity{
	EditText et_realnametest_realname,et_realnametest_idcardnum,et_realnametest_mobile;
	ByteArrayInputStream tInputStringStream = null;
	Intent intent;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_realnametest);
		intent =  getIntent();
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("实名验证");
		et_realnametest_realname = (EditText)findViewById(R.id.et_realnametest_realname);
		et_realnametest_idcardnum = (EditText)findViewById(R.id.et_realnametest_idcardnum);
		et_realnametest_mobile = (EditText)findViewById(R.id.et_realnametest_mobile);
		findViewAddListener(R.id.bt_realnametest_nextstep);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_realnametest_nextstep:
			if(et_realnametest_realname.getText().toString().length()==0 ||et_realnametest_idcardnum.getText().toString().length()==0
			||et_realnametest_mobile.getText().toString().length()==0){
				showToast("请填写完整信息");
				return;
			}
			if (et_realnametest_realname.getText().toString()    
					.matches("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")){
				
				ByteArrayInputStream is = null;

				
					try { 
						is = new ByteArrayInputStream(et_realnametest_idcardnum
								.getText().toString().getBytes("ISO-8859-1"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
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
					if (idNumMatcher.matches()){
						if(et_realnametest_mobile.getText().toString().length()==11){ 
							
							showProgressDialog("正在确认...");

							RequestParams params = new RequestParams(
									this);

							params.put(
									"idcard",
									et_realnametest_idcardnum
											.getText()
											.toString());

							execApi(ApiType.GET_ACCOUNTINFO,
									params);
							
						}else{
							showToast("手机号格式不正确");
							return;
						}
						
					}else{
						showToast("身份证号格式不正确");
						return;
					}
				
			}else{
				showToast("姓名格式不正确");
				return;
				
			}
			
			
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
			if (api == ApiType.GET_ACCOUNTINFO ) {

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
										if(xmlReturn.trim().equals("{}")){
											disMissDialog();
											showToast("您还没开立基金交易账户");
											return;
											
										}else{
											try {
												JSONObject jsonobj = new JSONObject(xmlReturn);
												if(jsonobj.getString("custname").equals(et_realnametest_realname.getText().toString())&&
												   jsonobj.getString("certificateno").equals(et_realnametest_idcardnum.getText().toString())&&
												   jsonobj.getString("mobileno").equals(et_realnametest_mobile.getText().toString())){
													
													disMissDialog();
													Intent intent1 = new Intent(RealNameTestActivity.this,NameTestSuccessActivity.class);
													intent1.putExtra("UserName", intent.getStringExtra("UserName"));
													intent1.putExtra("CustName", jsonobj.getString("custname"));
													intent1.putExtra("IDCard", jsonobj.getString("certificateno"));
													intent1.putExtra("Mobile", jsonobj.getString("mobileno"));
													intent1.putExtra("BankCard", jsonobj.getString("depositacct"));
													intent1.putExtra("ChannelName", jsonobj.getString("channelname"));
													startActivity(intent1);
													
												}else if(!(jsonobj.getString("custname").equals(et_realnametest_realname.getText().toString()))){
													disMissDialog();
													Intent intent2 = new Intent(RealNameTestActivity.this,NameTestFailActivity.class);
													intent2.putExtra("Flag", "真实姓名不匹配！");
													startActivity(intent2);
													
												}else if(!(jsonobj.getString("mobileno").equals(et_realnametest_mobile.getText().toString()))){
													disMissDialog();
													Intent intent2 = new Intent(RealNameTestActivity.this,NameTestFailActivity.class);
													intent2.putExtra("Flag", "手机号码不匹配！");
													startActivity(intent2);
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
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
