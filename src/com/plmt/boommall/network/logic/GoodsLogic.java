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
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.network.volley.toolbox.JsonObjectRequest;
import com.plmt.boommall.utils.JsonUtils;

public class GoodsLogic {

	public static final int NET_ERROR = 0;

	public static final int GOODS_LIST_GET_SUC = NET_ERROR + 1;

	public static final int GOODS_LIST_GET_FAIL = GOODS_LIST_GET_SUC + 1;

	public static final int GOODS_LIST_GET_EXCEPTION = GOODS_LIST_GET_FAIL + 1;

	public static final int GOODS_LIST_BY_KEY_GET_SUC = GOODS_LIST_GET_EXCEPTION + 1;

	public static final int GOODS_LIST_BY_KEY_GET_FAIL = GOODS_LIST_BY_KEY_GET_SUC + 1;

	public static final int GOODS_LIST_BY_KEY_GET_EXCEPTION = GOODS_LIST_BY_KEY_GET_FAIL + 1;

	public static final int GOODS_GET_SUC = GOODS_LIST_BY_KEY_GET_EXCEPTION + 1;

	public static final int GOODS_GET_FAIL = GOODS_GET_SUC + 1;

	public static final int GOODS_GET_EXCEPTION = GOODS_GET_FAIL + 1;

	public static final int CATEGROY_LIST_GET_SUC = GOODS_GET_EXCEPTION + 1;

	public static final int CATEGROY_LIST_GET_FAIL = CATEGROY_LIST_GET_SUC + 1;

	public static final int CATEGROY_LIST_GET_EXCEPTION = CATEGROY_LIST_GET_FAIL + 1;

	public static void getGoodsListByCategory(final Context context,
			final Handler handler, String category, final int pageNum,
			final int pageSize) {

		String url = RequestUrl.HOST_URL
				+ RequestUrl.goods.queryGoodsByCategoryNew;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
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

		try {
			String sucResult = response.getString("is_success").trim();
			if (sucResult.equals("1")) {

				JSONArray jsonArray = response.getJSONArray("data");
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
				message.what = GOODS_LIST_BY_KEY_GET_SUC;
				message.obj = mTempGoodsList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}

	}

	public static void getGoodsById(final Context context,
			final Handler handler, String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryGoodsByID;
		Log.e("xxx_getGoodsById_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			id = "21612";
			requestJson.put("id", URLEncoder.encode(id, "UTF-8"));

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequest(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									Log.e("xxx_queryGoodsByID",
											response.toString());
									if (null != response) {
										Log.e("xxx_queryGoodsByID",
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

}
