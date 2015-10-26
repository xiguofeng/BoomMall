package com.plmt.boommall.entity;

import java.io.Serializable;

public class Goods implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String name;

	private String countryOfManufacture;

	private String wc_model;

	private String price;

	private String image;

	private String tierPrice;

	private String wc_cap;

	private String finalPrice;

	private String isSaleable;

	private String cn_price;

	private String num;

	private String minSaleQty;
	
	private String maxSaleQty;
	/**
	 * 对应购物车的数量
	 */
	private String qty;
	
	/**
	 * 对应购物车的id
	 */
	private String scid;
	
	private String weight;
	
	private String type;
	
	private Comment comment;

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

	public String getCountryOfManufacture() {
		return countryOfManufacture;
	}

	public void setCountryOfManufacture(String countryOfManufacture) {
		this.countryOfManufacture = countryOfManufacture;
	}

	public String getWc_model() {
		return wc_model;
	}

	public void setWc_model(String wc_model) {
		this.wc_model = wc_model;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTierPrice() {
		return tierPrice;
	}

	public void setTierPrice(String tierPrice) {
		this.tierPrice = tierPrice;
	}

	public String getWc_cap() {
		return wc_cap;
	}

	public void setWc_cap(String wc_cap) {
		this.wc_cap = wc_cap;
	}

	public String getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getIsSaleable() {
		return isSaleable;
	}

	public void setIsSaleable(String isSaleable) {
		this.isSaleable = isSaleable;
	}

	public String getCn_price() {
		return cn_price;
	}

	public void setCn_price(String cn_price) {
		this.cn_price = cn_price;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getMinSaleQty() {
		return minSaleQty;
	}

	public void setMinSaleQty(String minSaleQty) {
		this.minSaleQty = minSaleQty;
	}

	public String getMaxSaleQty() {
		return maxSaleQty;
	}

	public void setMaxSaleQty(String maxSaleQty) {
		this.maxSaleQty = maxSaleQty;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getScid() {
		return scid;
	}

	public void setScid(String scid) {
		this.scid = scid;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	
}
