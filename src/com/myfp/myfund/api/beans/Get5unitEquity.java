package com.myfp.myfund.api.beans;

public class Get5unitEquity {
	private String DayBenefit,DealDate,FundCode,FundName,UnitEquity,TotalEquity;
	public String getTotalEquity() {
		return TotalEquity;
	}
	public void setTotalEquity(String totalEquity) {
		TotalEquity = totalEquity;
	}
	private int Count,FundType;
	public String getDayBenefit() {
		return DayBenefit;
	}
	public void setDayBenefit(String dayBenefit) {
		DayBenefit = dayBenefit;
	}
	public String getDealDate() {
		return DealDate;
	}
	public void setDealDate(String dealDate) {
		DealDate = dealDate;
	}
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
	public String getUnitEquity() {
		return UnitEquity;
	}
	public void setUnitEquity(String unitEquity) {
		UnitEquity = unitEquity;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	public int getFundType() {
		return FundType;
	}
	public void setFundType(int fundType) {
		FundType = fundType;
	}
}
