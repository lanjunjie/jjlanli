package com.myfp.myfund.api;

import com.myfp.myfund.OnDataReceivedListener;



/**
 * 网络接口调用的入口类
 * 
 * @author wqz
 * 
 */
public class RndDataApi {

//	private static final String TAG = BzDataApi.class.getSimpleName();

	/**
	 * 调用接口,扩充接口在{@link #ApiType}枚举中添加实例
	 * @param api		接口类型
	 * @param params	请求参数
	 * @param listener	回调方法
	 */
	public static void executeNetworkApi(ApiType api, RequestParams params,
			final OnDataReceivedListener listener) {

		//判断网络
		/*if(!BzUtil.isNetworkAvailable(BzApplication.context)){
		
			if(listener!=null){
				listener.onFailure("ERROR", "net_error");
			}
			return;
		}*/
		
		NetworkDataService.getNetworkDataService().callServerInterface(api,
				params, listener);
	}

}
