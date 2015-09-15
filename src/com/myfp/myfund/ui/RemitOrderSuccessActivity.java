package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;

public class RemitOrderSuccessActivity extends BaseActivity {
	Bundle bundle;
	private DealSearchResult res;
	TextView tv_remitorder_applycode, tv_remitorder_fundcode,
			tv_remitorder_fundname, tv_remitorder_jine, tv_remitorder_jineDX,
			tv_remitorder_bankcardnum,tv_remitorder_paytype,tv_remitorder_idcardnum,tv_remitorder_step03_bankcode;
	
	private String  encodeIdCard, encodePassWord, idcardshow;
	ByteArrayInputStream tInputStringStream = null;
	private String sessionId;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_remitordersuccess);
		bundle = getIntent().getExtras();
		encodeIdCard = bundle.getString("IDCard");
		encodePassWord = bundle.getString("PassWord");
		sessionId = bundle.getString("sessionId");
		idcardshow = bundle.getString("Certificateno");
		res = (DealSearchResult) bundle.getSerializable("DealSearchResult");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("您的基金买入下单成功");
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		layout.setVisibility(View.GONE);
		
		tv_remitorder_applycode = (TextView) findViewById(R.id.tv_remitorder_applycode);
		tv_remitorder_fundcode = (TextView) findViewById(R.id.tv_remitorder_fundCode);
		tv_remitorder_fundname = (TextView) findViewById(R.id.tv_remitorder_fundName);
		tv_remitorder_jine = (TextView) findViewById(R.id.tv_remitorder_dealjine);
		tv_remitorder_jineDX = (TextView) findViewById(R.id.tv_remitorder_jineDX);
		tv_remitorder_bankcardnum = (TextView) findViewById(R.id.tv_remitorder_bankcardnum);
		tv_remitorder_paytype = (TextView) findViewById(R.id.tv_remitorder_paytype);
		tv_remitorder_idcardnum = (TextView) findViewById(R.id.tv_remitorder_idcardnum);
		tv_remitorder_step03_bankcode = (TextView) findViewById(R.id.tv_remitorder_step03_bankcode);
		findViewAddListener(R.id.bt_remitorder_back);

		tv_remitorder_applycode.setText(bundle.getString("appsheetserialno"));
		tv_remitorder_fundcode.setText("[" + res.getFundCode() + "]");
		tv_remitorder_fundname.setText(res.getFundName());
		tv_remitorder_jine.setText(bundle.getString("JinE")+"元");
		tv_remitorder_jineDX.setText(new CnUpperCaser(bundle.getString("JinE")).getCnString()+"元");
		tv_remitorder_bankcardnum.setText(bundle.getString("BankCardCodeShow"));
		if(bundle.getString("PayType").equals("2")){
			tv_remitorder_paytype.setText("汇款支付");
		}
		
		if (idcardshow.length() == 18) {
			tv_remitorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(11, 16), "*****"));
		} else if (idcardshow.length() == 15) {
			tv_remitorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(8, 13), "*****"));
		}
		tv_remitorder_step03_bankcode.setText(bundle.getString("BankCardCodeShow"));
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_remitorder_back:
			
			Intent intent = new Intent(RemitOrderSuccessActivity.this,DealBuyActivity.class);
			//intent.putExtra("IDCard", encodeIdCard);
			//intent.putExtra("PassWord", encodePassWord);
			intent.putExtra("sessionId", sessionId);
			startActivity(intent);
			
			finish();
			DealBuyActivity.instance.finish();
			DealApplyActivity.instance.finish();
			DealConfirmActivity.instance.finish();
			break;

		default:
			break;
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
			
			Intent intent1 = new Intent(RemitOrderSuccessActivity.this,DealBuyActivity.class);
			//intent1.putExtra("IDCard", encodeIdCard);
			//intent1.putExtra("PassWord", encodePassWord);
			intent1.putExtra("sessionId", sessionId);
			startActivity(intent1);
			finish();
			DealBuyActivity.instance.finish();
			DealApplyActivity.instance.finish();
			DealConfirmActivity.instance.finish();
			
			}
		return super.onKeyDown(keyCode, event);
	}

}
