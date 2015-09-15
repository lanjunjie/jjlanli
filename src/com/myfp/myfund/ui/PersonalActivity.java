package com.myfp.myfund.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONArray;
import org.json.JSONException;

import u.aly.ca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.CreateBmpFactory;
import com.myfp.myfund.ui.view.HeadImageView;

/**
 * 个人中心
 * 
 * @author pengchong.jia
 * 
 */
public class PersonalActivity extends BaseActivity {

	private TextView tv_name, tv_mobile;
	private String name, mobile, userName;
	private LinearLayout layout_menu;
	private CreateBmpFactory mBmpFactory;
	private HeadImageView img_avatar;
	private Bitmap bitmap;
	private String bitmapPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()+ "/cailifang"
			+ File.separator
			+ App.getContext().getUserName() + ".jpg";
	private String uName;
	private String nameChecked;
	private TextView textw_Information;
	private TextView teView;
	private LinearLayout layout_personal_msg;
	

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_personal);

		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		System.out.println("name=============>"+name);
		mobile = intent.getStringExtra("mobile");
		System.out.println("mobile=================>"+mobile);
		userName = App.getContext().getUserName();

		mBmpFactory = new CreateBmpFactory(this);

		bitmap = BitmapFactory.decodeFile(bitmapPath);
		
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		execApi(ApiType.GET_USER_INFO, params);
		layout_personal_msg = (LinearLayout) findViewById(R.id.layout_personal_msg);
		layout_personal_msg.setOnClickListener(this);
		
		
	}

	@Override
	protected void initViews() {
		setTitle("个人中心");
		tv_name = (TextView) findViewById(R.id.tv_personal_name);
		tv_name.setText(name);
		tv_mobile = (TextView) findViewById(R.id.tv_personal_mobile);
		tv_mobile.setText(mobile);
		img_avatar = (HeadImageView) findViewById(R.id.hiv_avatar);
		if (bitmap != null) {
			img_avatar.setImageBitmap(bitmap);
		}
		textw_Information = (TextView) findViewById(R.id.textw_Information);
		
		findViewAddListener(R.id.layout_personal_info);
		//findViewAddListener(R.id.layout_personal_msg);		
		findViewAddListener(R.id.layout_personal_changePsw);
		//findViewAddListener(R.id.layout_personal_changePhone);
		findViewAddListener(R.id.bt_personal_logout);
		findViewAddListener(R.id.hiv_avatar);

		layout_menu = (LinearLayout) findViewById(R.id.layout_avatar_menu);
		findViewAddListener(R.id.tv_avatarMenu_camera);
		findViewAddListener(R.id.tv_avatarMenu_fromPhoto);
		findViewAddListener(R.id.tv_avatarMenu_cancel);
		layout_menu.setVisibility(View.GONE);
	}

/*	@Override
	protected void onRestart() {

		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		execApi(ApiType.GET_USER_INFO, params);
		super.onRestart();
	}
 */
	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		
		case R.id.layout_personal_info:

			Intent pInfo = new Intent(this, PersonalInfoActivity.class);
			pInfo.putExtra("UserName", userName);
			startActivity(pInfo);
			
			break;
	/*	case R.id.layout_personal_msg:
			startActivity(new Intent(this, RealNameTestActivity.class));
	
			break;*/
		case R.id.layout_personal_changePsw:
			startActivity(ResetPassActivity.class);
			break;
	/*case R.id.layout_personal_changePhone:
			Intent cMobile = new Intent(this, ChangeMobileActivity.class);
			cMobile.putExtra("UserName", userName);
			startActivity(cMobile);
			break; */
		case R.id.bt_personal_logout:
			// 退出登录
			App.getContext().setUserName(null);
			App.getContext().setIdCard(null);
			App.getContext().userLevel = -1;
			SharedPreferences sPreferences = getSharedPreferences("Setting",
					MODE_PRIVATE);
			Editor editor = sPreferences.edit();
			editor.putString("UserName", null);
			editor.putString("IDCard", null);
			editor.commit();
			startActivity(new Intent(this, MyActivityGroup.class));
			finish();
			break;
		case R.id.hiv_avatar:
			// 修改头像
			layout_menu.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_avatarMenu_camera:
			// 拍照
			mBmpFactory.OpenCamera();
			break;
		case R.id.tv_avatarMenu_fromPhoto:
			// 从相册选取
			mBmpFactory.OpenGallery();
			break;
		case R.id.tv_avatarMenu_cancel:
			// 取消
			layout_menu.setVisibility(View.GONE);
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
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_USER_INFO) {
				String displayName = array.getJSONObject(0).getString(
						"DisplayName");
				 uName = array.getJSONObject(0).getString("UserName");

				mobile = array.getJSONObject(0).getString("Mobile");
				nameChecked = array.getJSONObject(0).getString("IsRealNameChecked");
				System.out.println("nameChecked======---->"+nameChecked);
				
				if (nameChecked.equals("True")) {
					textw_Information.setText("实名认证" + "("+"已认证"+")");
					layout_personal_msg.setEnabled(false);
				}else {
					textw_Information.setText("实名认证" + "("+"未认证"+")");
					layout_personal_msg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							startActivity(new Intent(PersonalActivity.this, RealNameTestActivity.class));
						}
					});
				}
				
				
				if (TextUtils.isEmpty(displayName)) {
					name = uName;
				} else {
					name = displayName;
				}
				tv_name.setText(name);
				tv_mobile.setText(mobile);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String photoPath = mBmpFactory.getBitmapFilePath(requestCode,
				resultCode, data);
		bitmap = mBmpFactory.getBitmapByOpt(photoPath);
		if (bitmap != null) {
			img_avatar.setImageBitmap(bitmap);
			layout_menu.setVisibility(View.GONE);

			BufferedOutputStream bos;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(
						bitmapPath)));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
