package com.myfp.myfund.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class MyMeansActivity extends BaseActivity{
	private ImageView left;
	private Button right;
	private long lastBack;
	private SharedPreferences sPreferences;
	private FragmentManager fmr = getSupportFragmentManager();
	private FragmentTransaction fragmentMeans = getSupportFragmentManager().beginTransaction();
	@Override
	protected void setContentView() {
		setContentView(R.layout.new_means_fragment);
		fragmentMeans.replace(R.id.means_fragment, new FundMeansFragment());
		fragmentMeans.commit();
		sPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
		
	}

	@Override
	protected void initViews() {
		setTitle("我的资产");
		left=(ImageView) findViewById(R.id.iv_mainactivity_top_left);
		left.setBackgroundResource(R.drawable.header_back);
		left.setOnClickListener(this);
		right=(Button) findViewById(R.id.tv_text_main_publish);
		right.setText("快速注册");
		right.setVisibility(View.VISIBLE);
//		right.setTextColor(R.color.);
		right.getBackground().setAlpha(0);
		right.setOnClickListener(this);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_mainactivity_top_left:
		/*	Intent intent =new Intent(this, MyActivityGroup.class);
			intent.putExtra("Flag", "0");
			intent.putExtra("sessionId", getIntent().getStringExtra("sessionId"));
			startActivity(intent);*/
			finish();
			break;
		case R.id.tv_text_main_publish:
			//旧注册开户流程
			//startActivity(new Intent(MyMeansActivity.this, RegisterUserActivity.class));
			//快钱开户注册流程
			startActivity(new Intent(MyMeansActivity.this, ShortcutRegisterActivity.class));
		default:
			break;
		}
	}

}
