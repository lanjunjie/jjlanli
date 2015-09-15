package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class OnlinerActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		setContentView(R.layout.acativity_onliner);
	}

	@Override
	protected void initViews() {
		setTitle("预约点财通");
		findViewAddListener(R.id.butt_onlinerpay);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.butt_onlinerpay:
			Intent intent=new Intent(this, CemeteryFragmentActivity.class);
			startActivity(intent);
			finish();
			OnLineRemitActivity.intaer.finish();
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
