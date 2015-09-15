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

public class MembershipPayActivity extends BaseActivity{

	private EditText editt_cardnumber;
	private EditText editt_passw;
	private String cardnumber;
	private String passw;
	private String username;
	private String name;
	private String mobile;
	private String code;
	private String hint;
	private String info;
	public static MembershipPayActivity instance=null;
	private String referrer;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_membercard);
		instance=this;
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		name = intent.getStringExtra("name");
		mobile = intent.getStringExtra("UMobile");
		referrer = intent.getStringExtra("Referrer");
		
	}

	@Override
	protected void initViews() {
		setTitle("会员卡支付");
		findViewAddListener(R.id.butt_confirmpay);
		editt_cardnumber = (EditText) findViewById(R.id.editt_cardnumber);
		editt_passw = (EditText) findViewById(R.id.editt_passw);
	}
	
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.butt_confirmpay:
			cardnumber = editt_cardnumber.getText().toString();
			passw = editt_passw.getText().toString();
			RequestParams params=new RequestParams(this);
			params.put("CardNum", cardnumber);
			params.put("Cardmi", passw);
			params.put("username", username);
			params.put("name", name);
			params.put("JiangLi", "一年会员/￥399");
			params.put("UMobile", mobile);
			params.put("PaymentMethod", "3");
			params.put("Amt", "399");
			if (referrer==null) {
				params.put("Referral", "");
			}else {
				params.put("Referral", referrer);
				
			}
			execApi(ApiType.GET_MEMDERSHIPPAY, params);
			showProgressDialog("正在加载");
			
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api==ApiType.GET_MEMDERSHIPPAY) {
			try {
				JSONObject object=new JSONObject(json);
				code = object.getString("Code");
				hint = object.getString("Hint");
				info = object.getString("Info");
				if (code.equals("0000")) {
					Intent intent=new Intent(this, CongratulationsSuccessActivity.class);
					startActivity(intent);
					
				}else if (code.equals("500")) {
					showToastLong("卡号或密码错误！");
					disMissDialog();
					return;
				}else if (code.equals("200")) {
					showToastLong("卡号已使用！");
					disMissDialog();
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		disMissDialog();
	}

}
