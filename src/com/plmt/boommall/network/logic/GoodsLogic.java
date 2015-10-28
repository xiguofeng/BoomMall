package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Category;
import com.plmt.boommall.entity.Comment;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.JsonObjectRequestUtf;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.network.volley.toolbox.JsonObjectRequest;
import com.plmt.boommall.utils.JsonUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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

	public static final int CATEGROY_TOP_LIST_GET_SUC = GOODS_GET_EXCEPTION + 1;

	public static final int CATEGROY_TOP_LIST_GET_FAIL = CATEGROY_TOP_LIST_GET_SUC + 1;

	public static final int CATEGROY_TOP_LIST_GET_EXCEPTION = CATEGROY_TOP_LIST_GET_FAIL + 1;

	public static final int CATEGROY_SUB_LIST_GET_SUC = CATEGROY_TOP_LIST_GET_EXCEPTION + 1;

	public static final int CATEGROY_SUB_LIST_GET_FAIL = CATEGROY_SUB_LIST_GET_SUC + 1;

	public static final int CATEGROY_SUB_LIST_GET_EXCEPTION = CATEGROY_SUB_LIST_GET_FAIL + 1;

	public static void getGoodsListByCategory(final Context context,
			final Handler handler, String categoryName, final int pageNum,
			final int pageSize,final String sortType) {

		String url = RequestUrl.HOST_URL
				+ RequestUrl.goods.queryGoodsByCategoryNew;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("category",
					URLEncoder.encode(categoryName, "UTF-8"));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);
			requestJson.put("orderitme", sortType);

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequestUtf(Method.POST, url, requestJson,
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
			requestJson.put("id", URLEncoder.encode(id, "UTF-8"));

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequest(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_queryGoodsByID",
												response.toString());
										parseGoodsByIdData(response, handler);
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

	private static void parseGoodsByIdData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				Goods goods = (Goods) JsonUtils.fromJsonToJava(jsonObject,
						Goods.class);
				goods.setNum("0");

				JSONObject commentJsonObject = jsonObject
						.getJSONObject("first_commit");
				Comment comment = (Comment) JsonUtils.fromJsonToJava(
						commentJsonObject, Comment.class);
				goods.setComment(comment);

				Message message = new Message();
				message.what = GOODS_GET_SUC;
				message.obj = goods;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(GOODS_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_GET_EXCEPTION);
		}
	}

	public static void getTopCategory(final Context context,
			final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryTopCategory;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson
					.put("action", URLEncoder.encode("getcategory", "UTF-8"));

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequest(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_getTopCategory",
												response.toString());
										parseTopCategoryData(response, handler);
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

	private static void parseTopCategoryData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONArray jsonArray = response
						.getJSONArray(MsgResult.RESULT_DATA_TAG);

				ArrayList<Category> mTempCategoryList = new ArrayList<Category>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(i);
					Category category = (Category) JsonUtils.fromJsonToJava(
							categoryJsonObject, Category.class);
					mTempCategoryList.add(category);
				}
				Message message = new Message();
				message.what = CATEGROY_TOP_LIST_GET_SUC;
				message.obj = mTempCategoryList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CATEGROY_TOP_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_TOP_LIST_GET_EXCEPTION);
		}
	}

	public static void getSubCategory(final Context context,
			final Handler handler, final String topCategoryName) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.querySubCategory;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("categoryName",
					URLEncoder.encode(topCategoryName, "UTF-8"));

			BaseApplication.getInstanceRequestQueue().add(
					new JsonObjectRequest(Method.POST, url, requestJson,
							new Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) {
									if (null != response) {
										Log.e("xxx_getSubCategory",
												response.toString());
										parseSubCategoryData(response, handler);
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

	private static void parseSubCategoryData(JSONObject response,
			Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject dataJsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				JSONArray jsonArray = dataJsonObject
						.getJSONArray("subcategory");
				String parentImageurl = dataJsonObject.getString("image");

				ArrayList<Category> mTempCategoryList = new ArrayList<Category>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(i);
					Category category = (Category) JsonUtils.fromJsonToJava(
							categoryJsonObject, Category.class);
					category.setParentImageurl(parentImageurl);
					mTempCategoryList.add(category);
				}

				Message message = new Message();
				message.what = CATEGROY_SUB_LIST_GET_SUC;
				message.obj = mTempCategoryList;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CATEGROY_SUB_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_SUB_LIST_GET_EXCEPTION);
		}
	}
}
