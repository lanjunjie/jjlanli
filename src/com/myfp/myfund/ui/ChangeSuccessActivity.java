package com.myfp.myfund.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ChangeSuccessActivity extends BaseActivity{
	private Button bt_Change_bonus_OK;
	private String iDCard;
	private String passWord;
	@Override
	protected void setContentView() {
		setContentView(R.layout.layout_change_ok);
		SharedPreferences preferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		iDCard = preferences.getString("IDCard", null);
		passWord = preferences.getString("EncodePassWord", null);
	}

	@Override
	protected void initViews() {
		setTitle("变更分红方式");
		findViewAddListener(R.id.bt_Change_bonus_OK);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_Change_bonus_OK:
			finish();
			AlterMmodeChangeActivity.instance.finish();
			queryAssetsActivity.instance.finish();
			break;

		default:
			break;
		}
	}

}
