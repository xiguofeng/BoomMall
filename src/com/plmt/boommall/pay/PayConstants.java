package com.plmt.boommall.pay;

public class PayConstants {

	/************************************************
	 * 支付方式: cashpay-现金支付 pospay-POS机支付 alipay-支付宝支付 unionpay-银联支付 wxpay-微信支付
	 ************************************************/
	public static final String PAY_WAY_CASHPAY = "cashpay";
	public static final String PAY_WAY_POSPAY = "pospay";
	public static final String PAY_WAY_ALIPAY = "alipay";
	public static final String PAY_WAY_UNIONPAY = "unionpay";
	public static final String PAY_WAY_WXPAY = "wxpay";

	/**************************************************
	 * 支付状态 : 1-未支付 2-已支付 3-申请退款 4-已经退款 5-预支付
	 *************************************************/
	public static final String PAY_STATUS_UNPAID = "1";
	public static final String PAY_STATUS_PAID = "2";
	public static final String PAY_STATUS_APPLY_REFUND = "3";
	public static final String PAY_STATUS_REFUND = "4";
	public static final String PAY_STATUS_PREPAID = "5";

}
