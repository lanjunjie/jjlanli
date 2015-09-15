package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.adapter.BannerAdapter;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.ClubFund;
import com.myfp.myfund.ui.view.CirclePageIndicator;
import com.myfp.myfund.ui.view.FlexibilityListView;

/**
 * 财立方俱乐部首页（产品展示页）
 * @author Max.Zhao
 *
 */
public class ClubHomeActivity extends BaseActivity {
	
	private FlexibilityListView lv_fundsList;
	private ViewPager vp_banner;
	private CirclePageIndicator indicator;
	private ScheduledExecutorService scheduledExecutorService;
	int currentItem;
	private List<String> imgs;
	private Handler handler = new Handler();
	private List<ClubFund> clubFunds;
	private List<String> bannerUrl = new ArrayList<String>();
	private List<String> bannerDetail = new ArrayList<String>();
	private List<String> shareurl=new ArrayList<String>();
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_clubhome);
		
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.TYPE, 2);
		execApi(ApiType.GET_BANNERS, params);
		execApi(ApiType.GET_CLUB_LIST, params);
		showProgressDialog("正在加载");
	}

	@Override
	protected void initViews() {
		setTitle("财立方俱乐部");
		lv_fundsList = (FlexibilityListView) findViewById(R.id.lv_clubFund_list);
//		View headView = LayoutInflater.from(this).inflate(R.layout.headview_clubhome, null, false);
		View headView = View.inflate(getApplicationContext(), R.layout.headview_clubhome, null);
		AbsListView.LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,200);
		headView.setLayoutParams(param);
		lv_fundsList.addHeaderView(headView);
		
		vp_banner = (ViewPager) headView.findViewById(R.id.vp_clubHome_banner);
		indicator = (CirclePageIndicator) headView.findViewById(R.id.indicator_clubHome);
		
		vp_banner.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int item) {
				currentItem = item;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	protected void onViewClick(View v) {

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("获取失败!");
			disMissDialog();
			return;
		}
		try {
			if (api == ApiType.GET_BANNERS) {
				JSONArray array = new JSONArray(json);
				// 数据是获取的banner数据
				imgs = new ArrayList<String>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String url = obj.getString("BannerPic");
					url = url.substring(1);
					url = "http://www.myfund.com" + url;
					System.out.println(url+"---------------------------------------");
					imgs.add(url);
					
					String banner = obj.getString("BannerURL");
					bannerUrl.add(banner);
					
					bannerDetail.add(obj.getString("BannerDetail"));
				}

				BannerAdapter bannerAdapter = new BannerAdapter(
						getSupportFragmentManager(), imgs, this,bannerUrl,bannerDetail,shareurl);
				vp_banner.setAdapter(bannerAdapter);
				indicator.setViewPager(vp_banner);
			} else if (api == ApiType.GET_CLUB_LIST) {
				clubFunds = JSON.parseArray(json, ClubFund.class);
				lv_fundsList.setAdapter(new ClubAdapter(clubFunds));
				lv_fundsList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(ClubHomeActivity.this,ClubFundsDetailActivity.class);
						intent.putExtra("fund", clubFunds.get(position-1));
						startActivity(intent);
					}
				});
				disMissDialog();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 自动滚动
	 */
	private void autoChange() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 每隔2秒钟切换一张图片
		scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2,
				2, TimeUnit.SECONDS);
		new Thread() {
			public void run() {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 自动切换页面任务
	 * 
	 * @author Bruce.Wang
	 * 
	 */
	class ViewPagerTask implements Runnable {

		@Override
		public void run() {
			if (imgs == null && imgs.size() <= 0) {
				return;
			}
			currentItem = (currentItem + 1) % imgs.size();
			handler.post(new Runnable() {

				@Override
				public void run() {
					vp_banner.setCurrentItem(currentItem);
					indicator.setCurrentItem(currentItem);
				}
			});
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		autoChange();
	}

	@Override
	protected void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}
	
	class ClubAdapter extends BaseAdapter{

		private List<ClubFund> list;
		
		public ClubAdapter(List<ClubFund> list){
			this.list =list;
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
			convertView = LayoutInflater.from(ClubHomeActivity.this).inflate(R.layout.item_club_home, null, false);
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_clubHome_fundname);
			TextView tv_min = (TextView) convertView.findViewById(R.id.tv_clubHome_minInvestment);
			TextView tv_time = (TextView) convertView.findViewById(R.id.tv_clubHome_financingTime);
			TextView tv_commission = (TextView) convertView.findViewById(R.id.tv_clubHome_Commission);
			TextView tv_expectedEarnings = (TextView) convertView.findViewById(R.id.tv_clubHome_expectedEarnings);
			
			tv_name.setText(list.get(position).getFundName());
			tv_min.setText(list.get(position).getMinInvestment());
			tv_time.setText(list.get(position).getFinancingTime());
			
			String commission = list.get(position).getCommission();
			tv_commission.setText(commission.substring(0, commission.length()-1));
			
			String expected = list.get(position).getExpectedEarnings();
			tv_expectedEarnings.setText(expected.substring(0, expected.length()-1));
			
			return convertView;
		}
		
	}
}
