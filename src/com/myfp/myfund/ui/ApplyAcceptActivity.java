package com.myfp.myfund.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ApplyAcceptActivity extends BaseActivity{
	Bundle bundle;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_applyaccept);
		bundle = getIntent().getExtras();
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("申请受理");
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		layout.setVisibility(View.GONE);
		findViewAddListener(R.id.bt_applyaccept);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applyaccept:
			Intent intent = new Intent(ApplyAcceptActivity.this,DealBuyActivity.class);
			//intent.putExtra("IDCard", bundle.getString("IDCard"));
			//intent.putExtra("PassWord", bundle.getString("PassWord"));
			intent.putExtra("sessionId", bundle.getString("sessionId"));
			startActivity(intent);
			finish();
			DealBuyActivity.instance.finish();
			DealApplyActivity.instance.finish();
			DealConfirmActivity.instance.finish();
			break;

		default:
			break;
		}
		
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
			
			Intent intent = new Intent(ApplyAcceptActivity.this,DealBuyActivity.class);
			//intent.putExtra("IDCard", bundle.getString("IDCard"));
			//intent.putExtra("PassWord", bundle.getString("PassWord"));
			intent.putExtra("sessionId", bundle.getString("sessionId"));
			startActivity(intent);
			finish();
			DealBuyActivity.instance.finish();
			DealApplyActivity.instance.finish();
			DealConfirmActivity.instance.finish();
			
			}
		return super.onKeyDown(keyCode, event);
	}
}
