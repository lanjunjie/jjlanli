package com.myfp.myfund.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

/*
 * 委托查询
 **/

public class DealEntrustActivity extends BaseActivity {
	private FragmentManager fmr = getSupportFragmentManager();
	private FragmentTransaction fragmentMeans = getSupportFragmentManager().beginTransaction();
	@Override
	protected void setContentView() {
		setContentView(R.layout.new_means_fragment);
		fragmentMeans.replace(R.id.means_fragment, new FundPolingFragment());
		fragmentMeans.commit();
	}

	@Override
	protected void initViews() {
		setTitle("查询");
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
