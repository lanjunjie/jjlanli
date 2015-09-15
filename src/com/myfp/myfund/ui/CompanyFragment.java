package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CurrentProductResult;

public class CompanyFragment extends BaseFragment implements
		OnDataReceivedListener2 {
	int index;
	private String procode;
	private ProductDetailActivity activity;
	TextView tv_company_companyname, tv_company_place, tv_company_ziben,
			tv_company_guimo, tv_company_renwu, tv_company_createdate,
			tv_company_beianhao, tv_company_linian, tv_company_renwuinfor,
			tv_company_jiangxiang;
	ListView list_company_funds;
	List<CurrentProductResult> results;
	private String userName, agentScode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		procode = bundle.getString("FundCode");
		activity = (ProductDetailActivity) getActivity();

		RequestParams params = new RequestParams(activity);
		params.put("proCode", procode);
		activity.execApi(ApiType.GET_CODE, params, this);
		activity.showProgressDialog("正在加载");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userName = App.getContext().getUserName();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_company, null, false);

		tv_company_companyname = (TextView) view
				.findViewById(R.id.tv_company_companyname);
		tv_company_place = (TextView) view.findViewById(R.id.tv_company_place);
		tv_company_ziben = (TextView) view.findViewById(R.id.tv_company_ziben);
		tv_company_guimo = (TextView) view.findViewById(R.id.tv_company_guimo);
		tv_company_renwu = (TextView) view.findViewById(R.id.tv_company_renwu);
		tv_company_createdate = (TextView) view
				.findViewById(R.id.tv_company_createdate);
		tv_company_beianhao = (TextView) view
				.findViewById(R.id.tv_company_beianhao);
		tv_company_linian = (TextView) view
				.findViewById(R.id.tv_company_linian); 
		tv_company_renwuinfor = (TextView) view
				.findViewById(R.id.tv_company_renwuinfor);

		tv_company_jiangxiang = (TextView) view
				.findViewById(R.id.tv_company_jiangxiang);

		list_company_funds = (ListView) view
				.findViewById(R.id.list_company_funds);
		return view;
	}

	@Override
	public void onReceiveData(ApiType api, RequestParams params, String json) {
		// 在这里会收到数据,一定要判断是否为空
		if (json == null) {
			activity.disMissDialog();
			activity.showToast("获取失败!");
			return;
		}
		try {
			if (api == ApiType.GET_CODE) {
				JSONArray array = new JSONArray(json);
				JSONObject jsonobj = array.getJSONObject(0);

				String companycode = jsonobj.getString("CompanyCode");
				RequestParams params1 = new RequestParams(activity);
				params1.put("CompCode", companycode);
				activity.execApi(ApiType.GET_FUNDCOMPANY, params1, this);
				activity.execApi(ApiType.GET_SELLINGPRODUCTS, params1, this);

			} else if (api == ApiType.GET_FUNDCOMPANY) {
				JSONArray array = new JSONArray(json);
				JSONObject jsonobj = array.getJSONObject(0);

				tv_company_companyname.setText(jsonobj
						.getString("CompName_Full"));
				tv_company_place.setText(jsonobj.getString("IssuePlace"));
				tv_company_ziben
						.setText(jsonobj.getString("Reg_Capital") + "万");
				tv_company_guimo.setText(jsonobj.getString("SumPerson"));
				tv_company_renwu.setText(jsonobj.getString("KeyPeople"));
				tv_company_createdate.setText(jsonobj
						.getString("ThisYearYield").split(" ")[0].replaceAll(
						"-", "/"));
				tv_company_beianhao.setText(jsonobj.getString("CompRecord"));
				tv_company_linian.setText(jsonobj.getString("Invest_Idea"));
				tv_company_renwuinfor.setText(jsonobj.getString("Summary"));
				tv_company_jiangxiang.setText(jsonobj.getString("Reward"));

			} else if (api == ApiType.GET_SELLINGPRODUCTS) {
				results = JSON.parseArray(json, CurrentProductResult.class);
				list_company_funds.setAdapter(new CompanyListAdapter(results));
				setListViewHeightBasedOnChildren(list_company_funds);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		activity.disMissDialog();
	}

	class CompanyListAdapter extends BaseAdapter {
		private List<CurrentProductResult> data;

		public CompanyListAdapter(List<CurrentProductResult> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			convertView = View.inflate(activity, R.layout.item_currentproduct,
					null);
			holder = new ViewHolder();
			holder.tv_manager_fundname = (TextView) convertView
					.findViewById(R.id.tv_manager_fundname);
			holder.tv_manager_fundmanager = (TextView) convertView
					.findViewById(R.id.tv_manager_fundmanager);
			holder.tv_manager_equity = (TextView) convertView
					.findViewById(R.id.tv_manager_equity);
			holder.tv_manager_equitydate = (TextView) convertView
					.findViewById(R.id.tv_manager_equitydate);
			holder.tv_manager_jinnianyilai = (TextView) convertView
					.findViewById(R.id.tv_manager_jinnianyilai);
			holder.tv_manager_jinyinian = (TextView) convertView
					.findViewById(R.id.tv_manager_jinyinian);
			holder.tv_manager_createdate = (TextView) convertView
					.findViewById(R.id.tv_manager_createdate);
//			holder.bt_manager_order = (Button) convertView
//					.findViewById(R.id.bt_manager_order);

			convertView.setTag(holder);

			final CurrentProductResult cp = data.get(position);

			holder.tv_manager_fundname.setText(cp.getFundName());
			holder.tv_manager_fundmanager.setText(cp.getManager());

			holder.tv_manager_equity.setText(cp.getUnitEquity());
			holder.tv_manager_equitydate
					.setText("("
							+ cp.getDealDate().split(" ")[0].replaceAll("-",
									"/") + ")");
			holder.tv_manager_jinnianyilai.setText(cp.getThisYearYield());
			holder.tv_manager_jinyinian.setText(cp.getOneYearYield());

			holder.tv_manager_createdate.setText(cp.getEst_date().split(" ")[0]
					.replaceAll("-", "/"));

//			holder.bt_manager_order.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					if (userName == null) {
//						activity.showToast("未登录，请先登录！");
//						Intent intent = new Intent(activity,
//								LoginActivity.class);
//						// Bundle bundle = new Bundle();
//						// bundle.putSerializable("ProductList", pl);
//						// intent.putExtras(bundle);
//						activity.startActivity(intent);
//						return;
//					} else {
//						System.out.println("_+_+_+_+_+_+_+_+_+"+cp.getFundName());
//						
//						activity.showProgressDialog("正在加载");
//						RequestParams params1 = new RequestParams(activity);
//						params1.put("proName", cp.getFundName().toString());
//						activity.execApi(ApiType.GET_PRODUCTDETAIL, params1,
//								new OnDataReceivedListener() {
//
//									@Override
//									public void onReceiveData(ApiType api,
//											String json) {
//										if (json == null) {
//											activity.showToast("请求失败!");
//											return;
//										}
//										List<ProductList> result;
//										result = JSON.parseArray(json,
//												ProductList.class);
//										if(result.size() == 0){
//											activity.showToast("请求失败");
//											activity.disMissDialog();
//											return;
//										}
//										
//										Intent intent = new Intent(activity,
//												OrderActivity.class);
//										Bundle bundle = new Bundle();
//										bundle.putSerializable("ProductList",
//												result.get(0));
//										bundle.putString("UserName", userName);
//										bundle.putString("AgentScode",
//												agentScode);
//										intent.putExtras(bundle);
//										// intent.putExtra("UserName",
//										// userName);
//										startActivity(intent);
//										activity.disMissDialog();
//									}
//								});
//					}
//
//				}
//			});

			return convertView;
		}

		class ViewHolder {
			TextView tv_manager_fundname, tv_manager_fundmanager,
					tv_manager_equity, tv_manager_equitydate,
					tv_manager_jinnianyilai, tv_manager_jinyinian,
					tv_manager_createdate;
			Button bt_manager_order;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		CompanyListAdapter listAdapter = (CompanyListAdapter) listView
				.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

}
