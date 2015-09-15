package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;

/**
 * 注册界面
 * @author Bruce.Wang
 *
 */
public class RegistActivity extends BaseActivity {

	private EditText et_nickName;
	private EditText et_pass;
	private EditText et_repass;
	private EditText et_mobile;
	private EditText et_checknum;
	private String nickName;
	private String pass,repass;
	private String mobile;
	private String checknum;
	private CheckBox readed;
	private TextView tv_clause;
	private String captcha;
	private TimeCountButton bt_checkNum;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		et_nickName = (EditText) findViewById(R.id.et_nick_name);
		et_pass = (EditText) findViewById(R.id.et_pass);
		et_repass = (EditText) findViewById(R.id.et_repass);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_checknum = (EditText) findViewById(R.id.et_checknum);
		bt_checkNum = (TimeCountButton) findViewAddListener(R.id.bt_get_checknum);
		findViewAddListener(R.id.bt_reg);
		readed = (CheckBox) findViewById(R.id.cb_readed);
		readed.setChecked(true);
		tv_clause = (TextView) findViewAddListener(R.id.tv_clause);
		tv_clause.setText(Html.fromHtml("<u>服务条款</u>"));
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_reg:
			//注册
			regist();
			break;
		case R.id.bt_get_checknum:
			//获取验证码
			getCheckNum();
			break;
		case R.id.tv_clause:
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
		
		bt_checkNum.TimeStart();
		//TODO 获取验证码
		captcha = Math.round(Math.random()*8999+1000)+"";
		
		RequestParams params = new RequestParams(this);
		params.put("phone", mobile.trim());
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
		params.put(RequestParams.USERNAME, nickName);
		params.put(RequestParams.MOBILE, mobile);
		params.put(RequestParams.PASSWORD, pass);
		execApi(ApiType.GET_USERREGIST, params);
	}

	private boolean checkVal(int type) {
		
		nickName = et_nickName.getText().toString();
		pass = et_pass.getText().toString();
		repass = et_repass.getText().toString();
		mobile = et_mobile.getText().toString();
		checknum = et_checknum.getText().toString();
		
		if(type == 1){
			
			//注册
			if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(pass)
					|| TextUtils.isEmpty(repass) || TextUtils.isEmpty(mobile)) {
				showToast("请将资料填写完整!");
				return false;
			}
			
			if(!pass.equals(repass)){
				showToast("两次密码不一致!");
				return false;
			}
		
			if(TextUtils.isEmpty(checknum)){
				showToast("请输入验证码!");
				return false;
			}
			if (!checknum.equals(captcha)) {
				showToast("验证码不正确!");
				return false;
			}
			if (!readed.isChecked()) {
				showToast("请先阅读并同意相关服务条款!");
				return false;
			}
		}else if(type == 2){
			//获取验证码
			if (TextUtils.isEmpty(mobile)) {
				showToast("手机号码不能为空!");
				return false;
			}
		}
		
		return true;
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
				}else if (api == ApiType.GET_USERREGIST) {
					int result = array.getJSONObject(0).getInt("ReturnResult");
					switch (result) {
					case 0:
						showToast("注册成功!");
						Intent intent = new Intent(RegistActivity.this,RegistSuccessActivity.class);
						intent.putExtra("UserName", nickName);
						startActivity(intent);
						finish();
						break;
					case 3:
						showToast("密码长度只能为6到16位!");
						break;
					case 6:
						showToast("用户名已存在!");
						break;
					case 7:
						Intent intent1=new Intent(RegistActivity.this, NumberRegisteredActivity.class);
						startActivity(intent1);
						showToast("手机号码已存在!");
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
}
