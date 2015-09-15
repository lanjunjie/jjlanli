package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.PlacementResult;
import com.myfp.myfund.api.beans.PurchaseDetailsResult;
import com.myfp.myfund.api.beans.PurchaseDetailsResult;
import com.myfp.myfund.ui.ManagerFragment.ManagerListAdapter;
import com.myfp.myfund.ui.PlacementActivty.PlacementAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyPurchaseDetailsFragment extends BaseFragment {
	private View view;
	private MyPropertyMemberActivity activityMes;
	ByteArrayInputStream tInputStringStream = null;
	private ListView list_Member;
	private List<PurchaseDetailsResult> list;
	private JSONArray jsonArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityMes = (MyPropertyMemberActivity) getActivity();
		
		Intent intent = activityMes.getIntent();
		String idcard = intent.getStringExtra("idcard");
		RequestParams params=new RequestParams(activityMes);
		System.out.println("params我的详情-=-==-=-==-=<>"+params);
		params.put("idcard".trim(), idcard.trim());
		activityMes.execApi(ApiType.GET_PURCHASEDETAILSNEW,params,this);
		activityMes.showProgressDialog("正在加载");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.item_applicant_details, null);
		list_Member = (ListView) view.findViewById(R.id.list_Member);
		
		
		return view;
	}
	

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("jsonAPiType=========================>"+json);

		if (json==null) {
			activityMes.showToast("数据获取失败");
			activityMes.disMissDialog();
			return;
		}
		if (api == ApiType.GET_PURCHASEDETAILSNEW) {

			if (json != null && !json.equals("")) {
				tInputStringStream = new ByteArrayInputStream(
						json.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				try {
					parser.setInput(tInputStringStream, "UTF-8");
					int event = parser.getEventType();
					while (event != XmlPullParser.END_DOCUMENT) {
						Log.i("start_document", "start_document");
						switch (event) {
						case XmlPullParser.START_TAG:
							if ("return".equals(parser.getName())) {
								try {
									String xmlReturn = parser.nextText();
									System.out.println("---------------------->"
											+ xmlReturn);
									
									List<PurchaseDetailsResult> results = JSON.parseArray(xmlReturn, PurchaseDetailsResult.class);
									list_Member.setAdapter(new PurchaseDetailsAdapter(results));

									//setListViewHeight(list_Member);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							break;

						}
						try {
							event = parser.next();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			activityMes.disMissDialog();
		}
		
		
	}
	
	class PurchaseDetailsAdapter extends BaseAdapter{
		List<PurchaseDetailsResult> mList;
		public PurchaseDetailsAdapter(List<PurchaseDetailsResult> list){
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
			ViewHolder holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView = LayoutInflater.from(activityMes).inflate(R.layout.item_applicant_detailsitem, null);
				holder.te_fund_name = (TextView) convertView.findViewById(R.id.te_fund_name);
				holder.te_purchase_date = (TextView) convertView.findViewById(R.id.te_purchase_date);
				holder.te_Amount = (TextView) convertView.findViewById(R.id.te_Amount);
				holder.te_buy_charge = (TextView) convertView.findViewById(R.id.te_buy_charge);
				convertView.setTag(holder);
				
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			PurchaseDetailsResult res = mList.get(position);
			System.out.println("res=================>"+res);
			
			
			holder.te_fund_name.setText(res.getFundname());
			String data = res.getTradedate().split(" ")[0];
			 String date = data.replaceAll("-", "/");
			holder.te_purchase_date.setText(date);
			holder.te_Amount.setText(res.getConfirmmon());
			holder.te_buy_charge.setText(res.getZhfee());
			
			return convertView;
		}
		class ViewHolder{
			TextView te_fund_name,te_purchase_date,te_Amount,te_buy_charge;
		}
		
	}
	
 		public void setListViewHeight(ListView listView) {    
        
        // 获取ListView对应的Adapter    
        
        ListAdapter listAdapter = listView.getAdapter();    
        
        if (listAdapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目    
            View listItem = listAdapter.getView(i, null, listView);    
            listItem.measure(0, 0); // 计算子项View 的宽高    
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度    
        }    
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    }    
	
	
}
