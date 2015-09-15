package com.myfp.myfund.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import u.aly.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class PaymentActivity extends BaseActivity{

	private ImageView imag_choice_online;
	private ImageView imag_choice_offline;
	private int on=2;
	private String code;
	private String hint;
	private String info;
	private String name;
	private String mobile;
	private String displayname;
	private String username;
	private String in="会员费(380元/年)";
	private String pidString="展恒点融通";
	private String disp;
	private String url;
	private String strin;
	private String pid;
	private String idcard;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_payment);

		Intent intent = getIntent();
		 mobile = intent.getStringExtra("Mobile");
		 displayname = intent.getStringExtra("DisplayName");
		 username = intent.getStringExtra("UserName");
		 idcard = intent.getStringExtra("IDCard");
		
		try {
			disp = URLEncoder.encode(displayname,"GB2312");
			strin = URLEncoder.encode(in,"GB2312");
			pid = URLEncoder.encode(pidString,"GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void initViews() {
		setTitle("我的资产-基金配资");
		imag_choice_online = (ImageView) findViewById(R.id.imag_choice_online);
		imag_choice_offline = (ImageView) findViewById(R.id.imag_choice_offline);
		findViewAddListener(R.id.tex_in);
		findViewAddListener(R.id.tex_service_ch);
		findViewAddListener(R.id.tex_affirm_payment);
		findViewAddListener(R.id.tex_dead);
		imag_choice_offline.setImageResource(R.drawable.imag_select);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		//网上支付和线下汇款
		case R.id.tex_affirm_payment:
			RequestParams pa=new RequestParams(this);
			pa.put("username", username);
			pa.put("PaymentMethod", on);
			execApi(ApiType.GET_PAYWAY, pa);

			break;
		//我要配资
		case R.id.tex_dead:
			RequestParams parm=new RequestParams(this);
			parm.put("username", username);
			execApi(ApiType.GET_WHETHERPAYMENT, parm, new OnDataReceivedListener() {
				
				@Override
				public void onReceiveData(ApiType api, String json) {
					try {
						JSONObject object=new JSONObject(json);
						String code = object.getString("Code");
						String hint = object.getString("Hint");
						String info = object.getString("Info");
						if (code.equals("0")) {
							showToast("您还没有支付成功，请继续支付！");
						}else if (code.equals("1")) {
							Intent intent=new Intent(PaymentActivity.this, MyFundsWithCapitalActivity.class);
							intent.putExtra("idcard", idcard);
							startActivity(intent);
						}else if (code.equals("500")&&hint.equals("信息错误,您有多个信息")&&info.equals("false")) {
							showToast("对不起,您的信息有误！");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
			
			break;
		case R.id.tex_in:
			on=1;

			imag_choice_online.setVisibility(View.VISIBLE);
			imag_choice_online.setImageResource(R.drawable.imag_select);
			imag_choice_offline.setVisibility(View.GONE);
			break;
		case R.id.tex_service_ch:
			on=2;
			imag_choice_offline.setVisibility(View.VISIBLE);
			imag_choice_online.setVisibility(View.GONE);
			imag_choice_offline.setImageResource(R.drawable.imag_select);
				
			break;
		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("网络不给力！");
			disMissDialog();
			return;
			
		}
		if (api==ApiType.GET_PAYWAY) {
			System.out.println("json=====>"+json);
			JSONObject object;
			try {
				object = new JSONObject(json);
				System.out.println("object=========>"+object);
				code = object.getString("Code");
				System.out.println("code------>"+code);
				hint = object.getString("Hint");
				info = object.getString("Info");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (code.equals("0000")&&hint.equals("成功")&&info.equals("true")) {
				if (on==2) {
					Intent intent1=new Intent(this, PayWebActivity.class);
					intent1.putExtra("Mobile", mobile);
					intent1.putExtra("DisplayName", displayname);
					intent1.putExtra("UserName", username);
					startActivity(intent1);
				
					
				}else if(on==1){
					Intent intent=new Intent(this, OfflineRemittanceActivity.class);
					startActivity(intent);
					
				}
				
			}
		}
	}

}
