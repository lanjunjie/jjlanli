package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.TobeUsedResult;

public class GoodsPassActivity extends BaseActivity{

	private TextView tev_UserType;
	private TextView tev_certificate;
	private ImageView imagv_Online;
	private ImageView imagv_bank;
	private ImageView imagv_Clubcard;
	private String member;
	private int PHT=1;
	private String userName;
	private String amount;
	private CheckBox check_boxty;
	private String mobile;
	private String displayName;
	private String iDCard;
	private String formtwo;
	private int amou;
	private int unt;
	private String sume;
	public static GoodsPassActivity instance=null;
	private EditText editt_referrer;
	private String referrer;
	private String id;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_upongoodspass);
		instance=this;
		if (amount==null) {
			amou = Integer.parseInt("0");
		}else {
			amou = Integer.parseInt(amount);	
		}
		 userName = App.getContext().getUserName();
		 mobile = App.getContext().getMobile();
		 displayName = App.getContext().getDepositacctName();
		 System.out.println("mobile--------2>"+mobile);
		 RequestParams params=new RequestParams(this);
		 RequestParams pas=new RequestParams(this);
		 params.put("username", userName);
		 params.put("Status", "0");
		 execApi(ApiType.GET_DISCUNTCOUPON,params,this);
		 pas.put("username", userName);
		 execApi(ApiType.GET_USER_INFO, pas);
		 showProgressDialog("正在加载");

	}

	@Override
	protected void initViews() {
		setTitle("购买点财通");
		tev_UserType = (TextView) findViewById(R.id.tev_UserType);
		tev_certificate = (TextView) findViewById(R.id.tev_certificate);
		imagv_Online = (ImageView) findViewById(R.id.imagv_Online);
		imagv_bank = (ImageView) findViewById(R.id.imagv_bank);
		imagv_Clubcard = (ImageView) findViewById(R.id.imagv_Clubcard);
		check_boxty = (CheckBox) findViewById(R.id.Check_boxty);
		editt_referrer = (EditText) findViewById(R.id.editt_referrer);
		findViewAddListener(R.id.linea_UserType);
		findViewAddListener(R.id.linea_Online);
		findViewAddListener(R.id.linea_bank);
		findViewAddListener(R.id.linea_Clubcard);
		findViewAddListener(R.id.bttn_NextStep);
		findViewAddListener(R.id.textv_webview);
		imagv_Online.setImageResource(R.drawable.imag_select);
		if (amount==null) {
			tev_certificate.setText("0"+"元");
		}else {
			
			tev_certificate.setText(amount);
		}
		if (member==null) {
			tev_UserType.setText("一年期会员399元");
			unt = 399-amou;
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
					tev_UserType.setText("一年期会员399元");
				}else if (mem==1) {
					tev_UserType.setText("三年期会员799元");
				}else if (mem==2) {
					tev_UserType.setText("终身会员1999元");
				}
				if (mem==0) {
					unt = 399-amou;
				}else if (mem==1) {
					unt=799-amou;
				}else if (mem==2) {
					unt=1999-amou;
				}
				
				if (mem==0) {
					formtwo = "年度会员/￥399";
				}else if (mem==1) {
					formtwo="年度会员/￥799";
				}else if (mem==2) {
					formtwo="年度会员/￥1999";
				}
					
				
			}
		}).show();
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.linea_UserType:
			MemDialog();
			break;
		case R.id.linea_Online:
			PHT=1;
			imagv_Online.setVisibility(View.VISIBLE);
			imagv_Online.setImageResource(R.drawable.imag_select);
			imagv_bank.setVisibility(View.GONE);
			imagv_Clubcard.setVisibility(View.GONE);
			break;
		case R.id.linea_bank:
			PHT=2;
			imagv_bank.setVisibility(View.VISIBLE);
			imagv_bank.setImageResource(R.drawable.imag_select);
			imagv_Online.setVisibility(View.GONE);
			imagv_Clubcard.setVisibility(View.GONE);
			break;
		case R.id.linea_Clubcard:
			PHT=3;
			imagv_Clubcard.setVisibility(View.VISIBLE);
			imagv_Clubcard.setImageResource(R.drawable.imag_select);
			imagv_bank.setVisibility(View.GONE);
			imagv_Online.setVisibility(View.GONE);
			break;
		case R.id.bttn_NextStep:
			referrer = editt_referrer.getText().toString();
			if (!checkval(1)) {
				return;
			}else {
				RequestParams ps=new RequestParams(this);
				ps.put("Username", userName);
				execApi(ApiType.GET_JUDGEDOTGEIN, ps,new OnDataReceivedListener() {
					
					private String code;
					private String hint;
					private String info;
					@Override
					public void onReceiveData(ApiType api, String json) {
						JSONObject object;
						try {
							object = new JSONObject(json);
							code = object.getString("Code");
							hint = object.getString("Hint");
							info = object.getString("Info");
							if (code.equals("0000")&&hint.equals("该用户为点财通会员。")) {
								showToastLong("您已是点财通会员！");
							}else if(code.equals("500")&&hint.equals("该用户不是点财通会员。")){
								if (PHT==1) {
									sume = Integer.toString(unt);
									Intent intent=new Intent(GoodsPassActivity.this, OnLinePayWebActivity.class);
									intent.putExtra("Mobile", mobile);
									intent.putExtra("DisplayName", displayName);
									intent.putExtra("UserName", userName);
									intent.putExtra("Formtwo", formtwo);
									intent.putExtra("Referrer", referrer);
									intent.putExtra("Unt", sume);
									startActivity(intent);
								}else if (PHT==2) {
									sume = Integer.toString(unt);
									RequestParams pms=new RequestParams(GoodsPassActivity.this);
									pms.put("username", userName);
									pms.put("name", displayName);
									pms.put("JiangLi", formtwo);
									pms.put("UMobile", mobile);
									pms.put("PaymentMethod", "1");
									pms.put("op3_Amt", sume);
									pms.put("Referral", referrer);
									if (id==null) {
										pms.put("yhq", "");
									}else {
										pms.put("yhq", id);
										
									}
									execApi(ApiType.GET_BANKTRANSFERTWOA, pms, new OnDataReceivedListener() {
										
										private String code;
										private String hint;
										private String info;
										
										@Override
										public void onReceiveData(ApiType api, String json) {
											try {
												JSONObject object=new JSONObject(json);
												code = object.getString("Code");
												System.out.println("code============>"+code);
												hint = object.getString("Hint");
												System.out.println("Hint============>"+hint);
												info = object.getString("Info");
												System.out.println("info============>"+info);
												if (code.equals("0000")&&hint.equals("已经成功。")) {
													Intent intent=new Intent(GoodsPassActivity.this, OnLineRemitActivity.class);
													startActivity(intent);
												}else {
													showToastLong("对不起，您的帐号线下汇款失败！");
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
											
										}
									});
									
								}else if (PHT==3) {
									RequestParams params=new RequestParams(GoodsPassActivity.this);
									params.put("mobileno", mobile);
									execApi(ApiType.GET_FINDMOBILENO, params,new OnDataReceivedListener() {
										
										@Override
										public void onReceiveData(ApiType api, String json) {
											if (json != null && !json.equals("")) {
												tInputStringStream = new ByteArrayInputStream(json.getBytes());
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
																	try {
																		JSONObject object=new JSONObject(xmlReturn);
																		String msg = object.getString("msg");
																		System.out.println("msg1------------->"+msg);
																		if (msg.equals("0")) {
																			Intent intent=new Intent(GoodsPassActivity.this, ShortcutStatusActivity.class);
																			intent.putExtra("Phone", mobile);
																			startActivity(intent);
																			
																		}else if (msg.equals("1")) {
																			Intent intent=new Intent(GoodsPassActivity.this, MembershipPayActivity.class);
																			intent.putExtra("username", userName);
																			intent.putExtra("name", displayName);
																			intent.putExtra("UMobile", mobile);
																			intent.putExtra("Referrer", referrer);
																			startActivity(intent);
																		}

																	} catch (Exception e) {
																		e.printStackTrace();
																	}

																} catch (IOException e) {
																	e.printStackTrace();
																}
															}

															break;

														}
														try {
															event = parser.next();
														} catch (IOException e) {
															e.printStackTrace();
														}
													}

												} catch (XmlPullParserException e) {
													e.printStackTrace();
												}

											}
											
										}
									});
									
									
								}
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
				
			break;
		case R.id.textv_webview:
			Intent intent=new Intent(this, PayMemberWebActivity.class);
			intent.putExtra("ImageID", "07");
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api==ApiType.GET_DISCUNTCOUPON) {
			System.out.println("json===========>"+json);
			try {
				JSONArray array=new JSONArray(json);
				
				amount = array.getJSONObject(0).getString("Amount");
				System.out.println("amount========>"+amount);
				id = array.getJSONObject(0).getString("Id");
				System.out.println("id---------->"+id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (api == ApiType.GET_USER_INFO) {
			try {
				JSONArray array = new JSONArray(json);
				displayName = array.getJSONObject(0).getString("DisplayName");
				iDCard = array.getJSONObject(0).getString("IDCard");
				if (mobile==null) {
					mobile = array.getJSONObject(0).getString("Mobile");
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		disMissDialog();
		if (api==ApiType.GET_FINDMOBILENO) {
				if (json != null && !json.equals("")) {
					tInputStringStream = new ByteArrayInputStream(json.getBytes());
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
										try {
											JSONObject object=new JSONObject(xmlReturn);
											String msg = object.getString("msg");
											System.out.println("msg1------------->"+msg);
											if (msg.equals("0")) {
												Intent intent=new Intent(GoodsPassActivity.this, ShortcutStatusActivity.class);
												intent.putExtra("Phone", getIntent().getStringExtra("Mobile"));
												startActivity(intent);
												
											
											}else if (msg.equals("1")) {
												Intent intent=new Intent(GoodsPassActivity.this, MembershipPayActivity.class);
												intent.putExtra("username", userName);
												intent.putExtra("name", displayName);
												intent.putExtra("UMobile", mobile);
												intent.putExtra("Referrer", referrer);
												startActivity(intent);
											}

										} catch (Exception e) {
											e.printStackTrace();
										}

									} catch (IOException e) {
										e.printStackTrace();
									}
								}

								break;

							}
							try {
								event = parser.next();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					} catch (XmlPullParserException e) {
						e.printStackTrace();
					}

				}
				
			
		}
		
		
	}
	private boolean checkval(int typ){
		if (typ==1) {
			if (!check_boxty.isChecked()) {
				showToast("请先阅读并同意相关服务条款!");
				return false;
			}
		}
		return true;
	}

}
