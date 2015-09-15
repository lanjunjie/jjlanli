package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.TimeCountButton;
import com.myfp.myfund.utils.DESEncrypt;

public class ShortcutObligateActivity extends BaseActivity{

	private EditText edit_shortcut_importcard;
	private EditText edit_shortcut_phone;
	private TimeCountButton shortcut_register_gaincode;
	private Bundle bundle;
	private String token;
	private TimeCountButton new_register_gaincode;
	private String captcha;
	ByteArrayInputStream tInputStringStream = null;
	private EditText edit_shortcut_code;
	private String jsonstr;
	private String encodePassWord;
	public static ShortcutObligateActivity instance = null;
	private CheckBox shortcut_dredgede_readed;
	private TextView shortcut_dredgede_clause;


	@Override
	protected void setContentView() {
		setContentView(R.layout.shortcut_activity_binding);
		bundle = getIntent().getExtras();
		instance=this;
		App.getContext().setUserName(null);
		App.getContext().setIdCard(null);
		App.getContext().setMobile(null);
		App.getContext().setEncodePassWord(null);
	}

	@Override
	protected void initViews() {
		setTitle("绑定银行卡");
		edit_shortcut_importcard = (EditText) findViewById(R.id.edit_shortcut_importcard);
		edit_shortcut_phone = (EditText) findViewById(R.id.edit_shortcut_phone);
		edit_shortcut_code = (EditText) findViewById(R.id.edit_shortcut_code);
		findViewAddListener(R.id.butt_shortcut_confirm);
		shortcut_register_gaincode = (TimeCountButton) findViewById(R.id.shortcut_register_gaincode);
		shortcut_register_gaincode.setOnClickListener(this);
		shortcut_dredgede_readed = (CheckBox) findViewById(R.id.shortcut_dredgede_readed);
		shortcut_dredgede_readed.setChecked(true);
		shortcut_dredgede_clause = (TextView) findViewById(R.id.shortcut_dredgede_clause);
		shortcut_dredgede_clause.setText(Html.fromHtml("<u>服务条款</u>"));
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.butt_shortcut_confirm:
			 if (edit_shortcut_code.getText().toString().length()==0) {
				showToast("验证码不能为空");	
				return;
			}else if (edit_shortcut_code.getText().toString().length()!=6) {
				showToast("验证码不正确");
			}else if(shortcut_dredgede_readed.isChecked()==false) {
				showToast("请先阅读并同意相关服务条款!");
				return ;
			}
			else {
				RequestParams params=new RequestParams(this);
				params.put("channelid", bundle.getString("ChanneLid"));
				params.put("channelname", bundle.getString("ChanneName"));
				params.put("certificateno", bundle.getString("SDIdCard"));
				params.put("depositacct", edit_shortcut_importcard.getText().toString());
				params.put("depositacctname", bundle.getString("SDName"));
				params.put("mobiletelno", bundle.getString("Phone"));
				params.put("Keep_phone_num1", edit_shortcut_phone.getText().toString());
				params.put("token", token);
				params.put("keep_phone_code_num1", edit_shortcut_code.getText().toString());
				execApi(ApiType.GET_SMSOPENCARD, params);
			}
			break;
		case R.id.shortcut_register_gaincode:
			if (edit_shortcut_importcard.getText().length()==0) {
				showToast("请输入银行卡号");
				return;
			}else if (edit_shortcut_phone.getText().toString().length()==0) {
				showToast("请输入预留手机号");
				return;
			}else if (edit_shortcut_phone.getText().toString().length()!=11) {
				showToast("输入的手机号不正确");
				return;
			}else {
				shortcut_register_gaincode.TimeStart();
				//TODO 获取验证码
				captcha = Math.round(Math.random()*8999+1000)+"";
				
				RequestParams pam=new RequestParams(this);
				pam.put("channelid", bundle.getString("ChanneLid"));
				pam.put("channelname", bundle.getString("ChanneName"));
				pam.put("certificateno", bundle.getString("SDIdCard"));
				pam.put("depositacct", edit_shortcut_importcard.getText().toString());
				pam.put("depositacctname", bundle.getString("SDName"));
				pam.put("mobiletelno", bundle.getString("Phone"));
				pam.put("Keep_phone_num1", edit_shortcut_phone.getText().toString());
				execApi(ApiType.GET_OPENCARD, pam);
			}
			break;
		}
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if ("".equals(json)||json==null) {
			showToast("网络不给力");
			return;
		}
		if (api == ApiType.GET_CHECK_CODETWO) {
			JSONArray array;
			try {
				array = new JSONArray(json);
				int result = array.getJSONObject(0).getInt("returnValue");
				switch (result) {
				case 0:
					showToast("发送成功!");
					break;
				case -4:
					showToast("手机号码格式不正确!");
					break;
				case -9:
					showToast("发送失败，请重新发送!");
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if (api==ApiType.GET_OPENCARD) {
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
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);
										JSONObject jsonObj;
										try {
											jsonObj = new JSONObject(xmlReturn);
											if (jsonObj.getString("code").equals("0000")) {
												token = jsonObj.getString("token");
												
											}else {
												
												showToastLong(jsonObj.getString("msg"));
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
			
			
		}else if (api==ApiType.GET_SMSOPENCARD) {
			
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
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);
										JSONObject jsonObj;
										try {
											jsonObj = new JSONObject(
													xmlReturn);
											String code = jsonObj.getString("code");
											if (jsonObj.getString("code").equals("0000")) {
												
												DESEncrypt desEpt = new DESEncrypt();
												
													try {
														encodePassWord = desEpt.encrypt(bundle.getString("SDealPass"));
														System.out.println("encodePassWord============>"+encodePassWord);
													} catch (Exception e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												
												JSONObject jsonobj = new JSONObject();
													
												jsonobj.put("referralprovincename", "");
												jsonobj.put("transactorcerttype", "");
												jsonobj.put("minorflag", "");
												jsonobj.put("referralmobile", "");
												jsonobj.put("depositprov", bundle.getString("NewProvinces"));
												jsonobj.put("depositacctname",bundle.getString("SDName"));
												jsonobj.put("ismainback", "");
												jsonobj.put("officetelno", "");
												jsonobj.put("smallflag", "0");
												jsonobj.put("fax", "");
												jsonobj.put("nickname", "");
												jsonobj.put("bankname", bundle.getString("AccountAddress"));
												jsonobj.put("tpasswd", encodePassWord);
												jsonobj.put("annualincome", "");
												jsonobj.put("educationlevel", "");
												jsonobj.put("postcode", "");
												jsonobj.put("minorid", "");
												jsonobj.put("transactorname", "");
												jsonobj.put("familyname", "");
												jsonobj.put("country", "156");
												jsonobj.put("custfullname", bundle.getString("SDName"));
												jsonobj.put("depositacct", edit_shortcut_importcard.getText().toString());
												jsonobj.put("email", "");
												jsonobj.put("delivertype", "1");
												jsonobj.put("signflag", "1");
												jsonobj.put("paycenterid",bundle.getString("PayCenterId"));
												jsonobj.put("lpasswd", encodePassWord);
												jsonobj.put("certificatetype", "0");
												jsonobj.put("sex", "");
												jsonobj.put("channelname", bundle.getString("NewDBK"));
												jsonobj.put("referral",bundle.getString("Code"));
												jsonobj.put("telno", "");
												jsonobj.put("hometelno", "");
												jsonobj.put("depositcity", bundle.getString("NewCity"));
												jsonobj.put("vocationcode", "");
												jsonobj.put("channelid", bundle.getString("ChanneLid"));
												jsonobj.put("ismainpay", "");
												jsonobj.put("referralcityname", "");
												jsonobj.put("deliverway", "00000000");
												jsonobj.put("_", "");
												jsonobj.put("transactorcertno", "");
												jsonobj.put("custname", bundle.getString("SDName"));
												jsonobj.put("certificateno", bundle.getString("SDIdCard"));
												jsonobj.put("transactorvalidate", "");
												jsonobj.put("firstname", "");
												jsonobj.put("shsecuritiesaccountid", "");
												jsonobj.put("vailddate", "20591231");
													//手机号通过接口加载
												jsonobj.put("mobileno",bundle.getString("Phone"));
												jsonobj.put("investorsbirthday",bundle.getString("SDIdCard").substring(6, 14));
												jsonobj.put("address", "未填写");
												jsonobj.put("transactorcertrefer", "");
												jsonobj.put("transactortel", "");
												jsonobj.put("custmanagerid", "");
												jsonobj.put("szsecuritiesaccountid", "");
													 
												jsonstr = jsonobj.toString();
												RequestParams params = new RequestParams(this);
												params.put("paramMap",java.net.URLEncoder.encode(jsonstr));
												execApi(ApiType.GET_OPENACCOUNT2,params,new OnDataReceivedListener() {
													
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
																				System.out.println("<><><><><><><><><>"
																						+ xmlReturn);
																				if(xmlReturn.contains("appsheetserialno")){
																					JSONObject jsonObj;
																					try {
																						jsonObj = new JSONObject(
																								xmlReturn);
																						if(jsonObj.getString("appsheetserialno").length()==0){
																							disMissDialog();
																							showToast("客户签约失败");
																							return;
																						}else{
																							String sessionid = App.getContext().getSessionid();
																							System.out.println("开户sessionid----------------->"+sessionid);
																							showToast("开户成功！");
																							Intent intent=new Intent(ShortcutObligateActivity.this, ShortcutSuccesActiviyt.class);
																							intent.putExtra("DisplayName", bundle.getString("SDName"));
																							intent.putExtra("Mobile", bundle.getString("Phone"));
																							intent.putExtra("IDcard", bundle.getString("SDIdCard"));
																							intent.putExtra("sessionId", sessionid);
																							startActivity(intent);
																						
																						}
																					} catch (JSONException e) {
																						// TODO Auto-generated catch block
																						e.printStackTrace();
																					}
																					
																				}else{
																					JSONObject jsonObj;
																					try {
																						jsonObj = new JSONObject(xmlReturn);
																						disMissDialog();
																						showToast(jsonObj.getString("msg"));
																					} catch (JSONException e) {
																						// TODO Auto-generated catch block
																						e.printStackTrace();
																					}
																					
																					return;
																					
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
