package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import com.myfp.myfund.api.beans.EntrustSearchResult;

public class DealEntrusFragment extends BaseFragment implements OnClickListener {
	private EditText ed_entrust_startdate, ed_entrust_stopdate;
	private Button bt_entrust_reset, bt_entrust_search;
	private static final int START_DIALOG_ID = 0;
	private static final int STOP_DIALOG_ID = 1;

	private static final int START_DATAPICK = 0;
	private static final int STOP_DATAPICK = 1;

	private int mYear;

	private int mMonth;

	private int mDay;
	private String encodeIdCard, encodePassWord;
	ByteArrayInputStream tInputStringStream = null;
	public static DealEntrustActivity instance = null;
	private List<EntrustSearchResult> results;
	ListView list_entrustlist;
	private DealEntrustActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (DealEntrustActivity) getActivity();

		Intent intent = activity.getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		sessionId = intent.getStringExtra("sessionId");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_dealentrust, null);
		ed_entrust_startdate = (EditText) view
				.findViewById(R.id.ed_entrust_startdate);
		ed_entrust_stopdate = (EditText) view
				.findViewById(R.id.ed_entrust_stopdate);
		bt_entrust_search = (Button) view.findViewById(R.id.bt_entrust_search);
		bt_entrust_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!(ed_entrust_startdate.getText().toString() == null)) {
					if (!(ed_entrust_stopdate.getText().toString() == null)) {
						if (Integer.parseInt(ed_entrust_startdate.getText()
								.toString()) > Integer
								.parseInt(ed_entrust_stopdate.getText()
										.toString())) {
							activity.showToast("请输入正确的起止时间");
							return;
						} else {
							RequestParams params = new RequestParams(activity);
							//params.put("idcard", encodeIdCard);
//							params.put("passwd", encodePassWord);
							params.put("sessionId", sessionId);
							params.put("begindate", ed_entrust_startdate
									.getText().toString());
							params.put("enddate", ed_entrust_stopdate.getText()
									.toString());
							activity.showProgressDialog("正在搜索");
							activity.execApi(ApiType.GET_ENTURSTSEARCHTWO, params,
									new OnDataReceivedListener() {

										@Override
										public void onReceiveData(ApiType api,
												String json) {
											if (json == null) {
												activity.showToast("请求失败");
												activity.disMissDialog();
												return;
											}
											if (json != null
													&& !json.equals("")) {
												tInputStringStream = new ByteArrayInputStream(
														json.getBytes());
												XmlPullParser parser = Xml
														.newPullParser();
												try {
													parser.setInput(
															tInputStringStream,
															"UTF-8");
													int event = parser
															.getEventType();
													while (event != XmlPullParser.END_DOCUMENT) {
														Log.i("start_document",
																"start_document");
														switch (event) {
														case XmlPullParser.START_TAG:
															if ("return"
																	.equals(parser
																			.getName())) {
																String xmlReturn;
																try {
																	xmlReturn = parser
																			.nextText();
																	System.out
																			.println("<><><><><><><><><>"
																					+ xmlReturn);
																	results = JSON
																			.parseArray(
																					xmlReturn,
																					EntrustSearchResult.class);
																	if (results
																			.size() == 0) {
																		activity.showToast("没有符合条件的基金！");
																		activity.disMissDialog();
																		return;
																	}

																	list_entrustlist
																			.setAdapter(new EntrustSearchAdapter(
																					results));

																} catch (IOException e) {
																	// TODO
																	// Auto-generated
																	// catch
																	// block
																	e.printStackTrace();
																}

															}
															break;

														}
														try {
															event = parser
																	.next();

														} catch (IOException e) {
															// TODO
															// Auto-generated
															// catch block
															e.printStackTrace();
														}

													}
													try {
														tInputStringStream
																.close();
													} catch (IOException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												} catch (XmlPullParserException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
												activity.disMissDialog();

											}

										}
									});

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
		// activity.view.findViewById(R.id.)
		bt_entrust_reset = (Button) view.findViewById(R.id.bt_entrust_reset);

		bt_entrust_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setDateTime(ed_entrust_startdate);
				setDateTime(ed_entrust_stopdate);

			}
		});
		ed_entrust_startdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg = new Message();

				msg.what = START_DATAPICK;
				saleHandler.sendMessage(msg);

			}
		});
		ed_entrust_stopdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg1 = new Message();

				msg1.what = STOP_DATAPICK;
				saleHandler.sendMessage(msg1);

			}
		});
		setDateTime(ed_entrust_startdate);
		setDateTime(ed_entrust_stopdate);
		list_entrustlist = (ListView) view.findViewById(R.id.list_entrust_list);
		return view;
	}

	class EntrustSearchAdapter extends BaseAdapter {

		private List<EntrustSearchResult> list;

		public EntrustSearchAdapter(List<EntrustSearchResult> list) {
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

			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_dealentrust, null);
			holder = new ViewHolder();
			System.out.println("view" + convertView);
			holder.tv_entrust_fundCode = (TextView) convertView
					.findViewById(R.id.tv_entrust_fundcode);
			holder.tv_entrust_fundName = (TextView) convertView
					.findViewById(R.id.tv_entrust_fundname);
			holder.tv_entrust_yewu = (TextView) convertView
					.findViewById(R.id.tv_entrust_yewu);
			holder.bt_deal_entrust = (Button) convertView
					.findViewById(R.id.bt_deal_enturst);
			holder.tv_entrust_jine = (TextView) convertView
					.findViewById(R.id.tv_entrust_jine);

			convertView.setTag(holder);

			final EntrustSearchResult res = list.get(position);
			holder.tv_entrust_fundCode.setText(res.getFundcode());
			holder.tv_entrust_fundName.setText(res.getFundname());
			switch (Integer.parseInt(res.getBusinesscode())) {
			case 20:
				holder.tv_entrust_yewu.setText("认购");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 22:
				holder.tv_entrust_yewu.setText("申购");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 24:
				holder.tv_entrust_yewu.setText("赎回");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 26:
				holder.tv_entrust_yewu.setText("转托管申请");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 29:
				holder.tv_entrust_yewu.setText("设置分红方式");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 30:
				holder.tv_entrust_yewu.setText("认购结果");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 36:
				holder.tv_entrust_yewu.setText("基金转换");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;
			case 39:
				holder.tv_entrust_yewu.setText("定投");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 43:
				holder.tv_entrust_yewu.setText("分红");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 59:
				holder.tv_entrust_yewu.setText("定投开通");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationamount())));
				break;
			case 60:
				holder.tv_entrust_yewu.setText("定投撤销");
				holder.tv_entrust_jine.setText(String.format("%.2f",
						Double.parseDouble(res.getApplicationvol())));
				break;

			default:

				break;
			}

			holder.bt_deal_entrust.setOnClickListener(new OnClickListener() {

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
							EntrustInforActivity.class);
					Bundle bundle = new Bundle();
//					bundle.putString("IDCard", encodeIdCard);
//					bundle.putString("PassWord", encodePassWord);
					String tv_entrust = holder.tv_entrust_yewu.getText()
							.toString();
					bundle.putString("YeWu", tv_entrust);
					bundle.putSerializable("EntrustSearchResult", res);
					// intent.putExtra("IDCard", encodeIdCard);
					// intent.putExtra("PassWord", encodePassWord);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView tv_entrust_fundName, tv_entrust_fundCode, tv_entrust_yewu,
					tv_entrust_jine;
			Button bt_deal_entrust;
		}

	}

	private void setDateTime(EditText edt) {

		final Calendar c = Calendar.getInstance();
		if (edt.equals(ed_entrust_startdate)) {

			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 30);

			mYear = c.get(Calendar.YEAR);

			mMonth = c.get(Calendar.MONTH);

			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplayStart();
		} else if (edt.equals(ed_entrust_stopdate)) {
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

		ed_entrust_startdate.setText(new StringBuilder().append(mYear).append(

		(mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append(

		(mDay < 10) ? "0" + mDay : mDay));

	}

	private void updateDisplayStop() {

		ed_entrust_stopdate.setText(new StringBuilder().append(mYear).append(

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

	};

	// @Override
	// protected Dialog onCreateDialog(int id) {
	//
	// switch (id) {
	//
	// case START_DIALOG_ID:
	//
	// return new DatePickerDialog(activity, startDateSetListener, mYear,
	// mMonth,
	//
	// mDay);
	// case STOP_DIALOG_ID:
	//
	// return new DatePickerDialog(activity, stopDateSetListener, mYear,
	// mMonth,
	//
	// mDay);
	//
	// }
	//
	// return null;
	//
	// }

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

				  onCreateDialog(START_DIALOG_ID).show();

				break;
			case STOP_DATAPICK:

				onCreateDialog(STOP_DIALOG_ID).show();

				break;

			}

		}

	};
	private String sessionId;

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
