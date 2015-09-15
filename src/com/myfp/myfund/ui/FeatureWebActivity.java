package com.myfp.myfund.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.ui.ShowWebActivity.LoadHttpsChrome;
import com.myfp.myfund.ui.ShowWebActivity.LoadHttpsView;
import com.myfp.myfund.utils.DataCleanManager;

public class FeatureWebActivity extends BaseActivity{

	private String detailurl;
	private String image,detail;
	private WebView webView;
	private String filepath = "/data/data/com.myfp.myfund/cache/webviewCacheChromium";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_show_web);
		showProgressDialog("正在加载");
		Intent intent = getIntent();
		detailurl = intent.getStringExtra("detailurl");
		image = intent.getStringExtra("Image");
		detail = intent.getStringExtra("Detail");
//		execApi(ApiType.GET_FEATURE, null);
	}

	@Override
	protected void initViews() {
		setTitle("展恒基金网");
		if (image!=null||detail!=null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_right_view);
			layout.setVisibility(View.VISIBLE);
			ImageView img = (ImageView) findViewAddListener(R.id.iv_mainactivity_top_right);
			img.setImageResource(R.drawable.share_bt);
		}
		webView = (WebView) findViewById(R.id.webView_deal_show);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new LoadHttpsView());
		webView.setWebChromeClient(new LoadHttpsChrome());
		DataCleanManager.cleanCustomCache(filepath);
		webView.loadUrl(detailurl);
		
	}
	class LoadHttpsView extends WebViewClient {
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			
			view.loadUrl(detailurl);
			System.out.println("1111111111");
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			webView.setVisibility(View.GONE);
			showToast("无法连接到网络");
			disMissDialog();
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			disMissDialog();
		System.out.println("22222222222222");
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			disMissDialog();
			super.onPageFinished(view, url);
		}

	}

	class LoadHttpsChrome extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			if (newProgress >= 90) {
				System.out.println("444444444444444");
			}
			System.out.println("33333333333333");
			super.onProgressChanged(view, newProgress);
		}
		
		

		@Override
		public boolean onJsTimeout() {
			return super.onJsTimeout();
		}
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_mainactivity_top_right:
			showShare();
			
//			mSocialShare.show(ShowWebActivity.this.getWindow().getDecorView(), mImageContent, FrontiaTheme.DARK,  new ShareListener());
		}
	}
	
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(detailurl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(detail+detailurl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + File.separator + "QRCode.jpg");
		oks.setImageUrl(image);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(detailurl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("展恒基金网");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(detailurl);

		// 启动分享GUI
		oks.show(this);
	}

	

}
