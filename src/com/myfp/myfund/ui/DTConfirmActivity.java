package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DTSearchResult;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.ui.DTApplyActivity.BankSpinnerSelectedListener;
import com.myfp.myfund.utils.CnUpperCaser;

public class DTConfirmActivity extends BaseActivity {
	Bundle bundle;
	private DTSearchResult res;
	TextView tv_dtconfirm_agreement, tv_dtconfirm_fundcode,
			tv_dtconfirm_fundname, tv_dtconfirm_bankcardtype,
			tv_dtconfirm_firstpaydate, tv_dtconfirm_dtjine,
			tv_dtconfirm_jineDX, tv_dtconfirm_cycle, tv_dtconfirm_idCard,
			tv_dtconfirm_idCardNum,tv_dtconfirm_timing;

	Button bt_confirmdt;
	private String firstpaydate, agreementCode, idcardshow;
	ByteArrayInputStream tInputStringStream = null;
	public static DTConfirmActivity instance = null;
	private int recLen = 120;
	Timer timer = new Timer();
	private String sessionId;


	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dtconfirm);
		instance = this;
		bundle = getIntent().getExtras();
		res = (DTSearchResult) bundle.getSerializable("DTSearchResult");
	//	passwd = bundle.getString("passwd");
		idcardshow = bundle.getString("Certificateno");
		sessionId = bundle.getString("sessionId");
		JSONObject obj;
		try {
			obj = new JSONObject(bundle.getString("SignInfo"));
			firstpaydate = obj.getString("firstinvestdate");
			agreementCode = obj.getString("saveplanno");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() {		// UI thread
				@Override
				public void run() {
					recLen--;
					tv_dtconfirm_timing.setText("请在"+recLen+"秒内点击上方的按钮");
					if(recLen < 0){
						timer.cancel();
						tv_dtconfirm_timing.setText("您没有在限定的时间内转去银行签订协议，为了安全起见该笔协议已被禁止。");
						bt_confirmdt.setText("返回");
						bt_confirmdt.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								
								Intent intent = new Intent(DTConfirmActivity.this,DealDTActivity.class);
								//intent.putExtra("IDCard",bundle.getString("IDCard"));
								///intent.putExtra("PassWord",bundle.getString("PassWord"));
								intent.putExtra("sessionId", bundle.getString("sessionId"));
								intent.putExtra("CustomRiskLevel",bundle.getString("CustomRiskLevel"));
								startActivity(intent);
								finish();
								DealDTActivity.instance.finish();
								DTApplyActivity.instance.finish();
							}
						});
					}
				}
			});
		}
	};
	private String passwd;

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("确认信息");
		tv_dtconfirm_agreement = (TextView) findViewById(R.id.tv_dtconfirm_agreement);
		tv_dtconfirm_fundcode = (TextView) findViewById(R.id.tv_dtconfirm_fundCode);
		tv_dtconfirm_fundname = (TextView) findViewById(R.id.tv_dtconfirm_fundName);
		tv_dtconfirm_bankcardtype = (TextView) findViewById(R.id.tv_dtconfirm_bankcardtype);
		tv_dtconfirm_firstpaydate = (TextView) findViewById(R.id.tv_dtconfirm_firstpaydate);
		tv_dtconfirm_dtjine = (TextView) findViewById(R.id.tv_dtconfirm_dtjine);
		tv_dtconfirm_jineDX = (TextView) findViewById(R.id.tv_dtconfirm_jineDX);
		tv_dtconfirm_cycle = (TextView) findViewById(R.id.tv_dtconfirm_cycle);
		tv_dtconfirm_idCard = (TextView) findViewById(R.id.tv_dtconfirm_idCard);
		tv_dtconfirm_idCardNum = (TextView) findViewById(R.id.tv_dtconfirm_idCardNum);
		tv_dtconfirm_timing = (TextView) findViewById(R.id.tv_dtconfirm_timing);
		bt_confirmdt =(Button)findViewById(R.id.bt_dtconfirm_dt);
		findViewAddListener(R.id.bt_dtconfirm_dt);
		
		timer.schedule(task, 1000, 1000);
		
		tv_dtconfirm_agreement.setText(agreementCode);
		tv_dtconfirm_fundcode.setText("[" + res.getFundcode() + "]");
		tv_dtconfirm_fundname.setText(res.getFundname());
		tv_dtconfirm_bankcardtype.setText(bundle.getString("BankCardCodeShow"));
		tv_dtconfirm_firstpaydate.setText(firstpaydate);
		tv_dtconfirm_dtjine.setText(bundle.getString("JinE")+"(元)");
		
		tv_dtconfirm_jineDX.setText(new CnUpperCaser(bundle.getString("JinE")).getCnString()+"(元)");
		tv_dtconfirm_cycle.setText(bundle.getString("CycleValue"));
		tv_dtconfirm_idCard.setText("身份证");
		if (idcardshow.length() == 18) {
			tv_dtconfirm_idCardNum.setText(idcardshow.replace(
					idcardshow.subSequence(11, 16), "*****"));
		} else if (idcardshow.length() == 15) {
			tv_dtconfirm_idCardNum.setText(idcardshow.replace(
					idcardshow.subSequence(8, 13), "*****"));
		}

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_dtconfirm_dt:
			showProgressDialog("正在加载");
			
			RequestParams params = new RequestParams(this);
			//params.put("id", bundle.getString("IDCard"));
//			params.put("passwd", bundle.getString("PassWord"));
			params.put("sessionId", sessionId);
			params.put("certIdLength", idcardshow.length());
			params.put("fundcode", res.getFundcode());
			params.put("saveplanno",agreementCode);
			params.put("moneyaccount",bundle.getString("MoneyAccount"));
			params.put("applicationamount", bundle.getString("JinE"));
			params.put("fundname",res.getFundname());
			params.put("signInfo",java.net.URLEncoder.encode(bundle.getString("SignInfo").toString().trim()));
			
			execApi(ApiType.GET_DTPAYTWO, params);

			

			break;

		default:
			break;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {

			if (api == ApiType.GET_DTPAYTWO) {

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
											JSONObject obj = new JSONObject(
													xmlReturn);
											if (obj.getString("code").equals("0000")) {
												//showToast(obj.getString("msg"));
												
												Intent intent = new Intent(
														DTConfirmActivity.this,
														DTSignAcceptActivity.class);
												intent.putExtras(bundle);
												startActivity(intent);
											} else {
												
												showToast("签约失败，请重新签约");

												Intent intent = new Intent(DTConfirmActivity.this,DealDTActivity.class);
												//intent.putExtra("IDCard",bundle.getString("IDCard"));
												//intent.putExtra("PassWord",bundle.getString("PassWord"));
												intent.putExtra("sessionId", bundle.getString("sessionId"));
												intent.putExtra("CustomRiskLevel",bundle.getString("CustomRiskLevel"));
												startActivity(intent);
												finish();
												DealDTActivity.instance.finish();
												DTApplyActivity.instance.finish();
												
												return;
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
				disMissDialog();
			}
		}
	}

}
