package com.myfp.myfund.ui;

import android.view.View;
import android.widget.LinearLayout;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ProductOrderSuccessActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_productordersuccess);
		
	}

	@Override
	protected void initViews() {
		setTitle("产品预约");
		LinearLayout ll_top_layout_left_view = (LinearLayout)findViewById(R.id.ll_top_layout_left_view);
		ll_top_layout_left_view.setVisibility(View.GONE);
		
		findViewAddListener(R.id.bt_proordersuccess_confirm);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_proordersuccess_confirm:
			//startActivity(MyPropertyActivity.class);
			finish();
			break;

		default:
			break;
		}
		
	}

}
