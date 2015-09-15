package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.HistorySearchResult;

public class DealHistoryFragment extends BaseFragment implements
		OnClickListener {
	private EditText ed_history_startdate, ed_history_stopdate;
	private Button bt_history_search,bt_history_reset;
	private static final int START_DIALOG_ID = 0;
	private static final int STOP_DIALOG_ID = 1;

	private static final int START_DATAPICK = 0;
	private static final int STOP_DATAPICK = 1;

	private int mYear;

	private int mMonth;

	private int mDay;
	private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	public static DealHistoryActivity instance = null;
	private List<HistorySearchResult> results;
	ListView list_historylist;
	private DealEntrustActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (DealEntrustActivity) getActivity();
		Intent intent = activity.getIntent();
//		encodeIdCard = intent.getStringExtra("IDCard");
//		encodePassWord = intent.getStringExtra("PassWord");
		sessionId = intent.getStringExtra("sessionId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_dealhistory, null);
		ed_history_startdate = (EditText) view.findViewById(R.id.ed_history_startdate);
		ed_history_stopdate = (EditText) view.findViewById(R.id.ed_history_stopdate);
		bt_history_search=(Button) view.findViewById(R.id.bt_history_search);
		bt_history_reset=(Button) view.findViewById(R.id.bt_history_reset);
		bt_history_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!(ed_history_startdate.getText().toString() == null)) {
					if (!(ed_history_stopdate.getText().toString() == null)) {
						if (Integer.parseInt(ed_history_startdate.getText()
								.toString()) > Integer.parseInt(ed_history_stopdate
								.getText().toString())) {
							activity.showToast("请输入正确的起止时间");
							return;
						} else {
							RequestParams params = new RequestParams(activity);
							//params.put("idcard", encodeIdCard);
//							params.put("passwd", encodePassWord);
							params.put("sessionId", sessionId);
							params.put("begindate", ed_history_startdate.getText()
									.toString());
							params.put("enddate", ed_history_stopdate.getText()
									.toString());

							activity.execApi(ApiType.GET_HISTORYSEARCHTWO, params,new OnDataReceivedListener() {
								
								@Override
								public void onReceiveData(ApiType api, String json) {
									if (json == null) {
										activity.showToast("请求失败");
										activity.disMissDialog();
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
																	HistorySearchResult.class);
															if (results.size() == 0) {
																activity.showToast("没有符合条件的基金！");
																activity.disMissDialog();
																return;
															}

															list_historylist
																	.setAdapter(new HistorySearchAdapter(
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
										activity.disMissDialog();

									}

									
								}
							});
							activity.showProgressDialog("正在搜索");

						}
					} else {
						activity.showToast("请输入截止时间");
						return;
					}
				} else {
					activity.showToast("请输入起始时间");
					return;
				}
				
			}
		});
		bt_history_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setDateTime(ed_history_startdate);
				setDateTime(ed_history_stopdate);
				
			}
		});
		ed_history_startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();

				msg.what = START_DATAPICK;
				saleHandler.sendMessage(msg);

			}
		});
		ed_history_stopdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();

				msg.what =STOP_DATAPICK;
				saleHandler.sendMessage(msg);

			}
		});
		setDateTime(ed_history_startdate);
		setDateTime(ed_history_stopdate);
		list_historylist = (ListView) view.findViewById(R.id.list_history_list);

		return view;
	}

	

	@Override
	protected void onViewClick(View v) {
		
	}

	class HistorySearchAdapter extends BaseAdapter {

		private List<HistorySearchResult> list;

		public HistorySearchAdapter(List<HistorySearchResult> list) {
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
			final ViewHolder holder;

			convertView = LayoutInflater.from(activity)
					.inflate(R.layout.item_dealhistory, null);
			holder = new ViewHolder();

			holder.tv_history_fundCode = (TextView) convertView
					.findViewById(R.id.tv_history_fundcode);
			holder.tv_history_fundName = (TextView) convertView
					.findViewById(R.id.tv_history_fundname);
			holder.tv_history_yewu = (TextView) convertView
					.findViewById(R.id.tv_history_yewu);
			holder.bt_deal_history = (Button) convertView
					.findViewById(R.id.bt_deal_history);
			holder.tv_history_jine = (TextView) convertView
					.findViewById(R.id.tv_history_jine);

			convertView.setTag(holder);

			final HistorySearchResult res = list.get(position);
			holder.tv_history_fundCode.setText(res.getFundcode());
			holder.tv_history_fundName.setText(res.getFundname());
			switch (Integer.parseInt(res.getBusinesscode())) {
			case 20:
				holder.tv_history_yewu.setText("认购");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 22:
				holder.tv_history_yewu.setText("申购");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 24:
				holder.tv_history_yewu.setText("赎回");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getConfirmedamount())));
				break;
			case 26:
				holder.tv_history_yewu.setText("转托管申请");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 27:
				holder.tv_history_yewu.setText("转托管转入");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 28:
				holder.tv_history_yewu.setText("转托管转出");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 29:
				holder.tv_history_yewu.setText("设置分红方式");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 30:
				holder.tv_history_yewu.setText("认购结果");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 31:
				holder.tv_history_yewu.setText("基金份额冻结");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 32:
				holder.tv_history_yewu.setText("基金份额解冻");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 33:
				holder.tv_history_yewu.setText("非交易过户");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 34:
				holder.tv_history_yewu.setText("非交易过户转入");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 35:
				holder.tv_history_yewu.setText("非交易过户转出");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 36:
				holder.tv_history_yewu.setText("基金转换");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getConfirmedamount())));
				break;
			case 37:
				holder.tv_history_yewu.setText("基金转换入");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 38:
				holder.tv_history_yewu.setText("基金转换出");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 39:
				holder.tv_history_yewu.setText("定投");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getMoneyin())));
				break;
			case 42:
				holder.tv_history_yewu.setText("强制赎回");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getConfirmedamount())));
				break;
			case 43:
				holder.tv_history_yewu.setText("分红");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 44:
				holder.tv_history_yewu.setText("强行调增");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 45:
				holder.tv_history_yewu.setText("强行调减");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 49:
				holder.tv_history_yewu.setText("基金募集失败");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 50:
				holder.tv_history_yewu.setText("基金清盘");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;

			case 59:
				holder.tv_history_yewu.setText("定投开通");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 60:
				holder.tv_history_yewu.setText("定投撤销");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 61:
				holder.tv_history_yewu.setText("定投协议变更");
				holder.tv_history_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			default:

				break;
			}

			holder.bt_deal_history.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// if("2".equals(res.getStatus()) ||
					// "3".equals(res.getStatus())
					// || "4".equals(res.getStatus())
					// || "5".equals(res.getStatus())
					// || "9".equals(res.getStatus())
					// || "a".equals(res.getStatus())){
					// showToast("该基金暂时不能申购！！");
					// return;
					// }
					Intent intent = new Intent(activity,
							HistoryInforActivity.class);
					Bundle bundle = new Bundle();
//					bundle.putString("IDCard", encodeIdCard);
//					bundle.putString("PassWord", encodePassWord);
					bundle.putString("YeWu", holder.tv_history_yewu.getText()
							.toString());
					bundle.putSerializable("HistorySearchResult", res);
					// intent.putExtra("IDCard", encodeIdCard);
					// intent.putExtra("PassWord", encodePassWord);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView tv_history_fundName, tv_history_fundCode, tv_history_yewu,
					tv_history_jine;
			Button bt_deal_history;
		}

	}


	private void setDateTime(EditText edt) {

		final Calendar c = Calendar.getInstance();
		if (edt.equals(ed_history_startdate)) {

			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 30);

			mYear = c.get(Calendar.YEAR);

			mMonth = c.get(Calendar.MONTH);

			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplayStart();
		} else if (edt.equals(ed_history_stopdate)) {
			mYear = c.get(Calendar.YEAR);

			mMonth = c.get(Calendar.MONTH);

			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplayStop();
		}

	}

	/**
	 * 
	 * 更新日期
	 */

	private void updateDisplayStart() {

		ed_history_startdate.setText(new StringBuilder().append(mYear).append(

		(mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(

		(mDay < 10) ? "0" + mDay : mDay));

	}

	private void updateDisplayStop() {

		ed_history_stopdate.setText(new StringBuilder().append(mYear).append(

		(mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(

		(mDay < 10) ? "0" + mDay : mDay));

	}

	/**
	 * 
	 * 日期控件的事件
	 */

	private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			mYear = year;

			mMonth = monthOfYear;

			mDay = dayOfMonth;
			updateDisplayStart();

			System.out.println(mYear + "yyyyyy" + mMonth + "mmmmmm" + mDay
					+ "ddddddd");

		}

	};
	private DatePickerDialog.OnDateSetListener stopDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			mYear = year;

			mMonth = monthOfYear;

			mDay = dayOfMonth;
			updateDisplayStop();

			System.out.println(mYear + "yyyyyy" + mMonth + "mmmmmm" + mDay
					+ "ddddddd");

		}

	};

	/**
	 * 
	 * 选择日期Button的事件处理
	 * 
	 * 
	 * 
	 * @author Raul
	 * 
	 * 
	 */

	// class DateButtonOnClickListener implements
	//
	// android.view.View.OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	//
	// Message msg = new Message();
	//
	// if (pickDate.equals((Button) v)) {
	//
	// msg.what = DealEntrustActivity.SHOW_DATAPICK;
	//
	// }
	//
	// DealEntrustActivity.this.saleHandler.sendMessage(msg);
	//
	// }
	//
	// }
	public Dialog onDialog(int id){
		switch (id) {

		case START_DIALOG_ID:

			return new DatePickerDialog(activity, startDateSetListener, mYear,
					mMonth,

					mDay);
		case STOP_DIALOG_ID:

			return new DatePickerDialog(activity, stopDateSetListener, mYear,
					mMonth,

					mDay);

		}
		return null;
		
	}
	/*@Override
	public Dialog onCreateDialog(int id) {

		switch (id) {

		case START_DIALOG_ID:

			return new DatePickerDialog(activity, startDateSetListener, mYear,
					mMonth,

					mDay);
		case STOP_DIALOG_ID:

			return new DatePickerDialog(activity, stopDateSetListener, mYear,
					mMonth,

					mDay);

		}

		return null;

	} */

	public void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {

		case START_DIALOG_ID:

			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

			break;
		case STOP_DIALOG_ID:

			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

			break;

		}

	}

	/**
	 * 
	 * 处理日期控件的Handler
	 */

	Handler saleHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case START_DATAPICK:

				onDialog(START_DIALOG_ID).show();

				break;
			case STOP_DATAPICK:

				 onDialog(STOP_DIALOG_ID).show();


				break;

			}

		}

	};
	private String sessionId;

}
