package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.RequestParams;

public class CouponActivity extends BaseActivity {

	private TextView[] titles_main;
	private ViewPager viewPager_fundNews;
	private List<Fragment> fragmentsList;
	private long lastBack;
	private SharedPreferences sPreferences;

	private String password;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_coupon);
		SharedPreferences sPreferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		String name = sPreferences.getString("UserName", null);
		
	}

	@Override
	protected void initViews() {
		setTitle("我的优惠券");
		initViewPager();
//		initMainTitle();

	}

	@Override
	protected void onViewClick(View v) {

	}

//	private void initMainTitle() {
//		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_coupon_title);
//		titles_main = new TextView[2];
//		for (int i = 0; i < titles_main.length; i++) {
//			TextView textView = (TextView) layout.getChildAt(i);
//			titles_main[i] = textView;
//			titles_main[i].setEnabled(true);
//			titles_main[i].setTag(i);
//			titles_main[i].setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					viewPager_fundNews.setCurrentItem((Integer) v.getTag());
//				}
//			});
//		}
//		titles_main[0].setEnabled(false);
//	}

	private void initViewPager() {
		viewPager_fundNews = (ViewPager) findViewById(R.id.viewPager_couponNews);
		fragmentsList = new ArrayList<Fragment>();

		Bundle bundle = new Bundle();
//		for (int i = 0; i < 2; i++) {
//			if (i == 0) {
				CouponFragment fragment1 = new CouponFragment();
				bundle.putInt("index", 0);
				fragment1.setArguments(bundle);
				fragmentsList.add(fragment1);
//			} else if (i == 1) {
//				LunStampsFragment fragment2 = new LunStampsFragment();
//				bundle.putInt("index", i);
//				fragment2.setArguments(bundle);
//				fragmentsList.add(fragment2);
//			}
//		}

		viewPager_fundNews.setAdapter(new MyNewsAdapter(
				getSupportFragmentManager(), fragmentsList));
		viewPager_fundNews.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@SuppressLint("ResourceAsColor")
			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < titles_main.length; i++) {
					titles_main[i].setEnabled(true);
				}
				titles_main[position].setEnabled(false);
			}

		});
		viewPager_fundNews.setCurrentItem(0);
		viewPager_fundNews.setOffscreenPageLimit(1);
	}

	class MyNewsAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentsList = null;

		public MyNewsAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyNewsAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
			super(fm);
			this.fragmentsList = fragmentsList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentsList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentsList.size();
		}

	}

}
