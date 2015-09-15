package com.myfp.myfund;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.myfp.myfund.utils.RndUtil;

public class App extends Application {
	private final static String TAG = "App";
	private static App app;
	public List<Activity> unDestroyActivityList = new ArrayList<Activity>();
	public static String located;
	private boolean isFirst; // 第一次启动
	private boolean MainIsStart;
	private String userName,idCard,encodePassWord,sessionid; // 用户名
	public List<String> userList = new ArrayList<String>();
	private String userListStr;
	public static Context MCONTEXT = null;
	public static int tag = 0;
	public int userLevel = -1;
	private String mobile;
	private String depositacctName;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		initApp();

		SharedPreferences sPreferences = getSharedPreferences("Setting",MODE_PRIVATE);
		sessionid=sPreferences.getString("sessionid", null);
		isFirst = sPreferences.getBoolean("isFirst", true);
		userListStr = sPreferences.getString("UserList", null);
		userName = sPreferences.getString("UserName", null);
		idCard = sPreferences.getString("IDCard", null);
		encodePassWord = sPreferences.getString("EncodePassWord", null);
		depositacctName = sPreferences.getString("DepositacctName", null);
		mobile = sPreferences.getString("Mobile", null);
		if (userListStr != null) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>"+userListStr);
			userList = JSON.parseArray(userListStr, String.class);
		}
	}

	private void initApp() {
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		userName = null;
		MainIsStart = false;
		idCard = null;
		encodePassWord = null;
		sessionid=null;
		RndUtil.init();
		RndUtil.initImageLoader(app, null);
	}

	public static App getContext() {
		return app;
	}

	/**
	 * 退出应用
	 */
	public void quit() {
		for (Activity activity : unDestroyActivityList) {
			if (null != activity) {
				
				activity.finish();
			}
		}
		
		unDestroyActivityList.clear();
	}
	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getEncodePassWord() {
		return encodePassWord;
	}

	public void setEncodePassWord(String encodePassWord) {
		this.encodePassWord = encodePassWord;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public boolean isMainIsStart() {
		return MainIsStart;
	}

	public void setMainIsStart(boolean mainIsStart) {
		MainIsStart = mainIsStart;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDepositacctName() {
		return depositacctName;
	}

	public void setDepositacctName(String depositacctName) {
		this.depositacctName = depositacctName;
	}
	

}
