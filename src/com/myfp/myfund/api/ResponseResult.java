package com.myfp.myfund.api;

import java.io.Serializable;

import android.os.Bundle;

/**
 * 请求数据结果的基类
 * @author wqz
 *
 */
public class ResponseResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String RESULT_ERROR = "ERROR";
	public static final String RESULT_SUCCESS = "SUCCESS";
	public static final String RESULT_HTTP_ERROR = "HTTP_ERROR";
	
	public static final String RESULT_PASSWORD_ERROR = "password";
	public static final String RESULT_USERNAME_ERROR = "user_name";
	public static final String RESULT_MOBILE_ERROR = "mobile";
	public static final String RESULT_SYSTEM_ERROR = "system";
	public static final String RESULT_CAPTCHA_ERROR = "captcha";
	public static final String RESULT_MOBILE_EXISTS = "mobile_exists";
	
	
	public Bundle bundle = new Bundle();
	
	private String code;
	private String message;

	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public ResponseResult(){}



	public interface ResultStatus {

		// TODO 需要跟服务器接口一致
		String OK = "E_200";

		String FAILURE = "";

		public static final int ONE_POINT_STATUS = 9001;

	}

}
