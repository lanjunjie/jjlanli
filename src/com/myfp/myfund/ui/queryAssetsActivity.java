package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CancellationResult;
import com.myfp.myfund.api.beans.queryAssetsResult;
import com.myfp.myfund.ui.DealCancellationActivity.CancellationAdapter;

public class queryAssetsActivity extends BaseActivity{
	private String encodeIdCard, encodePassWord;
	private RequestParams params;
	private ListView list_inquire_Dividend;
	private List<queryAssetsResult> results;
	private Button bt_change;
	ByteArrayInputStream tInputStringStream = null;
	public static queryAssetsActivity instance=null;
	private String sessionId;
	
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_changebonus);
		instance=this;
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		sessionId = intent.getStringExtra("sessionId");
		params = new RequestParams(this);
		params.put("sessionId", sessionId);
		//params.put("idcard", encodeIdCard);
		// params.put(RequestParams.MOBILE, username);
		//params.put("passwd", encodePassWord);
		params.put("businessflag", 'a');
		execApi(ApiType.GET_INQUIRY_FUNDTWO, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		setTitle("分红方式变更");
		list_inquire_Dividend=(ListView) findViewById(R.id.list_inquire_Dividend);
	}

	@Override
	protected void onViewClick(View v) {
		
	}
	class queryAssetsAdapter extends BaseAdapter{
		private List<queryAssetsResult> mList;
		private Button bt_change;
		public queryAssetsAdapter(List<queryAssetsResult> list){
			this.mList=list;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			convertView = LayoutInflater.from(queryAssetsActivity.this)
					.inflate(R.layout.item_changebonus, null, false);
			holder = new ViewHolder();

			holder.tv_code = (TextView) convertView
					.findViewById(R.id.tv_code);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_manner = (TextView) convertView
					.findViewById(R.id.tv_manner);
			holder.bt_change = (Button) convertView
					.findViewById(R.id.bt_change);

			convertView.setTag(holder);
			final queryAssetsResult res = mList.get(position);
			holder.tv_code.setText(res.getFundcode());
			holder.tv_name.setText(res.getFundname());
			switch (Integer.parseInt(res.getDefdividendmethod())) {
			case 0:
				holder.tv_manner.setText("红利再投");
				break;
			case 1:
				holder.tv_manner.setText("现金分红");
				break;
			default:
				break;
			}
			if (res.getFundtype().equals("2")) {
				holder.bt_change.setEnabled(false);
				holder.bt_change.setBackgroundColor(Color.GRAY);
			}else {
				holder.bt_change
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								queryAssetsActivity.this,
								AlterMmodeChangeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("sessionId", sessionId);
						//bundle.putString("IDCard", encodeIdCard);
						//bundle.putString("PassWord", encodePassWord);
						bundle.putSerializable("queryAssetsResult",res);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				}); 
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_code, tv_name,
			tv_manner;
			Button bt_change;
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
								System.out.println("<-=-><-=-><-=-><-=-><-=-><-=-><-=-><-=-><-=->"
										+ xmlReturn);
								results = JSON.parseArray(xmlReturn,queryAssetsResult.class);
								System.out.println("results=============-------->"+results);
								if (results.equals("")||results==null) {
									showToastLong("您还没有分红设置！");
									disMissDialog();
								}else {
									list_inquire_Dividend
									.setAdapter(new queryAssetsAdapter(results));
									
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

	

