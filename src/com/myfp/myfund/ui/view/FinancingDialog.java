package com.myfp.myfund.ui.view;

import com.myfp.myfund.R;
import com.myfp.myfund.ui.PlacementActivty;
import com.myfp.myfund.ui.view.OrderDialog.MyDialogListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FinancingDialog extends Dialog implements android.view.View.OnClickListener{
	private String depositacctName, proName, phoneNum;
	TextView tv_financingdialog_username, tv_financingdialog_proname,
			tv_financingdialog_phonenum, tv_financingdialog_changephone,
			tv_financingdialog_kefu, tv_financingdialog_ordernow,
			tv_financingdialog_orderlater;
	PlacementActivty activity;
	private Context context;
	private DialogListener listener;

	public FinancingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public interface DialogListener {
		public void onClick(View view);
	}

	public FinancingDialog(Context context, String depositacctName, String proname,
			String phonenum, int theme, DialogListener listener) {
		super(context, theme);
		this.depositacctName = depositacctName;
		this.proName = proname;
		this.phoneNum = phonenum;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_financingdialog);
		tv_financingdialog_username = (TextView) findViewById(R.id.tv_financingdialog_username);
		tv_financingdialog_proname = (TextView) findViewById(R.id.tv_financingdialog_proname);
		tv_financingdialog_phonenum = (TextView) findViewById(R.id.tv_financingdialog_phonenum);
		tv_financingdialog_changephone = (TextView) findViewById(R.id.tv_financingdialog_changephone);
		tv_financingdialog_kefu = (TextView) findViewById(R.id.tv_financingdialog_kefu);
		tv_financingdialog_ordernow = (TextView) findViewById(R.id.tv_financingdialog_ordernow);
		tv_financingdialog_orderlater = (TextView) findViewById(R.id.tv_financingdialog_orderlater);

		tv_financingdialog_username.setText(depositacctName);
		tv_financingdialog_proname.setText("点融通会员");
		tv_financingdialog_phonenum.setText(phoneNum);
		
		tv_financingdialog_changephone.setOnClickListener(this);
		tv_financingdialog_kefu.setOnClickListener(this);
		tv_financingdialog_ordernow.setOnClickListener(this);
		tv_financingdialog_orderlater.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		 listener.onClick(v);

	}

}
