package com.plmt.boommall.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PreOrder implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;

	private String id;

	private String total;

	private String base_total;

	private String balance;
	
	/**
	 * -1 不需实名认证
	 * 0需要
	 */
	private String is_out_country;
	
	/**
	 * -1尚未实名认证
	 * 0 已实名认证
	 */
	private String is_authentication;
	
	private Payment payment;

	private Shipping shipping;

	private Address address;

	private ArrayList<PayMoney> payMoneyList = new ArrayList<PayMoney>();

	private ArrayList<Goods> goodsList = new ArrayList<Goods>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getBase_total() {
		return base_total;
	}

	public void setBase_total(String base_total) {
		this.base_total = base_total;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getIs_out_country() {
		return is_out_country;
	}

	public void setIs_out_country(String is_out_country) {
		this.is_out_country = is_out_country;
	}

	public String getIs_authentication() {
		return is_authentication;
	}

	public void setIs_authentication(String is_authentication) {
		this.is_authentication = is_authentication;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ArrayList<PayMoney> getPayMoneyList() {
		return payMoneyList;
	}

	public void setPayMoneyList(ArrayList<PayMoney> payMoneyList) {
		this.payMoneyList = payMoneyList;
	}

	public ArrayList<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<Goods> goodsList) {
		this.goodsList = goodsList;
	}
}
