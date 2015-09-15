package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
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
import android.widget.EditText;
import android.widget.TextView;


import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;
import com.myfp.myfund.utils.CustomDialog;
import com.myfp.myfund.utils.CustomDialog.InputDialogListener;
import com.myfp.myfund.utils.DESEncrypt;
/*
 * 赎回确认
 * */
public class  RedeemConfirmActivity extends BaseActivity{
	
	Bundle bundle;
	private RedeemSearchResult res;
	TextView tv_redeemconfirm_fundcode,tv_redeemconfirm_fundname,tv_redeemconfirm_chiyoufene,tv_redeemconfirm_dongjiefee,tv_redeemconfirm_shuhuifene,
	tv_redeemconfirm_feneDX,tv_redeemconfirm_flag;
	Button bt_confirmredeem;
	private String jinE;
	private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	public static RedeemConfirmActivity instance = null;
	private String sessionId;
	private String PassWord;

private  CustomDialog customDialog;
private InputDialogListener  inputDialogListener;
private String msg;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_redeemconfirm);
		instance = this;
		bundle = getIntent().getExtras();
		jinE = bundle.getString("JinE");
		sessionId = bundle.getString("sessionId");
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		System.out.println(bundle.getString("RedeemFenE"));
		System.out.println(bundle.getString("VastRedeemFlag").trim());
		String transactionaccountid = res.getTransactionaccountid();
		System.out.println("transactionaccountid==========>"+transactionaccountid);
		
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金赎回");
		tv_redeemconfirm_fundcode = (TextView)findViewById(R.id.tv_redeemconfirm_fundCode);
		tv_redeemconfirm_fundname = (TextView)findViewById(R.id.tv_redeemconfirm_fundName);
		tv_redeemconfirm_chiyoufene = (TextView)findViewById(R.id.tv_redeemconfirm_chiyoufene);
		tv_redeemconfirm_dongjiefee = (TextView)findViewById(R.id.tv_redeemconfirm_dongjiefene);
		tv_redeemconfirm_shuhuifene = (TextView)findViewById(R.id.tv_redeemconfirm_shuhuifene);
		tv_redeemconfirm_feneDX = (TextView)findViewById(R.id.tv_redeemconfirm_feneDX);
		tv_redeemconfirm_flag = (TextView)findViewById(R.id.tv_redeemconfirm_flag);
	
		findViewAddListener(R.id.bt_applyredeem);
		tv_redeemconfirm_fundcode.setText("["+res.getFundcode()+"]");
		tv_redeemconfirm_fundname.setText(res.getFundname());
		tv_redeemconfirm_chiyoufene.setText(res.getFundvolbalance());
		tv_redeemconfirm_dongjiefee.setText(res.getTrdfrozen());
		tv_redeemconfirm_shuhuifene.setText(bundle.getString("RedeemFenE")+"(份)");
		tv_redeemconfirm_feneDX.setText(new CnUpperCaser(bundle.getString("RedeemFenE")).getCnString()+"(份)");
		//tv_redeemconfirm_feneDX.setText(res.getFundname());
		if(bundle.getString("VastRedeemFlag").trim().equals("0")){
			tv_redeemconfirm_flag.setText("取消");
		}else{
			tv_redeemconfirm_flag.setText("顺延");
		}
		
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applyredeem:
			initDialog();
			break;

		default:
			break;
		}
		
	}
	private  void initDialog(){
		customDialog = new CustomDialog(RedeemConfirmActivity.this,R.style.mystyle,R.layout.customdialog) ;
		
		inputDialogListener  = new  InputDialogListener() {
			
			@Override
			public void onOK(String text) {
				
				//给密码文本框设置密码
				DESEncrypt desEpt = new DESEncrypt();
				try {
					PassWord = desEpt.encrypt(text);
					encodePassWord = java.net.URLEncoder
							.encode(PassWord);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				applyRedeem();
			}
			
		};
		customDialog.setListener(inputDialogListener);
		customDialog.show();
	}
	
	void applyRedeem() {

		showProgressDialog("正在确认...");

		
		// encodePassWord = rep(encodePassWord);
		System.out.println("加密以后的账号和密码"  + encodePassWord);
		RequestParams params = new RequestParams(this);
		//params.put("id", encodeIdCard);
		params.put("sessionId", sessionId);
		params.put("passwd", encodePassWord);
		params.put("fundcode", res.getFundcode());
		params.put("applicationamount", bundle.getString("RedeemFenE"));
		params.put("fundtype", res.getFundtype());
		params.put("fundstatus", res.getStatus());
		params.put("tano", res.getTano());
		params.put("transactionaccountid", res.getTransactionaccountid());
		params.put("vastredeemflag", bundle.getString("VastRedeemFlag"));
		
		// params.put(RequestParams.MOBILE, username);
		
		execApi(ApiType.GET_DEALREDEEMTWO, params);
	}
	
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_DEALREDEEMTWO) {

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
											//System.out.println("ddddddddddddddddddddddd"+applyCode);
											if(xmlReturn.contains("appsheetserialno")){
												//赎回申请单编号
										
												JSONObject 	jsonObj = new JSONObject(xmlReturn);
												String applyCode = jsonObj.getString("appsheetserialno");
												System.out.println("appsheetserialno"+applyCode);
												if (!(applyCode==null)) {
													
													disMissDialog();
													showToast("赎回成功！！");
													bundle.putString("appsheetserialno", applyCode);
													bundle.putString("sessionId", sessionId);
													Intent intent = new Intent(RedeemConfirmActivity.this,RedeemInforActivity.class);
													intent.putExtras(bundle);
													startActivity(intent);
											
													

												} else {
													
													disMissDialog();
													showToast("赎回失败！！");
													return;
												}
											}else{
												JSONObject 	jsonObj = new JSONObject(xmlReturn);
												msg = jsonObj.getString("msg");
												diadefh();
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
	protected void diadefh(){
		AlertDialog.Builder builder=new Builder(this);
		
			builder.setMessage("密码错误，请重新输入。");
		
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initDialog();
				
			}
		});
		
		builder.create().show();
	}

}
