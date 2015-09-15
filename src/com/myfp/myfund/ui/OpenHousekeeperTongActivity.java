package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.ButlertongDialog;
import com.myfp.myfund.ui.view.OrderDialog;

public class OpenHousekeeperTongActivity extends BaseActivity{
	
	private String userName, proName, mobile;
	ButlertongDialog dialog;
	private String depositacctName;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_customization_outsider);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		proName = preferences.getString("ProName", null);
		System.out.println("proName-=-=-=-=-=-=-=->"+proName);
		Intent intent = getIntent();
		userName = intent.getStringExtra("UserName");
		depositacctName = intent.getStringExtra("DepositacctName");
		
	}

	@Override
	protected void initViews() {
		setTitle("我的资产--管家通会员");
		findViewAddListener(R.id.tex_subscribe);
		findViewAddListener(R.id.tex_housekeeper_on);
		findViewAddListener(R.id.tex_make_an);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		//预约
		case R.id.tex_subscribe:
			
				showProgressDialog("正在加载");
				//proName = res.getFundName();
				RequestParams params = new RequestParams(
						getApplicationContext());
				params.put(RequestParams.USERNAME, userName);
				execApi(ApiType.GET_USER_INFO, params);

			
			break;
		//了解管家通详情
		case R.id.tex_housekeeper_on:
			Intent intent2=new Intent(this, TheDetailsActivity.class);
			intent2.putExtra("ImageID", "06");
			startActivity(intent2);
			break;
		//立即预约管家通会员
		case R.id.tex_make_an:
		
				showProgressDialog("正在加载");
				//proName = res.getFundName();
				RequestParams pms = new RequestParams(
						getApplicationContext());
				pms.put(RequestParams.USERNAME, userName);
				execApi(ApiType.GET_USER_INFO, pms);

			
			break;
		default:
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
				dialog = new ButlertongDialog(OpenHousekeeperTongActivity.this, depositacctName,
						proName, mobile, R.style.ButlertongDialog,
						new ButlertongDialog.ButDialogListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								switch (v.getId()) {
								case R.id.tv_butlertongdialog_changephone:
									Intent cMobile = new Intent(
											OpenHousekeeperTongActivity.this,
											ChangeMobileActivity.class);
									cMobile.putExtra("UserName", userName);
									startActivity(cMobile);

									break;
								case R.id.tv_butlertongdialog_kefu:
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_DIAL);
									intent.setData(Uri
											.parse("tel:400-888-6661"));
									startActivity(intent);

									break;
								case R.id.tv_butlertongdialog_ordernow:
									showProgressDialog("正在预约");
									RequestParams params = new RequestParams(
											OpenHousekeeperTongActivity.this);
									//params.put("productname", proName);
									params.put("username", userName);
									execApi(ApiType.GET_HOUSEKEEPERFLUXTWO,
											params);

									break;
								case R.id.tv_butlertongdialog_orderlater:
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
		}else if(api == ApiType.GET_HOUSEKEEPERFLUXTWO){
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
