package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.net.ParseException;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.IDcard;


public class PurchaseHardActivity extends BaseActivity{

	private EditText edit_nam_text;
	private TextView tex_idcard_gs;
	private TextView tex_phen_gs;
	private TextView tex_form_gs;
	private TextView tex_usm_gs;
	private EditText edit_usm_gs;
	private EditText edit_recom_gs;
	private ImageView imag_remit_gs;
	private ImageView imag_online_gs;
	private String userName;
	private String idCard;
	private String mobile;
	private int GUS=1;
	private CheckBox checkBox1;
	private String amt,e,qian,hou;
	public static PurchaseHardActivity instance = null;
	private TextView text_nam_text;
	private EditText edit_idcard_gs;
	private TextView tex_ht_gs;
	private String name,idcard;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_buyfixed);
		instance=this;
		userName = App.getContext().getDepositacctName();
		idCard = App.getContext().getIdCard();
		mobile = App.getContext().getMobile();
	}

	@Override
	protected void initViews() {
		setTitle("购买固收");
		text_nam_text = (TextView) findViewById(R.id.text_nam_text);
		edit_nam_text = (EditText) findViewById(R.id.edit_nam_text);
		tex_idcard_gs = (TextView) findViewById(R.id.tex_idcard_gs);
		edit_idcard_gs = (EditText) findViewById(R.id.edit_idcard_gs);
		tex_phen_gs = (TextView) findViewById(R.id.tex_phen_gs);
		tex_form_gs = (TextView) findViewById(R.id.tex_form_gs);
		tex_usm_gs = (TextView) findViewById(R.id.tex_usm_gs);
		edit_usm_gs = (EditText) findViewById(R.id.edit_usm_gs);
		edit_recom_gs = (EditText) findViewById(R.id.edit_recom_gs);
		imag_online_gs = (ImageView) findViewById(R.id.imag_online_gs);
		imag_remit_gs = (ImageView) findViewById(R.id.imag_remit_gs);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		tex_ht_gs=(TextView) findViewById(R.id.tex_ht_gs);
		findViewAddListener(R.id.Relat_online_gs);
		findViewAddListener(R.id.Relat_remit_gs);
		findViewAddListener(R.id.but_hdl_gs);
		findViewAddListener(R.id.tex_ht_gs);
		findViewAddListener(R.id.edit_nam_text);
		findViewAddListener(R.id.edit_idcard_gs);
		text_nam_text.setVisibility(View.GONE);
		edit_nam_text.setVisibility(View.GONE);
		tex_idcard_gs.setVisibility(View.GONE);
		edit_idcard_gs.setVisibility(View.GONE);
		tex_idcard_gs.setText(idCard);
		text_nam_text.setText(userName);
		tex_phen_gs.setText(mobile);
		tex_form_gs.setText(getIntent().getStringExtra("Sname"));
		tex_usm_gs.setText(getIntent().getStringExtra("Sdlowlimit")+"万");
		if(getIntent().getStringExtra("form").equals("5")){
			tex_ht_gs.setText("《恒得利投资合同》");
		}else if(getIntent().getStringExtra("form").equals("6")){
			tex_ht_gs.setText("《恒有财投资合同》");
		}
		imag_online_gs.setVisibility(View.VISIBLE);
		imag_online_gs.setImageResource(R.drawable.gushouduigou);
		String sdl = getIntent().getStringExtra("Sdlowlimit");
		
		 qian = sdl.substring(0, sdl.lastIndexOf("-"));
		 hou = sdl.substring(sdl.lastIndexOf("-")+1,sdl.length());
		 
		
		if (idCard==null||idCard.equals("")) {
			edit_nam_text.setVisibility(View.VISIBLE);
			edit_idcard_gs.setVisibility(View.VISIBLE);
			
		}else {
			text_nam_text.setVisibility(View.VISIBLE);
			tex_idcard_gs.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.Relat_online_gs:
			GUS=1;
			imag_online_gs.setVisibility(View.VISIBLE);
			imag_online_gs.setImageResource(R.drawable.gushouduigou);
			imag_remit_gs.setVisibility(View.GONE);
			break;
		case R.id.Relat_remit_gs:
			GUS=2;
			imag_remit_gs.setVisibility(View.VISIBLE);
			imag_remit_gs.setImageResource(R.drawable.gushouduigou);
			imag_online_gs.setVisibility(View.GONE);
			break;
		case R.id.but_hdl_gs:
			 e=edit_usm_gs.getText().toString();
			 System.out.println("eeeeee"+e);
			if(edit_nam_text.getVisibility()==View.VISIBLE||edit_idcard_gs.getVisibility()==View.VISIBLE){
				if(edit_nam_text.getText().length()==0){
					showToast("请输入姓名");
					return;
				}else if (edit_nam_text.getText().toString()
						.matches("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")) {
					if(edit_idcard_gs.getText().length()==0){
						showToast("请输入身份证号");
						return;
					}else{
					ByteArrayInputStream is;

					try {
						is = new ByteArrayInputStream(edit_idcard_gs
								.getText().toString().getBytes("ISO-8859-1"));
						BufferedReader consoleReader = new BufferedReader(
								new InputStreamReader(is));
						String idNum = null;
						try {
							idNum = consoleReader.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String ii;
					
							ii = IDcard.IDCardValidate(idNum);
						
							
					if(ii!=""){
						showToast(ii);
						return;
						} 
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}else{
					showToast("姓名格式不不正确");
					return;
				}	
				userName = edit_nam_text.getText().toString();
				idcard=edit_idcard_gs.getText().toString();
			}else{
				userName = text_nam_text.getText().toString();
				idcard=tex_idcard_gs.getText().toString();
			}
			
			if (edit_usm_gs.getText().length()==0) {
				showToast("请输入购买金额");
				return;
			}else if(Double.parseDouble(e)<Double.parseDouble(qian)||Double.parseDouble(e)>Double.parseDouble(hou)){
				
				showToast("购买金额超出范围");
				return;
			}else if (!checkval(1)) {
				return;
			}else{
				
				 int parseInt = Integer.parseInt(edit_usm_gs.getText().toString());
				 int i = 10000;
				 int usm=parseInt*i;
				 amt = String.valueOf(usm);
				RequestParams params=new RequestParams(this);
				params.put("IDcard", idcard);
				params.put("DisplayName", userName);
				params.put("Mobile", mobile);
				execApi(ApiType.GET_UPDATEMESSAGE, params);
				if (GUS==1) {
					Intent intent=new Intent(this, FirmReceivePayActivity.class);
					intent.putExtra("userName", userName);
					intent.putExtra("mobile", mobile);
					intent.putExtra("op3_Amt", amt);
					intent.putExtra("opid", getIntent().getStringExtra("Id"));
					intent.putExtra("form", getIntent().getStringExtra("form"));
					startActivity(intent);
				}else if (GUS==2) {
					Intent intent=new Intent(this, OfflineReceiveActivity.class);
					intent.putExtra("userName", userName);
					intent.putExtra("mobile", mobile);
					intent.putExtra("op3_Amt", amt);
					intent.putExtra("Referral", edit_recom_gs.getText().toString());
					intent.putExtra("opid", getIntent().getStringExtra("id"));
					intent.putExtra("form", getIntent().getStringExtra("form"));
					startActivity(intent);
				}
			}
			break;
		case R.id.tex_ht_gs:
	
			Intent intent =new Intent(this,QustionActivity.class);
			intent.putExtra("name", "投资合同");
			intent.putExtra("type","1"+getIntent().getStringExtra("form"));
			startActivity(intent);
			break;
		case R.id.edit_nam_text:
			name=edit_nam_text.getText().toString();
			break;
		case R.id.edit_idcard_gs:
			idcard=edit_idcard_gs.getText().toString();
			break;
			default:
				break;
		}
		
	}
	private boolean checkval(int typ){
		if (typ==1) {
			if (!checkBox1.isChecked()) {
				showToast("请先阅读并同意相关服务条款!");
				return false;
			}
		}
		return true;
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (api==ApiType.GET_UPDATEMESSAGE) {
			try {
			
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
