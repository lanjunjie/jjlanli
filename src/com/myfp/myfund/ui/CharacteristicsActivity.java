package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.FeatureResult;
import com.myfp.myfund.utils.HttpUtil;
import com.myfp.myfund.utils.ImageCacheManager;

public class CharacteristicsActivity extends BaseActivity{

	private ListView list_view_feature;
	private List<FeatureResult> results;
	private RequestQueue requestQueue;
	private ImageLoader loader;
	private FeatureResult res;
	private String baseUrl = "http://www.myfund.com";
	private Handler handler=new Handler(){

		public void handleMessage(Message msg) {
			System.out.println("msg---------------------->"+msg);
			switch (msg.what) {
			case 1:
				String vale=(String) msg.obj;
				System.out.println("Vale===============>"+vale);
				results = JSON.parseArray(vale, FeatureResult.class);
				System.out.println("results-------------->"+results);
				list_view_feature.setAdapter(new FeatureAdapter(results));
				System.out.println("list_view_feature-------------->"+list_view_feature);
				list_view_feature.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FeatureResult res = results.get(position);
						Intent intent=new Intent(CharacteristicsActivity.this, FeatureWebActivity.class);
						String detailurl = res.getDetailurl();
						intent.putExtra("detailurl", detailurl);
						startActivity(intent);
					}
				});
				
				break;

			}
			disMissDialog();
		};
		
	};
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_feature);
		list_view_feature = (ListView) findViewById(R.id.list_view_feature);
		showProgressDialog("正在加载");
		final String CEMETEEY_URL="http://app.myfund.com:8585/Service/CLFDemo.svc/GetSpecfixList";
		new Thread(){
			public void run() {
			 String sendGet = HttpUtil.sendGet(CEMETEEY_URL);
			 Message msg = Message.obtain();
			 msg.what=1;
			 msg.obj=sendGet;
			 handler.sendMessage(msg);
				
			};
			
		}.start();
		requestQueue = Volley.newRequestQueue(this);
		loader = new ImageLoader(requestQueue, ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {
		setTitle("特色固收");
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}

	class FeatureAdapter extends BaseAdapter{
		List<FeatureResult> mList;
		private NetworkImageView iv_imag_logo;
		
		
		public FeatureAdapter(List<FeatureResult> list){
			this.mList=list;
			System.out.println("MLIST===============》"+mList);
		}
		@Override
		public int getCount() {
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
			if (convertView==null) {
				convertView = LayoutInflater.from(CharacteristicsActivity.this).inflate(R.layout.item_feature, null,false);
				holder=new ViewHolder();
				iv_imag_logo = (NetworkImageView) convertView
						.findViewById(R.id.iv_imag_logo);
				holder.text_view_name = (TextView) convertView.findViewById(R.id.text_view_name);
				holder.text_view_mos = (TextView) convertView.findViewById(R.id.text_view_mos);
				holder.text_view_amount = (TextView) convertView.findViewById(R.id.text_view_amount);
				holder.text_view_percentage = (TextView) convertView.findViewById(R.id.text_view_percentage);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			res = mList.get(position);
			holder.text_view_name.setText(res.getProname());
			holder.text_view_mos.setText(res.getLimittime());
			holder.text_view_amount.setText(res.getMininverst());
			holder.text_view_percentage.setText(res.getYearearns());
			
			iv_imag_logo.setImageUrl(baseUrl
					+ res.getLogo().substring(1), loader);
			return convertView;
		}
		class ViewHolder{
			TextView text_view_name,text_view_mos,text_view_amount,text_view_percentage;
			NetworkImageView iv_imag_logo;
		}
		
	}

}
