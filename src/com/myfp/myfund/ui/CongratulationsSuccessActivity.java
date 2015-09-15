package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class CongratulationsSuccessActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_cardpay);
	}

	@Override
	protected void initViews() {
		setTitle("购买点财通");
		findViewAddListener(R.id.butt_acrdpay);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.butt_acrdpay:
			Intent intent=new Intent(this, Myfundsearch.class);
			intent.putExtra("flag", "2");
			startActivity(intent);
			finish();
			MembershipPayActivity.instance.finish();
			GoodsPassActivity.instance.finish();
			if (DianCaiTongActivity.instance!=null) {
				DianCaiTongActivity.instance.finish();
				
			}
			break;

		default:
			break;
		}
		
	}

}
