package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

import android.content.Intent;
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


//专家精选
public class ExpertSelectedActivity extends BaseActivity {
	
	private ViewPager viewPager_expertSelected;
	private List<Fragment> fragmentsList;
	private TextView[] arr_titles;
	private TextView[] arr_lines;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_expertselected);
	}

	@Override
	protected void initViews() {
		setTitle("专家精选");
		initIndicateLine();
		initTabView();
		initViewPager();
		

	}

	@Override
	protected void onViewClick(View v) {

	}
	

	//初始化头部导航条
	private void initTabView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_titles);
		arr_titles = new TextView[4];
		for (int i = 0; i < arr_titles.length; i++) {
			TextView textView = (TextView) layout.getChildAt(i);
			arr_titles[i] = textView;
			arr_titles[i].setEnabled(true);
			arr_titles[i].setTag(i);
			arr_titles[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager_expertSelected.setCurrentItem((Integer) v.getTag());
				}
			});
		}
		arr_titles[0].setEnabled(false);
		arr_titles[0].setTextColor(Color.BLACK);
	}
	
	private void initIndicateLine() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_lines);
		arr_lines = new TextView[4];
		for (int i = 0; i < arr_lines.length; i++) {
			TextView view = (TextView) layout.getChildAt(i);
			arr_lines[i] = view;
			arr_lines[i].setTag(i);
			arr_lines[i].setBackgroundColor(Color.rgb(238, 238, 238));
		}
		arr_lines[0].setBackgroundColor(Color.RED);
	}
	
	//导航条下面的红线
	private void initViewPager() {
		viewPager_expertSelected = (ViewPager) findViewById(R.id.viewPager_expertSelected);
		fragmentsList = new ArrayList<Fragment>();
		
		for(int i = 0;i < 4;i++){
		Fragment fragment = new SelectedListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("oqtype",i+1);                                                                                                                                                                                                                                                             
		fragment.setArguments(bundle);
		fragmentsList.add(fragment);
		}

		viewPager_expertSelected.setAdapter(new MyListAdapter(getSupportFragmentManager(),
				fragmentsList));
		viewPager_expertSelected.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < arr_titles.length; i++) {
					arr_titles[i].setEnabled(true);
					arr_lines[i].setBackgroundColor(Color.rgb(238, 238, 238));
				}
				arr_titles[position].setEnabled(false);
				arr_titles[position].setTextColor(Color.BLACK);
				arr_lines[position].setBackgroundColor(Color.RED);
			}

		});
		viewPager_expertSelected.setCurrentItem(0);
		viewPager_expertSelected.setOffscreenPageLimit(4);
	}
	
	class MyListAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragmentsList = null;

		public MyListAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyListAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
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
