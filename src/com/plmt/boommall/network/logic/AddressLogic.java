package com.plmt.boommall.network.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class AddressLogic {

	public static final int NET_ERROR = 0;

	public static final int ANDRESS_GET_SUC = NET_ERROR + 1;

	public static final int ANDRESS_GET_FAIL = ANDRESS_GET_SUC + 1;

	public static final int ANDRESS_GET_EXCEPTION = ANDRESS_GET_FAIL + 1;

	public static final int ANDRESS_LIST_GET_SUC = ANDRESS_GET_EXCEPTION + 1;

	public static final int ANDRESS_LIST_GET_FAIL = ANDRESS_LIST_GET_SUC + 1;

	public static final int ANDRESS_LIST_GET_EXCEPTION = ANDRESS_LIST_GET_FAIL + 1;

	public static final int ANDRESS_MODIFY_SUC = ANDRESS_LIST_GET_EXCEPTION + 1;

	public static final int ANDRESS_MODIFY_FAIL = ANDRESS_MODIFY_SUC + 1;

	public static final int ANDRESS_MODIFY_EXCEPTION = ANDRESS_MODIFY_FAIL + 1;

	public static final int ANDRESS_DEL_SUC = ANDRESS_MODIFY_EXCEPTION + 1;

	public static final int ANDRESS_DEL_FAIL = ANDRESS_DEL_SUC + 1;

	public static final int ANDRESS_DEL_EXCEPTION = ANDRESS_DEL_FAIL + 1;

	public static void getList(final Context context, final Handler handler) {

		String url = RequestUrl.HOST_URL + RequestUrl.address.list;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_address_getList",
										response.toString());
								parseListData(response, handler);
							}

						}
					}, null);

			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static void parseListData(JSONObject response, Handler handler) {
		try {

			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);
				
				JSONArray addressArray = jsonObject.getJSONArray("addresses");
				int size = addressArray.length();
				ArrayList<Address> addresslist = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					JSONObject addressJsonObject = addressArray.getJSONObject(i);
					Address address = (Address) JsonUtils.fromJsonToJava(
							addressJsonObject, Address.class);
					addresslist.add(address);
				}

				Message message = new Message();
				message.what = ANDRESS_LIST_GET_SUC;
				message.obj = addresslist;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(ANDRESS_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(ANDRESS_LIST_GET_EXCEPTION);
		}
	}

}
