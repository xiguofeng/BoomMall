package com.plmt.boommall.entity;

import java.io.Serializable;

public class Filter implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;
	
	private String label;
	
	private String value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}	
}
