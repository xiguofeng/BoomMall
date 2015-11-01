package com.plmt.boommall.entity;

import java.util.ArrayList;

public class ShoppingCart {

	private String id;

	private boolean ischeck;

	private String txt;

	private String price;

	private String manufacturer;

	private String name;

	private String minSaleQty;

	private String image;

	private String qty;

	private String pid;

	private String num;

	private String finalPrice;

	private String maxSaleQty;

	private ArrayList<Goods> goodsList = new ArrayList<>();

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final boolean isIscheck() {
		return ischeck;
	}

	public final void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public final String getTxt() {
		return txt;
	}

	public final void setTxt(String txt) {
		this.txt = txt;
	}

	public final String getPrice() {
		return price;
	}

	public final void setPrice(String price) {
		this.price = price;
	}

	public final String getManufacturer() {
		return manufacturer;
	}

	public final void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getMinSaleQty() {
		return minSaleQty;
	}

	public final void setMinSaleQty(String minSaleQty) {
		this.minSaleQty = minSaleQty;
	}

	public final String getImage() {
		return image;
	}

	public final void setImage(String image) {
		this.image = image;
	}

	public final String getQty() {
		return qty;
	}

	public final void setQty(String qty) {
		this.qty = qty;
	}

	public final String getPid() {
		return pid;
	}

	public final void setPid(String pid) {
		this.pid = pid;
	}

	public final String getNum() {
		return num;
	}

	public final void setNum(String num) {
		this.num = num;
	}

	public final String getFinalPrice() {
		return finalPrice;
	}

	public final void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	public final String getMaxSaleQty() {
		return maxSaleQty;
	}

	public final void setMaxSaleQty(String maxSaleQty) {
		this.maxSaleQty = maxSaleQty;
	}

	public final ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public final void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

}
