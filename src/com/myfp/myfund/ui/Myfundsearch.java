package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R.drawable;
import com.myfp.myfund.R.id;
import com.myfp.myfund.R.layout;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CemeteryResult;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.SearchResult;
import com.myfp.myfund.utils.HttpUtil;

public class Myfundsearch extends BaseActivity implements OnDataReceivedListener2{
	private EditText et_search;
	private List<SearchResult> results;
	private ListView lv_searchResult;
	private String userName,sessionId;
	private Button tv_chaxun;
	private LinearLayout lv_list,lv_rexiao;
	private ListView list_cemetery_view;
	private CemeteryResult res;
	private String fundCode, fundName,flag;
	ByteArrayInputStream tInputStringStream = null;
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
							Intent intent = new Intent(Myfundsearch.this,DetailActivity.class);
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
		setContentView(R.layout.activity_myfundsearch);
	Intent intent =getIntent();
	  flag=intent.getStringExtra("flag");
		userName=App.getContext().getUserName();
		sessionId=App.getContext().getSessionid();
        /**  
       * 用一个定时器控制当打开这个Activity的时候就出现软键盘  
       */   
      Timer timer=new Timer();  
      timer.schedule(new TimerTask() {  
           @Override   
           public void run() {  
              InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
              inputMethodManager.toggleSoftInput( 0 , InputMethodManager.SHOW_FORCED);  
          }  
      },  1000);  
   
	}

	@Override
	protected void initViews() {
		setTitle("基金搜索");
		et_search=(EditText) findViewById(R.id.et_addFund_search1);
//		et_search.requestFocus();
//		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
//		// 接受软键盘输入的编辑文本或其它视图 
//		inputMethodManager.showSoftInput(et_search, InputMethodManager.SHOW_FORCED); 
		lv_searchResult = (ListView) findViewById(R.id.listView_addFund_searchList1);	
		list_cemetery_view=(ListView) findViewById(R.id.list_cemetery_view1);
		lv_list=(LinearLayout) findViewById(R.id.layout_add_resultList1);
		lv_rexiao=(LinearLayout) findViewById(R.id.layout_add_rexiao);
		tv_chaxun=(Button) findViewById(R.id.tv_chaxun);
		findViewAddListener(R.id.tv_chaxun);
		findViewAddListener(R.id.img_addFund_delete1);
		System.out.println("et_search.getText().toString()"+et_search.getText().toString());
		if(flag.equals("1")){
			lv_rexiao.setVisibility(View.GONE);
		
		}
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
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_chaxun:
			RequestParams params = new RequestParams(this);
			if(et_search.getText().toString()==null||et_search.getText().toString().length()==0){
				
			showToast("您还没有输入基金代码");
			}else{
			
			params.put("InputFundPartValue", et_search.getText().toString());
			params.put("UserName", userName);
			execApi(ApiType.SEARCH_FUND, params);
			showProgressDialog("正在搜索");
			}
			break;
		case R.id.img_addFund_delete1:
			et_search.setText(null);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onReceiveData(ApiType api, RequestParams params, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.SEARCH_FUND) {
			lv_rexiao.setVisibility(View.GONE);
			lv_list.setVisibility(View.VISIBLE);
			
			results = JSON.parseArray(json, SearchResult.class);
			if (results.size() == 0) {
				disMissDialog();
				showToast("没有符合条件的基金！");
				
				return;
			}
			lv_searchResult.setAdapter(new AddSearchResultAdapter(results));
			lv_searchResult.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					SearchResult res = results.get(position);
					Intent intent = new Intent(Myfundsearch.this,DetailActivity.class);
					intent.putExtra("sessionId", sessionId);
					intent.putExtra("fundName", res.getFundName().trim());
					intent.putExtra("fundCode", res.getFundCode().trim());
					startActivity(intent);
				}
			});
			disMissDialog();
		
	}
	}
		class AddSearchResultAdapter extends BaseAdapter{
			
			private List<SearchResult> list;
			
			public AddSearchResultAdapter(List<SearchResult> list){
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
				final ViewHolder holder;
				final SearchResult res = list.get(position);
				if (convertView == null) {
					convertView = LayoutInflater.from(Myfundsearch.this).inflate(R.layout.item_search_result, null, false);
					holder = new ViewHolder();
					
					holder.tv_fundName = (TextView) convertView.findViewById(R.id.tv_searchResult_fundName);
					holder.tv_fundCode = (TextView) convertView.findViewById(R.id.tv_searchResult_fundCode);
					holder.tv_fundtype=(TextView) convertView.findViewById(R.id.tv_searchResult_fundType);
					holder.img_isSelected = (ImageView) convertView.findViewById(R.id.img_search_isSelected);
				
					convertView.setTag(holder);
				}else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.tv_fundName.setText(res.getFundName());
				holder.tv_fundCode.setText(res.getFundCode());
				holder.tv_fundtype.setText(res.getFundType());
				if (res.getIsFlag() != null && res.getIsFlag().equals("1")) {
					holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
				}
				holder.img_isSelected.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						RequestParams params = new RequestParams(Myfundsearch.this);
						params.put("UserName", userName);
						params.put("FundCode", res.getFundCode().trim());
						params.put("FundName", res.getFundName().trim());
						execApi(ApiType.GET_MY_FUND_CENTER, params, new OnDataReceivedListener() {
							
							@Override
							public void onReceiveData(ApiType api, String json) {
								if(json==null){
									showToast("请求失败!");
									return;
								}
								try {
									JSONArray array = new JSONArray(json);
									if(api == ApiType.GET_MY_FUND_CENTER){
										int returnResult = array.getJSONObject(0).getInt("ReturnResult");
										switch (returnResult) {
										case 0:
											showToast("添加成功!");
											holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
											break;
										case 1:
											showToast("用户名不能为空!");
											break;
										case 2:
											showToast("取消自选!");
											holder.img_isSelected.setImageResource(R.drawable.red_star_hollow_big);
											break;
										case 3:
											showToast("添加成功!");
											holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
											break;
										case 4:
											showToast("添加失败!");
											break;
										case 5:
											showToast("系统参数错误!");
											break;	

										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
					
				});
				
				
				return convertView;
			}
			class ViewHolder{
				TextView tv_fundName,tv_fundCode,tv_fundtype;
				ImageView img_isSelected;
			}
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
					convertView = LayoutInflater.from(Myfundsearch.this).inflate(R.layout.item_cemetery, null,false);
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
