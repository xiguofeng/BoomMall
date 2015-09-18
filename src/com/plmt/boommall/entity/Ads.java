package com.plmt.boommall.entity;

import java.io.Serializable;

public class Ads implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String imgUrl;

	private String jumpToUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getJumpToUrl() {
		return jumpToUrl;
	}

	public void setJumpToUrl(String jumpToUrl) {
		this.jumpToUrl = jumpToUrl;
	}
	
	

	

}
