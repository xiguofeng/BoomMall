package com.plmt.boommall.network.logic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class PropertyLogic {

	public static final int NET_ERROR = 0;

	public static final int INTEGRAL_GET_SUC = NET_ERROR + 1;

	public static final int INTEGRAL_GET_FAIL = INTEGRAL_GET_SUC + 1;

	public static final int INTEGRAL_GET_EXCEPTION = INTEGRAL_GET_FAIL + 1;

	public static final int BALANCE_GET_SUC = INTEGRAL_GET_EXCEPTION + 1;

	public static final int BALANCE_GET_FAIL = BALANCE_GET_SUC + 1;

	public static final int BALANCE_GET_EXCEPTION = BALANCE_GET_FAIL + 1;

	public static final int GIFTCARD_GET_SUC = BALANCE_GET_EXCEPTION + 1;

	public static final int GIFTCARD_GET_FAIL = GIFTCARD_GET_SUC + 1;

	public static final int GIFTCARD_GET_EXCEPTION = GIFTCARD_GET_FAIL + 1;

	public static void queryIntegral(final Context context,
			final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.property.queryIntegral;
		JSONObject requestJson = new JSONObject();
		CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
				requestJson, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != response) {
							parseQueryIntegralData(response, handler);
						}

					}
				}, null);
		cookieRequest.setCookie("frontend="
				+ UserInfoManager.getSession(context));

		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();
	}

	// {"msg":"","result":"0","data":"120"}
	private static void parseQueryIntegralData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				String integralNum = response.getString("data");
				Message message = new Message();
				message.what = INTEGRAL_GET_SUC;
				message.obj = integralNum;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(INTEGRAL_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(INTEGRAL_GET_EXCEPTION);
		}
	}

	public static void queryBalance(final Context context, final Handler handler) {
		JSONObject requestJson = new JSONObject();
		String url = RequestUrl.HOST_URL
				+ RequestUrl.property.queryRemainingMoney;

		CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
				requestJson, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != response) {
							parseQueryBalanceData(response, handler);
						}

					}

				}, null);
		cookieRequest.setCookie("frontend="
				+ UserInfoManager.getSession(context));

		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();

	}

	// {"msg":"","result":"0","data":"9.01"}
	private static void parseQueryBalanceData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				String integralNum = response.getString("data");
				Message message = new Message();
				message.what = BALANCE_GET_SUC;
				message.obj = integralNum;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(BALANCE_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(BALANCE_GET_FAIL);
		}
	}

	public static void queryGiftCard(final Context context,
			final Handler handler, final String giftcard) {
		try {
			String url = RequestUrl.HOST_URL
					+ RequestUrl.property.queryGiftCard;
			JSONObject requestJson = new JSONObject();
			requestJson.put("giftcard", giftcard);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								parseQueryGiftCardData(response, handler);
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

	// {"msg":"","result":"0","data":{"status":"可用","state":"1","balance":"0.0900"}}
	private static void parseQueryGiftCardData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String balance = jsonObject.getString("balance");
				Message message = new Message();
				message.what = GIFTCARD_GET_SUC;
				message.obj = balance;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(GIFTCARD_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GIFTCARD_GET_EXCEPTION);
		}
	}

}
