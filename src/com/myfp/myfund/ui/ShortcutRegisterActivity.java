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

public class ShortcutRegisterActivity extends BaseActivity{

	private EditText shortcut_register_phone;
	ByteArrayInputStream tInputStringStream = null;
	public static ShortcutRegisterActivity instance = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_register);
		instance=this;
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		shortcut_register_phone = (EditText) findViewById(R.id.shortcut_register_phone);
		findViewAddListener(R.id.shortcut_step);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_step:
			if (shortcut_register_phone.getText().toString().length()==0) {
				showToast("请输入手机号码");
			}else if (shortcut_register_phone.getText().toString().length()!=11) {
				showToast("手机号码不正确");
			}else {
				RequestParams params = new RequestParams(this);
				params.put("Mobile", shortcut_register_phone.getText().toString());
				execApi(ApiType.GET_CLIENTELEJUDGE, params);	
			}
			
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (api == ApiType.GET_CLIENTELEJUDGE) {
			JSONObject object;
			try {
				object = new JSONObject(json);
				String code = object.getString("Code");
				if (code.equals("500")) {
					Intent intent=new Intent(this, ShortcutVerifyActivity.class);
					intent.putExtra("Phone", shortcut_register_phone.getText().toString());
					startActivity(intent);
				}else {
					showToast("手机号已注册！");
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		/*	if (json != null && !json.equals("")) {
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
											Intent intent=new Intent(this, ShortcutVerifyActivity.class);
											intent.putExtra("Phone", shortcut_register_phone.getText().toString());
											startActivity(intent);
											
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

			}*/

		}
		
	}

}
