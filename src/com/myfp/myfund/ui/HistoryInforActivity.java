package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.EntrustSearchResult;
import com.myfp.myfund.api.beans.HistorySearchResult;

public class HistoryInforActivity extends BaseActivity {
	TextView tv_historyinfor_yewu, tv_historyinfor_accountnum,
			tv_historyinfor_fundcode, tv_historyinfor_fundname,
			tv_historyinfor_applyjine, tv_historyinfor_confirmfene,
			tv_historyinfor_paycanal, tv_historyinfor_fenevalue,
			tv_historyinfor_counterfee, tv_historyinfor_confirmdate,
			tv_historyinfor_comfirminfor, tv_historyinfor_fenhong,
			tv_historyinfor_oldapplycode, tv_historyinfor_TAcodenum,
			tv_historyinfor_targetfundcode, tv_historyinfor_targetfundname,
			tv_historyinfor_paystatus, tv_historyinfor_managestatus,
			tv_historyinfor_manageinstruction, tv_historyinfor_bankcardnnum,
			tv_historyinfor_applyfene, tv_historyinfor_confirmjine;
	private HistorySearchResult res;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	public static HistoryInforActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_historyinfor);
		instance = this;
		bundle = getIntent().getExtras();
		res = (HistorySearchResult) bundle
				.getSerializable("HistorySearchResult");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("交易确认明细");

		tv_historyinfor_oldapplycode = (TextView) findViewById(R.id.tv_historyinfor_oldapplycode);
		tv_historyinfor_paycanal = (TextView) findViewById(R.id.tv_historyinfor_paycanal);
		tv_historyinfor_accountnum = (TextView) findViewById(R.id.tv_historyinfor_accountnum);
		tv_historyinfor_applyjine = (TextView) findViewById(R.id.tv_historyinfor_applyjine);
		tv_historyinfor_confirmfene = (TextView) findViewById(R.id.tv_historyinfor_confirmfene);
		tv_historyinfor_counterfee = (TextView) findViewById(R.id.tv_historyinfor_counterfee);
		tv_historyinfor_confirmdate = (TextView) findViewById(R.id.tv_historyinfor_confirmdate);
		tv_historyinfor_fenhong = (TextView) findViewById(R.id.tv_historyinfor_fenhong);
		tv_historyinfor_TAcodenum = (TextView) findViewById(R.id.tv_historyinfor_TAcodenum);
		tv_historyinfor_fundcode = (TextView) findViewById(R.id.tv_historyinfor_fundcode);
		tv_historyinfor_fundname = (TextView) findViewById(R.id.tv_historyinfor_fundname);
		tv_historyinfor_yewu = (TextView) findViewById(R.id.tv_historyinfor_yewu);
		tv_historyinfor_applyfene = (TextView) findViewById(R.id.tv_historyinfor_applyfene);
		tv_historyinfor_confirmjine = (TextView) findViewById(R.id.tv_historyinfor_confirmjine);
		tv_historyinfor_fenevalue = (TextView) findViewById(R.id.tv_historyinfor_fenevalue);
		tv_historyinfor_comfirminfor = (TextView) findViewById(R.id.tv_historyinfor_confirminfor);
		tv_historyinfor_targetfundcode = (TextView) findViewById(R.id.tv_historyinfor_targetfundcode);
		tv_historyinfor_targetfundname = (TextView) findViewById(R.id.tv_historyinfor_targetfundname);
		if (res.getAppsheetserialno().length()==0) {

			tv_historyinfor_oldapplycode.setText("--");
		} else {
			tv_historyinfor_oldapplycode.setText(res.getAppsheetserialno());
		}
		tv_historyinfor_paycanal.setText(res.getApplicationamount());
		if (res.getPaycenterid().equals("0010")) {
			tv_historyinfor_paycanal.setText("银联渠道");
		} else if (res.getPaycenterid().equals("0108")) {
			tv_historyinfor_paycanal.setText("民生渠道");
		} else if (res.getPaycenterid().equals("0204")) {
			tv_historyinfor_paycanal.setText("易宝支付渠道");
		} else if (res.getPaycenterid().equals("0302")) {
			tv_historyinfor_paycanal.setText("通联渠道");
		} else {
			tv_historyinfor_paycanal.setText("汇款支付渠道");
		}
		tv_historyinfor_accountnum.setText(res.getTaaccountid());
		tv_historyinfor_applyjine.setText(res.getApplicationamount()+"元");
		tv_historyinfor_confirmfene.setText(res.getConfirmedvol()+"份");
		tv_historyinfor_counterfee.setText(res.getCharge());
		tv_historyinfor_confirmdate.setText(res.getTransactioncfmdate());

		if (res.getDefdividendmethod().equals("1")) {
			tv_historyinfor_fenhong.setText("现金分红");
		} else {
			tv_historyinfor_fenhong.setText("红利再投");
		}

		tv_historyinfor_TAcodenum.setText(res.getTaserialno());
		tv_historyinfor_fundcode.setText("[" + res.getFundcode() + "]");
		tv_historyinfor_fundname.setText(res.getFundname());
		tv_historyinfor_yewu.setText(bundle.getString("YeWu"));
		tv_historyinfor_applyfene.setText(res.getApplicationvol()+"份");
		tv_historyinfor_confirmjine.setText(res.getConfirmedamount()+"元");
		tv_historyinfor_fenevalue.setText(res.getNav());
		if (res.getReturncode().equals("0000")) {
			tv_historyinfor_comfirminfor.setText("成功");
		} else {
			tv_historyinfor_comfirminfor.setText(res.getReturnmsg());
		}

		tv_historyinfor_targetfundcode.setText("[" + res.getTargetfundcode()
				+ "]");
		tv_historyinfor_targetfundname.setText(res.getTargetfundname());

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
