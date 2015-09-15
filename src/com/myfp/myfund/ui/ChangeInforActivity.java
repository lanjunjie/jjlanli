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

public class ChangeInforActivity extends BaseActivity {
	Bundle bundle;
	private RedeemSearchResult res;
	private String encodeIdCard, encodePassWord;
	TextView tv_changeinfor_applycode, tv_changeinfor_fundcode,
			tv_changeinfor_targetfund, tv_changeinfor_fundname,
			tv_changeinfor_changefene, tv_changeinfor_feneDX;
	Button bt_changeinfor_back;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changeinfor);
		bundle = getIntent().getExtras();
		// jinE = bundle.getString("JinE");
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		encodeIdCard = bundle.getString("IDCard");
		encodePassWord = bundle.getString("PassWord");
//		System.out.println(bundle.getString("RedeemFenE"));
//		System.out.println(bundle.getString("VastRedeemFlag").trim());
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金转换");
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		LinearLayout layout1 = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		layout.setVisibility(View.VISIBLE);
		layout1.setVisibility(View.GONE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.home);

		tv_changeinfor_applycode = (TextView) findViewById(R.id.tv_changeinfor_applycode);
		tv_changeinfor_fundcode = (TextView) findViewById(R.id.tv_changeinfor_fundCode);
		tv_changeinfor_fundname = (TextView) findViewById(R.id.tv_changeinfor_fundName);
		tv_changeinfor_changefene = (TextView) findViewById(R.id.tv_changeinfor_changefene);
		tv_changeinfor_targetfund = (TextView) findViewById(R.id.tv_changeinfor_targetfund);
		tv_changeinfor_feneDX = (TextView) findViewById(R.id.tv_changeinfor_feneDX);
		findViewAddListener(R.id.bt_changeinfor_back);
		tv_changeinfor_applycode.setText(bundle.getString("appsheetserialno"));
		tv_changeinfor_fundcode.setText("[" + res.getFundcode() + "]");
		tv_changeinfor_fundname.setText(res.getFundname());
		tv_changeinfor_changefene.setText(bundle.getString("ChangeFenE")+"份");
		tv_changeinfor_feneDX.setText(new CnUpperCaser(bundle.getString("ChangeFenE")).getCnString()+"(份)");
		tv_changeinfor_targetfund.setText(bundle.getString("ChangeTargetFund"));
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			Intent intent = new Intent(ChangeInforActivity.this,
					MyActivityGroup.class);
			startActivity(intent);

			break;
		case R.id.bt_changeinfor_back:

			Intent intent1 = new Intent(ChangeInforActivity.this,
					DealChangeActivity.class);
			//intent1.putExtra("IDCard", encodeIdCard);
			//intent1.putExtra("PassWord", encodePassWord);
			intent1.putExtra("sessionId", bundle.getString("sessionId"));
			startActivity(intent1);

			finish();
			ChangeApplyActivity.instance.finish();
			ChangeConfirmActivity.instance.finish();
			DealChangeActivity.instance.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键

			Intent intent1 = new Intent(ChangeInforActivity.this,
					DealChangeActivity.class);
			intent1.putExtra("IDCard", encodeIdCard);
			intent1.putExtra("PassWord", encodePassWord);
			startActivity(intent1);
			finish();
			ChangeApplyActivity.instance.finish();
			ChangeConfirmActivity.instance.finish();
			DealChangeActivity.instance.finish();

		}
		return super.onKeyDown(keyCode, event);
	}

}
