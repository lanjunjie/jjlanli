package com.myfp.myfund.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class WriteInforActivity extends BaseActivity {
	EditText et_writeifor_mail, et_writeifor_password,
			et_writeifor_againpassword, et_writeifor_servecode;
	TextView tv_writeinfor_kh, tv_writeinfor_tzr, tv_writeinfor_fx,
			tv_writeinfor_yhzz;
	ImageView img_writeifor_agreement;
	private String RealName,IDCard,PhoneNum;
	int num = 1;
	ByteArrayInputStream tInputStringStream = null;
	public static WriteInforActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_writeinfor);
		instance = this;
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("填写信息");

		et_writeifor_mail = (EditText) findViewById(R.id.et_writeifor_mail);
		et_writeifor_password = (EditText) findViewById(R.id.et_writeifor_password);
		et_writeifor_againpassword = (EditText) findViewById(R.id.et_writeifor_againpassword);
		et_writeifor_servecode = (EditText) findViewById(R.id.et_writeifor_servecode);
		img_writeifor_agreement = (ImageView) findViewById(R.id.img_writeinfor_agreement);
		
		tv_writeinfor_kh = (TextView) findViewAddListener(R.id.tv_writeinfor_kh);
		tv_writeinfor_tzr = (TextView) findViewAddListener(R.id.tv_writeinfor_tzr);
		tv_writeinfor_fx = (TextView) findViewAddListener(R.id.tv_writeinfor_fx);
		tv_writeinfor_yhzz = (TextView) findViewAddListener(R.id.tv_writeinfor_yhzz);

		findViewAddListener(R.id.bt_writeifor_nextstep);
		img_writeifor_agreement = (ImageView) findViewById(R.id.img_writeinfor_agreement);
		img_writeifor_agreement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (num == 1) {
					img_writeifor_agreement
							.setImageResource(R.drawable.bg_radio_noselect);
					num = 0;
				} else if (num == 0) {
					img_writeifor_agreement
							.setImageResource(R.drawable.bg_radio_selected);
					num = 1;
				}
			}
		});
		// findViewAddListener(R.id.img_writeinfor_agreement);
//		tv_writeinfor_kh.setText(Html.fromHtml("<u>《开户协议》</u>"));
//		tv_writeinfor_tzr.setText(Html.fromHtml("<u>《投资人权益须知》</u>"));
//		tv_writeinfor_fx.setText(Html.fromHtml("<u>《风险提示》</u>"));
//		tv_writeinfor_yhzz.setText(Html.fromHtml("<u>《银行自动转账授权书》</u>"));
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_writeifor_nextstep:
			nextStep();
			break;
		case R.id.tv_writeinfor_kh:
			Intent intent = new Intent(WriteInforActivity.this,AgreementActivity.class);
			intent.putExtra("URL", "https://apptrade.myfund.com:8383/appweb/page/openAcct/openAcctAgreement1.htm");
			startActivity(intent);
			break;
		case R.id.tv_writeinfor_tzr:
			Intent intent1 = new Intent(WriteInforActivity.this,AgreementActivity.class);
			intent1.putExtra("URL", "https://apptrade.myfund.com:8383/appweb/page/openAcct/openAcctAgreement2.htm");
			startActivity(intent1);
			
			break;
		case R.id.tv_writeinfor_fx:
			Intent intent2 = new Intent(WriteInforActivity.this,AgreementActivity.class);
			intent2.putExtra("URL", "https://apptrade.myfund.com:8383/appweb/page/openAcct/openAcctAgreement3.htm");
			startActivity(intent2);
			
			break;
		case R.id.tv_writeinfor_yhzz:
			Intent intent3 = new Intent(WriteInforActivity.this,AgreementActivity.class);
			intent3.putExtra("URL", "https://apptrade.myfund.com:8383/appweb/page/openAcct/openAcctAgreement4.htm");
			startActivity(intent3);
			
			break;

		default:
			break;
		}
	}

	void nextStep() {
		Intent intent =getIntent();
		Bundle bundle =intent.getExtras();
		 RealName =bundle.getString("RealName");
		 IDCard =bundle.getString("IDCard");
		 PhoneNum =bundle.getString("PhoneNum");
									if (!(et_writeifor_mail.getText()
											.toString().length() == 0)) {
										if (et_writeifor_mail
												.getText()
												.toString()
												.matches(
														"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")) {
											if (!(et_writeifor_password
													.getText().toString()
													.length() == 0)) {
												if (et_writeifor_password
														.getText().toString()
														.length() > 8
														|| et_writeifor_password
																.getText()
																.toString()
																.length() < 6) {
													showToast("密码长度为6~8位");
													return;
												} else {
													if (!(et_writeifor_againpassword
															.getText()
															.toString()
															.length() == 0)) {
														if (et_writeifor_againpassword
																.getText()
																.toString()
																.length() > 8
																|| et_writeifor_againpassword
																		.getText()
																		.toString()
																		.length() < 6) {
															showToast("确认密码长度为6~8位");
															return;
														} else {
															if (et_writeifor_password
																	.getText()
																	.toString()
																	.equals(et_writeifor_againpassword
																			.getText()
																			.toString())) {
																if (!(et_writeifor_servecode
																		.getText()
																		.toString()
																		.length() == 0)) {
																	if (num == 1) {

																		showProgressDialog("正在确认...");

																		RequestParams params = new RequestParams(
																				this);

																		params.put(
																				"certificateno",
																				IDCard);
																		params.put(
																				"depositacct",
																				"");
																		params.put(
																				"certificatetype",
																				"0");

																		// params.put(RequestParams.MOBILE,
																		// username);

																		execApi(ApiType.GET_OPENACCOUNTSTATUS,
																				params);

																	} else {
																		showToast("请认真阅读协议");
																		return;
																	}
																} else {
																	showToast("服务代码为空");
																	return;
																}

															} else {
																showToast("两次密码输入不一致");
																return;
															}
														}

													} else {
														showToast("确认密码为空");
														return;
													}
												}
											} else {
												showToast("密码为空");
												return;
											}
										} else {
											showToast("邮箱地址格式不正确");
											return;
										}
									} else {
										showToast("邮箱地址为空");
										return;
									}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_OPENACCOUNTSTATUS) {

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
											if (jsonObj.getString("openstat")
													.equals("w")) {
												disMissDialog();
												showToast(jsonObj
														.getString("backmsg"));
												Intent intent = new Intent(
														WriteInforActivity.this,
														BindingBankActivity.class);
												Bundle bundle = new Bundle();
												bundle.putString("RealName",
														RealName);
												bundle.putString("IDCard",
														IDCard);
												bundle.putString("PhoneNum",
														PhoneNum);
												bundle.putString("Email",
														et_writeifor_mail
																.getText()
																.toString());
												bundle.putString("LoginPass",
														et_writeifor_againpassword
																.getText()
																.toString());
												bundle.putString("ServeCode",
														et_writeifor_servecode
																.getText()
																.toString());
												intent.putExtras(bundle);
												startActivity(intent);
											} else {
												disMissDialog();
												showToast(jsonObj
														.getString("backmsg"));
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

			}

		}
	}

}
