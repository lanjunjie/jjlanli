package com.myfp.myfund.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.utils.DataCleanManager;
import com.myfp.myfund.utils.VersionsUpdate;

/**
 * 设置页面
 * 
 * @author Bruce.Wang
 */
public class SettingActivity extends BaseActivity {
	
	private boolean pullIsOpen = true;
	private ImageView img_toggle;
	private TextView tv_version;
	private VersionsUpdate vu;
	@SuppressLint("SdCardPath")
	private String filepath = "/data/data/com.myfp.myfund";
	@SuppressLint("SdCardPath")
	private String filepath1 = "/data/data/com.myfp.myfund/cache/webviewCacheChromium";
	@SuppressLint("SdCardPath")
	private String filepath2 = "/data/data/com.myfp.myfund/cache/volley";
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_setting);
		vu = new VersionsUpdate(false,this);
	}

	@Override
	protected void initViews() {
		setTitle("设置");
//		img_toggle = (ImageView) findViewAddListener(R.id.img_setting_toggle);
		findViewAddListener(R.id.layout_setting_feedback);
		findViewAddListener(R.id.layout_setting_usingHelp);
		findViewAddListener(R.id.layout_setting_update);
		findViewAddListener(R.id.layout_setting_aboutUs);
		findViewAddListener(R.id.layout_setting_pushed);
//		findViewAddListener(R.id.layout_appShare);
		findViewAddListener(R.id.layout_cleandata);
		findViewAddListener(R.id.layout_quickmark);
		
		tv_version = (TextView) findViewById(R.id.tv_setting_version);
		try {
			tv_version.setText(vu.getVersionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
//		case R.id.img_setting_toggle:
//			//推送设置
//			SharedPreferences sPreferences = getSharedPreferences("Setting",
//					MODE_PRIVATE);
//
//			Editor editor = sPreferences.edit();
//			if (pullIsOpen) {
//				editor.putBoolean("IsAcceptPush", false);
//				editor.commit();
//				showToast("已关闭推送!");
//				img_toggle.setImageResource(R.drawable.switch_off);
//				pullIsOpen = false;
//				JPushInterface.stopPush(getApplicationContext());
//			}else {
//				editor.putBoolean("IsAcceptPush", true);
//				editor.commit();
//				showToast("已开启推送!");
//				img_toggle.setImageResource(R.drawable.switch_on);
//				pullIsOpen = true;
//				JPushInterface.resumePush(getApplicationContext());
//			}
//			break;
		case R.id.layout_setting_feedback:
			startActivity(FeedbackActivity.class);
			break;
		case R.id.layout_setting_usingHelp:
			startActivity(UsingHelpActivity.class);
			break;
		case R.id.layout_setting_update:
			//检查更新
			vu.getVersion();
			break;
		case R.id.layout_setting_aboutUs:
			startActivity(AboutUsActivity.class);
			break;
		case R.id.layout_setting_pushed:
			startActivity(PushListActivity.class);
			break;
//		case R.id.layout_appShare:
//			startActivity(AppShareActivity.class);
//			break;
			//清除数据
		case R.id.layout_cleandata:
			new Thread(){
				public void run() {
					DataCleanManager.cleanApplicationData(getApplicationContext(),
							new String[]{filepath,filepath1,filepath2});
				};
			}.start();
			showToast("数据清除成功");
			break;
		//分享给好友
		case R.id.layout_quickmark:
			startActivity(ShareActivity.class);
			break;
		default:
			break;
		}
	}

	
}
