package com.plmt.boommall.network.logic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.network.config.RequestUrl;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.UserInfoManager;

public class UserLogic {

	public static final int NET_ERROR = 0;

	public static final int REGIS_SUC = NET_ERROR + 1;

	public static final int REGIS_FAIL = REGIS_SUC + 1;

	public static final int REGIS_EXCEPTION = REGIS_FAIL + 1;

	public static final int LOGIN_SUC = REGIS_EXCEPTION + 1;

	public static final int LOGIN_FAIL = LOGIN_SUC + 1;

	public static final int LOGIN_EXCEPTION = LOGIN_FAIL + 1;

	public static final int MODIFY_PWD_SUC = LOGIN_EXCEPTION + 1;

	public static final int MODIFY_PWD_FAIL = MODIFY_PWD_SUC + 1;

	public static final int MODIFY_PWD_EXCEPTION = MODIFY_PWD_FAIL + 1;

	public static final int SEND_AUTHCODE_SUC = MODIFY_PWD_EXCEPTION + 1;

	public static final int SEND_AUTHCODE_FAIL = SEND_AUTHCODE_SUC + 1;

	public static final int SEND_AUTHCODE_EXCEPTION = SEND_AUTHCODE_FAIL + 1;

	public static void login(final Context context, final Handler handler, final User user) {

		String url = RequestUrl.HOST_URL + RequestUrl.account.login;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("name", URLEncoder.encode(user.getUserName(), "UTF-8"));
			requestJson.put("password", URLEncoder.encode(user.getPassword(), "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_login", response.toString());
						parseLoginData(response, handler);
					}

				}
			}, null);

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
			// //
			// new JsonObjectRequest(Method.POST, url, requestJson,
			// new Listener<JSONObject>() {
			// @Override
			// public void onResponse(JSONObject response) {
			// if (null != response) {
			// Log.e("xxx_login", response.toString());
			// parseLoginData(response, handler);
			// }
			//
			// }
			// }, null)

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// {"data":{"session":"vuoklov0duuvgekolkm1set245"},"result":"0","msg":"","Set-Cookie":"frontend=63t4cuvurhsbqdhunafrshf0h4;
	// path=\/"}
	private static void parseLoginData(JSONObject response, Handler handler) {
		try {
			// Log.e("xxx_login_suc", response.toString());
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String session = jsonObject.getString("session");
				//String session = response.getString("Set-Cookie");
				// session = StringUtils.getCookieValue(session);
				Message message = new Message();
				message.what = LOGIN_SUC;
				message.obj = session;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(LOGIN_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(LOGIN_EXCEPTION);
		}
	}

	public static void getInfo(final Context context, final Handler handler) {
		JSONObject requestJson = new JSONObject();
		try {
			// URLEncoder.encode(UserInfoManager.getSession(context), "UTF-8")
			requestJson.put("sessionid", "frontend="+UserInfoManager.getSession(context));
			Log.e("xxx_sessionid", "frontend="+UserInfoManager.getSession(context));
			String url = RequestUrl.HOST_URL + RequestUrl.account.getInfo;
			Log.e("xxx_getInfo_url", url);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url, requestJson, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					if (null != response) {
						Log.e("xxx_getInfo", response.toString());
						// parseLoginData(response, handler);
					}

				}

			}, null);
			cookieRequest.setCookie("frontend="+UserInfoManager.getSession(context));
			
			
			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static void getInfoByHttpUrl(final Context context, final Handler handler) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					URL httpurl = new URL(RequestUrl.HOST_URL + RequestUrl.account.getInfo);
					HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();

					connection.setDoOutput(true);// 允许连接提交信息
					connection.setRequestMethod("POST");// 提交方式“GET”、“POST”
					connection.setRequestProperty("Cookie", "ibismkhnd9hto8m8j5sj1vg5s5");// 将当前的sessionid一并上传

					connection.connect();
					// POST请求
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					JSONObject obj = new JSONObject();
					obj.put("sessionid", "ibismkhnd9hto8m8j5sj1vg5s5");

					out.writeBytes(obj.toString());
					out.flush();
					out.close();

					// 读取响应
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String lines;
					StringBuffer sb = new StringBuffer("");
					while ((lines = reader.readLine()) != null) {
						lines = new String(lines.getBytes(), "utf-8");
						sb.append(lines);
						Log.e("xxx_urlconnection_getinfo", sb.toString());
					}
					reader.close();
					// 断开连接
					connection.disconnect();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}

	public static void modifyPwd(final Context context, final Handler handler, final User user, final String authCode) {
	}

	// {"datas":"{}","message":"操作成功","result":"0"}
	private static void parseModifyPwdData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				// Log.e("xxx_modify_suc", sucResult);
				handler.sendEmptyMessage(MODIFY_PWD_SUC);
			} else {
				handler.sendEmptyMessage(MODIFY_PWD_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(MODIFY_PWD_EXCEPTION);
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 *            手机号
	 * @param authType
	 *            验证类型:1-客户登陆验证 2-下单验证 3-配送员修改密码验证
	 * @return
	 */
	public static void sendAuthCode(final Context context, final Handler handler, final String phone,
			final String authType) {
	}

	// {"datas":{"authCode":750152},"message":"操作成功","result":"0"}
	private static void parseSendAuthCodeData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String authCode = jsonObject.getString("authCode");
				Message message = new Message();
				message.what = SEND_AUTHCODE_SUC;
				message.obj = authCode;
				handler.sendMessage(message);

			} else {
				handler.sendEmptyMessage(SEND_AUTHCODE_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(SEND_AUTHCODE_EXCEPTION);
		}
	}

}
