package com.myfp.myfund.ui;

import java.io.File;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class ShareActivity extends BaseActivity {

	private String imgPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/myfund"
			+ File.separator
			+ "MyFund.jpg";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_share);
	}

	@Override
	protected void initViews() {
		setTitle("分享");

		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.setImageResource(R.drawable.share_bt);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			// showToast("分享");
//			WeChatWebShare share = new WeChatWebShare(this);
			// share.wechatShare(this,
			// "http://www.myfund.com/app/download.html", imgPath, "财立方",
			// "扫一扫，和我们一起走上创业之路！", 0);
			showShare();

			// Intent intent = new Intent(Intent.ACTION_SEND);
			// intent.setType("text/plain");
			// intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
			// intent.putExtra(Intent.EXTRA_TEXT, "财立方分享");
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(Intent.createChooser(intent, getTitle()));

			// mSocialShare.show(ShareActivity.this.getWindow().getDecorView(),
			// mImageContent, FrontiaTheme.DARK, new ShareListener());
			break;

		default:
			break;
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this, true);
		final OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("展恒基金网");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.myfund.com/app/zh_download.html");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("展恒基金网 - 买基金免手续费，全国独家，省钱赚钱，11年信誉保障");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(imgPath);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.myfund.com/app/zh_download.html");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("展恒基金网");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.myfund.com/app/zh_download.html");
		// 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
		// oks.setCallback(new OneKeyShareCallback());
		// 通过OneKeyShareCallback来修改不同平台分享的内容
		// oks.setShareContentCustomizeCallback(
		// new ShareContentCustomizeDemo());

		/*
		 * oks.setTitle("title财立方"); oks.setTitleUrl("http://sharesdk.cn");
		 * oks.setImagePath(imgPath); oks.setImageUrl(
		 * "http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
		 * oks.setUrl("http://www.sharesdk.cn"); oks.setFilePath(imgPath);
		 * oks.setSite("Site财立方"); oks.setSiteUrl("http://sharesdk.cn");
		 * oks.setVenueName("ShareSDK");
		 * oks.setVenueDescription("This is a beautiful place!");
		 * oks.setLatitude(23.056081f); oks.setLongitude(113.385708f);
		 */

		// 启动分享GUI
		oks.show(this);
	}

}
