package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

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

	/**
	 * 是否有货
	 */
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

	private String description;

	/**
	 * 评论数
	 */
	private String review_total;

	/**
	 * 好评率
	 */
	private String rating_avg;
	
	/**
	 * 所属商户
	 */
	private String manufacturer;
	
	private Comment comment;

	private ArrayList<String> litterImage;

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getCountryOfManufacture() {
		return countryOfManufacture;
	}

	public final void setCountryOfManufacture(String countryOfManufacture) {
		this.countryOfManufacture = countryOfManufacture;
	}

	public final String getWc_model() {
		return wc_model;
	}

	public final void setWc_model(String wc_model) {
		this.wc_model = wc_model;
	}

	public final String getPrice() {
		return price;
	}

	public final void setPrice(String price) {
		this.price = price;
	}

	public final String getImage() {
		return image;
	}

	public final void setImage(String image) {
		this.image = image;
	}

	public final String getTierPrice() {
		return tierPrice;
	}

	public final void setTierPrice(String tierPrice) {
		this.tierPrice = tierPrice;
	}

	public final String getWc_cap() {
		return wc_cap;
	}

	public final void setWc_cap(String wc_cap) {
		this.wc_cap = wc_cap;
	}

	public final String getFinalPrice() {
		return finalPrice;
	}

	public final void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	public final String getIsSaleable() {
		return isSaleable;
	}

	public final void setIsSaleable(String isSaleable) {
		this.isSaleable = isSaleable;
	}

	public final String getCn_price() {
		return cn_price;
	}

	public final void setCn_price(String cn_price) {
		this.cn_price = cn_price;
	}

	public final String getNum() {
		return num;
	}

	public final void setNum(String num) {
		this.num = num;
	}

	public final String getMinSaleQty() {
		return minSaleQty;
	}

	public final void setMinSaleQty(String minSaleQty) {
		this.minSaleQty = minSaleQty;
	}

	public final String getMaxSaleQty() {
		return maxSaleQty;
	}

	public final void setMaxSaleQty(String maxSaleQty) {
		this.maxSaleQty = maxSaleQty;
	}

	public final String getQty() {
		return qty;
	}

	public final void setQty(String qty) {
		this.qty = qty;
	}

	public final String getScid() {
		return scid;
	}

	public final void setScid(String scid) {
		this.scid = scid;
	}

	public final String getWeight() {
		return weight;
	}

	public final void setWeight(String weight) {
		this.weight = weight;
	}

	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getReview_total() {
		return review_total;
	}

	public final void setReview_total(String review_total) {
		this.review_total = review_total;
	}

	public final String getRating_avg() {
		return rating_avg;
	}

	public final void setRating_avg(String rating_avg) {
		this.rating_avg = rating_avg;
	}

	public final String getManufacturer() {
		return manufacturer;
	}

	public final void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public final Comment getComment() {
		return comment;
	}

	public final void setComment(Comment comment) {
		this.comment = comment;
	}

	public final ArrayList<String> getLitterImage() {
		return litterImage;
	}

	public final void setLitterImage(ArrayList<String> litterImage) {
		this.litterImage = litterImage;
	}

}
