package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;

/**
 * 修改绑定手机
 * @author Bruce.Wang
 *
 */
public class ChangeMobileActivity extends BaseActivity {
	
	private EditText et_phone,et_checkNum;
	private String mobile,checkNum,captcha,userName;
	private TimeCountButton bt_checkNum;
	private Intent intent;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_change_mobile);
		
		intent = getIntent();
		userName = App.getContext().getUserName();
		
	}

	@Override
	protected void initViews() {
		setTitle("修改绑定手机");
		
		et_phone = (EditText) findViewById(R.id.et_mobile);
		et_checkNum = (EditText) findViewById(R.id.et_checknum);
		
		findViewAddListener(R.id.bt_changeMobile_confirm);
		bt_checkNum = (TimeCountButton) findViewAddListener(R.id.bt_get_checknum);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_changeMobile_confirm:
			checkNum = et_checkNum.getText().toString();
			if (TextUtils.isEmpty(checkNum)) {
				showToast("验证码不能为空！");
				return;
			}else if (!captcha.equals(checkNum)) {
				showToast("验证码不正确！");
				return;
			}
			ChangeMobile();
			break;
		case R.id.bt_get_checknum:
			mobile = et_phone.getText().toString();
			if (TextUtils.isEmpty(mobile)) {
				showToast("手机号码不能为空！");
				return;
			}
			getCheckNum();
			break;
		default:
			break;
		}
	}

	private void ChangeMobile() {
		showProgressDialog("正在提交");
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		params.put("Mobile", mobile);
		execApi(ApiType.UPDATE_Mobile, params);
	}

	private void getCheckNum() {
		
		bt_checkNum.TimeStart();
		//TODO 获取验证码
		captcha = Math.round(Math.random()*8999+1000)+"";
		
		RequestParams params = new RequestParams(this);
		params.put("phone", mobile.trim());
		params.put("code",captcha.trim());
		params.put("tempid", "dct_msg2".trim());
		execApi(ApiType.GET_CHECK_CODETWO, params);
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			disMissDialog();
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
				}else if (api == ApiType.UPDATE_Mobile) {
					int result = array.getJSONObject(0).getInt("ReturnResult");
					disMissDialog();
					switch (result) {
					case 0:
						showToast("修改成功!");
						intent.putExtra("Mobile", mobile);
						setResult(2, intent);
						finish();
						break;
					case 1:
						showToast("手机号码不能为空!");
						break;
					case 2:
						showToast("手机号码不正确!");
						break;
					case 3:
						showToast("手机号码已存在!");
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
}
