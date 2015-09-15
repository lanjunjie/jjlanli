package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.FeatureHardResult;
import com.myfp.myfund.api.beans.SolidChargeResult;
import com.myfp.myfund.ui.MySolidChargeFragment.MySolidChargeAdapter;
import com.myfp.myfund.ui.MySolidChargeFragment.MySolidChargeAdapter.ViewHolder;

public class MyFixationActivity extends BaseActivity{
	/*	private FragmentManager fmr = getSupportFragmentManager();
	private FragmentTransaction fragmentTran = getSupportFragmentManager().beginTransaction();
	
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fixation_fragment);
		fragmentTran.replace(R.id.fragmentma, new MyFixationFragment());
		
		fragmentTran.commit();
	}

	@Override
	protected void initViews() {
		setTitle("我的资产--固收产品");
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
*/
	private TextView tex_feature_titl;
	private LinearLayout lin_feature_tis;
	private TextView tex_feature_rmd;
	private ListView list_feature_sik;
	private ListView list_feature_have;
	ByteArrayInputStream tInputStringStream = null;
	private List<FeatureHardResult> results;
	private List<SolidChargeResult> resul;
	private Button right;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_feature_firm);
		RequestParams params=new RequestParams(this);
		params.put("sdzjnumber".trim(), App.getContext().getIdCard().trim());
		execApi(ApiType.GET_SOLIDCHARGE,params,this);
	}

	@Override
	protected void initViews() {
		setTitle("特色固收");
		tex_feature_titl = (TextView) findViewById(R.id.tex_feature_titl);
		lin_feature_tis = (LinearLayout) findViewById(R.id.lin_feature_tis);
		tex_feature_rmd = (TextView) findViewById(R.id.tex_feature_rmd);
		list_feature_sik = (ListView) findViewById(R.id.list_feature_sik);
		list_feature_have = (ListView) findViewById(R.id.list_feature_have);
		list_feature_sik.setVisibility(View.GONE);
		list_feature_have.setVisibility(View.GONE);
		tex_feature_rmd.setVisibility(View.GONE);
		lin_feature_tis.setVisibility(View.GONE);
		tex_feature_titl.setVisibility(View.GONE);
		right=(Button) findViewById(R.id.tv_text_main_publish);
		right.setText("交易记录");
		right.setVisibility(View.VISIBLE);
		right.setTextSize(18);
		right.getBackground().setAlpha(0);
		right.setOnClickListener(this);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_text_main_publish:
			Intent intent=new Intent(this, MyFixedDealRecord.class);
			intent.putExtra("idcard", App.getContext().getIdCard().trim());
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);
		if (api==ApiType.GET_SOLIDCHARGE) {
			if (json.equals("[]")) {
				tex_feature_titl.setVisibility(View.VISIBLE);
				tex_feature_rmd.setVisibility(View.VISIBLE);
				list_feature_sik.setVisibility(View.VISIBLE);
				lin_feature_tis.setVisibility(View.VISIBLE);
				RequestParams pas=new RequestParams(this);
				RequestParams pms=new RequestParams(this);
				pas.put("procode", "hdl");
				pms.put("procode", "hyc");
				execApi(ApiType.GET_FEATUREPRMHDL, pas);
				execApi(ApiType.GET_FEATUREPRMHYC, pms);
				return;
			}else {
				tex_feature_titl.setVisibility(View.VISIBLE);
				list_feature_have.setVisibility(View.VISIBLE);
				resul = JSON.parseArray(json, SolidChargeResult.class);
				
				list_feature_have.setAdapter(new myfsinkeatureadapter(resul,this));
				disMissDialog();
			}
		}else if (api==ApiType.GET_FEATUREPRMHDL) {
			
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
								try {
									String xmlReturn = parser.nextText();
									results = JSON.parseArray(xmlReturn, FeatureHardResult.class);
									list_feature_sik.setAdapter(new myfeatureadapter(results));

								} catch (IOException e) {
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

				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			
			
		}else if (api==ApiType.GET_FEATUREPRMHYC) {
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
								try {
									String xmlReturn = parser.nextText();
									results = JSON.parseArray(xmlReturn, FeatureHardResult.class);
									list_feature_sik.setAdapter(new myfeatureadapter(results));

								} catch (IOException e) {
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

				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}
		
		
		}
	
	class myfeatureadapter extends BaseAdapter{
		List<FeatureHardResult> mList;
		
		public myfeatureadapter(List<FeatureHardResult> list){
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
			ViewHolder holder = null;
			

			if (convertView==null) {
				holder=new ViewHolder();
					convertView = LayoutInflater.from(MyFixationActivity.this).inflate(R.layout.item_feature_firm, null);
					
					System.out.println("convertView=-=-=-=>"+convertView);
					holder.tex_feature_head = (TextView) convertView.findViewById(R.id.tex_feature_head);
					holder.tex_feature_money = (TextView) convertView.findViewById(R.id.tex_feature_money);
					holder.tex_feature_sky = (TextView) convertView.findViewById(R.id.tex_feature_sky);
					holder.tex_feature_year = (TextView) convertView.findViewById(R.id.tex_feature_year);
					convertView.setTag(holder);
				
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			FeatureHardResult res = mList.get(position);
			holder.tex_feature_head.setText(res.getSname());
			holder.tex_feature_money.setText(res.getSdlowlimit());
			holder.tex_feature_sky.setText(res.getTerm()+"天");
			holder.tex_feature_year.setText(res.getSmodel());
			return convertView;
			
		}
		class ViewHolder{
			TextView tex_feature_head,tex_feature_money,tex_feature_sky,tex_feature_year;
		}
		
	}

}
