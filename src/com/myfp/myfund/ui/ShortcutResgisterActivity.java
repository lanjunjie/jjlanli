package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ShortcutResgisterActivity extends BaseActivity{
	public static ShortcutResgisterActivity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_resgister_success);
		instance=this;
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		findViewAddListener(R.id.shortcut_siccess_resgister);
		findViewAddListener(R.id.shortcut_success_kankan);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.shortcut_siccess_resgister:
			Intent intent=new Intent(this, ShortcutStatusActivity.class);
			intent.putExtra("Phone", getIntent().getStringExtra("Phone"));
			startActivity(intent);
			break;
		case R.id.shortcut_success_kankan:
			Intent intent2=new Intent(this, MyActivityGroup.class);
			startActivity(intent2);
			break;
		}
		
	}

}
