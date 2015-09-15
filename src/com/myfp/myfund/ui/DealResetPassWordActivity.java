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
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.DESEncrypt;

public class DealResetPassWordActivity extends BaseActivity{
	private String newpass,newpassagain,certificateno,certificatetype,encodePassWord;
	EditText et_newpass,et_newpassagain;
	ByteArrayInputStream tInputStringStream = null;
	public static DealResetPassWordActivity instance = null;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealresetpassword);
		instance = this;
		Intent intent = getIntent();
		certificateno = intent.getStringExtra("PapersCode");
		certificatetype = intent.getStringExtra("CertificateType");
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("重置密码");
		et_newpass = (EditText)findViewById(R.id.et_newpass);
		et_newpassagain = (EditText)findViewById(R.id.et_newpassagain);
		findViewAddListener(R.id.bt_nextstep);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_nextstep:
			newpass = et_newpass.getText().toString().trim();
			newpassagain = et_newpassagain.getText().toString().trim();
			if(!(newpass.length() == 0)){
				if(!(newpassagain.length() == 0)){
					
					if(newpass.equals(newpassagain)){
						DESEncrypt desEpt = new DESEncrypt();
						try {
							encodePassWord = desEpt.encrypt(newpass);
							encodePassWord = java.net.URLEncoder.encode(encodePassWord);
							RequestParams params = new RequestParams(this);
							params.put("newpwd", encodePassWord);
							// params.put(RequestParams.MOBILE, username);
							params.put("certificateno",certificateno);
							params.put("accttype","");
							params.put("certificatetype",certificatetype);
							execApi(ApiType.GET_RESETPASS, params);
							showProgressDialog("正在确认");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else{
						
						showToast("两次密码不一致");
						return;
					}
					
				}else{
					showToast("确认密码为空");
					return;
				}
				
			}else{
				showToast("密码为空");
				return;
			}
			
			break;

		default:
			break;
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
			if (api == ApiType.GET_RESETPASS) {

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
											JSONObject jsonObj = new JSONObject(xmlReturn);
											if (xmlReturn.contains("appsheetserialno")) {
												
												Intent intent = new Intent(
														DealResetPassWordActivity.this,
														DealResetSuccessActivity.class);

												startActivity(intent);
											} else {
												disMissDialog();
												showToast(jsonObj.getString("msg"));
												return;
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										//String returnResult = jsonObj.getString("loginflag");
										

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
