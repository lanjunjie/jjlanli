package com.myfp.myfund.api.beans;

public class MySelectFund {
	public String DealDate ,FundCode ,FundName ,FundType , IsFlag,IsOpenToBuy ,Rate  ,ZHRate;
	public Double SixMonthRedound,DayBenefit,OneMonthRedound ,OneWeekRedound,OneyearRedound ,ThisYearRedound ,ThreeMonthRedound ,TotalEquity,UnitEquity ;
	public MySelectFund(){
		
	}
	
	public MySelectFund(String FundName){
		this.FundName = FundName;
	}
	
	public Double getDayBenefit() {
		return DayBenefit;
	}

	public void setDayBenefit(Double dayBenefit) {
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

	public String getFundType() {
		return FundType;
	}

	public void setFundType(String fundType) {
		FundType = fundType;
	}

	public String getIsFlag() {
		return IsFlag;
	}

	public void setIsFlag(String isFlag) {
		IsFlag = isFlag;
	}

	public String getIsOpenToBuy() {
		return IsOpenToBuy;
	}

	public void setIsOpenToBuy(String isOpenToBuy) {
		IsOpenToBuy = isOpenToBuy;
	}

	public Double getOneMonthRedound() {
		return OneMonthRedound;
	}

	public void setOneMonthRedound(Double oneMonthRedound) {
		OneMonthRedound = oneMonthRedound;
	}

	public Double getOneWeekRedound() {
		return OneWeekRedound;
	}

	public void setOneWeekRedound(Double oneWeekRedound) {
		OneWeekRedound = oneWeekRedound;
	}

	public Double getOneyearRedound() {
		return OneyearRedound;
	}

	public void setOneyearRedound(Double oneyearRedound) {
		OneyearRedound = oneyearRedound;
	}

	public String getRate() {
		return Rate;
	}

	public void setRate(String rate) {
		Rate = rate;
	}

	public Double getSixMonthRedound() {
		return SixMonthRedound;
	}

	public void setSixMonthRedound(Double sixMonthRedound) {
		SixMonthRedound = sixMonthRedound;
	}

	public Double getThisYearRedound() {
		return ThisYearRedound;
	}

	public void setThisYearRedound(Double thisYearRedound) {
		ThisYearRedound = thisYearRedound;
	}

	public Double getThreeMonthRedound() {
		return ThreeMonthRedound;
	}

	public void setThreeMonthRedound(Double threeMonthRedound) {
		ThreeMonthRedound = threeMonthRedound;
	}

	public Double getTotalEquity() {
		return TotalEquity;
	}

	public void setTotalEquity(Double totalEquity) {
		TotalEquity = totalEquity;
	}

	public Double getUnitEquity() {
		return UnitEquity;
	}

	public void setUnitEquity(Double unitEquity) {
		UnitEquity = unitEquity;
	}

	public String getZHRate() {
		return ZHRate;
	}

	public void setZHRate(String zHRate) {
		ZHRate = zHRate;
	}


}
