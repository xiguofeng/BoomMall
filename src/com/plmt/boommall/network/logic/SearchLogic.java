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
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.JsonObjectRequestUtf;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;

public class SearchLogic {

	public static final int NET_ERROR = 0;

	public static final int NORAML_GET_SUC = NET_ERROR + 1;

	public static final int NORAML_GET_FAIL = NORAML_GET_SUC + 1;

	public static final int NORAML_GET_EXCEPTION = NORAML_GET_FAIL + 1;

	public static final int HOT_KEY_GET_SUC = NET_ERROR + 1;

	public static final int HOT_KEY_GET_FAIL = HOT_KEY_GET_SUC + 1;

	public static final int HOT_KEY_GET_EXCEPTION = HOT_KEY_GET_FAIL + 1;

	public static void queryGoods(final Context context, final Handler handler,
			String query, String categoryName, final int pageNum,
			final int pageSize, final String sortType) {

		String url = RequestUrl.HOST_URL + RequestUrl.search.normal;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("query", URLEncoder.encode(query, "UTF-8"));
			requestJson.put("category",
					URLEncoder.encode(categoryName, "UTF-8"));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);
			requestJson.put("orderitme", sortType);

			Log.e("xxx_queryGoods_request", requestJson.toString());

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequestUtf(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_queryGoods",
												response.toString());
										parseGoodsListData(response, handler);
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

	private static void parseGoodsListData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject dataJB = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				JSONArray jsonArray = dataJB.getJSONArray("productItems");
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				int size = jsonArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(j);
					Goods goods = (Goods) JsonUtils.fromJsonToJava(
							categoryJsonObject, Goods.class);
					goods.setNum("0");
					mTempGoodsList.add(goods);
				}

				Message message = new Message();
				message.what = NORAML_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(NORAML_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(NORAML_GET_EXCEPTION);
		}
	}

	public static void getHotKeys(final Context context, final Handler handler,
			String query, String categoryName, final int pageNum,
			final int pageSize, final String sortType) {

		String url = RequestUrl.HOST_URL + RequestUrl.search.normal;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("query", URLEncoder.encode(query, "UTF-8"));
			requestJson.put("category",
					URLEncoder.encode(categoryName, "UTF-8"));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);
			requestJson.put("orderitme", sortType);

			Log.e("xxx_queryGoods_request", requestJson.toString());

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequestUtf(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_queryGoods",
												response.toString());
										parseGoodsListData(response, handler);
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
}
