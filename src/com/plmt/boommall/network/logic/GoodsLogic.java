package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.HttpUtils;

public class GoodsLogic {

	public static final int NET_ERROR = 0;

	public static final int GOODS_LIST_GET_SUC = NET_ERROR + 1;

	public static final int GOODS_LIST_GET_FAIL = GOODS_LIST_GET_SUC + 1;

	public static final int GOODS_LIST_GET_EXCEPTION = GOODS_LIST_GET_FAIL + 1;

	public static final int GOODS_GET_SUC = GOODS_LIST_GET_EXCEPTION + 1;

	public static final int GOODS_GET_FAIL = GOODS_GET_SUC + 1;

	public static final int GOODS_GET_EXCEPTION = GOODS_GET_FAIL + 1;

	public static void getGoodsListByCategory(final Context context,
			final Handler handler, String category, final int pageNum,
			final int pageSize) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				String url = RequestUrl.HOST_URL
						+ RequestUrl.goods.queryGoodsByCategory;

				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				try {
					params.add(new BasicNameValuePair("category", URLEncoder
							.encode("防晒", "UTF-8")));
					params.add(new BasicNameValuePair("c", String.valueOf("1")));
					params.add(new BasicNameValuePair("s", String.valueOf("5")));

					String resultStr = HttpUtils
							.sendHttpRequestByHttpClientGet(url, params);
					Log.e("xxx_url", resultStr);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//
				// String url = RequestUrl.HOST_URL
				// + RequestUrl.goods.queryGoodsByCategory;
				// Log.e("xxx_url", url);
				// JSONObject response = null;
				//
				// JSONObject requestJson = new JSONObject();
				// try {
				// // URLEncoder.encode("防晒", "UTF-8")
				// requestJson.put("category", "防晒");
				// requestJson.put("c", Integer.parseInt("1"));
				// requestJson.put("s", Integer.parseInt("5"));
				//
				// response = new JSONObject(
				// HttpUtils.sendHttpRequestByHttpClientPost(url,
				// requestJson));
				//
				// Log.e("xxx_queryGoodsByCategory", response.toString());
				//
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }

			}
		}).start();
	}

	public static void getGoodsListByCategory2(final Context context,
			final Handler handler, String category, final int pageNum,
			final int pageSize) {

		String url = RequestUrl.HOST_URL
				+ RequestUrl.goods.queryGoodsByCategoryNew;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			// URLEncoder.encode("防晒", "UTF-8")
			requestJson.put("category", URLEncoder.encode("防晒", "UTF-8"));
			requestJson.put("c", Integer.parseInt("0"));
			requestJson.put("s", Integer.parseInt("5"));

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequest(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_queryGoodsByCategory",
												response.toString());
										parseGoodsListByCategoryData(response,
												handler);
									}

								}
							}, null));
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void parseGoodsListByCategoryData(JSONObject response,
			Handler handler) {
	}

}
