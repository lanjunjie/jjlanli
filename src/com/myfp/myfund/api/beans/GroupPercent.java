package com.myfp.myfund.api.beans;

public class GroupPercent {
	private String FundCode,FundName,FundType,InvestPercent;

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

	public String getFundType() {
		return FundType;
	}

	public void setFundType(String fundType) {
		FundType = fundType;
	}

	public String getInvestPercent() {
		return InvestPercent;
	}

	public void setInvestPercent(String investPercent) {
		InvestPercent = investPercent;
	}
}
