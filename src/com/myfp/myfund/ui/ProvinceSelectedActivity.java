package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.DealSearchResult;

public class ProvinceSelectedActivity extends BaseActivity {
	ByteArrayInputStream tInputStringStream = null;
	private List<String> provincelist;
	ListView list_provinceselected;
	private String province = "";

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_provinceselected);
		provincelist = new ArrayList<String>();
		provincelist.add("福建");
		provincelist.add("山东");
		provincelist.add("四川");
		provincelist.add("云南");
		provincelist.add("北京");
		provincelist.add("河北");
		provincelist.add("江苏");
		provincelist.add("台湾");
		provincelist.add("辽宁");
		provincelist.add("吉林");
		provincelist.add("浙江");
		provincelist.add("西藏");
		provincelist.add("青海");
		provincelist.add("澳门");
		provincelist.add("湖北");
		provincelist.add("其它");
		provincelist.add("广西");
		provincelist.add("河南");
		provincelist.add("山西");
		provincelist.add("重庆");
		provincelist.add("江西");
		provincelist.add("天津");
		provincelist.add("宁夏");
		provincelist.add("陕西");
		provincelist.add("上海");
		provincelist.add("香港");
		provincelist.add("内蒙古");
		provincelist.add("黑龙江");
		provincelist.add("广东");
		provincelist.add("甘肃");
		provincelist.add("贵州");
		provincelist.add("新疆");
		provincelist.add("湖南");
		provincelist.add("海南");
		provincelist.add("安徽");

		String fileName = "province.txt"; // 文件名字

		try {

			InputStream in = getResources().getAssets().open(fileName);

			// \Test\assets\yan.txt这里有这样的文件存在

			int length = in.available();

			byte[] buffer = new byte[length];

			in.read(buffer);

			province = EncodingUtils.getString(buffer, "GBK");

		} catch (Exception e) {

			e.printStackTrace();

		}
		
		System.out.println(province);

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("省份");
		list_provinceselected = (ListView) findViewById(R.id.list_provinceselected);
		list_provinceselected.setAdapter(new ProviinceSearchAdapter(
				provincelist));
		list_provinceselected.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long itemId) {
				// TODO Auto-generated method stub
				String str = provincelist.get(position);
				JSONTokener jsonParser = new JSONTokener(province);
				// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
				// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
				try {
					JSONObject pro = (JSONObject) jsonParser.nextValue();
					Bundle bundle = new Bundle();
					bundle.putString("Pname", str);
					bundle.putString("Cname", pro.getJSONArray(str).get(0)
							.toString());

					Intent intent = new Intent(ProvinceSelectedActivity.this,
							BindingBankActivity.class);
					intent.putExtras(bundle);

					setResult(2, intent);
					finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

	}

	class ProviinceSearchAdapter extends BaseAdapter {

		private List<String> list;

		public ProviinceSearchAdapter(List<String> list) {
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

			convertView = LayoutInflater.from(ProvinceSelectedActivity.this)
					.inflate(R.layout.item_provinceselected, null, false);
			holder = new ViewHolder();

			holder.tv_provinceselected_province = (TextView) convertView
					.findViewById(R.id.tv_provinceselected_province);

			convertView.setTag(holder);

			final String res = list.get(position);
			holder.tv_provinceselected_province.setText(res);

			return convertView;
		}

		class ViewHolder {
			TextView tv_provinceselected_province;
		}

	}

}
