package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeRecommend implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String image;

	private ArrayList<RootName> rootNameList;

	private ArrayList<Goods> goodsList;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ArrayList<RootName> getRootNameList() {
		return rootNameList;
	}

	public void setRootNameList(ArrayList<RootName> rootNameList) {
		this.rootNameList = rootNameList;
	}

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	
	
}
