package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.NotifyGoodsInfo;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NoticeLogic {

	public static final int NET_ERROR = 0;

	public static final int PRICE_REDUCE_SET_SUC = NET_ERROR + 1;

	public static final int PRICE_REDUCE_SET_FAIL = PRICE_REDUCE_SET_SUC + 1;

	public static final int PRICE_REDUCE_SET_EXCEPTION = PRICE_REDUCE_SET_FAIL + 1;

	public static final int PRICE_REDUCE_GET_SUC = PRICE_REDUCE_SET_EXCEPTION + 1;

	public static final int PRICE_REDUCE_GET_FAIL = PRICE_REDUCE_GET_SUC + 1;

	public static final int PRICE_REDUCE_GET_EXCEPTION = PRICE_REDUCE_GET_FAIL + 1;

	public static final int PRICE_REDUCE_CLOSE_SUC = PRICE_REDUCE_GET_EXCEPTION + 1;

	public static final int PRICE_REDUCE_CLOSE_FAIL = PRICE_REDUCE_CLOSE_SUC + 1;

	public static final int PRICE_REDUCE_CLOSE_EXCEPTION = PRICE_REDUCE_CLOSE_FAIL + 1;

	public static final int AOG_SET_SUC = PRICE_REDUCE_CLOSE_EXCEPTION + 1;

	public static final int AOG_SET_FAIL = AOG_SET_SUC + 1;

	public static final int AOG_SET_EXCEPTION = AOG_SET_FAIL + 1;

	public static final int AOG_GET_SUC = AOG_SET_EXCEPTION + 1;

	public static final int AOG_GET_FAIL = AOG_GET_SUC + 1;

	public static final int AOG_GET_EXCEPTION = AOG_GET_FAIL + 1;

	public static final int AOG_CLOSE_SUC = AOG_GET_EXCEPTION + 1;

	public static final int AOG_CLOSE_FAIL = AOG_CLOSE_SUC + 1;

	public static final int AOG_CLOSE_EXCEPTION = AOG_CLOSE_FAIL + 1;

	public static void setPriceReduce(final Context context, final Handler handler, String sku, String price,
			String phone) {

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
				String msg = result.getString("msg");
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
				String msg = result.getString("msg");
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

	public static void getPriceReduce(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.notice.getPriceReduce;
		JSONObject requestJson = new JSONObject();
		CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (null != response) {
					Log.e("xxx_getPriceReduce", response.toString());
					parseGetPriceReduceData(response, handler);
				}
			}
		}, null);
		cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();
	}

	private static void parseGetPriceReduceData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				JSONArray jsonArray = result.getJSONArray(MsgResult.RESULT_DATA_TAG);
				ArrayList<NotifyGoodsInfo> mTempList = new ArrayList<NotifyGoodsInfo>();
				if (null != jsonArray) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject notifyGoodsJO = jsonArray.getJSONObject(i);
						NotifyGoodsInfo notifyGoods = (NotifyGoodsInfo) JsonUtils.fromJsonToJava(notifyGoodsJO,
								NotifyGoodsInfo.class);
						mTempList.add(notifyGoods);
					}
				}
				Message message = new Message();
				message.what = PRICE_REDUCE_GET_SUC;
				message.obj = mTempList;
				handler.sendMessage(message);
			} else {
				String msg = result.getString("msg");
				Message message = new Message();
				message.what = PRICE_REDUCE_GET_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(PRICE_REDUCE_GET_EXCEPTION);
		}
	}

	public static void getAOG(final Context context, final Handler handler, String sku, String phone)
			throws JSONException {

		String url = RequestUrl.HOST_URL + RequestUrl.notice.aog;
		JSONObject requestJson = new JSONObject();
		CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (null != response) {
					Log.e("xxx_getAog", response.toString());
				    parseGetAOGData(response, handler);
				}
			}

		}, null);
		cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();
	}

	private static void parseGetAOGData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				JSONArray jsonArray = result.getJSONArray(MsgResult.RESULT_DATA_TAG);
				ArrayList<NotifyGoodsInfo> mTempList = new ArrayList<NotifyGoodsInfo>();
				if (null != jsonArray) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject notifyGoodsJO = jsonArray.getJSONObject(i);
						NotifyGoodsInfo notifyGoods = (NotifyGoodsInfo) JsonUtils.fromJsonToJava(notifyGoodsJO,
								NotifyGoodsInfo.class);
						mTempList.add(notifyGoods);
					}
				}
				Message message = new Message();
				message.what = AOG_GET_SUC;
				message.obj = mTempList;
				handler.sendMessage(message);
			} else {
				String msg = result.getString("msg");
				Message message = new Message();
				message.what = AOG_GET_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(AOG_GET_EXCEPTION);
		}
	}

	public static void closePriceReduce(final Context context, final Handler handler, String id) {
		String url = RequestUrl.HOST_URL + RequestUrl.notice.closePriceReduce;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("alert_id", URLEncoder.encode(id, "UTF-8"));
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_closePriceReduce", response.toString());
						parseClosePriceReduceData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseClosePriceReduceData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(PRICE_REDUCE_CLOSE_SUC);
			} else {
				String msg = result.getString("msg");
				Message message = new Message();
				message.what = PRICE_REDUCE_CLOSE_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(PRICE_REDUCE_CLOSE_EXCEPTION);
		}
	}

	public static void closeAOG(final Context context, final Handler handler, String id) {
		String url = RequestUrl.HOST_URL + RequestUrl.notice.closeAog;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("alert_id", URLEncoder.encode(id, "UTF-8"));
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_closeAOG", response.toString());
						parseCloseAOGData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseCloseAOGData(JSONObject result, Handler handler) {
		try {
			if (result.getString(MsgResult.RESULT_TAG).equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(AOG_CLOSE_SUC);
			} else {
				String msg = result.getString("msg");
				Message message = new Message();
				message.what = AOG_CLOSE_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(AOG_CLOSE_EXCEPTION);
		}
	}

}
