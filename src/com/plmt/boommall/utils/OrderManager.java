package com.plmt.boommall.utils;

import java.util.ArrayList;

import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.OrderOld;

public class OrderManager {

	public static String sCurrentOrderId;
	
	public static String sCurrentCommentOrderId;

	public static ArrayList<OrderOld> sOrderList = new ArrayList<OrderOld>();

	public static OrderOld sCurrentOrder = new OrderOld();
	
	public static  ArrayList<Goods> sCurrentOrderGoodsList = new ArrayList<Goods>();

	public static ArrayList<OrderOld> getsOrderList() {
		return sOrderList;
	}

	public static void setsOrderList(ArrayList<OrderOld> sOrderList) {
		OrderManager.sOrderList = sOrderList;
	}

	public static OrderOld getsCurrentOrder() {
		return sCurrentOrder;
	}

	public static void setsCurrentOrder(OrderOld sCurrentOrder) {
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
