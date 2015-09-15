package com.myfp.myfund.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class OnLineRemitActivity extends BaseActivity{
	public static OnLineRemitActivity intaer=null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_onlineremit);
		intaer=this;
	}

	@Override
	protected void initViews() {
		setTitle("汇款帐户");
		findViewAddListener(R.id.layot_pahtpay);
		findViewAddListener(R.id.butt_onlineremitpay);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.layot_pahtpay:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);   
			intent.setData(Uri.parse("tel:010-56236260"));
			startActivity(intent);
			break;
		case R.id.butt_onlineremitpay:
			Intent intent2=new Intent(this, OnlinerActivity.class);
			startActivity(intent2);
			
			break;
		default:
			break;
		}
	}

}
