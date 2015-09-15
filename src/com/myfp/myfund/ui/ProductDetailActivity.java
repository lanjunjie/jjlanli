package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import android.annotation.SuppressLint;
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

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.PlacementResult;
import com.myfp.myfund.ui.view.CirclePageIndicator;

public class ProductDetailActivity extends BaseActivity{
	private ViewPager viewPager_productdetail;
	private List<Fragment> fragmentsList;
	private TextView[] arr_titles;
	private TextView[] arr_lines;
	private CirclePageIndicator indicator;
	private ScheduledExecutorService scheduledExecutorService;
	int currentItem;
	Bundle bundle;
	PlacementResult res;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_productdetail);
		bundle = getIntent().getExtras();
		res = (PlacementResult) bundle.getSerializable("PlacementResult");
	}

	@Override
	protected void initViews() {
		setTitle("产品单页");
		initIndicateLine();
		initTabView();
		initViewPager();
		
	}
	
	// 初始化头部导航条
	private void initTabView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_productdetail_titles);
		arr_titles = new TextView[3];
		for (int i = 0; i < arr_titles.length; i++) {
			TextView textView = (TextView) layout.getChildAt(i);
			arr_titles[i] = textView;
			arr_titles[i].setEnabled(true);
//			arr_titles[i].setTextColor(this.getResources().getColor(
//					R.color.main_tab_name));
			arr_titles[i].setTextColor(this.getResources().getColor(
					R.color.main_tab_name));
			arr_titles[i].setTag(i);
			arr_titles[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager_productdetail.setCurrentItem((Integer) v.getTag());
				}
			});
		}
		arr_titles[0].setEnabled(false);
		//arr_titles[0].setTextColor(Color.WHITE);
	}
	
	
	@SuppressLint("ResourceAsColor")
	private void initIndicateLine() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_productdetail_lines);
		arr_lines = new TextView[3];
		for (int i = 0; i < arr_lines.length; i++) {
			TextView view = (TextView) layout.getChildAt(i);
			arr_lines[i] = view;
			arr_lines[i].setTag(i);
			arr_lines[i].setBackgroundColor(Color.WHITE);
		}
		arr_lines[0].setBackgroundColor(this.getResources().getColor(
				R.color.main_tab_name));
	}
	
	// 导航条下面的红线
		private void initViewPager() {
			viewPager_productdetail = (ViewPager) findViewById(R.id.viewPager_productdetail);
			fragmentsList = new ArrayList<Fragment>();

			Bundle bundle = new Bundle();
			for (int i = 0; i < 3; i++) {
				if (i == 0) {
					Fragment fragment1 = new ProductFragment();
					bundle.putString("FundCode",res.getFundCode());
					fragment1.setArguments(bundle);
					fragmentsList.add(fragment1);
				} else if (i == 1) {
					Fragment fragment2 = new ManagerFragment();
					bundle.putInt("index", i); 
					bundle.putString("PersonID",res.getPersonID());
					System.out.println("PersonID----->"+res.getPersonID());
					fragment2.setArguments(bundle);
					fragmentsList.add(fragment2);
				} else if (i == 2) {
					Fragment fragment3 = new CompanyFragment();
					bundle.putInt("index", i);
					bundle.putString("FundCode",res.getFundCode());
					fragment3.setArguments(bundle);
					fragmentsList.add(fragment3);
				}
	 
			}

			viewPager_productdetail.setAdapter(new MyListAdapter(
					getSupportFragmentManager(), fragmentsList));
			viewPager_productdetail
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageScrollStateChanged(int position) {
						}

						@Override
						public void onPageScrolled(int arg0, float arg1, int arg2) {
						}

						@SuppressLint("ResourceAsColor")
						@Override
						public void onPageSelected(int position) {
							for (int i = 0; i < arr_titles.length; i++) {
								arr_titles[i].setEnabled(true);
//								arr_titles[i].setTextColor(getApplicationContext()
//										.getResources().getColor(
//												R.color.main_tab_name));
								arr_lines[i]
										.setBackgroundColor(Color.WHITE);
							}
							arr_titles[position].setEnabled(false);
							//arr_titles[position].setTextColor(Color.WHITE);
							arr_lines[position].setBackgroundColor(getApplicationContext().getResources().getColor(
									R.color.main_tab_name));
						}

					});
			viewPager_productdetail.setCurrentItem(0);
			viewPager_productdetail.setOffscreenPageLimit(3);
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

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
