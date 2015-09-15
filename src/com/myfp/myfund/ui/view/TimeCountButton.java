package com.myfp.myfund.ui.view;

import com.myfp.myfund.R;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

public class TimeCountButton extends Button {
	
	private TimeCount timeCount;

	public TimeCountButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		timeCount = new TimeCount(60000, 1000);
		// TODO Auto-generated constructor stub
	}

	public TimeCountButton(Context context) {
		super(context);
		timeCount = new TimeCount(60000, 1000);
		// TODO Auto-generated constructor stub
	}
	
	public void TimeStart(){
		timeCount.start();
	}
	
	class TimeCount extends CountDownTimer{
		
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {
			setText(" 重新获取 ");
			setClickable(true);
			setBackgroundResource(R.color.white_bt_normal);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			setClickable(false);
			setBackgroundColor(Color.WHITE);
			setText("     "+millisUntilFinished / 1000+"s"+"     ");
		}
		
	}
}
