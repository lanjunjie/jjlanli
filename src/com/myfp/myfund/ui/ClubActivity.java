package com.myfp.myfund.ui;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

/**
 * 财立方俱乐部
 * @author Max.Zhao
 *
 */
public class ClubActivity extends BaseActivity {
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_club);
	}

	@Override
	protected void initViews() {
		findViewAddListener(R.id.button_joinclub);
		setTitle("财立方俱乐部");
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.button_joinclub:
			startActivity(JoinClubActivity.class);
			finish();
			break;

		default:
			break;
		}
	}

}
