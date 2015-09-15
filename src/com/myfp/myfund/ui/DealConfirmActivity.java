package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;
import com.myfp.myfund.utils.CustomDialog;
import com.myfp.myfund.utils.CustomDialog.InputDialogListener;
import com.myfp.myfund.utils.DESEncrypt;

/*
 * 交易确认
 * */
	
public class DealConfirmActivity extends BaseActivity{
	Bundle bundle;
	private DealSearchResult res;
	TextView tv_confirm_fundcode,tv_confirm_fundname,tv_confirm_jine,tv_confirm_jineDX,tv_confirm_bank;
	Button bt_bankorder;
	private String jinE,idcardshow;
	private InputDialogListener  inputDialogListener;
	private  CustomDialog customDialog;
	ByteArrayInputStream tInputStringStream = null;
	public static DealConfirmActivity instance = null;
	private String encodePassWord;
	private String PassWord;
	private String sessionId;
	private String applyCode;
	private String liqDate;
	private String code;
	private JSONObject jsonObj;
	private String appsheetserialno;
	private String msg;
	private TextView tv_bankorder_applycode;
	private TextView tv_bankorder_idcardnum;
	private int recLen = 120;
	private TextView tv_bankorder_timing;
	Timer timer = new Timer();
	private String sypaw;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_dealconfirm);
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		System.out.println("sessionId===============>"+sessionId);
		jinE = bundle.getString("JinE");
		res = (DealSearchResult) bundle.getSerializable("DealSearchResult");
		idcardshow = bundle.getString("Certificateno");
		
	}

	@Override
	protected void initViews() {
		setTitle("基金购买");
		tv_bankorder_applycode = (TextView) findViewById(R.id.tv_bankorder_applycode);
		tv_confirm_fundcode = (TextView)findViewById(R.id.tv_confirm_fundcode);
		tv_confirm_fundname = (TextView)findViewById(R.id.tv_confirm_fundname);
		tv_confirm_jine = (TextView)findViewById(R.id.tv_confirm_jine);
		tv_confirm_jineDX = (TextView)findViewById(R.id.tv_confirm_jineDX);
		tv_bankorder_idcardnum = (TextView) findViewById(R.id.tv_bankorder_idCardNum);
		tv_bankorder_timing = (TextView)findViewById(R.id.tv_bankorder_timinga);
		tv_confirm_bank = (TextView)findViewById(R.id.tv_confirm_bank);
		bt_bankorder = (Button)findViewById(R.id.bt_bankorder_paya);
		findViewAddListener(R.id.bt_bankorder_paya);
		
		tv_confirm_fundcode.setText("["+res.getFundCode()+"]");
		tv_confirm_fundname.setText(res.getFundName());
		tv_confirm_jine.setText(jinE);
		tv_confirm_jineDX.setText(new CnUpperCaser(jinE).getCnString()+"(元)");
		tv_confirm_bank.setText(bundle.getString("BankCardCodeShow"));
		if (idcardshow.length() == 18) {
			tv_bankorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(11, 16), "*****"));
		} else if (idcardshow.length() == 15) {
			tv_bankorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(8, 13), "*****"));
		}
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_bankorder_paya:
			initDialog();
			break;

		default:
			break;
		}
		
	}
	private  void initDialog(){
		customDialog = new CustomDialog(DealConfirmActivity.this,R.style.mystyle,R.layout.customdialog) ;
		
		inputDialogListener  = new  InputDialogListener() {
			
			@Override
			public void onOK(String paww) {
				//给密码文本框设置密码
				DESEncrypt desEpt = new DESEncrypt();
				try {
					PassWord = desEpt.encrypt(paww);
					encodePassWord = java.net.URLEncoder
							.encode(PassWord);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				applyDeal();
			}
			
		};
		customDialog.setListener(inputDialogListener);
		customDialog.show();
	}
	
	void applyDeal() {

		showProgressDialog("正在确认...");
		
		RequestParams params = new RequestParams(this);
		//params.put("id", bundle.getString("IDCard"));
		params.put("sessionId", sessionId);
		String paw = bundle.getString("PassWord");
		System.out.println("密码：paw---------------->"+paw);
		params.put("passwd", encodePassWord);
		params.put("fundcode", res.getFundCode());
		params.put("applicationamount", bundle.getString("JinE"));
		params.put("fundtype", res.getFundType());
		params.put("fundstatus", res.getStatus());
		params.put("tano", res.getTano());
		params.put("sharetype", res.getShareclasses());
		params.put("channelid", res.getChannelid());
		params.put("moneyaccount", res.getMoneyaccount());
		params.put("buyflag", "1");
		params.put("paytype", bundle.getString("PayType"));
		
		// params.put(RequestParams.MOBILE, username);
		
		execApi(ApiType.GET_ORDERTWO, params);
	}
	
	
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_ORDERTWO) {

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
												//赎回申请单编号
										
												JSONObject 	jsonObj = new JSONObject(xmlReturn);
												appsheetserialno = jsonObj.getString("appsheetserialno");
												System.out
														.println("appsheetserialno==========>"+appsheetserialno);
												if (!(appsheetserialno==null)) {
													liqDate = jsonObj.getString("liqdate").toString();
														disMissDialog();
														showToast("下单成功！！");
														bundle.putString("appsheetserialno", appsheetserialno);
														bundle.putString("liqdate", liqDate);
														bundle.putString("sessionId", sessionId);
														if(bundle.getString("PayType").equals("1")){
															//银行代扣
															RequestParams params = new RequestParams(this);
															params.put("sessionId", sessionId);
															params.put("certIdLength", idcardshow.length());
															params.put("applicationamount", jinE.trim());
															params.put("channelid", res.getChannelid().trim());
															params.put("fundtype", res.getFundType());
															params.put("fundstatus", res.getStatus());
															params.put("tano", res.getTano());
															params.put("moneyaccount", res.getMoneyaccount());
															params.put("liqdate", bundle.getString("liqdate"));
															params.put("fundcode", res.getFundCode());
															params.put("appsheetserialno",bundle.getString("appsheetserialno"));
															params.put("fundname",res.getFundName());
														
															execApi(ApiType.GET_BANKPAYTWO, params,new OnDataReceivedListener() {
																
																@Override
																public void onReceiveData(ApiType api, String json) {
																	// TODO Auto-generated method stub
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
																								JSONObject jsonObj = new JSONObject(xmlReturn);
																								if (jsonObj.getString("code").equals("0000")) {
																									
																									disMissDialog();
																									showToast("支付成功！！");
																									Intent intent = new Intent(DealConfirmActivity.this,ApplyAcceptActivity.class);
																									intent.putExtras(bundle);
																									startActivity(intent);
																									finish();

																								} else {
																									msg = jsonObj.getString("msg");
																									dialogfh();
																									disMissDialog(); 
																									showToast("支付失败！！");
																									
																									
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
															});
														}else{
															//汇款支付
															Intent intent = new Intent(DealConfirmActivity.this,RemitOrderSuccessActivity.class);
															intent.putExtras(bundle);
															startActivity(intent);
															finish();
														}
													
													
												}	else {
													disMissDialog();
													showToast("购买失败！！");
													return;
													
												}
											}else{
												JSONObject 	jsonObj = new JSONObject(xmlReturn);
												msg = jsonObj.getString("msg");
												//showToast(msg);
												dialog();
												disMissDialog();
												return;
											}

//											}

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
	protected void dialog(){
		AlertDialog.Builder builder=new Builder(this);
		if (msg.equals("用户不存在或密码输入错误，请重新输入!错误次数[1次] 还可尝试次数[2次]")||msg.equals("用户不存在或密码输入错误，请重新输入!错误次数[2次] 还可尝试次数[1次]")||msg.equals("您的密码已被锁定，请联系客服!")) {
			sypaw = "密码输入错误，请重新输入。";
		}
		if (msg.equals("用户不存在或密码输入错误，请重新输入!错误次数[1次] 还可尝试次数[2次]")||msg.equals("用户不存在或密码输入错误，请重新输入!错误次数[2次] 还可尝试次数[1次]")||msg.equals("您的密码已被锁定，请联系客服!")) {
			builder.setMessage(sypaw);
			
		}else {
			builder.setMessage(msg);
		}
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initDialog();
				
			}
		});
		
		builder.create().show();
	}

	protected void dialogfh(){
		AlertDialog.Builder builder=new Builder(this);
		
			builder.setMessage(msg);
		
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(DealConfirmActivity.this,DealBuyActivity.class);
				intent.putExtra("sessionId", bundle.getString("sessionId"));
				startActivity(intent);
				finish();
				DealBuyActivity.instance.finish();
				DealApplyActivity.instance.finish();
				DealConfirmActivity.instance.finish();
				
			}
		});
		
		builder.create().show();
	}
	

}
