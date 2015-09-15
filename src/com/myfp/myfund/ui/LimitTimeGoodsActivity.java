package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.MonthQuality;
import com.myfp.myfund.ui.view.FlexibilityListView;
import com.myfp.myfund.utils.ImageCacheManager;

/**
 * 限时优品界面
 * 
 * @author Bruce.Wang
 * 
 */
public class LimitTimeGoodsActivity extends BaseActivity {

	private List<MonthQuality> current, history;
	private FlexibilityListView listView;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String baseUrl = "http://www.myfund.com";
	private String userName;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_limit_time_goods);
		showProgressDialog("正在加载……");
		execApi(ApiType.GET_MONTH_QUALITY1, null);
		execApi(ApiType.GET_MONTH_QUALITY2, null);
		// 获取当前用户名
		userName = App.getContext().getUserName();

		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {
		setTitle("限时抢购");
		listView = (FlexibilityListView) findViewById(R.id.lv);
	}

	@Override
	protected void onResume() {
		userName = App.getContext().getUserName();
		super.onResume();
	}

	@Override
	protected void onViewClick(View v) {

	}

	class GoodsAdapter extends BaseAdapter {

		private List<MonthQuality> list1, list2;

		public GoodsAdapter(List<MonthQuality> list1, List<MonthQuality> list2) {
			// 限时优品列表
			this.list1 = list1;
			// 历史推荐列表
			this.list2 = list2;
		}

		@Override
		public int getCount() {
			return list1.size() + 1 + list2.size();
		}

		@Override
		public Object getItem(int position) {

			if (position < list1.size()) {
				return list1.get(position);
			} else if (position > list1.size()) {
				return list2.get(position);
			}
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final MonthQuality mQuality;
			if (position == list1.size()) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_limit_goods_title, null);
			} else {
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_limit_time_goods, null);

					NetworkImageView img_logo = (NetworkImageView) convertView
							.findViewById(R.id.img_MonthQuality_logo);
					TextView tv_fundName = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_fundName);
					TextView tv_slogan = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_slogan);
					TextView tv_proType = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_proType);
					TextView tv_expectedEarnings = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_expectedEarnings);
					TextView tv_minInvestment = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_minInvestment);
					TextView tv_timeLimit = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_timeLimit);
					TextView tv_month = (TextView) convertView
							.findViewById(R.id.tv_MonthQuality_month);
					TextView bt_order = (Button) convertView
							.findViewById(R.id.tv_MonthQuality_order);

				// 判断是限时优品还是历史推荐，position<list1.size()的话，显示的是限时优品的列表，显示在list1里。
				// 否则的话，是历史推荐列表，显示在list2中，list2就是position-list1.size()-1 。
				if (position < list1.size()) {
					mQuality = list1.get(position);
				} else {
					mQuality = list2.get(position - list1.size() - 1);
				}

				tv_fundName.setText(mQuality.getFundName());
				tv_slogan.setText(mQuality.getSlogan());
				tv_proType.setText(mQuality.getProType());
				if (mQuality.getExpectedEarnings().contains("%")) {
					tv_expectedEarnings.setText(mQuality
							.getExpectedEarnings());
				} else {
					tv_expectedEarnings.setText(mQuality
							.getExpectedEarnings() + "%");
				}
				tv_minInvestment.setText(mQuality.getMinInvestment());
				tv_timeLimit.setText(mQuality.getTimeLimit());
				tv_month.setText(mQuality.getMonth());
				img_logo.setErrorImageResId(R.drawable.ic_launcher);
				img_logo.setImageUrl(baseUrl
						+ mQuality.getLogo().substring(1), imageLoader);
				if (!"在售".equals(mQuality.getIsOnSale())) {
					bt_order.setEnabled(false);
					bt_order.setBackgroundColor(Color.GRAY);
				}
				bt_order.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_MonthQuality_order:
							// 先判断是否已经登录
							if (userName == null) {
								showToast("请先登录!");
								startActivity(MyMeansActivity.class);
								return;
							}
							Intent intent = new Intent(
									LimitTimeGoodsActivity.this,
									OrderOnlineActivity.class);
							intent.putExtra("MonthQuality", mQuality);
							startActivity(intent);
							break;

						default:
							break;
						}
					}
				});
			}

			return convertView;
		}

		class ViewHolder {
			NetworkImageView img_logo;
			TextView tv_fundName, tv_slogan, tv_proType, tv_expectedEarnings,
					tv_minInvestment, tv_timeLimit, tv_month;
			Button bt_order;
		}

	}

	private boolean flag1 = false;
	private boolean flag2 = false;

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_MONTH_QUALITY1) {
				flag1 = true;
				current = JSON.parseArray(json, MonthQuality.class);
			} else if (api == ApiType.GET_MONTH_QUALITY2) {
				flag2 = true;
				history = JSON.parseArray(json, MonthQuality.class);
			}
			if (flag1 && flag2) {
				listView.setAdapter(new GoodsAdapter(current, history));
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MonthQuality mq;
						if (position < current.size()) {
							mq = current.get(position);
						} else if (position > current.size()) {
							mq = history.get(position - current.size() - 1);
						} else {
							view.setClickable(false);
							return;
						}
						Intent intent = new Intent(LimitTimeGoodsActivity.this,
								PrivateFundDetailActivity.class);
						intent.putExtra("MonthQuality", mq);
						startActivity(intent);
					}
				});
				disMissDialog();
			}
		}
	}

}
