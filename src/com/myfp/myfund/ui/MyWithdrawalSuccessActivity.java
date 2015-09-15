package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class MyWithdrawalSuccessActivity extends BaseActivity{

	private String idcard;

	@Override
	protected void setContentView() {
		setContentView(R.layout.item_present_succeed);
		Intent intent = getIntent();
		idcard = intent.getStringExtra("idcard");
		
		
	}

	@Override
	protected void initViews() {
		setTitle("提现成功");
		findViewAddListener(R.id.but_confirm_ton);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.but_confirm_ton:
			Intent intent=new Intent(this, MyPropertyMemberActivity.class);
			intent.putExtra("idcard", idcard);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		
	}

}
