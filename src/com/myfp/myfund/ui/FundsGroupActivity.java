package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.FundsGroup;
import com.myfp.myfund.ui.view.FlexibilityListView;


/**
 * 省心组合
 * @author Max.Zhao
 *
 */
public class FundsGroupActivity extends BaseActivity {
	
	private FlexibilityListView listView_fundGroup;
	private List<FundsGroup> fundsList;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fundsgroup);
		showProgressDialog("正在加载");
		execApi(ApiType.GET_RECOM_FUND, null);
	}

	@Override
	protected void initViews() {
		setTitle("一键通系列");
		listView_fundGroup = (FlexibilityListView) findViewById(R.id.listView_fundGroup_list);
	}

	@Override
	protected void onViewClick(View v) {

	}
	
	class GroupAdapter extends BaseAdapter{
		
		List<FundsGroup> list;
		
		public GroupAdapter(List<FundsGroup> list){
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
			final FundsGroup fundsGroup = list.get(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(FundsGroupActivity.this).inflate(R.layout.item_list_fundgroup, null, false);
				holder = new ViewHolder();
				holder.tv_fundName = (TextView) convertView.findViewById(R.id.textView_fundGroup_fundName);
				holder.tv_slogan = (TextView) convertView.findViewById(R.id.textView_fundGroup_slogan);
				holder.tv_eamings = (TextView) convertView.findViewById(R.id.textView_fundGroup_expectedEarnings);
				holder.tv_zhRate = (TextView) convertView.findViewById(R.id.textView_fundGroup_zhRate);
				holder.tv_rate = (TextView) convertView.findViewById(R.id.textView_fundGroup_rate);
				holder.tv_rate.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.tv_riskLevel = (TextView) convertView.findViewById(R.id.textView_fundGroup_riskLevel);
				holder.tv_min = (TextView) convertView.findViewById(R.id.textView_fundGroup_minInvestment);
				holder.tv_thatYear = (TextView) convertView.findViewById(R.id.textView_fundGroup_thatYearRedound);
				holder.tv_highYear = (TextView) convertView.findViewById(R.id.textView_fundGroup_highYearRedound);
				holder.tv_thatTitle = (TextView) convertView.findViewById(R.id.textView_fundGroup_6);
				holder.tv_highTitle = (TextView) convertView.findViewById(R.id.textView_fundGroup_7);
				holder.bt_buy = (Button) convertView.findViewById(R.id.tv_fundGroup_buy);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_fundName.setText(fundsGroup.getGroupFundsName()+"("+fundsGroup.getGroupFundCode()+")");
			holder.tv_slogan.setText(fundsGroup.getSlogan());
			String eamings = fundsGroup.getExpectedEarnings().trim();
			holder.tv_eamings.setText(eamings.substring(0, eamings.length()-1));
			holder.tv_zhRate.setText(fundsGroup.getZHRate());
			holder.tv_rate.setText(fundsGroup.getRate());
			holder.tv_riskLevel.setText(fundsGroup.getRiskLevel());
			holder.tv_min.setText(fundsGroup.getMinInvestment());
			holder.tv_thatYear.setText(fundsGroup.getThatYearRedound());
			holder.tv_highYear.setText(fundsGroup.getHighYearRedound());
			holder.tv_thatTitle.setText(fundsGroup.getThatYearRedoundTitle());
			holder.tv_highTitle.setText(fundsGroup.getHighYearRedoundTitle());
			holder.bt_buy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(FundsGroupActivity.this,DealActivity.class);
					intent.putExtra("GroupBuyURL", fundsGroup.getGroupBuyURL());
					startActivity(intent);
				}
			});
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_fundName,tv_slogan,tv_eamings,tv_zhRate,tv_rate,tv_riskLevel,tv_thatYear,tv_highYear,tv_min,tv_highTitle,tv_thatTitle;
			Button bt_buy;
		}
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		fundsList = JSON.parseArray(json, FundsGroup.class);
		
		listView_fundGroup.setAdapter(new GroupAdapter(fundsList));
		listView_fundGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long itemId) {
				Intent intent = new Intent(FundsGroupActivity.this,GroupDetailActivity.class);
				intent.putExtra("recomtype", fundsList.get(position).getGroupFundFlag());
				intent.putExtra("fundName", fundsList.get(position).getGroupFundsName());
				intent.putExtra("fundCode", fundsList.get(position).getGroupFundCode());
				startActivity(intent);
			}
		});
		disMissDialog();
	}
}
