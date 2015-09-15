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
import android.widget.TextView;

import cn.jpush.android.util.ac;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.BaseFragment;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.GetVersionUpdate;
import com.myfp.myfund.ui.view.HeadImageView;
import com.myfp.myfund.utils.VersionsUpdate;

/**
 * 左滑菜单的显示内容
 * 
 * @author Bruce.Wang
 */
public class SlidingMenuFragment extends BaseFragment implements
		OnClickListener {

	private static SlidingMenuFragment INSTANCE = new SlidingMenuFragment();
	private TextView textView_name, textView_phone;
	private HeadImageView hiv_avatar;
	private String userName;
	private HomeDimensionActivity activity;
	private String name, mobile, uName;
	private String encodePassWord,idCard;
	ByteArrayInputStream tInputStringStream = null;
	private View tv_bbh;

	public static Fragment getInstance() {
		return INSTANCE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (HomeDimensionActivity) getActivity();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, null);
		view.findViewById(R.id.rl_khjy).setOnClickListener(this);
		view.findViewById(R.id.rl_jjcx).setOnClickListener(this);
		view.findViewById(R.id.rl_jjzx).setOnClickListener(this);
		view.findViewById(R.id.rl_jjkx).setOnClickListener(this);
		view.findViewById(R.id.rl_clfjlb).setOnClickListener(this);
		view.findViewById(R.id.rl_clfjlb).setOnClickListener(this);
		view.findViewById(R.id.ll_setting).setOnClickListener(this);
		view.findViewById(R.id.ll_user).setOnClickListener(this);
		view.findViewById(R.id.ll_share).setOnClickListener(this);
		textView_name = (TextView) view.findViewById(R.id.tv_userName);
		textView_phone = (TextView) view.findViewById(R.id.tv_phone);
		hiv_avatar = (HeadImageView) view.findViewById(R.id.hiv_sliding_avatar);
	//	tv_bbh = view.findViewById(R.id.tv_setting_bbh);
		
		return view;
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_user:
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
		case R.id.rl_khjy:
			// 开户交易
			
			idCard = App.getContext().getIdCard();
			encodePassWord = App.getContext().getEncodePassWord();
			
			
			System.out.println("++++++++++++++++++++++++"+idCard);
			if(encodePassWord == null){
				activity.startActivity(MyMeansActivity.class);
				return;
			}else{
				
				RequestParams params = new RequestParams(activity.getApplicationContext());
				params.put("id", idCard);
				// params.put(RequestParams.MOBILE, username);
				params.put("passwd", encodePassWord);
				activity.execApi(ApiType.GET_DEALLOGIN, params);
				activity.showProgressDialog("正在加载");
				
			}
			break;
		case R.id.rl_jjcx:
			// 基金查询
		
			if (userName == null) {
				Intent myFund = new Intent(activity, QueryFundActivity.class);
				myFund.putExtra("UserName", "");
				startActivity(myFund);
			} else {
				Intent myFund = new Intent(activity, QueryFundActivity.class);
				myFund.putExtra("UserName", uName);
				startActivity(myFund);
			}
			break;
		case R.id.rl_jjzx:
			// 基金自选
			if (userName == null) {
				activity.showToast("未登录，请先登录！");
				activity.startActivity(MyMeansActivity.class);
				return;
			}
			Intent myFund = new Intent(activity, FundSelectActivity.class);
			myFund.putExtra("UserName", uName);
			startActivity(myFund);
			break;
		case R.id.rl_jjkx:
			// 基金快讯
			startActivity(new Intent(getActivity(), FundNewsActivity.class));
			break;
		case R.id.rl_clfjlb:
			// 财立方俱乐部
			if (userName == null) {
				activity.showToast("未登录，请先登录！");
				activity.startActivity(MyMeansActivity.class);
				return;
			}
			loginClub();
			break;
		case R.id.ll_share:
			// 分享
			startActivity(new Intent(getActivity(), ShareActivity.class));
			break;
		case R.id.ll_setting:
			// 设置
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		default:
			break;
		}
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
			textView_name.setText("个人中心");
			textView_phone.setText(null);
			hiv_avatar.setImageResource(R.drawable.avatar_not_login);

		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
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
}
