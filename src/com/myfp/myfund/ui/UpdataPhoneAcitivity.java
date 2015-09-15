package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;

public class UpdataPhoneAcitivity extends BaseActivity {
	private String phone;
	private TimeCountButton updata_gaincode;
	private String captcha;
	private EditText edi_phone;
	private EditText new_updata_code;
	private EditText new_updata_traderpassword;
	private String updata_code;
	private String updata_traderpassword;
	private String iDCard;
	private String username;
	@Override
	protected void setContentView() {
		setContentView(R.layout.new_updata_phone);
		Intent intent = getIntent();
		iDCard = intent.getStringExtra("IDCard");
		username = intent.getStringExtra("username");
		
	}

	@Override
	protected void initViews() {
		setTitle("更改手机绑定");
		edi_phone = (EditText) findViewById(R.id.edi_phone);
		new_updata_code = (EditText) findViewById(R.id.new_updata_code);
	updata_gaincode = (TimeCountButton) findViewById(R.id.new_updata_gaincode);
		findViewAddListener(R.id.new_updata_gaincode);
		findViewAddListener(R.id.new_updata_affirm);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_updata_gaincode:
			getCheckNum();
			break;
		case R.id.new_updata_affirm:
			confirm();
			break;

		default:
			break;
		}
	}
	/**
	 * 调用修改绑定接口
	 * */
	private void confirm() {
		if (!checkVal(1)) {
			return;
		}
		// TODO 调用修改绑定接口
			RequestParams pms=new RequestParams(this);
			pms.put("UserName", username);
			pms.put("Mobile", edi_phone.getText().toString().trim());
			execApi(ApiType.UPDATE_Mobile, pms);
			
			RequestParams pams=new RequestParams(this);
			//身份证号
			pams.put("certificateno", iDCard);
			//pams.put("tpasswd", new_updata_traderpassword.getText().toString().trim());
			pams.put("mobileno", edi_phone.getText().toString().trim());
			execApi(ApiType.UPDATE_MOBILEID, pams);
	}
	
	/**
	 * 获取验证码
	 */
	private void getCheckNum() {
		if (!checkVal(2)) {
			return;
		}
		updata_gaincode.TimeStart();
		// TODO 获取验证码
		captcha = Math.round(Math.random() * 8999 + 1000) + "";

		RequestParams params = new RequestParams(this);
		params.put("phone", edi_phone.getText().toString());
		params.put("code",captcha.trim());
		params.put("tempid", "rset_pass".trim());
		//调用验证码接口
		execApi(ApiType.GET_CHECK_CODETWO, params);
	}
	private boolean checkVal(int type) {

		phone = edi_phone.getText().toString();
		updata_code = new_updata_code.getText().toString();
		//updata_traderpassword = new_updata_traderpassword.getText().toString();

		if (type == 1) {
			// 注册
			if (TextUtils.isEmpty(phone)) {
				showToast("请输入手机号码!");
				return false;
			}
			if (TextUtils.isEmpty(updata_code)) {
				showToast("请输入验证码!");
				return false;
			}
			if (!updata_code.equals(captcha)) {
				showToast("验证码不正确!");
				return false;
			}
			
		} else if (type == 2) {
			// 获取验证码
			if (TextUtils.isEmpty(phone)) {
				showToast("手机号码不能为空!");
				return false;
			}
		}

		return true;
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力!");
			return;
		}
		try {
			if (api == ApiType.GET_CHECK_CODETWO) {
				JSONArray array = new JSONArray(json);
				int result = array.getJSONObject(0).getInt("returnResult");
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
			} else if (api == ApiType.UPDATE_Mobile) {
				JSONArray array=new JSONArray(json);
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("手机号绑定成功!");
					finish();
					break;
				case 1:
					showToast("手机号为空!");
					break;
				case 2:
					showToast("手机号码正则不匹配!");
					break;
				case 3:
					showToast("手机号码已存在!");
					break;
				}
			}else if (api==ApiType.UPDATE_MOBILEID) {
				JSONObject object=new JSONObject(json);
				String ret = object.getString("ret_code");
				if (ret.equals("0000")) {
					showToast("手机绑定成功！");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
