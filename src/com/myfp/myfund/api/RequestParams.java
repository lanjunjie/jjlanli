package com.myfp.myfund.api;

import java.util.HashMap;

import android.content.Context;

/**
 * http请求参数
 * 
 * @author wqz
 * 
 */
public final class RequestParams extends HashMap<String, String> {

	private static final long serialVersionUID = 3693700342564975575L;

	public final static String TYPE = "type";
	
	
	public final static String USERNAME = "UserName";
	public final static String MOBILE = "Mobile";
	public final static String PASSWORD = "PassWord";
	public final static String iDIcard="idcard";
	public final static String RegistFro="RegistFrom";
	private Context mContext;

	public Context getmContext() {

		return mContext;
	}

	public void setmContext(Context mContext) {

		this.mContext = mContext;
	}

	// ===========================================================
	// Constructors
	// ===========================================================
	public RequestParams(Context context) {

		if (mContext == null) {
			mContext = context;
		}
		initParams();
	}

	private void initParams() {
		//TODO put一些必带的值
	}

	/**
	 * put文件信息
	 * @param key
	 * @param path
	 */
	public void putFile(String key,String path){
		put(NetworkHelper.Post_Entity_FILE_Data+key, path);
	}
	
	/**
	 * put a int value
	 * @param key
	 * @param val
	 */
	public void put(String key,int val){
		put(key,String.valueOf(val));
	}
	
	public void putDefault() {
		//put(USER_ID, BzGloableConfig.user_id);
		//put(TOKEN, BzGloableConfig.token);
	}
	
	public void removeDefaultParams() {
		//TODO remove init
	}

	@Override
	public void clear() {

		super.clear();
		initParams();
	}


	public interface ResultFormat {

		public static final String XML = "xml";

		public static final String JSON = "json";
	}



}
