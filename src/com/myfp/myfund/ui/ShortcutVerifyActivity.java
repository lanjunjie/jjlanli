package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
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
import com.myfp.myfund.ui.view.TimeCountButton;

public class ShortcutVerifyActivity extends BaseActivity{
	
	private TimeCountButton new_register_gaincode;
	private String captcha;
	private EditText shortcut_register_code;
	ByteArrayInputStream tInputStringStream = null;
	private TimeCountButton shortcut_register_gaincode;
	public static ShortcutVerifyActivity instance = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_verify);
		instance=this;
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		shortcut_register_code = (EditText) findViewById(R.id.shortcut_register_code);
		shortcut_register_gaincode = (TimeCountButton) findViewAddListener(R.id.shortcut_register_gaincode);
		findViewAddListener(R.id.shortcut_nextonebu);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_register_gaincode:
			getCheckNum();
			
			break;
		case R.id.shortcut_nextonebu:
			if (shortcut_register_code.getText().toString().length()==0) {
				showToast("验证码不能为空");
			}else if (!shortcut_register_code.getText().toString().equals(captcha)) {
				showToast("验证码不正确");
			}
			else {
				Intent intent=new Intent(this, ShortcutLoginPassActivity.class);
				intent.putExtra("Phone", getIntent().getStringExtra("Phone"));
				startActivity(intent);
			}
			
			break;
		}
		
	}
	/**
	 * 获取验证码
	 */
	private void getCheckNum() {
		
		shortcut_register_gaincode.TimeStart();
		//TODO 获取验证码
		captcha = Math.round(Math.random()*8999+1000)+"";

		RequestParams params = new RequestParams(this);
		params.put("phone", getIntent().getStringExtra("Phone"));
		params.put("code", captcha.trim());
		params.put("tempid","dct_msg2".trim());
		execApi(ApiType.GET_CHECK_CODETWO, params);
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力！");
			return;
		}
		
	
			try {
				if (api == ApiType.GET_CHECK_CODETWO) {
					JSONArray array = new JSONArray(json);
					int result = array.getJSONObject(0).getInt("returnValue");
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
	}

}
