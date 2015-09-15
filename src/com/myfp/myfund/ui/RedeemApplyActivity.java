package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.DealSearchResult;
import com.myfp.myfund.api.beans.RedeemSearchResult;
import com.myfp.myfund.ui.DealBuyActivity.DealSearchAdapter;
import com.myfp.myfund.utils.CnUpperCaser;

public class RedeemApplyActivity extends BaseActivity {
	private String encodeIdCard, encodePassWord, fundCode, fundName, fundType,
			applicationamount, fundstatus, vastredeemflag, tano,per_min_24,per_max_24;
	private RedeemSearchResult res;
	ByteArrayInputStream tInputStringStream = null;
	Bundle bundle;
	TextView tv_redeem_fundName, tv_redeem_fundCode, tv_redeem_chiyoufene,
			tv_redeem_dongjiefene, tv_redeem_keyongfene,tv_redeem_range,tv_redeem_feneDX;
	EditText ed_redeem_shuhuifene;
	private static final String[] m = { "取消", "顺延" };
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	public static RedeemApplyActivity instance = null;
	private String sessionId;
	private String minFundvol;
	private int i;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_redeemapply);
		instance = this;
		bundle = getIntent().getExtras();
		res = (RedeemSearchResult) bundle.getSerializable("RedeemSearchResult");
		encodeIdCard = bundle.getString("IDCard");
		encodePassWord = bundle.getString("PassWord");
		sessionId = bundle.getString("sessionId");
		fundCode = res.getFundcode();
		fundName = res.getFundname();
		fundType = res.getFundtype();
		fundstatus = res.getStatus();
		
		tano = res.getTano();
		RequestParams params = new RequestParams(this);
		//params.put("id", encodeIdCard);
	//	params.put("passwd", encodePassWord);
		params.put("sessionId", sessionId);
		params.put("condition", fundCode);
		params.put("fundType", null);
		params.put("company", null);
		execApi(ApiType.GET_DEALSEARCHONETWO, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("基金赎回");
		tv_redeem_fundName = (TextView) findViewById(R.id.tv_redeem_fundName);
		tv_redeem_fundCode = (TextView) findViewById(R.id.tv_redeem_fundCode);
		tv_redeem_chiyoufene = (TextView) findViewById(R.id.tv_redeem_chiyoufene);
		tv_redeem_dongjiefene = (TextView) findViewById(R.id.tv_redeem_dongjiefene);
		tv_redeem_keyongfene = (TextView) findViewById(R.id.tv_redeem_keyongfene);
		tv_redeem_range = (TextView)findViewById(R.id.tv_redeem_range);
		tv_redeem_feneDX = (TextView)findViewById(R.id.tv_redeem_feneDX);
		ed_redeem_shuhuifene = (EditText)findViewById(R.id.ed_redeem_shuhuifene);
		
		spinner = (Spinner) findViewById(R.id.sp_redeem_spinner);
		// 将可选内容与ArrayAdapter连接起来
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m);

		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

		tv_redeem_fundName.setText(fundName);
		tv_redeem_fundCode.setText("["+fundCode+"]");
		tv_redeem_chiyoufene.setText(res.getFundvolbalance());
		tv_redeem_dongjiefene.setText(res.getTrdfrozen());
		tv_redeem_keyongfene.setText(res.getAvailablevol());
		ed_redeem_shuhuifene.setText(res.getAvailablevol());
		tv_redeem_feneDX.setText(new CnUpperCaser(ed_redeem_shuhuifene.getText().toString()).getCnString()+"(份)");
		ed_redeem_shuhuifene.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(!(TextUtils.isDigitsOnly(ed_redeem_shuhuifene.getText().toString())||ed_redeem_shuhuifene.getText().toString().contains("."))){
					tv_redeem_feneDX.setText("");
				}else if(ed_redeem_shuhuifene.getText().toString().contains(" ")){
					tv_redeem_feneDX.setText("");
				}else{
					tv_redeem_feneDX.setText(new CnUpperCaser(ed_redeem_shuhuifene.getText().toString()).getCnString()+"份");
				}
			}
		});
		
		
		findViewAddListener(R.id.bt_applyredeem);
	}
	
	 //使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
      
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//view.setText("你的血型是："+m[arg2]);
			String redeemflag = m[arg2];
			if(redeemflag.equals("取消")){
				vastredeemflag = "0";
			}else{
				vastredeemflag = "1";
			}
			
			System.out.println(vastredeemflag);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			String redeemflag = m[0];
			if(redeemflag.equals("顺延")){
				vastredeemflag = "1";
			}else{
				vastredeemflag = "0";
			}
			System.out.println(vastredeemflag);
		}
    }

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_applyredeem:
			bundle.putString("RedeemFenE", ed_redeem_shuhuifene.getText().toString().trim());
			bundle.putString("VastRedeemFlag", vastredeemflag);
			if (!(ed_redeem_shuhuifene.getText().toString().trim().length()==0)) {
				
				switch (i) {
				case 1:
					if(minFundvol!=null){
						if (!ed_redeem_shuhuifene.getText().toString().equals(minFundvol)) {
							showToast("份额输入有误，请重新输入！");
							return;
						}else{
							Intent intent = new Intent(RedeemApplyActivity.this,
									RedeemConfirmActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
					break;
					
				case 2:
					if ((Double.parseDouble(ed_redeem_shuhuifene.getText().toString().trim()) > (Double.parseDouble(tv_redeem_keyongfene.getText().toString().trim())))
							
							) {
						
						showToast("份额输入有误，请重新输入！");
						return;

					}else if(ed_redeem_shuhuifene.getText().toString().trim().equals("0")){
						showToast("份额不能为0，请重新输入！");
						return;
						
					}
					else{
						Intent intent = new Intent(RedeemApplyActivity.this,
								RedeemConfirmActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				break;
				
				}
				//当输入赎回份额小于最小份额的时候或输入的赎回份额大于可用份额的时候，提示份额输入有误
				if ((Double.parseDouble(ed_redeem_shuhuifene.getText().toString().trim()) > (Double.parseDouble(tv_redeem_keyongfene.getText().toString().trim())))
						
						) {
					
					showToast("份额输入有误，请重新输入！");
					return;

				}else if(ed_redeem_shuhuifene.getText().toString().trim().equals("0")){
					showToast("份额不能为0，请重新输入！");
					return;
					
				}
			break;

		default:
			break;
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
								try {
									JSONArray array = new JSONArray(xmlReturn);
									JSONObject obj = array.getJSONObject(0);
									per_min_24 = obj.getString("per_min_24");
									per_max_24 = obj.getString("per_max_24");
								//	tv_redeem_range.setText("本基金赎回范围（" + per_min_24 +"份"+"~"
								//			+ tv_redeem_keyongfene.getText().toString().trim() + "份）");
									if (Double.parseDouble(res.getAvailablevol())<Double.parseDouble(per_min_24.toString().trim())) {
										if (Double.parseDouble(res.getFundvolbalance())<Double.parseDouble(per_min_24.toString().trim())) {
											minFundvol = res.getFundvolbalance();
											tv_redeem_range.setText("您本基金赎回范围（"+minFundvol+"）份");
											i = 1;
											
										}else {
											String max = res.getFundvolbalance();
											String min = per_min_24.toString().trim();
											tv_redeem_range.setText("您本基赎回范围（"+min+"份~"+max+"）份");
											i=2;
											
										}
									}else if(Double.parseDouble(res.getAvailablevol())>Double.parseDouble(per_max_24.toString().trim())){
										String max = per_max_24.toString().trim();
										String min = per_min_24.toString().trim();
										tv_redeem_range.setText("您本基赎回范围（"+min+"份~"+max+"）份");
										i=2;
									}else {
										String max = res.getAvailablevol();
										String min = per_min_24.toString().trim();
										tv_redeem_range.setText("您本基赎回范围（"+min+"份~"+max+"）份");
										i=2;
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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
