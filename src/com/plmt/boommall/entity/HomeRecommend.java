package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeRecommend implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;
	
	private String name;
	
	private String image;
	
	private String rootCategoryName;
	
	private ArrayList<RootName> rootNameList;

	private ArrayList<Goods> goodsList;

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getImage() {
		return image;
	}

	public final void setImage(String image) {
		this.image = image;
	}

	public final String getRootCategoryName() {
		return rootCategoryName;
	}

	public final void setRootCategoryName(String rootCategoryName) {
		this.rootCategoryName = rootCategoryName;
	}

	public final ArrayList<RootName> getRootNameList() {
		return rootNameList;
	}

	public final void setRootNameList(ArrayList<RootName> rootNameList) {
		this.rootNameList = rootNameList;
	}

	public final ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public final void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	

	
	
}
