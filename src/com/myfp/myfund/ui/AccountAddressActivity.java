package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.AccountAddressResult;

public class AccountAddressActivity extends BaseActivity {
	ByteArrayInputStream tInputStringStream = null;
	private List<AccountAddressResult> results;
	ListView list_accountaddress;
	EditText et_accountaddress_search;
	private AddressSearchAdapter addadapter;
	public static AccountAddressActivity instance = null;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_accountaddress);
		Intent intent = getIntent();
		RequestParams params = new RequestParams(this);
		params.put("paracity", intent.getStringExtra("City"));
		params.put("channelid", intent.getStringExtra("ChannelId"));
		execApi(ApiType.GET_OPENACCOUNTADDRESS, params);
		showProgressDialog("正在搜索");

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("开户网点");
		list_accountaddress = (ListView) findViewById(R.id.list_accountaddress);
		et_accountaddress_search = (EditText) findViewById(R.id.et_accountaddress_search);
		findViewAddListener(R.id.bt_accountaddress_search);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_accountaddress_search:
			if (et_accountaddress_search.getText().toString().length() == 0) {
				showToast("请输入搜索关键字");
				return;
			} else {
				final List<AccountAddressResult> addressList = new ArrayList<AccountAddressResult>();
				for (int i = 0; i < results.size(); i++) {
					if (results
							.get(i)
							.getParavalue()
							.contains(
									et_accountaddress_search.getText()
											.toString())) {
						addressList.add(results.get(i));
					}
				}
				if (addressList.size() == 0) {
					showToast("没有查询到结果");
					return;
				} else {
					addadapter.list = addressList;
					addadapter.notifyDataSetChanged();
					list_accountaddress.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							
							AccountAddressResult res = addressList.get(arg2);
							Bundle bundle = new Bundle();
							bundle.putString("AccountAddress", res.getParavalue());
							Intent intent = new Intent(AccountAddressActivity.this,
									BindingBankActivity.class);
							intent.putExtras(bundle);
							setResult(4, intent);
							finish();
						}
					});
				}

			}

			break;

		default:
			break;
		}

	}

	class AddressSearchAdapter extends BaseAdapter {

		private List<AccountAddressResult> list;

		public AddressSearchAdapter(List<AccountAddressResult> list) {
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

			convertView = LayoutInflater.from(AccountAddressActivity.this)
					.inflate(R.layout.item_accountaddress, null, false);
			holder = new ViewHolder();

			holder.tv_accountaddress_address = (TextView) convertView
					.findViewById(R.id.tv_accountaddress_address);

			convertView.setTag(holder);

			final AccountAddressResult res = list.get(position);
			holder.tv_accountaddress_address.setText(res.getParavalue());

			return convertView;
		}

		class ViewHolder {
			TextView tv_accountaddress_address;
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
								if (xmlReturn.contains("paravalue")) {
									disMissDialog();
									results = JSON.parseArray(xmlReturn,
											AccountAddressResult.class);
									if (results.size() == 0) {
										showToast("不存在开户网点");
										disMissDialog();
										return;
									}else{
										addadapter = new AddressSearchAdapter(results);
										list_accountaddress.setAdapter(addadapter);
										list_accountaddress.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> parent, View view,
													int position, long itemId) {
												// TODO Auto-generated method stub
												AccountAddressResult res = results.get(position);
												Bundle bundle = new Bundle();
												bundle.putString("AccountAddress", res.getParavalue());
												Intent intent = new Intent(AccountAddressActivity.this,
														BindingBankActivity.class);
												intent.putExtras(bundle);
												setResult(4, intent);
												finish();
											}
										});
									}
								} else {
									disMissDialog();
									showToast("获取失败");
									return;
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
