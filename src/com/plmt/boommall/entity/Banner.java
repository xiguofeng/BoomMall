package com.plmt.boommall.entity;

import java.io.Serializable;

public class Banner implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String title;

	private String bannerimage;
	
	private String imgurl;

	private String link;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBannerimage() {
		return bannerimage;
	}

	public void setBannerimage(String bannerimage) {
		this.bannerimage = bannerimage;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


	
}
