package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.string;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Type;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import cn.jpush.android.api.q;
import cn.jpush.android.data.p;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.R.integer;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.PlacementResult;
import com.myfp.myfund.ui.FundNewsActivity.MyNewsAdapter;
import com.myfp.myfund.ui.ManagerFragment.ManagerListAdapter;

public class MyPropertyMemberActivity extends BaseActivity{

	private View view;
	private MyPropertyActivity activity;
	private TextView tev_moneysum;
	private RadioGroup radio;
	private float mCurrentCheckedRadioLeft;
	private RadioButton bt_member_information;
	private RadioButton bt_Purchase_details;
	private RadioButton bt_show_details;
	private ViewPager viewpager_member;
	ByteArrayInputStream tInputStringStream = null;
	private FragmentManager fm = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();
	private EditText edi_import_cash;
	private String idcard;
	private String bankCardId;
	private Spinner sp_ne;
	private String transactionaccountid;
	private String channelname;
	private String depositacct;
	private List<String> list;
	private CharSequence subSequence;
	private String sequence;
	private String value;
	private TextView tex_has_been;
	private TextView tex_Khunti;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_my_information);
		fragmentTransaction.replace(R.id.lan_member, new MyMemberFragment());
		fragmentTransaction.commit();
		Intent intent = getIntent();
		idcard = intent.getStringExtra("idcard");
		bankCardId = intent.getStringExtra("BankCardId");
		RequestParams params=new RequestParams(activity);
		params.put("idcard", idcard.trim());
		execApi(ApiType.GET_MEMBERINFORMATIONNEW, params);
		execApi(ApiType.GET_BANKCARDMESSAGENEW, params);	
		sp_ne = (Spinner) findViewById(R.id.sp_ne);
		
	}
	
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
		tev_moneysum = (TextView) findViewById(R.id.tev_moneysum);
		edi_import_cash = (EditText) findViewById(R.id.edi_import_cash);
		
		tex_has_been = (TextView) findViewById(R.id.tex_has_been);
		tex_Khunti = (TextView) findViewById(R.id.tex_Khunti);
		findViewAddListener(R.id.present_button);
		
		
	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.present_button:
			String edi = edi_import_cash.getText().toString();
			
			if (edi.equals("")) {
				showToast("请输入提现金额...");
				disMissDialog();
			}else {
				
				int parseInt = Integer.parseInt(edi);
			
				if (parseInt<100||parseInt>800) {
					showToastLong("您提现的金额不能小于100元或大于800元");
					disMissDialog();
				}else {
					value = String.valueOf(parseInt);
			RequestParams params=new RequestParams(this);
			params.put("idcard", idcard.trim());
			
			execApi(ApiType.GET_JUDGEWITHDRAWALDEGREENEW, params,new OnDataReceivedListener() {
				
				@Override
				public void onReceiveData(ApiType api, String json) {
					
					if (json == null) {
						showToast("请求失败!");
						disMissDialog();
						return;
					} else {
						if (api == ApiType.GET_JUDGEWITHDRAWALDEGREENEW) {

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
														JSONObject jsonObj = new JSONObject(xmlReturn);
														if (jsonObj.getString("judgeitem").equals("true")) {
															RequestParams params=new RequestParams(MyPropertyMemberActivity.this);
															params.put("idcard", idcard.trim());
															params.put("point", value.trim());
															params.put("depositacct",sequence);
															execApi(ApiType.GET_WITHDREWDEPOSITNEW, params,new OnDataReceivedListener() { 
																
																@Override
																public void onReceiveData(ApiType api, String json) {
																	if (json==null) {
																		showToast("提现失败");
																		disMissDialog();
																		return;
																	}
																	if (api == ApiType.GET_WITHDREWDEPOSITNEW) {

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
																								System.out.println("我的提现json---------------->"
																										+ xmlReturn);

																								try {
		
																									JSONObject jsonObj = new JSONObject(xmlReturn);
																									if (jsonObj.getString("ret_code").equals("0000")&&jsonObj.getString("ret_data").equals("true")) {
																										Intent intent=new Intent(MyPropertyMemberActivity.this, MyWithdrawalSuccessActivity.class);
																										intent.putExtra("idcard", idcard);
																										startActivity(intent);
																									}else if (jsonObj.getString("ret_code").equals("-200")){
																										showToastLong("您的提现金额不能大于您所剩余金额！");
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
																	
																	}
																	
																}
									
															});
															
														}else {
															showToastLong("对不起，你的提现失败！请检查网络...");
															
														}
														
													} catch (JSONException e) {
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

					}
					
				}
			});
				}
			}	
			break;

		
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json==null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_MEMBERINFORMATIONNEW) {

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
									System.out.println("我的提现json---------------->"
											+ xmlReturn);

									try {
										
										JSONObject jsonObj = new JSONObject(xmlReturn);
										System.out.println("jsonObj-------------->"+jsonObj);
										
										String saveApply = jsonObj.getString("saveApplyCost");
										DecimalFormat df=new DecimalFormat("#0.##");
										double saveApplyCost=Double.parseDouble(saveApply);
										 String ApplyCost = df.format(saveApplyCost);
										tev_moneysum.setText(ApplyCost+"元");
										
										String alreadyReturnApplyCost = jsonObj.getString("alreadyReturnApplyCost");
										DecimalFormat de=new DecimalFormat("#0.##");
										double alreadyReturnApp=Double.parseDouble(alreadyReturnApplyCost);
										 String ReturnApplyCost = de.format(alreadyReturnApp);
										 tex_has_been.setText(ReturnApplyCost+"元");
										 
										 DecimalFormat sa=new DecimalFormat("#0.##");
										String canReturnApp = jsonObj.getString("canReturnApplyCost");
										double saveApp=Double.parseDouble(canReturnApp);
										 String Apply = sa.format(saveApp);
										 tex_Khunti.setText(Apply+"元");
										 
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
		
		} else if (api==ApiType.GET_BANKCARDMESSAGENEW) {
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
										list = new ArrayList<String>();
										System.out.println("list_---------------->"+list);
										JSONArray jsonArray=new JSONArray(xmlReturn);
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject object = jsonArray.getJSONObject(i);
											channelname = object.getString("channelname");
											System.out.println("channelname============>"+channelname);
											depositacct = object.getString("depositacct");
											System.out.println("channelname============>"+depositacct);
											ArrayAdapter<String> accountsadapter=new ArrayAdapter<String>(MyPropertyMemberActivity.this,android.R.layout.simple_spinner_item,list);
											System.out.println("accountsadapter------------->"+accountsadapter);
											list.add(channelname+":"+depositacct);
											System.out.println("list===============>"+list);
											sp_ne.setAdapter(accountsadapter);
											sp_ne.setSelection(0, true);
											sp_ne.setOnItemSelectedListener(new OnItemSelectedListener() {

												

												@Override
												public void onItemSelected(
														AdapterView<?> arg0,
														View arg1, int arg2,
														long arg3) {
													String str=list.get(arg2);
													 arg0.setVisibility(View.VISIBLE);
													 System.out.println("str--------------->"+str);
													int of = str.indexOf(":");
													subSequence = str.subSequence(of+1, str.length());
													System.out.println("subSequence============>"+subSequence);
													sequence = subSequence.toString();
													System.out.println("Sequence=-=-=-=-=-=->"+sequence);
												}

												@Override
												public void onNothingSelected(
														AdapterView<?> arg0) {
													// TODO Auto-generated method stub
													
												}
											});
											
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}
		
	}
	
	

	}
	


