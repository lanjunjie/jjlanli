package com.myfp.myfund.ui;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.utils.ManyAuDocking;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyActivityGroup extends ActivityGroup {
	private LinearLayout bodyView;
	private LinearLayout hom_a;
	private LinearLayout info_a;
	private LinearLayout tadre_a;
	private LinearLayout my_a;
	private int flag=0;
	private String encodePassWord;
	private ImageView imag_home;
	private ImageView imag_info;
	private ImageView imag_tadre;
	private ImageView imag_my;
	private long lastBack;
	public static MyMeansActivity activity;
	private String username;
	public static MyActivityGroup instance = null;
	public static ManyAuDocking aDocking;
	private String sessionid;
	private SharedPreferences sPreferences;
	ByteArrayInputStream tInputStringStream;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		instance=this;
		Thread();
		//手机Mac地址
		String mac = mac();
		System.out.println("mac------>"+mac);
		username =App.getContext().getUserName();
		System.out.println("usernameusernameusername"+username);
		encodePassWord=App.getContext().getEncodePassWord();
		
		Intent intent = getIntent();
		 sessionid = intent.getStringExtra("sessionid");
		String star = intent.getStringExtra("Flag");
		if (star!=null) {
			flag = Integer.valueOf(star);
		}
		initMainView();
		showView(flag);
		setonClik(flag);
	}

	public int setonClik(int i) {
		hom_a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Thread();
				flag=0;
				showView(flag);
			}
		});
		info_a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Thread();
				flag=1;
				showView(flag);
			}
		});
		tadre_a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Thread();
					flag=2;
					showView(flag);	
			}
		});
		
		my_a.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				username =App.getContext().getUserName();
				encodePassWord=App.getContext().getEncodePassWord();
				System.out.println("encodePassWord"+encodePassWord+"username"+username);
				Thread();
				if (encodePassWord!=null||username!=null) {
					flag=5;
					showView(flag);
					
				}else {
					flag=4;
					showView(flag);
				}
				
			}
		});
		return i;

	}

	public void initMainView(){
		bodyView = (LinearLayout) findViewById(R.id.body);
		hom_a = (LinearLayout) findViewById(R.id.home_a);
		imag_home = (ImageView) findViewById(R.id.imag_home);
		imag_info = (ImageView) findViewById(R.id.imag_info);
		imag_tadre = (ImageView) findViewById(R.id.imag_tadre);
		imag_my = (ImageView) findViewById(R.id.imag_my);
		info_a = (LinearLayout) findViewById(R.id.info_a);
		tadre_a = (LinearLayout) findViewById(R.id.tadre_a);
		my_a = (LinearLayout) findViewById(R.id.my_a);
	}

	public void showView(int flag){
		switch (flag) {
		case 0:
			bodyView.removeAllViews();
			Intent homedimen=new Intent(MyActivityGroup.this, MyfundHomeActivity.class);
			homedimen.putExtra("sessionId", sessionid);
			View v = getLocalActivityManager().startActivity("home_a",homedimen).getDecorView();
			imag_home.setBackgroundResource(R.drawable.home_b);
			imag_info.setBackgroundResource(R.drawable.info_a);
			imag_tadre.setBackgroundResource(R.drawable.trade_a);
			imag_my.setBackgroundResource(R.drawable.my_a);
			bodyView.addView(v);
			break;
		case 1:
			bodyView.removeAllViews();
			Intent tradeintent = new Intent(MyActivityGroup.this,RcommendmainActivity.class);
			tradeintent.putExtra("sessionId", getIntent().getStringExtra("sessionid"));
			View tradeView = getLocalActivityManager().startActivity("tadre_a",tradeintent).getDecorView();
			bodyView.addView(tradeView);
			imag_home.setBackgroundResource(R.drawable.home_a);
			imag_info.setBackgroundResource(R.drawable.info_b);
			imag_tadre.setBackgroundResource(R.drawable.trade_a);
			imag_my.setBackgroundResource(R.drawable.my_a);
			break;
		case 2:
			bodyView.removeAllViews();
			bodyView.addView(getLocalActivityManager().startActivity("info_b", 
					new Intent(MyActivityGroup.this,FundNewsActivity.class)).getDecorView());
			imag_home.setBackgroundResource(R.drawable.home_a);
			imag_info.setBackgroundResource(R.drawable.info_a);
			imag_tadre.setBackgroundResource(R.drawable.trade_b);
			imag_my.setBackgroundResource(R.drawable.my_a);
			
			break;
		case 3:
			//现在没用到
			bodyView.removeAllViews();
			bodyView.addView(getLocalActivityManager().startActivity("tadre_a", 
					new Intent(MyActivityGroup.this,MyMeansActivity.class)).getDecorView());
			imag_home.setBackgroundResource(R.drawable.home_a);
			imag_info.setBackgroundResource(R.drawable.info_a);
			imag_tadre.setBackgroundResource(R.drawable.trade_b);
			imag_my.setBackgroundResource(R.drawable.my_a);

			break;
		case 4:
			Intent intent=new Intent(this, MyMeansActivity.class);
			startActivity(intent);
			//bodyView.removeAllViews();
			//bodyView.addView(getLocalActivityManager().startActivity("my_a", 
			//		new Intent(MyActivityGroup.this,MyMeansActivity.class)).getDecorView());
			//imag_home.setBackgroundResource(R.drawable.home_a);
			//imag_info.setBackgroundResource(R.drawable.info_a);
			//imag_tadre.setBackgroundResource(R.drawable.trade_a);
			//imag_my.setBackgroundResource(R.drawable.my_b);
			break;
		case 5:
	
			bodyView.removeAllViews();
			Intent myIntent=new Intent(MyActivityGroup.this, PropertyActivity.class);
			myIntent.putExtra("IDCard", getIntent().getStringExtra("IDCard"));
			myIntent.putExtra("EncodePassWord", getIntent().getStringExtra("EncodePassWord"));
			myIntent.putExtra("CustomRiskLevel", getIntent().getStringExtra("CustomRiskLevel"));
			myIntent.putExtra("DepositacctName", getIntent().getStringExtra("DepositacctName"));
			myIntent.putExtra("TotalFundMarketValue", getIntent().getStringExtra("TotalFundMarketValue"));
			myIntent.putExtra("CountFund", getIntent().getStringExtra("CountFund"));
			myIntent.putExtra("PassWord", getIntent().getStringExtra("PassWord"));
			myIntent.putExtra("UserName", getIntent().getStringExtra("UserName"));
			myIntent.putExtra("msg", getIntent().getStringExtra("msg"));
			myIntent.putExtra("Mobile", getIntent().getStringExtra("Mobile"));
			myIntent.putExtra("certificateno", getIntent().getStringExtra("certificateno"));
			myIntent.putExtra("depositacct", getIntent().getStringExtra("depositacct"));
			myIntent.putExtra("certificatetype", getIntent().getStringExtra("certificatetype"));
			myIntent.putExtra("Moneyaccount", getIntent().getStringExtra("Moneyaccount"));
			myIntent.putExtra("Custno", getIntent().getStringExtra("Custno"));
			myIntent.putExtra("channelid", getIntent().getStringExtra("channelid"));
			myIntent.putExtra("Fundcode", getIntent().getStringExtra("Fundcode"));
			myIntent.putExtra("sessionid", sessionid);
			
			View view = getLocalActivityManager().startActivity("my_a",myIntent).getDecorView();
			bodyView.addView(view);
			imag_home.setBackgroundResource(R.drawable.home_a);
			imag_info.setBackgroundResource(R.drawable.info_a);
			imag_tadre.setBackgroundResource(R.drawable.trade_a);
			imag_my.setBackgroundResource(R.drawable.my_b);
			
			break;

		}
		return;
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
			//editor.putString("UserList", listStr);
			editor.commit();
			finish();
			
		}
	}
	public void Thread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String idcard=App.getContext().getIdCard();
				RequestParams params=new RequestParams(activity);
				System.out.println("idcard===============>"+idcard);      
				params.put(RequestParams.iDIcard, idcard);
				RndDataApi.executeNetworkApi(ApiType.GET_STEPVERIFICATION, params, new OnDataReceivedListener() {
					
					@Override
					public void onReceiveData(ApiType api, String json) {
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
											sessionid = jsonObj.getString("sessionid");
											System.out.println("se111111111111111"+sessionid);
											App.getContext().setSessionid(sessionid);
										}catch (JSONException e) {
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
		}).start();
	}
	public String mac(){
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		 
		WifiInfo info = wifi.getConnectionInfo();
		 System.out.println("info---------->"+info);
		 String macAddress = info.getMacAddress();
		 System.out.println("macAddress=======>"+macAddress);
		return info.getMacAddress();
	}

}
