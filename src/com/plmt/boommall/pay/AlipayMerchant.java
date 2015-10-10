package com.plmt.boommall.pay;

import java.io.Serializable;

public class AlipayMerchant implements Serializable {

	private static final long serialVersionUID = 8912365559481657349L;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 私钥
	 */
	private String privateKey;
	
	/**
	 * 商户ID
	 */
	private String partnerId;
	
	/**
	 * 签约卖家支付宝账号
	 */
	private String sellerAccount;
	
	 /**
     * 服务器异步通知页面路径
     */
	private String notifyUrl;
	
	/**
	 *  商户网站唯一订单号
	 */
	private String outTradeNo;
	
	/**
	 * 商品金额
	 */
	private String amount;

	/**
	 * 商品详情
	 */
	private String productDescription;
   
	/**
	 * 商品名称
	 */
	private String productName;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	
}
