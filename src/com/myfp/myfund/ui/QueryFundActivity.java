package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.beans.StockIndex;

public class QueryFundActivity extends BaseActivity {

	private LinearLayout layout_tabs;
	private TextView[] tabs, moreTabs;
	private HorizontalScrollView hScrollView;
	
	private String userName;

	private List<StockIndex> indexs;
	private TextView tv_shanghai_tClose, tv_shanghai_chg, tv_shenzhen_tClose,
	tv_shenzhen_chg;
	private ImageView img_shanghai, img_shenzhen;
	
    private FrameLayout mContainer;
	
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_fund_query);
		
		Intent intent = getIntent();
		userName = intent.getStringExtra("UserName");
		
//		execApi(ApiType.GET_INDEX, null);
	}

	@Override
	protected void initViews() {
		setTitle("基金超市");
		mContainer = (FrameLayout) findViewById(R.id.layout_queryFund_container);
		
		findViewAddListener(R.id.ll_top_layout_right_view).setVisibility(View.VISIBLE);
		
		findViewAddListener(R.id.ib_queryFund_more);
		findViewAddListener(R.id.ib_queryFund_more_open);

		layout_tabs = (LinearLayout) findViewById(R.id.ll_queryFund_moreTabs);
		hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView_queryFundActivity);
		initTabView();
		addFragment(1);
		initMoreTabs();
		
	
	}

	private void initTabView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_queryFund_title);
		tabs = new TextView[8];
		for (int i = 0; i < tabs.length; i++) {
			TextView textView = (TextView) layout.getChildAt(i);
			tabs[i] = textView;
			tabs[i].setEnabled(true);
			tabs[i].setTag(i);
			tabs[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					addFragment((Integer) v.getTag() + 1);

					for (int i = 0; i < tabs.length; i++) {
						;
						tabs[i].setEnabled(true);
					}
					int j = (Integer) v.getTag();
					tabs[j].setEnabled(false);
				}
			});
		}
		tabs[0].setEnabled(false);
		tabs[0].setTextColor(Color.BLACK);
	}

	private void initMoreTabs() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_queryFund_moreTabs);
		moreTabs = new TextView[8];
		for (int i = 0; i < 2; i++) {
			LinearLayout sonLayout = (LinearLayout) layout
					.getChildAt(2 * i + 1);	
			for (int j = 0; j < 4; j++) {
				TextView textView = (TextView) sonLayout.getChildAt(2 * j);
				moreTabs[i * 4 + j] = textView;
				moreTabs[i * 4 + j].setEnabled(true);
				moreTabs[i * 4 + j].setTag(i * 4 + j);
				moreTabs[i * 4 + j].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < moreTabs.length; i++) {
							moreTabs[i].setEnabled(true);
							moreTabs[i].setTextColor(Color.BLACK);
						}
						int t = (Integer) v.getTag();
						moreTabs[t].setEnabled(false);
						moreTabs[t].setTextColor(Color.RED);
						
						addFragment(t + 1);
						hScrollView.scrollTo(t*80, 0);
						for (int i = 0; i < tabs.length; i++) {
							;
							tabs[i].setEnabled(true);
						}
						tabs[t].setEnabled(false);
						
						layout_tabs.setVisibility(View.GONE);
					}
				});
			}
		}
		moreTabs[0].setEnabled(false);
		moreTabs[0].setTextColor(Color.RED);

	}

	private void addFragment(int index) {
        Fragment fragment = (Fragment) mFragmentPagerAdapter
                .instantiateItem(mContainer, index);
        mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
        mFragmentPagerAdapter.finishUpdate(mContainer);
	}

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 2:
                return QueryFundFragment.instantiation(2);
            case 3:
                return QueryFundFragment.instantiation(5);
            case 4:
                return QueryFundFragment.instantiation(4);
            case 5:
                return QueryFundFragment.instantiation(7);
            case 6:
                return QueryFundFragment.instantiation(3);
            case 7:
                return QueryFundFragment.instantiation(12);
            case 8:
                return QueryFundFragment.instantiation(6);
       /*     case 9:
                return QueryFundFragment.instantiation(9);
            case 10:
                return QueryFundFragment.instantiation(10);
            case 11:
                return QueryFundFragment.instantiation(11);
            case 12:
                return QueryFundFragment.instantiation(12);
                */
            case 1:
            default:
                return QueryFundFragment.instantiation(1);
            }
        }
        @Override
        public int getCount() {
            return 12;
        }
    };
	
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ib_queryFund_more:
			layout_tabs.setVisibility(View.VISIBLE);
			break;
		case R.id.ib_queryFund_more_open:
			layout_tabs.setVisibility(View.GONE);
			break;
		case R.id.ll_top_layout_right_view:
			//搜索
			Intent intent =new Intent(this,Myfundsearch.class);
			intent.putExtra("flag", "1");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		if (api == ApiType.GET_INDEX) {
			indexs = JSON.parseArray(json, StockIndex.class);
			tv_shanghai_tClose.setText(indexs.get(0).getTclose()
					.substring(0, 7));
			String shChg = indexs.get(0).getChg();
			String shPchg = indexs.get(0).getPchg();
			if (shChg.substring(0, 1).equals("-")) {
				tv_shanghai_chg.setText(shChg.substring(1,
						shChg.length() - 2)
						+ "   "
						+ shPchg.subSequence(1, shPchg.length() - 2) + "%");
				tv_shanghai_chg.setTextColor(Color.rgb(1, 153, 1));
				tv_shanghai_tClose.setTextColor(Color.rgb(1, 153, 1));
				img_shanghai.setImageResource(R.drawable.arrows_down_small);
			} else {
				tv_shanghai_chg.setText(shChg.substring(0,
						shChg.length() - 2)
						+ "   "
						+ shPchg.subSequence(0, shPchg.length() - 2) + "%");
				tv_shanghai_chg.setTextColor(Color.RED);
				tv_shanghai_tClose.setTextColor(Color.RED);
				img_shanghai.setImageResource(R.drawable.arrows_up_small);
			}
			if (indexs.size() > 1) {

				tv_shenzhen_tClose.setText(indexs.get(1).getTclose()
						.substring(0, 7));
				String szChg = indexs.get(1).getChg();
				String szPchg = indexs.get(1).getPchg();
				if (szChg.substring(0, 1).equals("-")) {
					tv_shenzhen_chg.setText(szChg.substring(1,
							szChg.length() - 2)
							+ "   "
							+ szPchg.subSequence(1, szPchg.length() - 2)
							+ "%");
					tv_shenzhen_chg.setTextColor(Color.rgb(1, 153, 1));
					tv_shenzhen_tClose.setTextColor(Color.rgb(1, 153, 1));
					img_shenzhen
							.setImageResource(R.drawable.arrows_down_small);
				} else {
					tv_shenzhen_chg.setText(szChg.substring(0,
							szChg.length() - 2)
							+ "   "
							+ szPchg.subSequence(0, szPchg.length() - 2)
							+ "%");
					tv_shenzhen_chg.setTextColor(Color.RED);
					tv_shenzhen_tClose.setTextColor(Color.RED);
					img_shenzhen
							.setImageResource(R.drawable.arrows_up_small);
				}
			}
		}
	}
}
