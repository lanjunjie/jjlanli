package com.myfp.myfund.api.beans;

public class GetFundReview {
	private String FundCode, FundName, FundReview, SendTime, UserName;

	public String getFundCode() {
		return FundCode;
	}

	public void setFundCode(String fundCode) {
		FundCode = fundCode;
	}

	public String getFundName() {
		return FundName;
	}

	public void setFundName(String fundName) {
		FundName = fundName;
	}

	public String getFundReview() {
		return FundReview;
	}

	public void setFundReview(String fundReview) {
		FundReview = fundReview;
	}

	public String getSendTime() {
		return SendTime;
	}

	public void setSendTime(String sendTime) {
		SendTime = sendTime;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
}
