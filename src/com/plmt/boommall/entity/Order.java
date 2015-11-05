package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String total;

	private String status;

	private String payUrl;

	private String statusLabel;

	private String created_time;

	private String item_count;

	private String increment_id;

	private String shipping_amount;

	private ArrayList<Goods> goodsList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getItem_count() {
		return item_count;
	}

	public void setItem_count(String item_count) {
		this.item_count = item_count;
	}

	public String getIncrement_id() {
		return increment_id;
	}

	public void setIncrement_id(String increment_id) {
		this.increment_id = increment_id;
	}

	public String getShipping_amount() {
		return shipping_amount;
	}

	public void setShipping_amount(String shipping_amount) {
		this.shipping_amount = shipping_amount;
	}

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

}
