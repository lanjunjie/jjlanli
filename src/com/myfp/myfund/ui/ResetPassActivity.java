package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;

/**
 * 修改密码
 * 
 * @author Bruce.Wang
 */
public class ResetPassActivity extends BaseActivity {

	private EditText et_mobile, et_checknum, et_pass, et_repass;
	private String mobile, checknum, pass, repass;
	private String captcha;
	private TimeCountButton bt_checkNum;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_reset_pass);
	}

	@Override
	protected void initViews() {
		setTitle("修改密码");
		et_pass = (EditText) findViewById(R.id.et_pass1);
		et_repass = (EditText) findViewById(R.id.et_repass1);
		et_mobile = (EditText) findViewById(R.id.et_mobile1);
		et_checknum = (EditText) findViewById(R.id.et_checknum1);
		bt_checkNum = (TimeCountButton) findViewAddListener(R.id.bt_get_checknum1);
		findViewAddListener(R.id.bt_get_checknum1);
		findViewAddListener(R.id.but_reg);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.but_reg:
			// 确定
			confirm();
			break;
		case R.id.bt_get_checknum1:
			// 获取验证码
			getCheckNum();
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getCheckNum() {
		if (!checkVal(2)) {
			return;
		}
		bt_checkNum.TimeStart();
		// TODO 获取验证码
		captcha = Math.round(Math.random() * 8999 + 1000) + "";

		RequestParams params = new RequestParams(this);
		params.put("phone", mobile.trim());
		params.put("code",captcha.trim());
		params.put("tempid", "rset_pass".trim());
		//调用验证码接口
		execApi(ApiType.GET_CHECK_CODETWO, params);
	}

	/**
	 * 确定
	 */
	private void confirm() {
		if (!checkVal(1)) {
			return;
		}
		// TODO 调用忘记密码接口
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.MOBILE, mobile);
		params.put(RequestParams.PASSWORD, pass);
		execApi(ApiType.FORGET_PASSWORD, params);
	}

	private boolean checkVal(int type) {

		pass = et_pass.getText().toString();
		repass = et_repass.getText().toString();
		mobile = et_mobile.getText().toString();
		checknum = et_checknum.getText().toString();

		if (type == 1) {

			// 注册
			if (TextUtils.isEmpty(pass)) {
				showToast("请输入密码!");
				return false;
			}

			if (!pass.equals(repass)) {
				showToast("两次密码不一致!");
				return false;
			}

			if (TextUtils.isEmpty(checknum)) {
				showToast("请输入验证码!");
				return false;
			}
			if (!checknum.equals(captcha)) {
				showToast("验证码不正确!");
				return false;
			}
		} else if (type == 2) {
			// 获取验证码
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
			} else if (api == ApiType.FORGET_PASSWORD) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("修改成功!");
					finish();
					break;
				case 4:
					showToast("密码长度只能为6到16位!");
					break;
				case 5:
					showToast("手机号码未绑定!");
					break;
				case 6:
					showToast("用户名不存在!");
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
