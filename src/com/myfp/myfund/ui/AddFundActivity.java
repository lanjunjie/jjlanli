 package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MySelectFund;
import com.myfp.myfund.api.beans.SearchResult;
import com.myfp.myfund.ui.view.CustomListView;
import com.myfp.myfund.ui.view.CustomListView.OnLoadMoreListener;
import com.myfp.myfund.ui.view.CustomListView.OnRefreshListener;


public class AddFundActivity extends BaseActivity implements OnDataReceivedListener2{
	
	private String userName;
	private ListView lv_searchResult;
	private CustomListView lv_addFund;
	private EditText et_search;
	private LinearLayout layout_initList,layout_resultList;
	private List<MySelectFund> funds,temp;
	private List<SearchResult> results;
	private int pageNum = 1;
	private AddAdapter adapter;
	private String sessionId;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_add_fund);
		
		showProgressDialog("正在加载");
		
		Intent intent = getIntent();
		userName = intent.getStringExtra("UserName");
		sessionId = intent.getStringExtra("sessionId");
		funds = new ArrayList<MySelectFund>();
		
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		params.put("pagenum", 1);
		execApi(ApiType.INIT_MY_SELECTED, params);
		
	}

	@Override
	protected void initViews() {
		setTitle("添加自选");

		lv_addFund = (CustomListView) findViewById(R.id.listView_addFund_initList);
		lv_searchResult = (ListView) findViewById(R.id.listView_addFund_searchList);
		
		et_search = (EditText) findViewById(R.id.et_addFund_search);
		et_search.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					//隐藏软键盘
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	                if(inputMethodManager.isActive()){  
	                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
	                }
	                
					RequestParams params = new RequestParams(AddFundActivity.this);
					params.put("InputFundPartValue", et_search.getText()
							.toString());
					params.put("UserName", userName);
					execApi(ApiType.SEARCH_FUND, params);
					showProgressDialog("正在搜索");
					
				}
				return false;
			}
		});
		
		findViewAddListener(R.id.img_addFund_search);
		findViewAddListener(R.id.img_addFund_delete);
		
		layout_initList = (LinearLayout) findViewById(R.id.layout_add_initList);
		layout_resultList = (LinearLayout) findViewById(R.id.layout_add_resultList);
		
		lv_addFund.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				pageNum = 1;
				RequestParams params = new RequestParams(AddFundActivity.this);
				params.put("UserName", userName);
				params.put("pagenum", pageNum);
				execApi(ApiType.INIT_MY_SELECTED, params);
			}
		});

		lv_addFund.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				pageNum++;
				RequestParams params = new RequestParams(AddFundActivity.this);
				params.put("UserName", userName);
				params.put("pagenum", pageNum);
				execApi(ApiType.INIT_MY_SELECTED, params);
			}
		});

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.img_addFund_search:
			RequestParams params = new RequestParams(this);
			params.put("InputFundPartValue", et_search.getText().toString());
			params.put("UserName", userName);
			execApi(ApiType.SEARCH_FUND, params);
			showProgressDialog("正在搜索");
			break;
		case R.id.img_addFund_delete:
			et_search.setText(null);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, RequestParams params, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.INIT_MY_SELECTED) {
			temp = JSON.parseArray(json, MySelectFund.class);
			if (params.get("pagenum").equals("1") ) {
				adapter = new AddAdapter(temp);
				lv_addFund.setAdapter(adapter);
				lv_addFund.onRefreshComplete();
				funds.addAll(temp);
			}else {
				funds.addAll(temp);
				adapter.addData(temp);
//				lv_addFund.setAdapter(adapter);
//				lv_addFund.setSelection((pageNum-1)*15-5);
				lv_addFund.onLoadMoreComplete();	
			}
			
			lv_addFund.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					MySelectFund fund = funds.get(position);
					Intent intent = new Intent(AddFundActivity.this,DetailActivity.class);
					intent.putExtra("sessionId", sessionId);
					intent.putExtra("fundName", fund.getFundName().trim());
					intent.putExtra("fundCode", fund.getFundCode().trim());
					startActivity(intent);
				}
			});
			disMissDialog();
			
		}else if (api == ApiType.SEARCH_FUND) {
			results = JSON.parseArray(json, SearchResult.class);
			if (results.size() == 0) {
				disMissDialog();
				showToast("没有符合条件的基金！");
				
				return;
			}
			lv_searchResult.setAdapter(new AddSearchResultAdapter(results));
			lv_searchResult.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					SearchResult res = results.get(position);
					Intent intent = new Intent(AddFundActivity.this,DetailActivity.class);
					intent.putExtra("sessionId", sessionId);
					intent.putExtra("fundName", res.getFundName().trim());
					intent.putExtra("fundCode", res.getFundCode().trim());
					startActivity(intent);
				}
			});
			
			layout_initList.setVisibility(View.GONE);
			layout_resultList.setVisibility(View.VISIBLE);
			disMissDialog();
		}
	}
	
	class AddAdapter extends BaseAdapter{

		private List<MySelectFund> list = new ArrayList<MySelectFund>();
		
		public void  addData(List<MySelectFund> addList){
			list.addAll(addList);
			notifyDataSetChanged();
		}
		
		public AddAdapter(List<MySelectFund> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final MySelectFund fund = list.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(AddFundActivity.this).inflate(R.layout.item_add_fund_list, null, false);
				holder = new ViewHolder();
				
				holder.tv_fundName = (TextView) convertView.findViewById(R.id.tv_add_fundName);
				holder.tv_fundCode = (TextView) convertView.findViewById(R.id.tv_add_fundCode);
				holder.tv_fundType = (TextView) convertView.findViewById(R.id.tv_add_fundType);
				holder.img_isSelected = (ImageView) convertView.findViewById(R.id.img_add_isSelected);
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_fundName.setText(fund.getFundName());
			holder.tv_fundCode.setText(fund.getFundCode());
			holder.tv_fundType.setText(fund.getFundType());
			if (fund.getIsFlag() != null && fund.getIsFlag().equals("1")) {
				holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
			}else{
				holder.img_isSelected.setImageResource(R.drawable.red_star_hollow_big);
			}
			holder.img_isSelected.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RequestParams params = new RequestParams(AddFundActivity.this);
					params.put("UserName", userName);
					params.put("FundCode", fund.getFundCode().trim());
					params.put("FundName", fund.getFundName().trim());
					execApi(ApiType.GET_MY_FUND_CENTER, params, new OnDataReceivedListener() {
						
						@Override
						public void onReceiveData(ApiType api, String json) {
							if(json==null){
								showToast("请求失败!");
								return;
							}
							try {
								JSONArray array = new JSONArray(json);
								if(api == ApiType.GET_MY_FUND_CENTER){
									int returnResult = array.getJSONObject(0).getInt("ReturnResult");
									switch (returnResult) {
									case 0:
										showToast("添加成功!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
										break;
									case 1:
										showToast("用户名不能为空!");
										break;
									case 2:
										showToast("取消自选!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_hollow_big);
										break;
									case 3:
										showToast("添加成功!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
										break;
									case 4:
										showToast("添加失败!");
										break;
									case 5:
										showToast("系统参数错误!");
										break;	

									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
				
			});
			
			return convertView;
		}
		class ViewHolder{
			TextView tv_fundName,tv_fundCode,tv_fundType;
			ImageView img_isSelected;
		}
		
	}
	
	class AddSearchResultAdapter extends BaseAdapter{
		
		private List<SearchResult> list;
		
		public AddSearchResultAdapter(List<SearchResult> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final SearchResult res = list.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(AddFundActivity.this).inflate(R.layout.item_search_result, null, false);
				holder = new ViewHolder();
				
				holder.tv_fundName = (TextView) convertView.findViewById(R.id.tv_searchResult_fundName);
				holder.tv_fundCode = (TextView) convertView.findViewById(R.id.tv_searchResult_fundCode);
				holder.tv_fundType = (TextView) convertView.findViewById(R.id.tv_searchResult_fundType);
				holder.img_isSelected = (ImageView) convertView.findViewById(R.id.img_search_isSelected);
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_fundName.setText(res.getFundName());
			holder.tv_fundCode.setText(res.getFundCode());
			holder.tv_fundType.setText(res.getFundType());
			if (res.getIsFlag() != null && res.getIsFlag().equals("1")) {
				holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
			}
			holder.img_isSelected.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RequestParams params = new RequestParams(AddFundActivity.this);
					params.put("UserName", userName);
					params.put("FundCode", res.getFundCode().trim());
					params.put("FundName", res.getFundName().trim());
					execApi(ApiType.GET_MY_FUND_CENTER, params, new OnDataReceivedListener() {
						
						@Override
						public void onReceiveData(ApiType api, String json) {
							if(json==null){
								showToast("请求失败!");
								return;
							}
							try {
								JSONArray array = new JSONArray(json);
								if(api == ApiType.GET_MY_FUND_CENTER){
									int returnResult = array.getJSONObject(0).getInt("ReturnResult");
									switch (returnResult) {
									case 0:
										showToast("添加成功!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
										break;
									case 1:
										showToast("用户名不能为空!");
										break;
									case 2:
										showToast("取消自选!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_hollow_big);
										break;
									case 3:
										showToast("添加成功!");
										holder.img_isSelected.setImageResource(R.drawable.red_star_solid_big);
										break;
									case 4:
										showToast("添加失败!");
										break;
									case 5:
										showToast("系统参数错误!");
										break;	

									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
				
			});
			
			
			return convertView;
		}
		class ViewHolder{
			TextView tv_fundName,tv_fundCode,tv_fundType;
			ImageView img_isSelected;
		}
	}
}
