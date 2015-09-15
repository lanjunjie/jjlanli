package com.myfp.myfund.api.beans;

public class ShareInfo {
	private String appName;
	private String appLogo;
	private String appDetail;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppLogo() {
		return appLogo;
	}
	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}
	public String getAppDetail() {
		return appDetail;
	}
	public void setAppDetail(String appDetail) {
		this.appDetail = appDetail;
	}
	public String getAppAndroidURL() {
		return appAndroidURL;
	}
	public void setAppAndroidURL(String appAndroidURL) {
		this.appAndroidURL = appAndroidURL;
	}
	public String getAppIOSURL() {
		return appIOSURL;
	}
	public void setAppIOSURL(String appIOSURL) {
		this.appIOSURL = appIOSURL;
	}
	private String appAndroidURL;
	private String appIOSURL;

}
