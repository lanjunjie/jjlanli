/**   
 * @Title: CustomViewPager.java
 * @Package com.rndchina.cailifang.ui.view
 * @Description: TODO(��һ�仰�������ļ���ʲô)
 * @author liangyao  
 * @date 2014-8-6 ����1:54:27
 * @version V1.0   
 */

package com.myfp.myfund.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @ClassName: CustomViewPager
 * @Description:
 * @author liangyao
 * @date 2014-8-6 ����1:54:27
 * 
 */

public class CustomViewPager extends ViewPager {

	private boolean enabled;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomViewPager(Context context) {
		super(context);
	}

	@Override
	public void setOffscreenPageLimit(int limit) {
		super.setOffscreenPageLimit(0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
