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
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class AddressLogic {

	public static final int NET_ERROR = 0;

	public static final int ANDRESS_GET_SUC = NET_ERROR + 1;

	public static final int ANDRESS_GET_FAIL = ANDRESS_GET_SUC + 1;

	public static final int ANDRESS_GET_EXCEPTION = ANDRESS_GET_FAIL + 1;

	public static final int ANDRESS_LIST_GET_SUC = ANDRESS_GET_EXCEPTION + 1;

	public static final int ANDRESS_LIST_GET_FAIL = ANDRESS_LIST_GET_SUC + 1;

	public static final int ANDRESS_LIST_GET_EXCEPTION = ANDRESS_LIST_GET_FAIL + 1;

	public static final int ANDRESS_MODIFY_SUC = ANDRESS_LIST_GET_EXCEPTION + 1;

	public static final int ANDRESS_MODIFY_FAIL = ANDRESS_MODIFY_SUC + 1;

	public static final int ANDRESS_MODIFY_EXCEPTION = ANDRESS_MODIFY_FAIL + 1;

	public static final int ANDRESS_DEL_SUC = ANDRESS_MODIFY_EXCEPTION + 1;

	public static final int ANDRESS_DEL_FAIL = ANDRESS_DEL_SUC + 1;

	public static final int ANDRESS_DEL_EXCEPTION = ANDRESS_DEL_FAIL + 1;

	public static final int ANDRESS_DATA_GET_SUC = ANDRESS_DEL_EXCEPTION + 1;

	public static final int ANDRESS_DATA_GET_FAIL = ANDRESS_DATA_GET_SUC + 1;

	public static final int ANDRESS_DATA_GET_EXCEPTION = ANDRESS_DATA_GET_FAIL + 1;
	
	public static final int ANDRESS_SET_SHIPPING_SUC = ANDRESS_DATA_GET_EXCEPTION + 1;

	public static final int ANDRESS_SET_SHIPPING_FAIL = ANDRESS_SET_SHIPPING_SUC + 1;

	public static final int ANDRESS_SET_SHIPPING_EXCEPTION = ANDRESS_SET_SHIPPING_FAIL + 1;


	public static void getList(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.list;
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

				JSONArray addressArray = jsonObject.getJSONArray("addresses");
				int size = addressArray.length();
				ArrayList<Address> addresslist = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					JSONObject addressJsonObject = addressArray
							.getJSONObject(i);
					Address address = (Address) JsonUtils.fromJsonToJava(
							addressJsonObject, Address.class);
					addresslist.add(address);
				}

				Message message = new Message();
				message.what = ANDRESS_LIST_GET_SUC;
				message.obj = addresslist;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ANDRESS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_LIST_GET_EXCEPTION);
		}
	}

	public static void update(final Context context, final Handler handler,
			final String id, final String cn_name, final String cn_province,
			final String cn_city, final String cn_district,
			final String street, final String postcode, final String telephone,
			final String country_id) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.update;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("id", id);
			requestJson.put("cn_name", cn_name);
			requestJson.put("cn_province", cn_province);
			requestJson.put("cn_city", cn_city);
			requestJson.put("cn_district", cn_district);
			requestJson.put("street", street);
			requestJson.put("postcode", postcode);
			requestJson.put("telephone", telephone);
			requestJson.put("country_id", country_id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_address_update", response.toString());
								parseUpdateData(response, handler);
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

	// {"data":{"id":"70"},"result":"0","msg":"","Set-Cookie":"frontend=pnm8jqa1s6vjcf0g3sf60ak597;
	// expires=Wed, 28-Oct-2015 09:32:53 GMT; path=\/; domain=120.55.116.206;
	// httponly"}
	private static void parseUpdateData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(ANDRESS_MODIFY_SUC);
			} else {
				handler.sendEmptyMessage(ANDRESS_MODIFY_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_MODIFY_EXCEPTION);
		}
	}

	public static void del(final Context context, final Handler handler,
			final String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.del;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("id", id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_address_del", response.toString());
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

	// {"data":[],"result":"0","msg":"","Set-Cookie":"frontend=4ao29o9jqds2vm7gmqj9sgbll1;
	// expires=Sun, 25-Oct-2015 07:49:35 GMT; path=\/; domain=120.55.116.206;
	// httponly"}
	private static void parseDelData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(ANDRESS_DEL_SUC);
			} else {
				handler.sendEmptyMessage(ANDRESS_DEL_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_DEL_EXCEPTION);
		}
	}

	public static void getAddressData(final Context context,
			final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.data;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_address_data", response.toString());
								parseAddressDataData(response, handler);
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

	private static void parseAddressDataData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				Message message = new Message();
				message.what = ANDRESS_DATA_GET_SUC;
				message.obj = response.toString();
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ANDRESS_DATA_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_DATA_GET_EXCEPTION);
		}
	}
	
	public static void setShippingAddress(final Context context, final Handler handler,
			final String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.setShippingAddress;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("address_id", id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_setShippingAddress", response.toString());
								parseSetShippingAddressData(response, handler);
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
	
	private static void parseSetShippingAddressData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				Message message = new Message();
				message.what = ANDRESS_SET_SHIPPING_SUC;
				message.obj = response.toString();
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ANDRESS_SET_SHIPPING_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_SET_SHIPPING_EXCEPTION);
		}
	}

}
