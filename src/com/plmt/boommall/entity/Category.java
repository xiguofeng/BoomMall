package com.plmt.boommall.entity;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String name;

	private String subimageurl;

	private int localImage;

	private String parentImageurl;

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

	public String getSubimageurl() {
		return subimageurl;
	}

	public void setSubimageurl(String subimageurl) {
		this.subimageurl = subimageurl;
	}

	public int getLocalImage() {
		return localImage;
	}

	public void setLocalImage(int localImage) {
		this.localImage = localImage;
	}

	public String getParentImageurl() {
		return parentImageurl;
	}

	public void setParentImageurl(String parentImageurl) {
		this.parentImageurl = parentImageurl;
	}

}
