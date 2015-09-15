package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MyHoldAPrivateResutl;
import com.myfp.myfund.api.beans.PlacementResult;
import com.myfp.myfund.ui.PlacementActivty.PlacementAdapter;
import com.myfp.myfund.ui.PlacementActivty.PlacementAdapter.ViewHolder;
import com.myfp.myfund.ui.view.OrderDialog;

public class MyPrivateProductsActivity extends BaseActivity{
/*	private FragmentManager fmr = getSupportFragmentManager();
	private FragmentTransaction fragmentTran = getSupportFragmentManager().beginTransaction();
	
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.layout_fragment);
		fragmentTran.replace(R.id.frag, new MyPrivateProductsFragment());
		fragmentTran.commit();
	}

	@Override
	protected void initViews() {
		setTitle("我的资产--私募产品");
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
	}
	*/
	private ListView list_view_placement;
	private String userName, proName, mobile;
	OrderDialog dialog;
	List<PlacementResult> results;
	private String depositacctName;
	private Button right;
	private ListView list_have_placement;
	private LinearLayout layout;
	private TextView text_v;
	private List<MyHoldAPrivateResutl> array;
	private TextView te_recommend;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_privatenot_listview);
		RequestParams params=new RequestParams(this);
		params.put("sdzjnumber".trim(), App.getContext().getIdCard());
		execApi(ApiType.GET_HOLDAPRIVATE,params,this);
		showProgressDialog("正在加载");
	

	}

	@Override
	protected void initViews() {
		setTitle("私募基金");
		list_view_placement = (ListView) findViewById(R.id.list_not_placement);
		list_have_placement = (ListView) findViewById(R.id.list_have_placement);
		te_recommend = (TextView) findViewById(R.id.te_recommend);
		layout = (LinearLayout) findViewById(R.id.layout);
		text_v = (TextView) findViewById(R.id.text_v);
		te_recommend.setVisibility(View.GONE);
		list_view_placement.setVisibility(View.GONE);
		list_have_placement.setVisibility(View.GONE);
		layout.setVisibility(View.GONE);
		text_v.setVisibility(View.GONE);
		
		right=(Button) findViewById(R.id.tv_text_main_publish);
		right.setText("交易记录");
		right.setVisibility(View.VISIBLE);
		right.setTextSize(18);
		right.getBackground().setAlpha(0);
		right.setOnClickListener(this);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.tv_text_main_publish:
			App.getContext().getIdCard();
			Intent intent=new Intent(this, MyDealRecord.class);
			intent.putExtra("idcard", App.getContext().getIdCard());
			startActivity(intent);
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
			if (api==ApiType.GET_HOLDAPRIVATE) {
			if (json.equals("[]")) {
					list_view_placement.setVisibility(View.VISIBLE);
					layout.setVisibility(View.VISIBLE);
					text_v.setVisibility(View.VISIBLE);
					te_recommend.setVisibility(View.VISIBLE);
					execApi(ApiType.GET_PLACEMENTTWOT, null);
		}else {
			list_have_placement.setVisibility(View.VISIBLE);
			text_v.setVisibility(View.VISIBLE);
			array = JSON.parseArray(json, MyHoldAPrivateResutl.class);
			list_have_placement.setAdapter(new MyHavePrivateAdapter(array,this));
			
		}
			}else 
			if (api == ApiType.GET_PLACEMENTTWOT) {
				results = JSON.parseArray(json, PlacementResult.class);
				list_view_placement.setAdapter(new PlacementAdapter(results));
				list_view_placement
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								PlacementResult res = results.get(arg2);
								String personID = res.getPersonID();
								System.out.println("personID1-------->"+personID);
								Intent intent = new Intent(
										MyPrivateProductsActivity.this,
										ProductDetailActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("PlacementResult", res);
								intent.putExtras(bundle);
								startActivity(intent);

							}
						});
			} else if (api == ApiType.GET_USER_INFO) {

				try {
					JSONArray array = new JSONArray(json);
					mobile = array.getJSONObject(0).getString("Mobile");
					// 初始化一个自定义的Dialog
					dialog = new OrderDialog(MyPrivateProductsActivity.this, depositacctName,
							proName, mobile, R.style.OrderDialog,
							new OrderDialog.MyDialogListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									switch (v.getId()) {
									case R.id.tv_orderdialog_changephone:
										Intent cMobile = new Intent(
												MyPrivateProductsActivity.this,
												UpdataPhoneAcitivity.class);
										cMobile.putExtra("UserName", userName);
										cMobile.putExtra("IDCard", App.getContext().getIdCard());
										startActivity(cMobile);

										break;
									case R.id.tv_orderdialog_kefu:
										Intent intent = new Intent();
										intent.setAction(Intent.ACTION_DIAL);
										intent.setData(Uri
												.parse("tel:400-888-6661"));
										startActivity(intent);

										break;
									case R.id.tv_orderdialog_ordernow:
										showProgressDialog("正在预约");
										RequestParams params = new RequestParams(
												MyPrivateProductsActivity.this);
										params.put("productname", proName);
										params.put("username", userName);
										execApi(ApiType.GET_APPOINTMENTFUNDINFO,
												params);

										break;
									case R.id.tv_orderdialog_orderlater:
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
			}else if(api == ApiType.GET_APPOINTMENTFUNDINFO){
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

		}
		disMissDialog();
	}

	class PlacementAdapter extends BaseAdapter {
		List<PlacementResult> mList;
		private String date;

		public PlacementAdapter(List<PlacementResult> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
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
			if (convertView == null) {
				convertView = LayoutInflater.from(MyPrivateProductsActivity.this)
						.inflate(R.layout.item_placement, null);
				holder = new ViewHolder();
				holder.text_headline = (TextView) convertView
						.findViewById(R.id.text_headline);
				holder.text_amaldar_name = (TextView) convertView
						.findViewById(R.id.text_amaldar_name);
				holder.text_date = (TextView) convertView
						.findViewById(R.id.text_date);
				holder.text_view_earnings = (TextView) convertView
						.findViewById(R.id.text_view_earnings);
				holder.text_origin = (TextView) convertView
						.findViewById(R.id.text_origin);
				holder.bt_subscribe = (Button) convertView
						.findViewById(R.id.bt_subscribe);
				holder.button_xq=(Button)convertView.findViewById(R.id.button_xq);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final PlacementResult res = mList.get(position);
			if (!"".equals(res.getFundName())) {
				holder.text_headline.setText(res.getFundName());
			} else {
				holder.text_headline.setText("--");
			}
			if (!"".equals(res.getName())) {
				holder.text_amaldar_name.setText(res.getName());
			} else {
				holder.text_amaldar_name.setText("--");
			}
			// date =(String) res.getDates().subSequence(0,8);
			String data = res.getDates().split(" ")[0];
			date = data.replaceAll("-", "/");
			if (!"".equals(res.getDates())) {
				holder.text_date.setText(date);
			} else {
				holder.text_date.setText("--");

			}
			if (!"".equals(res.getWholeYield())) {
				holder.text_view_earnings.setText(res.getWholeYield());
			} else {
				holder.text_view_earnings.setText("--");

			}
			if (!"".equals(res.getMoney())) {
				holder.text_origin.setText(res.getMoney() + "万");
			} else {
				holder.text_origin.setText("--");
			}
			System.out.println("date------------->" + date);

			holder.bt_subscribe.setOnClickListener(new OnClickListener() {

				

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences sPreferences = getSharedPreferences("Setting",
							MODE_PRIVATE);
					 userName = sPreferences.getString("UserName", null);
					 depositacctName = sPreferences.getString("DepositacctName", null);
					//userName = App.getContext().getUserName();
					if (userName == null) {
						showToast("没有登录，请登录！");
						Intent intent = new Intent(MyPrivateProductsActivity.this,
								MyMeansActivity.class);
						startActivity(intent);
						finish();
					} else {
						showProgressDialog("正在加载");
						proName = res.getFundName();
						RequestParams params = new RequestParams(
								getApplicationContext());
						params.put(RequestParams.USERNAME, userName);
						execApi(ApiType.GET_USER_INFO, params);

					}

				}
			});
			holder.button_xq.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(
							MyPrivateProductsActivity.this,
							ProductDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("PlacementResult", res);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView text_headline, text_amaldar_name, text_date,
					text_view_earnings, text_origin;
			Button bt_subscribe,button_xq;
		}

	}
}
