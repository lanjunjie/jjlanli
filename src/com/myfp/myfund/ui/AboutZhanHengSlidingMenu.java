package com.myfp.myfund.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.util.ac;
import cn.sharesdk.onekeyshare.SlideMenu;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.HeadImageView;
import com.myfp.myfund.ui.view.SlidingMenu;
import com.myfp.myfund.utils.VersionsUpdate;

/**
 * 左滑菜单的显示内容
 * 
 * @author Bruce.Wang
 */
public class AboutZhanHengSlidingMenu extends BaseFragment implements
		OnClickListener {

	private static AboutZhanHengSlidingMenu INSTANCE = new AboutZhanHengSlidingMenu();
	private TextView textView_name, textView_phone;
	private HeadImageView hiv_avatar;
	private String userName;
	private MyfundHomeActivity activity;
	private String name, mobile, uName;
	private String encodePassWord,idCard;
	private VersionsUpdate vu;
	ByteArrayInputStream tInputStringStream = null;

	public static Fragment getInstance() {
		return INSTANCE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MyfundHomeActivity) getActivity();
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.framg_slidingmenu, null);
		
		
		view.findViewById(R.id.rl_gyzh).setOnClickListener(this);
		view.findViewById(R.id.rl_tssj).setOnClickListener(this);
		view.findViewById(R.id.rl_yjfk).setOnClickListener(this);
		view.findViewById(R.id.rl_bbgx).setOnClickListener(this);
		view.findViewById(R.id.rl_sybz).setOnClickListener(this);
		view.findViewById(R.id.rl_fx).setOnClickListener(this);
		view.findViewById(R.id.rl_gwdgf).setOnClickListener(this);
		view.findViewById(R.id.ll_set).setOnClickListener(this);
		view.findViewById(R.id.ll_us).setOnClickListener(this);
		view.findViewById(R.id.ll_fx).setOnClickListener(this);
//		view.findViewById(R.id.img_setting_toggle).setOnClickListener(this);
		
		textView_name = (TextView) view.findViewById(R.id.tv_usName);
		textView_phone = (TextView) view.findViewById(R.id.tv_phone);
		hiv_avatar = (HeadImageView) view.findViewById(R.id.hiv_sliding_tar);
		
		return view;
	}

	
	private void loginClub() {
		//判断用户权限
		switch (App.getContext().userLevel) {
		case 0:
			//跳转之前将权恢复默认
			App.getContext().userLevel = -1;
			activity.startActivity(ClubHomeActivity.class);
			break;
		case 1:
			App.getContext().userLevel = -1;
			activity.showToast("您还未登录，请先登录");
			activity.startActivity(MyMeansActivity.class);
			break;
		case 2:
			App.getContext().userLevel = -1;
			Intent intent2 = new Intent(getActivity(), ClubVerifyActivity.class);
			intent2.putExtra("result", 2);
			startActivity(intent2);
			break;
			
		case 3:
			App.getContext().userLevel = -1;
			Intent intent1 = new Intent(getActivity(), ClubVerifyActivity.class);
			intent1.putExtra("result", 1);
			startActivity(intent1);
			break;
		case 4:
			App.getContext().userLevel = -1;
			activity.startActivity(ClubActivity.class);
			break;
		case 5:
			App.getContext().userLevel = -1;
			activity.showToast("系统参数错误！");
			break;
		default:
			RequestParams params = new RequestParams(activity);
			params.put("UserName", uName);
			activity.execApi(ApiType.GET_USER_LEVEL, params, this);
			activity.showProgressDialog("获取权限中……");
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		// 查询是否登录
		userName = App.getContext().getUserName();
		if (userName != null) {
			RequestParams params = new RequestParams(activity);
			params.put(RequestParams.USERNAME, userName);
			activity.execApi(ApiType.GET_USER_INFO, params, this);

		} else {
			textView_name.setText("马上登陆");
			textView_phone.setText(null);
			hiv_avatar.setImageResource(R.drawable.avatar_login);

		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		System.out.println("json请求-=-=-=-=》"+json);
		if (json == null) {
			activity.showToast("请求失败");
			activity.disMissDialog();
			return;
		}
		try {
			
			if (api == ApiType.GET_USER_INFO) {
				JSONArray array = new JSONArray(json);
				String displayName = array.getJSONObject(0).getString(
						"DisplayName");
				uName = array.getJSONObject(0).getString("UserName");

				mobile = array.getJSONObject(0).getString("Mobile");
				if (TextUtils.isEmpty(displayName)) {
					name = uName;
				} else {
					name = displayName;
				}
				textView_name.setText(name);
				textView_phone.setText(mobile);
				
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory().getAbsolutePath()+ "/cailifang"
						+ File.separator + uName + ".jpg");
				
				if (bitmap != null) {
					hiv_avatar.setImageBitmap(bitmap);
				} else {
					hiv_avatar.setImageResource(R.drawable.avatar_login);
				}
			} else if (api == ApiType.GET_USER_LEVEL) {
				JSONArray array = new JSONArray(json);
				App.getContext().userLevel = array.getJSONObject(0).getInt("ReturnResult");
				activity.disMissDialog();
				loginClub();
			}else if (api == ApiType.GET_DEALLOGIN) {

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
										
										
										try {
											JSONObject jsonObj = new JSONObject(xmlReturn);
											if (xmlReturn.contains("custname")) {
												

												App.getContext().setEncodePassWord(encodePassWord);
												App.getContext().setIdCard(idCard);
												
												
												activity.showToast("登陆成功！！");
												Intent intent = new Intent(
														activity.getApplicationContext(),
														DealInforActivity.class);
												intent.putExtra("IDCard",
														idCard);
												intent.putExtra("PassWord",
														encodePassWord);
												intent.putExtra("CustomRiskLevel",
														jsonObj.getString("risklevel"));
												intent.putExtra("DepositacctName",
														jsonObj.getString("depositacctname"));
												intent.putExtra("TotalFundMarketValue",
														jsonObj.getString("totalfundmarketvalue"));
												intent.putExtra("CountFund",
														jsonObj.getString("countfund"));
												startActivity(intent);
											} else {
												activity.showToast("登录失败，请重新登陆！！");
												return;
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										//String returnResult = jsonObj.getString("loginflag");
										

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
				activity.disMissDialog();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	class CheckUpdatesActivity extends BaseActivity{

		private TextView tv_setting_bbh;

		@Override
		protected void setContentView() {
			// TODO Auto-generated method stub
			vu = new VersionsUpdate(false,this);
		}

		@Override
		protected void initViews() {
			tv_setting_bbh = (TextView)findViewById(R.id.tv_setting_bbh);
			try {
				tv_setting_bbh.setText(vu.getVersionName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onViewClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.ll_us:
					// 登录or用户中心
					
					if (userName != null) {
						// 跳转用户中心界面
						Intent intent = new Intent(getActivity(),
								PersonalActivity.class);
						intent.putExtra("UserName", userName);
						intent.putExtra("name", name);
						intent.putExtra("mobile", mobile);
						startActivity(intent);
					} else {
						startActivity(new Intent(getActivity(), MyMeansActivity.class));
					}
					break;
				//关于展恒
				case R.id.rl_gyzh:
					startActivity(new Intent(getActivity(),AboutUsActivity.class));
					break;
				//意见反馈
				case R.id.rl_yjfk:
						startActivity(new Intent(getActivity(),FeedbackActivity.class));
					break;
				case R.id.rl_bbgx:
					vu.getVersion();
					break;
					
				case R.id.rl_sybz:
					startActivity(new Intent(getActivity(),UsingHelpActivity.class));
					break;
//				case R.id.img_setting_toggle:
					//推送设置
					
//					break;
				case R.id.rl_fx:
					startActivity(new Intent(getActivity(), ShareActivity.class));
					break;
				// 分享
				case R.id.ll_fx:
					startActivity(new Intent(getActivity(), ShareActivity.class));
					break;
				case R.id.ll_set:
					// 设置
					startActivity(new Intent(getActivity(), SettingActivity.class));
					break;
				
				}
		
	}
	
	
}
