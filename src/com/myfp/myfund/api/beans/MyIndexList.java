package com.myfp.myfund.api.beans;

public class MyIndexList {
	private String FundCode,FundName,FundType,OneYearRedound,RecommendReason,ThisYearRedound;

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

	public String getOneYearRedound() {
		return OneYearRedound;
	}

	public void setOneYearRedound(String oneYearRedound) {
		OneYearRedound = oneYearRedound;
	}

	public String getRecommendReason() {
		return RecommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		RecommendReason = recommendReason;
	}

	public String getThisYearRedound() {
		return ThisYearRedound;
	}

	public void setThisYearRedound(String thisYearRedound) {
		ThisYearRedound = thisYearRedound;
	}

	@Override
	public String toString() {
		return "MyIndexList [FundCode=" + FundCode + ", FundName=" + FundName
				+ ", FundType=" + FundType + ", OneYearRedound="
				+ OneYearRedound + ", RecommendReason=" + RecommendReason
				+ ", ThisYearRedound=" + ThisYearRedound + "]";
	}
	
}
