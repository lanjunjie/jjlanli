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
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.adapter.BannerAdapter;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.ClubFund;
import com.myfp.myfund.ui.view.CirclePageIndicator;

/**
 * 俱乐部产品单页
 * @author pengchong.jia
 *
 */
public class ClubFundsDetailActivity extends BaseActivity {
	
	private ViewPager vp_banner;
	private CirclePageIndicator indicator;
	private ScheduledExecutorService scheduledExecutorService;
	int currentItem;
	private List<String> imgs;
	private Handler handler = new Handler();
	private ClubFund cFund;
	private TextView tv_isSaled,tv_expectedEarnings,tv_proType,tv_timeLimit,tv_minInvestment,tv_minInvestPlus,tv_financingTime,tv_sellAccount,tv_fundInfo,tv_moreInfo;
	private List<String> bannerUrl = new ArrayList<String>();
	private List<String> bannerDetail = new ArrayList<String>();
	private List<String> shareurl=new ArrayList<String>();
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_clubfunds_detail);
		
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.TYPE, 2);
		execApi(ApiType.GET_BANNERS, params);
		
		Intent intent = getIntent();
		cFund = (ClubFund) intent.getSerializableExtra("fund");
	}

	@Override
	protected void initViews() {
		setTitle("俱乐部产品单页");
		
		vp_banner = (ViewPager) findViewById(R.id.vp_clubDetail_banner);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator_clubDetail);
		
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
		
		tv_isSaled = (TextView) findViewById(R.id.tv_clubFund_saled);
		tv_expectedEarnings = (TextView) findViewById(R.id.tv_clubFund_expectedEarnings);
		tv_proType = (TextView) findViewById(R.id.tv_clubFund_proType);
		tv_timeLimit = (TextView) findViewById(R.id.tv_clubFund_timeLimit);
		tv_minInvestment = (TextView) findViewById(R.id.tv_clubFund_minInvestment);
		tv_minInvestPlus = (TextView) findViewById(R.id.tv_clubFund_minInvestPlus);
		tv_financingTime = (TextView) findViewById(R.id.tv_clubFund_financingTime);
		tv_sellAccount = (TextView) findViewById(R.id.tv_clubFund_sellAccount);
		tv_fundInfo = (TextView) findViewById(R.id.tv_clubFund_fundInfo);
		findViewAddListener(R.id.tv_clubFund_moreInfo);
		
		tv_isSaled.setText(cFund.getIsOnSale());
		tv_expectedEarnings.setText(cFund.getExpectedEarnings());
		tv_proType.setText(cFund.getFundType());
		tv_timeLimit.setText(cFund.getTimeLimit());
		tv_minInvestment.setText(cFund.getMinInvestment());
		tv_minInvestPlus.setText(cFund.getMinInvestPlus());
		tv_financingTime.setText(cFund.getFinancingTime());
		tv_sellAccount.setText(cFund.getSellAccount());
		tv_fundInfo.setText(cFund.getProDetail());
		findViewAddListener(R.id.bt_club_tel);
		
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_club_tel:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);   
			intent.setData(Uri.parse("tel:400-888-6661"));
			startActivity(intent);
			break;
		case R.id.tv_clubFund_moreInfo:
			Intent web = new Intent(this,ShowWebActivity.class);
			web.putExtra("Url", cFund.getMoreURL());
			startActivity(web);
		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("获取失败!");
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
}
