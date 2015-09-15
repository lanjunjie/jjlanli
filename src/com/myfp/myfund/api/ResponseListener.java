package com.myfp.myfund.api;


/**
 * 连网API的回调接口
 * 
 * @author wqz
 *
 */
public interface ResponseListener {

	public void onResponse(ResponseResult result);

	public void onFailure(ErrorResult result);
}
