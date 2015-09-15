package com.myfp.myfund.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Bruce.Wang
 */
public class CustomProgressView extends View {
	
	public CustomProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomProgressView(Context context) {
		super(context);
	}

	
	private int first = 0;
	private int second = 0;
	private int third = 0;
	private int fouth = 0;
	
	/**
	 * 设置4个矩形的比例,4个数的和必须是100
	 * @param first
	 * @param second
	 * @param third
	 * @param fouth
	 */
	public void setProportion(int first,int second,int third,int fouth){
		int sum = first + second + third + fouth;
//		if(sum != 10000){
//			throw new RuntimeException("4个矩形的比例和必须是100!");
//		}
		this.first = first;
		this.second = second;
		this.third = third;
		this.fouth = fouth;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		System.out.println(height+"----"+width);
		
		//计算4个矩形的右坐标
		int p1Right = (first*width)/10000;
		int p2Right = (second*width)/10000 + p1Right;
		int p3Right = (third*width)/10000 + p2Right;
		int p4Right = (fouth*width)/10000 + p3Right;
		
		Paint paint = new Paint();
		
		//第一个矩形
		paint.setColor(Color.parseColor("#EB6876"));
		canvas.drawRect(new Rect(getLeft(), getTop(), p1Right, getBottom()), paint);
		//第二个矩形
		paint.setColor(Color.parseColor("#F9AF34"));
		canvas.drawRect(new Rect(p1Right, getTop(), p2Right, getBottom()), paint);
		//第三个矩形
		paint.setColor(Color.parseColor("#8783D7"));
		canvas.drawRect(new Rect(p2Right, getTop(), p3Right, getBottom()), paint);
		//第四个矩形
		paint.setColor(Color.parseColor("#00B8EE"));
		canvas.drawRect(new Rect(p3Right, getTop(), p4Right, getBottom()), paint);
		
	}
	
}
