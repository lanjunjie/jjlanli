/**   
* @Title: MyFragmentPagerAdapter.java
* @Package com.rndchina.cailifang.ui.view
* @Description: TODO(��һ�仰�������ļ���ʲô)
* @author liangyao  
* @date 2014-8-6 ����12:15:27
* @version V1.0   
*/


package com.myfp.myfund.ui.view;

import java.util.ArrayList;

import com.myfp.myfund.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @ClassName: MyFragmentPagerAdapter
 * @Description: TODO(������һ�仰��������������)
 * @author liangyao
 * @date 2014-8-6 ����12:15:27
 * 
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


	private ArrayList<BaseFragment> fragmentsList;
	
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	

	public MyFragmentPagerAdapter(FragmentManager fm,
			ArrayList<BaseFragment> fragmentsList) {
		super(fm);
		this.fragmentsList = fragmentsList;
	}






	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}


	@Override
	public int getCount() {
		return fragmentsList.size();
	}


	
	
	
	@Override
	public Fragment getItem(int arg0) {
		
		return fragmentsList.get(arg0);
	}

}
