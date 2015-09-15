package com.myfp.myfund.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class NumberRegisteredActivity extends BaseActivity{
	private String phoneNum="tel:400-888-6661";
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_number_registered);
		
	}

	@Override
	protected void initViews() {
		setTitle("注册");
		findViewAddListener(R.id.text_forget);
		findViewAddListener(R.id.text_contact);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.text_forget:
			startActivity(ResetPassActivity.class);
			break;

		case R.id.text_contact:
			showDialogCall(this, null, phoneNum);
			break;
		}
	}
	public static void showDialogCall(final Context context,String title, final String phoneNum){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("展恒客服电话:");
		builder.setMessage("400-888-6661");
		builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
					Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(phoneNum));  //ACTION_DIAL是跳转到拨号界面，ACTION_CALL是直接拨号  
	                context.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create();
		builder.show();
	}

}
