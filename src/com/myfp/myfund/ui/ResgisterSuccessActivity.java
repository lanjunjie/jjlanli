package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ResgisterSuccessActivity extends BaseActivity{

	private String phone;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_resgister_success);
		Intent intent = getIntent();
		phone = intent.getStringExtra("Phone");
		
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		findViewAddListener(R.id.new_siccess_resgister);
		findViewAddListener(R.id.new_success_kankan);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_siccess_resgister:
			//开通交易帐户
			Intent intent=new Intent(this, OpenTradingAccountActivity.class);
			System.out.println("手机号2=====>"+phone);
			intent.putExtra("Phone", phone);
			startActivity(intent);
			break;
		case R.id.new_success_kankan:
			//先看看
			Intent intent2=new Intent(this, MyActivityGroup.class);
			intent2.putExtra("Mobile", phone);
			intent2.putExtra("Flag", "5");
			startActivity(intent2);
			break;

		default:
			break;
		}
		
	}

}
