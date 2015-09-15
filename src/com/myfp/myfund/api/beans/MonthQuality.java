package com.myfp.myfund.api.beans;

import java.io.Serializable;

public class MonthQuality implements Serializable{
	
	private static final long serialVersionUID = 7060210544600464481L;
	
	private String BannerLogo,CreateTime,ExpectedEarnings,FundCode,FundName,IsOnSale,Logo,MinInvestment,Month,MonthFlag,ProDetail,ProDetail2,ProType,Slogan,TimeLimit,Year,SignTime,SignTimeEnd,SignTimeStart;

	public String getSignTime() {
		return SignTime;
	}

	public void setSignTime(String signTime) {
		SignTime = signTime;
	}

	public String getSignTimeEnd() {
		return SignTimeEnd;
	}

	public void setSignTimeEnd(String signTimeEnd) {
		SignTimeEnd = signTimeEnd;
	}

	public String getSignTimeStart() {
		return SignTimeStart;
	}

	public void setSignTimeStart(String signTimeStart) {
		SignTimeStart = signTimeStart;
	}

	public String getBannerLogo() {
		return BannerLogo;
	}

	public void setBannerLogo(String bannerLogo) {
		BannerLogo = bannerLogo;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getExpectedEarnings() {
		return ExpectedEarnings;
	}

	public void setExpectedEarnings(String expectedEarnings) {
		ExpectedEarnings = expectedEarnings;
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

	public String getIsOnSale() {
		return IsOnSale;
	}

	public void setIsOnSale(String isOnSale) {
		IsOnSale = isOnSale;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getMinInvestment() {
		return MinInvestment;
	}

	public void setMinInvestment(String minInvestment) {
		MinInvestment = minInvestment;
	}

	public String getMonth() {
		return Month;
	}

	public void setMonth(String month) {
		Month = month;
	}

	public String getMonthFlag() {
		return MonthFlag;
	}

	public void setMonthFlag(String monthFlag) {
		MonthFlag = monthFlag;
	}

	public String getProDetail() {
		return ProDetail;
	}

	public void setProDetail(String proDetail) {
		ProDetail = proDetail;
	}

	public String getProDetail2() {
		return ProDetail2;
	}

	public void setProDetail2(String proDetail2) {
		ProDetail2 = proDetail2;
	}

	public String getProType() {
		return ProType;
	}

	public void setProType(String proType) {
		ProType = proType;
	}

	public String getSlogan() {
		return Slogan;
	}

	public void setSlogan(String slogan) {
		Slogan = slogan;
	}

	public String getTimeLimit() {
		return TimeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		TimeLimit = timeLimit;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}
}
