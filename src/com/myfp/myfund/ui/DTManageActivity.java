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
import com.myfp.myfund.api.beans.DTManageSearchResult;

public class DTManageActivity extends BaseActivity{
	private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	private List<DTManageSearchResult> results;
	ListView list_dtmanagelist;
	public static DTManageActivity instance = null;
	private String sessionId;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dtmanage);
		instance = this;
		Intent intent = getIntent();
		sessionId = intent.getStringExtra("sessionId");
		RequestParams params = new RequestParams(this);

		params.put("sessionId", sessionId);
		params.put("Saveplanflag", "1");
		execApi(ApiType.GET_DTMANAGETWO, params);
		showProgressDialog("正在加载");
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("定期投资查询和终止");
		list_dtmanagelist = (ListView) findViewById(R.id.list_dtmanage_list);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	class DTManageSearchAdapter extends BaseAdapter {

		private List<DTManageSearchResult> list;

		public DTManageSearchAdapter(List<DTManageSearchResult> list) {
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

			convertView = LayoutInflater.from(DTManageActivity.this).inflate(
					R.layout.item_dtmanagelist, null, false);
			holder = new ViewHolder();

			holder.tv_dtmanage_fundName = (TextView) convertView
					.findViewById(R.id.tv_dtmanage_fundname);
			holder.tv_dtmanage_firstpaydate = (TextView) convertView
					.findViewById(R.id.tv_dtmanage_firstpaydate);
			holder.tv_dtmanage_cycle = (TextView) convertView
					.findViewById(R.id.tv_dtmanage_cycle);
			holder.bt_dtmanage_stop = (Button) convertView
					.findViewById(R.id.bt_dtmanage_stop);
			

			convertView.setTag(holder);
			
			final DTManageSearchResult res = list.get(position);
			holder.tv_dtmanage_fundName.setText(res.getFundname());
			holder.tv_dtmanage_firstpaydate.setText(res.getFirstinvestdate());

			holder.tv_dtmanage_cycle.setText(res.getPeriodremark());
		
			if (res.getStatus().equals("X")) {
				holder.bt_dtmanage_stop.setText("未知");
				holder.bt_dtmanage_stop.setEnabled(false);
				holder.bt_dtmanage_stop.setBackgroundColor(Color.GRAY);
			}
			else if (res.getStatus().equals("W")) {
				holder.bt_dtmanage_stop.setText("待确认");
				holder.bt_dtmanage_stop.setEnabled(false);
				holder.bt_dtmanage_stop.setBackgroundColor(Color.GRAY);
			}
			else if (res.getStatus().equals("P")) {
				holder.bt_dtmanage_stop.setText("暂停");
				holder.bt_dtmanage_stop.setEnabled(false);
				holder.bt_dtmanage_stop.setBackgroundColor(Color.GRAY);
			}
			else if (res.getStatus().equals("D")) {
				holder.bt_dtmanage_stop.setText("系统作废");
				holder.bt_dtmanage_stop.setEnabled(false);
				holder.bt_dtmanage_stop.setBackgroundColor(Color.GRAY);
			}
			else if (res.getStatus().equals("C")) {
				
				holder.bt_dtmanage_stop.setEnabled(false);
				holder.bt_dtmanage_stop.setBackgroundColor(Color.GRAY);
			}else {
				holder.bt_dtmanage_stop.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(DTManageActivity.this,
								DTStopActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("sessionId", sessionId);
						//bundle.putString("IDCard", encodeIdCard);
						//bundle.putString("PassWord", encodePassWord);
						bundle.putSerializable("DTManageSearchResult", res);
						// intent.putExtra("IDCard", encodeIdCard);
						// intent.putExtra("PassWord", encodePassWord);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});
			}

			return convertView;
		}

		class ViewHolder {
			TextView tv_dtmanage_fundName, tv_dtmanage_firstpaydate, tv_dtmanage_cycle;
					
			Button bt_dtmanage_stop;
		}

	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		DTManageSearchAdapter listAdapter = (DTManageSearchAdapter) listView
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
								System.out.println("<><><><><><><><><>"+ xmlReturn);
								
								if (xmlReturn.contains("msg")&& xmlReturn.contains("code")) {
									showToast("您没有可以管理的定投基金");
									disMissDialog();
									return;
								}else{
									results = JSON.parseArray(xmlReturn,DTManageSearchResult.class);
								}

								list_dtmanagelist
										.setAdapter(new DTManageSearchAdapter(
												results));
								setListViewHeightBasedOnChildren(list_dtmanagelist);

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
