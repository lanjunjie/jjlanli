package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.ButlerTongResult;

public class ButlerTongActivity extends BaseActivity{

	private TextView tev_your_account;
	private TextView tev_entrusted_management;
	private TextView tev_assetin_place;
	private TextView tev_float_profitand;
	private TextView tev_commissioned_during;
	private TextView tev_membership_drive;
	private ListView lsit_butler_tong;
	private String idcard;
	private String xmlReturn;
	ByteArrayInputStream tInputStringStream = null;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_butlertong);
		Intent intent = getIntent();
		idcard = intent.getStringExtra("idcard");
		RequestParams params=new RequestParams(this);
		params.put("idcard", idcard);
		
		execApi(ApiType.GET_BUTLERTONG, params);
		RequestParams par=new RequestParams(this);
		par.put("sdzjnumber", idcard);
		execApi(ApiType.GET_THEHOUSEKEEPERMEMBERSHIP, par);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		setTitle("我的资产--管家通");
		tev_your_account = (TextView) findViewById(R.id.tev_your_account);
		tev_entrusted_management = (TextView) findViewById(R.id.tev_entrusted_management);
		tev_assetin_place = (TextView) findViewById(R.id.tev_assetin_place);
		tev_float_profitand = (TextView) findViewById(R.id.tev_float_profitand);
		tev_commissioned_during = (TextView) findViewById(R.id.tev_commissioned_during);
		tev_membership_drive = (TextView) findViewById(R.id.tev_membership_drive);
		lsit_butler_tong = (ListView) findViewById(R.id.lsit_butler_tong);
		
		
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json==null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api==ApiType.GET_THEHOUSEKEEPERMEMBERSHIP) {
			if (json!=null&&!json.equals("")) {
				try {
					System.out.println("json===========>"+json);
					JSONArray jsArray=new JSONArray(json);
					System.out.println("jsArray=======----->"+jsArray);
					for (int i = 0; i < jsArray.length(); i++) {
						JSONObject object = jsArray.getJSONObject(i);
						System.out.println("object----------->"+object);
						String Beginasset = object.getString("Beginasset").substring(0, object.getString("Beginasset").indexOf("."));
						tev_your_account.setText(Beginasset+"元");
						String Manageprice = object.getString("Manageprice").substring(0, object.getString("Manageprice").indexOf("."));
						tev_entrusted_management.setText(Manageprice+"元");
						String Currentcountasset = object.getString("Currentcountasset").substring(0, object.getString("Currentcountasset").indexOf("."));
						tev_assetin_place.setText(Currentcountasset+"元");
						String Floatinterestrate = object.getString("Floatinterestrate").substring(0, object.getString("Floatinterestrate").indexOf("."));
						tev_float_profitand.setText(Floatinterestrate+"元");
						String Periodinterestrate = object.getString("Periodinterestrate").substring(0, object.getString("Periodinterestrate").indexOf("."));
						tev_commissioned_during.setText(Periodinterestrate+"%");
						String data = object.getString("Memberbegintoend").split(" ")[0];
						String Memberbegintoend = data.replaceAll("/", "/");
						String dat = object.getString("Memberend").split(" ")[0];
						String Memberend = dat.replaceAll("/", "/");
						tev_membership_drive.setText(Memberbegintoend+"-"+Memberend);
					}  
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}else 
		if (api==ApiType.GET_BUTLERTONG) {
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
									xmlReturn = parser.nextText();
									System.out.println("--------------->"+xmlReturn);
									List<ButlerTongResult> results = JSON.parseArray(xmlReturn, ButlerTongResult.class);
									lsit_butler_tong.setAdapter(new ButlerTongAdapter(results));
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
		}
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	class ButlerTongAdapter extends BaseAdapter{
		 List<ButlerTongResult> mList;
		public ButlerTongAdapter(List<ButlerTongResult> list){
			this.mList=list;
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
			ViewHolder holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView= LayoutInflater.from(ButlerTongActivity.this).inflate(R.layout.item_butlertong, null);
				holder.te_word_xt = (TextView) convertView.findViewById(R.id.te_word_xt);
				holder.te_name_xt = (TextView) convertView.findViewById(R.id.te_name_xt);
				holder.te_market_value = (TextView) convertView.findViewById(R.id.te_market_value);
				holder.te_profit_and = (TextView) convertView.findViewById(R.id.te_profit_and);
				holder.te_earnings_xt = (TextView) convertView.findViewById(R.id.te_earnings_xt);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			 ButlerTongResult res = mList.get(position);
			holder.te_word_xt.setText(res.getFundcode());
			holder.te_name_xt.setText(res.getFundname());
			holder.te_market_value.setText(res.getFundmarketvalueandincome());
			DecimalFormat df=new DecimalFormat("#0.##");
			double floatprof=Double.parseDouble(res.getFloatprofit());
			String Floatprofit = df.format(floatprof);
			 if (Floatprofit.startsWith("-")) {
				holder.te_profit_and.setTextColor(Color.GREEN);
				 holder.te_profit_and.setText(Floatprofit);
			}else {
				holder.te_profit_and.setTextColor(Color.RED);
				 holder.te_profit_and.setText(Floatprofit);
			}
			String format = String.format("%.2f",Double.parseDouble(res.getAddincomerate()));
			if (format.startsWith("-")) {
				holder.te_earnings_xt.setTextColor(Color.GREEN);
				holder.te_earnings_xt.setText(format+"%");
			}else {
				holder.te_earnings_xt.setTextColor(Color.RED);
				holder.te_earnings_xt.setText(format+"%");
			}
			System.out.println("format==========>"+format);
			return convertView;
		}
		class ViewHolder{
			TextView te_word_xt,te_name_xt,te_market_value,te_profit_and,te_earnings_xt;
		}
		
	}

}
