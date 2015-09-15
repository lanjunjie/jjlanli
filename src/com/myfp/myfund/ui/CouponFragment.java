package com.myfp.myfund.ui;

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
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;

public class CouponFragment extends BaseFragment implements OnCheckedChangeListener{
	private View view;
	private int index;
	
	private ViewPager view_me;
	private RadioGroup adRadioGroup;
	private RadioButton dRadioButton1, dRadioButton2,dRadioButton3;
	private LinearLayout linear;
	private float mCurrentCheckedRadioLeft;
	private List<Fragment> mFragmentList;
	private MyFragmentAdapter adapter;
	
	@Override
	protected void onViewClick(View v) {
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_favorable, null);
		initView();
		initListener();
		return view;
	}
	public void initView() {
		mFragmentList = new ArrayList<Fragment>();
		mFragmentList.add(new TobeusedFragment());
		mFragmentList.add(new HasbeenusedFragment());
		mFragmentList.add(new HaveExpiredFragment());
		view_me = (ViewPager) view.findViewById(R.id.view_me);
		System.out.println("mViewPager------------>"+view_me);
		adRadioGroup = (RadioGroup) view.findViewById(R.id.ad);
		dRadioButton1 = (RadioButton) view.findViewById(R.id.bt_chase_d);
		dRadioButton2 = (RadioButton) view.findViewById(R.id.bt_sh_d);
		dRadioButton3 = (RadioButton) view.findViewById(R.id.bt_th_d);
		
		linear = (LinearLayout) view.findViewById(R.id.linear);
		adapter = new MyFragmentAdapter(getChildFragmentManager(),
				mFragmentList);
		view_me.setAdapter(adapter);
		dRadioButton1.setChecked(true);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		adRadioGroup.setOnCheckedChangeListener(this);
		view_me.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				((RadioButton) adRadioGroup.getChildAt(position))
						.setChecked(true);

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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.bt_chase_d:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo1));
			view_me.setCurrentItem(0);
			break;
		case R.id.bt_sh_d:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo2));
			view_me.setCurrentItem(1);
			break;
		case R.id.bt_th_d:
			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo3));
			view_me.setCurrentItem(2);
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
		if (dRadioButton1.isChecked()) {
			return getResources().getDimension(R.dimen.rdo1);
		} else if (dRadioButton2.isChecked()) {
			return getResources().getDimension(R.dimen.rdo2);
		} else if (dRadioButton3.isChecked()) {
			return getResources().getDimension(R.dimen.rdo3);
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
