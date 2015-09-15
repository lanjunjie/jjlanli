package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;


/**
 * 俱乐部申请页
 * @author Max.Zhao
 *
 */
public class JoinClubActivity extends BaseActivity {
	
	private EditText et_name,et_mobile,et_email,et_idCard,et_referrer;
	private TextView tv_sex;
	private String mobile,userName,name,sex,idCard,email,referrer;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_joinclub);
		
		userName = App.getContext().getUserName();
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		execApi(ApiType.GET_USER_INFO, params);
	}

	@Override
	protected void initViews() {
		setTitle("财立方俱乐部");
		et_name = (EditText) findViewById(R.id.et_joinClub_name);
		et_idCard = (EditText) findViewById(R.id.et_joinClub_identityCard);
		et_email = (EditText) findViewById(R.id.et_joinClub_email);
		et_mobile = (EditText) findViewById(R.id.et_joinClub_phone);
		et_referrer = (EditText) findViewById(R.id.et_joinClub_referrer);
		tv_sex = (TextView) findViewById(R.id.tv_joinClub_sex);
		tv_sex.setOnClickListener(this);
		findViewAddListener(R.id.button_joinclub_apply);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_joinClub_sex:
			Intent intent = new Intent(this,SexChoiceActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.button_joinclub_apply:
			applyJoin();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			sex = intent.getStringExtra("Sex");
			tv_sex.setText(sex);
		}
	}
	
	public boolean isComplete(){
		if (TextUtils.isEmpty(name)) {
			showToast("请填写姓名！");
			return false;
		}
		if (TextUtils.isEmpty(sex)) {
			showToast("请选择性别！");
			return false;
		}
		if (TextUtils.isEmpty(mobile)) {
			showToast("请填写手机！");
			return false;
		}
		if (TextUtils.isEmpty(idCard)) {
			showToast("请填写身份证！");
			return false;
		}
		if (TextUtils.isEmpty(email)) {
			showToast("请填写邮箱！");
			return false;
		}
		/*
		if (TextUtils.isEmpty(referrer)) {
			showToast("请填写推荐人！");
			return false;
		}
	    */
		
		return true;
	}
	
	
	


	private void applyJoin() {
		name = et_name.getText().toString();
		sex = tv_sex.getText().toString();
		mobile = et_mobile.getText().toString();
		idCard = et_idCard.getText().toString();
		email = et_email.getText().toString();
		referrer = et_referrer.getText().toString();
		
		if (!isComplete()) {
			return;
		}
		
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		params.put("Realname", name);
		params.put("Mobile", mobile);
		params.put("Sex", sex);
		params.put("IDCard", idCard);
		params.put("Email", email);
		params.put("Referral", referrer);
		execApi(ApiType.GET_CLUB_APPLY, params);
	}
	
	public void setTextAndEnable(String str,EditText et) {
		if (!TextUtils.isEmpty(str)) {
			et.setText(str);
			et.setEnabled(false);
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_USER_INFO) {
				JSONObject obj = array.getJSONObject(0);
				setTextAndEnable(obj.getString("DisplayName"), et_name);
				et_idCard.setText(obj.getString("IDCard"));
				et_email.setText(obj.getString("Email"));
				setTextAndEnable(obj.getString("Mobile"), et_mobile);
				String tempSex = obj.getString("Sex");
				if (!TextUtils.isEmpty(tempSex)) {
					tv_sex.setText(tempSex);
					//tv_sex.setEnabled(false);
				}
			} else if (api == ApiType.GET_CLUB_APPLY) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("申请提交成功！");
					Intent intent1 = new Intent(this,ClubVerifyActivity.class);
					intent1.putExtra("result", 1);
					startActivity(intent1);
					finish();
					break;
				case 1:
					showToast("手机号码错误！");
					break;
				case 2:
					showToast("身份证格式错误！");
					break;
				case 3:
					showToast("邮箱格式错误！");
					break;
				case 4:
					showToast("姓名包含特殊字符！");
					break;
				case 5:
					showToast("您已通过审核，欢迎进入财立方！");
					startActivity(ClubHomeActivity.class);
					finish();
					break;
				case 6:
					Intent intent2 = new Intent(this,ClubVerifyActivity.class);
					intent2.putExtra("result", 2);
					startActivity(intent2);
					finish();
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
