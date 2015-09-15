package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class StartTest extends BaseActivity{

	private String sessionId;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_startrcp);
		Intent intent = getIntent();
		sessionId = intent.getStringExtra("sessionId");
		
	}

	@Override
	protected void initViews() {
		setTitle("风险测评");
		findViewAddListener(R.id.bu_test);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bu_test:
			Intent intent=new Intent(this,RiskAssessmentActivity.class);
			intent.putExtra("sessionId", sessionId);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}
