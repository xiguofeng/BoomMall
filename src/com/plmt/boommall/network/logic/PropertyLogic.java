package com.plmt.boommall.network.logic;

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

	public static final int RECHARGE_SET_SUC = GIFTCARD_GET_EXCEPTION + 1;

	public static final int RECHARGE_SET_FAIL = RECHARGE_SET_SUC + 1;

	public static final int RECHARGE_SET_EXCEPTION = RECHARGE_SET_FAIL + 1;

	public static final int BALANCE_PAY_SUC = RECHARGE_SET_EXCEPTION + 1;

	public static final int BALANCE_PAY_FAIL = BALANCE_PAY_SUC + 1;

	public static final int BALANCE_PAY_EXCEPTION = BALANCE_PAY_FAIL + 1;

	public static final int GIFTCARD_PAY_SUC = BALANCE_PAY_EXCEPTION + 1;

	public static final int GIFTCARD_PAY_FAIL = GIFTCARD_PAY_SUC + 1;

	public static final int GIFTCARD_PAY_EXCEPTION = GIFTCARD_PAY_FAIL + 1;

	public static void queryIntegral(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.property.queryIntegral;
		JSONObject requestJson = new JSONObject();
		CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (null != response) {
					parseQueryIntegralData(response, handler);
				}

			}
		}, null);
		cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();
	}

	// {"msg":"","result":"0","data":"120"}
	private static void parseQueryIntegralData(JSONObject response, Handler handler) {
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
		String url = RequestUrl.HOST_URL + RequestUrl.property.queryRemainingMoney;

		CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (null != response) {
					parseQueryBalanceData(response, handler);
				}

			}

		}, null);
		cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();

	}

	// {"msg":"","result":"0","data":"9.01"}
	private static void parseQueryBalanceData(JSONObject response, Handler handler) {
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

	public static void queryGiftCard(final Context context, final Handler handler, final String giftcard) {
		try {
			String url = RequestUrl.HOST_URL + RequestUrl.property.queryGiftCard;
			JSONObject requestJson = new JSONObject();
			requestJson.put("giftcard", giftcard);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_giftcard", response.toString());
						parseQueryGiftCardData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// {"msg":"","result":"0","data":{"status":"可用","state":"1","balance":"0.0900"}}
	private static void parseQueryGiftCardData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

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

	public static void rechargeBalance(final Context context, final Handler handler, final String giftcard) {
		try {
			String url = RequestUrl.HOST_URL + RequestUrl.property.rechargeBalance;
			JSONObject requestJson = new JSONObject();
			requestJson.put("giftcard", giftcard);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_rechargeBalance", response.toString());
						parseRechargeBalanceData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private static void parseRechargeBalanceData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(RECHARGE_SET_SUC);
			} else {
				String msg = response.getString("msg");
				Message message = new Message();
				message.what = RECHARGE_SET_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(RECHARGE_SET_EXCEPTION);
		}
	}

	/**
	 * 
	 * @param context
	 * @param handler
	 * @param giftcard
	 *            1表示使用余额支付，0表示取消余额支付
	 */
	public static void balancePay(final Context context, final Handler handler, final String isUse) {
		try {
			String url = RequestUrl.HOST_URL + RequestUrl.property.balancePay;
			JSONObject requestJson = new JSONObject();
			requestJson.put("use_customer_balance", isUse);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_balancePay", response.toString());
						parseBalancePayData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	//{"data":"1000","result":"0","msg":"","Set-Cookie":"frontend=b9k6gd9eq3f7mib98kuifpuia7; expires=Sat, 31-Oct-2015 13:42:19 GMT; path=\/; domain=120.55.116.206; httponly"}
	private static void parseBalancePayData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(BALANCE_PAY_SUC);
			} else {
				handler.sendEmptyMessage(BALANCE_PAY_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(BALANCE_PAY_EXCEPTION);
		}
	}
	/**
	 * 
	 * @param context
	 * @param handler
	 * @param giftcard
	 */
	public static void giftCardPay(final Context context, final Handler handler, final String giftcardCode,
			final String isUse) {
		try {
			String url = RequestUrl.HOST_URL + RequestUrl.property.giftCardPay;
			JSONObject requestJson = new JSONObject();
			requestJson.put("giftcardCode", giftcardCode);
			requestJson.put("removeGiftcard", isUse);
			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_giftCardPay", response.toString());
						parseRechargeBalanceData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
