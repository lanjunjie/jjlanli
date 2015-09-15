package com.myfp.myfund.ui;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class ShortcutDealPassAcitvity extends BaseActivity{

	private EditText shortcut_dredgede_paddword;
	private EditText shortcut_dredgede_nextpassword;

	public static ShortcutDealPassAcitvity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_deal_pass);
		instance=this;
	}

	@Override
	protected void initViews() {
		setTitle("交易密码");
		shortcut_dredgede_paddword = (EditText) findViewById(R.id.shortcut_dredgede_paddword);
		shortcut_dredgede_nextpassword = (EditText) findViewById(R.id.shortcut_dredgede_nextpassword);
		findViewAddListener(R.id.shortcut_dredgede);

		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		
		case R.id.shortcut_dredgede:
			password();
			break;
		}
		
	}
	void password(){
		if (!(shortcut_dredgede_paddword
				.getText().toString()
				.length() == 0)) {
			if (shortcut_dredgede_paddword
					.getText().toString()
					.length() > 8
					|| shortcut_dredgede_paddword
							.getText()
							.toString()
							.length() < 6) {
				showToast("密码长度为6~8位");
				return;
			} else {
				if (!(shortcut_dredgede_nextpassword
						.getText()
						.toString()
						.length() == 0)) {
					if (shortcut_dredgede_nextpassword
							.getText()
							.toString()
							.length() > 8
							|| shortcut_dredgede_nextpassword
									.getText()
									.toString()
									.length() < 6) {
						showToast("确认密码长度为6~8位");
						return;
					} else {
						if (shortcut_dredgede_paddword
								.getText()
								.toString()
								.equals(shortcut_dredgede_nextpassword
										.getText()
										.toString())) {
							
						
									Intent intent=new Intent(this, ShortcutBindingActivity.class);
									intent.putExtra("SDName", getIntent().getStringExtra("SDName"));
									intent.putExtra("SDIdCard", getIntent().getStringExtra("SDIdCard"));
									intent.putExtra("Code", getIntent().getStringExtra("Code"));
									intent.putExtra("Phone", getIntent().getStringExtra("Phone"));
									intent.putExtra("SDealPass",shortcut_dredgede_nextpassword.getText().toString());
									startActivity(intent);
								
							

						} else {
							showToast("两次密码输入不一致");
							return;
						}
					}

				} else {
					showToast("确认密码为空");
					return;
				}
			}
		} else {
			showToast("密码为空");
			return;
		}
	}

}
