package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.UserInfoManager;

public class CartLogic {

	public static final int NET_ERROR = 0;

	public static final int CART_GET_SUC = NET_ERROR + 1;

	public static final int CART_GET_FAIL = CART_GET_SUC + 1;

	public static final int CART_GET_EXCEPTION = CART_GET_FAIL + 1;

	public static final int CART_LIST_GET_SUC = CART_GET_EXCEPTION + 1;

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
								Log.e("xxx_address_getList",
										response.toString());
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
		}
	}

	private static void parseListData(JSONObject response, Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String session = jsonObject.getString("session");

				Message message = new Message();
				message.what = CART_LIST_GET_SUC;
				message.obj = session;
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
			requestJson.put("action", URLEncoder.encode(action, "UTF-8"));
			requestJson.put("qty", URLEncoder.encode(qty, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
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

}
