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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMinSaleQty() {
		return minSaleQty;
	}

	public void setMinSaleQty(String minSaleQty) {
		this.minSaleQty = minSaleQty;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getMaxSaleQty() {
		return maxSaleQty;
	}

	public void setMaxSaleQty(String maxSaleQty) {
		this.maxSaleQty = maxSaleQty;
	}

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

}
