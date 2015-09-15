package com.myfp.myfund.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class BeinAuditActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_beinaudit);
	}

	@Override
	protected void initViews() {
		setTitle("产品预约");
		findViewAddListener(R.id.te_v_ph);
		//findViewAddListener(R.id.but_return);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.te_v_ph:
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:010-56236258"));
			startActivity(intent);
			break;
		//case R.id.but_return:
		//	startActivity(MyPropertyActivity.class);
		//	finish();
		//	break;
		default:
			break;
		}
	}

}
