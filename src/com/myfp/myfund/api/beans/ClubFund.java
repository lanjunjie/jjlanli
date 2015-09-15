package com.myfp.myfund.api.beans;

import java.io.Serializable;

public class ClubFund implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 132987529705L;
	private String Commission,ExpectedEarnings,FinancingTime,FundCode,FundName,FundType,IsOnSale,MinInvestPlus,MinInvestment,ProDetail,SellAccount,TimeLimit,MoreURL;

	public String getMoreURL() {
		return MoreURL;
	}

	public void setMoreURL(String moreURL) {
		MoreURL = moreURL;
	}

	public String getCommission() {
		return Commission;
	}

	public void setCommission(String commission) {
		Commission = commission;
	}

	public String getExpectedEarnings() {
		return ExpectedEarnings;
	}

	public void setExpectedEarnings(String expectedEarnings) {
		ExpectedEarnings = expectedEarnings;
	}

	public String getFinancingTime() {
		return FinancingTime;
	}

	public void setFinancingTime(String financingTime) {
		FinancingTime = financingTime;
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

	public String getFundType() {
		return FundType;
	}

	public void setFundType(String fundType) {
		FundType = fundType;
	}

	public String getIsOnSale() {
		return IsOnSale;
	}

	public void setIsOnSale(String isOnSale) {
		IsOnSale = isOnSale;
	}

	public String getMinInvestPlus() {
		return MinInvestPlus;
	}

	public void setMinInvestPlus(String minInvestPlus) {
		MinInvestPlus = minInvestPlus;
	}

	public String getMinInvestment() {
		return MinInvestment;
	}

	public void setMinInvestment(String minInvestment) {
		MinInvestment = minInvestment;
	}

	public String getProDetail() {
		return ProDetail;
	}

	public void setProDetail(String proDetail) {
		ProDetail = proDetail;
	}

	public String getSellAccount() {
		return SellAccount;
	}

	public void setSellAccount(String sellAccount) {
		SellAccount = sellAccount;
	}

	public String getTimeLimit() {
		return TimeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		TimeLimit = timeLimit;
	}
}
