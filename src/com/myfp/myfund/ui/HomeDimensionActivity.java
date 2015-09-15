package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.R;
import com.myfp.myfund.adapter.BannerAdapter;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.BaseSlidingFragmentActivity;
import com.myfp.myfund.ui.view.CirclePageIndicator;
import com.myfp.myfund.ui.view.SlidingMenu;
import com.myfp.myfund.utils.VersionsUpdate;

public class HomeDimensionActivity extends BaseSlidingFragmentActivity{
	
	private SlidingMenu sm;
	private ViewPager vp_banner;
	private CirclePageIndicator indicator;
	private ScheduledExecutorService scheduledExecutorService;
	int currentItem;
	private List<String> imgs;
	private Handler handler = new Handler();
	private long lastBack;
	private int earning;    
	private List<String> bannerUrl = new ArrayList<String>();
	private List<String> shareurl = new ArrayList<String>();
	private List<String> bannerDetail = new ArrayList<String>();
	private SharedPreferences sPreferences;
	private String encodePassWord,idCard;
	ByteArrayInputStream tInputStringStream = null;
	private ScrollView scrol;
	private TextView textview_view;
	private String userName,password,sessionid;
	private String flag="true";
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_envisage);
		initData();
		new VersionsUpdate(true, this).getVersion();
		sPreferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		Editor editor = sPreferences.edit();
		editor.putBoolean("isFirst", false);
		editor.putBoolean("mainIsStart", true);
		editor.commit();
		idCard=sPreferences.getString("IDCard", null);

		
		 userName = sPreferences.getString("UserName", null);
		sessionid=App.getContext().getSessionid();
		System.out.println("11111111111111"+sessionid);
		App.getContext().setMainIsStart(true);
		textview_view = (TextView) findViewById(R.id.textview_view);
		textview_view.setTextColor(this.getResources().getColor(R.color.red_bt_normal));
		LinearLayout ll_top_layout_left_view = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		ll_top_layout_left_view.setOnClickListener(this);
		
	}

	@Override
	protected void initViews() {
		initSlidingMenu();
		vp_banner = (ViewPager) findViewAddListener(R.id.vp_banner);
		indicator = (CirclePageIndicator) findViewAddListener(R.id.indicator);
		SharedPreferences preferences = getSharedPreferences("Setting", MODE_PRIVATE);
		
		sm.addIgnoredView(vp_banner);
		
		findViewAddListener(R.id.layout_cemetery);
		findViewAddListener(R.id.layout_feature);
		findViewAddListener(R.id.layout_private);
		findViewAddListener(R.id.layout_subscribe);
		findViewAddListener(R.id.layout_fintheircing);
		findViewAddListener(R.id.layout_privateone);
		findViewAddListener(R.id.text_phone);
	
		vp_banner.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int item) {
				currentItem = item;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0){

			}
		});
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.pushnews);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_left_view:
			// 切换元素
			startActivity(SettingActivity.class);
		//	sm.toggle();
			break;   
			//公募基金
		case R.id.layout_cemetery:
			Intent intent =new Intent(this, CemeteryFragmentActivity.class);
	  		intent.putExtra("sessionId",sessionid);  
			startActivity(intent);
			break;
			//自选基金
		case R.id.layout_feature:
			//startActivity(CharacteristicsActivity.class);
			System.out.println("sssssssssssssssssss"+sessionid);
			sessionid=App.getContext().getSessionid();
			System.out.println("11111111111111"+sessionid);
			if(sessionid==null){
				Intent intent7=new Intent(HomeDimensionActivity.this, MyMeansActivity.class);
				intent7.putExtra("Flag", flag);
				
				startActivity(intent7);
			}else{
				Intent intent5=new Intent(HomeDimensionActivity.this, FundSelectActivity.class);
//				intent5.putExtra("UserName", userName);
				intent5.putExtra("sessionId", sessionid);
				startActivity(intent5);
			}
			
			break;    
			//私募基金
		case R.id.layout_private:
			startActivity(PlacementActivty.class);
			break;
			//免申购费
		case R.id.layout_subscribe:
			Intent intent1 = new Intent(HomeDimensionActivity.this,DianCaiTongActivity.class);
			intent1.putExtra("ImageID", "08");
			startActivity(intent1);	
			break;
			//基金配资
		case R.id.layout_fintheircing:
			Intent intent2=new Intent(HomeDimensionActivity.this, FeatureWebActivity.class);
			intent2.putExtra("detailurl", "http://m.myfund.com/hdl/index.aspx");
			startActivity(intent2);
//			startActivity(CharacteristicsActivity.class);
			break;
		case R.id.layout_privateone:
			Intent intent4=new Intent(HomeDimensionActivity.this,SeniorCustomActivity.class);
			intent4.putExtra("ImageID", "06");
			startActivity(intent4);
			break;
			//电话
		case R.id.text_phone:
			Intent intent3=new Intent();
			intent3.setAction(Intent.ACTION_DIAL);
			intent3.setData(Uri.parse("tel:400-888-6661"));
			startActivity(intent3);
			break;
			//搜索
		case R.id.ll_top_layout_right_view:
			startActivity(PushListActivity.class);
			break;
		}
	}
	private void initData() {

		// 从服务器获取banner图片列表
		RequestParams params = new RequestParams(this);
		params.put(RequestParams.TYPE, 1);
		execApi(ApiType.GET_BANNERS, params);
//		execApi(ApiType.GET_MYFUNDBANNER, params);		
		execApi(ApiType.GET_MAX_EXPECTED, null);
		execApi(ApiType.GET_INDEX, null);
		// ----------end------------

	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("json数据==========++++++++++++++++++++=>"+json);
		// 在这里会收到数据,一定要判断是否为空
		if (json == null) {
			disMissDialog();
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

					String banner = obj.getString("BannerURL");
					bannerUrl.add(banner);
					String share=obj.getString("ShareURL");
					shareurl.add(share);
					String bDetail = obj.getString("BannerDetail");
					bannerDetail.add(bDetail);

					url = url.substring(1);
					url = "http://www.myfund.com" + url;
					imgs.add(url);
				}

				BannerAdapter bannerAdapter = new BannerAdapter(
						getSupportFragmentManager(), imgs, this, bannerUrl,
						bannerDetail,shareurl);
				vp_banner.setAdapter(bannerAdapter);

				indicator.setViewPager(vp_banner);
			
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//加载侧滑菜单
		private void initSlidingMenu() {
			sm = getSlidingMenu();
			sm.setMode(SlidingMenu.LEFT);// 璁剧疆Slidingmenu鍑虹幇鐨勬柟鍚�
			sm.setShadowDrawable(R.drawable.shadow);// 璁剧疆slidingmenu鐨勯槾褰辫祫婧�
			sm.setShadowWidthRes(R.dimen.shadow_width);// 璁剧疆slidingmenu鐨勯槾褰辩殑瀹藉害
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 璁剧疆slidingmenu鍦ㄤ富椤甸潰鍑虹幇鏃剁殑瀹藉害
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 鍦ㄥ睆骞曞叏灞忛兘鍙互婊戝嚭slidingmenu
			// sm.setMenu(R.layout.slidingmenu);//璁剧疆slidingmenu浣跨敤鐨勫竷灞�枃浠�
			setBehindContentView(R.layout.slidingmenu);
			// sm.attachToActivity(this,
			// SlidingMenu.SLIDING_CONTENT);//灏唖lidingmenu娣诲姞鍒板綋鍓峚ctivity涓�
			// sm.addIgnoredView(mViewPager);
			FragmentTransaction tf = getSupportFragmentManager().beginTransaction();
			tf.replace(R.id.menu, AboutZhanHengSlidingMenu.getInstance());
			tf.commit();
		}

		/**
		 * 自动滚动
		 */
		private void autoChange() {
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			// 每隔2秒钟切换一张图片
			scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 5,
					5, TimeUnit.SECONDS);
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

		@Override
		protected void onDestroy() {

			SharedPreferences sPreferences = getSharedPreferences("Setting",
					MODE_PRIVATE);
			Editor editor = sPreferences.edit();
			editor.putBoolean("mainIsStart", false);
			editor.commit();
			super.onDestroy();
		}

		@Override
		public void onBackPressed() {
			long curTime = System.currentTimeMillis();
			if (curTime - lastBack > 2000) {
				lastBack = curTime;
				showToast("再按一次退出展恒基金网");
			} else {
				String listStr = JSON.toJSONString(App.getContext().userList);
				Editor editor = sPreferences.edit();
				editor.putString("UserList", listStr);
				editor.commit();
				finish();
				
			}
		}
	
		

}
