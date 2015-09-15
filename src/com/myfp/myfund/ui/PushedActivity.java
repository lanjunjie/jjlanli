package com.myfp.myfund.ui;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.ImageCacheManager;

/**
 * 推送历史 activity
 * 
 * @author zhangtao
 * 
 */
public class PushedActivity extends BaseActivity {

	private String imgPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/myfund" + File.separator + "MyFund.jpg";
	private WebView webView;
	private String shareUrl, shareTitle, smallLogo;
	private NetworkImageView img_pushedContentLogo;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String pushKey;
	private String baseUrl = "http://www.myfund.com";
	TextView tv_pushedTitle, tv_pushedSource, tv_pushedAddDate,
			tv_pushedContent, tv_pushedSaleContent, tv_pushedDetail;
	ImageView img_share;
	private String detailURL;
	LinearLayout layout;
	private String url;
	private String contentlogo;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_pushed);

		Bundle bundle = getIntent().getExtras();

		String value = bundle.getString(JPushInterface.EXTRA_EXTRA);

		if (value == null) {
			Intent intent = getIntent();
			pushKey = intent.getStringExtra("PushKey");
		} else {
			try {
				;
				JSONObject obj = new JSONObject(value);
				pushKey = obj.getString("PushKey");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		showProgressDialog("正在加载");

		RequestParams params = new RequestParams(this);
		params.put("Key", pushKey);
		execApi(ApiType.GET_JPUSH_DETAIL, params);

		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {

		setTitle("最新信息");
		tv_pushedTitle = (TextView) findViewById(R.id.pushed_title);
		tv_pushedSource = (TextView) findViewById(R.id.pushed_source);
		tv_pushedAddDate = (TextView) findViewById(R.id.pushed_addDate);
		tv_pushedContent = (TextView) findViewById(R.id.pushed_content);
		tv_pushedSaleContent = (TextView) findViewById(R.id.pushed_saleContent);
		tv_pushedDetail = (TextView) findViewById(R.id.pushed_detail);
		img_pushedContentLogo = (NetworkImageView) findViewById(R.id.pushed_contentLogo);
		layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		img_share = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img_share.setImageResource(R.drawable.share_bt);

		/*
		 * LinearLayout layout = (LinearLayout)
		 * findViewById(R.id.ll_top_layout_right_view);
		 * layout.setVisibility(View.VISIBLE); ImageView img = (ImageView)
		 * findViewAddListener(R.id.iv_mainactivity_top_right);
		 * img.setImageResource(R.drawable.share_bt);
		 * 
		 * webView = (WebView) findViewById(R.id.webView_pushed); WebSettings
		 * webSettings = webView.getSettings();
		 * webSettings.setJavaScriptEnabled(true); webView.loadUrl(url);
		 * webView.setWebViewClient(new LoadHttpsView());
		 * webView.setWebChromeClient(new LoadHttpsChrome());
		 */
	}

	/*
	 * class LoadHttpsView extends WebViewClient { public void
	 * onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
	 * { view.loadUrl(url); }
	 * 
	 * @Override public void onPageStarted(WebView view, String url, Bitmap
	 * favicon) { super.onPageStarted(view, url, favicon); }
	 * 
	 * @Override public void onPageFinished(WebView view, String url) {
	 * disMissDialog(); super.onPageFinished(view, url); }
	 * 
	 * }
	 * 
	 * class LoadHttpsChrome extends WebChromeClient {
	 * 
	 * @Override public void onProgressChanged(WebView view, final int
	 * newProgress) { if (newProgress >= 90) { } super.onProgressChanged(view,
	 * newProgress); }
	 * 
	 * @Override public boolean onJsTimeout() { return super.onJsTimeout(); } }
	 */
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:

			showShare();

			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		// TODO Auto-generated method stub
		if (json == null) {
			showToast("请求失败");
			disMissDialog();
			return;
		}
		if (api == ApiType.GET_JPUSH_DETAIL) {
			JSONArray array;
			try {
				array = new JSONArray(json);
				final JSONObject obj = array.getJSONObject(0);
				tv_pushedTitle.setText(obj.getString("Title"));
				shareTitle = obj.getString("Title");

				tv_pushedSource.setText(obj.getString("Source"));
				tv_pushedAddDate.setText(obj.getString("PushTime"));
				shareUrl = obj.getString("ShareURL");
				String smalllg = obj.getString("SmallLogo");
				if (!(smalllg.length() == 0)) {
					smallLogo = smalllg.substring(1);
				}
				url = obj.getString("ContentLogo");
				if (!(url.length() == 0)) {
					contentlogo = url.substring(1);
					img_pushedContentLogo.setImageUrl(
							baseUrl + url.substring(1), imageLoader);
				}

				String content = obj.getString("Content");
				if (content.substring(0, 1) != null) {
					content = "        " + content;
				}
				content = content.replace("$", "\r\n" + "        ");
				tv_pushedContent.setText(content);

				String content1 = obj.getString("SaleContent");
				content1 = content1.replace("$", "\r\n");
				tv_pushedSaleContent.setText(content1);

				tv_pushedDetail.setText(Html.fromHtml("<u>查看详情</u>"));
				tv_pushedDetail.setOnClickListener(new OnClickListener() {

				

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {

						case R.id.pushed_detail:
							Intent intent = new Intent(PushedActivity.this,
									PushedDetailActivity.class);
							try {
								detailURL = obj.getString("DetailURL");
								intent.putExtra("DetailURL",
										obj.getString("DetailURL"));
								startActivity(intent);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			disMissDialog();
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
		oks.setTitleUrl(shareUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareTitle);
		oks.setImageUrl(baseUrl + contentlogo);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + File.separator + "QRCode.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(shareUrl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

}
