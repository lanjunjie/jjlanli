package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class MyMemberFragment extends BaseFragment implements OnCheckedChangeListener{
	private View view;
	private int index;
	ByteArrayInputStream tInputStringStream = null;
	private MyPropertyActivity activity;
	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2, mRadioButton3;
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
		view = inflater.inflate(R.layout.activity_individual_member, null);
		System.out.println("view=-=-=-=-=-=-=-=-=-=>"+view);
		initView();
		initListener();
		return view;
	}
	public void initView() {
		mFragmentList = new ArrayList<Fragment>();
		System.out.println("mFragmentList--------------->"+mFragmentList);
		mFragmentList.add(new MyMessageFragment());
		mFragmentList.add(new MyPurchaseDetailsFragment());
		mFragmentList.add(new MyShowDetailsFragment());
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_member);
		System.out.println("mViewPager------------>"+mViewPager);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radio);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.bt_member_information);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.bt_Purchase_details);
		mRadioButton3 = (RadioButton) view.findViewById(R.id.bt_show_details);
	//	mImageView = (ImageView) view.findViewById(R.id.img1);
		mHorizontalScrollView = (LinearLayout) view
				.findViewById(R.id.horizontalScrollView);
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
				((RadioButton) mRadioGroup.getChildAt(position))
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
		case R.id.bt_member_information:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo1));
			mViewPager.setCurrentItem(0);
			break;
		case R.id.bt_Purchase_details:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo2));
			mViewPager.setCurrentItem(1);
			break;
		case R.id.bt_show_details:

			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo3));
			mViewPager.setCurrentItem(2);
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

		// mImageView.bringToFront();
	//	mImageView.startAnimation(animationSet);
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
		} else if (mRadioButton3.isChecked()) {
			// Log.i("zj",
			// "currentCheckedRadioLeft="+getResources().getDimension(R.dimen.rdo3));
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
