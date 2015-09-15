package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.BankTypeResult;

public class BankTypeActivity extends BaseActivity {
	ByteArrayInputStream tInputStringStream = null;
	private List<BankTypeResult> results,banklist;
	ListView list_banktype;
	public static BankTypeActivity instance = null;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_banktype);
		showProgressDialog("正在加载");
		execApi(ApiType.GET_OPENACCOUNTBANKS, null);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("银行卡类型");
		list_banktype = (ListView)findViewById(R.id.list_banktype);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	class BankSearchAdapter extends BaseAdapter {

		private List<BankTypeResult> list;

		public BankSearchAdapter(List<BankTypeResult> list) {
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

			convertView = LayoutInflater.from(BankTypeActivity.this).inflate(
					R.layout.item_banktype, null, false);
			holder = new ViewHolder();

			holder.tv_banktype_bankname = (TextView) convertView
					.findViewById(R.id.tv_banktype_bankname);
			holder.img_banktype_pic = (ImageView) convertView
					.findViewById(R.id.img_banktype_pic);
			

			convertView.setTag(holder);
			
			final BankTypeResult res = list.get(position);
			if(res.getChannelid().equals("8882")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.gsbank);
			}else if(res.getChannelid().equals("0105")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.jsbank);
			}else if(res.getChannelid().equals("0103")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.nybank);
			}
		/*	else if(res.getChannelid().equals("0311")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.yzbank);
			} */
			else if(res.getChannelid().equals("0308")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.zsbank);
			}else if(res.getChannelid().equals("0410")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.pabank);
			}else if(res.getChannelid().equals("0309")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.xybank);
			}else if(res.getChannelid().equals("0303")){
				holder.tv_banktype_bankname.setText(res.getChannelname());
				holder.img_banktype_pic.setImageResource(R.drawable.gdbank);
			}
			

			return convertView;
		}

		class ViewHolder {
			TextView tv_banktype_bankname;
			ImageView img_banktype_pic;
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
								System.out.println("<><><><><><><><><>"
										+ xmlReturn);
								results = JSON.parseArray(xmlReturn,
										BankTypeResult.class);
								if (results.size() == 0) {
									showToast("没有可获取的银行卡");
									disMissDialog();
									return;
								}
									banklist = new ArrayList<BankTypeResult>();
								for(int i = 0;i<results.size();i++){
									BankTypeResult res = new BankTypeResult();
									res = results.get(i);
									if(res.getChannelid().equals("8882")){
										banklist.add(res);
									}else if(res.getChannelid().equals("0105")){
										banklist.add(res);
									}else if(res.getChannelid().equals("0103")){
										banklist.add(res);
									}
							/*		else if(res.getChannelid().equals("0311")){
										banklist.add(res);
									} */
									else if(res.getChannelid().equals("0308")){
										banklist.add(res);
									}else if(res.getChannelid().equals("0410")){
										banklist.add(res);
									}else if(res.getChannelid().equals("0309")){
										banklist.add(res);
									}else if(res.getChannelid().equals("0303")){
										banklist.add(res);
									}
								}
								
								

								list_banktype
										.setAdapter(new BankSearchAdapter(
												banklist));
								list_banktype.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long itemId) {
										// TODO Auto-generated method stub
										BankTypeResult res = banklist.get(position);
										Bundle bundle = new Bundle();
										bundle.putSerializable("BankTypeResult", res);
										Intent intent = new Intent(
												BankTypeActivity.this,
												BindingBankActivity.class);
										intent.putExtras(bundle);
												
										setResult(0, intent);
										finish();
									}
								});
								

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
