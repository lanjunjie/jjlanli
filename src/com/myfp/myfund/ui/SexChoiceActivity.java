package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class SexChoiceActivity extends BaseActivity {
	
	private Intent intent;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_sex_choice);
		
		intent = getIntent();
	}

	@Override
	protected void initViews() {
		setTitle("性别选择");
		
		findViewAddListener(R.id.layout_sex_gentleman);
		findViewAddListener(R.id.layout_sex_lady);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.layout_sex_gentleman:
			intent.putExtra("Sex", "男");
			setResult(1, intent);
			finish();
			break;
		case R.id.layout_sex_lady:
			intent.putExtra("Sex", "女");
			setResult(1, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
