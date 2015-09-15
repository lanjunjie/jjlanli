package com.myfp.myfund.api.beans;

import java.util.List;

public class NewRiskSearchResult {
	private String resultcontent;
	private String resultpoint;
	private String questioncode;
	private String questionname;
	private List<String> listresult;
	private List<String> listpoint;
	
	
	
	public List<String> getListpoint() {
		return listpoint;
	}
	public void setListpoint(List<String> listpoint) {
		this.listpoint = listpoint;
	}
	public String getResultcontent() {
		return resultcontent;
	}
	public void setResultcontent(String resultcontent) {
		this.resultcontent = resultcontent;
	}
	public String getResultpoint() {
		return resultpoint;
	}
	public void setResultpoint(String resultpoint) {
		this.resultpoint = resultpoint;
	}
	public String getQuestioncode() {
		return questioncode;
	}
	public void setQuestioncode(String questioncode) {
		this.questioncode = questioncode;
	}
	public String getQuestionname() {
		return questionname;
	}
	public void setQuestionname(String questionname) {
		this.questionname = questionname;
	}
	public List<String> getListresult() {
		return listresult;
	}
	public void setListresult(List<String> listresult) {
		this.listresult = listresult;
	}
	
	

}
