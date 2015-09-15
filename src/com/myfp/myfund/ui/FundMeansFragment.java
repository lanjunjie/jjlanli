package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;

public class FundMeansFragment extends BaseFragment implements OnCheckedChangeListener{
	private View view;
	private int index;
	ByteArrayInputStream tInputStringStream = null;
	private MyMeansActivity activity;
	
	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2;
	private LinearLayout mHorizontalScrollView;
	//private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;
	private List<Fragment> mFragmentList;
	private MyFragmentAdapter adapter;
	
	@Override
	protected void onViewClick(View v) {
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.new_activity_login, null);
		System.out.println("view=-=-=-=-=-=-=-=-=-=>"+view);
		initView();
		initListener();
		return view;
	}
	public void initView() {
		mFragmentList = new ArrayList<Fragment>();
		System.out.println("mFragmentList--------------->"+mFragmentList);
		mFragmentList.add(new FundAccountsFragment());
		mFragmentList.add(new DealAccountsFragment());
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_login);
		System.out.println("mViewPager------------>"+mViewPager);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radiolg);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.new_radio_account);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.new_radio_deal);
		mHorizontalScrollView = (LinearLayout) view
				.findViewById(R.id.linear_layout);
		adapter = new MyFragmentAdapter(getChildFragmentManager(),
				mFragmentList);
		System.out.println("adapter========>"+adapter);
		mViewPager.setAdapter(adapter);
		mRadioButton1.setChecked(true);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				((RadioButton) mRadioGroup.getChildAt(position)).setChecked(true);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.new_radio_account:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo1));
			mViewPager.setCurrentItem(0);
			break;
		case R.id.new_radio_deal:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo2));
			mViewPager.setCurrentItem(1);
			break;
		
		
		default:
			break;
		}

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
		
	}

	public void imageTranslateAnimation(float f) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				mCurrentCheckedRadioLeft, f, 0f, 0f);

		animationSet.addAnimation(translateAnimation);
		animationSet.setFillBefore(false);
		animationSet.setFillAfter(true);
		animationSet.setDuration(100);

	}

	private float getCurrentCheckedRadioLeft() {
		// TODO Auto-generated method stub
		if (mRadioButton1.isChecked()) {
			// Log.i("zj",
			// "currentCheckedRadioLeft="+getResources().getDimension(R.dimen.rdo1));
			return getResources().getDimension(R.dimen.rdo1);
		} else if (mRadioButton2.isChecked()) {
			// Log.i("zj",
			// "currentCheckedRadioLeft="+getResources().getDimension(R.dimen.rdo2));
			return getResources().getDimension(R.dimen.rdo2);
		} 
		return 0f;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
