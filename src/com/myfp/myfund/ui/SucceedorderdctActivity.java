package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class SucceedorderdctActivity extends BaseActivity{
	private TextView leixing;
	private String tex_passtype;
	private Button back;
	@Override
	protected void setContentView() {
		
		setContentView(R.layout.activity_succeedorderdct);
//		back=(Button) findViewById(R.id.backonclick);
		findViewAddListener(R.id.backonclick);
		Intent intent =getIntent();
		tex_passtype=intent.getStringExtra("tex_passtype");
		leixing=(TextView) findViewById(R.id.huiyuan);
		leixing.setText(leixing.getText()+tex_passtype);
	}

	@Override
	protected void initViews() {
		setTitle("购买点财通");
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.backonclick:
			Intent intent1=new Intent(SucceedorderdctActivity.this,MyActivityGroup.class);
			startActivity(intent1);
			break;
		}
	}



}
