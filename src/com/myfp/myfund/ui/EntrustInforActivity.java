package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.EntrustSearchResult;
import com.myfp.myfund.api.beans.RedeemSearchResult;

public class EntrustInforActivity extends BaseActivity {
	TextView tv_entrustinfor_applycode,tv_entrustinfor_yewu, tv_entrustinfor_accountnum,
			tv_entrustinfor_fundcode, tv_entrustinfor_fundname,
			tv_entrustinfor_jine, tv_entrustinfor_fene,
			tv_entrustinfor_paycanal, tv_entrustinfor_paytype,
			tv_entrustinfor_orderdate, tv_entrustinfor_ordertime,
			tv_entrustinfor_applydate, tv_entrustinfor_fenhong,
			tv_entrustinfor_oldapplycode, tv_entrustinfor_agreementcode,
			tv_entrustinfor_targetfundcode, tv_entrustinfor_targetfundname,
			tv_entrustinfor_paystatus, tv_entrustinfor_managestatus,
			tv_entrustinfor_manageinstruction, tv_entrustinfor_bankcardnnum;
	private EntrustSearchResult res;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	public static EntrustInforActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_entrustinfor);	
		instance = this;
		bundle = getIntent().getExtras();
		res = (EntrustSearchResult) bundle
				.getSerializable("EntrustSearchResult");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("交易申请明细");
		tv_entrustinfor_applycode = (TextView)findViewById(R.id.tv_entrustinfor_applycode);
		tv_entrustinfor_yewu = (TextView)findViewById(R.id.tv_entrustinfor_yewu);
		tv_entrustinfor_accountnum = (TextView)findViewById(R.id.tv_entrustinfor_accountnum);
		tv_entrustinfor_fundcode = (TextView)findViewById(R.id.tv_entrustinfor_fundcode);
		tv_entrustinfor_fundname = (TextView)findViewById(R.id.tv_entrustinfor_fundname);
		tv_entrustinfor_jine = (TextView)findViewById(R.id.tv_entrustinfor_jine);
		tv_entrustinfor_fene = (TextView)findViewById(R.id.tv_entrustinfor_fene);
		tv_entrustinfor_paycanal = (TextView)findViewById(R.id.tv_entrustinfor_paycanal);
		tv_entrustinfor_paytype = (TextView)findViewById(R.id.tv_entrustinfor_paytype);
		tv_entrustinfor_orderdate = (TextView)findViewById(R.id.tv_entrustinfor_orderdate);
		tv_entrustinfor_ordertime = (TextView)findViewById(R.id.tv_entrustinfor_ordertime);
		tv_entrustinfor_applydate = (TextView)findViewById(R.id.tv_entrustinfor_applydate);
		tv_entrustinfor_fenhong = (TextView)findViewById(R.id.tv_entrustinfor_fenhong);
		tv_entrustinfor_oldapplycode = (TextView)findViewById(R.id.tv_entrustinfor_oldapplycode);
		tv_entrustinfor_agreementcode = (TextView)findViewById(R.id.tv_entrustinfor_agreementcode);
		tv_entrustinfor_targetfundcode = (TextView)findViewById(R.id.tv_entrustinfor_targetfundcode);
		tv_entrustinfor_targetfundname = (TextView)findViewById(R.id.tv_entrustinfor_targetfundname);
		tv_entrustinfor_paystatus = (TextView)findViewById(R.id.tv_entrustinfor_paystatus);
		tv_entrustinfor_managestatus = (TextView)findViewById(R.id.tv_entrustinfor_managestatus);
		tv_entrustinfor_manageinstruction = (TextView)findViewById(R.id.tv_entrustinfor_manageinstruction);
		tv_entrustinfor_bankcardnnum = (TextView)findViewById(R.id.tv_entrustinfor_bankcardnnum);
		
		
		tv_entrustinfor_applycode.setText(res.getAppsheetserialno());
		tv_entrustinfor_yewu.setText(bundle.getString("YeWu"));
		tv_entrustinfor_accountnum.setText(res.getTaaccountid());
		tv_entrustinfor_fundcode.setText("["+res.getFundcode()+"]");
		tv_entrustinfor_fundname.setText(res.getFundname());
		tv_entrustinfor_jine.setText(res.getApplicationamount()+"元");
		if(res.getPaycenterid().equals("0010")){
			tv_entrustinfor_paycanal.setText("银联渠道");
		}else if(res.getPaycenterid().equals("0108")){
			tv_entrustinfor_paycanal.setText("民生渠道");
		}
		else if(res.getPaycenterid().equals("0204")){
			tv_entrustinfor_paycanal.setText("易宝支付渠道");
		}
		else if(res.getPaycenterid().equals("0302")){
			tv_entrustinfor_paycanal.setText("通联渠道");
		}
		else{
			tv_entrustinfor_paycanal.setText("汇款支付渠道");
		}
		tv_entrustinfor_fene.setText(res.getApplicationvol()+"份");
		if(res.getPaytype().equals("1")){
			tv_entrustinfor_paytype.setText("银行转账");
		}else if(res.getPaytype().equals("2")){
			tv_entrustinfor_paytype.setText("银行汇款");
		}else{
			tv_entrustinfor_paytype.setText("--");
		}
		
		tv_entrustinfor_orderdate.setText(res.getOperdate());
		tv_entrustinfor_ordertime.setText(res.getOpertime());
		tv_entrustinfor_applydate.setText(res.getTransactiondate());
		if(res.getDefdividendmethod().equals("1")){
			tv_entrustinfor_fenhong.setText("现金分红");
		}else{
			tv_entrustinfor_fenhong.setText("红利再投");
		}
		if(res.getOriginalappsheetno().contains(" ")){
			
			tv_entrustinfor_oldapplycode.setText("--");
		}else{
			tv_entrustinfor_oldapplycode.setText(res.getOriginalappsheetno());
		}
		
		tv_entrustinfor_agreementcode.setText(res.getProtocolno());
		tv_entrustinfor_targetfundcode.setText("["+res.getTargetbranchcode()+"]");
		tv_entrustinfor_targetfundname.setText(res.getTargetfundname());
		if(res.getPaystatus().equals("00")){
			tv_entrustinfor_paystatus.setText("未支付");
		}else if(res.getPaystatus().equals("01")){
			tv_entrustinfor_paystatus.setText("委托正在处理");
		}else if(res.getPaystatus().equals("02")){
			tv_entrustinfor_paystatus.setText("支付成功");
		}else if(res.getPaystatus().equals("03")){
			tv_entrustinfor_paystatus.setText("支付失败");
		}else if(res.getPaystatus().equals("07")){
			tv_entrustinfor_paystatus.setText("托收成功");
		}else{
			tv_entrustinfor_paystatus.setText("--");
		}
		if(res.getStatus().equals("00")){
			tv_entrustinfor_managestatus.setText("待复核");
		}else if(res.getStatus().equals("01")){
			tv_entrustinfor_managestatus.setText("待勾兑");
		}else if(res.getStatus().equals("02")){
			tv_entrustinfor_managestatus.setText("待报");
		}
		else if(res.getStatus().equals("04")){
			tv_entrustinfor_managestatus.setText("废单");
		}
		else if(res.getStatus().equals("05")){
			tv_entrustinfor_managestatus.setText("已撤");
		}
		else if(res.getStatus().equals("06")){
			tv_entrustinfor_managestatus.setText("已报");
		}
		else if(res.getStatus().equals("07")){
			tv_entrustinfor_managestatus.setText("已确认");
		}
		else if(res.getStatus().equals("08")){
			tv_entrustinfor_managestatus.setText("已结束");
		}
		
		tv_entrustinfor_manageinstruction.setText("--");
		if(res.getDepositacct().length()<6){
			tv_entrustinfor_bankcardnnum.setText(res.getDepositacct().replace(res.getDepositacct().substring(res.getDepositacct().length()-5, res.getDepositacct().length()-2), "***"));
		}else{
			tv_entrustinfor_bankcardnnum.setText(res.getDepositacct().replace(res.getDepositacct().substring(res.getDepositacct().length()-6, res.getDepositacct().length()-3), "***"));
		}

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
