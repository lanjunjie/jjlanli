package com.myfp.myfund.api.beans;


/**
 * 资讯列表结果
 * @author pengchongjia
 *
 */
public class NewsListResult {
	private String AddDate;
	private String Code;
	private String Source;
	private String Title;
	private String Id;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getAddDate() {
		return AddDate;
	}
	public void setAddDate(String addDate) {
		AddDate = addDate;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	@Override
	public String toString() {
		return "NewsListResult [AddDate=" + AddDate + ", Code=" + Code
				+ ", Source=" + Source + ", Title=" + Title + "]";
	}
}
