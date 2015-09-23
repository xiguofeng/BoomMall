package com.plmt.boommall.entity;

public class MenuItem {

	private String id;

	private String name;

	private String title;

	private String imgUrl;
	
	private int localImage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getLocalImage() {
		return localImage;
	}

	public void setLocalImage(int localImage) {
		this.localImage = localImage;
	}

}
