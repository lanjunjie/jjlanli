package com.myfp.myfund.api.beans;

public class SearchResult {
	private String FundName,FundCode,fundType,IsFlag;

	public String getIsFlag() {
		return IsFlag;
	}

	public void setIsFlag(String isFlag) {
		IsFlag = isFlag;
	}

	public String getFundName() {
		return FundName;
	}

	public void setFundName(String fundName) {
		FundName = fundName;
	}

	public String getFundCode() {
		return FundCode;
	}

	public void setFundCode(String fundCode) {
		FundCode = fundCode;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
}
