package com.myfp.myfund.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.myfp.myfund.App;
import com.myfp.myfund.R;

public class RndUtil {

	private static SharedPreferences mPref;

	public static void init() {
		mPref = App.getContext().getSharedPreferences(RndConstant.SP_NAME, Context.MODE_PRIVATE);
	}
	
	public static void clearSP() {
		mPref.edit().clear().commit();
	}
	
	public static void writeSP(String key, String value) {
		mPref.edit().putString(key, value).commit();
	}
	
	public static String getSP(String key) {
		return mPref.getString(key, "");
	}
	public static String getSP(String key,String defVal) {
		return mPref.getString(key, defVal);
	}
	
	/**
	 * 获取MD5加密字符串(32位)
	 * @param plainText
	 * @return
	 */
	public static String Md5(String plainText) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(plainText.getBytes());
		byte b[] = md.digest();

		int i;

		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	} 
	
	
	/**
	 * 初始化图片加载器
	 * @param mContext
	 * @param defaultOptions
	 */
	public static void initImageLoader(Context mContext,
			DisplayImageOptions defaultOptions) {

		if (defaultOptions == null)
			defaultOptions = buildImageOptions(mContext);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext)
				.threadPoolSize(5)
				.defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(30 * 1024 * 1024)
				.discCache(
						new UnlimitedDiscCache(IOUtils.getImageLoaderCacheDir()))
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 创建图片参数
	 * @param mContext
	 * @return
	 */
	private static DisplayImageOptions buildImageOptions(Context mContext) {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.banner_default)
//				.showImageForEmptyUri(R.drawable.banner_default)
//				.showImageOnFail(R.drawable.banner_default)
				.considerExifParams(true).cacheInMemory(true).cacheOnDisc(true)
				.build();

		return defaultOptions;
	}
	
	/**
	 * 判断activity是否在运行
	 * @param mContext
	 * @param activityClassName
	 * @return
	 */
	public static boolean isActivityRunning(Context mContext,
			String activityClassName) {
		
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> info = activityManager.getRunningTasks(1);
		if (info != null && info.size() > 0) {
			ComponentName component = info.get(0).topActivity;
			if (activityClassName.equals(component.getClassName())) {
				RndLog.d("RndUtils", activityClassName+" is running...");
				return true;
			}
		}
		return false;
	} 
	
}
