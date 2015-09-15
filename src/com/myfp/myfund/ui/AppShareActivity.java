package com.myfp.myfund.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.ShareInfo;
import com.myfp.myfund.utils.ImageCacheManager;


public class AppShareActivity extends BaseActivity {
	private ListView listView_appshare;
	private List<ShareInfo> shareList;
	private NetworkImageView img_applogo;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private String baseUrl = "http://www.myfund.com";
	private String downLoadURL;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_appshare);
		showProgressDialog("正在加载");

		execApi(ApiType.GET_APPSHARE, null);
		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("应用推荐");
		listView_appshare = (ListView) findViewById(R.id.appshare_list);

	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class ShareListAdapter extends BaseAdapter {

		private List<ShareInfo> data;

		public ShareListAdapter(List<ShareInfo> data) {
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
			ShareInfo si = data.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(AppShareActivity.this)
						.inflate(R.layout.item_appshare, null, false);
				holder = new ViewHolder();
				holder.tv_appname = (TextView) convertView
						.findViewById(R.id.share_appname);
				holder.tv_appdetail = (TextView) convertView
						.findViewById(R.id.share_appdetail);
				holder.img_applogo = (NetworkImageView) convertView
						.findViewById(R.id.share_applogo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_appname.setText(si.getAppName());
			holder.tv_appdetail.setText(si.getAppDetail());
			String url = si.getAppLogo().substring(1);
			if (url != null) {
				holder.img_applogo.setImageUrl(baseUrl + url, imageLoader);
			}
			// holder.imageView_logo.setDefaultImageResId(R.drawable.ic_launcher);
			// holder.imageView_logo.setErrorImageResId(R.drawable.ic_launcher);
			return convertView;
		}

		class ViewHolder {

			TextView tv_appname, tv_appdetail;
			NetworkImageView img_applogo;

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_APPSHARE) {
			shareList = JSON.parseArray(json, ShareInfo.class);

			listView_appshare.setAdapter(new ShareListAdapter(shareList));
			listView_appshare.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long itemId) {
				    downLoadURL = shareList.get(position).getAppAndroidURL();
					String pakgName = downLoadURL.substring(downLoadURL.indexOf("=") + 1,
							downLoadURL.length());
					System.out.println("--------------" + pakgName);
					List<ApplicationInfo> ls = getPackageManager()
							.getInstalledApplications(0);
					List<String> packageNames = new ArrayList<String>();
					// 获得包名
					if (ls != null) {
						for (int i = 0; i < ls.size(); i++) {
							String packName = ls.get(i).packageName;
							packageNames.add(packName);
							System.out.println("=============" + packName);
							System.out.println("121212121212121212"
									+ pakgName.equals(packName));
							
						}
						if (packageNames.contains(pakgName)) {
							
							Intent intent = getPackageManager()
									.getLaunchIntentForPackage(pakgName);
							startActivity(intent);
						}else {
							 Intent intent = new Intent();       
						        intent.setAction("android.intent.action.VIEW");   
						        Uri content_url = Uri.parse(downLoadURL);  
						        intent.setData(content_url); 
						        startActivity(intent);
						}

						// 打开应用程序
						// getPackageManager().getLaunchIntentForPackage(packName);
						// ComponentName comp = new
						// ComponentName(pakgName,pakgName+".ui.WelcomeActivity");
						// Intent intent = new Intent();
						// intent.setComponent(comp);
						// intent.setAction("android.intent.action.VIEW");
						// if(intent != null){
						// startActivity(intent);
						// }else{
						// Intent intent2 = new
						// Intent(AppShareActivity.this,PushedDetailActivity.class);
						// intent2.putExtra("DetailURL", url);
						// startActivity(intent2);
						// }

					}
				}
			});
			disMissDialog();
		}
	}
}
