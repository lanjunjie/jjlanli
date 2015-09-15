package com.myfp.myfund.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

/**
 * 基金自选
 * 
 * @author pengchong.jia
 * 
 */
public class FundSelectActivity extends BaseActivity {

	private TextView[] titles;
	private String userName;
	private FrameLayout mContainer;
	private String sessionId;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fundselect);

		Intent intent = getIntent();
	//	SharedPreferences sPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
	//	userName =sPreferences.getString("UserName", null);
	//	userName = intent.getStringExtra("userName");
		userName=App.getContext().getUserName();
		sessionId = App.getContext().getSessionid();
		System.out.println("userName5---------->"+userName);
		//sessionId = intent.getStringExtra("sessionId");
		
	}

	@Override
	protected void initViews() {
		setTitle("基金自选");

		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewAddListener(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.plus);
		
		mContainer = (FrameLayout) findViewById(R.id.layout_fundSelect_container);

		initTitle();

		addFragment(0);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_mainactivity_top_right:
			Intent intent = new Intent(this, Myfundsearch.class);
			intent.putExtra("UserName", userName);
			intent.putExtra("sessionId", sessionId);
			intent.putExtra("flag", "1");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void initTitle() {  
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_fundSelect_tab);
		titles = new TextView[2];
		for (int i = 0; i < titles.length; i++) {
			TextView textView = (TextView) layout.getChildAt(i * 2);
			titles[i] = textView;
			titles[i].setEnabled(true);
			titles[i].setTag(i);
			titles[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < titles.length; i++) {
						titles[i].setEnabled(true);
					}
					titles[(Integer) v.getTag()].setEnabled(false);
					addFragment((Integer) v.getTag());
				}
			});
		}
		titles[0].setEnabled(false);
	}

	private void addFragment(int index) {
		// FundSelectFragment fragment = new FundSelectFragment();
		// Bundle bundle = new Bundle();
		// bundle.putInt("index", index);
		// bundle.putString("UserName", userName);
		// fragment.setArguments(bundle);
		//
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.replace(R.id.layout_fundSelect_container, fragment,
		// "fundSelectFragment");
		// transaction.commit();

		Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(
				mContainer, index);
		mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
		mFragmentPagerAdapter.finishUpdate(mContainer);
	}

	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 1:
				return FundSelectFragment.instantiation(1);
			case 0:
			default:
				return FundSelectFragment.instantiation(0);
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

}
