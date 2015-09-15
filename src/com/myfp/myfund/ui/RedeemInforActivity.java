package com.myfp.myfund.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;

public class RedeemInforActivity extends BaseActivity {
	Bundle bundle;
	private RedeemSearchResult res;
	private String encodeIdCard, encodePassWord;
	TextView tv_redeeminfor_applycode, tv_redeeminfor_fundcode,
			tv_redeeminfor_fundname, tv_redeeminfor_shuhuifene,
			tv_redeeminfor_feneDX;
	Button bt_redeeminfor_back;
	private String sessionId;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_redeeminfor);
		bundle = getIntent().getExtras();
		// jinE = bundle.getString("JinE");
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		encodeIdCard = bundle.getString("IDCard");
		encodePassWord = bundle.getString("PassWord");
		System.out.println(bundle.getString("RedeemFenE"));
		sessionId = bundle.getString("sessionId");
		System.out.println("sssssssssssssssssss"+sessionId);
		System.out.println(bundle.getString("VastRedeemFlag").trim());

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("赎回信息");
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		LinearLayout layout1 = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		layout.setVisibility(View.VISIBLE);
		layout1.setVisibility(View.GONE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.home);
		// layout.setBackgroundColor(R.drawable.et);
		tv_redeeminfor_applycode = (TextView) findViewById(R.id.tv_redeeminfor_applycode);
		tv_redeeminfor_fundcode = (TextView) findViewById(R.id.tv_redeeminfor_fundCode);
		tv_redeeminfor_fundname = (TextView) findViewById(R.id.tv_redeeminfor_fundName);
		tv_redeeminfor_shuhuifene = (TextView) findViewById(R.id.tv_redeeminfor_shuhuifene);
		tv_redeeminfor_feneDX = (TextView) findViewById(R.id.tv_redeeminfor_feneDX);
		findViewAddListener(R.id.bt_redeeminfor_back);
		tv_redeeminfor_applycode.setText(bundle.getString("appsheetserialno"));
		tv_redeeminfor_fundcode.setText("["+res.getFundcode()+"]");
		tv_redeeminfor_fundname.setText(res.getFundname());
		tv_redeeminfor_shuhuifene.setText(bundle.getString("RedeemFenE"));
		tv_redeeminfor_feneDX.setText(new CnUpperCaser(bundle.getString("RedeemFenE")).getCnString());
		//tv_redeeminfor_feneDX
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			Intent intent = new Intent(RedeemInforActivity.this,MyActivityGroup.class);
			startActivity(intent);
		
			break;
		case R.id.bt_redeeminfor_back:
			

			Intent intent1 = new Intent(this,DealRedeemActivity.class);
			intent1.putExtra("sessionId", sessionId);
		
			startActivity(intent1);
			
			finish();
			RedeemApplyActivity.instance.finish();
		
			RedeemConfirmActivity.instance.finish();
			DealRedeemActivity.instance.finish();
		
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
			
			Intent intent1 = new Intent(RedeemInforActivity.this,DealRedeemActivity.class);
			intent1.putExtra("IDCard", encodeIdCard);
			intent1.putExtra("PassWord", encodePassWord);
			startActivity(intent1);
			finish();
			RedeemApplyActivity.instance.finish();
			RedeemConfirmActivity.instance.finish();
			DealRedeemActivity.instance.finish();
			
			}
		return super.onKeyDown(keyCode, event);
	}
	
	

}
