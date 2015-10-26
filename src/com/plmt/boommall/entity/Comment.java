package com.plmt.boommall.entity;

import java.io.Serializable;

public class Comment implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String review_id;

	private String nickname;

	private String zl_value;

	private String jg_value;

	private String kd_value;

	private String created_at;

	private String customer_id;

	private String detail;

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getZl_value() {
		return zl_value;
	}

	public void setZl_value(String zl_value) {
		this.zl_value = zl_value;
	}

	public String getJg_value() {
		return jg_value;
	}

	public void setJg_value(String jg_value) {
		this.jg_value = jg_value;
	}

	public String getKd_value() {
		return kd_value;
	}

	public void setKd_value(String kd_value) {
		this.kd_value = kd_value;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
