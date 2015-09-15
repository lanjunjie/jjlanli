package com.myfp.myfund.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.JavaScriptInterface;
import com.myfp.myfund.R;
import com.myfp.myfund.R.integer;
import com.myfp.myfund.ui.OnLinePayWebActivity.LoadHttpsChrome;
import com.myfp.myfund.ui.OnLinePayWebActivity.LoadHttpsView;

public class FirmReceivePayActivity extends BaseActivity{
	private WebView webView;
	private TextView textview;
	private static final String TAG = "DealActivity";
	private String fundCode, groupBuyUrl;
	private String mobile;
	private String displayname;
	private String username;
	private String in="年度会员/￥399";
	private String pidString="展恒恒得利";
	private String pidstr="展恒恒有财";
	private String disp;
	private String strin;
	private String pid;
	private String url;
	private String unt;
	private String formtwo;
	private String form;
	private String referrer;
	private String refer;
	private String id;
	private String op3_Amt;
	private String amt;

	@SuppressWarnings("deprecation")
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_deal);
		Intent intent = getIntent();
		 mobile = intent.getStringExtra("Mobile");
		 displayname = intent.getStringExtra("DisplayName");
		 username = intent.getStringExtra("UserName");
		 op3_Amt = intent.getStringExtra("op3_Amt");
		 form = getIntent().getStringExtra("form");
		 unt = intent.getStringExtra("Unt");
		 id = getIntent().getStringExtra("Id");
		try {
			if (form.equals("5")) {
				pid = URLEncoder.encode(pidString,"GB2312");
				
			}else if (form.equals("6")) {
				pid = URLEncoder.encode(pidstr,"GB2312");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url="http://www.myfund.com/yeeappay/paypre.aspx?ophone="+mobile+"&osource=APP&ousername="+displayname+"&oJiangLi="+amt+"&opid="+id+"&op3_Amt="+op3_Amt+"&orid="+5+"&op5_Pid="+pid+"&oTuiJianR=&yhq=";
		System.out.println("url=-=-=-=-=-=-=-=>"+url);
	}

	@Override
	protected void initViews() {
		setTitle("支付");
		webView = (WebView) findViewById(R.id.webView_deal);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		webView.loadUrl(url);
		
		webView.clearCache(true);
		webView.destroyDrawingCache();
		
		//findViewAddListener(R.id.imageView_deal_home);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
	//	case R.id.imageView_deal_home:
	//		finish();
	//		break;

		default:
			break;
		}
	}

	class LoadHttpsView extends WebViewClient {
		
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			Log.i(TAG, "current " + url);
			view.loadUrl(url);
			
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			webView.setVisibility(View.GONE);
			showToast("无法连接到网络");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i(TAG, "current >>>>>>>>>>>>>>>>>>> " + url);
			showProgressDialog("正在加载");
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.i(TAG, "current<<<<<<<<<<<<<<<<<<<< " + url);
			disMissDialog();
		}

	}

	class LoadHttpsChrome extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			Log.i(TAG, "current------------------------------------------------------ " + url);
/*			if(newProgress==1){
			showProgressDialog("正在加载中。。。。");
			}else if(newProgress==99){
				disMissDialog();
			}
			if (newProgress >= 90) {
			}*/
		
			super.onProgressChanged(view, newProgress);
		}
		
		

		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();
		}
	}

}
