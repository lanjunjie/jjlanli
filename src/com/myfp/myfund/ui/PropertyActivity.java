package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import u.aly.ap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.api.beans.HoldDetail;
import com.myfp.myfund.ui.DealSystem.HoldListAdapter;

public class PropertyActivity extends BaseActivity {
	private String userName, idCard;
	ByteArrayInputStream tInputStringStream = null;
	private TextView txt_general_assets, txt_profit_andloss, txt_earnings;
	private String encodePassWord;
	private String password;
	private String customRiskLevel;
	private String depositacctName;
	private String countFund;
	private String totalfundmarketvalue;
	private String mobile;
	private boolean value;
	private String totalfloatprofit;
	private String viplevel;
	private String sessionId;
	private String fundcode;
	private JSONObject jsonObj;
	private long lastBack;
	private SharedPreferences sPreferences;
	private String risklevel;
	private LinearLayout lrukou;


	@Override
	protected void setContentView() {
		setContentView(R.layout.new_activity_property);
		sPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
		Intent intent = getIntent();
		fundcode = intent.getStringExtra("Fundcode");
		password=sPreferences.getString("password", null);
		encodePassWord = sPreferences.getString("EncodePassWord", null);
		customRiskLevel = sPreferences.getString("CustomRiskLevel", null);
		countFund = sPreferences.getString("CountFund", null);
		totalfundmarketvalue = sPreferences.getString("TotalFundMarketValue", null);
		sessionId = sPreferences.getString("SessionId", null);
		depositacctName = App.getContext().getDepositacctName();
		mobile = App.getContext().getMobile();
		userName = App.getContext().getUserName();
		idCard = App.getContext().getIdCard();
		sessionId = getIntent().getStringExtra("sessionid");
		RequestParams params = new RequestParams(this);
		params.put("idcard", idCard.trim());
		execApi(ApiType.GET_STEPVERIFICATION, params);
		execApi(ApiType.GET_MYPROPERTY, params);
		execApi(ApiType.GET_MEBERJUDGE, params);
		RequestParams pas=new RequestParams(this);
		pas.put("IDcard", idCard);
		execApi(ApiType.GET_IDCRADAUDITUSANM, pas);
		showProgressDialog("正在加载,请稍后...");
	}

	@Override
	protected void initViews() {
		setTitle("我的资产");
		ll_top_layout_right_view.setVisibility(View.VISIBLE);
		ll_top_layout_left_view.setVisibility(View.GONE);
		ImageView iv_mainactivity_top_right = (ImageView) ll_top_layout_right_view
				.findViewById(R.id.iv_mainactivity_top_right);
		iv_mainactivity_top_right.setImageResource(R.drawable.set_bt);
		txt_general_assets = (TextView) findViewById(R.id.txt_general_assets);
		txt_profit_andloss = (TextView) findViewById(R.id.txt_profit_andloss);
		txt_earnings = (TextView) findViewById(R.id.txt_earnings);
		lrukou = (LinearLayout) findViewById(R.id.layout_rukou);
		findViewAddListener(R.id.lay_optional);
		findViewAddListener(R.id.lay_free_subscription);
		findViewAddListener(R.id.lay_feature);
		findViewAddListener(R.id.lay_fund_fintheircing);
		findViewAddListener(R.id.lay_my_coupon);
		findViewAddListener(R.id.lay_personal_tailor);
		findViewAddListener(R.id.lay_sunshine_placement);
		findViewAddListener(R.id.layout_rukou);
		findViewById(R.id.ll_top_layout_right_view);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		//设置
		case R.id.ll_top_layout_right_view:
				Intent intent9 =new Intent(this,MySettingActivity.class);
				intent9.putExtra("IDCard", idCard);
				intent9.putExtra("CustomRiskLevel", customRiskLevel);
				intent9.putExtra("CountFund", countFund);   
				intent9.putExtra("DepositacctName", depositacctName);
				intent9.putExtra("TotalFundMarketValue", totalfundmarketvalue);
				intent9.putExtra("Mobile", mobile);
				intent9.putExtra("password", password);
				intent9.putExtra("UserName", userName);
				startActivity(intent9);
			break;
		//入口
		case R.id.layout_rukou:
			Intent intent8=new Intent(this,DealSystem.class);
			intent8.putExtra("IDCard", idCard);
			intent8.putExtra("PassWord", getIntent().getStringExtra("PassWord"));
			System.out.println("PassWord----->"+getIntent().getStringExtra("PassWord"));
			System.out.println("encodePassWord===================>"+encodePassWord);
			intent8.putExtra("CustomRiskLevel", customRiskLevel);
			intent8.putExtra("CountFund", countFund);
			intent8.putExtra("UserName", userName);
			intent8.putExtra("DepositacctName", depositacctName);
			intent8.putExtra("TotalFundMarketValue", totalfundmarketvalue);
			intent8.putExtra("Totalfloatprofit", totalfloatprofit);
			System.out.println("ssid==========>"+getIntent().getStringExtra("sessionId"));
			intent8.putExtra("sessionid", sessionId);
			startActivity(intent8);
			break;
		// 自选基金
		case R.id.lay_optional:
			Intent intent6 = new Intent(this, FundSelectActivity.class);
			intent6.putExtra("userName", userName);
			System.out.println("userName3===============>"+userName);
			intent6.putExtra("sessionId", sessionId);
			startActivity(intent6);
			break;
		// 免申购费
		case R.id.lay_free_subscription:
			if (value==true) {
				System.out.println("mem------------>"+value);
				Intent intent=new Intent(this, MyPropertyMemberActivity.class);
				intent.putExtra("idcard",idCard);
				intent.putExtra("viplevel", viplevel);
				startActivity(intent);
			}else {
				Intent intent=new Intent(this, MyMemberDredgeActivity.class);
				intent.putExtra("idcard", idCard);
				startActivity(intent);
			}
			break;
		// 特色固收
		case R.id.lay_feature:
			Intent intent2 = new Intent(this, MyFixationActivity.class);
			intent2.putExtra("idcard", idCard);
			startActivity(intent2);
			break;
		// 阳光私募
		case R.id.lay_sunshine_placement:
			Intent intent1 = new Intent(this, MyPrivateProductsActivity.class);
			intent1.putExtra("idcard", idCard);
			startActivity(intent1);
			break;
		// 基金配资
		case R.id.lay_fund_fintheircing:
			RequestParams para = new RequestParams(this);
			para.put("sdzjnumber", idCard);
			para.put("username", userName);
			execApi(ApiType.GET_JUDGEFNDSTWO, para,
					new OnDataReceivedListener() {

						private String code;
						private String hint;
						private String info;

						@Override
						public void onReceiveData(ApiType api, String json) {
							if (json == null) {
								showToast("网络不给力！");
								disMissDialog();
								return;

							}

							JSONObject object;
							try {
								object = new JSONObject(json);
								code = object.getString("Code");
								hint = object.getString("Hint");
								info = object.getString("Info");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (code.equals("0000")
									&& hint.equals("已经拥有个人净值记录")
									&& info.equals("true")) {

								Intent intent4 = new Intent(
										PropertyActivity.this,
										MyFundsWithCapitalActivity.class);
								intent4.putExtra("idcard", idCard);
								startActivity(intent4);

							} else if (code.equals("0001")
									&& hint.equals("已经预约，正在审核")
									&& info.equals("true")) {
								Intent intent5 = new Intent(
										PropertyActivity.this,
										FinancingAuditActivity.class);
								startActivity(intent5);

							} else if (code.equals("0002")
									&& hint.equals("认证成功，未支付")
									&& info.equals("true")) {
								Intent intent = new Intent(
										PropertyActivity.this,
										SucceedNonPaymentActivity.class);
								startActivity(intent);
							} else if (code.equals("0003")
									&& hint.equals("认证成功，已支付")
									&& info.equals("true")) {
								Intent intent3 = new Intent(
										PropertyActivity.this,
										PaySuccessActivity.class);
								startActivity(intent3);
							} else if (code.equals("500")
									&& hint.equals("还没有预约")
									&& info.equals("false")) {
								Intent intent6 = new Intent(
										PropertyActivity.this,
										DistributionInformationActivity.class);
								intent6.putExtra("UserName", userName);
								intent6.putExtra("DepositacctName", depositacctName);
								startActivity(intent6);
							}
						}
					});
			break;
		// 私人定制
		case R.id.lay_personal_tailor:
			RequestParams param = new RequestParams(this);
			param.put("sdzjnumber", idCard);
			param.put("username", userName);
			execApi(ApiType.GET_TODETERMINETHEMEBEER, param,
					new OnDataReceivedListener() {
						private String code;
						private String hint;
						private String info;

						@Override
						public void onReceiveData(ApiType api, String json) {
							if (json == null) {
								showToast("网络不给力！");
								disMissDialog();
								return;
							}
							JSONObject object;
							try {
								object = new JSONObject(json);
								code = object.getString("Code");
								hint = object.getString("Hint");
								info = object.getString("Info");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (code.equals("0000") && hint.equals("ok")
									&& info.equals("true")) {

								Intent intent4 = new Intent(
										PropertyActivity.this,
										ButlerTongActivity.class);
								intent4.putExtra("idcard", idCard);
								startActivity(intent4);

							} else if (code.equals("0001")
									&& hint.equals("您已预约了管家通")
									&& info.equals("true")) {
								Intent intent5 = new Intent(
										PropertyActivity.this,
										BeinAuditActivity.class);
								startActivity(intent5);

							} else if (code.equals("500") && hint.equals("无数据")
									&& info.equals("false")) {
								Intent intent6 = new Intent(
										PropertyActivity.this,
										OpenHousekeeperTongActivity.class);
								intent6.putExtra("UserName", userName);
								intent6.putExtra("DepositacctName", depositacctName);
								startActivity(intent6);
							}
						}
					});
			break;
		// 我的优惠券
		case R.id.lay_my_coupon:
			Intent intent = new Intent(this, CouponActivity.class);
			intent.putExtra("userName", userName);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			//showToast("");
			disMissDialog();
			return;
		}
		if(api==ApiType.GET_STEPVERIFICATION) {
			if (json!=null&&!json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event!= XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								try {
									String xmlReturn = parser.nextText();
									System.out.println("--------------->"+xmlReturn);
									
									JSONObject jsonObj=new JSONObject(xmlReturn);
									sessionId = jsonObj.getString("sessionid");
									System.out.println("sessionid============>"+sessionId);
									customRiskLevel = jsonObj.getString("risklevel");
									
									System.out.println("risklevel==========>"+risklevel);
									App.getContext().setSessionid(sessionId);
								
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
							break;
							
						default:
							break;
						}
						try {
							event = parser.next();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (XmlPullParserException e) {
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			disMissDialog();
		}else 
		if (api==ApiType.GET_IDCRADAUDITUSANM) {
			if (json.equals("[]")) {
				txt_general_assets.setText("未开通展恒帐号");
				lrukou.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Intent intent=new Intent(PropertyActivity.this, RegisterUserActivity.class);
						Intent intent=new Intent(PropertyActivity.this, ShortcutRegisterActivity.class);
						startActivity(intent);
					}
				});
			}else {
			try {
				JSONArray array=new JSONArray(json);
				userName = array.getJSONObject(0).getString("UserName");
			//	App.getContext().setUserName(userName);
				System.out.println("userNameabc------------->"+userName);
				if (mobile==null) {
					mobile = array.getJSONObject(0).getString("Mobile");
					System.out.println("mobileabc============>"+mobile);
				}
				SharedPreferences sPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
				Editor editor = sPreferences.edit();
//				editor.putString("UserName", userName);
				editor.putString("Mobile", mobile);
				editor.commit();
				RequestParams pms=new RequestParams(this);
				pms.put("mobileno", mobile);
				execApi(ApiType.GET_FINDMOBILENO, pms,new OnDataReceivedListener() {
					
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
														txt_general_assets.setText("未开户");
														lrukou.setOnClickListener(new OnClickListener() {
															
															@Override
															public void onClick(View v) {
																//旧开户判断是否开户
															/*	Intent intent=new Intent(PropertyActivity.this, OpenTradingAccountActivity.class);
																intent.putExtra("Phone", getIntent().getStringExtra("Mobile"));
																startActivity(intent);
																*/
																//新开户判断是否开户
																Intent intent=new Intent(PropertyActivity.this, ShortcutStatusActivity.class);
																intent.putExtra("Phone", getIntent().getStringExtra("Mobile"));
																System.out.println("Phone2次开户流程------->"+getIntent().getStringExtra("Mobile"));
																startActivity(intent);
															}
														});
													
													}else if (msg.equals("1")) {
														RequestParams params = new RequestParams(PropertyActivity.
																this);
														params.put(
																"certificateno",
																getIntent().getStringExtra("certificateno"));
														System.out.println("certificateno--->"+getIntent().getStringExtra("certificateno"));
														params.put(
																"depositacct",
																getIntent().getStringExtra("depositacct"));
														System.out.println("depositacct--->"+getIntent().getStringExtra("depositacct"));
														params.put(
																"depositacctname",
																depositacctName);
														params.put(
																"certificatetype",
																getIntent().getStringExtra("certificatetype"));
														System.out.println("certificatetype--->"+getIntent().getStringExtra("certificatetype"));
														params.put("channelid", getIntent().getStringExtra("channelid"));
														System.out.println("channelid--->"+getIntent().getStringExtra("channelid"));
														execApi(ApiType.GET_QRYSMALLMONEYTWO, params);
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		}else
		
		if (api == ApiType.GET_MYPROPERTY) {

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
									System.out
											.println("---------------------->"
													+ xmlReturn);
									try {
										JSONArray jsonArray = new JSONArray(
												xmlReturn);
										System.out
												.println("jsonArray---------->"
														+ jsonArray);
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject obj = jsonArray
													.getJSONObject(i);
											double total = Double
													.parseDouble(obj
															.getString("totalfloatprofit"));
											DecimalFormat df = new DecimalFormat(
													"#0.##");
											totalfloatprofit = df
													.format(total);

											String totaladdincomerate = obj
													.getString("totaladdincomerate");
											Double totaladd = Double
													.parseDouble(totaladdincomerate);
											Double comerate = totaladd * 100;
											DecimalFormat dfi = new DecimalFormat(
													"#0.##");
											String comer = dfi.format(comerate);
											txt_profit_andloss
													.setText(totalfloatprofit);
											txt_earnings.setText(comer + "%");
											DecimalFormat de = new DecimalFormat(
													"#0.##");
											double totalfundmar = Double
													.parseDouble(obj
															.getString("totalfundmarketvalue"));
											totalfundmarketvalue = de
													.format(totalfundmar);
											txt_general_assets
													.setText(totalfundmarketvalue);
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			disMissDialog();
		}else
		if (api==ApiType.GET_MEBERJUDGE) {
			if (json!=null&&!json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event!= XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								try {
									String xmlReturn = parser.nextText();
									System.out.println("--------------->"+xmlReturn);
									
									JSONObject jsonObj=new JSONObject(xmlReturn);
									JSONObject object = jsonObj.getJSONObject("ret_data");
									System.out.println("object===========>"+object);
									String mber = object.getString("member");
									Boolean bool=new Boolean(mber);
									value = bool.booleanValue();
									System.out.println("value-------------->"+value);
									viplevel = object.getString("viplevel");
									System.out.println("member============>"+mber);
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
							break;
							
						default:
							break;
						}
						try {
							event = parser.next();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			disMissDialog();
		}else if (api == ApiType.GET_QRYSMALLMONEYTWO) {

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
								String xmlReturn;
								try {
									xmlReturn = parser.nextText();
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);
									try {
										jsonObj = new JSONObject(xmlReturn);
										
										if (xmlReturn
												.contains("result")) {
											if (jsonObj.getString("result")
													.equals("1")) {
												disMissDialog();
												showToast("请激活账户");
												txt_general_assets.setText("未验卡");
												lrukou.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														Intent intent = new Intent(
																PropertyActivity.this,
																NewParagRaphActivity.class);
														intent.putExtra("IdCard",
																idCard);
														intent.putExtra("PassWord",
																encodePassWord);
														intent.putExtra(
																"CustomRiskLevel",
																getIntent().getStringExtra("CustomRiskLevel"));
														intent.putExtra(
																"Name",
																depositacctName);
														intent.putExtra(
																"TotalFundMarketValue",
																getIntent().getStringExtra("TotalFundMarketValue"));
														intent.putExtra(
																"CountFund",
																getIntent().getStringExtra("CountFund"));
														intent.putExtra(
																"Certificateno",
																getIntent().getStringExtra("certificateno"));
														intent.putExtra(
																"Depositacct",
																getIntent().getStringExtra("depositacct"));
														intent.putExtra(
																"CertificateType",
																getIntent().getStringExtra("certificatetype"));
														intent.putExtra(
																"Channelid",
																getIntent().getStringExtra("channelid"));
														intent.putExtra(
																"MoneyAccount",
																getIntent().getStringExtra("Moneyaccount"));
														intent.putExtra("Custno",
																getIntent().getStringExtra("Custno"));
														intent.putExtra("sessionid", sessionId);
														try {
															intent.putExtra(
																	"BusiDate",jsonObj.getString("busidate"));
														} catch (JSONException e) {
															e.printStackTrace();
														}
														startActivity(intent);

														finish();
														
													}
												});

											} 
										} 

									} catch (JSONException e) {
										// TODO Auto-generated catch block
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
					try {
						tInputStringStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}

			}

		}
		
		}
	@Override
	public void onBackPressed() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastBack > 2000) {
			lastBack = curTime;
			Toast.makeText(this, "再按一次退出展恒基金网", Toast.LENGTH_LONG).show();
		} else {
			String listStr = JSON.toJSONString(App.getContext().userList);
			Editor editor = sPreferences.edit();
			editor.putString("UserList", listStr);
			editor.commit();
			finish();
			
		}
	}
	
	
}

