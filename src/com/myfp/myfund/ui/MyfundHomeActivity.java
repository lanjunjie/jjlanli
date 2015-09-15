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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.R;
import com.myfp.myfund.adapter.BannerAdapter;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MyIndexList;
import com.myfp.myfund.ui.view.BaseSlidingFragmentActivity;
import com.myfp.myfund.ui.view.CirclePageIndicator;
import com.myfp.myfund.ui.view.SlidingMenu;
import com.myfp.myfund.utils.VersionsUpdate;

public class MyfundHomeActivity extends BaseSlidingFragmentActivity implements
OnDataReceivedListener2{
	private ViewPager vp_ban;
	private CirclePageIndicator indicat;
	int currItem;
	private List<String> arrayimgs;
	private SlidingMenu sm;
	private SharedPreferences sPreferences;
	private List<String> bannerUrl = new ArrayList<String>();
	private List<String> shareurl = new ArrayList<String>();
	private List<String> bannerDetail = new ArrayList<String>();
	private ScheduledExecutorService scheduledExecutorService;
	private Handler handler = new Handler();
	private long lastBack;
	private LinearLayout chaoshi,zixuan,tesegushou,simu;
	private String sessionid;
	private int type=1;
	private String flag="true";
	private MyfundHomeActivity activity;
	private List<MyIndexList> list;
	private String fundcode1,fundcode2,fundcode3,fundcode4;
	private ListView cakeshome_list,liwuhome_list;
	private List<MyIndexList> listret;
	private String userName;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_home);
		initData();
		activity=this;
		new VersionsUpdate(true, this).getVersion();
		sPreferences = getSharedPreferences("Setting",
				MODE_PRIVATE);
		Editor editor = sPreferences.edit();
		editor.putBoolean("isFirst", false);
		editor.putBoolean("mainIsStart", true);
		editor.commit();
		App.getContext().getUserName();
		System.out.println("12345678910-------->"+App.getContext().getUserName());
	
	}

	@Override
	protected void initViews() {
		initSlidingMenu();
		vp_ban = (ViewPager) findViewAddListener(R.id.vp_ban);
		indicat = (CirclePageIndicator) findViewAddListener(R.id.indicat);
		sm.addIgnoredView(vp_ban);
		chaoshi=(LinearLayout) findViewAddListener(R.id.chaoshi);
		zixuan=(LinearLayout) findViewAddListener(R.id.zixuan);
		tesegushou=(LinearLayout) findViewAddListener(R.id.tesegushou);
		simu=(LinearLayout) findViewAddListener(R.id.simu);
		cakeshome_list=(ListView) findViewById(R.id.cakeshome_list);
		liwuhome_list=(ListView) findViewById(R.id.liwuhome_list);
		findViewAddListener(R.id.search);
		findViewAddListener(R.id.shenggou);
		findViewAddListener(R.id.guanjia);
		findViewAddListener(R.id.youxuan);
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.GONE);
		LinearLayout ll_top_layout_left_view = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		findViewAddListener(R.id.ll_top_layout_left_view);
		ScrollView sView = (ScrollView)findViewById(R.id.sView); sView.setVerticalScrollBarEnabled(false);
		vp_ban.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event)	{
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		vp_ban.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int item) {
				currItem = item;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0){

			}
		});

		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.pushnews);
		
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
		RequestParams params2 = new RequestParams(this);
			params2.put("type", 1);
			execApi(ApiType.GET_INDEXFUNDLIST, params2);
			RequestParams params3 = new RequestParams(this);
			params3.put("type", 2);
			execApi(ApiType.GET_INDEXFUNDLISTTWO, params3);

	}
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_left_view:
			// 切换元素
			startActivity(SettingActivity.class);
		//	sm.toggle();
			break;
		//搜索
		case R.id.search:
			Intent intent9 =new Intent(MyfundHomeActivity.this,Myfundsearch.class);
			intent9.putExtra("flag", "2");
			startActivity(intent9);
			break;
		//基金超市
		case R.id.chaoshi:
			Intent intent=new Intent(MyfundHomeActivity.this, QueryFundActivity.class);
			intent.putExtra("sessionId",  App.getContext().getSessionid());
			startActivity(intent);
			break;
		//基金自选
		case R.id.zixuan:
			userName = App.getContext().getUserName();
			System.out.println("userNameuserNameuserName"+userName);
			if(userName==null||userName.equals("")){
				Intent intent7=new Intent(MyfundHomeActivity.this, MyMeansActivity.class);
				intent7.putExtra("Flag", flag);	
				startActivity(intent7);
			}else{
				Intent intent5=new Intent(MyfundHomeActivity.this, FundSelectActivity.class);
				intent5.putExtra("sessionId", sessionid);
				startActivity(intent5);
			}
			
			break;    
		//特色固收
		case R.id.tesegushou:
			Intent intent2=new Intent(MyfundHomeActivity.this, FeatureFixedActivity.class);
			//intent2.putExtra("detailurl", "http://m.myfund.com/hdl/index.aspx");
			startActivity(intent2);
			break;
		//私募基金
		case R.id.simu:
			startActivity(PlacementActivty.class);
			break;
		//免申购
		case R.id.shenggou:
		Intent intent1 = new Intent(MyfundHomeActivity.this,DianCaiTongActivity.class);
		intent1.putExtra("ImageID", "08");
		startActivity(intent1);	
		break;
		//私人管家
		case R.id.guanjia:
			Intent intent7=new Intent(MyfundHomeActivity.this,SeniorCustomActivity.class);
			intent7.putExtra("ImageID", "06");
			startActivity(intent7);	
			break;
		//展恒优选
		case R.id.youxuan:
			Intent intent8=new Intent(MyfundHomeActivity.this, FundOptimizationActivity.class);
			intent8.putExtra("ImageID", "10");
			startActivity(intent8);
			//startActivity(MyOptimize.class);
			break;
		default:
			break;
		}
	}


	@Override
	public void onReceiveData(ApiType api, RequestParams params, String json) {
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
				arrayimgs = new ArrayList<String>();
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
					System.out.println("URL---------->"+url);
					arrayimgs.add(url);
				}

				BannerAdapter bannerAdapter = new BannerAdapter(
						getSupportFragmentManager(), arrayimgs, this, bannerUrl,
						bannerDetail,shareurl);
				vp_ban.setAdapter(bannerAdapter);

				indicat.setViewPager(vp_ban);
			
			}else if(api==ApiType.GET_INDEXFUNDLIST){
				list = JSON.parseArray(json, MyIndexList.class);
				cakeshome_list.setAdapter(new MyfundHomeAdpter(list));
				Utility.setListViewHeightBasedOnChildren(cakeshome_list);
				cakeshome_list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MyIndexList indexList = list.get(position);
						Intent intent=new Intent(activity, DetailActivity.class);
						intent.putExtra("sessionId", sessionid);
						intent.putExtra("fundCode",indexList.getFundCode());
						intent.putExtra("fundName", indexList.getFundName());
						startActivity(intent);
					}
				});
				
			}
			else if (api==ApiType.GET_INDEXFUNDLISTTWO) {
				listret = JSON.parseArray(json, MyIndexList.class);
				liwuhome_list.setAdapter(new MyfundHomeAdpter(listret));
				Utility.setListViewHeightBasedOnChildren(liwuhome_list);
				liwuhome_list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MyIndexList myIndexList = listret.get(position);
						Intent intent =new Intent(activity, DetailActivity.class);
						intent.putExtra("sessionId", sessionid);
						intent.putExtra("fundCode",myIndexList.getFundCode());
						intent.putExtra("fundName", myIndexList.getFundName());
						startActivity(intent);
						
					}
				});
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static class Utility {  
	    public static void setListViewHeightBasedOnChildren(ListView listView) {  
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {  
	            // pre-condition  
	            return;  
	        }  
	  
	        int totalHeight = 0;  
	        for (int i = 0; i < listAdapter.getCount(); i++) {  
	            View listItem = listAdapter.getView(i, null, listView);  
	            listItem.measure(0, 0);  
	            totalHeight += listItem.getMeasuredHeight();  
	        }  
	  
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
	        listView.setLayoutParams(params);  
	    }  
	} 
	class MyfundHomeAdpter extends BaseAdapter{
		List<MyIndexList> mList;
		private MyIndexList res;
		
		
		public MyfundHomeAdpter(List<MyIndexList> list){
			this.mList=list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			holder=new ViewHolder();
			convertView=LayoutInflater.from(activity).inflate(R.layout.itme_home_lisetview, null);
			holder.fundname1 = (TextView) convertView.findViewById(R.id.fundname1);
			holder.recommend1 = (TextView) convertView.findViewById(R.id.recommend1);
			holder.year1 = (TextView) convertView.findViewById(R.id.year1);
			
			convertView.setTag(holder);
			
			holder = (ViewHolder) convertView.getTag();
			res = mList.get(position);
			holder.fundname1.setText(res.getFundName());
			holder.recommend1.setText(res.getRecommendReason());
			holder.year1.setText(res.getThisYearRedound());
			
			return convertView;
		}
		
		class ViewHolder{
			TextView fundname1,recommend1,year1;
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
			if (arrayimgs == null && arrayimgs.size() <= 0) {
				return;
			}
			currItem = (currItem + 1) % arrayimgs.size();
			handler.post(new Runnable() {

				@Override
				public void run() {
					vp_ban.setCurrentItem(currItem);
					indicat.setCurrentItem(currItem);
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
		//	editor.putString("UserList", listStr);
			editor.commit();
			finish();
			
		}
	}


}
