package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.LunStampsResult;
import com.myfp.myfund.utils.HttpUtil;
import com.myfp.myfund.utils.ImageCacheManager;

public class LunStampsFragment extends BaseFragment{
	public CouponActivity activity;
	private RequestQueue requestQueue;
	private ImageLoader loader;
	private ListView list_coupon_view;
	private LinearLayout youhuiquan;
	private View view;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String vale=(String) msg.obj;
				final List<LunStampsResult> results = JSON.parseArray(vale, LunStampsResult.class);
				System.out.println("results====>"+results);
				list_coupon_view.setAdapter(new LunStampsAdapter(results));
				System.out.println("list_coupon_view=-=-=>"+list_coupon_view);
				list_coupon_view.setOnItemClickListener(new OnItemClickListener() {

					private LunStampsResult rse;

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						rse = results.get(arg2);
						Intent intent=new Intent(activity, SomeMoneyWebActivity.class);
						intent.putExtra("QuanURL", rse.getQuanURL());
						intent.putExtra("QuanDetail", rse.getQuanDetail());
						intent.putExtra("QuanPic", rse.getQuanPic());
						startActivity(intent);
						
					}
				});
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (CouponActivity) getActivity();
		final String GET_LUNSTAMPS="http://app.myfund.com:8585/Service/CLFDemo.svc/GetQuanList";
		new Thread(){
			public void run() {
				String sendGet=HttpUtil.sendGet(GET_LUNSTAMPS);
				Message msg = Message.obtain();
				msg.what=1;
				msg.obj=sendGet;
				handler.sendMessage(msg);
			};
		}.start();
		requestQueue = Volley.newRequestQueue(activity);
		loader = new ImageLoader(requestQueue, ImageCacheManager.getInstance());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_couponlistview, null);
		list_coupon_view = (ListView) view.findViewById(R.id.list_coupon_view);
		youhuiquan=(LinearLayout) view.findViewById(R.id.youhuiquan);
		youhuiquan.setVisibility(view.GONE);
		return view;
	} 
	@Override
	protected void onViewClick(View v) {
		
	}

	
	class LunStampsAdapter extends BaseAdapter{
		private List<LunStampsResult> mList;
		private NetworkImageView lun_stamps;
		public LunStampsAdapter(List<LunStampsResult> list){
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
			ViewHder hder = null ;
			//if (convertView==null) {
				new ViewHder();
				convertView = LayoutInflater.from(activity).inflate(R.layout.fragment_lunstamps, null);
				lun_stamps = (NetworkImageView) convertView.findViewById(R.id.lun_stamps);
				
			//	convertView.setTag(hder);
			//}else {
				//hder = (ViewHder) convertView.getTag();
		//	}
			LunStampsResult res = mList.get(position);
			String quanPic = res.getQuanPic();
			lun_stamps.setImageUrl(quanPic, loader);
			System.out.println("quanPic==============>"+quanPic);
			
			return convertView;
		}
		class ViewHder{
			NetworkImageView lun_stamps;
		}
		
	}
	
	

}
