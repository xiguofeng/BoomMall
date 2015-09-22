package com.plmt.boommall.entity;

import java.io.Serializable;

public class OrderGoods implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String type;

	private String image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
