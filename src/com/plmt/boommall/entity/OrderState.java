package com.plmt.boommall.entity;

public class OrderState {

	public static String[] state = new String[] { "已下单", "已抢单", "已配送", "已收货 ", "已验货", "已评论","已取消 ", "已删除", "已关闭", "已超时"};

	/************************************************
	 * 订单状态 : 1-下单 2-抢单 3-配送 4-收货 5-验货 6-评论 7-取消 8-删除 9-关闭
	 ************************************************/
	public static final String ORDER_STATUS_ORDERED = "1";
	public static final String ORDER_STATUS_GRABBED = "2";
	public static final String ORDER_STATUS_DELIVERY = "3";
	public static final String ORDER_STATUS_CONFIRMED = "4";
	public static final String ORDER_STATUS_CHECKED = "5";
	public static final String ORDER_STATUS_COMMENTED = "6";
	public static final String ORDER_STATUS_CANCELED = "7";
	public static final String ORDER_STATUS_DELETED = "8";
	public static final String ORDER_STATUS_CLOSED = "9";

}
