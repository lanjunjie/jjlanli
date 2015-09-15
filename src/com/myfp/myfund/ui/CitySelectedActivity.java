package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.ui.ProvinceSelectedActivity.ProviinceSearchAdapter;
import com.myfp.myfund.ui.ProvinceSelectedActivity.ProviinceSearchAdapter.ViewHolder;

public class CitySelectedActivity extends BaseActivity {
	ByteArrayInputStream tInputStringStream = null;
	private List<String> citylist;
	ListView list_cityselected;
	private String city = "";
	private String province;

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_cityselected);
		Intent intent = getIntent();
		province = intent.getStringExtra("Province");
		String fileName = "province.txt"; // 文件名字

		try {

			InputStream in = getResources().getAssets().open(fileName);

			// \Test\assets\yan.txt这里有这样的文件存在

			int length = in.available();

			byte[] buffer = new byte[length];

			in.read(buffer);

			city = EncodingUtils.getString(buffer, "GBK");

		} catch (Exception e) {

			e.printStackTrace();

		}

		JSONTokener jsonParser = new JSONTokener(city);
		// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
		// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
		JSONObject cty;
		citylist = new ArrayList<String>();
		try {
			cty = (JSONObject) jsonParser.nextValue();
			for (int i = 0; i < cty.getJSONArray(province).length(); i++) {
				citylist.add(cty.getJSONArray(province).get(i).toString());
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(city);

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("市");
		list_cityselected = (ListView) findViewById(R.id.list_cityselected);
		list_cityselected.setAdapter(new CitySearchAdapter(citylist));
		list_cityselected.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long itemId) {
				// TODO Auto-generated method stub
				String str = citylist.get(position);

				Bundle bundle = new Bundle();
				bundle.putString("Cname",str);
				Intent intent = new Intent(CitySelectedActivity.this,
						BindingBankActivity.class);
				intent.putExtras(bundle);

				setResult(3, intent);
				finish();

			}
		});
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class CitySearchAdapter extends BaseAdapter {

		private List<String> list;

		public CitySearchAdapter(List<String> list) {
			this.list = list;
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
			ViewHolder holder;

			convertView = LayoutInflater.from(CitySelectedActivity.this)
					.inflate(R.layout.item_cityselected, null, false);
			holder = new ViewHolder();

			holder.tv_cityselected_city = (TextView) convertView
					.findViewById(R.id.tv_cityselected_city);

			convertView.setTag(holder);

			final String res = list.get(position);
			holder.tv_cityselected_city.setText(res);

			return convertView;
		}

		class ViewHolder {
			TextView tv_cityselected_city;
		}

	}

}
