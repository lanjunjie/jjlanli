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


public class DealCancellationActivity extends BaseActivity {
	private String encodeIdCard, encodePassWord,yewutype;
	ByteArrayInputStream tInputStringStream = null;
	private List<CancellationResult> results;
	ListView list_cancellationlist;
	public static DealCancellationActivity instance = null;
	private String sessionId;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealcancellation);
		instance = this;
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		sessionId = intent.getStringExtra("sessionId");
		RequestParams params = new RequestParams(this);
		params.put("sessionId", sessionId);
		//params.put("id", encodeIdCard);
		params.put("passwd", encodePassWord);
		execApi(ApiType.GET_CANCELLATIONLISTTWO, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金交易撤单");
		list_cancellationlist = (ListView) findViewById(R.id.list_cancellation_list);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class CancellationAdapter extends BaseAdapter {

		private List<CancellationResult> list;

		public CancellationAdapter(List<CancellationResult> list) {
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

			convertView = LayoutInflater.from(DealCancellationActivity.this)
					.inflate(R.layout.item_cancellationlist, null, false);
			holder = new ViewHolder();

			holder.tv_cancellation_fundName = (TextView) convertView
					.findViewById(R.id.tv_cancellation_fundname);
			holder.tv_cancellation_yewu = (TextView) convertView
					.findViewById(R.id.tv_cancellation_yewu);
			holder.tv_cancellation_fene = (TextView) convertView
					.findViewById(R.id.tv_cancellation_fene);
			holder.bt_deal_cancellation = (Button) convertView
					.findViewById(R.id.bt_deal_cancellation);
			holder.tv_cancellation_date = (TextView) convertView
					.findViewById(R.id.tv_cancellation_date);

			convertView.setTag(holder);

			final CancellationResult res = list.get(position);
			holder.tv_cancellation_fundName.setText(res.getFundname());
			
			switch (Integer.parseInt(res.getBusinesscode())) {
			case 20:
				holder.tv_cancellation_yewu.setText("认购");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 22:
				holder.tv_cancellation_yewu.setText("申购");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 24:
				holder.tv_cancellation_yewu.setText("赎回");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 26:
				holder.tv_cancellation_yewu.setText("转托管申请");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 29:
				holder.tv_cancellation_yewu.setText("设置分红方式");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 36:
				holder.tv_cancellation_yewu.setText("基金转换");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 39:
				holder.tv_cancellation_yewu.setText("定投");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 59:
				holder.tv_cancellation_yewu.setText("定投开通");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 60:
				holder.tv_cancellation_yewu.setText("定投撤销");
				holder.tv_cancellation_fene.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;

			default:
				
				break;
			}
			yewutype = holder.tv_cancellation_yewu.getText().toString();

			
			holder.tv_cancellation_date.setText(String.format("%.2f",
					Double.parseDouble(res.getOperdate())).substring(0, 8));

			holder.bt_deal_cancellation
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(
									DealCancellationActivity.this,
									CancellationConfirmActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("sessionId", sessionId);
							bundle.putString("IDCard", encodeIdCard);
							bundle.putString("PassWord", encodePassWord);
							bundle.putSerializable("CancellationResult", res);
							bundle.putString("YeWuType", yewutype);
							// intent.putExtra("IDCard", encodeIdCard);
							// intent.putExtra("PassWord", encodePassWord);
							intent.putExtras(bundle);
							startActivity(intent);

						}
					});

			return convertView;
		}

		class ViewHolder {
			TextView tv_cancellation_fundName, tv_cancellation_yewu,
					tv_cancellation_fene, tv_cancellation_date;
			Button bt_deal_cancellation;
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
										CancellationResult.class);
								if (results.size() == 0) {
									showToast("您没有可以撤销的基金");
									disMissDialog();
									return;
								}

								list_cancellationlist
										.setAdapter(new CancellationAdapter(
												results));
								

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
