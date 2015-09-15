package com.myfp.myfund.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class FinancingAuditActivity extends BaseActivity{

	private SpannableString msp;
	private TextView tex_z;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_audit_financing);
		
	}

	@Override
	protected void initViews() {
		setTitle("我的资产-基金配资");
		//findViewAddListener(R.id.te_ph);
		//findViewAddListener(R.id.but_retu);
		findViewAddListener(R.id.tex_k);
		findViewAddListener(R.id.tex_m);
		tex_z = (TextView) findViewById(R.id.tex_z);
		msp = new SpannableString("注:证明文件可以是证券账户卡(上海/深圳)或基金开户证明(须盖有开户公章)。");
		msp.setSpan(new ForegroundColorSpan(Color.RED), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(Color.RED), 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tex_z.setText(msp);
	
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		/*case R.id.te_ph:
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:010-56236258"));
			startActivity(intent);
			break;*/
		//case R.id.but_retu:
		//	startActivity(MyPropertyActivity.class);
		//	finish();
		case R.id.tex_k:
			Intent intent2=new Intent(this, CalyonSecuritiesActivity.class);
			startActivity(intent2);
			break;
		case R.id.tex_m:
			Intent intent3=new Intent(this, AccountOpeningActivity.class);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}

}
