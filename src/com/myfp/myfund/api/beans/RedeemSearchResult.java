package com.myfp.myfund.api.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RedeemSearchResult implements Serializable  {
	
	private String status;
	private String fundtype;
	private String fundcode;
	private String fundname;
	private String risklevel;
	private String distributorcode ;
	private String countfundcode;
	private String totalfundfrozenvalue;
	private String totalfundvolbalance;
	private String totalfundmarketvalue;
	private String trdfrozen ;
	private String confirmfrozen;
	private String fundvolbalance;
	private String availablevol;
	private String fundmarketvalue;
	private String floatprofit;
	private String costprice;
	private String tano;
	private String sharetype;
	private String transactionaccountid;
	
	
	public String getTransactionaccountid() {
		return transactionaccountid;
	}
	public void setTransactionaccountid(String transactionaccountid) {
		this.transactionaccountid = transactionaccountid;
	}
	public String getSharetype() {
		return sharetype;
	}
	public void setSharetype(String sharetype) {
		this.sharetype = sharetype;
	}
	public String getTano() {
		return tano;
	}
	public void setTano(String tano) {
		this.tano = tano;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFundtype() {
		return fundtype;
	}
	public void setFundtype(String fundtype) {
		this.fundtype = fundtype;
	}
	public String getFundcode() {
		return fundcode;
	}
	public void setFundcode(String fundcode) {
		this.fundcode = fundcode;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	private String costmoney ;
	private String addincome;
	private String invtp;
	private String last_frozen;
	private String frozen;
	private String trd_frozen;
	private String last_abnmfrozen;
	private String abnmfrozen ;
	private String trd_abnmfrozen   ;
	private String dividendratecustreg ;
	private String availbal_mode1;
	
	
	public String getRisklevel() {
		return risklevel;
	}
	public void setRisklevel(String risklevel) {
		this.risklevel = risklevel;
	}
	public String getDistributorcode() {
		return distributorcode;
	}
	public void setDistributorcode(String distributorcode) {
		this.distributorcode = distributorcode;
	}
	public String getCountfundcode() {
		return countfundcode;
	}
	public void setCountfundcode(String countfundcode) {
		this.countfundcode = countfundcode;
	}
	public String getTotalfundfrozenvalue() {
		return totalfundfrozenvalue;
	}
	public void setTotalfundfrozenvalue(String totalfundfrozenvalue) {
		this.totalfundfrozenvalue = totalfundfrozenvalue;
	}
	public String getTotalfundvolbalance() {
		return totalfundvolbalance;
	}
	public void setTotalfundvolbalance(String totalfundvolbalance) {
		this.totalfundvolbalance = totalfundvolbalance;
	}
	public String getTotalfundmarketvalue() {
		return totalfundmarketvalue;
	}
	public void setTotalfundmarketvalue(String totalfundmarketvalue) {
		this.totalfundmarketvalue = totalfundmarketvalue;
	}
	public String getTrdfrozen() {
		return trdfrozen;
	}
	public void setTrdfrozen(String trdfrozen) {
		this.trdfrozen = trdfrozen;
	}
	public String getConfirmfrozen() {
		return confirmfrozen;
	}
	public void setConfirmfrozen(String confirmfrozen) {
		this.confirmfrozen = confirmfrozen;
	}
	public String getFundvolbalance() {
		return fundvolbalance;
	}
	public void setFundvolbalance(String fundvolbalance) {
		this.fundvolbalance = fundvolbalance;
	}
	public String getAvailablevol() {
		return availablevol;
	}
	public void setAvailablevol(String availablevol) {
		this.availablevol = availablevol;
	}
	public String getFundmarketvalue() {
		return fundmarketvalue;
	}
	public void setFundmarketvalue(String fundmarketvalue) {
		this.fundmarketvalue = fundmarketvalue;
	}
	public String getFloatprofit() {
		return floatprofit;
	}
	public void setFloatprofit(String floatprofit) {
		this.floatprofit = floatprofit;
	}
	public String getCostprice() {
		return costprice;
	}
	public void setCostprice(String costprice) {
		this.costprice = costprice;
	}
	public String getCostmoney() {
		return costmoney;
	}
	public void setCostmoney(String costmoney) {
		this.costmoney = costmoney;
	}
	public String getAddincome() {
		return addincome;
	}
	public void setAddincome(String addincome) {
		this.addincome = addincome;
	}
	public String getInvtp() {
		return invtp;
	}
	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}
	public String getLast_frozen() {
		return last_frozen;
	}
	public void setLast_frozen(String last_frozen) {
		this.last_frozen = last_frozen;
	}
	public String getFrozen() {
		return frozen;
	}
	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}
	public String getTrd_frozen() {
		return trd_frozen;
	}
	public void setTrd_frozen(String trd_frozen) {
		this.trd_frozen = trd_frozen;
	}
	public String getLast_abnmfrozen() {
		return last_abnmfrozen;
	}
	public void setLast_abnmfrozen(String last_abnmfrozen) {
		this.last_abnmfrozen = last_abnmfrozen;
	}
	public String getAbnmfrozen() {
		return abnmfrozen;
	}
	public void setAbnmfrozen(String abnmfrozen) {
		this.abnmfrozen = abnmfrozen;
	}
	public String getTrd_abnmfrozen() {
		return trd_abnmfrozen;
	}
	public void setTrd_abnmfrozen(String trd_abnmfrozen) {
		this.trd_abnmfrozen = trd_abnmfrozen;
	}
	public String getDividendratecustreg() {
		return dividendratecustreg;
	}
	public void setDividendratecustreg(String dividendratecustreg) {
		this.dividendratecustreg = dividendratecustreg;
	}
	public String getAvailbal_mode1() {
		return availbal_mode1;
	}
	public void setAvailbal_mode1(String availbal_mode1) {
		this.availbal_mode1 = availbal_mode1;
	}
	
	

}
