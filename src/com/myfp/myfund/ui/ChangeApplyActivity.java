package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.ui.RedeemApplyActivity.SpinnerSelectedListener;
import com.myfp.myfund.utils.CnUpperCaser;

public class ChangeApplyActivity extends BaseActivity {
	public static ChangeApplyActivity instance = null;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	TextView tv_changeapply_fundcode, tv_changeapply_fundname,
			tv_changeapply_range, tv_changeapply_chiyoufene,
			tv_changeapply_dongjiefene, tv_changeapply_keyongfene,
			tv_changeapply_feneDX;
	EditText ed_changeapply_changefene;
	private RedeemSearchResult res;
	String[] changeFundArray, changeTargetFundCode, changeTargetFundName;
	private String change_min, changeTargetFund, targetFundCode,
			targetFundName;
	Button bt_changeapply;
	private String sessionId;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changeapply);
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		RequestParams params = new RequestParams(this);
		//params.put("id", bundle.getString("IDCard"));
		params.put("sessionId", sessionId);
		//params.put("passwd", bundle.getString("PassWord"));
		params.put("fundcode", res.getFundcode());
		params.put("tano", res.getTano());
		params.put("sharetype", res.getSharetype());
		execApi(ApiType.GET_CHANGEFORFUNDTWO, params);
		RequestParams params1 = new RequestParams(this);
		//params1.put("id", bundle.getString("IDCard"));
		//params1.put("passwd", bundle.getString("PassWord"));
		params1.put("sessionId", sessionId);
		params1.put("condition", res.getFundcode().trim());
		execApi(ApiType.GET_FUNDTOTALINFORTWO, params1);

		showProgressDialog("正在加载");

	}

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// view.setText("你的血型是："+m[arg2]);
			changeTargetFund = changeFundArray[arg2];
			targetFundCode = changeTargetFundCode[arg2];
			targetFundName = changeTargetFundName[arg2];
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金转换");
		tv_changeapply_fundname = (TextView) findViewById(R.id.tv_changeapply_fundname);
		tv_changeapply_fundcode = (TextView) findViewById(R.id.tv_changeapply_fundcode);
		tv_changeapply_chiyoufene = (TextView) findViewById(R.id.tv_changeapply_chiyoufene);
		tv_changeapply_dongjiefene = (TextView) findViewById(R.id.tv_changeapply_dongjiefene);
		tv_changeapply_keyongfene = (TextView) findViewById(R.id.tv_changeapply_keyongfene);
		tv_changeapply_range = (TextView) findViewById(R.id.tv_changeapply_range);
		ed_changeapply_changefene = (EditText) findViewById(R.id.ed_changeapply_changefene);
		tv_changeapply_feneDX = (TextView) findViewById(R.id.tv_changeapply_feneDX);
		bt_changeapply = (Button) findViewById(R.id.bt_changeapply);
		spinner = (Spinner) findViewById(R.id.sp_changeapply_spinner);

		findViewAddListener(R.id.bt_changeapply);

		tv_changeapply_fundcode.setText("[" + res.getFundcode() + "]");
		tv_changeapply_fundname.setText(res.getFundname());
		tv_changeapply_chiyoufene.setText(res.getFundvolbalance());
		tv_changeapply_dongjiefene.setText(res.getTrdfrozen());
		tv_changeapply_keyongfene.setText(res.getAvailablevol());
		ed_changeapply_changefene.setText(res.getAvailablevol());
		tv_changeapply_feneDX.setText(new CnUpperCaser(res.getAvailablevol())
				.getCnString() + "(份)");

		ed_changeapply_changefene.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				if (!(TextUtils.isDigitsOnly(ed_changeapply_changefene.getText()
						.toString()) || ed_changeapply_changefene.getText().toString()
						.contains("."))) {
					tv_changeapply_feneDX.setText("");
				} else if (ed_changeapply_changefene.getText().toString().contains(" ")) {
					tv_changeapply_feneDX.setText("");
				} else {
					tv_changeapply_feneDX.setText(new CnUpperCaser(ed_changeapply_changefene
							.getText().toString()).getCnString() + "份");
				}

			}
		});
	

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_changeapply:
			bundle.putString("ChangeFenE", ed_changeapply_changefene.getText()
					.toString().trim());
			bundle.putString("ChangeTargetFund", changeTargetFund);
			bundle.putString("TargetFundCode", targetFundCode);
			bundle.putString("TargetFundName", targetFundName);
			if (!(ed_changeapply_changefene.getText().toString().trim()
					.length() == 0)) {
				if ((Double.parseDouble(ed_changeapply_changefene.getText()
						.toString().trim()) < Double.parseDouble(change_min.toString().trim()))
						|| (Double.parseDouble(ed_changeapply_changefene.getText().toString().trim()) > Double.parseDouble(tv_changeapply_keyongfene
										.getText().toString().trim()))) {
					showToast("份额输入有误，请重新输入！");
					return;

				} else if (ed_changeapply_changefene.getText().toString()
						.trim().equals("0")) {
					showToast("份额不能为0，请重新输入！");
					return;

				} else {
					Intent intent = new Intent(ChangeApplyActivity.this,
							ChangeConfirmActivity.class);
					
					intent.putExtras(bundle);
					startActivity(intent);
				}
			} else {
				showToast("份额为空，请输入金额！");
				return;
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_CHANGEFORFUNDTWO) {
			if (json != null && !json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								String xmlReturn;
								try {
									xmlReturn = parser.nextText();
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);
									try {
										if (xmlReturn.contains("查询可转换基金成功!")) {

											changeFundArray = new String[] { "没有可转换的目标基金" };
											changeTargetFundCode = new String[] { "" };
											changeTargetFundName = new String[] { "" };
											bt_changeapply.setEnabled(false);
											bt_changeapply
													.setBackgroundColor(Color.GRAY);
										} else {
											JSONArray array = new JSONArray(
													xmlReturn);
											changeFundArray = new String[array
													.length()];
											changeTargetFundCode = new String[array
													.length()];
											changeTargetFundName = new String[array
													.length()];
											for (int i = 0; i < array.length(); i++) {
												JSONObject obj = array
														.getJSONObject(i);
												changeTargetFundCode[i] = obj
														.getString("targetfundcode");
												changeTargetFundName[i] = obj
														.getString("fundname");
												changeFundArray[i] = "["
														+ obj.getString("targetfundcode")
														+ "]"
														+ obj.getString("fundname");
											}
										}

										// 将可选内容与ArrayAdapter连接起来
										adapter = new ArrayAdapter<String>(
												this,
												android.R.layout.simple_spinner_item,
												changeFundArray);

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
		} else if (api == ApiType.GET_FUNDTOTALINFORTWO) {
			if (json != null && !json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								String xmlReturn;
								try {
									xmlReturn = parser.nextText();
									System.out.println("<><><><><><><><><>"
											+ xmlReturn);
									try {
										JSONArray array = new JSONArray(
												xmlReturn);
										JSONObject obj = array.getJSONObject(0);
										change_min = obj
												.getString("per_min_36");
										System.out.println("-----------------"
												+ change_min);
										tv_changeapply_range.setText("本基金转换范围（"
												+ change_min + "份~"
												+ res.getAvailablevol() + "份）");

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
				disMissDialog();

			}
		}

	}
}
