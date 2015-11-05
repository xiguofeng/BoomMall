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
import com.plmt.boommall.entity.Comment;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.JsonUtils;
import com.plmt.boommall.utils.UserInfoManager;

public class CommetLogic {

	public static final int NET_ERROR = 0;

	public static final int COMMENT_LIST_GET_SUC = NET_ERROR + 1;

	public static final int COMMENT_LIST_GET_FAIL = COMMENT_LIST_GET_SUC + 1;

	public static final int COMMENT_LIST_SESSION_TIME_OUT = COMMENT_LIST_GET_FAIL + 1;

	public static final int COMMENT_LIST_GET_EXCEPTION = COMMENT_LIST_SESSION_TIME_OUT + 1;

	public static void getList(final Context context, final Handler handler,
			String id) {

		String url = RequestUrl.HOST_URL + RequestUrl.comment.list;
		JSONObject requestJson = new JSONObject();
		try {

			requestJson.put("session",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("id", id);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_Comment_getList",
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
				JSONArray commentArray = response
						.getJSONArray(MsgResult.RESULT_DATA_TAG);
				int size = commentArray.length();
				ArrayList<Comment> commentlist = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					JSONObject jsonObject = commentArray.getJSONObject(i);
					JSONObject commentJsonObject = jsonObject
							.getJSONObject("comment");
					Comment comment = (Comment) JsonUtils.fromJsonToJava(
							commentJsonObject, Comment.class);
					commentlist.add(comment);
				}

				Message message = new Message();
				message.what = COMMENT_LIST_GET_SUC;
				message.obj = commentlist;
				handler.sendMessage(message);
			} else if (sucResult.equals(MsgResult.RESULT_SESSION_TIMEOUT)) {
				handler.sendEmptyMessage(COMMENT_LIST_SESSION_TIME_OUT);
			} else {
				handler.sendEmptyMessage(COMMENT_LIST_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(COMMENT_LIST_GET_EXCEPTION);
		}
	}

}
