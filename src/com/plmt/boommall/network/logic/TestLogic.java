package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;

public class TestLogic {

	public static final int NET_ERROR = 0;

	public static final int TEST_SUC = NET_ERROR + 1;

	public static final int TEST_FAIL = TEST_SUC + 1;

	public static final int TEST_EXCEPTION = TEST_FAIL + 1;

	public static void test(final Context context, final Handler handler,
			final String parameter) throws UnsupportedEncodingException {

		String url = "http://120.55.116.206:8060/mapi/pdp/testimport";
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("base64", parameter);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_test", response.toString());
							}

						}
					}, null);

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
