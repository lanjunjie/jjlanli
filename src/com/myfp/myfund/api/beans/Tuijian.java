package com.myfp.myfund.api.beans;

public class Tuijian {
	private String AddDate,BannerDetail,BannerId,BannerPic,BannerURL,Flag,Title;

	public String getAddDate() {
		return AddDate;
	}

	public void setAddDate(String addDate) {
		AddDate = addDate;
	}

	public String getBannerDetail() {
		return BannerDetail;
	}

	public void setBannerDetail(String bannerDetail) {
		BannerDetail = bannerDetail;
	}

	public String getBannerId() {
		return BannerId;
	}

	public void setBannerId(String bannerId) {
		BannerId = bannerId;
	}

	public String getBannerPic() {
		return BannerPic;
	}

	public void setBannerPic(String bannerPic) {
		BannerPic = bannerPic;
	}

	public String getBannerURL() {
		return BannerURL;
	}

	public void setBannerURL(String bannerURL) {
		BannerURL = bannerURL;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	@Override
	public String toString() {
		return "Tuijian [AddDate=" + AddDate + ", BannerDetail=" + BannerDetail
				+ ", BannerId=" + BannerId + ", BannerPic=" + BannerPic
				+ ", BannerURL=" + BannerURL + ", Flag=" + Flag + ", Title="
				+ Title + "]";
	}
	
}
