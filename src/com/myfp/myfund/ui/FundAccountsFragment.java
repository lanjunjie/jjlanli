package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.RndDataApi;

public class FundAccountsFragment extends BaseFragment {

	private EditText editt_fund_naemmobile;
	private EditText editt_pasww_text;
	private TextView textv_forget_pasww;
	private Button but_register_ton;
//	private TextView textv_Register_Now;
	private MyMeansActivity activity;
	private String username,password,idcard,user;
	private SharedPreferences sPreferences;
	private boolean MainIsStart;
	private String moblie;
	private String displayName;
	private String openaccount;
	private String sessionid;
	private TextView textv_dealphone1;
	private String flag;
	ByteArrayInputStream tInputStringStream = null;
	private String certificateno;
	private String moneyaccount;
	private String risklevel;
	private String countfund;
	private String depositacctname;
	private String channelid;
	private String depositacct;
	private String custno;
	private String totalfundmarketvalue;
	private String custname;
	private String certificatetype;
	private JSONObject jsonObj;
	private Button shortcut_register_ton;
	private String userName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MyMeansActivity) getActivity();
		sPreferences = activity.getSharedPreferences("Setting",
				activity.MODE_PRIVATE);
		MainIsStart = sPreferences.getBoolean("mainIsStart", false);
		Intent intent = activity.getIntent();
		flag =intent.getStringExtra("Flag");
		user = intent.getStringExtra("User");
		System.out.println("user123------------>"+user);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_fund_accounts, null);
		editt_fund_naemmobile = (EditText) view.findViewById(R.id.editt_fund_naemmobile);
		editt_pasww_text = (EditText) view.findViewById(R.id.editt_pasww_text);
		textv_forget_pasww = (TextView) view.findViewById(R.id.textv_forget_pasww);
		but_register_ton = (Button) view.findViewById(R.id.but_register_ton);
//		textv_Register_Now = (TextView) view.findViewById(R.id.textv_Register_Now);
		textv_dealphone1=(TextView) view.findViewById(R.id.textv_dealphone1);
		//shortcut_register_ton = (Button) view.findViewById(R.id.shortcut_register_ton);
		//shortcut_register_ton.setOnClickListener(this);
		textv_dealphone1.setOnClickListener(this);
		textv_forget_pasww.setOnClickListener(this);
		but_register_ton.setOnClickListener(this);
//		textv_Register_Now.setOnClickListener(this);
		return view;
	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.textv_forget_pasww:
			//修改密码   
			Intent intent=new Intent(getActivity(), ResetPassActivity.class);
			startActivity(intent);
			break;
		case R.id.but_register_ton:
			//登录
			login();
			break;
//		case R.id.textv_Register_Now:
//			//注册
//			startActivity(new Intent(getActivity(), RegisterUserActivity.class));
//			break;
		case R.id.textv_dealphone1:
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_DIAL);
			intent1.setData(Uri.parse("tel:400-888-6661"));
			startActivity(intent1);
			break;	
	/*	case R.id.shortcut_register_ton:
			Intent intent2=new Intent(activity, ShortcutRegisterActivity.class);
			startActivity(intent2);
			break;
			*/
		default:
			break;
		}
		
	}
	/**
	 * 登陆 
	 */
	private void login() {
		userName = editt_fund_naemmobile.getText().toString();
		 password =editt_pasww_text.getText().toString();
		System.out.println("密码————"+password);
//		showProgressDialog("正在登录");
		activity.showProgressDialog("正在登录");
		RequestParams params = new RequestParams(activity);
		params.put(RequestParams.USERNAME, userName);
		params.put(RequestParams.PASSWORD, password);
		RndDataApi.executeNetworkApi(ApiType.LOGIN, params, this);
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if(json==null){
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		try {
			if(api == ApiType.LOGIN){
				JSONArray array = new JSONArray(json);
				int returnResult = array.getJSONObject(0).getInt("ReturnResult");
				activity.disMissDialog();
				switch (returnResult) {
				case 0:
					if (!App.getContext().userList.contains(userName)) {
						App.getContext().userList.add(userName);
					}
					RequestParams params = new RequestParams(activity);
					params.put(RequestParams.USERNAME, userName);
				
					RndDataApi.executeNetworkApi(ApiType.GET_USER_INFO, params, this);
					
					break;
				case 1:
					activity.showToast("用户名不能为空!");
					break;
				case 2:
					activity.showToast("密码不能为空!");
					break;
				case 3:
					activity.showToast("密码长度只能为6到16位!");
					break;
				case 4:
					activity.showToast("密码不正确!");
					break;
				case 5:
					activity.showToast("用户名不存在，请先注册!");
					break;	
				case 6:
					activity.showToast("该手机已注册其他用户!");
					break;
				}
			}
			else if (api == ApiType.GET_USER_INFO) {
				 JSONArray array = new JSONArray(json);
				username = array.getJSONObject(0).getString("UserName");
				System.out.println("username2====>"+username);
				System.out.println("moblie2------->"+moblie);
				idcard = array.getJSONObject(0).getString("IDCard");
		
				System.out.println("moblie2------->"+moblie);
				moblie = array.getJSONObject(0).getString("Mobile");
				System.out.println("moblie2------->"+moblie);
				App.getContext().setMobile(moblie);
				App.getContext().setUserName(username);
				System.out.println("username2username2"+App.getContext().getUserName());
				SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
				Editor editor = sPreferences.edit();
				editor.putString("UserName", username);
				editor.commit();
				//真实姓名
				displayName = array.getJSONObject(0).getString("DisplayName");
				
				//判断是否开户
				openaccount = array.getJSONObject(0).getString("OpenAccount");
				
				
//				if (!idcard.equals("")) {
					
					RequestParams pam=new RequestParams(activity);
					System.out.println("idcard===============>"+idcard+"1111");
					pam.put(RequestParams.iDIcard, idcard);
					RndDataApi.executeNetworkApi(ApiType.GET_STEPVERIFICATION, pam,new OnDataReceivedListener() {

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
														jsonObj = new JSONObject(xmlReturn);
														System.out.println("jsonObj===========>"+jsonObj);
														if (jsonObj.getString("loginflag").equals("false")) {
															activity.showToast("登陆成功!");
															System.out.println("登陆后的idcard-------------->"+idcard);
													
															if(flag==null){
																if (user==null) {
																	App.getContext().setUserName(username);
																	System.out.println("ussssssssssssssssssssss"+username);
																	App.getContext().setIdCard(idcard);
																	App.getContext().setSessionid(sessionid);
																	App.getContext().setDepositacctName(displayName);
																	
																	SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
																	Editor editor = sPreferences.edit();
																	editor.putString("UserName", username);
																	
																	editor.putString("IDCard", idcard);
																	editor.putString("password", password);
																	editor.putString("Mobile", moblie);
																	editor.putString("DepositacctName", displayName);
																	editor.putString("sessionid", sessionid);
																	editor.commit();
																	Intent intent=new Intent(activity,MyActivityGroup.class);
																	intent.putExtra("iDCard", idcard);
																	intent.putExtra("DisplayName", displayName);
																	intent.putExtra("UserName", username);
																	System.out.println("ussssssssssssssssssssss"+username);
																	System.out.println("sessionid3---------->"+sessionid);
																	intent.putExtra("SessionId", sessionid);
																	intent.putExtra("Mobile", moblie);
																	intent.putExtra("Flag", "5");
																	startActivity(intent);
																	activity.finish();
																	activity.disMissDialog();
																} else {
																	
																	Intent intent=new Intent(activity, GoodsPassActivity.class);
																	startActivity(intent);
																	activity.finish();
																	activity.disMissDialog();
																	
																}
																App.getContext().setUserName(username);
																System.out.println("ussssssssssssssssssssss"+username);
																App.getContext().setIdCard(idcard);
																App.getContext().setSessionid(sessionid);
																App.getContext().setDepositacctName(displayName);
																SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
																Editor editor = sPreferences.edit();
																editor.putString("UserName", username);
																editor.putString("IDCard", idcard);
																editor.putString("password", password);
																editor.putString("Mobile", moblie);
																editor.putString("DepositacctName", displayName);
																editor.putString("sessionid", sessionid);
																editor.commit();
																activity.finish();
																activity.disMissDialog();
															} else{
																
																Intent intent=new Intent(activity,FundSelectActivity.class);
																intent.putExtra("sessionId", sessionid);
																startActivity(intent);
																App.getContext().setUserName(username);
																System.out.println("ussssssssssssssssssssss"+username);
																App.getContext().setIdCard(idcard);
																App.getContext().setSessionid(sessionid);
																App.getContext().setDepositacctName(displayName);
																SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
																Editor editor = sPreferences.edit();
																editor.putString("UserName", username);
																editor.putString("IDCard", idcard);
																editor.putString("password", password);
																editor.putString("Mobile", moblie);
																editor.putString("DepositacctName", displayName);
																editor.putString("sessionid", sessionid);
																editor.commit();
																activity.finish();
																activity.disMissDialog();
															}
															
															
															
														}else {
														sessionid = jsonObj.getString("sessionid");
														certificateno = jsonObj.getString("certificateno");
														moneyaccount = jsonObj.getString("moneyaccount");
														risklevel = jsonObj.getString("risklevel");
														countfund = jsonObj.getString("countfund");
														depositacctname = jsonObj.getString("depositacctname");
														channelid = jsonObj.getString("channelid");
														depositacct = jsonObj.getString("depositacct");
														custno = jsonObj.getString("custno");
														totalfundmarketvalue = jsonObj.getString("totalfundmarketvalue");
														custname = jsonObj.getString("custname");
														certificatetype = jsonObj.getString("certificatetype");
														System.out.println("sessionid1------------>"+sessionid);
														activity.showToast("登陆成功!");
														System.out.println("登陆后的idcard-------------->"+idcard);
														if (activity.getIntent().getStringExtra("gms")!=null) {
															Intent intent=new Intent(activity,MyActivityGroup.class);
															intent.putExtra("iDCard", idcard);
															intent.putExtra("DisplayName", displayName);
															intent.putExtra("UserName", username);
															intent.putExtra("sessionid", sessionid);
															intent.putExtra("Mobile", moblie);
															intent.putExtra(
																	"CustomRiskLevel",
																	risklevel);
															System.out.println("risklevel---->"+risklevel);
															intent.putExtra(
																	"DepositacctName",
																	depositacctname);
															System.out.println("depositacctname---->"+depositacctname);
															intent.putExtra(
																	"TotalFundMarketValue",
																	totalfundmarketvalue);
															System.out.println("totalfundmarketvalue---->"+totalfundmarketvalue);
															intent.putExtra(
																	"CountFund",
																	countfund);
															System.out.println("countfund---->"+countfund);
															intent.putExtra("certificateno", certificateno);
															System.out.println("certificateno---->"+certificateno);
															intent.putExtra("depositacct", depositacct);
															System.out.println("depositacct---->"+depositacct);
															intent.putExtra("certificatetype", certificatetype);
															System.out.println("countfund---->"+certificatetype);
															intent.putExtra("Moneyaccount", moneyaccount);
															System.out.println("countfund---->"+moneyaccount);
															intent.putExtra("Custno", custno);
															System.out.println("custno---->"+custno);
															intent.putExtra("channelid", channelid);
															System.out.println("channelid---->"+channelid);
															intent.putExtra("Flag", "0");
															startActivity(intent);
															App.getContext().setUserName(username);
															App.getContext().setIdCard(idcard);
															App.getContext().setSessionid(sessionid);
															App.getContext().setDepositacctName(displayName);
															SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
															Editor editor = sPreferences.edit();
															editor.putString("UserName", username);
															editor.putString("IDCard", idcard);
															editor.putString("password", password);
															editor.putString("Mobile", moblie);
															editor.putString("DepositacctName", displayName);
															editor.putString("sessionid", sessionid);
															editor.commit();
														
															activity.finish();
															activity.disMissDialog();
														} else{
															if(flag==null){
																if (user==null) {
																	
																	Intent intent=new Intent(activity,MyActivityGroup.class);	
																	intent.putExtra("iDCard", idcard);
																	intent.putExtra("DisplayName", displayName);
																	intent.putExtra("UserName", username);
																	intent.putExtra("sessionid", sessionid);
																	intent.putExtra("Mobile", moblie);
																	intent.putExtra(
																			"CustomRiskLevel",
																			risklevel);
																	System.out.println("risklevel---->"+risklevel);
																	intent.putExtra(
																			"DepositacctName",
																			depositacctname);
																	System.out.println("depositacctname---->"+depositacctname);
																	intent.putExtra(
																			"TotalFundMarketValue",
																			totalfundmarketvalue);
																	System.out.println("totalfundmarketvalue---->"+totalfundmarketvalue);
																	intent.putExtra(
																			"CountFund",
																			countfund);
																	System.out.println("countfund---->"+countfund);
																	intent.putExtra("certificateno", certificateno);
																	System.out.println("certificateno---->"+certificateno);
																	intent.putExtra("depositacct", depositacct);
																	System.out.println("depositacct---->"+depositacct);
																	intent.putExtra("certificatetype", certificatetype);
																	System.out.println("countfund---->"+certificatetype);
																	intent.putExtra("Moneyaccount", moneyaccount);
																	System.out.println("countfund---->"+moneyaccount);
																	intent.putExtra("Custno", custno);
																	System.out.println("custno---->"+custno);
																	intent.putExtra("channelid", channelid);
																	System.out.println("channelid---->"+channelid);
																	intent.putExtra("Flag", "5");
																	startActivity(intent);
																	activity.finish();
																	activity.disMissDialog();
																} else {
																	
																	Intent intent=new Intent(activity, GoodsPassActivity.class);
																	startActivity(intent);
																	activity.finish();
																	activity.disMissDialog();
																	
																}
																App.getContext().setUserName(username);
																App.getContext().setIdCard(idcard);
																App.getContext().setSessionid(sessionid);
																App.getContext().setDepositacctName(displayName);
																SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
																Editor editor = sPreferences.edit();
																editor.putString("UserName", username);
																editor.putString("IDCard", idcard);
																editor.putString("password", password);
																editor.putString("Mobile", moblie);
																editor.putString("DepositacctName", displayName);
																editor.putString("sessionid", sessionid);
																editor.commit();
																activity.finish();
																activity.disMissDialog();
															} else{
																
																Intent intent=new Intent(activity,FundSelectActivity.class);
																intent.putExtra("sessionId", sessionid);
																startActivity(intent);
																App.getContext().setUserName(username);
																App.getContext().setIdCard(idcard);
																App.getContext().setSessionid(sessionid);
																App.getContext().setDepositacctName(displayName);
																SharedPreferences sPreferences = activity.getSharedPreferences("Setting",activity.MODE_PRIVATE);
																Editor editor = sPreferences.edit();
																editor.putString("UserName", username);
																editor.putString("IDCard", idcard);
																editor.putString("password", password);
																editor.putString("Mobile", moblie);
																editor.putString("DepositacctName", displayName);
																editor.putString("sessionid", sessionid);
																editor.commit();
																activity.finish();
																activity.disMissDialog();
															}
															
															
														}
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
									try {
										//	tInputStringStream.close();
									} catch (Exception e) {
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
				

//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
}
