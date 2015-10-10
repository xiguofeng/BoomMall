package com.plmt.boommall.entity;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String username;

	private String text;
	
	private String content;

	private String telephone;

	private String postCode;

	private String selected;

	private String cn_province;

	private String cn_city;

	private String cn_district;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getCn_province() {
		return cn_province;
	}

	public void setCn_province(String cn_province) {
		this.cn_province = cn_province;
	}

	public String getCn_city() {
		return cn_city;
	}

	public void setCn_city(String cn_city) {
		this.cn_city = cn_city;
	}

	public String getCn_district() {
		return cn_district;
	}

	public void setCn_district(String cn_district) {
		this.cn_district = cn_district;
	}

	
}
