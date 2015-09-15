package com.myfp.myfund.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class ShortcutLoginPassActivity extends BaseActivity{

	private EditText shortcut_register_password;
	private EditText shortcut_register_nextpassword;
	private String code;
	public static ShortcutLoginPassActivity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_deal_password);
		instance=this;
		
	}

	@Override
	protected void initViews() {
		setTitle("登录密码");
		shortcut_register_password = (EditText) findViewById(R.id.shortcut_register_password);
		shortcut_register_nextpassword = (EditText) findViewById(R.id.shortcut_register_nextpassword);
		
		findViewAddListener(R.id.shortcut_register_affirm);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_register_affirm:
			if (shortcut_register_password.getText().toString().length()==0) {
				showToast("密码不能为空");
				return;
			}else if (shortcut_register_password.getText().toString().length()<6||shortcut_register_password.getText().toString().length()>12) {
				showToast("密码不能小于6位或大于12位");
				return;
			}
			else if (shortcut_register_nextpassword.getText().toString().length()==0) {
				showToast("请确认登陆密码");
				return;
			}else if (!shortcut_register_password.getText().toString().equals(shortcut_register_nextpassword.getText().toString())) {
				showToast("两次输入密码不一至，请重新输入");
				return;
			}
			else {
				RequestParams params = new RequestParams(this);
				params.put(RequestParams.MOBILE, getIntent().getStringExtra("Phone"));
				params.put(RequestParams.PASSWORD, shortcut_register_nextpassword.getText().toString());
				params.put(RequestParams.RegistFro, "AN_APP");
				execApi(ApiType.GET_POINTREGISTERTWO, params);
				
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力！");
			return;
		}
		if (api == ApiType.GET_POINTREGISTERTWO) {
			JSONObject object;
			try {
				object = new JSONObject(json);
				code = object.getString("Code");
				System.out.println("code----------->"+code);
				String Hint = object.getString("Hint");
				String Info = object.getString("Info");
				if (code.equals("0000")) {
					Intent intent=new Intent(this, ShortcutResgisterActivity.class);
					intent.putExtra("Phone", getIntent().getStringExtra("Phone"));
					startActivity(intent);
				}else if (code.equals("200")) {
					showToastLong("该手机号已经绑定过其他用户");
				}else if (code.equals("500")) {
					showToastLong("注册失败");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}

}
