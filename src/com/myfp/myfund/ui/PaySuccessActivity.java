package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import u.aly.ca;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.renderscript.Element;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.hpl.sparta.Text;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.R.integer;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class PaySuccessActivity extends BaseActivity{

	private TextView tex_num;
	private String ch;
	private int wh;
	private String gearing;
	private String deadline;
	private TextView tex_gearing;
	private TextView tex_deadlineone;
	private TextView tex_deadline;
	private CheckBox check_consent;
	private String idcard;
	ByteArrayInputStream tInputStringStream = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_financing_case);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		idcard = preferences.getString("IDCard", null);
	}

	@Override
	protected void initViews() {
		setTitle("我的资产-基金配资");
		findViewAddListener(R.id.tex_stake);
		tex_num = (TextView) findViewById(R.id.tex_num);
		findViewAddListener(R.id.tex_gearingone);
		tex_gearing = (TextView) findViewById(R.id.tex_gearing);
		findViewAddListener(R.id.tex_deadlineone);
		tex_deadline = (TextView) findViewById(R.id.tex_deadline);
		findViewAddListener(R.id.tex_interest);
		findViewAddListener(R.id.tex_service_charge);
		check_consent = (CheckBox) findViewById(R.id.check_consent);
		check_consent.setChecked(true);
		findViewAddListener(R.id.but_myfutures);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tex_stake:
			openDialog();
			break;
		case R.id.tex_gearingone:
			pryDialog();
			break;
		case R.id.tex_deadlineone:
			timeDialog();
			break;
		case R.id.but_myfutures:
			if (!checkval(1)) {
				return;
			}else {
				RequestParams params=new RequestParams(this);
				if (ch!=null) {
					
					if (ch.equals("0")) {
						params.put("guaranteemon", "10000");
					}else if (ch.equals("1")) {
						params.put("guaranteemon", "20000");
					}else if (ch.equals("2")) {
						params.put("guaranteemon", "30000");
					}else if (ch.equals("3")) {
						params.put("guaranteemon", "40000");
					}else if (ch.equals("4")) {
						params.put("guaranteemon", "50000");
					}
				}else {
					params.put("guaranteemon", "10000");
				}
				if (gearing!=null) {
					if (gearing.equals("0")) {
						params.put("ratio", "1:1");
					}else if (gearing.equals("1")) {
						params.put("ratio", "1:2");
					}else if (gearing.equals("2")) {
						params.put("ratio", "1:3");
					}
					
				}else {
					params.put("ratio", "1:1");
				}
				if (deadline!=null) {
					
					if (deadline.equals("0")) {
						params.put("term", "91");
					}else if (deadline.equals("1")) {
						params.put("term", "182");
					}
				}else {
					params.put("term", "91");
				}
				params.put("idcard", idcard);
				execApi(ApiType.GET_ADDFUTURES, params);
			}
			
			break;
		default:
			break;
		}
	}

	private boolean checkval(int typ){
		if (typ==1) {
			if (!check_consent.isChecked()) {
				showToast("请先阅读并同意相关服务条款!");
				return false;
			}
		}
		return true;
	}
	@SuppressWarnings("openDialog")  
    public void openDialog() {  
		 final String items[] = { "1万", "2万", "3万", "4万","5万" };  
		  
	        new AlertDialog.Builder(this).setTitle("请选择投资本金")  
	                .setItems(items, new DialogInterface.OnClickListener() {  

						@Override  
	                    public void onClick(DialogInterface dialog, int which) {  
	                        wh = which;
	                        ch = Integer.toString(wh);
	                        System.out.println("你以经选中了open："+which);
	                        System.err.println("以选择open："+ch);
	                        if (wh==0) {
	                			tex_num.setText("1万");
	                		}else if (wh==1) {
	                			tex_num.setText("2万");
	                		}else if (wh==2) {
	                			tex_num.setText("3万");
	                		}else if (wh==3) {
	                			tex_num.setText("4万");
	                		}else if (wh==4) {
	                			tex_num.setText("5万");
	                		}
	                        
	                    }  
	                }).show();   
    }  
	@SuppressWarnings("pryDialog") 
	public void pryDialog(){
		final String pay[]={"1:1","1:2","1:3"};
		 new AlertDialog.Builder(this).setTitle("请选择杠杆比例") 
		 .setItems(pay, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				 int pay = which;
				gearing = Integer.toString(pay);
				System.out.println("你以经选中了pay："+which);
                System.err.println("以选择pay："+ch);
				if (pay==0) {
					tex_gearing.setText("1:1");
				}else if (pay==1) {
					tex_gearing.setText("1:2");
				}else if (pay==2) {
					tex_gearing.setText("1:3");
				}
				
			}
		}).show();
	}
	@SuppressWarnings("timeDialog")
	public void timeDialog(){
		final String time[]={"91天","182天"};
		new AlertDialog.Builder(this).setTitle("请选择期限")
		.setItems(time, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int time = which;
				deadline = Integer.toString(time);
				 System.out.println("你以经选中了time："+which);
                 System.err.println("以选择time："+ch);
				if (time==0) {
					tex_deadline.setText("91天");
				}else if (time==1) {
					tex_deadline.setText("182天");
				}
			}
		}).show();
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			return;
		}
		if (api == ApiType.GET_ADDFUTURES) {

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
									if (xmlReturn.equals("true")) {
										showToastLong("您以成功配资");
									}else {
										showToastLong("配资失败");
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
			disMissDialog();
		}
	}

}
