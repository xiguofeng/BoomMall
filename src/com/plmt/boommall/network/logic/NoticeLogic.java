package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.Comment;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.HomeRecommend;
import com.plmt.boommall.entity.RootName;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.utils.JsonObjectRequestUtf;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.network.volley.toolbox.JsonObjectRequest;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NoticeLogic {

	public static final int NET_ERROR = 0;

	public static final int PRICE_REDUCE_SET_SUC = NET_ERROR + 1;

	public static final int PRICE_REDUCE_SET_FAIL = PRICE_REDUCE_SET_SUC + 1;

	public static final int PRICE_REDUCE_SET_EXCEPTION = PRICE_REDUCE_SET_FAIL + 1;

	public static final int AOG_SET_SUC = PRICE_REDUCE_SET_EXCEPTION + 1;

	public static final int AOG_SET_FAIL = AOG_SET_SUC + 1;

	public static final int AOG_SET_EXCEPTION = AOG_SET_FAIL + 1;	

	public static void setPriceReduce(final Context context, final Handler handler, String sku,
			String price, String phone) {

		String url = RequestUrl.HOST_URL + RequestUrl.notice.priceReduce;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("alert_product_sku", URLEncoder.encode(sku, "UTF-8"));
			requestJson.put("alert_price", URLEncoder.encode(price, "UTF-8"));
			requestJson.put("alert_phone", URLEncoder.encode(phone, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_setPriceReduce", response.toString());
						parseSetPriceReduceData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void parseSetPriceReduceData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(PRICE_REDUCE_SET_SUC);
			} else {
				String msg=result.getString("msg");
				Message message = new Message();
				message.what = PRICE_REDUCE_SET_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(PRICE_REDUCE_SET_EXCEPTION);
		}
	}

	public static void setAOG(final Context context, final Handler handler, String sku, String phone) {

		String url = RequestUrl.HOST_URL + RequestUrl.notice.aog;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("alert_product_sku", URLEncoder.encode(sku, "UTF-8"));
			requestJson.put("alert_telPhone", URLEncoder.encode(phone, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_setAog", response.toString());
						parseAOGData(response, handler);
					}
				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseAOGData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(AOG_SET_SUC);
			} else {
				String msg=result.getString("msg");
				Message message = new Message();
				message.what = AOG_SET_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(AOG_SET_EXCEPTION);
		}
	}
}
