package com.myfp.myfund.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.SelectedListResult;
import com.myfp.myfund.ui.view.FlexibilityListView;
import com.myfp.myfund.utils.ImageCacheManager;

/**
 * 单只精品列表
 * 
 * @author Max.Zhao
 * 
 */
public class SelectedListFragment extends BaseFragment {

	private int oqtype;
	private FlexibilityListView listView_selectedList;
	private ExpertSelectedActivity activity;
	private List<SelectedListResult> datas;
	private NetworkImageView networkImageView;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String baseUrl = "http://www.myfund.com";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		oqtype = bundle.getInt("oqtype");

		activity = (ExpertSelectedActivity) getActivity();
		activity.showProgressDialog("正在加载");
		
		RequestParams params = new RequestParams(activity);
		params.put("oqtype", oqtype);
		
		activity.execApi(ApiType.GET_ONEQUALITY, params, this);

		RequestParams bParams = new RequestParams(activity);
		bParams.put("InPutValue", oqtype + 10);
		activity.execApi(ApiType.GET_BANNER, bParams, this);

		requestQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_selectedlist, null,
				false);
		final LinearLayout headView = (LinearLayout)inflater.inflate(R.layout.view_selectedlist_head, null,
				false);
		listView_selectedList = (FlexibilityListView) view
				.findViewById(R.id.listView_selectedlist);
		listView_selectedList.addHeaderView(headView);
	
		networkImageView = (NetworkImageView) headView
				.findViewById(R.id.imageView_selectedlist_head);
		// networkImageView.setDefaultImageResId(R.drawable.ic_launcher);
		// networkImageView.setErrorImageResId(R.drawable.ic_launcher);
		// networkImageView.setImageUrl("http://www.baidu.com/img/baidu_sylogo1.gif",
		// imageLoader);
		

		return view;
	}

	class SelectedListAdapter extends BaseAdapter {

		private List<SelectedListResult> data;

		public SelectedListAdapter(List<SelectedListResult> data) {
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
			SelectedListResult slResult = data.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_selectedlist, null, false);
				holder = new ViewHolder();
				holder.textView_title = (TextView) convertView
						.findViewById(R.id.textView_selectedListItem_title);
				holder.textView_slogan = (TextView) convertView
						.findViewById(R.id.textView_selectedListItem_slogan);
				holder.textView_zhRate = (TextView) convertView
						.findViewById(R.id.textView_selectedListItem_zhRate);
				holder.textView_earning = (TextView) convertView
						.findViewById(R.id.textView_selectedListItem_earning);
				holder.textView_rate = (TextView) convertView
						.findViewById(R.id.textView_selectedListItem_rate);

				holder.imageView_logo = (NetworkImageView) convertView
						.findViewById(R.id.imageView_selectedListItem_logo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView_title.setText(slResult.getFundName() + "("
					+ slResult.getFundCode() + ")");
			holder.textView_slogan.setText(slResult.getSlogan());
			holder.textView_zhRate.setText(slResult.getZhRate());
			holder.textView_rate.setText(slResult.getRate());
			holder.textView_rate.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			String earning = slResult.getOneYearRedound();
			if (earning != null) {
				holder.textView_earning.setText(earning.substring(0,
						earning.length() - 1));
				if ("-".equals(earning.substring(0, 1))) {
					holder.textView_earning.setTextColor(Color.rgb(1, 153, 1));
				} else {
					holder.textView_earning.setTextColor(Color.RED);
				}
			}else {
				holder.textView_earning.setText("0.00");
			}
			// holder.imageView_logo.setDefaultImageResId(R.drawable.ic_launcher);
			// holder.imageView_logo.setErrorImageResId(R.drawable.ic_launcher);
			
			
			if (slResult.getLogo() != null) {
				holder.imageView_logo.setImageUrl(baseUrl
						+ slResult.getLogo().substring(1), imageLoader);
			}
			return convertView;
		}

		class ViewHolder {
			NetworkImageView imageView_logo;
			TextView textView_title, textView_slogan, textView_zhRate,
					textView_earning, textView_rate;
		}

	}

	@Override
	protected void onViewClick(View v) {

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		if (api == ApiType.GET_ONEQUALITY) {

			datas = JSON.parseArray(json, SelectedListResult.class);
			// 排序
			ListSort(datas);
			Collections.reverse(datas);

			listView_selectedList.setAdapter(new SelectedListAdapter(datas));
			listView_selectedList
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long itemId) {
							// 通过position从类表中得到当前基金名字
							if (position > 0) {
								String fundName = datas.get(position - 1)
										.getFundName();
								String fundCode = datas.get(position - 1)
										.getFundCode();

								Intent intent = new Intent(getActivity(),
										DetailActivity.class);
								intent.putExtra("fundName", fundName);
								intent.putExtra("fundCode", fundCode);
								startActivity(intent);
							}
						}
					});
			activity.disMissDialog();
		} else if (api == ApiType.GET_BANNER) {
			try {
				JSONArray array = new JSONArray(json);
				JSONObject obj = array.getJSONObject(0);
				String url = obj.getString("BannerPic").substring(1);
				if (url != null) {
					networkImageView.setImageUrl(baseUrl + url, imageLoader);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void ListSort(List<SelectedListResult> list) {
		ComparatorEarning comparator = new ComparatorEarning();
		Collections.sort(list, comparator);
	}

	class ComparatorEarning implements Comparator<SelectedListResult> {
		public int compare(SelectedListResult res1, SelectedListResult res2) {
			int flag = strToDouble(res1.getOneYearRedound()).compareTo(
					strToDouble(res2.getOneYearRedound()));
			return flag;
		}

		public Double strToDouble(String earning) {
			if (earning != null) {
				String temp = earning.substring(0, earning.length() - 1);
				Double _earning = Double.parseDouble(temp);
				return _earning;
			}
			return 0D;
		}
	}
}
