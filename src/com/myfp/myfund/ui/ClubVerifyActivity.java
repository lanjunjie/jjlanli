package com.myfp.myfund.ui;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;


/**
 * 财立方俱乐部 等待审核
 * @author Max.Zhao
 *
 */
public class ClubVerifyActivity extends BaseActivity {
	
	private int result;
	private TextView tv_result;
	private Button bt_applyAgain;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_clubverify);
		
		Intent intent = getIntent();
		result = intent.getIntExtra("result", 1);
	}

	@Override
	protected void initViews() {
		setTitle("财立方俱乐部");
		findViewAddListener(R.id.button_clubHome);
//		bt_applyAgain = (Button) findViewAddListener(R.id.button_applyAgain);
		tv_result = (TextView) findViewById(R.id.tv_verify_result);
		if (result == 2) {
			tv_result.setText("很遗憾，审核未通过！");
//			bt_applyAgain.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.button_clubHome:
			startActivity(MyActivityGroup.class);
			finish();
			break;
//		case R.id.button_applyAgain:
//			startActivity(JoinClubActivity.class);
//			finish();
//			break;
		}
	}

}
