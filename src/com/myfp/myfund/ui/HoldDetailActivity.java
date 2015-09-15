package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.HoldDetail;
import com.myfp.myfund.api.beans.MySelectFund;

public class HoldDetailActivity extends BaseActivity {

	private String userName, encodeIdCard, encodePassWord;
	int result;
	ByteArrayInputStream tInputStringStream = null;
	private ListView listView;
	TextView tv_zongjijinshu, tv_zongshizhi, tv_zongyingkui, tv_zongshouyilv,
			tv_deal, tv_kaihu;
	ImageView img_reminder;


	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		showProgressDialog("正在加载");

		// SharedPreferences sPreferences = getSharedPreferences("Setting",
		// MODE_PRIVATE);
		// userName = sPreferences.getString("UserName", null);
		// idCard = sPreferences.getString("IDCard", null);

		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		RequestParams params = new RequestParams(this);
		params.put("id", encodeIdCard);
		params.put("passwd", encodePassWord);
		execApi(ApiType.GET_HOLD_DETAIL, params);
		// RndDataApi.executeNetworkApi(ApiType.GET_HOLD_DETAIL, params1, this);
		setContentView(R.layout.activity_holddetail);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("我的资产");

		tv_zongjijinshu = (TextView) findViewById(R.id.tv_zongjijinshu);
		tv_zongshizhi = (TextView) findViewById(R.id.tv_zongshizhi);
		tv_zongyingkui = (TextView) findViewById(R.id.tv_zongyingkui);
		tv_zongshouyilv = (TextView) findViewById(R.id.tv_zongshouyilv);
		
		img_reminder = (ImageView) findViewAddListener(R.id.reminder);

		listView = (ListView) findViewById(R.id.lv_holddetail);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reminder:
			// 温馨提示
			startActivity(ReminderActivity.class);
			break;
	

		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		// 在这里会收到数据,一定要判断是否为空
		if (json == null) {
			showToast("获取失败!");
			disMissDialog();
			return;
		}

		if (api == ApiType.GET_HOLD_DETAIL) {

			try {
				if (json != null && !json.trim().equals("")) {
					tInputStringStream = new ByteArrayInputStream(
							json.getBytes());
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								String xmlReturn = parser.nextText();
								System.out.println("<><><><><><><><><>"
										+ xmlReturn);
								if(xmlReturn.equals("[]")){
									disMissDialog();
									showToast("您没有持有基金");
									return;
								}else{
									final List<HoldDetail> datas = JSON.parseArray(
											xmlReturn, HoldDetail.class);
									
									double zongshizhi = 0;
									double zongyingkui = 0;
									double zongshouyilv = 0;
									
									
									zongshouyilv = Double.parseDouble(datas.get(0)
											.getTotaladdincomerate());
									zongyingkui = Double.parseDouble(datas.get(0)
											.getTotalfloatprofit());
									for (int i = 1; i < datas.size(); i++) {
										
										zongshizhi += (Double.parseDouble(datas
												.get(i).getMktvalue()));
									}
									
									tv_zongjijinshu.setText("（" + (datas.size()-1)
											+ "支" + "）");
									tv_zongshizhi.setText(String.format("%.2f",
											zongshizhi));
									tv_zongyingkui.setText(String.format("%.2f",
											zongyingkui));
									if (zongyingkui < 0) {

										tv_zongyingkui.setTextColor(Color.rgb(1,
												153, 1));
									} else {
										tv_zongyingkui.setTextColor(Color.RED);
									}

									tv_zongshouyilv.setText("("
											+ String.format("%.2f",
													zongshouyilv * 100) + "%)");
									if (zongshouyilv < 0) {
										tv_zongshouyilv.setTextColor(Color.rgb(1,
												153, 1));
									} else {
										tv_zongshouyilv.setTextColor(Color.RED);
									}

									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongshizhi * 100));
									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongyingkui * 100));
									System.out.println("<><><><><><><><><>"
											+ String.format("%.2f",
													zongshouyilv * 100));
									System.out.println("<><><><><><><><><>"
											+ (datas.size()-1) + "");

									listView.setAdapter(new HoldListAdapter(datas));
									listView.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent, View view,
												int position, long itemId) {
											// TODO Auto-generated method stub
											HoldDetail hd = datas.get(position+1);
											Intent intent = new Intent(
													HoldDetailActivity.this,
													DetailActivity.class);
											intent.putExtra("fundName", hd
													.getFundname().trim());
											intent.putExtra("fundCode", hd
													.getFundcode().trim());
											startActivity(intent);

										}

									});
								}
								
								
							}

							break;

						}
						event = parser.next();
					}
					tInputStringStream.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
			disMissDialog();

		}

	}

	class HoldListAdapter extends BaseAdapter {
		private List<HoldDetail> data;

		public HoldListAdapter(List<HoldDetail> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size()-1;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			HoldDetail hd = data.get(position+1);
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_holddetail, null);
				holder = new ViewHolder();
				holder.textView_fundName = (TextView) convertView
						.findViewById(R.id.tv_hold_fundname);
				holder.textView_fundCode = (TextView) convertView
						.findViewById(R.id.tv_hold_fundcode);
				holder.textView_shizhi = (TextView) convertView
						.findViewById(R.id.tv_hold_shizhi);
				holder.textView_yingkui = (TextView) convertView
						.findViewById(R.id.tv_hold_yingkui);
				holder.textView_shouyilv = (TextView) convertView
						.findViewById(R.id.tv_hold_shouyilv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView_fundName.setText(hd.getFundname());
			holder.textView_fundName.setTextColor(Color.BLACK);

			holder.textView_fundCode.setText(hd.getFundcode());
			holder.textView_fundCode.setTextColor(Color.BLACK);

			holder.textView_shizhi.setText(String.format("%.2f",
					Double.parseDouble(hd.getMktvalue())));
			holder.textView_shizhi.setTextColor(Color.BLACK);

			holder.textView_yingkui.setText(String.format("%.2f",
					Double.parseDouble(hd.getFloatprofit())));

			if (Double.parseDouble(hd.getFloatprofit()) < 0) {
				holder.textView_yingkui.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.textView_yingkui.setTextColor(Color.RED);
			}
			holder.textView_shouyilv.setText(String.format("%.2f",
					(Double.parseDouble(hd.getAddincomerate())*100)) + "%");

			if (Double.parseDouble(hd.getAddincomerate()) < 0) {
				holder.textView_shouyilv.setTextColor(Color.rgb(1, 153, 1));
			} else {
				holder.textView_shouyilv.setTextColor(Color.RED);
			}

			return convertView;
		}

		class ViewHolder {
			TextView textView_fundName, textView_fundCode, textView_shizhi,
					textView_yingkui, textView_shouyilv;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		HoldListAdapter listAdapter = (HoldListAdapter) listView.getAdapter();
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

}
