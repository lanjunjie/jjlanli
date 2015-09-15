package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragmentActivity;
import com.myfp.myfund.R;

/**
 * 基金快讯
 * 
 * @author Max.Zhao
 * 
 */
public class FundNewsActivity extends BaseActivity {

	private TextView[] titles_main;
	private ViewPager viewPager_fundNews;
	private List<Fragment> fragmentsList;
	private long lastBack;
	private SharedPreferences sPreferences;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fundnews);
	}

	@Override
	protected void initViews() {
		setTitle("基金快讯");
		initViewPager();
		initMainTitle();
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.pushnews);

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			startActivity(PushListActivity.class);
			break;

		default:
			break;
		}
	}

	// 新闻资讯&研报快讯
	private void initMainTitle() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_fundNews_mainTitle);
		titles_main = new TextView[2];
		for (int i = 0; i < titles_main.length; i++) {
			TextView textView = (TextView) layout.getChildAt(i);
			titles_main[i] = textView;
			titles_main[i].setEnabled(true);
			titles_main[i].setTag(i);
			titles_main[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager_fundNews.setCurrentItem((Integer) v.getTag());
				}
			});
		}
		titles_main[0].setEnabled(false);
	}

	private void initViewPager() {
		viewPager_fundNews = (ViewPager) findViewById(R.id.viewPager_fundNews);
		fragmentsList = new ArrayList<Fragment>();

		for (int i = 0; i < 2; i++) {
			Fragment fragment = new NewsListFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			fragment.setArguments(bundle);
			fragmentsList.add(fragment);
		}

		viewPager_fundNews.setAdapter(new MyNewsAdapter(
				getSupportFragmentManager(), fragmentsList));
		viewPager_fundNews.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < titles_main.length; i++) {
					titles_main[i].setEnabled(true);
				}
				titles_main[position].setEnabled(false);
			}

		});
		viewPager_fundNews.setCurrentItem(0);
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
	
	@Override
	public void onBackPressed() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastBack > 2000) {
			lastBack = curTime;
			showToast("再按一次退出展恒基金网");
		} else {
			String listStr = JSON.toJSONString(App.getContext().userList);
			Editor editor = sPreferences.edit();
			editor.putString("UserList", listStr);
			editor.commit();
			finish();
			
		}
	}
	
}
