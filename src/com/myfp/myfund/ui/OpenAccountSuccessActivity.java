package com.myfp.myfund.ui;

import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class OpenAccountSuccessActivity extends BaseActivity{

	private String fundCode;
	public static OpenAccountSuccessActivity instance = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_openaccountsuccess);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		fundCode = preferences.getString("FundCode", fundCode);
		
	}

	@Override
	protected void initViews() {
		setTitle("开户成功");
		findViewAddListener(R.id.bt_openaccountsuccess_confirm);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_openaccountsuccess_confirm:
			finish();
			BindingBankActivity.instance.finish();
			WriteInforActivity.instance.finish();
			WriteNextActivity.instance.finish();
			
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
			
			finish(); 
			BindingBankActivity.instance.finish();
			WriteInforActivity.instance.finish();
			WriteNextActivity.instance.finish();
			//DealLoginActivity.instance.finish();
			
			}
		return super.onKeyDown(keyCode, event);
	}

}
