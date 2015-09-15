package com.myfp.myfund.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import cn.jpush.android.data.o;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class MyMemberDredgeActivity extends BaseActivity{

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_my_assets);
		
	}

	@Override
	protected void initViews() {
		setTitle("我的资产--点财通会员");
		findViewAddListener(R.id.imag_dredge_view);
		findViewAddListener(R.id.image_the_details);
//		findViewAddListener(R.id.imag_membership_criteria);
		TextView tex_cont = (TextView) findViewById(R.id.tex_cont);
		SpannableString spann=new SpannableString("成为点财通会员，即可享受免申购费购买60多家基金公司的1600多只基金!");
		spann.setSpan(new ForegroundColorSpan(Color.RED), 12, 16, spann.SPAN_EXCLUSIVE_EXCLUSIVE);
		tex_cont.setText(spann);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.imag_dredge_view:
			Intent intent=new Intent(this, GoodsPassActivity.class);
			startActivity(intent);
			//Intent intent = new Intent(this,OpenButtonActivity.class);
			//intent.putExtra("ImageID", "01");
			//startActivity(intent);	
			break;
		case R.id.image_the_details:
			Intent intent1 = new Intent(this,KnowSomeMoneyActivity.class);
			intent1.putExtra("ImageID", "08");
			startActivity(intent1);	
			break;
//		case R.id.imag_membership_criteria:
//			Intent intent2 = new Intent(this,ImmediatelyApplyActivity.class);
//			intent2.putExtra("ImageID", "01");
//			startActivity(intent2);	
//			break;

		default:
			break;
		}
	}

}
