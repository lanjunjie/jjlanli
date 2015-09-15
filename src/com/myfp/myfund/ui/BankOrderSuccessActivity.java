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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;

public class BankOrderSuccessActivity extends BaseActivity {
	Bundle bundle;
	private DealSearchResult res;
	TextView tv_bankorder_applycode, tv_bankorder_fundcode,
			tv_bankorder_fundname, tv_bankorder_jine, tv_bankorder_jineDX,
			tv_bankorder_idcardnum,tv_bankorder_timing;
	Button bt_confirmdeal;
	private String jinE, idcardshow;
	ByteArrayInputStream tInputStringStream = null;
	private String sessionId;
	private int recLen = 120;
	Timer timer = new Timer();

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_bankordersuccess);
		bundle = getIntent().getExtras();
		jinE = bundle.getString("JinE");
		idcardshow = bundle.getString("Certificateno");
		sessionId = bundle.getString("sessionId");
		res = (DealSearchResult) bundle.getSerializable("DealSearchResult");
		
	}
	
	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() {		// UI thread
				@Override
				public void run() {
					recLen--;
					tv_bankorder_timing.setText("请在"+recLen+"秒内点击上方的按钮");
					if(recLen < 0){
						timer.cancel();
						tv_bankorder_timing.setText("付款超时，请点击返回，重新购买");
						bt_confirmdeal.setText("返回");
						bt_confirmdeal.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								
								Intent intent = new Intent(BankOrderSuccessActivity.this,DealBuyActivity.class);
								//intent.putExtra("IDCard",bundle.getString("IDCard"));
								//intent.putExtra("PassWord",bundle.getString("PassWord"));
								intent.putExtra("sessionId", sessionId);
								startActivity(intent);
								finish();
								DealBuyActivity.instance.finish();
								DealApplyActivity.instance.finish();
								DealConfirmActivity.instance.finish();
							}
						});
					}
				}
			});
		}
	};
	

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("您的基金买入下单成功");
		tv_bankorder_applycode = (TextView) findViewById(R.id.tv_bankorder_applycode);
		tv_bankorder_fundcode = (TextView) findViewById(R.id.tv_bankorder_fundCode);
		tv_bankorder_fundname = (TextView) findViewById(R.id.tv_bankorder_fundName);
		tv_bankorder_jine = (TextView) findViewById(R.id.tv_bankorder_dealjine);
		tv_bankorder_jineDX = (TextView) findViewById(R.id.tv_bankorder_jineDX);
		tv_bankorder_idcardnum = (TextView) findViewById(R.id.tv_bankorder_idCardNum);
		tv_bankorder_timing = (TextView)findViewById(R.id.tv_bankorder_timing);
		bt_confirmdeal = (Button)findViewById(R.id.bt_bankorder_pay);
		findViewAddListener(R.id.bt_bankorder_pay);
		
		timer.schedule(task, 1000, 1000);

		tv_bankorder_applycode.setText(bundle.getString("appsheetserialno"));
		tv_bankorder_fundcode.setText("[" + res.getFundCode() + "]");
		tv_bankorder_fundname.setText(res.getFundName());
		tv_bankorder_jine.setText(bundle.getString("JinE"));
		tv_bankorder_jineDX.setText(new CnUpperCaser(bundle.getString("JinE")).getCnString()+"(元)");
		if (idcardshow.length() == 18) {
			tv_bankorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(11, 16), "*****"));
		} else if (idcardshow.length() == 15) {
			tv_bankorder_idcardnum.setText(idcardshow.replace(
					idcardshow.subSequence(8, 13), "*****"));
		}

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_bankorder_pay:
			bankPay();
			break;

		default:
			break;
		}
	}

	void bankPay() {
		
		showProgressDialog("正在确认...");

		RequestParams params = new RequestParams(this);
		params.put("sessionId", bundle.getString("sessionId"));
		params.put("certIdLength", idcardshow.length());
		params.put("applicationamount", jinE.trim());
		params.put("channelid", res.getChannelid().trim());
		params.put("fundtype", res.getFundType());
		params.put("fundstatus", res.getStatus());
		params.put("tano", res.getTano());
		params.put("moneyaccount", res.getMoneyaccount());
		params.put("liqdate", bundle.getString("liqdate"));
		params.put("fundcode", res.getFundCode());
		params.put("appsheetserialno",bundle.getString("appsheetserialno"));
		params.put("fundname",res.getFundName());
		


		execApi(ApiType.GET_BANKPAYTWO, params);
	}
	
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_BANKPAYTWO) {

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
											if (jsonObj.getString("code").equals("0000")) {
												
												disMissDialog();
												showToast("支付成功！！");
												Intent intent = new Intent(BankOrderSuccessActivity.this,ApplyAcceptActivity.class);
												intent.putExtras(bundle);
												startActivity(intent);
												finish();

											} else {
												disMissDialog();
												showToast("支付失败！！");
												Intent intent = new Intent(BankOrderSuccessActivity.this,DealBuyActivity.class);
												intent.putExtra("sessionId", bundle.getString("sessionId"));
												startActivity(intent);
												finish();
												DealBuyActivity.instance.finish();
												DealApplyActivity.instance.finish();
												DealConfirmActivity.instance.finish();
												
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

}
