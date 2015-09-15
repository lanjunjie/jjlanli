package com.myfp.myfund.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class OfflineReceiveActivity extends BaseActivity{

	private String username;
	private String code;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_binkhuikuan);
		username =App.getContext().getUserName();
		RequestParams params=new RequestParams(this);
		params.put("username", username);
		params.put("name", getIntent().getStringExtra("userName"));
		params.put("Level", getIntent().getStringExtra("op3_Amt"));
		params.put("UMobile", getIntent().getStringExtra("mobile"));
		params.put("PaymentMethod", "1");
		params.put("op3_Amt", getIntent().getStringExtra("op3_Amt"));
		params.put("Referral", getIntent().getStringExtra("Referral"));
		params.put("Pid", getIntent().getStringExtra("opid"));
		params.put("ApplyFrom", "APP");
		params.put("RoleID", getIntent().getStringExtra("form"));
		execApi(ApiType.GET_OFFLINERECIVE, params);
		
	}

	@Override
	protected void initViews() {
		setTitle("银行汇款");
		findViewAddListener(R.id.tex_offline_gs);
		findViewAddListener(R.id.bink_phone);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tex_offline_gs:
			if (code.equals("0000")) {
				showToast("预约成功！");
				finish();
				PurchaseHardActivity.instance.finish();
				FeatureDeatailedActivity.instance.finish();
			}
			break;
		case R.id.bink_phone:
		Intent intent1 = new Intent();
		intent1.setAction(Intent.ACTION_DIAL);
		intent1.setData(Uri.parse("tel:010-56236260"));
		startActivity(intent1);
		break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (api==ApiType.GET_OFFLINERECIVE) {
			try {
				JSONObject object=new JSONObject(json);
				code = object.getString("Code");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
