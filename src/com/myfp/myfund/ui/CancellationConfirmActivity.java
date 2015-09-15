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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CancellationResult;
import com.myfp.myfund.api.beans.RedeemSearchResult;

public class CancellationConfirmActivity extends BaseActivity {
	Bundle bundle;
	private CancellationResult res;
	TextView tv_cancellationconfirm_fundcode, tv_cancellationconfirm_fundname,
			tv_cancellationconfirm_oldapplycode, tv_cancellationconfirm_fene,
			tv_cancellationconfirm_jine, tv_cancellationconfirm_yewu;
	Button bt_confirmredeem;
	// private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	public static CancellationConfirmActivity instance = null;
	private String sessionId;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_cancellationconfirm);
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		res = (CancellationResult) bundle.getSerializable("CancellationResult");
		// encodeIdCard = bundle.getString("IDCard");
		// encodePassWord = bundle.getString("PassWord");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金交易撤单");
		tv_cancellationconfirm_fundcode = (TextView) findViewById(R.id.tv_cancellationconfirm_fundCode);
		tv_cancellationconfirm_fundname = (TextView) findViewById(R.id.tv_cancellationconfirm_fundName);
		tv_cancellationconfirm_oldapplycode = (TextView) findViewById(R.id.tv_cancellationconfirm_oldapplycode);
		tv_cancellationconfirm_fene = (TextView) findViewById(R.id.tv_cancellationconfirm_fene);
		tv_cancellationconfirm_jine = (TextView) findViewById(R.id.tv_cancellationconfirm_jine);
		tv_cancellationconfirm_yewu = (TextView) findViewById(R.id.tv_cancellationconfirm_yewu);
		findViewAddListener(R.id.bt_applycancellation);
		tv_cancellationconfirm_oldapplycode.setText(res.getAppsheetserialno());
		tv_cancellationconfirm_fundcode.setText("[" + res.getFundcode() + "]");
		tv_cancellationconfirm_fundname.setText(res.getFundname());
		tv_cancellationconfirm_fene.setText(res.getApplicationvol());
		tv_cancellationconfirm_jine.setText(res.getApplicationamount());
		tv_cancellationconfirm_yewu.setText(bundle.getString("YeWuType"));

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applycancellation:
			applyCancellation();
			break;

		default:
			break;
		}
	}

	void applyCancellation() {

		showProgressDialog("正在确认...");

		RequestParams params = new RequestParams(this);
		//params.put("id", bundle.getString("IDCard"));
		params.put("sessionId", sessionId);
		//params.put("passwd", bundle.getString("PassWord"));
		params.put("originalappsheetno", res.getAppsheetserialno());

		// params.put(RequestParams.MOBILE, username);

		execApi(ApiType.GET_CANCELLATIONTWO, params);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_CANCELLATIONTWO) {

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
										if (xmlReturn
												.contains("appsheetserialno")) {

											try {
												JSONObject jsonObj = new JSONObject(xmlReturn);

												// 赎回申请单编号
												String applyCode = jsonObj
														.getString("appsheetserialno");
												// System.out.println("ddddddddddddddddddddddd"+applyCode);

												if (!(applyCode == null)) {

													disMissDialog();
													//showToast("撤单成功！！");
													bundle.putString(
															"appsheetserialno",
															applyCode);
													Intent intent = new Intent(
															CancellationConfirmActivity.this,
															CancellationAcceptActivity.class);
													intent.putExtras(bundle);
													startActivity(intent);

												}else {
													disMissDialog();
													showToast("撤单失败！！");
													return;
												}

											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										} else {
											disMissDialog();
											showToast("撤单失败！！");
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

		}
	}

}
