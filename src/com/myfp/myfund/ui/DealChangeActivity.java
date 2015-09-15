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
import com.myfp.myfund.api.beans.RedeemSearchResult;
/*
 * 转换
 * */
public class DealChangeActivity extends BaseActivity{
	private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	private List<RedeemSearchResult> results;
	ListView list_dealchangelist;
	public static DealChangeActivity instance = null;
	private String sessionId;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealchange);
		instance = this;
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		sessionId = intent.getStringExtra("sessionId");
		RequestParams params = new RequestParams(this);
		//params.put("id", encodeIdCard);
		params.put("sessionId", sessionId);
		//params.put("passwd", encodePassWord);
		execApi(ApiType.GET_DEALREDEEM_LISTTWO, params);
		showProgressDialog("正在加载");
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金转换");
		list_dealchangelist = (ListView) findViewById(R.id.list_dealchange_list);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	class ChangeSearchAdapter extends BaseAdapter {

		private List<RedeemSearchResult> list;

		public ChangeSearchAdapter(List<RedeemSearchResult> list) {
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

			convertView = LayoutInflater.from(DealChangeActivity.this).inflate(
					R.layout.item_dealchangelist, null, false);
			holder = new ViewHolder();

			holder.tv_change_fundName = (TextView) convertView
					.findViewById(R.id.tv_change_fundname);
			holder.tv_change_fundCode = (TextView) convertView
					.findViewById(R.id.tv_change_fundcode);
			holder.tv_change_fene = (TextView) convertView
					.findViewById(R.id.tv_change_fene);
			holder.bt_deal_change = (Button) convertView
					.findViewById(R.id.bt_deal_change);
			holder.tv_change_shizhi = (TextView) convertView
					.findViewById(R.id.tv_change_shizhi);

			convertView.setTag(holder);
			
			final RedeemSearchResult res = list.get(position);
			
			holder.tv_change_fundName.setText(res.getFundname());
			holder.tv_change_fundCode.setText(res.getFundcode());
			String transactionaccountid = res.getTransactionaccountid();
			System.out.println("transactionaccountid====>"+transactionaccountid);
			holder.tv_change_fene.setText(String.format("%.2f",
					Double.parseDouble(res.getAvailablevol())));
			holder.tv_change_shizhi.setText(String.format("%.2f",
					Double.parseDouble(res.getFundmarketvalue())));
			if (Double.parseDouble(res.getAvailablevol()) == 0) {
				holder.bt_deal_change.setEnabled(false);
				holder.bt_deal_change.setBackgroundColor(Color.GRAY);
			} else {
				holder.bt_deal_change.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(DealChangeActivity.this,
								ChangeApplyActivity.class);
						Bundle bundle = new Bundle();
						//bundle.putString("IDCard", encodeIdCard);
						//bundle.putString("PassWord", encodePassWord);
						bundle.putString("sessionId", sessionId);
						bundle.putSerializable("RedeemSearchResult", res);
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
			TextView tv_change_fundName, tv_change_fundCode, tv_change_fene,
					tv_change_shizhi;
			Button bt_deal_change;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ChangeSearchAdapter listAdapter = (ChangeSearchAdapter) listView
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
								System.out.println("<><><><><><><><><>"
										+ xmlReturn);
								results = JSON.parseArray(xmlReturn,
										RedeemSearchResult.class);
								if (results.size() == 0) {
									showToast("您没有可以转换的基金");
									disMissDialog();
									return;
								}

								list_dealchangelist
										.setAdapter(new ChangeSearchAdapter(
												results));
								setListViewHeightBasedOnChildren(list_dealchangelist);

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
