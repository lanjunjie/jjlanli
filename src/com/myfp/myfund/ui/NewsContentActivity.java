package com.myfp.myfund.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.NewsContent;


/**
 * 资讯详情页
 * @author Max.Zhao
 *
 */
public class NewsContentActivity extends BaseActivity {
	
	private TextView textView_title,textView_source,textView_content;
	private int newstype;
	private String title,pTitle;
	private String contentUrl;
	private String content;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_newscontent);
		
		showProgressDialog("正在加载");
		
		Intent intent = getIntent();
		newstype = intent.getIntExtra("newstype", 1);
		title = intent.getStringExtra("id");
		RequestParams params = new RequestParams(this);
//		params.put("newstype", newstype);
//		try {
//			pTitle = URLEncoder.encode(title,"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		params.put("id", title);
		execApi(ApiType.GET_CONTENTS, params,this);
	}

	@Override
	protected void initViews() {
		setTitle("资讯详情");
		textView_title = (TextView) findViewById(R.id.textView_newsContent_title);
		textView_source = (TextView) findViewById(R.id.textView_newsContent_source);
		textView_content = (TextView) findViewById(R.id.textView_newsContent_content);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewAddListener(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.share_bt);
	}
	
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_mainactivity_top_right:
			// showToast("分享");
//			Intent intent = new Intent(Intent.ACTION_SEND);
//			intent.setType("text/plain");
//			intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//			intent.putExtra(Intent.EXTRA_TEXT, title+contentUrl);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(Intent.createChooser(intent, getTitle()));
			
			showShare();
			
//			mSocialShare.show(NewsContentActivity.this.getWindow().getDecorView(), mImageContent, FrontiaTheme.DARK,  new ShareListener());
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("获取数据失败");
			return;
		}
		List<NewsContent> data = JSON.parseArray(json, NewsContent.class);
		textView_title.setText(data.get(0).getTitle());
		textView_source.setText(data.get(0).getSource()+"  "+data.get(0).getAddDate());
		content = data.get(0).getContent();
		content = content.replace("$", "\r\n");
		textView_content.setText(content);
		textView_content.setTextSize(16);
		contentUrl = data.get(0).getContentURL();
		disMissDialog();
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
		oks.setTitleUrl(contentUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(title+contentUrl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + File.separator + "QRCode.jpg");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(contentUrl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("展恒基金网");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
	
}
