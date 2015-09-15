package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.DESEncrypt;


public class ResetPasswordActivity extends BaseActivity{
	private String encodeIdCard;
	private RequestParams params;
	private EditText ed_original_password;
	private EditText ed_New_Password;
	private EditText ed_Confirm_Password;
	private String originalpassword;
	private String nEWPASS2;
	private String cONFIOMPASS2;
	private String ianlpassword;
	private String newPass;
	public static ResetPasswordActivity instance = null;
	private String confiomPass;
	ByteArrayInputStream tInputStringStream = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_resetpass);
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
	
	}

	@Override
	protected void initViews() {
		setTitle("修改密码");
		ed_original_password=(EditText) findViewById(R.id.ed_original_password);
		ed_New_Password=(EditText) findViewById(R.id.ed_New_Password);
		ed_Confirm_Password=(EditText) findViewById(R.id.ed_Confirm_Password);
		findViewAddListener(R.id.bu_modification_button);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bu_modification_button:
			
			originalpassword = ed_original_password.getText().toString();
			newPass = ed_New_Password.getText().toString();
			confiomPass = ed_Confirm_Password.getText().toString();
			if (TextUtils.isEmpty(newPass)) {
				showToast("请输入密码!");
			}

			if (!newPass.equals(confiomPass)) {
				showToast("两次密码不一致!");
			}
			try {
				nEWPASS2 = DESEncrypt.encrypt(newPass);
				cONFIOMPASS2 = DESEncrypt.encrypt(confiomPass);
				ianlpassword = DESEncrypt.encrypt(originalpassword);
				params = new RequestParams(this);
				params.put("id",encodeIdCard.trim());
				params.put("passwdold",java.net.URLEncoder
						.encode(ianlpassword.trim()));
				params.put("pwdtype", "0");
				params.put("passwdnew", java.net.URLEncoder
						.encode(nEWPASS2.trim()));
				params.put("passwdconfirm",java.net.URLEncoder
						.encode(cONFIOMPASS2.trim()));
				execApi(ApiType.GET_CHANGE_PASSWORD, params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			showToast("修改失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_CHANGE_PASSWORD) {

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
										System.out.println("---------------------->"
												+ xmlReturn);
										JSONObject jsonObj = new JSONObject(xmlReturn);
										System.out.println("jsonObj-=-=-=-=-=-=-=-=-=>"+jsonObj);
										if (jsonObj.getString("code").equals("0000")) {
											
											disMissDialog();
											Intent intent = new Intent(ResetPasswordActivity.this,ModificationSucceedActivity.class);
											startActivity(intent);
											finish();

										} else {
											disMissDialog();
											showToast("修改失败！！");
											Intent intent = new Intent(ResetPasswordActivity.this,ResetPasswordActivity.class);
											startActivity(intent);
											finish();
											
										}

									} catch (Exception e) {
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
	