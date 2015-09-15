package com.myfp.myfund.ui.view;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class EventScrollView extends ScrollView {
	
	private Object childView;

	public EventScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setChildView(Object chiView) {
		this.childView=chiView;
	}
	
	public Object getChildView() {
		return childView;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (this.childView ==null||this.childView instanceof GraphicalView) {
			return super.dispatchTouchEvent(ev);
		}
		return true;
	 
	}

	@Override
	public void setOverScrollMode(int mode) {
		// TODO Auto-generated method stub
		super.setOverScrollMode(mode);
	}
	
	

}
