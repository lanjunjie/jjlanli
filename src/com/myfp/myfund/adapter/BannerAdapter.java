package com.myfp.myfund.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.ui.ShowWebActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Banner的适配器
 * 
 * @author Bruce.Wang
 * 
 */
public class BannerAdapter extends FragmentPagerAdapter {

	private List<String> list,urlList,details,shareurl;
	private Context context;
	
	private List<Fragment> views;
	
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	public BannerAdapter(FragmentManager manager,List<String> list, Context context,List<String> urls,List<String> details, List<String> shareurl) {
		super(manager);
		this.list = list;
		this.context = context;
		this.urlList = urls;
		this.details = details;
		this.shareurl=shareurl;
		views = new ArrayList<Fragment>();
		for (int i = 0;i < list.size(); i++ ) {
			PicFragment frg = new PicFragment();
			frg.setUrl(list.get(i),urlList.get(i),details.get(i),shareurl.get(i));
			views.add(frg);
		}
	}
	
	


	@Override
	public Fragment getItem(int arg0) {
		return views.get(arg0);
	}
	
	public static class PicFragment extends BaseFragment{
		String url,contentUrl,detail,shareurl;
		
		public PicFragment(){}
		
		public void setUrl(String url,String contentUrl,String detail, String shareurl){
			this.url = url;
			this.contentUrl = contentUrl;
			this.detail = detail;
			this.shareurl=shareurl;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.item_banner_pic, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv);
			iv.setOnClickListener(this);
			ImageLoader.getInstance().displayImage(url, iv);
			return view;
		}

		@Override
		protected void onViewClick(View v) {
			Intent banner = new Intent(getActivity(),ShowWebActivity.class);
			banner.putExtra("Url", contentUrl);
			banner.putExtra("Detail", detail);
			banner.putExtra("Image", url);
			banner.putExtra("Share", shareurl);
			startActivity(banner);
		}
	}


}
