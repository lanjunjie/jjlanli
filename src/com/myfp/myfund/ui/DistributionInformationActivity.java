package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.FinancingDialog;

public class DistributionInformationActivity extends BaseActivity{

	private String  proName, mobile;
	FinancingDialog dialog;
	private String userName;
	private String depositacctName;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_joinwith);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		proName = preferences.getString("ProName", null);
		Intent intent = getIntent();
		userName = intent.getStringExtra("UserName");
		depositacctName = intent.getStringExtra("DepositacctName");
	}

	@Override
	protected void initViews() {
		setTitle("我的资产-基金配资");
	
		findViewAddListener(R.id.tex_join);
		findViewAddListener(R.id.tex_ph_call);
		findViewAddListener(R.id.tex_admittance);
		findViewAddListener(R.id.tex_notice);
		TextView text_depos_v = (TextView) findViewById(R.id.text_depos_v);
		text_depos_v.setText(depositacctName);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		//加入
		case R.id.tex_join:
		//	userName = App.getContext().getUserName();
				showProgressDialog("正在加载");
				//proName = res.getFundName();
				RequestParams params = new RequestParams(
						getApplicationContext());
				params.put(RequestParams.USERNAME, userName);
				execApi(ApiType.GET_USER_INFO, params);

			
			break;
		//了解基金配资详情
		//case R.id.tex_fintheircing_housekeeper:
			//Intent intent1=new Intent(DistributionInformationActivity.this, FinancingWebActivity.class);
			//intent1.putExtra("ImageID", "05");
			//startActivity(intent1);
			//break;
		//预约基金配资服务
		
		case R.id.tex_ph_call:
			//电话
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);   
			intent.setData(Uri.parse("tel:010-56236258"));
			startActivity(intent);
			break;
		//会员准入条件
		case R.id.tex_admittance:
			Intent intent2=new Intent(this, EntryCriteriaActivity.class);
			startActivity(intent2);
			
			break;
		//配资须知
		case R.id.tex_notice:
			Intent intent3=new Intent(this, AndInstructionsActivity.class);
			startActivity(intent3);
			break;
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_USER_INFO) {

			try {
				JSONArray array = new JSONArray(json);
				mobile = array.getJSONObject(0).getString("Mobile");
				// 初始化一个自定义的Dialog
				dialog = new FinancingDialog(DistributionInformationActivity.this, depositacctName,
						proName, mobile, R.style.FinancingDialog,
						new FinancingDialog.DialogListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								switch (v.getId()) {
								case R.id.tv_financingdialog_changephone:
									Intent Mobile = new Intent(
											DistributionInformationActivity.this,
											ChangeMobileActivity.class);
										Mobile.putExtra("UserName", userName);
									startActivity(Mobile);

									break;
								case R.id.tv_financingdialog_kefu:
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_DIAL);
									intent.setData(Uri
											.parse("tel:400-888-6661"));
									startActivity(intent);

									break;
								case R.id.tv_financingdialog_ordernow:
									showProgressDialog("正在预约");
									RequestParams params = new RequestParams(
											DistributionInformationActivity.this);
									//params.put("productname", proName);
									params.put("username", userName);
									execApi(ApiType.GET_ANAPPOINTMENTTWO,
											params);

									break;
								case R.id.tv_financingdialog_orderlater:
									dialog.dismiss();
									break;

								default:
									break;
								}

							}
						});
				// 设置它的ContentView

				dialog.show();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(api == ApiType.GET_ANAPPOINTMENTTWO){
			try {
				JSONObject jsonobj = new JSONObject(json);
				if(jsonobj.getString("Code").equals("0000")){
					disMissDialog();
					showToast("预约成功");
					startActivity(ProductOrderSuccessActivity.class);
					dialog.dismiss();
				}else{
					disMissDialog();
					showToast("预约失败");
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		disMissDialog();
	}

}
