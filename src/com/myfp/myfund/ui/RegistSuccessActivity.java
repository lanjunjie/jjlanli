package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class RegistSuccessActivity extends BaseActivity {
	Intent intent;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_registsuccess);
		intent = getIntent();
		

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("注册成功");
		findViewAddListener(R.id.tv_openaccpunt_now);
		findViewAddListener(R.id.tv_statustest);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_openaccpunt_now:
			Intent intent1 = new Intent(RegistSuccessActivity.this,
					WriteInforActivity.class);
			startActivity(intent1);

			break;
		case R.id.tv_statustest:
			Intent intent2 = new Intent(RegistSuccessActivity.this,
					RealNameTestActivity.class);
			intent2.putExtra("UserName", intent.getStringExtra("UserName"));
			startActivity(intent2);
			break;

		default:
			break;
		}

	}

}
