package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.R.color;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.PurchaseDetailsResult;
import com.myfp.myfund.api.beans.ShowDetailsResult;

public class MyShowDetailsFragment extends BaseFragment{
	//点财通会员状态返现表custpoint  status字段值，待审核、审核通过、审核拒绝、已撤销、已处理
	private static final String checkbefore="14234663106880752586351455931795";
	private static final String checkagree="14234663265850351456259010569947";
	private static final String checkrefuse="14234663424190539198989088535606";
	private static final String checkcancel="14234663602500188885615001905430";
	private static final String checkover="14234663799841989515922143107609";
	private View view;
	private MyPropertyMemberActivity activityMes;
	ByteArrayInputStream tInputStringStream = null;
	private ListView list_Show_details;
	private List<PurchaseDetailsResult> list;
	private JSONArray jsonArray;
	private String idcard;
	private List<ShowDetailsResult> results;
	private String cancelId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityMes = (MyPropertyMemberActivity) getActivity();
		
		Intent intent = activityMes.getIntent();
		idcard = intent.getStringExtra("idcard");
		
		RequestParams params=new RequestParams(activityMes);
		System.out.println("params我的详情-=-==-=-==-=<>"+params);
		params.put("idcard".trim(), idcard.trim());
		activityMes.execApi(ApiType.GET_SHOWDETAILSTWONEW,params,this);
		activityMes.showProgressDialog("正在加载");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_show_details, null);
		list_Show_details = (ListView) view.findViewById(R.id.list_Show_details);
		
		return view;
	}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json==null) {
			activityMes.showToast("数据获取失败");
			activityMes.disMissDialog();
			return;
		}
		if (api == ApiType.GET_SHOWDETAILSTWONEW) {

			if (json != null && !json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
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
									System.out.println("---------------------->"
											+ xmlReturn);
									
									results = JSON.parseArray(xmlReturn, ShowDetailsResult.class);
									
									System.out.println("results========>"+results);
									ShowDetailsAdapter adapter=new ShowDetailsAdapter(results);
									System.out.println("adapter是否有数据==============>"+adapter);
									list_Show_details.setAdapter(adapter);
									adapter.notifyDataSetChanged();
									
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
			activityMes.disMissDialog();
		}
		
		
	}
	
	class ShowDetailsAdapter extends BaseAdapter{
		List<ShowDetailsResult> mList;
		private Date parse;
		private ShowDetailsResult res;
		private String cancel;
		private Date date;
		public ShowDetailsAdapter(List<ShowDetailsResult> list){
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
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			//if (convertView==null) {
				convertView = LayoutInflater.from(activityMes).inflate(R.layout.item_show_detailsitem, null);
				holder=new ViewHolder();
				holder.te_withdrawal_date = (TextView) convertView.findViewById(R.id.te_withdrawal_date);
				holder.te_Withdrawal_amount = (TextView) convertView.findViewById(R.id.te_Withdrawal_amount);
				holder.te_state = (TextView) convertView.findViewById(R.id.te_state);
				holder.but_operation = (Button)convertView.findViewById(R.id.but_operation);
				convertView.setTag(holder);
				
			//}else {
				holder = (ViewHolder) convertView.getTag();
			//}
			res = mList.get(position);
			System.out.println("res=================>"+res);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
			
			Date formatDateString = null;
			String fora = null;
			//try {
				//formatDateString = sdf.parse(res.getFormatdate());
				//fora = sdf1.format(formatDateString);
			//} catch (ParseException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			//}
			//String dateStr = "Jun 26,2014 4:15:04 PM";
			  DateFormat formatFrom = new SimpleDateFormat("MMM dd,yy", Locale.ENGLISH);
			  try {
				  String date2 = res.getDate();
				  System.out.println("date2=-=-=-=-=-=-==>"+date2);
				date = formatFrom.parse(res.getDate());
				System.out.println("date------------->"+date);
				DateFormat formatTo = new SimpleDateFormat("yyyy/MM/dd");
				fora = formatTo.format(date);
				System.out.println("fora==============>"+fora);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			holder.te_withdrawal_date.setText(fora);
			holder.te_Withdrawal_amount.setText(res.getPoint());
			if (res.getStatus().equals(checkbefore)) {
				holder.te_state.setText("待审核");
			}else if (res.getStatus().equals(checkagree)) {
				holder.te_state.setText("审核通过");
			}else if (res.getStatus().equals(checkrefuse)) {
				holder.te_state.setText("审核拒绝");
			}else if (res.getStatus().equals(checkcancel)) {
				holder.te_state.setText("已撤销");
			}else if (res.getStatus().equals(checkover)) {
				holder.te_state.setText("已处理");
				
			}
			
			if (res.getStatus().equals(checkagree)) {
				holder.but_operation.setEnabled(false);
				holder.but_operation.setBackgroundColor(Color.GRAY);
			}else if (res.getStatus().equals(checkrefuse)) {
				holder.but_operation.setEnabled(false);
				holder.but_operation.setBackgroundColor(Color.GRAY);
			}else if (res.getStatus().equals(checkcancel)) {
				holder.but_operation.setEnabled(false);
				holder.but_operation.setBackgroundColor(Color.GRAY);
			}else if (res.getStatus().equals(checkover)) {
				holder.but_operation.setEnabled(false);
				holder.but_operation.setBackgroundColor(Color.GRAY);
				
			}else {
	        
				list_Show_details.setItemChecked(position, getRetainInstance());
				ShowDetailsResult resu = results.get(position);
				cancel = resu.getId();
			holder.but_operation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					RequestParams params=new RequestParams(activityMes);
					params.put("idcard", idcard.trim());
					params.put("cancelId", cancel);
					
					activityMes.execApi(ApiType.GET_WITHDRaAWCASHBACKRECORDNEW, params,new OnDataReceivedListener() {
						
						@Override
						public void onReceiveData(ApiType api, String json) {
							if (json==null) {
								activityMes.showToast("数据获取失败");
								activityMes.disMissDialog();
								return;
							}
							if (api == ApiType.GET_WITHDRaAWCASHBACKRECORDNEW) {

								if (json != null && !json.equals("")) {
									tInputStringStream = new ByteArrayInputStream(
											json.getBytes());
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
														System.out.println("---------------------->"
																+ xmlReturn);
														try {
															JSONObject object=new JSONObject(xmlReturn);
															if (object.getString("result").equals("true")) {
																activityMes.showToastLong("您的撤销已成功！");
															}else {
																activityMes.showToastLong("您未能成功撤销！");
															}
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
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
								activityMes.disMissDialog();
							}
						}
					});
					
				}
			});
			
			
			}
			return convertView;
		}
		 class ViewHolder{
			 TextView te_withdrawal_date,te_Withdrawal_amount,te_state;
			 Button but_operation;
		}
		
	}
	
	

}
