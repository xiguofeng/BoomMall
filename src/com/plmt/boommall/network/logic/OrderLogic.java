package com.plmt.boommall.network.logic;

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
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.OrderOld;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.OrderManager;
import com.plmt.boommall.utils.UserInfoManager;

public class OrderLogic {

	public static final int NET_ERROR = 0;

	public static final int ORDER_CREATE_SUC = NET_ERROR + 1;

	public static final int ORDER_CREATE_FAIL = ORDER_CREATE_SUC + 1;

	public static final int ORDER_CREATE_EXCEPTION = ORDER_CREATE_FAIL + 1;

	public static final int ORDERLIST_GET_SUC = ORDER_CREATE_EXCEPTION + 1;

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

	public static void createOrder(final Context context,
			final Handler handler, final OrderOld orderOld, final String authCode,
			final ArrayList<Goods> goodsList) {

	}

	private static void parseCreateOrderData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				String orderID = response.getString("orderId").trim();
				if (!TextUtils.isEmpty(orderID)) {
					OrderManager.setsCurrentOrderId(orderID);
					OrderManager.setsCurrentCommentOrderId(orderID);
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

	public static void getOrders(final Context context, final Handler handler,
			final String sessionid, final String pageNum, final String pageSize) {
		JSONObject requestJson = new JSONObject();
		try {
			// URLEncoder.encode(UserInfoManager.getSession(context), "UTF-8")
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);

			Log.e("xxx_getOrders_sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			String url = RequestUrl.HOST_URL + RequestUrl.order.queryOrderList;
			Log.e("xxx_Orders_url", url);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getOrders", response.toString());
								// parseLoginData(response, handler);
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

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				ArrayList<OrderOld> tempOrderList = new ArrayList<OrderOld>();
				JSONArray orderListArray = jsonObject
						.getJSONArray(MsgResult.RESULT_LIST_TAG);

				HashMap<String, Object> msgMap = new HashMap<String, Object>();

				int size = orderListArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject orderJsonObject = orderListArray
							.getJSONObject(i);
					OrderOld orderOld = (OrderOld) JsonUtils.fromJsonToJava(
							orderJsonObject, OrderOld.class);
					tempOrderList.add(orderOld);

					ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
					JSONArray goodsArray = orderJsonObject
							.getJSONArray("items");

					for (int j = 0; j < goodsArray.length(); j++) {
						JSONObject goodsJsonObject = goodsArray
								.getJSONObject(j);
						Goods goods = new Goods();
						goods.setId(goodsJsonObject.getString("productId"));
						goods.setName(goodsJsonObject.getString("productName"));

						goods.setNum(goodsJsonObject.getString("count"));
						tempGoodsList.add(goods);
					}
					msgMap.put(orderOld.getId(), tempGoodsList);

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
