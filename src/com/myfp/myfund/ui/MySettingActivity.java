package com.myfp.myfund.ui;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.CreateBmpFactory;
import com.myfp.myfund.ui.view.HeadImageView;

public class MySettingActivity extends BaseActivity {
	private String idCard, passWord, encodeIdCard, encodePassWord,
			customrisklevel, countfund, depositacctname, totalfundmarketvalue,
			mobile, password, username;
	private String lean = "false";
	private TextView layout_person_name, layout_person_account;
	private HeadImageView img_avatar1;
	private CreateBmpFactory mBmpFactory;
	private String bitmapPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/cailifang"
			+ File.separator
			+ App.getContext().getUserName() + ".jpg";
	private Bitmap bitmap;
	private LinearLayout layout_menu;
	ByteArrayInputStream tInputStringStream = null;
	private String photoPath;
	private String custno;
	private String moneyaccount;
	private String depositacct;
	private String certificatetype;
	private String channeLid;

	@Override
	protected void setContentView() {
		setContentView(R.layout.new_activity_fit);
		Intent intent = getIntent();
		username = intent.getStringExtra("UserName");
		System.out.println("username3============>"+username);
		password = intent.getStringExtra("password");
		encodeIdCard = intent.getStringExtra("IDCard");
		encodePassWord = intent.getStringExtra("PassWord");
		customrisklevel = intent.getStringExtra("CustomRiskLevel");
		countfund = intent.getStringExtra("CountFund");
		depositacctname = intent.getStringExtra("DepositacctName");
		totalfundmarketvalue = intent.getStringExtra("TotalFundMarketValue");
		mobile = App.getContext().getMobile();
		mBmpFactory = new CreateBmpFactory(this);
		bitmap = BitmapFactory.decodeFile(bitmapPath);
		RequestParams params=new RequestParams(this);
		params.put("idcard", encodeIdCard);
		execApi(ApiType.GET_STEPVERIFICATION, params);
	}

	@Override
	protected void initViews() {
		setTitle("设置");
		layout_person_name = (TextView) findViewById(R.id.layout_person_name);
		layout_person_account = (TextView) findViewById(R.id.layout_person_account);
		img_avatar1 = (HeadImageView) findViewById(R.id.hiv_avatar1);
		findViewAddListener(R.id.hiv_avatar1);
		findViewAddListener(R.id.layout_person);
		//findViewAddListener(R.id.layout_personal);
		findViewAddListener(R.id.layout_phone);
		findViewAddListener(R.id.layout_loginpassword);
		findViewAddListener(R.id.layout_dealpassword);
		findViewAddListener(R.id.btn_backlogin);
		layout_menu = (LinearLayout) findViewById(R.id.layout_avatar_menu1);
		findViewAddListener(R.id.tv_avatarMenu_camera1);
		findViewAddListener(R.id.tv_avatarMenu_fromPhoto1);
		findViewAddListener(R.id.tv_avatarMenu_cancel1);
		layout_menu.setVisibility(View.GONE);
		layout_person_name.setText(depositacctname);
		layout_person_account.setText(mobile);
		if (bitmap != null) {
			img_avatar1.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoPath = mBmpFactory.getBitmapFilePath(requestCode,
				resultCode, data);
		bitmap = mBmpFactory.getBitmapByOpt(photoPath);
		if (bitmap != null) {
			img_avatar1.setImageBitmap(bitmap);
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

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.hiv_avatar1:
			// 更换头像
			layout_menu.setVisibility(View.VISIBLE);
			break;

		case R.id.tv_avatarMenu_camera1:
			// 拍照
			mBmpFactory.OpenCamera();
			break;
		case R.id.tv_avatarMenu_fromPhoto1:
			// 从相册选取
			mBmpFactory.OpenGallery();
			break;
		case R.id.tv_avatarMenu_cancel1:
			// 取消
			layout_menu.setVisibility(View.GONE);
			break;
		case R.id.layout_person:
			// 账户信息
			Intent intent9 = new Intent(MySettingActivity.this,
					PersonalInfoActivity.class);
			intent9.putExtra("DepositacctName", depositacctname);
			intent9.putExtra("Mobile", mobile);
			intent9.putExtra("IDCard", idCard);
			intent9.putExtra("UserName", username);
			intent9.putExtra("photoPath", photoPath);
			startActivity(intent9);
			break;
	//	case R.id.layout_personal:
		/*	// 我的银行卡
			Intent intent=new Intent(this, NewParagRaphActivity.class);
			intent.putExtra("IdCard", encodeIdCard);
			intent.putExtra("MoneyAccount", moneyaccount);
			intent.putExtra("Depositacct", depositacct);
			intent.putExtra("Custno", custno);
			intent.putExtra("Name", depositacctname);
			intent.putExtra("Channelid", channeLid);
			startActivity(intent);
			break;*/
		case R.id.layout_phone:
			// 更改手机绑定
			Intent intent =new Intent(MySettingActivity.this,UpdataPhoneAcitivity.class);
			intent.putExtra("username", username);
			intent.putExtra("IDCard", encodeIdCard);
			startActivity(intent);
			
			break;
		case R.id.layout_loginpassword:
			
			// 修改登录密码
			Intent intent12=new Intent(MySettingActivity.this, ResetPassActivity.class);
			startActivity(intent12);
			break;
		case R.id.layout_dealpassword:
			// 修改交易密码
			Intent intent13 = new Intent(MySettingActivity.this,
					ConfirmInformationActivity.class);
			intent13.putExtra("tar", "false");
			//intent13.putExtra("IDCard", encodeIdCard);
			//intent13.putExtra("PassWord", encodePassWord);
			startActivity(intent13);
			break;
		case R.id.btn_backlogin:
			// 安全退出
			App.getContext().setUserName(null);
			App.getContext().setIdCard(null);
			App.getContext().setEncodePassWord(null);
			App.getContext().setSessionid(null);
			App.getContext().userLevel = -1;
			SharedPreferences sPreferences = getSharedPreferences("Setting",
					MODE_PRIVATE);
			Editor editor = sPreferences.edit();
			editor.putString("EncodePassWord", null);
			editor.putString("password", null);
			editor.putString("UserName", null);
			editor.putString("sessionid", null);
			editor.commit();
			
			Intent intent10 = new Intent(MySettingActivity.this,
					MyActivityGroup.class);
			//intent10.putExtra("Flag", "4");
			startActivity(intent10);
			finish();
			MyActivityGroup.instance.finish();
			break;

		default:
			break;
		}

	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			disMissDialog();
			return;
		} else {
			if (api == ApiType.GET_STEPVERIFICATION) {

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
										System.out.println("<><><><><><><><><>"
												+ xmlReturn);
										
											JSONObject jsonObj;
											try {
												jsonObj = new JSONObject(
															xmlReturn);
												System.out.println("jsonObj======>"+jsonObj);
												custno = jsonObj.getString("custno");
												System.out.println("custno=====>"+custno);
												moneyaccount = jsonObj.getString("moneyaccount");
												System.out.println("moneyaccount----->"+moneyaccount);
												depositacct = jsonObj.getString("depositacct");
												System.out.println("depositacct----->"+depositacct);
												certificatetype = jsonObj.getString("certificatetype");
												System.out.println("certificatetype----->"+certificatetype);
												channeLid = jsonObj.getString("channelid");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										
									
										

									} catch (IOException e) {
										// TODO Auto-generated catch block
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
						try {
							tInputStringStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
	}


}
