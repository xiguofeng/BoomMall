package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PreOrder implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String total;

	private String base_total;

	private String balance;

	private Payment payment;

	private Shipping shipping;

	private Address address;

	private ArrayList<PayMoney> payMoneyList = new ArrayList<PayMoney>();

	private ArrayList<Goods> goodsList = new ArrayList<Goods>();

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getTotal() {
		return total;
	}

	public final void setTotal(String total) {
		this.total = total;
	}

	public final String getBase_total() {
		return base_total;
	}

	public final void setBase_total(String base_total) {
		this.base_total = base_total;
	}

	public final String getBalance() {
		return balance;
	}

	public final void setBalance(String balance) {
		this.balance = balance;
	}

	public final Payment getPayment() {
		return payment;
	}

	public final void setPayment(Payment payment) {
		this.payment = payment;
	}

	public final Shipping getShipping() {
		return shipping;
	}

	public final void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public final Address getAddress() {
		return address;
	}

	public final void setAddress(Address address) {
		this.address = address;
	}

	public final ArrayList<PayMoney> getPayMoneyList() {
		return payMoneyList;
	}

	public final void setPayMoneyList(ArrayList<PayMoney> payMoneyList) {
		this.payMoneyList = payMoneyList;
	}

	public final ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public final void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}

}
