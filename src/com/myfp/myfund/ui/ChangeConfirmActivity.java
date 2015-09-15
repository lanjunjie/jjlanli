package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;
import com.myfp.myfund.utils.CustomDialog;
import com.myfp.myfund.utils.CustomDialog.InputDialogListener;
import com.myfp.myfund.utils.DESEncrypt;

public class ChangeConfirmActivity extends BaseActivity{
	ByteArrayInputStream tInputStringStream = null;
	public static ChangeConfirmActivity instance = null;
	Bundle bundle;
	private RedeemSearchResult res;
	TextView tv_changeconfirm_fundcode,tv_changeconfirm_fundname,tv_changeconfirm_changetargetfund,tv_changeconfirm_changefene,
	tv_changeconfirm_feneDX;
	Button bt_confirmchange;
	private String sessionId;
	private String PassWord;
	private String encodePassWord;
	private InputDialogListener  inputDialogListener;
	private  CustomDialog customDialog;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changeconfirm);
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金转换");
		tv_changeconfirm_fundcode = (TextView)findViewById(R.id.tv_changeconfirm_fundCode);
		tv_changeconfirm_fundname = (TextView)findViewById(R.id.tv_changeconfirm_fundName);
		tv_changeconfirm_changetargetfund = (TextView)findViewById(R.id.tv_changeconfirm_changetargetfund);
		tv_changeconfirm_changefene = (TextView)findViewById(R.id.tv_changeconfirm_changefene);
		tv_changeconfirm_feneDX = (TextView)findViewById(R.id.tv_changeconfirm_feneDX);
		findViewAddListener(R.id.bt_applychange);
		tv_changeconfirm_fundcode.setText("["+res.getFundcode()+"]");
		tv_changeconfirm_fundname.setText(res.getFundname());
		tv_changeconfirm_changetargetfund.setText(bundle.getString("ChangeTargetFund"));
		tv_changeconfirm_changefene.setText(bundle.getString("ChangeFenE")+"份");
		//tv_changeconfirm_fundcode.setText("["+res.getFundcode()+"]");
		tv_changeconfirm_feneDX.setText(new CnUpperCaser(bundle.getString("ChangeFenE")).getCnString()+"(份)");
		
	}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applychange:
			initDialog();
			break;

		default:
			break;
		}
	}
	private  void initDialog(){
		customDialog = new CustomDialog(ChangeConfirmActivity.this,R.style.mystyle,R.layout.customdialog) ;
		
		inputDialogListener  = new  InputDialogListener() {
			
			@Override
			public void onOK(String paww) {
				System.out.println("pawabc------------->"+paww);
				//给密码文本框设置密码
				DESEncrypt desEpt = new DESEncrypt();
				try {
					PassWord = desEpt.encrypt(paww);
					System.out.println("PassWord===========>"+PassWord);
					encodePassWord = java.net.URLEncoder
							.encode(PassWord);
					System.out.println("encodePassWord-------------->"+encodePassWord);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				applyChage();
			}
			
		};
		customDialog.setListener(inputDialogListener);
		customDialog.show();
	}
	
	void applyChage() {

		showProgressDialog("正在确认...");
		
		RequestParams params = new RequestParams(this);
		params.put("sessionId", sessionId);
		//params.put("id", bundle.getString("IDCard"));
		params.put("passwd", encodePassWord);
		params.put("fundcode", res.getFundcode());
		params.put("applicationamount", bundle.getString("ChangeFenE"));
		params.put("targetfundcode",  bundle.getString("TargetFundCode"));
		params.put("tano", res.getTano());
		params.put("transactionaccountid", res.getTransactionaccountid());
		// params.put(RequestParams.MOBILE, username);
		
		execApi(ApiType.GET_CHANGEFUNDTWO, params);
	}
	
	protected void dialog(){
		AlertDialog.Builder builder=new Builder(this);
		builder.setMessage("密码输入错误，请重新输入。");
		builder.setTitle("提示");
		builder.setPositiveButton("重试", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initDialog();
				
			}
		});

		builder.create().show();
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_CHANGEFUNDTWO) {

				if (json != null && !json.equals("")) {
					tInputStringStream = new ByteArrayInputStream(
							json.getBytes());
					XmlPullParser parser = Xml.newPullParser();
					try {
						parser.setInput(tInputStringStream, "UTF-8");
						int event = parser.getEventType();
						while (event != XmlPullParser.END_DOCUMENT) {
							Log.i("start_document", "start_document");
							switch (event) {
							case XmlPullParser.START_TAG:
								if ("return".equals(parser.getName())) {
									try {
										String xmlReturn = parser.nextText();
										System.out.println("<><><><><><><><><>"
												+ xmlReturn);

										try {
											if(xmlReturn.contains("appsheetserialno")){
												JSONObject jsonObj = new JSONObject(xmlReturn);
												
												//转换申请单编号
												String applyCode = jsonObj.getString("appsheetserialno");
												//System.out.println("ddddddddddddddddddddddd"+applyCode);
												
												if (!(applyCode==null)) {
													
													disMissDialog();
													showToast("转换成功！！");
													bundle.putString("appsheetserialno", applyCode);
													bundle.putString("sessionId", sessionId);
													Intent intent = new Intent(ChangeConfirmActivity.this,ChangeInforActivity.class);
													intent.putExtras(bundle);
													startActivity(intent);
											
													

												} else {
													disMissDialog();
													showToast("转换失败！！");
													return;
												}
											}else{
												JSONObject jsonObj = new JSONObject(xmlReturn);
												dialog();
												disMissDialog();
												return;
											}
											
											

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								break;

							}
							try {
								event = parser.next();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							tInputStringStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}

		}
	}

}
