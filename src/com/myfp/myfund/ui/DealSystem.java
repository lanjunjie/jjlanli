package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.HoldDetail;
import com.myfp.myfund.ui.HoldDetailActivity.HoldListAdapter;
import com.myfp.myfund.ui.HoldDetailActivity.HoldListAdapter.ViewHolder;

public class DealSystem extends BaseActivity{
	private String encodeIdCard;
	private String encodePassWord;
	private String customrisklevel;
	private String countfund;
	private String depositacctname;
	ByteArrayInputStream tInputStringStream = null;
	private TextView tex_sun_t;
	private TextView new_deal_openprofit;
	private TextView new_deal_capitalization;
	private ListView new_holddetall;
	private String sessionid;
	private String risklevel;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_activity_deal);
		Intent intent = getIntent();
		encodeIdCard = App.getContext().getIdCard();
		System.out.println("PassWord--------------->"+encodePassWord);
		risklevel = intent.getStringExtra("CustomRiskLevel");
		countfund = intent.getStringExtra("CountFund");
		System.out.println("countfund数据为空不============>"+countfund);
		depositacctname = intent.getStringExtra("DepositacctName");
		System.out.println("depositacctname数据是否为空------------》"+depositacctname);
		intent.getStringExtra("TotalFundMarketValue");
		intent.getStringExtra("Totalfloatprofit");
		sessionid = intent.getStringExtra("sessionid");
		if (sessionid==null) {
			RequestParams pam=new RequestParams(this);
			pam.put("idcard", encodeIdCard);
			execApi(ApiType.GET_STEPVERIFICATION, pam);
		}
		System.out.println("sessionId=====>"+sessionid);
		RequestParams params=new RequestParams(this);
		params.put("sessionId", sessionid);
		execApi(ApiType.GET_HOLD_DETAILTWO, params);
		
	}

	@Override
	protected void initViews() {
		setTitle("交易系统");
		TextView new_deal_person = (TextView) findViewById(R.id.new_deal_person);
		new_deal_capitalization = (TextView) findViewById(R.id.new_deal_capitalization);
		new_deal_openprofit = (TextView) findViewById(R.id.new_deal_openprofit);
		TextView new_deal_risk = (TextView) findViewById(R.id.new_deal_risk);
		tex_sun_t = (TextView) findViewById(R.id.tex_sun_t);
		new_holddetall = (ListView) findViewById(R.id.new_holddetall);
		findViewAddListener(R.id.new_deal_reappraise);
		findViewAddListener(R.id.new_deal_buy);
		findViewAddListener(R.id.new_deal_ransom);
		findViewAddListener(R.id.new_deal_ipush);
		findViewAddListener(R.id.new_deal_change);
		findViewAddListener(R.id.new_deal_manage);
		findViewAddListener(R.id.new_deal_cancle);
		findViewAddListener(R.id.new_deal_query);
		findViewAddListener(R.id.new_deal_bonus);
		new_deal_person.setText(depositacctname);
		if(risklevel!=null) {
			switch (Integer.parseInt(risklevel)) {
			case 01:
				new_deal_risk.setText("安逸型");
				break;
			case 02:
				new_deal_risk.setText("保守型");
				break;
			case 03:
				new_deal_risk.setText("稳健型");
				break;
			case 04:
				new_deal_risk.setText("积极型");
				break;
			case 05:
				new_deal_risk.setText("激进型");
				break;
				
			default:
				break;
			}
			
		}
		
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_deal_buy:
			// 基金购买
			Intent intent = new Intent(DealSystem.this,
				DealBuyActivity.class);
				intent.putExtra("sessionId", sessionid);
				startActivity(intent);
			break;
		case R.id.new_deal_ransom:
			// 基金赎回
			Intent intent1 = new Intent(DealSystem.this,
				DealRedeemActivity.class);
				intent1.putExtra("sessionId", sessionid);
				startActivity(intent1);
			break;
		case R.id.new_deal_ipush:
			// 定投
		Intent intent7 = new Intent(DealSystem.this,
				DealDTActivity.class);
				intent7.putExtra("sessionId", sessionid);
				intent7.putExtra("CustomRiskLevel", risklevel);
				startActivity(intent7);		
			break;
		case R.id.new_deal_change:
			// 基金转换
		Intent intent4 = new Intent(DealSystem.this,
			DealChangeActivity.class);
			intent4.putExtra("sessionId", sessionid);
			startActivity(intent4);
			break;
		case R.id.new_deal_manage:
			// 定投管理
		Intent intent8 = new Intent(DealSystem.this,
				DTManageActivity.class);
			intent8.putExtra("sessionId", sessionid);
			intent8.putExtra("CustomRiskLevel", customrisklevel);
			startActivity(intent8);
			break;
		case R.id.new_deal_cancle:
			// 基金撤单
		Intent intent2 = new Intent(DealSystem.this,
				DealCancellationActivity.class);
				intent2.putExtra("sessionId", sessionid);
				startActivity(intent2);
			break;
		case R.id.new_deal_query:
			// 委托查询
			Intent intent5 = new Intent(DealSystem.this,
					DealEntrustActivity.class);
			intent5.putExtra("sessionId", sessionid);
			startActivity(intent5);
			break;
		case R.id.new_deal_bonus:
			//分红方式
			Intent intent12=new Intent(DealSystem.this, 
					queryAssetsActivity.class);
			intent12.putExtra("sessionId", sessionid);
			startActivity(intent12);
			break;
		case R.id.new_deal_reappraise:
			//重新评估
			Intent intent3=new Intent(DealSystem.this, StartTest.class);
			intent3.putExtra("sessionId", sessionid);
			//intent3.putExtra("IDCard", encodeIdCard);
			startActivity(intent3);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		// 在这里会收到数据,一定要判断是否为空
		if (json == null) {
			//showToast("获取失败!");
			disMissDialog();
			return;
		}
		if(api==ApiType.GET_STEPVERIFICATION) {
			if (json!=null&&!json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event!= XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								try {
									String xmlReturn = parser.nextText();
									System.out.println("--------------->"+xmlReturn);
									
									JSONObject jsonObj=new JSONObject(xmlReturn);
									sessionid = jsonObj.getString("sessionid");
									App.getContext().setSessionid(sessionid);
									System.out.println("sessionid============>"+sessionid);
									risklevel = jsonObj.getString("risklevel");
									System.out.println("risklevel==========>"+risklevel);
									RequestParams params=new RequestParams(this);
									params.put("sessionId", sessionid);
									execApi(ApiType.GET_HOLD_DETAILTWO, params,new OnDataReceivedListener() {
										
										@Override
										public void onReceiveData(ApiType api, String json) {
											
											try {
												if (json != null && !json.trim().equals("")) {
													tInputStringStream = new ByteArrayInputStream(
															json.getBytes());
													XmlPullParser parser = Xml.newPullParser();
													parser.setInput(tInputStringStream, "UTF-8");
													int event = parser.getEventType();
													while (event != XmlPullParser.END_DOCUMENT) {
														Log.i("start_document", "start_document");
														switch (event) {
														case XmlPullParser.START_TAG:
															if ("return".equals(parser.getName())) {
																String xmlReturn = parser.nextText();
																System.out.println("<><><><><><><><><>"
																		+ xmlReturn);
																if(xmlReturn.equals("[]")){
																	disMissDialog();
																	showToast("您没有持有基金");
																	return;
																}else{
																	final List<HoldDetail> datas = JSON.parseArray(
																			xmlReturn, HoldDetail.class);
																	
																	double zongshizhi = 0;
																	double zongyingkui = 0;
																	double zongshouyilv = 0;
																	
																	
																	zongshouyilv = Double.parseDouble(datas.get(0)
																			.getTotaladdincomerate());
																	zongyingkui = Double.parseDouble(datas.get(0)
																			.getTotalfloatprofit());
																	for (int i = 1; i < datas.size(); i++) {
																		
																		zongshizhi += (Double.parseDouble(datas
																				.get(i).getMktvalue()));
																	}
																	
																	tex_sun_t.setText("（" + (datas.size()-1)
																			+ "支" + "）");
																	new_deal_capitalization.setText(String.format("%.2f",
																			zongshizhi));
																	new_deal_openprofit.setText(String.format("%.2f",
																			zongyingkui));
																	if (zongyingkui < 0) {

																		new_deal_openprofit.setTextColor(Color.rgb(1,
																				153, 1));
																	} else {
																		new_deal_openprofit.setTextColor(Color.RED);
																	}


																	System.out.println("<><><><><><><><><>"
																			+ String.format("%.2f",
																					zongshizhi * 100));
																	System.out.println("<><><><><><><><><>"
																			+ String.format("%.2f",
																					zongyingkui * 100));
																	System.out.println("<><><><><><><><><>"
																			+ String.format("%.2f",
																					zongshouyilv * 100));
																	System.out.println("<><><><><><><><><>"
																			+ (datas.size()-1) + "");

																	new_holddetall.setAdapter(new HoldListAdapter(datas));
																	setListViewHeightBasedOnChildren(new_holddetall);
																	new_holddetall.setOnItemClickListener(new OnItemClickListener() {

																		@Override
																		public void onItemClick(
																				AdapterView<?> parent, View view,
																				int position, long itemId) {
																			// TODO Auto-generated method stub
																			HoldDetail hd = datas.get(position+1);
																			Intent intent = new Intent(DealSystem.this,
																					DetailActivity.class);
																			intent.putExtra("fundName", hd
																					.getFundname().trim());
																			intent.putExtra("fundCode", hd
																					.getFundcode().trim());
																			intent.putExtra("sessionId", sessionid);
																			startActivity(intent);

																		}

																	});
																}
																
																
															}

															break;

														}
														event = parser.next();
													}
													tInputStringStream.close();
												}
											} catch (Exception e) {
												// TODO: handle exception
												return;
											}
											disMissDialog();

										
											
										}
									});
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
							break;
							
						default:
							break;
						}
						try {
							event = parser.next();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			disMissDialog();
		}else

		if (api == ApiType.GET_HOLD_DETAILTWO) {

			try {
				if (json != null && !json.trim().equals("")) {
					tInputStringStream = new ByteArrayInputStream(
							json.getBytes());
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								String xmlReturn = parser.nextText();
								System.out.println("<><><><><><><><><>"
										+ xmlReturn);
								if(xmlReturn.equals("[]")){
									showToast("您没有持有基金");
									disMissDialog();
									return;
								}else{
									final List<HoldDetail> datas = JSON.parseArray(
											xmlReturn, HoldDetail.class);
									
									double zongshizhi = 0;
									double zongyingkui = 0;
									double zongshouyilv = 0;
									
									
									zongshouyilv = Double.parseDouble(datas.get(0)
											.getTotaladdincomerate());
									zongyingkui = Double.parseDouble(datas.get(0)
											.getTotalfloatprofit());
									for (int i = 1; i < datas.size(); i++) {
										
										zongshizhi += (Double.parseDouble(datas
												.get(i).getMktvalue()));
									}
									
									tex_sun_t.setText("（" + (datas.size()-1)
											+ "支" + "）");
									new_deal_capitalization.setText(String.format("%.2f",
											zongshizhi));
									new_deal_openprofit.setText(String.format("%.2f",
											zongyingkui));
									if (zongyingkui < 0) {

										new_deal_openprofit.setTextColor(Color.rgb(1,
												153, 1));
									} else {
										
										new_deal_openprofit.setTextColor(Color.RED);
									}

							
									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongshizhi * 100));
									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongyingkui * 100));
									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongshouyilv * 100));
									System.out.println("<><><><><><><><><>"
											+ (datas.size()-1) + "");

									new_holddetall.setAdapter(new HoldListAdapter(datas));
									setListViewHeightBasedOnChildren(new_holddetall);
									new_holddetall.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent, View view,
												int position, long itemId) {
											// TODO Auto-generated method stub
											HoldDetail hd = datas.get(position+1);
											Intent intent = new Intent(DealSystem.this,
													DetailActivity.class);
											intent.putExtra("fundName", hd
													.getFundname().trim());
											intent.putExtra("fundCode", hd
													.getFundcode().trim());
											intent.putExtra("sessionId", sessionid);
											startActivity(intent);

										}

									});
								}
								
								
							}

							break;

						}
						event = parser.next();
					}
					tInputStringStream.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
			disMissDialog();

		}

	}

	class HoldListAdapter extends BaseAdapter {
		private List<HoldDetail> data;

		public HoldListAdapter(List<HoldDetail> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size()-1;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			HoldDetail hd = data.get(position+1);
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_holddetail, null);
				holder = new ViewHolder();
				holder.textView_fundName = (TextView) convertView
						.findViewById(R.id.tv_hold_fundname);
				holder.textView_fundCode = (TextView) convertView
						.findViewById(R.id.tv_hold_fundcode);
				holder.textView_shizhi = (TextView) convertView
						.findViewById(R.id.tv_hold_shizhi);
				holder.textView_yingkui = (TextView) convertView
						.findViewById(R.id.tv_hold_yingkui);
				holder.textView_shouyilv = (TextView) convertView
						.findViewById(R.id.tv_hold_shouyilv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView_fundName.setText(hd.getFundname());
			holder.textView_fundName.setTextColor(Color.BLACK);

			holder.textView_fundCode.setText(hd.getFundcode());
			holder.textView_fundCode.setTextColor(Color.BLACK);

			holder.textView_shizhi.setText(String.format("%.2f",
					Double.parseDouble(hd.getMktvalue())));
			holder.textView_shizhi.setTextColor(Color.BLACK);

			holder.textView_yingkui.setText(String.format("%.2f",
					Double.parseDouble(hd.getFloatprofit())));

			if (Double.parseDouble(hd.getFloatprofit()) < 0) {
				holder.textView_yingkui.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.textView_yingkui.setTextColor(Color.RED);
			}
			holder.textView_shouyilv.setText(String.format("%.2f",
					(Double.parseDouble(hd.getAddincomerate())*100)) + "%");

			if (Double.parseDouble(hd.getAddincomerate()) < 0) {
				holder.textView_shouyilv.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.textView_shouyilv.setTextColor(Color.RED);
			}

			return convertView;
		}

		class ViewHolder {
			TextView textView_fundName, textView_fundCode, textView_shizhi,
					textView_yingkui, textView_shouyilv;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		HoldListAdapter listAdapter = (HoldListAdapter) listView.getAdapter();
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
