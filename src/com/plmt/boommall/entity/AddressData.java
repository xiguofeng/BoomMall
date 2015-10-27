package com.plmt.boommall.entity;

import java.io.Serializable;

public class AddressData implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String name;

	private String code;

	private String zip_code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	
}
