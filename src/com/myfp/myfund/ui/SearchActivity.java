package com.myfp.myfund.ui;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.SearchResult;

/**
 * 搜索页面
 * 
 * @author pengchong.jia
 * 
 */
public class SearchActivity extends BaseActivity {

	private EditText et_searchContent;
	private ListView lv_searchResult;
	private List<SearchResult> results;
	private LinearLayout layout_title, layout_whole;
	private String userName;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_search);
		userName = App.getContext().getUserName();
		
	}

	@Override
	protected void initViews() {
		findViewById(R.id.ll_main_top_layout).setVisibility(View.GONE);
		findViewById(R.id.layout_title_search).setVisibility(View.VISIBLE);

		findViewById(R.id.tv_searchResult_title_selected).setVisibility(View.GONE);
		
		findViewAddListener(R.id.tv_text_of_right);
		findViewAddListener(R.id.img_delete);
		findViewAddListener(R.id.img_search);

		et_searchContent = (EditText) findViewById(R.id.et_main_search);
		lv_searchResult = (ListView) findViewById(R.id.listView_search_result);

		layout_title = (LinearLayout) findViewById(R.id.layout_searchResult_title);
		layout_whole = (LinearLayout) findViewById(R.id.layout_searchResult);
		
		et_searchContent.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					//隐藏软键盘
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	                if(inputMethodManager.isActive()){  
	                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
	                }
					RequestParams params = new RequestParams(SearchActivity.this);
					params.put("InputFundPartValue", et_searchContent.getText()
							.toString());
					params.put("UserName", userName);
					execApi(ApiType.SEARCH_FUND, params);
					showProgressDialog("正在搜索");
				}
				return false;
			}
		});
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.img_search:
			RequestParams params = new RequestParams(this);
			params.put("InputFundPartValue", et_searchContent.getText()
					.toString());
			params.put("UserName", userName);
			execApi(ApiType.SEARCH_FUND1, params);
			showProgressDialog("正在搜索");
			break;
		case R.id.img_delete:
			et_searchContent.setText(null);
			break;
		case R.id.tv_text_of_right:
			finish();
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
		setTheme(android.R.style.Theme_Light_NoTitleBar);
		layout_whole.setBackgroundColor(Color.parseColor("#ffffffff"));
		layout_title.setVisibility(View.VISIBLE);
		results = JSON.parseArray(json, SearchResult.class);
		if (results.size() == 0) {
			showToast("没有符合条件的基金！");
			disMissDialog();
			return;
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>");
		lv_searchResult.setAdapter(new SearchResultAdapter(results));
		lv_searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchResult res = results.get(position);
				Intent intent = new Intent(SearchActivity.this,DetailActivity.class);
				intent.putExtra("fundName", res.getFundName().trim());
				intent.putExtra("fundCode", res.getFundCode().trim());
				startActivity(intent);
			}
		});
		disMissDialog();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String uName = App.getContext().getUserName();
		if(!TextUtils.isEmpty(uName)){
			userName = uName;
			System.out.println(userName+">>>>>>>>>>>>>");
			RequestParams params = new RequestParams(this);
			params.put("InputFundPartValue", et_searchContent.getText()
					.toString());
			params.put("UserName", userName);
			execApi(ApiType.SEARCH_FUND, params);
		}
	}

	class SearchResultAdapter extends BaseAdapter {

		private List<SearchResult> list;

		public SearchResultAdapter(List<SearchResult> list) {
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
				convertView = LayoutInflater.from(SearchActivity.this).inflate(
						R.layout.item_search_result, null, false);
				holder = new ViewHolder();

				holder.tv_fundName = (TextView) convertView
						.findViewById(R.id.tv_searchResult_fundName);
				holder.tv_fundCode = (TextView) convertView
						.findViewById(R.id.tv_searchResult_fundCode);
				holder.tv_fundType = (TextView) convertView
						.findViewById(R.id.tv_searchResult_fundType);
				holder.img_isSelected = (ImageView) convertView
						.findViewById(R.id.img_search_isSelected);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tv_fundName.setText(res.getFundName());
			holder.tv_fundCode.setText(res.getFundCode());
			holder.tv_fundType.setText(res.getFundType());
			if (res.getIsFlag() != null && res.getIsFlag().equals("1")) {
				holder.img_isSelected
						.setImageResource(R.drawable.red_star_solid_big);
			}
			holder.img_isSelected.setVisibility(View.GONE);
/*			holder.img_isSelected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (TextUtils.isEmpty(userName)) {
						Toast.makeText(SearchActivity.this, "请先登录", Toast.LENGTH_SHORT)
								.show();
						startActivity(new Intent(SearchActivity.this,
								LoginActivity.class));
					} else {
						RequestParams params = new RequestParams(
								SearchActivity.this);
						params.put("UserName", userName);
						params.put("FundCode", res.getFundCode().trim());
						params.put("FundName", res.getFundName().trim());
						execApi(ApiType.GET_MY_FUND_CENTER, params,
								new OnDataReceivedListener() {

									@Override
									public void onReceiveData(ApiType api,
											String json) {
										if (json == null) {
											showToast("请求失败!");
											return;
										}
										try {
											JSONArray array = new JSONArray(
													json);
											if (api == ApiType.GET_MY_FUND_CENTER) {
												int returnResult = array
														.getJSONObject(0)
														.getInt("ReturnResult");
												switch (returnResult) {
												case 0:
													showToast("添加成功!");
													holder.img_isSelected
															.setImageResource(R.drawable.red_star_solid_big);
													break;
												case 1:
													showToast("用户名不能为空!");
													break;
												case 2:
													showToast("取消自选!");
													holder.img_isSelected
															.setImageResource(R.drawable.red_star_hollow_big);
													break;
												case 3:
													showToast("添加成功!");
													holder.img_isSelected
															.setImageResource(R.drawable.red_star_solid_big);
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
				}
			});*/

			return convertView;
		}

		class ViewHolder {
			TextView tv_fundName, tv_fundCode, tv_fundType;
			ImageView img_isSelected;
		}
	}
}
