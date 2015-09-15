package com.myfp.myfund.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.ChangeMobileActivity;
import com.myfp.myfund.ui.PlacementActivty;

public class OrderDialog extends Dialog implements
		android.view.View.OnClickListener {
	private String depositacctName, proName, phoneNum;
	TextView tv_orderdialog_username, tv_orderdialog_proname,
			tv_orderdialog_phonenum, tv_orderdialog_changephone,
			tv_orderdialog_kefu, tv_orderdialog_ordernow,
			tv_orderdialog_orderlater;
	PlacementActivty activity;
	private Context context;
	private MyDialogListener listener;

	public OrderDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public interface MyDialogListener {
		public void onClick(View view);
	}

	public OrderDialog(Context context, String depositacctName, String proname,
			String phonenum, int theme, MyDialogListener listener) {
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
		this.setContentView(R.layout.dialog_orderdialog);
		tv_orderdialog_username = (TextView) findViewById(R.id.tv_orderdialog_username);
		tv_orderdialog_proname = (TextView) findViewById(R.id.tv_orderdialog_proname);
		tv_orderdialog_phonenum = (TextView) findViewById(R.id.tv_orderdialog_phonenum);
		tv_orderdialog_changephone = (TextView) findViewById(R.id.tv_orderdialog_changephone);
		tv_orderdialog_kefu = (TextView) findViewById(R.id.tv_orderdialog_kefu);
		tv_orderdialog_orderlater = (TextView) findViewById(R.id.tv_orderdialog_orderlater);
		tv_orderdialog_ordernow = (TextView) findViewById(R.id.tv_orderdialog_ordernow);

		tv_orderdialog_username.setText(depositacctName);
		tv_orderdialog_proname.setText(proName);
		tv_orderdialog_phonenum.setText(phoneNum);
		
		tv_orderdialog_changephone.setOnClickListener(this);
		tv_orderdialog_kefu.setOnClickListener(this);
		tv_orderdialog_ordernow.setOnClickListener(this);
		tv_orderdialog_orderlater.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		 listener.onClick(v);

	}

}
