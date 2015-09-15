package com.myfp.myfund.api.beans;

public class PurchaseHistoryResult {
	private String Dtransaction;
	private String Fnetmoney;
	private String Operation;
	private String SName;
	private String Status;
	public String getDtransaction() {
		return Dtransaction;
	}
	public void setDtransaction(String dtransaction) {
		Dtransaction = dtransaction;
	}
	public String getFnetmoney() {
		return Fnetmoney;
	}
	public void setFnetmoney(String fnetmoney) {
		Fnetmoney = fnetmoney;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public String getSName() {
		return SName;
	}
	public void setSName(String sName) {
		SName = sName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
}
