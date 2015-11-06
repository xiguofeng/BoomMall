package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
import com.plmt.boommall.entity.Shipping;
import com.plmt.boommall.entity.ShoppingCart;
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

	public static final int CART_ADD_SESSION_TIME_OUT_FAIL = CART_ADD_FAIL + 1;

	public static final int CART_ADD_EXCEPTION = CART_ADD_SESSION_TIME_OUT_FAIL + 1;

	public static final int CART_LIST_GET_SUC = CART_ADD_EXCEPTION + 1;

	public static final int CART_LIST_GET_FAIL = CART_LIST_GET_SUC + 1;

	public static final int CART_LIST_GET_SESSION_TIME_OUT_FAIL = CART_LIST_GET_FAIL + 1;

	public static final int CART_LIST_GET_EXCEPTION = CART_LIST_GET_SESSION_TIME_OUT_FAIL + 1;

	public static final int CART_UPDATE_SUC = CART_LIST_GET_EXCEPTION + 1;

	public static final int CART_UPDATE_FAIL = CART_UPDATE_SUC + 1;

	public static final int CART_UPDATE_SESSION_TIME_OUT_FAIL = CART_UPDATE_FAIL + 1;

	public static final int CART_UPDATE_EXCEPTION = CART_UPDATE_SESSION_TIME_OUT_FAIL + 1;

	public static final int CART_DEL_SUC = CART_UPDATE_EXCEPTION + 1;

	public static final int CART_DEL_FAIL = CART_DEL_SUC + 1;

	public static final int CART_DEL_SESSION_TIME_OUT_FAIL = CART_DEL_FAIL + 1;

	public static final int CART_DEL_EXCEPTION = CART_DEL_SESSION_TIME_OUT_FAIL + 1;

	public static final int CART_SET_SELECT_SUC = CART_DEL_EXCEPTION + 1;

	public static final int CART_SET_SELECT_FAIL = CART_SET_SELECT_SUC + 1;

	public static final int CART_SET_SELECT_SESSION_TIME_OUT_FAIL = CART_SET_SELECT_FAIL + 1;

	public static final int CART_SET_SELECT_EXCEPTION = CART_SET_SELECT_SESSION_TIME_OUT_FAIL + 1;

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
								Log.e("xxx_cartlist", response.toString());
								// parseListData(response, handler);
								parseListNewData(response, handler);
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
					if (TextUtils.isEmpty(goods.getManufacturer())
							|| goods.getManufacturer().equals("null")) {
						goods.setManufacturer("0");
					}
					tempGoodsList.add(goods);
				}
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

	private static void parseListNewData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject cartJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				JSONArray shoppingCartArray = cartJsonObject
						.getJSONArray("items");
				int size = shoppingCartArray.length();
				ArrayList<ShoppingCart> tempGoodsList = new ArrayList<ShoppingCart>();
				for (int i = 0; i < size; i++) {
					JSONObject goodsJsonObject = shoppingCartArray
							.getJSONObject(i);
					ShoppingCart shoppingCart = (ShoppingCart) JsonUtils
							.fromJsonToJava(goodsJsonObject, ShoppingCart.class);
					if (TextUtils.isEmpty(shoppingCart.getManufacturer())
							|| shoppingCart.getManufacturer().equals("null")) {
						shoppingCart.setManufacturer("0");
					}
					shoppingCart.setNum(shoppingCart.getQty());
					tempGoodsList.add(shoppingCart);
				}
				Message message = new Message();
				message.what = CART_LIST_GET_SUC;
				message.obj = tempGoodsList;
				handler.sendMessage(message);

			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(CART_LIST_GET_SESSION_TIME_OUT_FAIL);
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
								parseAddData(response, handler);
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

	private static void parseAddData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(CART_ADD_SUC);
			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(CART_ADD_SESSION_TIME_OUT_FAIL);
			} else {
				String msgStr = response.getString("msg").trim();
				Message message = new Message();
				message.what = CART_ADD_FAIL;
				message.obj = msgStr;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CART_ADD_EXCEPTION);
		}
	}

	public static void update(final Context context, final Handler handler,
			final String cartitemsid, final String action, final String qty) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.update;
		Log.e("xxx_cart_update", url);
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
							Log.e("xxx_cart_update_response",
									response.toString());
							if (null != response) {
								Log.e("xxx_cart_update_response",
										response.toString());
								parseUpdateData(response, handler);
							}

						}
					}, null);
			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
			Log.e("xxx_cart_update", "start()");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void parseUpdateData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(CART_UPDATE_SUC);
			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(CART_UPDATE_SESSION_TIME_OUT_FAIL);
			} else {
				handler.sendEmptyMessage(CART_UPDATE_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CART_UPDATE_EXCEPTION);
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
								parseDelData(response, handler);
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

	private static void parseDelData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(CART_DEL_SUC);
			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(CART_DEL_SESSION_TIME_OUT_FAIL);
			} else {
				handler.sendEmptyMessage(CART_DEL_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CART_DEL_EXCEPTION);
		}
	}

	public static void setSelectItem(final Context context,
			final Handler handler, final String selectitems) {

		String url = RequestUrl.HOST_URL + RequestUrl.cart.setSelectItem;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("selectitems", selectitems);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_cart_SelectItem",
										response.toString());
								parseSetSelectItemData(response, handler);
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

	private static void parseSetSelectItemData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(CART_SET_SELECT_SUC);
			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(CART_SET_SELECT_SESSION_TIME_OUT_FAIL);
			} else {
				String msgStr = response.getString("msg").trim();
				Message message = new Message();
				message.what = CART_SET_SELECT_FAIL;
				message.obj = msgStr;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CART_SET_SELECT_EXCEPTION);
		}
	}

}
