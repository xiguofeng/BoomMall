package com.plmt.boommall.utils;

import java.util.ArrayList;

import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.Order;

public class OrderManager {

	public static String sCurrentOrderId;
	
	public static String sCurrentCommentOrderId;

	public static ArrayList<Order> sOrderList = new ArrayList<Order>();

	public static Order sCurrentOrder = new Order();
	
	public static  ArrayList<Goods> sCurrentOrderGoodsList = new ArrayList<Goods>();

	public static ArrayList<Order> getsOrderList() {
		return sOrderList;
	}

	public static void setsOrderList(ArrayList<Order> sOrderList) {
		OrderManager.sOrderList = sOrderList;
	}

	public static Order getsCurrentOrder() {
		return sCurrentOrder;
	}

	public static void setsCurrentOrder(Order sCurrentOrder) {
		OrderManager.sCurrentOrder = sCurrentOrder;
	}

	public static String getsCurrentOrderId() {
		return sCurrentOrderId;
	}

	public static void setsCurrentOrderId(String sCurrentOrderId) {
		OrderManager.sCurrentOrderId = sCurrentOrderId;
	}

	public static ArrayList<Goods> getsCurrentOrderGoodsList() {
		return sCurrentOrderGoodsList;
	}

	public static void setsCurrentOrderGoodsList(ArrayList<Goods> sCurrentOrderGoodsList) {
		OrderManager.sCurrentOrderGoodsList = sCurrentOrderGoodsList;
	}
	
	public static String getsCurrentCommentOrderId() {
		return sCurrentCommentOrderId;
	}

	public static void setsCurrentCommentOrderId(String sCurrentCommentOrderId) {
		OrderManager.sCurrentCommentOrderId = sCurrentCommentOrderId;
	}

}
