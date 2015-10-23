package com.plmt.boommall.network.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Banner;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PromotionLogic {

	public static final int NET_ERROR = 0;

	public static final int BANNER_GET_SUC = NET_ERROR + 1;

	public static final int BANNER_GET_FAIL = BANNER_GET_SUC + 1;

	public static final int BANNER_GET_EXCEPTION = BANNER_GET_FAIL + 1;

	public static final int ANDRESS_LIST_GET_SUC = BANNER_GET_EXCEPTION + 1;

	public static final int ANDRESS_LIST_GET_FAIL = ANDRESS_LIST_GET_SUC + 1;

	public static final int ANDRESS_LIST_GET_EXCEPTION = ANDRESS_LIST_GET_FAIL + 1;

	public static final int ANDRESS_MODIFY_SUC = ANDRESS_LIST_GET_EXCEPTION + 1;

	public static final int ANDRESS_MODIFY_FAIL = ANDRESS_MODIFY_SUC + 1;

	public static final int ANDRESS_MODIFY_EXCEPTION = ANDRESS_MODIFY_FAIL + 1;

	public static final int ANDRESS_DEL_SUC = ANDRESS_MODIFY_EXCEPTION + 1;

	public static final int ANDRESS_DEL_FAIL = ANDRESS_DEL_SUC + 1;

	public static final int ANDRESS_DEL_EXCEPTION = ANDRESS_DEL_FAIL + 1;

	public static void getBannerList(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.promotion.getBanner;
		JSONObject requestJson = new JSONObject();
		CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
				requestJson, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != response) {
							Log.e("xxx_Banner_getList",
									response.toString());
							parseBannerListData(response, handler);
						}

					}
				}, null);

		BaseApplication.getInstanceRequestQueue().add(cookieRequest);
		BaseApplication.getInstanceRequestQueue().start();
	}

	private static void parseBannerListData(JSONObject response, Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {				
				JSONArray bannerArray = response.getJSONArray(MsgResult.RESULT_DATA_TAG);
				int size = bannerArray.length();
				ArrayList<Banner> bannerList = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					JSONObject bannerJsonObject = bannerArray.getJSONObject(i);
					Banner banner = (Banner) JsonUtils.fromJsonToJava(
							bannerJsonObject, Banner.class);
					bannerList.add(banner);
				}

				Message message = new Message();
				message.what = BANNER_GET_SUC;
				message.obj = bannerList;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(BANNER_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(BANNER_GET_EXCEPTION);
		}
	}

}
