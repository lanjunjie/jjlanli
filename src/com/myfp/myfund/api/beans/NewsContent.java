package com.myfp.myfund.api.beans;

/**
 * 资讯详情
 * @author pengchongjia
 *
 */
public class NewsContent {
	private String AddDate;
	private String Content;
	private String Source;
	private String Title,ContentURL,Id,NodeID;
	public String getContentURL() {
		return ContentURL;
	}
	public void setContentURL(String contentURL) {
		ContentURL = contentURL;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getNodeID() {
		return NodeID;
	}
	public void setNodeID(String nodeID) {
		NodeID = nodeID;
	}
	public String getAddDate() {
		return AddDate;
	}
	public void setAddDate(String addDate) {
		AddDate = addDate;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
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
}
