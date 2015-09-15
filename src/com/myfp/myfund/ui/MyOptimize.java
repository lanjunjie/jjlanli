package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CemeteryResult;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.SearchResult;
import com.myfp.myfund.ui.Myfundsearch.AddSearchResultAdapter;
import com.myfp.myfund.ui.Myfundsearch.CemeteryAdpter;
import com.myfp.myfund.utils.HttpUtil;

public class MyOptimize extends BaseActivity  {
	ByteArrayInputStream tInputStringStream = null;
	private CemeteryResult res;
	private String fundCode, fundName;

	private String userName, sessionId;
	private ListView gupiaoxing, wenjianxing, qdiixing;
	private Handler handler = new Handler() {

		private List<CemeteryResult> results;
		

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:
				String vale = (String) msg.obj;
				results = JSON.parseArray(vale, CemeteryResult.class);
				gupiaoxing.setAdapter(new CemeteryAdpter(results));
				Utility.setListViewHeightBasedOnChildren(gupiaoxing);
				gupiaoxing.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						CemeteryResult res = results.get(position);
						Intent intent = new Intent(MyOptimize.this,
								DetailActivity.class);
						intent.putExtra("sessionId", App.getContext()
								.getSessionid());
						intent.putExtra("fundName", res.getFundName().trim());
						intent.putExtra("fundCode", res.getFundCode().trim());
						// showProgressDialog("正在加载");
						startActivity(intent);
					}
				});

				disMissDialog();
				break;
			case 2:
				String vale1 = (String) msg.obj;

				System.out.println("Vale===============>" + vale1);
				results = JSON.parseArray(vale1, CemeteryResult.class);
				wenjianxing.setAdapter(new CemeteryAdpter(results));
				Utility.setListViewHeightBasedOnChildren(wenjianxing);
				wenjianxing.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						CemeteryResult res = results.get(arg2);
						Intent intent = new Intent(MyOptimize.this,
								DetailActivity.class);
						intent.putExtra("sessionId", App.getContext()
								.getSessionid());
						intent.putExtra("fundName", res.getFundName().trim());
						intent.putExtra("fundCode", res.getFundCode().trim());
						// showProgressDialog("正在加载");
						startActivity(intent);

					}

				});
				disMissDialog();
				break;
			case 3:
				String vale3 = (String) msg.obj;

				System.out.println("Vale===============>" + vale3);
				results = JSON.parseArray(vale3, CemeteryResult.class);
				qdiixing.setAdapter(new CemeteryAdpter(results));
				Utility.setListViewHeightBasedOnChildren(qdiixing);
				qdiixing.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						CemeteryResult res = results.get(position);
						Intent intent = new Intent(MyOptimize.this,
								DetailActivity.class);
						intent.putExtra("sessionId", App.getContext()
								.getSessionid());
						intent.putExtra("fundName", res.getFundName().trim());
						intent.putExtra("fundCode", res.getFundCode().trim());
						// showProgressDialog("正在加载");
						startActivity(intent);
					}
				});
				break;
			}
		};
	};

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_my_optimize);
		userName = App.getContext().getUserName();
		sessionId = App.getContext().getSessionid();
	}

	@Override
	protected void initViews() {
		setTitle("展恒优选");

		gupiaoxing = (ListView) findViewById(R.id.gupiaoxing);
		wenjianxing = (ListView) findViewById(R.id.wenjianxing);
		qdiixing = (ListView) findViewById(R.id.qdiixing);
		showProgressDialog("正在加载");
		final String CEMETEEY_URL = "http://app.myfund.com:8484/Service/MyfundDemo.svc/GetHotfundList30?type=1";
		final String CEMETEEY_URL3 = "http://app.myfund.com:8484/Service/MyfundDemo.svc/GetHotfundList30?type=3";
		final String CEMETEEY_URL4 = "http://app.myfund.com:8484/Service/MyfundDemo.svc/GetHotfundList30?type=4";
		new Thread() {
			public void run() {
				String sendGet = HttpUtil.sendGet(CEMETEEY_URL);
				String sendGet3 = HttpUtil.sendGet(CEMETEEY_URL3);
				String sendGet4 = HttpUtil.sendGet(CEMETEEY_URL4);
				Message msg = Message.obtain();
				Message msg2 = Message.obtain();
				Message msg3 = Message.obtain();
				msg.what = 1;
				msg2.what = 2;
				msg3.what = 3;
				msg.obj = sendGet;
				msg2.obj = sendGet3;
				msg3.obj = sendGet4;
				handler.sendMessage(msg);
				handler.sendMessage(msg2);
				handler.sendMessage(msg3);
			};

		}.start();
	}

	@Override
	protected void onViewClick(View v) {

	}

	class CemeteryAdpter extends BaseAdapter {
		List<CemeteryResult> mList;

		public CemeteryAdpter(List<CemeteryResult> list) {
			this.mList = list;
			System.out.println("mList_-------------->" + mList);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("position+====================>" + position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(MyOptimize.this).inflate(
						R.layout.item_cemetery, null, false);
				holder = new ViewHolder();
				holder.tv_fund_code = (TextView) convertView
						.findViewById(R.id.tv_fund_code);
				holder.tv_fund_name = (TextView) convertView
						.findViewById(R.id.tv_fund_name);
				holder.tv_earnings = (TextView) convertView
						.findViewById(R.id.tv_earnings);
				holder.button_buy = (Button) convertView
						.findViewById(R.id.button_buy);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			res = mList.get(position);
			System.out.println("res+---------------->" + res);
			fundCode = res.getFundCode();
			holder.tv_fund_code.setText(fundCode);
			fundName = res.getFundName();
			holder.tv_fund_name.setText(fundName);
			holder.tv_earnings.setText(res.getOneYearRedound());
			holder.button_buy.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dealBuy();
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView tv_fund_code, tv_fund_name, tv_earnings, button_buy;
		}

	}

	public void dealBuy() {

		if (App.getContext().getSessionid() == null) {
			Intent intent = new Intent(getApplicationContext(),
					MyMeansActivity.class);

			intent.putExtra("gms", "true");
			startActivity(intent);
			return;
		} else {
			showProgressDialog("正在加载");
			RequestParams params = new RequestParams(getApplicationContext());
			params.put("sessionId", App.getContext().getSessionid());
			params.put("condition", res.getFundCode());
			params.put("fundType", null);
			params.put("company", null);
			execApi(ApiType.GET_DEALSEARCHONETWO, params,
					new OnDataReceivedListener() {

						private DealSearchResult res;

						@Override
						public void onReceiveData(ApiType api, String json) {
							System.out.println("json=-=-=-=>" + json);
							if (json == null) {
								showToast("请求失败");
								disMissDialog();
								return;
							}
							if (json != null && !json.equals("")) {
								tInputStringStream = new ByteArrayInputStream(
										json.getBytes());
								XmlPullParser parser = Xml.newPullParser();
								System.out.println("parser--------->" + parser);
								try {
									parser.setInput(tInputStringStream, "UTF-8");
									int event = parser.getEventType();
									while (event != XmlPullParser.END_DOCUMENT) {
										Log.i("start_document",
												"start_document");
										switch (event) {
										case XmlPullParser.START_TAG:
											if ("return".equals(parser
													.getName())) {
												String xmlReturn;
												try {
													xmlReturn = parser
															.nextText();
													System.out
															.println("<><><><><><><><><>"
																	+ xmlReturn);
													List<DealSearchResult> list = JSON
															.parseArray(
																	xmlReturn,
																	DealSearchResult.class);
													System.out
															.println("list=========>"
																	+ list);
													int size = list.size();
													System.out
															.println("list------>"
																	+ size);

													DealSearchResult res = list
															.get(0);
													Intent intent = new Intent(
															getApplicationContext(),
															DealApplyActivity.class);
													Bundle bundle = new Bundle();
													bundle.putString(
															"sessionId",
															App.getContext()
																	.getSessionid());
													bundle.putSerializable(
															"DealSearchResult",
															res);
													intent.putExtras(bundle);
													startActivity(intent);

												} catch (IOException e) {
													// TODO Auto-generated catch
													// block
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
					});
		}
	}
	public static class Utility {  
	    public static void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {  
	            // pre-condition  
	            return;  
	        }  
	  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        listView.setLayoutParams(params);  
	    }  
	} 

}
