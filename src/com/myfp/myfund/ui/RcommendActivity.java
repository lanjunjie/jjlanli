package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.TuiJian2;

public class RcommendActivity extends BaseActivity {
	private ListView recommend;
	private List<TuiJian2> list;
	private String fundCode, fundName;
	private String iamgeurl;
	private String encodePassWord, idCard;
	ByteArrayInputStream tInputStringStream = null;
	private TuiJian2 tuiJian2;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_rcommend);
		RequestParams params = new RequestParams(this);

		execApi(ApiType.GET_HOTFUNDLIST2, params);

	}

	@Override
	protected void initViews() {
		setTitle("推荐");
		recommend = (ListView) findViewById(R.id.recommendlv);
		

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.recommendlv:

			break;

		default:
			break;
		}

	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("json========>"+json);
		if (json == null) {
			disMissDialog();
			showToast("获取失败!");
			return;
		}
		if(api==ApiType.GET_HOTFUNDLIST2){
	

				final List<TuiJian2> res = JSON.parseArray(json, TuiJian2.class);
				recommend.setAdapter(new MyAdapter(this, res));
				recommend.setOnItemClickListener(new OnItemClickListener() {

					

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						tuiJian2 = res.get(arg2);
					Intent intent=new Intent(RcommendActivity.this, DetailActivity.class);
					intent.putExtra("sessionId", App.getContext().getSessionid());
					intent.putExtra("fundName", tuiJian2.getFundName());
					intent.putExtra("fundCode", tuiJian2.getFundCode());
					startActivity(intent);
						//dealBuy();
						
					}
				});
		
		}
		
	}
	class MyAdapter extends BaseAdapter {
		private List<TuiJian2> list;
		private LayoutInflater mInflater;
	
//		private NetworkImageView image;
		public MyAdapter(Context context,List<TuiJian2> mlist){
			this.list=mlist;
			this.mInflater=LayoutInflater.from(context);
			
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
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
				if(convertView==null){
					convertView=mInflater.inflate(R.layout.item_rcommend, null);
//					image=(NetworkImageView) convertView.findViewById(R.id.imagerecmmend);
					holder.fundname=(TextView) convertView.findViewById(R.id.fundname);
					holder.oneyearredound=(TextView) convertView.findViewById(R.id.oneyearredound);
					holder.recommendreason=(TextView) convertView.findViewById(R.id.recommendreason);
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				TuiJian2 res = list.get(position);
				fundCode=res.getFundCode().trim();
				fundName=res.getFundName().trim();
				holder.fundname.setText(res.getFundName()+"("+res.getFundCode()+")"+res.getFundType());
				holder.oneyearredound.setText(res.getThisYearRedound());
				holder.recommendreason.setText(res.getRecommendReason());
				
			return convertView;
			
		}
			class ViewHolder{

				public TextView fundname,oneyearredound,recommendreason;
			}
	}
	class ViewHolder{
		TextView tv_fund_code,tv_fund_name,tv_earnings,button_buy;
	}
		
	
	
	
	public void dealBuy(){
		encodePassWord = App.getContext().getEncodePassWord();
		idCard = App.getContext().getIdCard();

		System.out.println("++++++++++++++++++++++++" + idCard);
		if (encodePassWord == null) {
			Intent intent = new Intent(getApplicationContext(),
					DetailActivity.class);
			intent.putExtra("fundCode",tuiJian2.getFundCode());
			intent.putExtra("fundName", tuiJian2.getFundName());
			intent.putExtra("sessionId", getIntent().getStringExtra("sessionId"));
			startActivity(intent);
			return;
		} else {
			showProgressDialog("正在加载");
			RequestParams params = new RequestParams(getApplicationContext());
			//params.put("id", idCard);
			//params.put("passwd", encodePassWord);
			params.put("sessionId", getIntent().getStringExtra("sessionId"));
			params.put("condition", fundCode);
			params.put("fundType", null);
			params.put("company", null);
			execApi(ApiType.GET_DEALSEARCHTWO, params,new OnDataReceivedListener() {

						private DealSearchResult res1;

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
													//bundle.putString("IDCard", idCard);
													bundle.putString("PassWord", encodePassWord);
													bundle.putString("sessionId", getIntent().getStringExtra("sessionId"));
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



