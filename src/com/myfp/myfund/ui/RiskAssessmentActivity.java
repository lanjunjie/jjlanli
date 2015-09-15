package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.NewRiskSearchResult;
import com.myfp.myfund.api.beans.RiskSearchResult;
import com.myfp.myfund.ui.DealRedeemActivity.RedeemSearchAdapter;

public class RiskAssessmentActivity extends BaseActivity {
	ByteArrayInputStream tInputStringStream = null;
	private List<RiskSearchResult> results;
	private ListView list_riskassessment;
	private String pointList;
	private List<RiskSearchResult> data;
	private List<List<RiskSearchResult>> datas;
	private String result, resultlist, temp1, temp2, temp3, temp4, temp5,
			temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14,
			point1, point2, point3, point4, point5, point6, point7, point8,
			point9, point10, point11, point12, point13, point14;
	RadioButton radio;
	private String answer = "undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined";
	int num;
	Intent intent;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_riskassessment);
		intent = getIntent();
		RequestParams params = new RequestParams(this);
		params.put("sessionId", getIntent().getStringExtra("sessionId"));
		params.put("papercode", "");
		params.put("pointList", "");
		params.put("iscontinue", "");
		params.put("answer", "");
		params.put("invtp", "1");
		execApi(ApiType.GET_RISKQUESTIONTWO, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("个人风险测试");
		list_riskassessment = (ListView) findViewById(R.id.list_riskassessment);
		findViewAddListener(R.id.bt_riskassessment_submit);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_riskassessment_submit:
			if (resultlist == null || resultlist.contains("null")) {
				showToast("请回答全部问题！！");
			} else {
				Intent intent = getIntent();
				RequestParams params = new RequestParams(this);
				params.put("sessionId", getIntent().getStringExtra("sessionId"));
				params.put("papercode", "001");
				params.put("pointList",java.net.URLEncoder.encode(pointList));
				//params.put("pointList",java.net.URLEncoder.encode("2|2|2|2|2|2|2|2|2|2|2|2|2|2"));
				params.put("iscontinue", "1");
				params.put("answer",java.net.URLEncoder.encode(answer));
				//params.put("answer",java.net.URLEncoder.encode("undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined|undefined"));
				params.put("invtp", "1");
				execApi(ApiType.GET_RISKSUBMITTWO, params);
				showProgressDialog("正在提交");
			}
			break;

		default:
			break;
		}

	}

	class RiskSearchAdapter extends BaseAdapter {

		private List<NewRiskSearchResult> list;

		public RiskSearchAdapter(List<NewRiskSearchResult> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			convertView = LayoutInflater.from(RiskAssessmentActivity.this)
					.inflate(R.layout.item_riskassessment, null, false);
			holder = new ViewHolder();

			holder.tv_risklist_questionno = (TextView) convertView
					.findViewById(R.id.tv_risklist_questionno);
			holder.tv_risklist_question = (TextView) convertView
					.findViewById(R.id.tv_risklist_question);

			holder.rp_risk_radioGroup = (RadioGroup) convertView
					.findViewById(R.id.rp_risk_radioGroup);

			convertView.setTag(holder);

			final NewRiskSearchResult res = list.get(position);
			// List<RiskSearchResult> risklist = new
			// ArrayList<RiskSearchResult>();

			holder.tv_risklist_questionno.setText((position + 1) + ".");
			holder.tv_risklist_question.setText(res.getQuestionname());

			for (int i = 0; i < res.getListresult().size(); i++) {
				radio = new RadioButton(getApplicationContext());

				if (!(resultlist == null)) {
					if (resultlist.contains(res.getListresult().get(i))) {
						radio.setChecked(true);
					} else {
						radio.setChecked(false);
					}
				} else {
					radio.setChecked(false);
				}

				radio.setTextSize(13);
				radio.setText(res.getListresult().get(i));
				radio.setTextColor(Color.BLACK);
				holder.rp_risk_radioGroup.addView(radio);
				
			}
			holder.rp_risk_radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0, int arg1) {
							// TODO Auto-generated method stub
							int radioButtonId = arg0.getCheckedRadioButtonId();
							// 根据ID获取RadioButton的实例
							RadioButton radiobt = (RadioButton) findViewById(radioButtonId);

							String result = null;
							String resultpoint = null;

							if (result == null) {
								result = radiobt.getText().toString();
							}
							for (int i = 0; i < res.getListresult().size(); i++) {
								if (radiobt.getText().equals(
										res.getListresult().get(i))) {
									resultpoint = res.getListpoint().get(i)
											.substring(0, res.getListpoint().get(i).indexOf("."));
								}
							}
							System.out.println(resultpoint);
							System.out.println(result);

							if (res.getQuestioncode().equals("01")) {
								temp1 = result;
								point1 = resultpoint;
							} else if (res.getQuestioncode().equals("02")) {
								temp2 = result;
								point2 = resultpoint;
							} else if (res.getQuestioncode().equals("03")) {
								temp3 = result;
								point3 = resultpoint;
							} else if (res.getQuestioncode().equals("04")) {
								temp4 = result;
								point4 = resultpoint;
							} else if (res.getQuestioncode().equals("05")) {
								temp5 = result;
								point5 = resultpoint;
							}

							else if (res.getQuestioncode().equals("06")) {
								temp6 = result;
								point6 = resultpoint;
							} else if (res.getQuestioncode().equals("07")) {
								temp7 = result;
								point7 = resultpoint;
							} else if (res.getQuestioncode().equals("08")) {
								temp8 = result;
								point8 = resultpoint;
							} else if (res.getQuestioncode().equals("09")) {
								temp9 = result;
								point9 = resultpoint;
							} else if (res.getQuestioncode().equals("10")) {
								temp10 = result;
								point10 = resultpoint;
							} else if (res.getQuestioncode().equals("11")) {
								temp11 = result;
								point11 = resultpoint;
							} else if (res.getQuestioncode().equals("12")) {
								temp12 = result;
								point12 = resultpoint;
							} else if (res.getQuestioncode().equals("13")) {
								temp13 = result;
								point13 = resultpoint;
							} else if (res.getQuestioncode().equals("14")) {
								temp14 = result;
								point14 = resultpoint;
							}

							resultlist = temp1 + temp2 + temp3 + temp4 + temp5
									+ temp6 + temp7 + temp8 + temp9 + temp10
									+ temp11 + temp12 + temp13 + temp14;
							pointList = point1 + "|" + point2 + "|" + point3
									+ "|" + point4 + "|" + point5 + "|"
									+ point6 + "|" + point7 + "|" + point8
									+ "|" + point9 + "|" + point10 + "|"
									+ point11 + "|" + point12 + "|" + point13
									+ "|" + point14;
								
						/*	pointList = 2 + "|" + 2 + "|" + 2
									+ "|" + 2 + "|" + 2 + "|"
									+ 2 + "|" + 2 + "|" + 2
									+ "|" + 2 + "|" + 2 + "|"
									+ 2 + "|" + 2 + "|" + 2
									+ "|" + 2;
							*/
							System.out.println(resultlist);
							System.out.println(pointList);
						}
					});

			return convertView;
		}

		class ViewHolder {
			TextView tv_risklist_questionno, tv_risklist_question;
			RadioGroup rp_risk_radioGroup;
			RadioButton radio;
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("网络不给力！");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_RISKQUESTIONTWO) {
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
									results = JSON.parseArray(xmlReturn,
											RiskSearchResult.class);
									if (results.size() == 0) {

										disMissDialog();
										return;
									} else {
										List<NewRiskSearchResult> listresult = new ArrayList<NewRiskSearchResult>();

										NewRiskSearchResult res1 = new NewRiskSearchResult();
										NewRiskSearchResult res2 = new NewRiskSearchResult();
										NewRiskSearchResult res3 = new NewRiskSearchResult();
										NewRiskSearchResult res4 = new NewRiskSearchResult();
										NewRiskSearchResult res5 = new NewRiskSearchResult();
										NewRiskSearchResult res6 = new NewRiskSearchResult();
										NewRiskSearchResult res7 = new NewRiskSearchResult();
										NewRiskSearchResult res8 = new NewRiskSearchResult();
										NewRiskSearchResult res9 = new NewRiskSearchResult();
										NewRiskSearchResult res10 = new NewRiskSearchResult();
										NewRiskSearchResult res11 = new NewRiskSearchResult();
										NewRiskSearchResult res12 = new NewRiskSearchResult();
										NewRiskSearchResult res13 = new NewRiskSearchResult();
										NewRiskSearchResult res14 = new NewRiskSearchResult();

										List<String> list1 = new ArrayList<String>();
										List<String> list2 = new ArrayList<String>();
										List<String> list3 = new ArrayList<String>();
										List<String> list4 = new ArrayList<String>();
										List<String> list5 = new ArrayList<String>();
										List<String> list6 = new ArrayList<String>();
										List<String> list7 = new ArrayList<String>();
										List<String> list8 = new ArrayList<String>();
										List<String> list9 = new ArrayList<String>();
										List<String> list10 = new ArrayList<String>();
										List<String> list11 = new ArrayList<String>();
										List<String> list12 = new ArrayList<String>();
										List<String> list13 = new ArrayList<String>();
										List<String> list14 = new ArrayList<String>();

										List<String> listpoint1 = new ArrayList<String>();
										List<String> listpoint2 = new ArrayList<String>();
										List<String> listpoint3 = new ArrayList<String>();
										List<String> listpoint4 = new ArrayList<String>();
										List<String> listpoint5 = new ArrayList<String>();
										List<String> listpoint6 = new ArrayList<String>();
										List<String> listpoint7 = new ArrayList<String>();
										List<String> listpoint8 = new ArrayList<String>();
										List<String> listpoint9 = new ArrayList<String>();
										List<String> listpoint10 = new ArrayList<String>();
										List<String> listpoint11 = new ArrayList<String>();
										List<String> listpoint12 = new ArrayList<String>();
										List<String> listpoint13 = new ArrayList<String>();
										List<String> listpoint14 = new ArrayList<String>();
										System.out
												.println("+++++++++111111111");
										for (int i = 0; i < results.size(); i++) {
											if (results.get(i)
													.getQuestioncode()
													.equals("01")) {

												res1.setQuestionname(results
														.get(i)
														.getQuestionname());
												res1.setQuestioncode(results
														.get(i)
														.getQuestioncode());

												listpoint1.add(results.get(i)
														.getResultpoint());
												list1.add(results.get(i)
														.getResultcontent());
												System.out
														.println("+++++++++5628");
											} else if (results.get(i)
													.getQuestioncode()
													.equals("02")) {
												res2.setQuestionname(results
														.get(i)
														.getQuestionname());
												res2.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint2.add(results.get(i)
														.getResultpoint());
												list2.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("03")) {
												res3.setQuestionname(results
														.get(i)
														.getQuestionname());
												res3.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint3.add(results.get(i)
														.getResultpoint());
												list3.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("04")) {
												res4.setQuestionname(results
														.get(i)
														.getQuestionname());
												res4.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint4.add(results.get(i)
														.getResultpoint());
												list4.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("05")) {
												res5.setQuestionname(results
														.get(i)
														.getQuestionname());
												res5.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint5.add(results.get(i)
														.getResultpoint());
												list5.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("06")) {
												res6.setQuestionname(results
														.get(i)
														.getQuestionname());
												res6.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint6.add(results.get(i)
														.getResultpoint());
												list6.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("07")) {
												res7.setQuestionname(results
														.get(i)
														.getQuestionname());
												res7.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint7.add(results.get(i)
														.getResultpoint());
												list7.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("08")) {
												res8.setQuestionname(results
														.get(i)
														.getQuestionname());
												res8.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint8.add(results.get(i)
														.getResultpoint());
												list8.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("09")) {
												res9.setQuestionname(results
														.get(i)
														.getQuestionname());
												res9.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint9.add(results.get(i)
														.getResultpoint());
												list9.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("10")) {
												res10.setQuestionname(results
														.get(i)
														.getQuestionname());
												listpoint10.add(results.get(i)
														.getResultpoint());
												res10.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												list10.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("11")) {
												res11.setQuestionname(results
														.get(i)
														.getQuestionname());
												listpoint11.add(results.get(i)
														.getResultpoint());
												res11.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												list11.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("12")) {
												res12.setQuestionname(results
														.get(i)
														.getQuestionname());
												res12.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint12.add(results.get(i)
														.getResultpoint());
												list12.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("13")) {
												res13.setQuestionname(results
														.get(i)
														.getQuestionname());
												res13.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint13.add(results.get(i)
														.getResultpoint());

												list13.add(results.get(i)
														.getResultcontent());
											} else if (results.get(i)
													.getQuestioncode()
													.equals("14")) {
												res14.setQuestionname(results
														.get(i)
														.getQuestionname());
												res14.setQuestioncode(results
														.get(i)
														.getQuestioncode());
												listpoint14.add(results.get(i)
														.getResultpoint());
												list14.add(results.get(i)
														.getResultcontent());
											}

										}

										System.out.println("+++++++++1234");
										res1.setListresult(list1);
										res2.setListresult(list2);
										res3.setListresult(list3);
										res4.setListresult(list4);
										res5.setListresult(list5);
										res6.setListresult(list6);
										res7.setListresult(list7);
										res8.setListresult(list8);
										res9.setListresult(list9);
										res10.setListresult(list10);
										res11.setListresult(list11);
										res12.setListresult(list12);
										res13.setListresult(list13);
										res14.setListresult(list14);

										res1.setListpoint(listpoint1);
										res2.setListpoint(listpoint2);
										res3.setListpoint(listpoint3);
										res4.setListpoint(listpoint4);
										res5.setListpoint(listpoint5);
										res6.setListpoint(listpoint6);
										res7.setListpoint(listpoint7);
										res8.setListpoint(listpoint8);
										res9.setListpoint(listpoint9);
										res10.setListpoint(listpoint10);
										res11.setListpoint(listpoint11);
										res12.setListpoint(listpoint12);
										res13.setListpoint(listpoint13);
										res14.setListpoint(listpoint14);

										listresult.add(res1);
										listresult.add(res2);
										listresult.add(res3);
										listresult.add(res4);
										listresult.add(res5);
										listresult.add(res6);
										listresult.add(res7);
										listresult.add(res8);
										listresult.add(res9);
										listresult.add(res10);
										listresult.add(res11);
										listresult.add(res12);
										listresult.add(res13);
										listresult.add(res14);
										list_riskassessment
												.setAdapter(new RiskSearchAdapter(
														listresult));
										//setListViewHeightBasedOnChildren(list_riskassessment);
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

		} else if (api == ApiType.GET_RISKSUBMITTWO) {

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
									if (xmlReturn.contains("custrisk")) {
										try {
											JSONObject jsonObj = new JSONObject(
													xmlReturn);
											disMissDialog();
											showToast("提交成功");
											Intent intent1 = new Intent(
													RiskAssessmentActivity.this,
													TestResultActivity.class);
											intent1.putExtra(
													"CustomRiskLevel",
													jsonObj.getString("custrisk"));
											intent1.putExtra(
													"DepositacctName",
													intent.getStringExtra("DepositacctName"));
											intent1.putExtra(
													"TotalFundMarketValue",
													intent.getStringExtra("TotalFundMarketValue"));
											intent1.putExtra(
													"CountFund",
													intent.getStringExtra("CountFund"));
											intent1.putExtra("sessionId", getIntent().getStringExtra("sessionId"));
											startActivity(intent1);
											
											finish();
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									} else {
										disMissDialog();
										showToast("提交失败，请重新提交");
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

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		RiskSearchAdapter listAdapter = (RiskSearchAdapter) listView
				.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

}
