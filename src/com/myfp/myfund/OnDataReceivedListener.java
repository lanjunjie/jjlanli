package com.myfp.myfund;

import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

/**
 * 收到网络请求时调用的回调
 * @author Bruce.Wang
 *
 */
public interface OnDataReceivedListener{
	
	/**
	 * 收到网络请求
	 * @param json
	 */
	void onReceiveData(ApiType api,String json);
	
	public static interface OnDataReceivedListener2 extends OnDataReceivedListener{
		void onReceiveData(ApiType api,RequestParams params,String json);
	}
	
}
