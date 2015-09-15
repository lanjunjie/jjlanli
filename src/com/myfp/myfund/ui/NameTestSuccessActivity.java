package com.myfp.myfund.ui;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class NameTestSuccessActivity extends BaseActivity{
	TextView tv_nametestsuccess_name,tv_nametestsuccess_mobile,tv_nametestsuccess_bankcard,tv_nametestsuccess_idcard;
	Intent intent;
	private String userName;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_nametestsuccess);
		intent = getIntent();
		SharedPreferences preferences = getSharedPreferences("Setting",MODE_PRIVATE);
		userName = preferences.getString("UserName", null);
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("实名验证");
		tv_nametestsuccess_name = (TextView)findViewById(R.id.tv_nametestsuccess_name);
		tv_nametestsuccess_mobile = (TextView)findViewById(R.id.tv_nametestsuccess_mobile);
		tv_nametestsuccess_bankcard = (TextView)findViewById(R.id.tv_nametestsuccess_bankcard);
		tv_nametestsuccess_idcard = (TextView)findViewById(R.id.tv_nametestsuccess_idcard);
		
		tv_nametestsuccess_name.setText(intent.getStringExtra("CustName"));
		tv_nametestsuccess_mobile.setText(intent.getStringExtra("Mobile"));
		tv_nametestsuccess_bankcard.setText("("+intent.getStringExtra("ChannelName")+")"+intent.getStringExtra("BankCard"));
		tv_nametestsuccess_idcard.setText(intent.getStringExtra("IDCard"));
		findViewAddListener(R.id.bt_nametestsuccess_confirm);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_nametestsuccess_confirm:
			showProgressDialog("正在提交");
			RequestParams params = new RequestParams(
					this);
			if (userName==null) {
				params.put("username",intent.getStringExtra("UserName").trim());
			}else {
				params.put("username",userName.trim());
			}
			params.put("mobile",intent.getStringExtra("Mobile"));
			params.put("displayname",intent.getStringExtra("CustName"));
			params.put("idcard",intent.getStringExtra("IDCard"));
			execApi(ApiType.GET_SAVENAMETESTINFO,
					params);
			
			break;

		default:
			break;
		}
		
		
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_SAVENAMETESTINFO ) {
				try {
					JSONObject jsonobj = new JSONObject(json);
					if(jsonobj.getString("Code").equals("0000")){
						disMissDialog();
						showToast("提交信息成功");
						Intent intent = new Intent(NameTestSuccessActivity.this,MyActivityGroup.class);
						startActivity(intent);
						finish();
					}else{
						disMissDialog();
						showToast("提交信息失败");
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
