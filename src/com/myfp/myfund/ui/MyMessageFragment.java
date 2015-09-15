package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cn.jpush.android.util.ac;

import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyMessageFragment extends BaseFragment {
	private View view;
	private MyPropertyMemberActivity activityMes;
	ByteArrayInputStream tInputStringStream = null;
	private TextView tev_member;
	private TextView tev_member_date;
	private TextView tev_total_fee;
	private TextView tev_Withdraw_fee;
	private TextView tev_surplus_money;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activityMes = (MyPropertyMemberActivity) getActivity();
		
		Intent intent = activityMes.getIntent();
		String idcard = intent.getStringExtra("idcard");
		RequestParams params=new RequestParams(activityMes);
		params.put("idcard", idcard.trim());
		activityMes.execApi(ApiType.GET_MEMBERINFORMATIONNEW, params,this);
		activityMes.showProgressDialog("正在加载");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_member_information, null);
		tev_member = (TextView) view.findViewById(R.id.tev_member);
		tev_member_date = (TextView) view.findViewById(R.id.tev_member_date);
		tev_total_fee = (TextView) view.findViewById(R.id.tev_total_fee);
		tev_Withdraw_fee = (TextView) view.findViewById(R.id.tev_Withdraw_fee);
		tev_surplus_money = (TextView) view.findViewById(R.id.tev_surplus_money);
		
		return view;
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json==null) {
			activityMes.showToast("请求失败");
			activityMes.disMissDialog();
			return;
		}
		if (api == ApiType.GET_MEMBERINFORMATIONNEW) {

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
										
										JSONObject jsonObj = new JSONObject(xmlReturn);
										System.out.println("jsonObj-------------->"+jsonObj);
										tev_member.setText(jsonObj.getString("dctVipTypeName"));
										String data = jsonObj.getString("dctVipBeginDate").split(" ")[0];
										String dctVipBeginDate = data.replaceAll("-", "/");
										String dct = jsonObj.getString("dctVipEndDate").split(" ")[0];
										String dctVipEndDate = dct.replaceAll("-", "/");
										tev_member_date.setText(dctVipBeginDate+" ~ "+dctVipEndDate);
										
										DecimalFormat df=new DecimalFormat("#0.##");
										double saveApplyCost=Double.parseDouble(jsonObj.getString("saveApplyCost"));
										String saveApply = df.format(saveApplyCost);
										tev_total_fee.setText(saveApply+"元");
										
										DecimalFormat de=new DecimalFormat("#0.##");
										double alreadyReturnApp=Double.parseDouble(jsonObj.getString("alreadyReturnApplyCost"));
										 String alreadyReturnApplyCost = de.format(alreadyReturnApp);
										tev_Withdraw_fee.setText(alreadyReturnApplyCost+"元");
										
										DecimalFormat sa=new DecimalFormat("#0.##");
										double canReturnApp=Double.parseDouble(jsonObj.getString("canReturnApplyCost"));
										 String ReturnApp = sa.format(canReturnApp);
										tev_surplus_money.setText(ReturnApp+"元");
									} catch (Exception e) {
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
}
