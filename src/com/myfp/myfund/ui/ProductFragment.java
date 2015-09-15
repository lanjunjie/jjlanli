package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.ProductDetailResult;

public class ProductFragment extends BaseFragment implements
		OnDataReceivedListener2 {
	int index;
	private String procode;

	private ProductDetailActivity activity;
	TextView tv_product_proname, tv_product_procode, tv_product_unitequity,
			tv_product_date, tv_product_protype, tv_product_celue,
			tv_product_jinyinian, tv_product_startpoint, tv_product_createdate,
			tv_product_fundmanager, tv_product_fundcompany,
			tv_product_zd_onemonth, tv_product_zd_threemonth,
			tv_product_zd_sixmonth, tv_product_zd_oneyear,
			tv_product_hs_onemonth, tv_product_hs_threemonth,
			tv_product_hs_sixmonth, tv_product_hs_oneyear,
			tv_product_xd_onemonth, tv_product_xd_threemonth,
			tv_product_xd_sixmonth, tv_product_xd_oneyear,
			tv_product_celueinfor;

	ListView list_product_historyequity;
	List<ProductDetailResult> results;
	private TextView tv_fund_trustee;
	private TextView tv_management_cost;
	private TextView tv_sales_charge;
	private TextView tv_closed_period;
	private TextView tv_stop_lossline;
	private TextView tv_opent_day;
	private TextView tv_security_middleman;
	private TextView tv_trustee_feet;
	private TextView tv_rewarding_bymerit;
	private TextView tv_precautious_line;
	private TextView tv_redemption_rate;
	private TextView tv_screen_name;
	private TextView tv_opening_bank;
	private TextView tv_account_numbertwo;
	private TextView tv_payment_system;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		procode = bundle.getString("FundCode");
		System.out.println("Fundcode---------->"+procode);
		activity = (ProductDetailActivity) getActivity();

		RequestParams params = new RequestParams(activity);
		params.put("Fundcode", procode);
		activity.execApi(ApiType.GET_FUNDINFOR, params, this);
		activity.execApi(ApiType.GET_HISTORYRATE, params, this);
		activity.execApi(ApiType.GET_EQUITYCHART, params, this);
		activity.showProgressDialog("正在加载");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_product, null, false);
		tv_product_proname = (TextView) view
				.findViewById(R.id.tv_product_proname);
		tv_product_procode = (TextView) view
				.findViewById(R.id.tv_product_procode);
		tv_product_unitequity = (TextView) view
				.findViewById(R.id.tv_product_unitequity);
		tv_product_date = (TextView) view.findViewById(R.id.tv_product_date);
		tv_product_protype = (TextView) view
				.findViewById(R.id.tv_product_protype);
		tv_product_celue = (TextView) view.findViewById(R.id.tv_product_celue);
		tv_product_jinyinian = (TextView) view
				.findViewById(R.id.tv_product_jinyinian);
		tv_product_startpoint = (TextView) view
				.findViewById(R.id.tv_product_startpoint);
		tv_product_createdate = (TextView) view
				.findViewById(R.id.tv_product_createdate);
		tv_product_fundmanager = (TextView) view
				.findViewById(R.id.tv_product_fundmanager);
		tv_product_fundcompany = (TextView) view
				.findViewById(R.id.tv_product_fundcompany);
		tv_product_zd_onemonth = (TextView) view
				.findViewById(R.id.tv_product_zd_onemonth);
		tv_product_zd_threemonth = (TextView) view
				.findViewById(R.id.tv_product_zd_threemonth);
		tv_product_zd_sixmonth = (TextView) view
				.findViewById(R.id.tv_product_zd_sixmonth);
		tv_product_zd_oneyear = (TextView) view
				.findViewById(R.id.tv_product_zd_oneyear);
		tv_product_hs_onemonth = (TextView) view
				.findViewById(R.id.tv_product_hs_onemonth);
		tv_product_hs_threemonth = (TextView) view
				.findViewById(R.id.tv_product_hs_threemonth);
		tv_product_hs_sixmonth = (TextView) view
				.findViewById(R.id.tv_product_hs_sixmonth);
		tv_product_hs_oneyear = (TextView) view
				.findViewById(R.id.tv_product_hs_oneyear);
		tv_product_xd_onemonth = (TextView) view
				.findViewById(R.id.tv_product_xd_onemonth);
		tv_product_xd_threemonth = (TextView) view
				.findViewById(R.id.tv_product_xd_threemonth);
		tv_product_xd_sixmonth = (TextView) view
				.findViewById(R.id.tv_product_xd_sixmonth);
		tv_product_xd_oneyear = (TextView) view
				.findViewById(R.id.tv_product_xd_oneyear);
		tv_product_celueinfor = (TextView) view
				.findViewById(R.id.tv_product_celueinfor);
		
		tv_fund_trustee = (TextView) view.findViewById(R.id.tv_fund_trustee);
		tv_management_cost = (TextView) view.findViewById(R.id.tv_management_cost);
		tv_sales_charge = (TextView) view.findViewById(R.id.tv_sales_charge);
		tv_closed_period = (TextView) view.findViewById(R.id.tv_closed_period);
		tv_stop_lossline = (TextView) view.findViewById(R.id.tv_stop_lossline);
		tv_opent_day = (TextView) view.findViewById(R.id.tv_opent_day);
		tv_security_middleman = (TextView) view.findViewById(R.id.tv_security_middleman);
		tv_trustee_feet = (TextView) view.findViewById(R.id.tv_trustee_feet);
		tv_rewarding_bymerit = (TextView) view.findViewById(R.id.tv_rewarding_bymerit);
		tv_precautious_line = (TextView) view.findViewById(R.id.tv_precautious_line);
		tv_redemption_rate = (TextView) view.findViewById(R.id.tv_redemption_rate);
		
		
		tv_screen_name = (TextView) view.findViewById(R.id.tv_screen_name);
		tv_opening_bank = (TextView) view.findViewById(R.id.tv_opening_bank);
		tv_account_numbertwo = (TextView) view.findViewById(R.id.tv_account_numbertwo);
		tv_payment_system = (TextView) view.findViewById(R.id.tv_payment_system);
		
		

		list_product_historyequity = (ListView) view
				.findViewById(R.id.list_product_historyequity);
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
			if (api == ApiType.GET_FUNDINFOR) {
				JSONArray array = new JSONArray(json);
				JSONObject jsonobj = array.getJSONObject(0);
				tv_product_proname.setText(jsonobj.getString("FundName"));
				tv_product_procode.setText(jsonobj.getString("FundCode"));
				tv_product_unitequity.setText(jsonobj.getString("UnitEquity"));
				tv_product_date.setText("("+jsonobj.getString("DealDate")
						.split(" ")[0].replaceAll("-", "/")+")");
				tv_product_protype.setText(jsonobj.getString("Types"));
				tv_product_celue.setText(jsonobj.getString("Pan"));
				String OneYearYield = jsonobj.getString("OneYearYield");
				if (OneYearYield.endsWith("%")) {
					if (OneYearYield.startsWith("-")) {
						tv_product_jinyinian.setTextColor(Color.GREEN);
						tv_product_jinyinian.setText(OneYearYield);
						
					}else {
						tv_product_jinyinian.setText(OneYearYield);
					}
				}else {
					tv_product_jinyinian.setText(OneYearYield);
				}
				
				
				tv_product_startpoint.setText(jsonobj.getString("Money")+"万 ");
				tv_product_createdate.setText(jsonobj.getString("Dates").split(" ")[0].replaceAll("-", "/"));
				tv_product_fundmanager.setText(jsonobj.getString("Name"));
				tv_product_fundcompany.setText(jsonobj.getString("CompName"));
				tv_product_celueinfor.setText(jsonobj
						.getString("Strategy_Desc"));
				tv_fund_trustee.setText(jsonobj.getString("Trustee"));
				tv_management_cost.setText(jsonobj.getString("AdminFee"));
				tv_sales_charge.setText(jsonobj.getString("BuyFee"));
				tv_closed_period.setText(jsonobj.getString("CloseDate"));
				tv_stop_lossline.setText(jsonobj.getString("StopLossLine"));
				tv_opent_day.setText(jsonobj.getString("OpenDate"));
				tv_security_middleman.setText(jsonobj.getString("BondBroker"));
				tv_trustee_feet.setText(jsonobj.getString("TrusteeFee"));
				tv_rewarding_bymerit.setText(jsonobj.getString("RewordFee"));
				tv_precautious_line.setText(jsonobj.getString("WarningLine"));
				tv_redemption_rate.setText(jsonobj.getString("RebackFee"));
				
				tv_screen_name.setText(jsonobj.getString("RemitName"));
				tv_opening_bank.setText(jsonobj.getString("RemitBank"));
				tv_account_numbertwo.setText(jsonobj.getString("RemitAccount"));
				tv_payment_system.setText(jsonobj.getString("LargePayAcount"));

			} else if (api == ApiType.GET_HISTORYRATE) {
				JSONArray array = new JSONArray(json);
				JSONObject jsonobj = array.getJSONObject(0);
				tv_product_zd_onemonth.setText(jsonobj
						.getString("OneMonthYield"));
				tv_product_zd_threemonth.setText(jsonobj
						.getString("ThreeMonthYield"));
				tv_product_zd_sixmonth.setText(jsonobj
						.getString("SixMonthYield"));
				tv_product_zd_oneyear
						.setText(jsonobj.getString("OneYearYield"));
				tv_product_hs_onemonth.setText(jsonobj
						.getString("HSOneMonthYield"));
				tv_product_hs_threemonth.setText(jsonobj
						.getString("HSThreeMonthYield"));
				tv_product_hs_sixmonth.setText(jsonobj
						.getString("HSSixMonthYield"));
				tv_product_hs_oneyear.setText(jsonobj
						.getString("HSOneYearYield"));
				
				String OneMonthYied = jsonobj.getString("XyOneMonthYield");
				if (OneMonthYied.endsWith("%")) {
					if (OneMonthYied.startsWith("-")) {
						tv_product_xd_onemonth.setTextColor(Color.GREEN);
						tv_product_xd_onemonth.setText(OneMonthYied);
						
					}else {
						tv_product_hs_onemonth.setText(OneMonthYied);
					}
				}else {
					tv_product_hs_onemonth.setText(OneMonthYied);
				}
				String ThreeMonthYield = jsonobj.getString("XyThreeMonthYield");
				if (ThreeMonthYield.endsWith("%")) {
					if (ThreeMonthYield.startsWith("-")) {
						tv_product_xd_threemonth.setTextColor(Color.GREEN);
						tv_product_xd_threemonth.setText(ThreeMonthYield);
						
					}else {
						tv_product_xd_threemonth.setText(ThreeMonthYield);
					}
				}else {
					tv_product_xd_threemonth.setText(ThreeMonthYield);
				}
				String SixMonthYield = jsonobj.getString("XySixMonthYield");
				if (SixMonthYield.endsWith("%")) {
					if (SixMonthYield.startsWith("-")) {
						tv_product_xd_sixmonth.setTextColor(Color.GREEN);
						tv_product_xd_sixmonth.setText(SixMonthYield);
						
					}else {
						tv_product_xd_sixmonth.setText(SixMonthYield);
					}
				}else {
					tv_product_xd_sixmonth.setText(SixMonthYield);
				}
				String OneYearYield = jsonobj.getString("XyOneYearYield");
				if (OneYearYield.endsWith("%")) {
					if (OneYearYield.startsWith("-")) {
						tv_product_xd_oneyear.setTextColor(Color.GREEN);
						tv_product_xd_oneyear.setText(OneYearYield);
						
					}else {
						tv_product_xd_oneyear.setText(OneYearYield);
					}
				}else {
					tv_product_xd_oneyear.setText(OneYearYield);
				}
				
				
			/*	tv_product_xd_onemonth.setText(jsonobj
						.getString("XyOneMonthYield"));
				tv_product_xd_threemonth.setText(jsonobj
						.getString("XyThreeMonthYield"));
				tv_product_xd_sixmonth.setText(jsonobj
						.getString("XySixMonthYield"));
				tv_product_xd_oneyear.setText(jsonobj
						.getString("XyOneYearYield"));
					*/
			} else if (api == ApiType.GET_EQUITYCHART) {
				results = JSON.parseArray(json, ProductDetailResult.class);
				list_product_historyequity.setAdapter(new ProductListAdapter(
						results));
				setListViewHeightBasedOnChildren(list_product_historyequity);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		activity.disMissDialog();
	}

	class ProductListAdapter extends BaseAdapter {
		private List<ProductDetailResult> data;

		public ProductListAdapter(List<ProductDetailResult> data) {
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

			convertView = View.inflate(activity,
					R.layout.item_prohistoryequity, null);
			holder = new ViewHolder();
			holder.tv_equitydate = (TextView) convertView
					.findViewById(R.id.tv_equitydate);
			holder.tv_unitequity = (TextView) convertView
					.findViewById(R.id.tv_unitequity);
			holder.tv_totalequity = (TextView) convertView
					.findViewById(R.id.tv_totalequity);
			holder.tv_zengzhanglv = (TextView) convertView
					.findViewById(R.id.tv_zengzhanglv);

			convertView.setTag(holder);

			ProductDetailResult pd = data.get(position);
			holder.tv_equitydate.setText(pd.getDealDate().split(" ")[0]
					.replaceAll("-", "/"));
			holder.tv_unitequity.setText(pd.getUnitEquity());

			holder.tv_totalequity.setText(pd.getTotalEquity());
			String DayBenefit = pd.getDayBenefit();
			if (DayBenefit.endsWith("%")) {
				if (DayBenefit.startsWith("-")) {
					
					holder.tv_zengzhanglv.setTextColor(Color.GREEN);
					holder.tv_zengzhanglv.setText(DayBenefit);
				}else {
					holder.tv_zengzhanglv.setText(DayBenefit);
				}
			}else {
				holder.tv_zengzhanglv.setText(DayBenefit);
			}
			//holder.tv_zengzhanglv.setText(pd.getDayBenefit());

			return convertView;
		}

		class ViewHolder {
			TextView tv_equitydate, tv_unitequity, tv_totalequity,
					tv_zengzhanglv;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ProductListAdapter listAdapter = (ProductListAdapter) listView
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
