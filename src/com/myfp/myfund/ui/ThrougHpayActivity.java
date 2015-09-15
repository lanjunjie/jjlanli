package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class ThrougHpayActivity extends BaseActivity {

	private TextView tex_somegoods;
	private String unt;
	private TextView tex_passtype;
	private Intent intent;
	private String id;
	private RadioGroup radioGroup_Manner;
	private RadioButton rB;
	private String formtwo;
	private String username;
	private String mobile;
	private String displayName;
	private String iDCard;
	private RequestParams params;
	private String code,hint,info;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_throughpay);
		intent = getIntent();
		unt = intent.getStringExtra("Sume");
		id = intent.getStringExtra("ID");
		
		formtwo = intent.getStringExtra("Formtwo");
		SharedPreferences preferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		username = preferences.getString("UserName", null);
		System.out.println("username==========>" + username);
		RequestParams params = new RequestParams(this);
		params.put("username", username);
		params.put("id", id);
//		execApi(ApiType.GET_UPDATECOUPONS,params);
		execApi(ApiType.GET_USER_INFO, params);

	}

	@Override
	protected void initViews() {
		setTitle("购买点财通");
		tex_somegoods = (TextView) findViewById(R.id.tex_somegoods);
		tex_passtype = (TextView) findViewById(R.id.tex_passtype);
		findViewAddListener(R.id.but_throughubmit);
		tex_passtype.setText("点财通" + intent.getStringExtra("Form"));
		tex_somegoods.setText(intent.getStringExtra("Sume") + "元");
		radioGroup_Manner = (RadioGroup) findViewById(R.id.radioGroup_Manner);


		radioGroup_Manner
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						int RadioButtonId = arg0.getCheckedRadioButtonId();
						rB = (RadioButton) findViewById(RadioButtonId);
						System.out.println("rB================>" + rB);

					}
				});

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		super.onReceiveData(api, json);
		if (api == ApiType.GET_USER_INFO) {
			try {
				JSONArray array = new JSONArray(json);
				displayName = array.getJSONObject(0).getString("DisplayName");
				iDCard = array.getJSONObject(0).getString("IDCard");
				mobile = array.getJSONObject(0).getString("Mobile");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.but_throughubmit:
//			showToastLong(hint);
		
			if (rB==null) {
				params = new RequestParams(this);
				params.put("username", username);
				params.put("name", displayName);
				params.put("JiangLi", formtwo);
				params.put("UMobile", mobile);
				params.put("PaymentMethod", "2");

				params.put("op3_Amt", unt);

										Intent intent2 = new Intent(
												ThrougHpayActivity.this,
												SomeGoodsWebActivity.class);
										intent2.putExtra("Mobile", mobile);
										intent2.putExtra("DisplayName",
												displayName);
										intent2.putExtra("Unt", unt);
										intent2.putExtra("UserName", username);
										intent2.putExtra("Formtwo", formtwo);

										startActivity(intent2);

			} else if (rB.getText().equals("线下汇款")) {
				params = new RequestParams(this);
				params.put("username", username);
				params.put("name", displayName);
				params.put("JiangLi", formtwo);
				params.put("UMobile", mobile);
				params.put("PaymentMethod", "1");

				params.put("op3_Amt", unt);
				execApi(ApiType.GET_SOMEMONEYWAY, params,
						new OnDataReceivedListener() {

							@Override
							public void onReceiveData(ApiType api, String json) {
								Intent intent4 = new Intent(
										ThrougHpayActivity.this,
										SucceedorderdctActivity.class);
								intent4.putExtra("tex_passtype",
										tex_passtype.getText());
								startActivity(intent4);

							}

						});
			}

			break;

		default:
			break;
		}

	}

}
