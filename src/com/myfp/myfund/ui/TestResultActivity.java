package com.myfp.myfund.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class TestResultActivity extends BaseActivity{
	TextView tv_testresult_risklevel,tv_testresult_risktype,tv_testresult_accept,tv_testresult_return;
	Intent intent;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_testresult);
		intent = getIntent();
		
	}

	@Override
	protected void initViews() {
		setTitle("测试结果");
		tv_testresult_risklevel = (TextView)findViewById(R.id.tv_testresult_risklevel);
		tv_testresult_risktype = (TextView)findViewById(R.id.tv_testresult_risktype);
		tv_testresult_accept = (TextView)findViewById(R.id.tv_testresult_accept);
		tv_testresult_return = (TextView)findViewById(R.id.tv_testresult_return);
		findViewAddListener(R.id.bt_testresult_back);
		if(intent.getStringExtra("CustomRiskLevel").equals("05")){
			tv_testresult_risklevel.setText("风险等级：激进型");
			tv_testresult_risktype.setText(R.string.risklevel05);
			tv_testresult_accept.setText(R.string.risklevel05_accept);
			tv_testresult_return.setText(R.string.risklevel05_return);
			
		}else if(intent.getStringExtra("CustomRiskLevel").equals("04")){
			tv_testresult_risklevel.setText("风险等级：积极型");
			tv_testresult_risktype.setText(R.string.risklevel04);
			tv_testresult_accept.setText(R.string.risklevel04_accept);
			tv_testresult_return.setText(R.string.risklevel04_return);
		}else if(intent.getStringExtra("CustomRiskLevel").equals("03")){
			tv_testresult_risklevel.setText("风险等级：稳健型");
			tv_testresult_risktype.setText(R.string.risklevel03);
			tv_testresult_accept.setText(R.string.risklevel03_accept);
			tv_testresult_return.setText(R.string.risklevel03_return);
		}else if(intent.getStringExtra("CustomRiskLevel").equals("02")){
			tv_testresult_risklevel.setText("风险等级：保守型");
			tv_testresult_risktype.setText(R.string.risklevel02);
			tv_testresult_accept.setText(R.string.risklevel02_accept);
			tv_testresult_return.setText(R.string.risklevel02_return);
		}else if(intent.getStringExtra("CustomRiskLevel").equals("01")){
			tv_testresult_risklevel.setText("风险等级：安逸型");
			tv_testresult_risktype.setText(R.string.risklevel01);
			tv_testresult_accept.setText(R.string.risklevel01_accept);
			tv_testresult_return.setText(R.string.risklevel01_return);
		}
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_testresult_back:
			
			
				Intent intent1 = new Intent(
						TestResultActivity.this,
						MyActivityGroup.class);
				intent1.putExtra("IDCard",
						intent.getStringExtra("IDCard"));
				intent1.putExtra(
						"CustomRiskLevel",
						intent.getStringExtra("CustomRiskLevel"));
				intent1.putExtra(
						"DepositacctName",
						intent.getStringExtra("DepositacctName"));
				intent1.putExtra(
						"TotalFundMarketValue",
						intent.getStringExtra("TotalFundMarketValue"));
				intent1.putExtra(
						"CountFund",
						intent.getStringExtra("CountFund"));
				intent1.putExtra("Flag", "5");
				intent1.putExtra("sessionId", getIntent().getStringExtra("sessionId"));
				startActivity(intent1);
				finish();
			
			
			
			break;

		default:
			break;
		}
	}

}
