package com.myfp.myfund.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class UponGoodspassActivity extends BaseActivity{

	private TextView tex_member_sume;
	private TextView tex_tex_membership_sume;
	private String amount;
	private String member;
	private int unt;
	private String sume;
	private String form;
	private String id;
	private int mem;
	private int amou;
	private String formtwo;
	private int code,count;
	private String username;

	@Override
	protected void setContentView() {
		setContentView(R.layout.fragment_upongoodspass);
		Intent intent = getIntent();
		amount = intent.getStringExtra("Amount");
		id = intent.getStringExtra("ID");
		amou = Integer.parseInt(amount);
		username=intent.getStringExtra("userName");

	}

	@Override
	protected void initViews() {
		setTitle("购买点财通");
		findViewAddListener(R.id.linear_Member_type);
		findViewAddListener(R.id.linear_Membership);
		findViewAddListener(R.id.bun_Membership_next);
		tex_member_sume = (TextView) findViewById(R.id.tex_member_sume);
		tex_tex_membership_sume = (TextView) findViewById(R.id.tex_tex_membership_sume);
		tex_tex_membership_sume.setText(amount+"元");
		if (member==null) {
			tex_member_sume.setText("一年期会员\n399元");
			unt = 399-amou;
			form = "一年期会员";
			formtwo = "年度会员/￥399";
		}
		
	}

	@SuppressWarnings("MemDialog") 
	public void MemDialog(){
		final String Mem[]={"一年期会员399元","三年期会员799元","终身会员1999元"};
		new AlertDialog.Builder(this).setTitle("点财通会员种类") 
		.setItems(Mem, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				int mem = which;
				
				member = Integer.toString(mem);
				System.out.println("你以经选中了pay："+which);
				System.err.println("以选择pay："+member);
				if (mem==0) {
					tex_member_sume.setText("一年期会员\n399元");
				}else if (mem==1) {
					tex_member_sume.setText("三年期会员\n799元");
				}else if (mem==2) {
					tex_member_sume.setText("终身会员\n1999元");
				}

				if (mem==0) {
					unt = 399-amou;
				}else if (mem==1) {
					unt=799-amou;
				}else if (mem==2) {
					unt=1999-amou;
				}
				
				if (mem==0) {
					form = "一年期会员";
				}else if (mem==1) {
					form="三年期会员";
				}else if (mem==2) {
					form="终身会员";
				}
				if (mem==0) {
					formtwo = "年度会员/￥399";
				}else if (mem==1) {
					formtwo="年度会员/￥799";
				}else if (mem==2) {
					formtwo="年度会员/￥1999";
				}
				System.out.println("formtwo========="+formtwo);
				
					
				
			}
		}).show();
	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.linear_Member_type:
			MemDialog();
			break;
		case R.id.bun_Membership_next:
	
		   
			sume = Integer.toString(unt);
			Intent intent=new Intent(this,ThrougHpayActivity.class);
			intent.putExtra("Sume", sume);
			intent.putExtra("Form", form);
			intent.putExtra("ID", id);
			intent.putExtra("Formtwo", formtwo);
			intent.putExtra("userName", username);
			startActivity(intent);
			
			break;

		default:
			break;
		}
	}

}
