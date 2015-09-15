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
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DTManageSearchResult;

public class DTStopActivity extends BaseActivity {
	Bundle bundle;
	private DTManageSearchResult res;
	TextView tv_dtstop_agreementcode, tv_dtstop_fundcode, tv_dtstop_fundname,
			tv_dtstop_yewu, tv_dtstop_paybank, tv_dtstop_firstpaydate,
			tv_dtstop_firstpayjine, tv_dtstop_latertype;
	Button bt_confirmdeal;
	private String jinE;
	ByteArrayInputStream tInputStringStream = null;
	public static DTStopActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dtstop);
		instance = this;
		bundle = getIntent().getExtras();
		jinE = bundle.getString("JinE");
		res = (DTManageSearchResult) bundle
				.getSerializable("DTManageSearchResult");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("定期投资查询和终止");
		tv_dtstop_agreementcode = (TextView) findViewById(R.id.tv_dtstop_agreementcode);
		tv_dtstop_fundcode = (TextView) findViewById(R.id.tv_dtstop_fundcode);
		tv_dtstop_fundname = (TextView) findViewById(R.id.tv_dtstop_fundname);
		tv_dtstop_yewu = (TextView) findViewById(R.id.tv_dtstop_yewu);
		tv_dtstop_paybank = (TextView) findViewById(R.id.tv_dtstop_paybank);
		tv_dtstop_firstpaydate = (TextView) findViewById(R.id.tv_dtstop_firstpaydate);
		tv_dtstop_firstpayjine = (TextView) findViewById(R.id.tv_dtstop_firstpayjine);
		tv_dtstop_latertype = (TextView) findViewById(R.id.tv_dtstop_latertype);
		findViewAddListener(R.id.bt_dtstop_confirm);
		findViewAddListener(R.id.bt_dtstop_back);
		tv_dtstop_agreementcode.setText(res.getBuyplanno());
		tv_dtstop_fundcode.setText("[" + res.getFundcode() + "]");
		tv_dtstop_fundname.setText(res.getFundname());
		 tv_dtstop_yewu.setText("定期定额");
		 
		 tv_dtstop_paybank.setText(res.getChannelname());
			
			
		tv_dtstop_firstpaydate.setText(res.getFirstinvestdate());
		tv_dtstop_firstpayjine.setText(res.getFirstinvestamount());
		if (res.getInvestmode().equals("0")) {
			tv_dtstop_latertype.setText("按余额比例扣款");
		} else if (res.getInvestmode().equals("1")) {
			tv_dtstop_latertype.setText("按递增金额扣款");
		} else if (res.getInvestmode().equals("2")) {
			tv_dtstop_latertype.setText("按后续投资金额不变");
		}

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_dtstop_confirm:
			dtStop();

			break;
		case R.id.bt_dtstop_back:
			//bundle.putString("IDCard", bundle.getString("IDCard"));
			//bundle.putString("PassWord", bundle.getString("PassWord"));
			Intent intent = new Intent(DTStopActivity.this,DTManageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			DTManageActivity.instance.finish();
			break;

		default:
			break;
		}

	}

	void dtStop() {

		showProgressDialog("正在确认...");

		RequestParams params = new RequestParams(this);
		
		//params.put("id", bundle.getString("IDCard"));
		//params.put("passwd", bundle.getString("PassWord"));
		params.put("sessionId", bundle.getString("sessionId"));
		params.put("saveplanno", res.getBuyplanno());
		params.put("transactionaccountid", res.getTransactionaccountid());
		params.put("depositacct", res.getDepositacct());
		params.put("direction", "B");
		params.put("branchcode", res.getBranchcode());
		params.put("channelid", res.getChannelid());

		// params.put(RequestParams.MOBILE, username);

		execApi(ApiType.GET_DTSTOPTWO, params);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
			
			//bundle.putString("IDCard", bundle.getString("IDCard"));
			//bundle.putString("PassWord", bundle.getString("PassWord"));
			Intent intent = new Intent(DTStopActivity.this,DTManageActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			DTManageActivity.instance.finish();
			
			}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_DTSTOPTWO) {

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

										if (xmlReturn.contains("appsheetserialno")) {

											disMissDialog();
											showToast("定投终止成功！！");
								
												bundle.putString("IDCard", bundle.getString("IDCard"));
												bundle.putString("PassWord", bundle.getString("PassWord"));
												bundle.putString("sessionId", bundle.getString("sessionId"));
												Intent intent = new Intent(DTStopActivity.this,DTManageActivity.class);
												intent.putExtras(bundle);
												startActivity(intent);
												finish();
												DTManageActivity.instance.finish();
											

										} else {
											disMissDialog();
											showToast("定投终止失败，请重新操作");
											bundle.putString("IDCard", bundle.getString("IDCard"));
											bundle.putString("PassWord", bundle.getString("PassWord"));
											Intent intent = new Intent(DTStopActivity.this,DTManageActivity.class);
											intent.putExtras(bundle);
											startActivity(intent);
											finish();
											DTManageActivity.instance.finish();
											
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
