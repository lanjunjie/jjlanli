package com.myfp.myfund.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ListItemHorizontalScrollView extends HorizontalScrollView {
	
	BaseFixColumnListView mListView;
	Activity activity;
	
	public ListItemHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (Activity) context;
	}

	
	public ListItemHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Activity) context;
	}

	public ListItemHorizontalScrollView(Context context) {
		super(context);
		activity = (Activity) context;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {		
		mListView.TouchView=this;
		return super.onTouchEvent(ev);
	}
	
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
 
		HorizontalScrollView touchView=mListView.TouchView;
		if(touchView == this) {
			mListView.ScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
		
	}

   public void setListView(BaseFixColumnListView listView) {
	   mListView = listView;
	
   }
	 
}
