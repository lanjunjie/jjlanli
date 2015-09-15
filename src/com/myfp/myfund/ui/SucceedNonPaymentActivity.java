package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;

import u.aly.br;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class SucceedNonPaymentActivity extends BaseActivity{
	private String username;
	private String mobile;
	private String displayName;
	private String iDCard;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_contributo_annuo);
		SharedPreferences preferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		username = preferences.getString("UserName", null);
		System.out.println("username==========>"+username);
		RequestParams params=new RequestParams(this);
		params.put("username", username);
		execApi(ApiType.GET_USER_INFO, params);
		
	}

	@Override
	protected void initViews() {
		setTitle("我的资产-基金配资");
		findViewAddListener(R.id.tex_pay);
		findViewAddListener(R.id.te_phone_call);
		findViewAddListener(R.id.te_admittance);
		findViewAddListener(R.id.te_notice);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tex_pay:
			//支付
			Intent intent4=new Intent(this, PaymentActivity.class);
			intent4.putExtra("Mobile", mobile);
			intent4.putExtra("DisplayName", displayName);
			intent4.putExtra("UserName", username);
			intent4.putExtra("IDCard", iDCard);
			startActivity(intent4);
			break;
		case R.id.te_phone_call:
			//电话
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);   
			intent.setData(Uri.parse("tel:010-56236258"));
			startActivity(intent);
			break;
		//会员准入条件
		case R.id.te_admittance:
			Intent intent2=new Intent(this, EntryCriteriaActivity.class);
			startActivity(intent2);
			
			break;
		//配资须知
		case R.id.te_notice:
			Intent intent3=new Intent(this, AndInstructionsActivity.class);
			startActivity(intent3);
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		super.onReceiveData(api, json);
		if (api == ApiType.GET_USER_INFO) {
			try {
				JSONArray array = new JSONArray(json);
				displayName = array.getJSONObject(0).getString("DisplayName");
				System.out.println("displayName===================>"+displayName);
				iDCard = array.getJSONObject(0).getString("IDCard");
				System.out.println("iDCard----------------->"+iDCard);
				mobile = array.getJSONObject(0).getString("Mobile");
				System.out.println("mobile-------------------->"+mobile);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
