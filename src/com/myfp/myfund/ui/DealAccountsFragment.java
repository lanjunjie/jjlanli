package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.utils.DESEncrypt;

public class DealAccountsFragment extends BaseFragment{
	
	ByteArrayInputStream tInputStringStream = null;
	private String idCard, passWord, encodeIdCard, encodePassWord, fundcode,
			risklevel, depositacctname, totalfundmarketvalue, countfund,
			certificateno, depositacct, certificatetype, channelid,
			moneyaccount, busidate, custno, expiredflag, idcard18_show,
			idcard15_show;
	int count;
	
	MyMeansActivity activity ;
	SharedPreferences sPreferences;
	private EditText editt_id_number;
	private EditText editt_dealpasww_text;
	private TextView textv_deal_pasww;
	private Button butt_register_on;
	private TextView textv_open_account;
	private String userName;
	private String mobile;
	private String user;
	private String sessionid,flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=(MyMeansActivity) getActivity();
		Intent intent = activity.getIntent();
		fundcode = intent.getStringExtra("FundCode");
		flag =intent.getStringExtra("Flag");
		System.out.println("flageeeee"+flag);
		sPreferences = activity.getSharedPreferences("Setting", activity.MODE_PRIVATE);
		user = intent.getStringExtra("User");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_dealaccounts, null);
		editt_id_number = (EditText) view.findViewById(R.id.editt_id_number);
		editt_dealpasww_text = (EditText) view.findViewById(R.id.editt_dealpasww_text);
		textv_deal_pasww = (TextView) view.findViewById(R.id.textv_deal_pasww);
		butt_register_on = (Button) view.findViewById(R.id.butt_register_on);
		textv_open_account = (TextView) view.findViewById(R.id.textv_dealphone);
		textv_open_account.setOnClickListener(this);
		textv_deal_pasww.setOnClickListener(this);
		butt_register_on.setOnClickListener(this);
		if (sPreferences.getString("IDCard", null) != null) {
			if (sPreferences.getString("IDCard", null).length() == 15) {
				editt_id_number.setText(sPreferences.getString("IDCard", null)
						.replace(
								sPreferences.getString("IDCard", null)
										.substring(8, 13), "*****"));
				idcard15_show = sPreferences.getString("IDCard", null)
						.replace(
								sPreferences.getString("IDCard", null)
										.substring(8, 13), "*****");
			} else if (sPreferences.getString("IDCard", null).length() == 18) {
				editt_id_number.setText(sPreferences.getString("IDCard", null)
						.replace(
								sPreferences.getString("IDCard", null)
										.substring(11, 16), "*****"));
				idcard15_show = sPreferences.getString("IDCard", null).replace(
						sPreferences.getString("IDCard", null)
								.substring(11, 16), "*****");
			}

		}
		
		return view;
	}
	@Override
	protected void onViewClick(View v) {
	switch (v.getId()) {
	case R.id.textv_deal_pasww:
		Intent intent=new Intent(activity, ConfirmInformationActivity.class);
		intent.putExtra("tar", "true");
		startActivity(intent);
		break;
	case R.id.butt_register_on:
		login();
		break;
	case R.id.textv_dealphone:
		Intent intent1 = new Intent();
		intent1.setAction(Intent.ACTION_DIAL);
		intent1.setData(Uri.parse("tel:400-888-6661"));
		startActivity(intent1);
		break;
	default:
		break;
	}
		
	}
	void login() {
		idCard = editt_id_number.getText().toString();
		passWord = editt_dealpasww_text.getText().toString();
		if (idCard.length() == 0 || passWord.length() == 0) {
			activity.showToast("身份证号或密码为空！！");
			return;
		} else {
			ByteArrayInputStream is;
			try {
				if (sPreferences.getString("IDCard", null) != null&& (idCard.equals(idcard15_show) || idCard
								.equals(idcard18_show))) {
					idCard = sPreferences.getString("IDCard", null);
				}
				is = new ByteArrayInputStream(idCard.getBytes("ISO-8859-1"));
				BufferedReader consoleReader = new BufferedReader(
						new InputStreamReader(is));
				String idNum = null;
				try {
					idNum = consoleReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
				Pattern idNumPattern = Pattern
						.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
				// 通过Pattern获得Matcher
				Matcher idNumMatcher = idNumPattern.matcher(idNum);
				if (idNumMatcher.matches()) {
					if (passWord.contains(" ")) {
						activity.showToast("交易密码格式不正确，请重新输入！");
						return;
					} else {
						DESEncrypt desEpt = new DESEncrypt();
						try {
							encodePassWord = desEpt.encrypt(passWord);
							encodePassWord = java.net.URLEncoder
									.encode(encodePassWord);
							System.out.println("加密以后的账号和密码" + encodeIdCard
									+ "  " + encodePassWord);
							RequestParams pms=new RequestParams(activity);
							pms.put("IDcard", idCard);
							RndDataApi.executeNetworkApi(ApiType.GET_IDCRADAUDITUSANM, pms, this);

							RequestParams params = new RequestParams(activity);
							params.put("id", idCard);
							params.put("passwd", encodePassWord);
							RndDataApi.executeNetworkApi(ApiType.GET_DEALLOGINTWO, params, this);
							activity.showProgressDialog("正在登录");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					activity.showToast("身份证号格式不正确");
					return;
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			activity.showToast("请求失败!");
			activity.disMissDialog();
			return;
		} if (api==ApiType.GET_IDCRADAUDITUSANM) {
			JSONArray array;
			try {
				array = new JSONArray(json);
				userName = array.getJSONObject(0).getString("UserName");
				System.out.println("userNameabc------------->"+userName);
				mobile = array.getJSONObject(0).getString("Mobile");
				App.getContext().setUserName(userName);
				App.getContext().setMobile(mobile);
				System.out.println("mobileabc============>"+mobile);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if (api == ApiType.GET_DEALLOGINTWO) {

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
											JSONObject jsonObj = new JSONObject(
													xmlReturn);

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
												sessionid = jsonObj.getString("sessionid");
												System.out
														.println("sessionId=====>"+sessionid);
												activity.showToast("登录成功！");
												if (activity.getIntent().getStringExtra("gms")!=null) {
													Intent intent = new Intent(
															activity ,
															MyActivityGroup.class);
													intent.putExtra(
															"IDCard",
															idCard);
													intent.putExtra(
															"PassWord",
															encodePassWord);
													intent.putExtra(
															"CustomRiskLevel",
															risklevel);
													intent.putExtra(
															"DepositacctName",
															depositacctname);
													intent.putExtra(
															"TotalFundMarketValue",
															totalfundmarketvalue);
													intent.putExtra(
															"CountFund",
															countfund);
													intent.putExtra("certificateno", certificateno);
													intent.putExtra("depositacct", depositacct);
													intent.putExtra("certificatetype", certificatetype);
													intent.putExtra("Moneyaccount", moneyaccount);
													intent.putExtra("Custno", custno);
													intent.putExtra("channelid", channelid);
													intent.putExtra("Fundcode", fundcode);
													intent.putExtra("sessionid", sessionid);
													intent.putExtra("UserName", userName);
													intent.putExtra("Mobile", mobile);
													intent.putExtra("Flag", "0");
													startActivity(intent);
													App.getContext()
													.setEncodePassWord(
															encodePassWord);
													App.getContext()
													.setIdCard(
															idCard);
													App.getContext().setUserName(userName);
													App.getContext().setSessionid(sessionid);
													App.getContext().setDepositacctName(depositacctname);
													App.getContext().setMobile(mobile);
													SharedPreferences sPreferences = activity.getSharedPreferences(
															"Setting",
															activity.MODE_PRIVATE);
													Editor editor = sPreferences
															.edit();
													editor.putString(
															"EncodePassWord",
															encodePassWord);
													editor.putString(
															"IDCard",
															idCard);
													editor.putString(
															"CustomRiskLevel", 
															risklevel);
													editor.putString(
															"CountFund", 
															countfund);
													editor.putString(
															"DepositacctName", 
															depositacctname);
													editor.putString(
															"TotalFundMarketValue",
															totalfundmarketvalue);
													editor.putString("Mobile", mobile);
													editor.putString("UserName", userName);
													editor.putString("sessionid", sessionid);
													editor.commit();
													activity.finish();

														activity.disMissDialog();
												} else{
												if(flag==null){
													if (user==null) {
														Intent intent = new Intent(
																activity ,
																MyActivityGroup.class);
														intent.putExtra(
																"IDCard",
																idCard);
														intent.putExtra(
																"PassWord",
																encodePassWord);
														intent.putExtra(
																"CustomRiskLevel",
																risklevel);
														intent.putExtra(
																"DepositacctName",
																depositacctname);
														intent.putExtra(
																"TotalFundMarketValue",
																totalfundmarketvalue);
														intent.putExtra(
																"CountFund",
																countfund);
														intent.putExtra("certificateno", certificateno);
														intent.putExtra("depositacct", depositacct);
														intent.putExtra("certificatetype", certificatetype);
														intent.putExtra("Moneyaccount", moneyaccount);
														intent.putExtra("Custno", custno);
														intent.putExtra("channelid", channelid);
														intent.putExtra("Fundcode", fundcode);
														intent.putExtra("sessionid", sessionid);
														intent.putExtra("UserName", userName);
														intent.putExtra("Mobile", mobile);
														intent.putExtra("Flag", "5");
														startActivity(intent);
															activity.finish();
										
													
														
														}else{Intent intent=new Intent(activity, GoodsPassActivity.class);
														startActivity(intent);
														activity.finish();
														}
													App.getContext()
													.setEncodePassWord(
															encodePassWord);
													App.getContext()
													.setIdCard(
															idCard);
													App.getContext().setUserName(userName);
													App.getContext().setDepositacctName(depositacctname);
													App.getContext().setSessionid(sessionid);
													App.getContext().setMobile(mobile);
													SharedPreferences sPreferences = activity.getSharedPreferences(
															"Setting",
															activity.MODE_PRIVATE);
													Editor editor = sPreferences
															.edit();
													editor.putString(
															"EncodePassWord",
															encodePassWord);
													editor.putString(
															"IDCard",
															idCard);
													editor.putString(
															"CustomRiskLevel", 
															risklevel);
													editor.putString(
															"CountFund", 
															countfund);
													editor.putString(
															"DepositacctName", 
															depositacctname);
													editor.putString(
															"TotalFundMarketValue",
															totalfundmarketvalue);
													editor.putString("Mobile", mobile);
													editor.putString("UserName", userName);
													editor.putString("sessionid", sessionid);
													editor.commit();
													activity.finish();

														activity.disMissDialog();
												} else{
											
														Intent intent=new Intent(activity,FundSelectActivity.class);
														intent.putExtra("sessionId", sessionid);
														startActivity(intent);
														App.getContext()
														.setEncodePassWord(
																encodePassWord);
														App.getContext()
														.setIdCard(
																idCard);
														App.getContext().setUserName(userName);
														App.getContext().setSessionid(sessionid);
														App.getContext().setDepositacctName(depositacctname);
														App.getContext().setMobile(mobile);
														SharedPreferences sPreferences = activity.getSharedPreferences(
																"Setting",
																activity.MODE_PRIVATE);
														Editor editor = sPreferences
																.edit();
														editor.putString(
																"EncodePassWord",
																encodePassWord);
														editor.putString(
																"IDCard",
																idCard);
														editor.putString(
																"CustomRiskLevel", 
																risklevel);
														editor.putString(
																"CountFund", 
																countfund);
														editor.putString(
																"DepositacctName", 
																depositacctname);
														editor.putString(
																"TotalFundMarketValue",
																totalfundmarketvalue);
														editor.putString("Mobile", mobile);
														editor.putString("UserName", userName);
														editor.putString("sessionid", sessionid);
														editor.commit();
														activity.finish();
														activity.disMissDialog();
													}
													
												}	
							
											} else {
												JSONObject object=new JSONObject(xmlReturn);
												String logingflag = object.getString("loginflag");
												if (logingflag.equals("false")) {
													App.getContext().setEncodePassWord(null);
													App.getContext().setUserName(null);
													App.getContext().setSessionid(null);
												}
												activity.disMissDialog();
												activity.showToast("密码不正确！");
										

												return;
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// String returnResult =
										// jsonObj.getString("loginflag");

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
				activity.disMissDialog();
			} else if (api == ApiType.GET_DEALSEARCHONETWO) {

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
										List<DealSearchResult> list;
										list = JSON.parseArray(xmlReturn,
												DealSearchResult.class);
										DealSearchResult res = list.get(0);
										String sessionId2 = res.getSessionId();
										System.out.println("sessionId2------>"+sessionId2);
										System.out.println("res============>"+res);
										Intent intent = new Intent(
												activity,
												DealApplyActivity.class);
										Bundle bundle = new Bundle();
										bundle.putString("IDCard", idCard);
										bundle.putString("PassWord",
												encodePassWord);
										bundle.putSerializable(
												"DealSearchResult", res);
										// intent.putExtra("IDCard",
										// encodeIdCard);
										// intent.putExtra("PassWord",
										// encodePassWord);
										intent.putExtras(bundle);
										startActivity(intent);
										activity.disMissDialog();

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
	}

	private String rep(String str) {
		String repStr;
		repStr = str.replace("=", "%3D").replace("+", "%2B")
				.replace(" ", "%20").replace("/", "%2F").replace("?", "%3F")
				.replace("%", "%25").replace("#", "%23").replace("&", "%26");
		return repStr;

	}
}
