package com.myfp.myfund.api.beans;

public class MessageList {
	private String Title,ShortFlag,CreateTime,Content,AddDate,IsReaded,Id;

	public String getIsReaded() {
		return IsReaded;
	}

	public void setIsReaded(String isReaded) {
		IsReaded = isReaded;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getShortFlag() {
		return ShortFlag;
	}

	public void setShortFlag(String shortFlag) {
		ShortFlag = shortFlag;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getAddDate() {
		return AddDate;
	}

	public void setAddDate(String addDate) {
		AddDate = addDate;
	}
}
