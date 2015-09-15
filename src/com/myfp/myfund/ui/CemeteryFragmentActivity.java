package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CemeteryResult;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.FeatureResult;
import com.myfp.myfund.utils.HttpUtil;

public class CemeteryFragmentActivity extends BaseActivity {
	private LinearLayout bt_sell;
	private LinearLayout bt_inquire;
	private LinearLayout bt_optional;
	private ListView list_cemetery_view;
	private String uName;
	private String encodePassWord, idCard,password;
	private String fundCode, fundName;
	private CemeteryResult res;
	ByteArrayInputStream tInputStringStream = null;
	private DetailActivity activity;
	private String userName;
	
	private Handler handler=new Handler(){

		private List<CemeteryResult> results;

		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case 1:
				String vale=(String) msg.obj;
				System.out.println("Vale===============>"+vale);
				results = JSON.parseArray(vale, CemeteryResult.class);
				System.out.println("results-------------->"+results);
				list_cemetery_view.setAdapter(new CemeteryAdpter(results));
				list_cemetery_view.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
							CemeteryResult res = results.get(position);
							Intent intent = new Intent(CemeteryFragmentActivity.this,DetailActivity.class);
							intent.putExtra("sessionId", App.getContext().getSessionid());
							intent.putExtra("fundName",res.getFundName().trim());
							intent.putExtra("fundCode",res.getFundCode().trim());
							//showProgressDialog("正在加载");
							startActivity(intent);
					}
				});
				
				disMissDialog();
			}
		};
	};
	
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_cemetery_fund);
		bt_sell = (LinearLayout) findViewById(R.id.bt_sell);
		bt_sell.setBackgroundColor(Color.RED);
		bt_inquire = (LinearLayout) findViewById(R.id.bt_inquire);
		bt_optional = (LinearLayout) findViewById(R.id.bt_optional);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		password=preferences.getString("password", null);
		
		Intent intent = getIntent();
		fundName = intent.getStringExtra("fundName");
		System.out.println("fundName=-=-=-=-=?>"+fundName);
		fundCode = intent.getStringExtra("fundCode");
		System.out.println("fundCode===========>"+fundCode);
		userName = App.getContext().getUserName();
		
		
		System.out.println("idCard===========>"+idCard);
		list_cemetery_view = (ListView) findViewById(R.id.list_cemetery_view);
		bt_inquire.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(CemeteryFragmentActivity.this, QueryFundActivity.class);
				intent.putExtra("sessionId",  App.getContext().getSessionid());
				startActivity(intent);
				
				
			}
		});
		
		
		bt_optional.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if( App.getContext().getSessionid()==null){
					Intent intent7=new Intent(CemeteryFragmentActivity.this, MyMeansActivity.class);
					intent7.putExtra("Flag", "true");
					
					startActivity(intent7);
				}else{
					Intent intent5=new Intent(CemeteryFragmentActivity.this, FundSelectActivity.class);
					intent5.putExtra("sessionId", App.getContext().getSessionid());
					intent5.putExtra("UserName", uName);
					
					startActivity(intent5);
				}
			}
		});
		
		showProgressDialog("正在加载");
		final String CEMETEEY_URL="http://app.myfund.com:8585/Service/CLFDemo.svc/GetHotfundList";
		new Thread(){
			public void run() {
			 String sendGet = HttpUtil.sendGet(CEMETEEY_URL);
			 Message msg = Message.obtain();
			 msg.what=1;
			 msg.obj=sendGet;
			 handler.sendMessage(msg);
				
			};
			
		}.start();
		
	}

	@Override
	protected void initViews() {
		setTitle("公募基金");
		findViewAddListener(R.id.ll_top_layout_right_view).setVisibility(View.VISIBLE);
	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			//搜索
			startActivity(SearchActivity.class);
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		super.onReceiveData(api, json);
	}
	class CemeteryAdpter extends BaseAdapter{
		List<CemeteryResult> mList;
		
		
		
		public CemeteryAdpter(List<CemeteryResult> list){
			this.mList=list;
			System.out.println("mList_-------------->"+mList);
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
			System.out.println("position+====================>"+position);
			ViewHolder holder;
			if (convertView==null) {
				convertView = LayoutInflater.from(CemeteryFragmentActivity.this).inflate(R.layout.item_cemetery, null,false);
				holder=new ViewHolder();
				holder.tv_fund_code =(TextView) convertView.findViewById(R.id.tv_fund_code);
				holder.tv_fund_name = (TextView) convertView.findViewById(R.id.tv_fund_name);
				holder.tv_earnings = (TextView) convertView.findViewById(R.id.tv_earnings);
				holder.button_buy = (Button) convertView.findViewById(R.id.button_buy);
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			 res = mList.get(position);
			System.out.println("res+---------------->"+res);
			fundCode = res.getFundCode();
			holder.tv_fund_code.setText(fundCode);
			fundName= res.getFundName();
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
	class ViewHolder{
		TextView tv_fund_code,tv_fund_name,tv_earnings,button_buy;
	}
		
	}
	
	
	public void dealBuy(){
		encodePassWord = App.getContext().getEncodePassWord();
		idCard = App.getContext().getIdCard();

		if ( App.getContext().getSessionid()==null) {
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
			execApi(ApiType.GET_DEALSEARCHONETWO, params,new OnDataReceivedListener() {

						private DealSearchResult res;

						@Override
						public void onReceiveData(ApiType api,
								String json) {
							System.out.println("json=-=-=-=>"+json);
							if (json == null) {
								showToast("请求失败");
								disMissDialog();
								return;
							}
							if (json != null && !json.equals("")) {
								tInputStringStream = new ByteArrayInputStream(json.getBytes());
								XmlPullParser parser = Xml.newPullParser();
								System.out.println("parser--------->"+parser);
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
													List<DealSearchResult> list = JSON.parseArray(xmlReturn,DealSearchResult.class);
													System.out.println("list=========>"+list);
													int size = list.size();
													System.out.println("list------>"+size);
													
													DealSearchResult res = list.get(0);
													Intent intent = new Intent(getApplicationContext(),
															DealApplyActivity.class);
													Bundle bundle = new Bundle();
													bundle.putString("sessionId", App.getContext().getSessionid());
													bundle.putSerializable("DealSearchResult", res);
													intent.putExtras(bundle);
													startActivity(intent);
													
											

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
					});
		}
	}
	

}
