package com.myfp.myfund.ui.view;

import com.myfp.myfund.R;
import com.myfp.myfund.ui.PlacementActivty;
import com.myfp.myfund.ui.view.FinancingDialog.DialogListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ButlertongDialog extends Dialog implements android.view.View.OnClickListener{
	private String depositacctName, proName, phoneNum;
	TextView tv_butlertongdialog_username, tv_butlertongdialog_proname,
			tv_butlertongdialog_phonenum, tv_butlertongdialog_changephone,
			tv_butlertongdialog_kefu, tv_butlertongdialog_ordernow,
			tv_butlertongdialog_orderlater;
	PlacementActivty activity;
	private Context context;
	private ButDialogListener listener;

	public ButlertongDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public interface ButDialogListener {
		public void onClick(View view);
	}

	public ButlertongDialog(Context context, String depositacctName, String proname,
			String phonenum, int theme, ButDialogListener listener) {
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
		this.setContentView(R.layout.dialog_butlertongdialog);
		tv_butlertongdialog_username = (TextView) findViewById(R.id.tv_butlertongdialog_username);
		tv_butlertongdialog_proname = (TextView) findViewById(R.id.tv_butlertongdialog_proname);
		tv_butlertongdialog_phonenum = (TextView) findViewById(R.id.tv_butlertongdialog_phonenum);
		tv_butlertongdialog_changephone = (TextView) findViewById(R.id.tv_butlertongdialog_changephone);
		tv_butlertongdialog_kefu = (TextView) findViewById(R.id.tv_butlertongdialog_kefu);
		tv_butlertongdialog_ordernow = (TextView) findViewById(R.id.tv_butlertongdialog_ordernow);
		tv_butlertongdialog_orderlater = (TextView) findViewById(R.id.tv_butlertongdialog_orderlater);

		tv_butlertongdialog_username.setText(depositacctName);
		tv_butlertongdialog_proname.setText("管家通会员服务");
		tv_butlertongdialog_phonenum.setText(phoneNum);
		
		tv_butlertongdialog_changephone.setOnClickListener(this);
		tv_butlertongdialog_kefu.setOnClickListener(this);
		tv_butlertongdialog_ordernow.setOnClickListener(this);
		tv_butlertongdialog_orderlater.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		 listener.onClick(v);

	}
}
