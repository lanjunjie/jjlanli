package com.myfp.myfund.ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.CurrentProductResult;
import com.myfp.myfund.utils.ImageCacheManager;

public class ManagerFragment extends BaseFragment implements
		OnDataReceivedListener2 {
	int index;
	private String procode;
	private ProductDetailActivity activity;
	List<CurrentProductResult> results;
	TextView tv_manager_managername, tv_manager_managername2, tv_manager_pro,
			tv_manager_company, tv_manager_xueli, tv_manager_yuanxiao,
			tv_manager_jingyan, tv_manager_gaikuang;

	ListView list_manager_funds;
	private NetworkImageView imageView_manager_photo;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String userName, agentScode, managercode;
	private String baseUrl = "http://www.myfund.com";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		managercode = bundle.getString("PersonID");
		activity = (ProductDetailActivity) getActivity();

		RequestParams params = new RequestParams(activity);
		params.put("PersonID", managercode);
		activity.execApi(ApiType.GET_FUNDMANAGER, params, this);
		activity.execApi(ApiType.GET_CURRENTPRODUCT, params, this);

		activity.showProgressDialog("正在加载");

		requestQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());

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
		View view = inflater.inflate(R.layout.fragment_manager, null, false);

		// imageView_manager_photo = (NetworkImageView) view
		// .findViewById(R.id.imageView_manager_photo);
		tv_manager_managername = (TextView) view
				.findViewById(R.id.tv_manager_managername);
		tv_manager_managername2 = (TextView) view
				.findViewById(R.id.tv_manager_managername2);
		tv_manager_pro = (TextView) view.findViewById(R.id.tv_manager_pro);
		tv_manager_company = (TextView) view
				.findViewById(R.id.tv_manager_company);
		tv_manager_xueli = (TextView) view.findViewById(R.id.tv_manager_xueli);
		tv_manager_yuanxiao = (TextView) view
				.findViewById(R.id.tv_manager_yuanxiao);
		tv_manager_jingyan = (TextView) view
				.findViewById(R.id.tv_manager_jingyan);
		tv_manager_gaikuang = (TextView) view
				.findViewById(R.id.tv_manager_gaikuang);

		list_manager_funds = (ListView) view
				.findViewById(R.id.list_manager_funds);
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

			if (api == ApiType.GET_FUNDMANAGER) {
				JSONArray array = new JSONArray(json);
				JSONObject jsonobj = array.getJSONObject(0);
				System.out.println("4444444444444444444444444"
						+ jsonobj.getString("PicName"));
				// String url;
				// // url =
				// java.net.URLEncoder.encode(jsonobj.getString("PicName").toString());
				// // url= new String(
				// // jsonobj.getString("PicName").toString().getBytes( "utf-8"
				// // ), "gbk");
				// try {
				// url= new String(
				// jsonobj.getString("PicName").toString().getBytes( "utf-8"
				// ), "GB2312");
				// // url = URLEncoder.encode(
				// // jsonobj.getString("PicName").trim(), "GB2312");
				// imageView_manager_photo.setImageUrl(url, imageLoader);
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				tv_manager_managername.setText(jsonobj.getString("ManageName"));
				tv_manager_managername2.setText(jsonobj.getString("ManageName"));
				tv_manager_pro.setText(jsonobj.getString("FundProduct") + "只");
				tv_manager_company.setText(jsonobj.getString("CompName"));
				tv_manager_xueli.setText(jsonobj.getString("Degree"));
				tv_manager_yuanxiao.setText(jsonobj.getString("Colleges"));
				tv_manager_jingyan.setText(jsonobj.getString("Experience")
						+ "年");
				tv_manager_gaikuang.setText(jsonobj.getString("Summary"));

			} else if (api == ApiType.GET_CURRENTPRODUCT) {
				results = JSON.parseArray(json, CurrentProductResult.class);
				list_manager_funds.setAdapter(new ManagerListAdapter(results));
				// setListViewHeightBasedOnChildren(list_manager_funds);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		activity.disMissDialog();
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	class ManagerListAdapter extends BaseAdapter {
		private List<CurrentProductResult> data;

		public ManagerListAdapter(List<CurrentProductResult> data) {
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
			// holder.bt_manager_order = (Button) convertView
			// .findViewById(R.id.bt_manager_order);

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

			// holder.bt_manager_order.setOnClickListener(new OnClickListener()
			// {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// if (userName == null) {
			// activity.showToast("未登录，请先登录！");
			// Intent intent = new Intent(activity,
			// LoginActivity.class);
			// // Bundle bundle = new Bundle();
			// // bundle.putSerializable("ProductList", pl);
			// // intent.putExtras(bundle);
			// activity.startActivity(intent);
			// return;
			// } else {
			// activity.showProgressDialog("正在加载");
			// RequestParams params1 = new RequestParams(activity);
			// params1.put("proName", cp.getFundName());
			// activity.execApi(
			// ApiType.GET_PRODUCTDETAIL,
			// params1,
			// new OnDataReceivedListener() {
			//
			// @Override
			// public void onReceiveData(
			// ApiType api,
			// String json) {
			// if (json == null) {
			// activity.showToast("请求失败!");
			// return;
			// }
			//
			// List<ProductList> result;
			// result = JSON.parseArray(json, ProductList.class);
			// if(result.size() == 0){
			// activity.showToast("请求失败");
			// activity.disMissDialog();
			// return;
			// }
			//
			// Intent intent = new Intent(activity,
			// OrderActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("ProductList", result.get(0));
			// bundle.putString("UserName", userName);
			// bundle.putString("AgentScode", agentScode);
			// intent.putExtras(bundle);
			// // intent.putExtra("UserName", userName);
			// startActivity(intent);
			// activity.disMissDialog();
			// }
			// });
			// }
			//
			// }
			// });

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
		ManagerListAdapter listAdapter = (ManagerListAdapter) listView
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
