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
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class CartLogic {

	public static final int NET_ERROR = 0;

	public static final int CART_ADD_SUC = NET_ERROR + 1;

	public static final int CART_ADD_FAIL = CART_ADD_SUC + 1;

	public static final int CART_ADD_EXCEPTION = CART_ADD_FAIL + 1;

	public static final int CART_LIST_GET_SUC = CART_ADD_EXCEPTION + 1;

	public static final int CART_LIST_GET_FAIL = CART_LIST_GET_SUC + 1;

	public static final int CART_LIST_GET_EXCEPTION = CART_LIST_GET_FAIL + 1;

	public static final int CART_MODIFY_SUC = CART_LIST_GET_EXCEPTION + 1;

	public static final int CART_MODIFY_FAIL = CART_MODIFY_SUC + 1;

	public static final int CART_MODIFY_EXCEPTION = CART_MODIFY_FAIL + 1;

	public static final int CART_DEL_SUC = CART_MODIFY_EXCEPTION + 1;

	public static final int CART_DEL_FAIL = CART_DEL_SUC + 1;

	public static final int CART_DEL_EXCEPTION = CART_DEL_FAIL + 1;

	public static void getList(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.list;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_cart_getList", response.toString());
								parseListData(response, handler);
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

	private static void parseListData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject cartJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				JSONArray goodsArray = cartJsonObject.getJSONArray("items");
				int size = goodsArray.length();
				ArrayList<Goods> tempGoodsList = new ArrayList<Goods>();
				for (int i = 0; i < size; i++) {
					JSONObject goodsJsonObject = goodsArray.getJSONObject(i);
					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							goodsJsonObject, Goods.class);
					goods.setId(goodsJsonObject.getString("pid"));
					goods.setScid(goodsJsonObject.getString("id"));
					goods.setNum(goodsJsonObject.getString("qty"));
					tempGoodsList.add(goods);
				}
				Log.e("xxx_Cartlist", "" + tempGoodsList.size());
				Message message = new Message();
				message.what = CART_LIST_GET_SUC;
				message.obj = tempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CART_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CART_LIST_GET_EXCEPTION);
		}
	}

	public static void add(final Context context, final Handler handler,
			final String id, final String qty) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.add;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("id", URLEncoder.encode(id, "UTF-8"));
			requestJson.put("qty", URLEncoder.encode(qty, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_cart_add", response.toString());
								// parseListData(response, handler);
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

	public static void update(final Context context, final Handler handler,
			final String cartitemsid, final String action, final String qty) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.update;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("cartitemsid",
					URLEncoder.encode(cartitemsid, "UTF-8"));
			requestJson.put("action", URLEncoder.encode("update_qty", "UTF-8"));
			requestJson.put("qty", URLEncoder.encode(qty, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.e("xxx_cart_update", response.toString());
							if (null != response) {
								Log.e("xxx_cart_update", response.toString());
								// parseListData(response, handler);
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

	public static void del(final Context context, final Handler handler,
			final String cartitemsid) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.del;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("cartitemsid",
					URLEncoder.encode(cartitemsid, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_cart_del", response.toString());
								// parseListData(response, handler);
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

}
