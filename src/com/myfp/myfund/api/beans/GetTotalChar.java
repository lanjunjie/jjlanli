package com.myfp.myfund.api.beans;

public class GetTotalChar {
	private String DealDate;
	private String FundCode;
	private String FundName;
	private int FundType;
	private double Pchg;
	private double TotalEquity;
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
	public int getFundType() {
		return FundType;
	}
	public void setFundType(int fundType) {
		FundType = fundType;
	}
	public double getPchg() {
		return Pchg;
	}
	public void setPchg(double pchg) {
		Pchg = pchg;
	}
	public double getTotalEquity() {
		return TotalEquity;
	}
	public void setTotalEquity(double totalEquity) {
		TotalEquity = totalEquity;
	}
}
