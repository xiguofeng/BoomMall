package com.plmt.boommall.entity;

import java.io.Serializable;

public class UnionpayMerchant implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String tn;

	private String orderId;

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
