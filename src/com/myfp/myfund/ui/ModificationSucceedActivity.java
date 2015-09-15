package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ModificationSucceedActivity extends BaseActivity{
	private Button bt_Confirm_Button;
	private String encodeIdCard, encodePassWord;
	private String customRiskLevel;
	public static ResetPasswordActivity instance=null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.layout_modificationok);
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		customRiskLevel = intent.getStringExtra("CustomRiskLevel");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("成功修改");
		findViewAddListener(R.id.bt_Confirm_Button);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_Confirm_Button:
			finish();
			break;

		default:
			break;
		}
		
	}

}
