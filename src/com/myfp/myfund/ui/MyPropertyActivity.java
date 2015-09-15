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

import u.aly.co;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.R.id;
import com.myfp.myfund.R.integer;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.PlacementResult;

public class MyPropertyActivity extends BaseActivity{

	private String idCard;
	private String  encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	private TextView tv_my_earnings;
	private TextView tv_my_revenue;
	private TextView tv_my_income;
	private String xmlReturn;
	private JSONObject object;
	private String viplevel;
	private boolean value;
	private long lastBack;
	private SharedPreferences sPreferences;
	private MyMeansActivity activity;
	private String customrisklevel;
	private String countfund;
	private String depositacctname;
	private String totalfundmarketvalue;
	private String userName;
	private String lean="true";
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_my_property);
		SharedPreferences preferences = getSharedPreferences("Setting",MODE_PRIVATE);
		idCard = preferences.getString("IDCard", idCard);
		RequestParams params=new RequestParams(this);
		params.put("idcard".trim(),idCard.trim());
		execApi(ApiType.GET_MYPROPERTY, params);
		execApi(ApiType.GET_MEBERJUDGE, params);
		showProgressDialog("正在加载,请稍后...");
	}
	@Override
	protected void initViews() {
		setTitle("我的资产");
		ll_top_layout_right_view.setVisibility(View.VISIBLE);
		ImageView iv_mainactivity_top_right = (ImageView)ll_top_layout_right_view.findViewById(R.id.iv_mainactivity_top_right);
		iv_mainactivity_top_right.setImageResource(R.drawable.set_bt);
		tv_my_earnings = (TextView) findViewById(R.id.tv_my_earnings);
		tv_my_revenue = (TextView) findViewById(R.id.tv_my_revenue);
		tv_my_income = (TextView) findViewById(R.id.tv_my_income);
		findViewAddListener(R.id.la_my_member);
		findViewAddListener(R.id.la_my_product);
		findViewAddListener(R.id.la_my_placement);
		findViewAddListener(R.id.la_my_Financing);
		findViewAddListener(R.id.la_my_Customize);
		findViewAddListener(R.id.la_my_Integral);
		findViewAddListener(R.id.lay_image);
		findViewAddListener(R.id.ll_top_layout_right_view);
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json==null) {
			showToast("网络不给力！");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_MYPROPERTY) {

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
									System.out.println("---------------------->"
											+ xmlReturn);
									try {
										JSONArray jsonArray=new JSONArray(xmlReturn);
										System.out.println("jsonArray---------->"+jsonArray);
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject obj = jsonArray.getJSONObject(i);
											double total=Double.parseDouble(obj.getString("totalfloatprofit"));
											DecimalFormat df=new DecimalFormat("#0.##");
											String totalfloatprofit = df.format(total);
											
											String totaladdincomerate = obj.getString("totaladdincomerate");
											Double totaladd = Double.parseDouble(totaladdincomerate);
											Double comerate = totaladd*100;
											DecimalFormat dfi=new DecimalFormat("#0.##");
											String comer = dfi.format(comerate);
											tv_my_revenue.setText(totalfloatprofit+"元"+"("+comer+"%"+")");
											DecimalFormat de=new DecimalFormat("#0.##");
											double totalfundmar=Double.parseDouble(obj.getString("totalfundmarketvalue"));
											String totalfundmarketvalue = de.format(totalfundmar);
											tv_my_income.setText(totalfundmarketvalue+"元");
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
		}
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
									xmlReturn = parser.nextText();
									System.out.println("--------------->"+xmlReturn);
									
										JSONObject jsonObj=new JSONObject(xmlReturn);
										object = jsonObj.getJSONObject("ret_data");
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
		}
		
	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		//点财通会员
		case R.id.la_my_member:
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
		//固收产品
		case R.id.la_my_product:
			Intent intent2=new Intent(this, MyFixationActivity.class);
			intent2.putExtra("idcard", idCard);
			startActivity(intent2);
			break;
		//私募产品
		case R.id.la_my_placement:	
			Intent intent1=new Intent(this, MyPrivateProductsActivity.class);
			intent1.putExtra("idcard", idCard);
			startActivity(intent1);

			break;
		//基金配资
		case R.id.la_my_Financing:
			
			userName = App.getContext().getUserName();
			 RequestParams para=new RequestParams(this);
			 para.put("sdzjnumber", idCard);
			 para.put("username", userName);
			 execApi(ApiType.GET_JUDGEFNDSTWO, para, new OnDataReceivedListener() {
				
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
					if (code.equals("0000")&&hint.equals("已经拥有个人净值记录")&&info.equals("true")) {
						
						Intent intent4=new Intent(MyPropertyActivity.this, MyFundsWithCapitalActivity.class);
						intent4.putExtra("idcard", idCard);
						startActivity(intent4);
						
					}else if (code.equals("0001")&&hint.equals("已经预约，正在审核")&&info.equals("true")) {
						Intent intent5=new Intent(MyPropertyActivity.this, FinancingAuditActivity.class);
						startActivity(intent5);
						
					}else if (code.equals("0002")&&hint.equals("认证成功，未支付")&&info.equals("true")) {
						Intent intent=new Intent(MyPropertyActivity.this, SucceedNonPaymentActivity.class);
						startActivity(intent);
					}
					else if (code.equals("0003")&&hint.equals("认证成功，已支付")&&info.equals("true")) {
						Intent intent3=new Intent(MyPropertyActivity.this, PaySuccessActivity.class);
						startActivity(intent3);
					}
					else if (code.equals("500")&&hint.equals("还没有预约")&&info.equals("false")) {
						Intent intent6=new Intent(MyPropertyActivity.this, DistributionInformationActivity.class);
						startActivity(intent6);
					}
				}
			});  

		    break;  
		 //私人订制
		case R.id.la_my_Customize:
			
			userName = App.getContext().getUserName();
			 RequestParams param=new RequestParams(this);
			 param.put("sdzjnumber", idCard);
			 param.put("username", userName);
			 execApi(ApiType.GET_TODETERMINETHEMEBEER, param, new OnDataReceivedListener() {
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
					if (code.equals("0000")&&hint.equals("ok")&&info.equals("true")) {
						
						Intent intent4=new Intent(MyPropertyActivity.this, ButlerTongActivity.class);
						intent4.putExtra("idcard", idCard);
						startActivity(intent4);
						
					}else if (code.equals("0001")&&hint.equals("您已预约了管家通")&&info.equals("true")) {
						Intent intent5=new Intent(MyPropertyActivity.this, BeinAuditActivity.class);
						startActivity(intent5);
						
					}else if (code.equals("500")&&hint.equals("无数据")&&info.equals("false")) {
						Intent intent6=new Intent(MyPropertyActivity.this, OpenHousekeeperTongActivity.class);
						startActivity(intent6);
					}
				}
			});  
			break;
		//我的优惠券
		case R.id.la_my_Integral:
			userName = App.getContext().getUserName();
			Intent intent=new Intent(this, CouponActivity.class);
			intent.putExtra("userName", userName);
			startActivity(intent);
			break;
		//个人中心
		case R.id.ll_top_layout_right_view:
			startActivity(PersonalActivity.class);
			break;   
		//交易登陆
		case R.id.lay_image:
			encodePassWord = App.getContext().getEncodePassWord();
			if (encodePassWord!=null) {
				showProgressDialog("正在加载");
				RequestParams params = new RequestParams(getApplicationContext());
				params.put("id", idCard);
				params.put("passwd", encodePassWord);
				execApi(ApiType.GET_DEALLOGIN, params,new OnDataReceivedListener() {

							private DealSearchResult res1;
							private String certificateno;
							private String depositacct;
							private String certificatetype;
							private String channelid;
							private String moneyaccount;
							private String custno;
							private String expiredflag;
							private String risklevel;

							@Override
							public void onReceiveData(ApiType api,
									String json) {
								System.out.println("json=-=-=-=>"+json);
								if (json == null) {
									showToast("请求失败");
									disMissDialog();
									return;
								}
								if (json != null && !json.equals("")) {
									tInputStringStream = new ByteArrayInputStream(json.getBytes());
									XmlPullParser parser = Xml.newPullParser();
									System.out.println("parser--------->"+parser);
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
														JSONObject jsonObj = new JSONObject(xmlReturn);

														if (xmlReturn.contains("certificateno")) {

															risklevel = jsonObj
																	.getString("risklevel");
															depositacctname = jsonObj
																	.getString("depositacctname");
															totalfundmarketvalue = jsonObj
																	.getString("totalfundmarketvalue");
															countfund = jsonObj
																	.getString("countfund");
															certificateno = jsonObj
																	.getString("certificateno");
															depositacct = jsonObj
																	.getString("depositacct");
															certificatetype = jsonObj
																	.getString("certificatetype");
															channelid = jsonObj
																	.getString("channelid");
															moneyaccount = jsonObj
																	.getString("moneyaccount");
															custno = jsonObj
																	.getString("custno");
															expiredflag = jsonObj
																	.getString("expiredflag");
															Intent intent=new Intent(getApplicationContext(), DealInforActivity.class);
															intent.putExtra("IDCard", idCard);
															intent.putExtra("PassWord", encodePassWord);
															intent.putExtra("CustomRiskLevel", risklevel);
															intent.putExtra("CountFund", countfund);
															intent.putExtra("DepositacctName", depositacctname);
															intent.putExtra("TotalFundMarketValue", totalfundmarketvalue);
															intent.putExtra("lean", lean);
															startActivity(intent);

														} 
													} catch (Exception e) {
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
									disMissDialog();
								}


							}
						});
			}else {
				Intent intent6=new Intent(MyPropertyActivity.this, MyMeansActivity.class );
				startActivity(intent6);
				
			}
			break;
		}
	}
	
	
	@Override
	public void onBackPressed() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastBack > 2000) {
			lastBack = curTime;
			showToast("再按一次退出展恒基金网");
		} else {
			String listStr = JSON.toJSONString(App.getContext().userList);
			//Editor editor = sPreferences.edit();
			//editor.putString("UserList", listStr);
			//editor.commit();
			finish();
			
		}
	}


}
