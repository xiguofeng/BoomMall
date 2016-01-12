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
import com.plmt.boommall.entity.Filter;
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

	public static final int CATEGROY_SUB_HOME_LIST_GET_SUC = CATEGROY_SUB_LIST_GET_EXCEPTION + 1;

	public static final int CATEGROY_SUB_HOME_LIST_GET_FAIL = CATEGROY_SUB_HOME_LIST_GET_SUC + 1;

	public static final int CATEGROY_SUB_HOME_LIST_GET_EXCEPTION = CATEGROY_SUB_HOME_LIST_GET_FAIL + 1;

	public static final int CATEGROY_HOME_LIST_GET_SUC = CATEGROY_SUB_HOME_LIST_GET_EXCEPTION + 1;

	public static final int CATEGROY_HOME_LIST_GET_FAIL = CATEGROY_HOME_LIST_GET_SUC + 1;

	public static final int CATEGROY_HOME_LIST_GET_EXCEPTION = CATEGROY_HOME_LIST_GET_FAIL + 1;
	
	public static final int FILTE_GET_SUC = CATEGROY_HOME_LIST_GET_EXCEPTION + 1;

	public static final int FILTE_GET_FAIL = FILTE_GET_SUC + 1;

	public static final int FILTE_GET_EXCEPTION = FILTE_GET_FAIL + 1;

	public static void getGoodsListByCategory(final Context context, final Handler handler, String categoryName,
			final int pageNum, final int pageSize, final String sortType) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryGoodsByCategoryNew;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("category", URLEncoder.encode(categoryName, "UTF-8"));
			requestJson.put("c", pageNum);
			requestJson.put("s", pageSize);
			requestJson.put("orderitem", sortType);
			Log.e("xxx_orderitme", "orderitme:" + sortType);

			BaseApplication.getInstanceRequestQueue()
					.add(new JsonObjectRequestUtf(Method.POST, url, requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_queryGoodsByCategory" + "--orderitme:" + sortType, response.toString());
								parseGoodsListByCategoryData(response, handler);
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

	private static void parseGoodsListByCategoryData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject dataJB = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String total = dataJB.getString("productCount");
				JSONArray jsonArray = dataJB.getJSONArray("productItems");
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				int size = jsonArray.length();
				for (int j = 0; j < size; j++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(j);
					Goods goods = (Goods) JsonUtils.fromJsonToJava(categoryJsonObject, Goods.class);
					goods.setNum("0");
					mTempGoodsList.add(goods);
				}

				Message message = new Message();
				message.what = GOODS_LIST_BY_KEY_GET_SUC;
				message.obj = mTempGoodsList;
				Bundle bData = new Bundle();
				bData.putString("total", total);
				message.setData(bData);
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(GOODS_LIST_BY_KEY_GET_EXCEPTION);
		}

	}

	public static void getGoodsById(final Context context, final Handler handler, String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryGoodsByID;
		Log.e("xxx_getGoodsById_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("id", URLEncoder.encode(id, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_submitOrder", response.toString());
						parseGoodsByIdData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend=" + UserInfoManager.getSession(context));
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

			// BaseApplication.getInstanceRequestQueue()
			// .add(new JsonObjectRequest(Method.POST, url, requestJson, new
			// Listener<JSONObject>() {
			// @Override
			// public void onResponse(JSONObject response) {
			// if (null != response) {
			// Log.e("xxx_queryGoodsByID", response.toString());
			// parseGoodsByIdData(response, handler);
			// }
			//
			// }
			// }, null));
			// BaseApplication.getInstanceRequestQueue().start();

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
				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

				Goods goods = (Goods) JsonUtils.fromJsonToJava(jsonObject, Goods.class);
				goods.setNum("0");

				JSONArray jsonArray = jsonObject.getJSONArray("litterImage");
				ArrayList<String> tempLitterImage = new ArrayList<>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject imageJson = jsonArray.getJSONObject(i);
					tempLitterImage.add(imageJson.getString("thumbnail"));
				}
				ArrayList<String> litterImage = new ArrayList<>();
				goods.setLitterImage(litterImage);
				goods.getLitterImage().addAll(tempLitterImage);

				JSONObject commentJsonObject = jsonObject.getJSONObject("first_commit");
				Comment comment = (Comment) JsonUtils.fromJsonToJava(commentJsonObject, Comment.class);
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

	public static void getTopCategory(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryTopCategory;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("action", URLEncoder.encode("getcategory", "UTF-8"));

			BaseApplication.getInstanceRequestQueue()
					.add(new JsonObjectRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getTopCategory", response.toString());
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

	private static void parseTopCategoryData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONArray jsonArray = response.getJSONArray(MsgResult.RESULT_DATA_TAG);

				ArrayList<Category> mTempCategoryList = new ArrayList<Category>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(i);
					Category category = (Category) JsonUtils.fromJsonToJava(categoryJsonObject, Category.class);
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

	public static void getSubCategory(final Context context, final Handler handler, final String topCategoryName) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.querySubCategory;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("categoryName", URLEncoder.encode(topCategoryName, "UTF-8"));

			BaseApplication.getInstanceRequestQueue()
					.add(new JsonObjectRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getSubCategory", response.toString());
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

	private static void parseSubCategoryData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject dataJsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);
				JSONArray jsonArray = dataJsonObject.getJSONArray("subcategory");
				String parentImageurl = dataJsonObject.getString("image");

				ArrayList<Category> mTempCategoryList = new ArrayList<Category>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject categoryJsonObject = jsonArray.getJSONObject(i);
					Category category = (Category) JsonUtils.fromJsonToJava(categoryJsonObject, Category.class);
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

	public static void getSubCategoryHome(final Context context, final Handler handler, final String topCategoryName) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.querySubCategoryHome;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("rootCategory", URLEncoder.encode(topCategoryName, "UTF-8"));

			BaseApplication.getInstanceRequestQueue()
					.add(new JsonObjectRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getSubCategoryHome", response.toString());
								parseSubCategoryHomeData(response, handler);
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

	private static void parseSubCategoryHomeData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject dataJsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

				JSONObject categoryJsonObject = dataJsonObject.getJSONObject("Category");
				String parentImageurl = categoryJsonObject.getString("image");
				String rootCategoryName = categoryJsonObject.getString("rootCategoryName");

				JSONObject subCategoryJsonObject = dataJsonObject.getJSONObject("subCategory");
				JSONArray rootNameJsonArray = subCategoryJsonObject.getJSONArray("rootName");
				ArrayList<RootName> mTempRootNameList = new ArrayList<RootName>();
				for (int j = 0; j < rootNameJsonArray.length(); j++) {
					JSONObject rootNameJsonObject = rootNameJsonArray.getJSONObject(j);
					RootName rootName = (RootName) JsonUtils.fromJsonToJava(rootNameJsonObject, RootName.class);
					mTempRootNameList.add(rootName);
				}

				JSONArray goodsJsonArray = subCategoryJsonObject.getJSONArray("product");
				ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
				int size = goodsJsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject goodsJsonObject = goodsJsonArray.getJSONObject(i);
					Goods goods = (Goods) JsonUtils.fromJsonToJava(goodsJsonObject, Goods.class);
					mTempGoodsList.add(goods);
				}
				HomeRecommend homeRecommend = new HomeRecommend();
				homeRecommend.setImage(parentImageurl);
				homeRecommend.setRootCategoryName(rootCategoryName);
				ArrayList<Goods> goodsList = new ArrayList<Goods>();
				homeRecommend.setGoodsList(goodsList);
				homeRecommend.getGoodsList().addAll(mTempGoodsList);
				ArrayList<RootName> rootNameList = new ArrayList<RootName>();
				homeRecommend.setRootNameList(rootNameList);
				homeRecommend.getRootNameList().addAll(mTempRootNameList);
				Log.e("xxx_RootNameList", "size:" + mTempRootNameList.size());

				Message message = new Message();
				message.what = CATEGROY_SUB_HOME_LIST_GET_SUC;
				message.obj = homeRecommend;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CATEGROY_SUB_HOME_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_SUB_HOME_LIST_GET_EXCEPTION);
		}
	}

	public static void getHomeCategory(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.goods.queryHomeCategory;
		JSONObject requestJson = new JSONObject();
		BaseApplication.getInstanceRequestQueue()
				.add(new JsonObjectRequestUtf(Method.POST, url, requestJson, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != response) {
							Log.e("xxx_getHomeCategory", response.toString());
							parseHomeCategoryData(response, handler);
						}

					}
				}, null));
		BaseApplication.getInstanceRequestQueue().start();
	}

	private static void parseHomeCategoryData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				HashMap<String, HomeRecommend> recommendMap = new LinkedHashMap<String, HomeRecommend>();

				JSONArray dataJsonArray = response.getJSONArray(MsgResult.RESULT_DATA_TAG);
				for (int k = 0; k < dataJsonArray.length(); k++) {
					JSONObject recommendJsonObject = dataJsonArray.getJSONObject(k);
					HomeRecommend recommend = (HomeRecommend) JsonUtils.fromJsonToJava(recommendJsonObject,
							HomeRecommend.class);

					JSONArray rootNameJsonArray = recommendJsonObject.getJSONArray("rootName");
					ArrayList<RootName> mTempRootNameList = new ArrayList<RootName>();
					for (int j = 0; j < rootNameJsonArray.length(); j++) {
						JSONObject rootNameJsonObject = rootNameJsonArray.getJSONObject(j);
						RootName rootName = (RootName) JsonUtils.fromJsonToJava(rootNameJsonObject, RootName.class);
						mTempRootNameList.add(rootName);
					}

					JSONArray goodsJsonArray = recommendJsonObject.getJSONArray("product");
					ArrayList<Goods> mTempGoodsList = new ArrayList<Goods>();
					int size = goodsJsonArray.length();
					for (int i = 0; i < size; i++) {
						JSONObject goodsJsonObject = goodsJsonArray.getJSONObject(i);
						Goods goods = (Goods) JsonUtils.fromJsonToJava(goodsJsonObject, Goods.class);
						mTempGoodsList.add(goods);
					}

					ArrayList<Goods> goodsList = new ArrayList<Goods>();
					recommend.setGoodsList(goodsList);
					recommend.getGoodsList().addAll(mTempGoodsList);
					ArrayList<RootName> rootNameList = new ArrayList<RootName>();
					recommend.setRootNameList(rootNameList);
					recommend.getRootNameList().addAll(mTempRootNameList);

					recommendMap.put(String.valueOf(k), recommend);
				}

				Message message = new Message();
				message.what = CATEGROY_HOME_LIST_GET_SUC;
				message.obj = recommendMap;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(CATEGROY_HOME_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(CATEGROY_HOME_LIST_GET_EXCEPTION);
		}
	}
	
	public static void getFilter(final Context context, final Handler handler, String category_id,String attribute_price,String attribute_brand_filter,String attribute_continent) {
		String url = RequestUrl.HOST_URL + RequestUrl.goods.getFilter;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("category_id", URLEncoder.encode(category_id, "UTF-8"));
			requestJson.put("attribute_price", URLEncoder.encode(attribute_price, "UTF-8"));
			requestJson.put("attribute_brand_filter", URLEncoder.encode(attribute_brand_filter, "UTF-8"));
			requestJson.put("attribute_continent", URLEncoder.encode(attribute_continent, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_getFilter", response.toString());
						parseFilterData(response, handler);
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
	
	private static void parseFilterData(JSONObject response, Handler handler) {

		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
               Log.e("xxx_1", "suc");
				HashMap<String, ArrayList<Filter>> filterMap = new LinkedHashMap<>();
				JSONObject jsonObject =response.getJSONObject(MsgResult.RESULT_DATA_TAG);
				
				if(jsonObject.has("price")){
					ArrayList<Filter> priceFilterList = new ArrayList<Filter>();
					JSONArray priceJsonArray = jsonObject.getJSONArray("price");
					for(int i=0;i<priceJsonArray.length();i++){
						JSONObject priceJsonObject = priceJsonArray.getJSONObject(i);
						Filter priceFilter = (Filter) JsonUtils.fromJsonToJava(priceJsonObject,
								Filter.class);
						priceFilterList.add(priceFilter);
					}
					filterMap.put("price", priceFilterList);
				}
				
				if(jsonObject.has("color")){
					ArrayList<Filter> colorFilterList = new ArrayList<Filter>();
					JSONArray colorJsonArray = jsonObject.getJSONArray("color");
					for(int i=0;i<colorJsonArray.length();i++){
						JSONObject colorJsonObject = colorJsonArray.getJSONObject(i);
						Filter colorFilter = (Filter) JsonUtils.fromJsonToJava(colorJsonObject,
								Filter.class);
						colorFilterList.add(colorFilter);
					}
					filterMap.put("color", colorFilterList);
				}
				
				if(jsonObject.has("brand_filter")){
					ArrayList<Filter> brandFilterList = new ArrayList<Filter>();
					JSONArray brandJsonArray = jsonObject.getJSONArray("brand_filter");
					for(int i=0;i<brandJsonArray.length();i++){
						JSONObject brandJsonObject = brandJsonArray.getJSONObject(i);
						Filter brandFilter = (Filter) JsonUtils.fromJsonToJava(brandJsonObject,
								Filter.class);
						brandFilterList.add(brandFilter);
					}
					filterMap.put("brand_filter", brandFilterList);
				}
				
				if(jsonObject.has("continent")){
					ArrayList<Filter> continentFilterList = new ArrayList<Filter>();
					JSONArray continentJsonArray = jsonObject.getJSONArray("continent");
					for(int i=0;i<continentJsonArray.length();i++){
						JSONObject continentJsonObject = continentJsonArray.getJSONObject(i);
						Filter continentFilter = (Filter) JsonUtils.fromJsonToJava(continentJsonObject,
								Filter.class);
						continentFilterList.add(continentFilter);
					}
					filterMap.put("continent", continentFilterList);
				}

				Message message = new Message();
				message.what = FILTE_GET_SUC;
				message.obj = filterMap;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(FILTE_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(FILTE_GET_EXCEPTION);
		}
	}
	
}
