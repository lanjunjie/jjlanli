package com.myfp.myfund.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.myfp.myfund.api.beans.MySelectFund;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

public abstract class BaseFixColumnListView extends ListView {

	protected List<ListItemHorizontalScrollView> mHScrollViews = new ArrayList<ListItemHorizontalScrollView>();
	public HorizontalScrollView TouchView;
	protected List<MySelectFund> mList = null;
	protected Context mContext;
	private FixColumnScrollViewAdapter viewAdapter;

	public BaseFixColumnListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public BaseFixColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;

	}

	public BaseFixColumnListView(Context context, AttributeSet pAttrs,
			int pDefStyle) {
		super(context, pAttrs, pDefStyle);
		mContext = context;
	}

	public void addHeaderScrollView(ListItemHorizontalScrollView header) {

		mHScrollViews.add(0, header);
	}

	public void layoutListView() {
		mList = setData();
		viewAdapter = new FixColumnScrollViewAdapter(mContext, mList);
		this.setAdapter(viewAdapter);
	}

	public void add2Header(List<MySelectFund> headerList) {
		if (mList == null) {
			mList = new ArrayList<MySelectFund>();
		}
		mList.addAll(0, headerList);
		if (viewAdapter == null) {
			viewAdapter = new FixColumnScrollViewAdapter(mContext, mList);
			this.setAdapter(viewAdapter);
		} else {
			// viewAdapter =(FixColumnScrollViewAdapter) this.getAdapter();
			viewAdapter.add2Header(headerList);
			viewAdapter.notifyDataSetChanged();
		}
	}

	public void add2Footer(List<MySelectFund> footerList) {
		if (mList == null) {
			mList = new ArrayList<MySelectFund>();
			mList.addAll(footerList);
		} else {
			if (viewAdapter == null) {
				mList.addAll(footerList);
				viewAdapter = new FixColumnScrollViewAdapter(mContext, mList);
				this.setAdapter(viewAdapter);
			}

			else {
				// viewAdapter =(FixColumnScrollViewAdapter) this.getAdapter();
				viewAdapter.add2Footer(footerList);
				viewAdapter.notifyDataSetChanged();
			}
		}
	}

	public void repData(List<MySelectFund> replaceList) {
		System.out.println(">>>>>>1111>>>>>>>>" + replaceList);
		if (mList == null) {
			mList = new ArrayList<MySelectFund>();
		} else {
			mList.clear();
		}
		System.out.println(">>>>>>2222>>>>>>>>" + replaceList);
		mList.addAll(replaceList);
		if (viewAdapter == null) {
			viewAdapter = new FixColumnScrollViewAdapter(mContext, mList);
			this.setAdapter(viewAdapter);
		} else {
			// viewAdapter =(FixColumnScrollViewAdapter) this.getAdapter();
			System.out.println(">>>>>>>>>>>444444>>>>>>>>>>>>" + replaceList);
			viewAdapter.replaceData(replaceList);
			viewAdapter.notifyDataSetChanged();
		}
	}

	public void ScrollChanged(int l, int t, int oldl, int oldt) {
		for (ListItemHorizontalScrollView scrollView : mHScrollViews) {
			// ��ֹ�ظ�����
			if (TouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
		// TODO Auto-generated method stub

	}

	public List<ListItemHorizontalScrollView> getItemScrollViewList() {
		return mHScrollViews;
	}

	public void addHViews(final ListItemHorizontalScrollView hScrollView) {

		if (!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
			ListItemHorizontalScrollView scrollView = mHScrollViews
					.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			// ��һ�����������»�������һ������ڿ�ʼʱδ����
			if (scrollX != 0) {
				BaseFixColumnListView.this.post(new Runnable() {
					@Override
					public void run() {
						// ��listViewˢ�����֮�󣬰Ѹ����ƶ�������λ��
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		mHScrollViews.add(hScrollView);
	}

	protected abstract View getCustomerView(int positon, View convertView,
			ViewGroup parent);

	protected abstract List<MySelectFund> setData();

	protected class FixColumnScrollViewAdapter extends BaseAdapter {

		Context mContext;
		List<MySelectFund> mList = null;

		public FixColumnScrollViewAdapter(Context context,
				List<MySelectFund> list) {
			mContext = context;
			mList = list;
		}

		public void add2Header(List<MySelectFund> headerList) {
			if (mList == null) {
				mList = new ArrayList<MySelectFund>();
			}
			mList.addAll(0, headerList);
			notifyDataSetChanged();
		}

		public void add2Footer(List<MySelectFund> footerList) {
			if (mList == null) {
				mList = new ArrayList<MySelectFund>();
			}
			mList.addAll(footerList);
			notifyDataSetChanged();
		}

		public void replaceData(List<MySelectFund> replaceList) {
			if (mList == null) {
				mList = new ArrayList<MySelectFund>();
			} else {
				mList.clear();
			}
			mList.addAll(replaceList);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int positon) {
			// TODO Auto-generated method stub
			return mList.get(positon);
		}

		@Override
		public long getItemId(int positon) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int positon, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomerView(positon, convertView, parent);
		}

	}

}
