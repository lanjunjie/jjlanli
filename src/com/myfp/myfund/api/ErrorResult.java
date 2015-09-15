package com.myfp.myfund.api;

/**
 * 
 * 
 * @author wqz
 *
 */
public class ErrorResult extends ResponseResult {

	private static final long serialVersionUID = 1L;
	public ErrorResult(){}
	public ErrorResult(String code,String message){
		setCode(code);
		setMessage(message);
	}
}
