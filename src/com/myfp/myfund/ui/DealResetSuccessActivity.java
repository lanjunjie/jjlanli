package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class DealResetSuccessActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealresetsuccess);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("重置成功");
		findViewAddListener(R.id.bt_dealaccountlogin);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_dealaccountlogin:
			Intent intent=new Intent(this, MyActivityGroup.class);
			intent.putExtra("Flag", "4");
			startActivity(intent);
			finish();
			DealResetPassWordActivity.instance.finish();
			ConfirmInformationActivity.instance.finish();
			break;

		default:
			break;
		}
		
	}

}
