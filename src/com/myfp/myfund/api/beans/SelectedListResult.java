package com.myfp.myfund.api.beans;


/**
 * 单品基金列表
 * @author pengchongjia
 *
 */
public class SelectedListResult {

	private String BannerLogo;
	private String CreateTime;
	private String ExpectedEarnings;
	private String FundCode;
	private String FundName;
	private String IsOnSale;
	private String Logo;
	private String MinInvestment;
	private String OneYearRedound;
	private String ProDetail;
	private String ProType;
	private String Rate;
	private String Slogan;
	private String TimeLimit;
	private String ZhRate;

	public String getOneYearRedound() {
		return OneYearRedound;
	}

	public void setOneYearRedound(String oneYearRedound) {
		OneYearRedound = oneYearRedound;
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

	public String getProDetail() {
		return ProDetail;
	}

	public void setProDetail(String proDetail) {
		ProDetail = proDetail;
	}

	public String getProType() {
		return ProType;
	}

	public void setProType(String proType) {
		ProType = proType;
	}

	public String getRate() {
		return Rate;
	}

	public void setRate(String rate) {
		Rate = rate;
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

	public String getZhRate() {
		return ZhRate;
	}

	public void setZhRate(String zhRate) {
		ZhRate = zhRate;
	}

	@Override
	public String toString() {
		return "SelectedListResult [BannerLogo=" + BannerLogo + ", CreateTime="
				+ CreateTime + ", ExpectedEarnings=" + ExpectedEarnings
				+ ", FundCode=" + FundCode + ", FundName=" + FundName
				+ ", IsOnSale=" + IsOnSale + ", Logo=" + Logo
				+ ", MinInvestment=" + MinInvestment + ", ProDetail="
				+ ProDetail + ", ProType=" + ProType + ", Rate=" + Rate
				+ ", Slogan=" + Slogan + ", TimeLimit=" + TimeLimit
				+ ", ZhRate=" + ZhRate + "]";
	}
}
