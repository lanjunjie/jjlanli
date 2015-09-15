package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.utils.DESEncrypt;

public class DealApplyActivity extends BaseActivity {
	private String encodeIdCard, encodePassWord, fundCode, fundName, jinE,
			fundType, fundStatus, tano, shareType, channelId, moneyAccount,
			per_min_22, per_max_22, bankCardCode, bankCardCode_show, paytype,
			certificateno;
	private DealSearchResult res;
	TextView tv_deal_fundcode, tv_deal_fundname, tv_deal_range,
			tv_dealapply_rate;
	EditText ed_deal_jine;
	RadioGroup group;
	RadioButton rb;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	private Spinner spinner;
	private ArrayAdapter<String> adapter, adapter1;
	String[] bankArray, bankCardArray, remitArrray, remitCardArray;
	public static DealApplyActivity instance = null;
	private String sessionId;
	private EditText edit_paww_text;
	private String passWord;
	private String con_per_min_22;
	private String con_per_max_22;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealapply);
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		res = (DealSearchResult) bundle.getSerializable("DealSearchResult");
		encodeIdCard = bundle.getString("IDCard");
		System.out.println("encodeIdCard=====>"+encodeIdCard);
		encodePassWord = bundle.getString("PassWord");
		System.out.println("encodePassWord------------>"+encodePassWord);
		fundCode = res.getFundCode();
		fundName = res.getFundName();
		fundType = res.getFundType();
		fundStatus = res.getStatus();
		tano = res.getTano();
		shareType = res.getShareclasses();
		channelId = res.getChannelid();
		moneyAccount = res.getMoneyaccount();
		per_min_22 = res.getFirst_per_min_22();
		con_per_min_22 = res.getCon_per_min_22();
		con_per_max_22 = res.getCon_per_max_22();
		per_max_22 = res.getFirst_per_max_22();
		System.out.println("++++++++++:" + encodeIdCard + ">>>>>>"
				+ encodePassWord + "------------" + res.getFundName());
		
//		RequestParams params1 = new RequestParams(
//				getApplicationContext());
//		params1.put("id", encodeIdCard);
//		params1.put("passwd", encodePassWord);
//		params1.put("fundcode", res.getFundCode()); 
//		params1.put("sharetype", res.getShareclasses());
//		params1.put("tano", tano);
//		params1.put("applicationamount", "100000");
//		params1.put("businesscode", "22");
//		params1.put("applicationvol", "");
//		params1.put("channelid", res.getChannelid());
//		execApi(ApiType.GET_RATEFEE, params1);
//		showProgressDialog("正在加载");

		RequestParams params = new RequestParams(this);
		params.put("sessionId", sessionId);
		//params.put("id", encodeIdCard);
		//params.put("passwd", encodePassWord);
		execApi(ApiType.GET_ONLINEBANKINFORTWO, params);
		
		showProgressDialog("正在加载");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金购买");
		// 根据ID找到RadioGroup实例
		group = (RadioGroup) this.findViewById(R.id.radioGroup);
		paytype = "1";
		// rb = (RadioButton) findViewById(R.id.rb_bank);
		// shareType = rb.getText().toString();

		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				// shareType = rb.getText().toString();
				System.out.println(radioButtonId);
			
				spinner = (Spinner) findViewById(R.id.sp_dealapply_spinner);
				showProgressDialog("正在确认");

				if (rb.getText().equals("汇款支付")) {
					paytype = "2";
					RequestParams params = new RequestParams(
							getApplicationContext());
					//params.put("id", encodeIdCard);
					params.put("sessionId", sessionId);
					//params.put("passwd", encodePassWord);
					execApi(ApiType.GET_REMITBANKINFORTWO, params);

				} else {
					paytype = "1";
					RequestParams params = new RequestParams(
							getApplicationContext());
					//params.put("id", encodeIdCard);
					params.put("sessionId", sessionId);
					//params.put("passwd", encodePassWord);
					execApi(ApiType.GET_ONLINEBANKINFORTWO, params);
				}
				// showToast("你选的是" + rb.getText());
			}
		});
		tv_deal_fundcode = (TextView) findViewById(R.id.tv_deal_fundcode);
		tv_deal_fundname = (TextView) findViewById(R.id.tv_deal_fundname);
		ed_deal_jine = (EditText) findViewById(R.id.ed_deal_jine);
		tv_deal_range = (TextView) findViewById(R.id.tv_deal_range);
		//tv_dealapply_rate = (TextView) findViewById(R.id.tv_dealapply_rate);
		spinner = (Spinner) findViewById(R.id.sp_dealapply_spinner);

		

		findViewAddListener(R.id.bt_applydeal);
		tv_deal_fundcode.setText("[" + fundCode + "]");
		tv_deal_fundname.setText(fundName);
		tv_deal_range.setText("本基金申购范围（" + per_min_22 + "~"
				+ per_max_22.substring(0, per_max_22.indexOf(".") + 3) + "元）");

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applydeal:
			bundle.putString("ShareType", shareType);
			bundle.putString("JinE", ed_deal_jine.getText().toString().trim());
			if (!(ed_deal_jine.getText().toString().trim().length() == 0)) {
				if ((Double.parseDouble(ed_deal_jine.getText().toString()
						.trim()) < Double.parseDouble(per_min_22.toString()
						.trim()))
						|| (Double.parseDouble(ed_deal_jine.getText()
								.toString().trim()) > Double
								.parseDouble(per_max_22.toString().trim()))) {
					showToast("金额输入有误，请重新输入！");
					return;
					
				} else if (ed_deal_jine.getText().toString().trim().equals("0")) {
					showToast("金额不能为0，请重新输入！");
					return;

				} else {
					if (bankCardCode.equals("****请选择银行卡****")) {
						showToast("请选择银行卡");
						return;
					} else {
			
							System.out.println("+++++++++++"
									+ bankCardCode);
							bundle.putString("BankCardCode", bankCardCode);
							bundle.putString("BankCardCodeShow", bankCardCode_show);
							bundle.putString("PayType", paytype);
							bundle.putString("Certificateno", certificateno);
							bundle.putString("PassWord", encodePassWord);
							Intent intent = new Intent(DealApplyActivity.this,
									DealConfirmActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
			
					}
				}
			} else {
				showToast("金额为空，请输入金额！");
				return;
			}

			break;

		default:
			break;
		}

	}

	

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// view.setText("你的血型是："+m[arg2]);
			bankCardCode = bankCardArray[arg2];
			bankCardCode_show = bankArray[arg2];
			// showToast("你选择的是" + bankArray[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			bankCardCode = bankCardArray[0];
		}
	}

//	// 使用数组形式操作
//	class SpinnerSelectedListener1 implements OnItemSelectedListener {
//
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//			// view.setText("你的血型是："+m[arg2]);
//			bankCardCode = remitCardArray[arg2];
//			bankCardCode_show = remitArrray[arg2];
//			// showToast("你选择的是" + remitArrray[arg2]);
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			// TODO Auto-generated method stub
//			bankCardCode = remitCardArray[0];
//		}
//	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_ONLINEBANKINFORTWO) {

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

											JSONArray array = new JSONArray(
													xmlReturn);
											bankArray = new String[array
													.length()];
											bankCardArray = new String[array
													.length()];

											for (int i = 0; i < array.length(); i++) {

												JSONObject jsonObj = array
														.getJSONObject(i);
												bankArray[i] =jsonObj
														.getString("channelname")+"["+ jsonObj
														.getString("depositacctByConfusion")+"]";
												bankCardArray[i] = jsonObj
														.getString("depositacct");

											}
											certificateno = array
													.getJSONObject(0)
													.getString("certificateno");
											// 将可选内容与ArrayAdapter连接起来
											ArrayAdapter<String> adapter = new ArrayAdapter<String>(
													this,
													android.R.layout.simple_spinner_item,
													bankArray);

											// 设置下拉列表的风格
											adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

											// 将adapter 添加到spinner中
											spinner.setAdapter(adapter);

											// 添加事件Spinner事件监听
											spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

											// 设置默认值
											spinner.setVisibility(View.VISIBLE);

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
			} else if (api == ApiType.GET_REMITBANKINFORTWO) {

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

											JSONArray array = new JSONArray(
													xmlReturn);
											bankArray = new String[array
													.length()];
											bankCardArray = new String[array
													.length()];

											for (int i = 0; i < array.length(); i++) {

												JSONObject jsonObj = array
														.getJSONObject(i);
												bankArray[i] = "["+ jsonObj
														.getString("depositacctByConfusion")+"]";
												bankCardArray[i] = jsonObj
														.getString("depositacct");

											}
//											certificateno = array
//													.getJSONObject(0)
//													.getString("certificateno");
											// 将可选内容与ArrayAdapter连接起来
											ArrayAdapter<String> adapter = new ArrayAdapter<String>(
													this,
													android.R.layout.simple_spinner_item,
													bankArray);

											// 设置下拉列表的风格
											adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

											// 将adapter 添加到spinner中
											spinner.setAdapter(adapter);

											// 添加事件Spinner事件监听
											spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

											// 设置默认值
											spinner.setVisibility(View.VISIBLE);

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
