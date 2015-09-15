package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import u.aly.de;

import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.R.string;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CancellationResult;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.api.beans.queryAssetsResult;
import com.myfp.myfund.ui.DealChangeActivity.ChangeSearchAdapter.ViewHolder;

public class AlterMmodeChangeActivity extends BaseActivity{
	private Button bt_Mode_change;
	private LinearLayout la_Trading_Number;
	private LinearLayout la_Manner;
	private TextView tv_text1;
	private ImageView iv_image1;
	private ListView list_View;
	private TextView text_View;
	Bundle bundle;
	ByteArrayInputStream tInputStringStream = null;
	private Spinner Spinner_accounts;
	private queryAssetsResult res;
	private FrameLayout bt;  
	private  List<String> list ;
	public static AlterMmodeChangeActivity instance = null;
	private String encodeIdCard, encodePassWord;
	private RequestParams params;
	private ListView lv_view;
	private String depositacct;
	private Collection<? extends String> map;
	private ArrayAdapter<String> adapter;
	private String channelname;
	private String transactionaccountid;
	private TextView text_Bonus_to;
	private TextView fangsi,jijin_name;
	private String defdiv;
	private String defdivden;
	private String defdividendmethod;
	private List<String> mlist;
	private String de;
	private int bon;
	private int cas;
	private String sessionId;
	@Override
	protected void setContentView() {
		setContentView(R.layout.item_alterationbutton);
		
		instance = this;
		bundle = getIntent().getExtras();
		sessionId = bundle.getString("sessionId");
		res = (queryAssetsResult) bundle.getSerializable("queryAssetsResult");
		bt_Mode_change=(Button) findViewById(R.id.bt_Mode_change);
		bt_Mode_change.setOnClickListener(this);
		Spinner_accounts=(Spinner) findViewById(R.id.Spinner_accounts);
		text_Bonus_to = (TextView) findViewById(R.id.text_Bonus_to);
		jijin_name=(TextView) findViewById(R.id.jijin_name);
		fangsi=(TextView) findViewById(R.id.fangsi);
		Intent intent = getIntent();
		encodeIdCard = intent.getStringExtra("IDCard");
		
		encodePassWord = intent.getStringExtra("PassWord");
		int i=Integer.parseInt(res.getDefdividendmethod());
		String fundcode = res.getFundcode();
		
		
		List<String> pList=geProint();
		ArrayAdapter<String> accountsadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pList);
		Spinner_accounts.setAdapter(accountsadapter);
		final List<String> mlist = getManner();
		ArrayAdapter<String> Bonusadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mlist);
		jijin_name.setText("["+res.getFundcode()+"]"+res.getFundname());
		if(i==0){
			fangsi.setText("红利再投");
			text_Bonus_to.setText("现金分红");
			bon = 0;
		}else if(i==1){
			fangsi.setText("现金分红");
			text_Bonus_to.setText("红利再投");
			cas = 1;
		};		

		
	}
	@Override
	protected void initViews() {
		setTitle("变更分红方式");
		
	}
	private List<String> geProint(){
		transactionaccountid = res.getTransactionaccountid();
		depositacct = res.getDepositacct();
		channelname = res.getChannelname();
		list=new ArrayList<String>();
		list.add("[帐号交易: "+transactionaccountid+"] "+channelname+" ["+depositacct+"]");
	return list;
		
	}
	private List<String> getManner(){
		
		mlist=new ArrayList<String>();
		boolean bonus = mlist.add("红利再投");
		boolean cash = mlist.add("现金分红");
		
		return mlist;
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_Mode_change:
			
			params = new RequestParams(this);
			//params.put("id", encodeIdCard);
			params.put("sessionId", sessionId);
			//params.put("passwd", encodePassWord);
			params.put("transactorcerttype", "");
			params.put("fundcode", res.getFundcode());
			params.put("transactionaccountid", res.getTransactionaccountid());
			params.put("ratio", "1");
			params.put("branchcode", res.getBranchcode());
			if (bon==0) {
				params.put("dividendmethod", 0);
			}
			if (cas==1) {
				params.put("dividendmethod", 1);
			}
			params.put("transactorname", "");
			params.put("transactorcertno", "");
			execApi(ApiType.GET_SET_FUNDTWO, params);
			break;

		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("变更失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_SET_FUNDTWO) {

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
											
											if (xmlReturn.contains("appsheetserialno")) {
												JSONObject jsonObj = new JSONObject(xmlReturn);
												if (jsonObj.getString("appsheetserialno")!=null) {
													Intent intent=new Intent(AlterMmodeChangeActivity.this, ChangeSuccessActivity.class);
													startActivity(intent);
													finish();
													
												}else {
													String loginflag = jsonObj.getString("loginflag");
													showToast("对不起，方式变更失败！");
												}
												//queryAssetsActivity.instance.finish();
											}else {
												JSONObject object=new JSONObject(xmlReturn);
												String msg = object.getString("msg");
												showToastLong(msg);
											}
											
										} catch (JSONException e) {
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
								e.printStackTrace();
							}
						}
						
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				disMissDialog();
			}

		}

	}

}
