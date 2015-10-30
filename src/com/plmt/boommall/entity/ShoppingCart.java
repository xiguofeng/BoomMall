package com.plmt.boommall.entity;

import java.util.ArrayList;

public class ShoppingCart {

	private String id;

	private boolean ischeck;

	private String txt;

	private ArrayList<Goods> goodsList;

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

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}
}
