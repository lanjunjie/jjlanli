package com.myfp.myfund;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.myfp.myfund.ui.CemeteryFragmentActivity;
import com.myfp.myfund.ui.CouponActivity;
import com.myfp.myfund.ui.DealActivity;
import com.myfp.myfund.ui.DealApplyActivity;
import com.myfp.myfund.ui.DetailActivity;
import com.myfp.myfund.ui.FundsGroupActivity;
import com.myfp.myfund.ui.GoodsPassActivity;
import com.myfp.myfund.ui.GroupDetailActivity;
import com.myfp.myfund.ui.JoinActivity;
import com.myfp.myfund.ui.MyMeansActivity;
import com.myfp.myfund.ui.MyOptimize;
import com.myfp.myfund.ui.WriteNextActivity;

//网页跳转监听判断

public class JavaScriptInterface {

	private Context context;
	private String userName,sessionid;
	private String user = "trun";

	public JavaScriptInterface(Context context) {
		this.context = context;
		userName = App.getContext().getUserName();
		sessionid =App.getContext().getSessionid();
	}

	@JavascriptInterface
	public void showDetial(String type) {
		System.out.println(">>>>>>>>>>>>>来自web");
		Intent intent = null;
		if ("1".equals(type)) {
			intent = new Intent(context, FundsGroupActivity.class);
		} else if ("2".equals(type)) {
			intent = new Intent(context, GroupDetailActivity.class);
			intent.putExtra("recomtype", "2");
			intent.putExtra("fundName", "一键通·保守型");
			intent.putExtra("fundCode", "ZH0001");

		} else if ("3".equals(type)) {
			intent = new Intent(context, GroupDetailActivity.class);
			intent.putExtra("recomtype", "3");
			intent.putExtra("fundName", "一键通·稳健型");
			intent.putExtra("fundCode", "ZH0002");
		} else if ("4".equals(type)) {
			intent = new Intent(context, GroupDetailActivity.class);
			intent.putExtra("recomtype", "4");
			intent.putExtra("fundName", "一键通·平衡型");
			intent.putExtra("fundCode", "ZH0003");
		} else if ("5".equals(type)) {
			intent = new Intent(context, GroupDetailActivity.class);
			intent.putExtra("recomtype", "5");
			intent.putExtra("fundName", "一键通·积极型");
			intent.putExtra("fundCode", "ZH0004");
		} else if ("400-888-6661".equals(type)) {
			intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:400-888-6661"));
		} else if ("010-62020088".equals(type)) {
			intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:010-62020088"));
		} else if ("http://cai.myfund.com/diancaitong/aindex.html".equals(type)) {
			intent = new Intent(context, JoinActivity.class);
			intent.putExtra("JoinURL", type);
		} else if ("https://apptrade.myfund.com:8383/appweb/page/frame/frame.htm?test=y"
				.equals(type)) {
			intent = new Intent(context, DealActivity.class);
			intent.putExtra("KaihuURL", type);
		} else if ("duanwu2015".equals(type)) {
			Intent intent1 = new Intent(context, CouponActivity.class);
			intent1.putExtra("userName", userName);
			context.startActivity(intent1);
			((Activity) context).finish();
		} else if ("dct20150702".equals(type)) {
			 //sessionid = App.getContext().getSessionid();
			 userName = App.getContext().getUserName();
			 System.out.println("sessionid--dct20150702--->"+sessionid);
			if (userName == null) {
				intent = new Intent(context, MyMeansActivity.class);
				intent.putExtra("User", user);
			} else {
				intent = new Intent(context, GoodsPassActivity.class);
				
			}

		} else if ("Buyfund20150707".equals(type)) {
			intent = new Intent(context, CemeteryFragmentActivity.class);
		} else if ("OpenAcct20150707".equals(type)) {
			intent = new Intent(context, WriteNextActivity.class);
		} else if ("070019".equals(type)) {
		
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundCode", "070019");
			 intent.putExtra("fundName", "嘉实价值优势股票");

		} else if ("001017".equals(type)) {
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundName", "泰达改革动力混合");
			intent.putExtra("fundCode", "001017");
		} else if ("000534".equals(type)) {
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundName", "长盛高端装备混合");
			intent.putExtra("fundCode", "000534");
		} else if ("000800".equals(type)) {
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundName", "华商未来主题股票");
			intent.putExtra("fundCode", "000800");
		} else if ("161024".equals(type)) {
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundName", "富国中证军工指数分级");
			intent.putExtra("fundCode", "161024");
		} else if ("150205".equals(type)) {
			intent = new Intent(context, DetailActivity.class);
			intent.putExtra("fundName", "鹏华中证国防指数分级A");
			intent.putExtra("fundCode", "150205");
		}else if ("fund30".equals(type)) {
			intent=new Intent(context, MyOptimize.class);
			
		}
		else {
			intent = new Intent(context, DealActivity.class);
			intent.putExtra("GroupBuyURL", type);
		}
		context.startActivity(intent);
	}
}
