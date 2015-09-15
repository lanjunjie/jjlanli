package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DTSearchResult;
import com.myfp.myfund.utils.CnUpperCaser;
import com.myfp.myfund.utils.CustomDialog;
import com.myfp.myfund.utils.DESEncrypt;
import com.myfp.myfund.utils.CustomDialog.InputDialogListener;

public class DTApplyActivity extends BaseActivity {
	private String cycleValue, dateValue, bankCardCode, bankCardCode_show,
			certificateno, per_min_39, per_max_39, channelid, moneyaccount,
			paycenterid;
	private DTSearchResult res;
	TextView tv_dtapply_fundcode, tv_dtapply_fundname, tv_dtapply_range,
			tv_dtapply_jineDX;
	EditText ed_dtapply_jine;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	private Spinner sp_dtapply_cycle, sp_dtapply_date, sp_dtapply_banktype;
	private ArrayAdapter<String> bankAdapter, cycleAdapter, dateAdapter;
	String[] bankArray, bankCardArray, channelIdArray, moneyAccountArray,
			payCenterIdArray;
	String[] cycleArray = new String[] { "每月", "每周", "双周" };
	String[] dateArray = new String[] { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28" };
	public static DTApplyActivity instance = null;
	private String PassWord;
	private String encodePassWord,signInfo,agreementCode,returnResult;
	private  CustomDialog customDialog;
	private InputDialogListener  inputDialogListener;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dtapply);
		instance = this;
		bundle = getIntent().getExtras();
		res = (DTSearchResult) bundle.getSerializable("DTSearchResult");
		per_min_39 = res.getPer_min_39();
		per_max_39 = res.getPer_max_39();
		RequestParams params = new RequestParams(this);
		params.put("sessionId", bundle.getString("sessionId"));
		execApi(ApiType.GET_ONLINEBANKINFORTWO, params);
		showProgressDialog("正在加载");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("定期定额基金买入");
		tv_dtapply_fundcode = (TextView) findViewById(R.id.tv_dtapply_fundCode);
		tv_dtapply_fundname = (TextView) findViewById(R.id.tv_dtapply_fundName);
		tv_dtapply_range = (TextView) findViewById(R.id.tv_dtapply_range);
		tv_dtapply_jineDX = (TextView) findViewById(R.id.tv_dtapply_jineDX);
		ed_dtapply_jine = (EditText) findViewById(R.id.ed_dtapply_jine);
		sp_dtapply_banktype = (Spinner) findViewById(R.id.sp_dtapply_banktype);
		findViewAddListener(R.id.bt_applydt);

		tv_dtapply_fundcode.setText("[" + res.getFundcode() + "]");
		tv_dtapply_fundname.setText(res.getFundname());
		tv_dtapply_range.setText("本基金定投范围（" + per_min_39 + "~"
				+ per_max_39.substring(0, per_max_39.indexOf(".") + 3) + "元）");
		ed_dtapply_jine.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
				if(!(TextUtils.isDigitsOnly(ed_dtapply_jine.getText().toString())||ed_dtapply_jine.getText().toString().contains("."))){
					tv_dtapply_jineDX.setText("");
				}else if(ed_dtapply_jine.getText().toString().contains(" ")){
					tv_dtapply_jineDX.setText("");
				}else{
					tv_dtapply_jineDX.setText(new CnUpperCaser(ed_dtapply_jine.getText().toString()).getCnString()+"元");
				}
				
			}
		});
		
	
		
		sp_dtapply_cycle = (Spinner) findViewById(R.id.sp_dtapply_cycle);
		// 将可选内容与ArrayAdapter连接起来
		cycleAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cycleArray);

		// 设置下拉列表的风格
		cycleAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		sp_dtapply_cycle.setAdapter(cycleAdapter);

		// 添加事件Spinner事件监听
		sp_dtapply_cycle
				.setOnItemSelectedListener(new CycleSpinnerSelectedListener());

		// 设置默认值
		sp_dtapply_cycle.setVisibility(View.VISIBLE);

		sp_dtapply_date = (Spinner) findViewById(R.id.sp_dtapply_date);
		// 将可选内容与ArrayAdapter连接起来
		dateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dateArray);

		// 设置下拉列表的风格
		dateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		sp_dtapply_date.setAdapter(dateAdapter);

		// 添加事件Spinner事件监听
		sp_dtapply_date
				.setOnItemSelectedListener(new DateSpinnerSelectedListener());

		// 设置默认值
		sp_dtapply_date.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applydt:

			if (!(ed_dtapply_jine.getText().toString().trim().length() == 0)) {
				if ((Double.parseDouble(ed_dtapply_jine.getText().toString()
						.trim()) < Double.parseDouble(per_min_39.toString()
						.trim()))
						|| (Double.parseDouble(ed_dtapply_jine.getText()
								.toString().trim()) > Double
								.parseDouble(per_max_39.toString().trim()))) {
					showToast("金额输入有误，请重新输入！");
					return;

				} else if (ed_dtapply_jine.getText().toString().trim()
						.equals("0")) {
					showToast("金额不能为0，请重新输入！");
					return;

				} else {
					bundle.putString("JinE", ed_dtapply_jine.getText()
							.toString().trim());
					bundle.putString("BankCardCode", bankCardCode);
					bundle.putString("BankCardCodeShow", bankCardCode_show);
					bundle.putString("CycleValue", cycleValue);
					bundle.putString("DateValue", dateValue);
					bundle.putString("ChannelId", channelid);
					bundle.putString("Certificateno", certificateno);
					bundle.putString("MoneyAccount", moneyaccount);
System.out.println("bundle.getStringCustomRiskLevel"+bundle.getString("CustomRiskLevel"));
					if (Integer.parseInt(bundle.getString("CustomRiskLevel")) < Integer
							.parseInt(res.getRisklevel())) {
						Dialog alertDialog = new AlertDialog.Builder(this)
								.setTitle("提示")
								.setMessage(
										"当前产品风险等级["
												+ res.getRisklevel()
												+ "]与客户风险等级["
												+ bundle.getString("CustomRiskLevel")
												+ "]不匹配,确定要继续吗？")
								.setIcon(R.drawable.ic_launcher)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
												//applyAgreement("0","1");
												initDialog();
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
											
												finish();
												
											}
										}).create();
						alertDialog.show();

					} else {
						initDialog();
						
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
	
	private  void initDialog(){
		customDialog = new CustomDialog(DTApplyActivity.this,R.style.mystyle,R.layout.customdialog) ;
		
		inputDialogListener  = new  InputDialogListener() {
			
			@Override
			public void onOK(String text) {
				
				//给密码文本框设置密码
				DESEncrypt desEpt = new DESEncrypt();
				try {
					PassWord = desEpt.encrypt(text);
					encodePassWord = java.net.URLEncoder
							.encode(PassWord);
					bundle.putString("passwd", encodePassWord);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				applyAgreement("0","1");
			}
			
		};
		customDialog.setListener(inputDialogListener);
		customDialog.show();
	}
	
	

	void applyAgreement(String riskmatching,String buyflag ) {
		RequestParams params = new RequestParams(this);
		//params.put("id", bundle.getString("IDCard"));
		params.put("sessionId", bundle.getString("sessionId"));
		params.put("passwd", encodePassWord);
		params.put("moneyaccount", moneyaccount);
		params.put("firstinvestamount", ed_dtapply_jine.getText().toString()
				.trim());
		params.put("continueinvestamount", ed_dtapply_jine.getText().toString()
				.trim());
		params.put("firstinvestdate", dateValue);
		params.put("channelid", channelid);
		params.put("saveplanflag", "1");
		params.put("investmode", "2");
		params.put("tano", res.getTano());
		params.put("investcyclevalue", dateValue);

		if (cycleValue.equals("每月")) {
			params.put("investcycle", "0");
		} else if (cycleValue.equals("每周")) {
			params.put("investcycle", "1");
		} else if (cycleValue.equals("每双周")) {
			params.put("investcycle", "2");
		}

		params.put("fundcode", res.getFundcode());
		params.put("certificateno", certificateno);
		if (cycleValue.equals("每月")) {
			params.put("investperiods", "0");
		} else if (cycleValue.equals("每周")) {
			params.put("investperiods", "1");
		} else if (cycleValue.equals("每双周")) {
			params.put("investperiods", "2");
		}

		params.put("depositacct", bankCardCode);
		params.put("buyflag", buyflag);
		params.put("riskmatching", riskmatching);
		if (cycleValue.equals("每双周")) {
			params.put("investperiodsvalue", "2");
		} else {
			params.put("investperiodsvalue", "1");
		}

		params.put("paycenterid", paycenterid);
		params.put("certificatetype", "0");
		execApi(ApiType.GET_DTAGREEMENTTWO, params);

		showProgressDialog("正在加载");
         
	}

	// 使用数组形式操作
	class BankSpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// view.setText("你的血型是："+m[arg2]);
			bankCardCode = bankCardArray[arg2];
			bankCardCode_show = bankArray[arg2];
			channelid = channelIdArray[arg2];
			moneyaccount = moneyAccountArray[arg2];
			paycenterid = payCenterIdArray[arg2];
			//showToast("你选择的是" + bankArray[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			bankCardCode = bankCardArray[0];
			channelid = channelIdArray[0];
			moneyaccount = moneyAccountArray[0];
			paycenterid = payCenterIdArray[0];
		}
	}

	// 使用数组形式操作
	class CycleSpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// view.setText("你的血型是："+m[arg2]);
			cycleValue = cycleArray[arg2];
			if (cycleValue.equals("每月")) {

				dateArray = new String[] { "01", "02", "03", "04", "05", "06",
						"07", "08", "09", "10", "11", "12", "13", "14", "15",
						"16", "17", "18", "19", "20", "21", "22", "23", "24",
						"25", "26", "27", "28" };

			} else {
				dateArray = new String[] { "1", "2", "3", "4", "5" };

			}
			dateAdapter = new ArrayAdapter<String>(instance,
					android.R.layout.simple_spinner_item, dateArray);
			// 设置下拉列表的风格
			dateAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			sp_dtapply_date.setAdapter(dateAdapter);

			// 添加事件Spinner事件监听
			sp_dtapply_date
					.setOnItemSelectedListener(new DateSpinnerSelectedListener());

			// 设置默认值
			sp_dtapply_date.setVisibility(View.VISIBLE);

			//showToast("你选择的是" + cycleArray[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			cycleValue = cycleArray[0];
		}
	}

	// 使用数组形式操作
	class DateSpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// view.setText("你的血型是："+m[arg2]);
			dateValue = dateArray[arg2];
			//showToast("你选择的是" + dateArray[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			dateValue = dateArray[0];
		}
	}

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
											channelIdArray = new String[array
													.length()];
											moneyAccountArray = new String[array
													.length()];
											payCenterIdArray = new String[array
													.length()];

											for (int i = 0; i < array.length(); i++) {

												JSONObject jsonObj = array
														.getJSONObject(i);
												bankArray[i] = jsonObj
														.getString("channelname")+"["+ jsonObj
														.getString("depositacctByConfusion")+"]";
												bankCardArray[i] = jsonObj
														.getString("depositacct");
												channelIdArray[i] = jsonObj
														.getString("channelid");
												moneyAccountArray[i] = jsonObj
														.getString("moneyaccount");
												payCenterIdArray[i] = jsonObj
														.getString("paycenterid");

											}
											certificateno = array
													.getJSONObject(0)
													.getString("certificateno");
											// 将可选内容与ArrayAdapter连接起来
											bankAdapter = new ArrayAdapter<String>(
													this,
													android.R.layout.simple_spinner_item,
													bankArray);

											// 设置下拉列表的风格
											bankAdapter
													.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

											// 将adapter 添加到spinner中
											sp_dtapply_banktype
													.setAdapter(bankAdapter);

											// 添加事件Spinner事件监听
											sp_dtapply_banktype
													.setOnItemSelectedListener(new BankSpinnerSelectedListener());

											// 设置默认值
											sp_dtapply_banktype
													.setVisibility(View.VISIBLE);

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
			} else if (api == ApiType.GET_DTAGREEMENTTWO) {

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
										JSONObject obj1;
										try {
											if (xmlReturn.contains("signInfo")) {
												showToast("定投下单成功");
												JSONObject obj = new JSONObject(
														xmlReturn);
												
												signInfo=obj.getString("signInfo");
												
											
													obj1 = new JSONObject(signInfo);
												
													agreementCode = obj1.getString("saveplanno");
											
												RequestParams params = new RequestParams(this);
											
												params.put("sessionId", bundle.getString("sessionId"));
												
												params.put("certIdLength", certificateno.length());
												params.put("fundcode", res.getFundcode());
												params.put("saveplanno",agreementCode);
												params.put("moneyaccount",moneyaccount);
												params.put("applicationamount", ed_dtapply_jine.getText()
														.toString().trim());
												params.put("fundname",res.getFundname());
												params.put("signInfo",java.net.URLEncoder.encode(signInfo.toString().trim()));
												execApi(ApiType.GET_DTPAYTWO, params);	
											} else {
												disMissDialog();
												JSONObject obj = new JSONObject(
														xmlReturn);
												 returnResult = obj
														.getString("msg");
//												showToast(returnResult);
												 dialog();
											 	disMissDialog();
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
			else if (api == ApiType.GET_DTPAYTWO) {

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
														DTApplyActivity.this,
														DTSignAcceptActivity.class);
												intent.putExtras(bundle);
												startActivity(intent);
											} else {
												
												showToast("签约失败，请重新签约");

												Intent intent = new Intent(DTApplyActivity.this,DealDTActivity.class);
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
	protected void dialog(){
		AlertDialog.Builder builder=new Builder(this);
		builder.setMessage("密码输入错误，请重新输入。");
		builder.setTitle("提示");
		builder.setPositiveButton("重试", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initDialog();
				
			}
		});
//		builder.setNegativeButton("忘记密码", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent=new Intent(DTApplyActivity.this, ConfirmInformationActivity.class);
//				startActivity(intent);
//			}
//		});
		builder.create().show();
	}
}
