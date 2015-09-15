package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.R.id;
import com.myfp.myfund.R.layout;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.TheCurrentProduct;

public class FeatureFixedActivity extends BaseActivity {
	private List<TheCurrentProduct> results2;
	private List<TheCurrentProduct> results1;
	private List<TheCurrentProduct> results;
	private ListView lv_fixed_tuijian,lv_fixed_tuijian1;
	private TextView tx_dealphone1;
	private String abc;
	ByteArrayInputStream tInputStringStream = null;
	LayoutInflater layoutInflater;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_feature_fixed);
		Intent intent = getIntent();
		String idcard = intent.getStringExtra("idcard");
		RequestParams params = new RequestParams(this);
		params.put("procode", "hdl");
		execApi(ApiType.GET_FEATUREPRMHDL, params, this);
		RequestParams params2 = new RequestParams(this);
		params2.put("procode", "hyc");
		execApi(ApiType.GET_FEATUREPRMHYC, params2, this);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		setTitle("特色固收");
		lv_fixed_tuijian = (ListView) findViewById(R.id.lv_fixed_tuijian);
		lv_fixed_tuijian1 = (ListView) findViewById(R.id.lv_fixed_tuijian1);
		findViewAddListener(R.id.tx_dealphone1);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>" + json);

		if (json.equals("")) {

			disMissDialog();
			return;
		}
		if (api==ApiType.GET_FEATUREPRMHDL||api==ApiType.GET_FEATUREPRMHYC) {
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
								if(api==ApiType.GET_FEATUREPRMHDL){
									results1=JSON.parseArray(xmlReturn,
														TheCurrentProduct.class);
									
									lv_fixed_tuijian
									.setAdapter(new MyPrivatePurchaseRecordsAdapter(
											results1));
									Utility.setListViewHeightBasedOnChildren(lv_fixed_tuijian);
									lv_fixed_tuijian.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											TheCurrentProduct rest = results1.get(arg2);
											Intent intent =new Intent(FeatureFixedActivity.this,FeatureDeatailedActivity.class);
											intent.putExtra("Term", rest.getTerm());
											intent.putExtra("Sdlowlimit", rest.getSdlowlimit());
											intent.putExtra("Productscale", rest.getProductscale());
											intent.putExtra("Id", rest.getId());
											intent.putExtra("Smodel", rest.getSmodel());
											intent.putExtra("Sspec", rest.getSspec());
											intent.putExtra("Sdstartdate", rest.getSdstartdate());
											intent.putExtra("Sdoverdate", rest.getSdoverdate());
											intent.putExtra("Security", rest.getSecurity());
											intent.putExtra("Dalaceway", rest.getBalaceway());
											intent.putExtra("Sname", rest.getSname());
											intent.putExtra("form", "5");
											startActivity(intent);
											
										}
										
									});
								}if(api==ApiType.GET_FEATUREPRMHYC){
									results2=JSON.parseArray(xmlReturn,
													TheCurrentProduct.class);

									lv_fixed_tuijian1
									.setAdapter(new MyPrivatePurchaseRecordsAdapter(
											results2));
									Utility.setListViewHeightBasedOnChildren(lv_fixed_tuijian1);
									lv_fixed_tuijian1.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											TheCurrentProduct res = results2.get(arg2);
											Intent intent =new Intent(FeatureFixedActivity.this,FeatureDeatailedActivity.class);
											intent.putExtra("Term", res.getTerm());
											intent.putExtra("Sdlowlimit", res.getSdlowlimit());
											intent.putExtra("Productscale", res.getProductscale());
											intent.putExtra("Id", res.getId());
											intent.putExtra("Smodel", res.getSmodel());
											intent.putExtra("Sspec", res.getSspec());
											intent.putExtra("Sdstartdate", res.getSdstartdate());
											intent.putExtra("Sdoverdate", res.getSdoverdate());
											intent.putExtra("Security", res.getSecurity());
											intent.putExtra("Dalaceway", res.getBalaceway());
											intent.putExtra("Sname", res.getSname());
											intent.putExtra("form", "6");
											startActivity(intent);
											
										}
										
									});
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

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tx_dealphone1:
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_DIAL);
			intent1.setData(Uri.parse("tel:010-56236260"));
			startActivity(intent1);
			break;

		default:
			break;
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
	class MyPrivatePurchaseRecordsAdapter extends BaseAdapter {
		List<TheCurrentProduct> mList;

		public MyPrivatePurchaseRecordsAdapter(List<TheCurrentProduct> list) {
			this.mList = list;
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
			if (convertView == null) {
				System.out.println("44444444");
				holder = new ViewHolder();
				convertView = LayoutInflater.from(FeatureFixedActivity.this)
						.inflate(R.layout.activity_fixed_item, null);
				holder.tx_fixed_name = (TextView) convertView
						.findViewById(R.id.tx_fixed_name);
				holder.tx_fixed_xiane = (TextView) convertView
						.findViewById(R.id.tx_fixed_xiane);
				holder.tx_fixed_qixian = (TextView) convertView
						.findViewById(R.id.tx_fixed_qixian);
				holder.tx_fixed_shouyi = (TextView) convertView
						.findViewById(R.id.tx_fixed_shouyi);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			TheCurrentProduct res = mList.get(position);
			holder.tx_fixed_name.setText(res.getSname());
			holder.tx_fixed_xiane.setText(res.getSdlowlimit()+"万");
			holder.tx_fixed_qixian.setText(res.getTerm()+"天");
			holder.tx_fixed_shouyi.setText(res.getSmodel());

			return convertView;
		}

		class ViewHolder {
			TextView tx_fixed_name, tx_fixed_xiane, tx_fixed_qixian,
					tx_fixed_shouyi;
		}

	}

}
