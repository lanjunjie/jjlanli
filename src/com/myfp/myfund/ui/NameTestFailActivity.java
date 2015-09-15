package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class NameTestFailActivity extends BaseActivity{
	TextView tv_nametestfail_testinfo;
	Intent intent;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_nametestfail);
		intent = getIntent();
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("实名验证");
		tv_nametestfail_testinfo = (TextView)findViewById(R.id.tv_nametestfail_testinfo);
		findViewAddListener(R.id.bt_nametestfail_confirm);
		tv_nametestfail_testinfo.setText(intent.getStringExtra("Flag"));
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_nametestfail_confirm:
			finish();
			break;

		default:
			break;
		}
		
	}

}
