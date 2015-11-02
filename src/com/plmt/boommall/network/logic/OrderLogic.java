package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.entity.PayMoney;
import com.plmt.boommall.entity.Payment;
import com.plmt.boommall.entity.PreOrder;
import com.plmt.boommall.entity.Shipping;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.pay.AlipayMerchant;
import com.plmt.boommall.pay.UnionpayMerchant;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_CREATE_SUC = NET_ERROR + 1;

	public static final int ORDER_CREATE_FAIL = ORDER_CREATE_SUC + 1;

	public static final int ORDER_CREATE_EXCEPTION = ORDER_CREATE_FAIL + 1;

	public static final int ORDER_PRE_INFO_GET_SUC = ORDER_CREATE_EXCEPTION + 1;

	public static final int ORDER_PRE_INFO_GET_FAIL = ORDER_PRE_INFO_GET_SUC + 1;

	public static final int ORDER_PRE_INFO_GET_EXCEPTION = ORDER_PRE_INFO_GET_FAIL + 1;

	public static final int ORDER_SET_ADDRESS_SUC = ORDER_PRE_INFO_GET_EXCEPTION + 1;

	public static final int ORDER_SET_ADDRESS_FAIL = ORDER_SET_ADDRESS_SUC + 1;

	public static final int ORDER_SET_ADDRESS_EXCEPTION = ORDER_SET_ADDRESS_FAIL + 1;

	public static final int ORDERLIST_GET_SUC = ORDER_SET_ADDRESS_EXCEPTION + 1;

	public static final int ORDERLIST_GET_FAIL = ORDERLIST_GET_SUC + 1;

	public static final int ORDERLIST_GET_EXCEPTION = ORDERLIST_GET_FAIL + 1;

	public static final int ORDER_CANCEL_SUC = ORDERLIST_GET_EXCEPTION + 1;

	public static final int ORDER_CANCEL_FAIL = ORDER_CANCEL_SUC + 1;

	public static final int ORDER_CANCEL_EXCEPTION = ORDER_CANCEL_FAIL + 1;

	public static final int ORDER_PAY_TYPE_SET_SUC = ORDER_CANCEL_EXCEPTION + 1;

	public static final int ORDER_PAY_TYPE_SET_FAIL = ORDER_PAY_TYPE_SET_SUC + 1;

	public static final int ORDER_PAY_TYPE_SET_EXCEPTION = ORDER_PAY_TYPE_SET_FAIL + 1;

	public static final int ORDER_PRE_PAY_TYPE_SET_SUC = ORDER_PAY_TYPE_SET_EXCEPTION + 1;

	public static final int ORDER_PRE_PAY_TYPE_SET_FAIL = ORDER_PRE_PAY_TYPE_SET_SUC + 1;

	public static final int ORDER_PRE_PAY_TYPE_SET_EXCEPTION = ORDER_PRE_PAY_TYPE_SET_FAIL + 1;

	public static final int ORDER_PAY_RESULT_CHECK_SUC = ORDER_PRE_PAY_TYPE_SET_EXCEPTION + 1;

	public static final int ORDER_PAY_RESULT_CHECK_FAIL = ORDER_PAY_RESULT_CHECK_SUC + 1;

	public static final int ORDER_PAY_RESULT_CHECK_EXCEPTION = ORDER_PAY_RESULT_CHECK_FAIL + 1;

	public static final int ORDER_PAY_UNION_TN_GET_SUC = ORDER_PAY_RESULT_CHECK_EXCEPTION + 1;

	public static final int ORDER_PAY_UNION_TN_GET_FAIL = ORDER_PAY_UNION_TN_GET_SUC + 1;

	public static final int ORDER_PAY_UNION_TN_GET_EXCEPTION = ORDER_PAY_UNION_TN_GET_FAIL + 1;

	public static final int ORDER_PAY_INFO_GET_SUC = ORDER_PAY_UNION_TN_GET_EXCEPTION + 1;

	public static final int ORDER_PAY_INFO_GET_FAIL = ORDER_PAY_INFO_GET_SUC + 1;

	public static final int ORDER_PAY_INFO_GET_EXCEPTION = ORDER_PAY_INFO_GET_FAIL + 1;

	public static void createOrder(final Context context,
			final Handler handler, PreOrder preOrder) {
		JSONObject requestJson = new JSONObject();
		try {
			Address address = preOrder.getAddress();
			Shipping shipping = preOrder.getShipping();
			Payment payment = preOrder.getPayment();
			// URLEncoder.encode(UserInfoManager.getSession(context), "UTF-8")
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("cn_name",
					URLEncoder.encode(address.getUsername(), "UTF-8"));
			requestJson.put("cn_province",
					URLEncoder.encode(address.getCn_province(), "UTF-8"));
			requestJson.put("cn_city",
					URLEncoder.encode(address.getCn_city(), "UTF-8"));
			requestJson.put("cn_district",
					URLEncoder.encode(address.getCn_district(), "UTF-8"));
			requestJson.put("street",
					URLEncoder.encode(address.getContent(), "UTF-8"));
			requestJson.put("postcode",
					URLEncoder.encode(address.getPostCode(), "UTF-8"));
			requestJson.put("telephone",
					URLEncoder.encode(address.getTelephone(), "UTF-8"));
			requestJson.put("country_id", URLEncoder.encode("CN", "UTF-8"));
			requestJson.put("shipping",
					URLEncoder.encode(shipping.getTitle(), "UTF-8"));
			requestJson.put("payment",
					URLEncoder.encode(payment.getTitle(), "UTF-8"));

			String url = RequestUrl.HOST_URL + RequestUrl.order.submitOrder;
			Log.e("xxx_submitOrder_request", requestJson.toString());

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_submitOrder", response.toString());
								parseCreateOrderData(response, handler);
							}

						}

					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private static void parseCreateOrderData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject dataJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				String orderID = dataJsonObject.getString("order_id").trim();
				if (!TextUtils.isEmpty(orderID)) {
					Message message = new Message();
					message.what = ORDER_CREATE_SUC;
					message.obj = orderID;
					handler.sendMessage(message);
				} else {
					handler.sendEmptyMessage(ORDER_CREATE_FAIL);
				}

			} else {
				handler.sendEmptyMessage(ORDER_CREATE_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_CREATE_EXCEPTION);
		}
	}

	public static void getOrderPayInfo(final Context context,
			final Handler handler, final String orderId) {
		JSONObject requestJson = new JSONObject();
		try {
			Log.e("xxx_getOrderPayInfo", "start");
			requestJson.put("order_id", URLEncoder.encode(orderId, "UTF-8"));

			String url = RequestUrl.HOST_PAY_URL
					+ RequestUrl.order.getOrderPayInfo;

			Log.e("xxx_getOrderPayInfo", url);
			Log.e("xxx_getOrderPayInfo_orderId", orderId);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getOrderPayInfo",
										response.toString());
								parseOrderPayInfoData(response, handler);
							}

						}

					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private static void parseOrderPayInfoData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject dataJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				AlipayMerchant alipayMerchant = (AlipayMerchant) JsonUtils
						.fromJsonToJava(dataJsonObject, AlipayMerchant.class);
				Message message = new Message();
				message.what = ORDER_PAY_INFO_GET_SUC;
				message.obj = alipayMerchant;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDER_PAY_INFO_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_PAY_INFO_GET_EXCEPTION);
		}
	}

	public static void getOrderPayInfoByUnion(final Context context,
			final Handler handler, final String orderId) {
		JSONObject requestJson = new JSONObject();
		Log.e("xxx_getOrderPayInfo", "start");
		try {
			requestJson.put("order_id", URLEncoder.encode(orderId, "UTF-8"));
			String url = RequestUrl.HOST_PAY_URL
					+ RequestUrl.order.getOrderPayByUnionInfo;

			Log.e("xxx_getOrderPayInfoByUnion", url);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getOrderPayInfoByUnion_result",
										"result" + response.toString());
								parseOrderPayInfoByUnionData(response, handler);
							}

						}

					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseOrderPayInfoByUnionData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject dataJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				UnionpayMerchant unionpayMerchant = (UnionpayMerchant) JsonUtils
						.fromJsonToJava(dataJsonObject, UnionpayMerchant.class);
				Message message = new Message();
				message.what = ORDER_PAY_UNION_TN_GET_SUC;
				message.obj = unionpayMerchant;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDER_PAY_UNION_TN_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDER_PAY_UNION_TN_GET_EXCEPTION);
		}
	}

	public static void getOrderPreInfo(final Context context,
			final Handler handler) {
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));

			String url = RequestUrl.HOST_URL + RequestUrl.order.cartDetail;

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_cartDetail_str", response.toString());
								parseOrderPreInfoData(response, handler);
							}

						}

					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseOrderPreInfoData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject dataJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				PreOrder preOrder = new PreOrder();

				JSONObject addressJsonObject = dataJsonObject
						.getJSONObject("address");
				Address address = (Address) JsonUtils.fromJsonToJava(
						addressJsonObject, Address.class);

				JSONObject shippingJsonObject = dataJsonObject
						.getJSONObject("shipping");
				Shipping shiping = (Shipping) JsonUtils.fromJsonToJava(
						shippingJsonObject, Shipping.class);

				JSONObject paymentJsonObject = dataJsonObject
						.getJSONObject("payment");
				Payment payment = (Payment) JsonUtils.fromJsonToJava(
						paymentJsonObject, Payment.class);

				JSONArray goodsJsonArray = dataJsonObject.getJSONArray("items");
				ArrayList<Goods> goodsArrayList = new ArrayList<>();
				for (int i = 0; i < goodsJsonArray.length(); i++) {
					JSONObject goodsJsonObject = goodsJsonArray
							.getJSONObject(i);
					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							goodsJsonObject, Goods.class);
					goodsArrayList.add(goods);
				}

				JSONArray payMoneyJsonArray = dataJsonObject
						.getJSONArray("totals");
				ArrayList<PayMoney> payMoneyArrayList = new ArrayList<>();
				for (int j = 0; j < payMoneyJsonArray.length(); j++) {
					JSONObject payMoneyJsonObject = payMoneyJsonArray
							.getJSONObject(j);
					PayMoney payMoney = (PayMoney) JsonUtils.fromJsonToJava(
							payMoneyJsonObject, PayMoney.class);
					payMoneyArrayList.add(payMoney);
				}

				preOrder.setAddress(address);
				preOrder.setShipping(shiping);
				preOrder.setPayment(payment);
				preOrder.setBase_total(dataJsonObject.getString("base_total"));
				preOrder.setTotal(dataJsonObject.getString("total"));
				preOrder.setBalance(dataJsonObject.getString("balance"));
				preOrder.getGoodsList().addAll(goodsArrayList);
				preOrder.getPayMoneyList().addAll(payMoneyArrayList);

				Log.e("xxx_parseOrderPreInfo_suc", "");

				Message message = new Message();
				message.what = ORDER_PRE_INFO_GET_SUC;
				message.obj = preOrder;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ORDER_PRE_INFO_GET_FAIL);
			}
		} catch (JSONException e) {
			Log.e("xxx_parseOrderPreInfo_suc_EXCEPTION", e.getMessage());
			handler.sendEmptyMessage(ORDER_PRE_INFO_GET_EXCEPTION);
		}
	}

	public static void getOrders(final Context context, final Handler handler,
			final String pageNum, final String pageSize,
			final String orderstatus) {
		JSONObject requestJson = new JSONObject();
		try {
			// URLEncoder.encode(UserInfoManager.getSession(context), "UTF-8")
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);
			requestJson.put("orderstatus", orderstatus);

			String url = RequestUrl.HOST_URL + RequestUrl.order.queryOrderList;

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getOrders", response.toString());
								parseOrdersData(response, handler);
							}

						}

					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseOrdersData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				ArrayList<Order> tempOrderList = new ArrayList<Order>();

				JSONObject dataResponse = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				JSONArray orderListArray = dataResponse
						.getJSONArray("order_data");

				HashMap<String, Object> msgMap = new HashMap<String, Object>();

				int size = orderListArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject orderJsonObject = orderListArray
							.getJSONObject(i);
					Order order = (Order) JsonUtils.fromJsonToJava(
							orderJsonObject, Order.class);
					tempOrderList.add(order);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					JSONArray goodsArray = orderJsonObject
							.getJSONArray("items");

					for (int j = 0; j < goodsArray.length(); j++) {
						JSONObject goodsJsonObject = goodsArray
								.getJSONObject(j);
						Goods goods = (Goods) JsonUtils.fromJsonToJava(
								goodsJsonObject, Goods.class);
						tempGoodsList.add(goods);
					}
					ArrayList<Goods> goodsList = new ArrayList<Goods>();
					order.setGoodsList(goodsList);
					order.getGoodsList().addAll(tempGoodsList);
					msgMap.put(order.getIncrement_id(), tempGoodsList);

				}
				msgMap.put(MsgResult.ORDER_TAG, tempOrderList);

				Message message = new Message();
				message.what = ORDERLIST_GET_SUC;
				message.obj = msgMap;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(ORDERLIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ORDERLIST_GET_EXCEPTION);
		}
	}

}
