package com.plmt.boommall.network.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class CollectionLogic {

	public static final int NET_ERROR = 0;

	public static final int COLLECTION_LIST_GET_SUC = NET_ERROR + 1;

	public static final int COLLECTION_LIST_GET_FAIL = COLLECTION_LIST_GET_SUC + 1;

	public static final int COLLECTION_LIST_GET_EXCEPTION = COLLECTION_LIST_GET_FAIL + 1;

	public static final int COLLECTION_ADD_SUC = COLLECTION_LIST_GET_EXCEPTION + 1;

	public static final int COLLECTION_ADD_FAIL = COLLECTION_ADD_SUC + 1;

	public static final int COLLECTION_ADD_EXCEPTION = COLLECTION_ADD_FAIL + 1;

	public static final int COLLECTION_DEL_SUC = COLLECTION_ADD_EXCEPTION + 1;

	public static final int COLLECTION_DEL_FAIL = COLLECTION_DEL_SUC + 1;

	public static final int COLLECTION_DEL_EXCEPTION = COLLECTION_DEL_FAIL + 1;

	public static void getList(final Context context, final Handler handler,
			final int pageNum, final int pageSize) {

		String url = RequestUrl.HOST_URL + RequestUrl.collection.list;
		JSONObject requestJson = new JSONObject();
		try {

			requestJson.put("session",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_Collection_getList",
										response.toString());
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
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				JSONArray goodsArray = jsonObject.getJSONArray("items");
				int size = goodsArray.length();
				ArrayList<Goods> goodslist = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					JSONObject goodsJsonObject = goodsArray.getJSONObject(i);
					Goods address = (Goods) JsonUtils.fromJsonToJava(
							goodsJsonObject, Goods.class);
					goodslist.add(address);
				}

				Message message = new Message();
				message.what = COLLECTION_LIST_GET_SUC;
				message.obj = goodslist;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(COLLECTION_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(COLLECTION_LIST_GET_EXCEPTION);
		}
	}

	public static void add(final Context context, final Handler handler,
			final String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.collection.add;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("id", id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_collection_add", response.toString());
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
		}
	}

	private static void parseAddData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(COLLECTION_ADD_SUC);
			} else {
				String msg = response.getString("msg");
				Message message = new Message();
				message.what = COLLECTION_ADD_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(COLLECTION_ADD_EXCEPTION);
		}
	}

	public static void del(final Context context, final Handler handler,
			final String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.collection.del;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("id", id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_collection_del", response.toString());
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
		}
	}

	// {"data":[],"result":-1,"msg":"item does not exist","Set-Cookie":"frontend=sbove44n0fcgolruoauvrv89c3;
	// expires=Mon, 26-Oct-2015 08:25:00 GMT; path=\/; domain=120.55.116.206;
	// httponly"}
	private static void parseDelData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(COLLECTION_DEL_SUC);
			} else {
				String msg = response.getString("msg");
				Message message = new Message();
				message.what = COLLECTION_DEL_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(COLLECTION_DEL_EXCEPTION);
		}
	}
}
