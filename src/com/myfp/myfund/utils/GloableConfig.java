package com.myfp.myfund.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 
 * 存储全局<b>变量</b>
 * @author wqz
 *
 */
public class GloableConfig {

	private static Context appContext;
	
	//-----------------------------------
	//---变量
	
	/**	用户手机号	*/
	public static String user_mobile = "";
	//登陆的用户类型
	public static int user_type = 1;
	
	

	public synchronized static void setContext(Context ctx) {
		appContext = ctx;
	}

	public synchronized static Context getContext() {
		return appContext;
	}

	
	/**
	 * 获取设备的IMEI号
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		// TODO
		String imei = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

		if (TextUtils.isEmpty(imei)) {

			imei = "123456";
		}
		return imei;
	}

	/***
	 * 获取当前包的版本号(内部识别号)
	 * @return APP versionCode
	 */
	public static int getVersionCode(Context context)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
