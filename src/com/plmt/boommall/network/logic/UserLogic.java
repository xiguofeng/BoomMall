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
import com.plmt.boommall.utils.JsonUtils;
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

	public static final int USER_INFO_GET_SUC = MODIFY_PWD_EXCEPTION + 1;

	public static final int USER_INFO_GET_FAIL = USER_INFO_GET_SUC + 1;

	public static final int USER_INFO_GET_EXCEPTION = USER_INFO_GET_FAIL + 1;

	public static final int SEND_AUTHCODE_SUC = MODIFY_PWD_EXCEPTION + 1;

	public static final int SEND_AUTHCODE_FAIL = SEND_AUTHCODE_SUC + 1;

	public static final int SEND_AUTHCODE_EXCEPTION = SEND_AUTHCODE_FAIL + 1;

	public static final int SET_REAL_SUC = SEND_AUTHCODE_EXCEPTION + 1;

	public static final int SET_REAL_FAIL = SET_REAL_SUC + 1;

	public static final int SET_REAL_EXCEPTION = SET_REAL_FAIL + 1;

	public static void login(final Context context, final Handler handler,
			final User user) {

		String url = RequestUrl.HOST_URL + RequestUrl.account.login;
		Log.e("xxx_url", url);
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("name",
					URLEncoder.encode(user.getAccount(), "UTF-8"));
			requestJson.put("password",
					URLEncoder.encode(user.getPassword(), "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
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
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				String session = jsonObject.getString("session");
				// String session = response.getString("Set-Cookie");
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
			requestJson.put("sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			Log.e("xxx_sessionid",
					"frontend=" + UserInfoManager.getSession(context));
			String url = RequestUrl.HOST_URL + RequestUrl.account.getInfo;
			Log.e("xxx_getInfo_url", url);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_getInfo", response.toString());
								parseInfoData(response, handler);
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

	private static void parseInfoData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATA_TAG);

				User user = (User) JsonUtils.fromJsonToJava(jsonObject,
						User.class);

				Message message = new Message();
				message.what = USER_INFO_GET_SUC;
				message.obj = user;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(USER_INFO_GET_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(USER_INFO_GET_EXCEPTION);
		}
	}

	public static void getInfoByHttpUrl(final Context context,
			final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL httpurl = new URL(RequestUrl.HOST_URL
							+ RequestUrl.account.getInfo);
					HttpURLConnection connection = (HttpURLConnection) httpurl
							.openConnection();

					connection.setDoOutput(true);// 允许连接提交信息
					connection.setRequestMethod("POST");// 提交方式“GET”、“POST”
					connection.setRequestProperty("Cookie",
							"ibismkhnd9hto8m8j5sj1vg5s5");// 将当前的sessionid一并上传

					connection.connect();
					// POST请求
					DataOutputStream out = new DataOutputStream(connection
							.getOutputStream());
					JSONObject obj = new JSONObject();
					obj.put("sessionid", "ibismkhnd9hto8m8j5sj1vg5s5");

					out.writeBytes(obj.toString());
					out.flush();
					out.close();

					// 读取响应
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
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

	public static void register(final Context context, final Handler handler,
			final User user, final String authCode) {

		String url = RequestUrl.HOST_URL + RequestUrl.account.register;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("phone",
					URLEncoder.encode(user.getPhone(), "UTF-8"));
			requestJson.put("pwd",
					URLEncoder.encode(user.getPassword(), "UTF-8"));
			requestJson.put("pwd2",
					URLEncoder.encode(user.getPassword(), "UTF-8"));
			requestJson.put("authCode", URLEncoder.encode(authCode, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_register", response.toString());
								parseRegisterData(response, handler);
							}

						}
					}, null);

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// {"data":[],"result":"0","msg":"","Set-Cookie":"frontend=t1b5e79bqp00pf80jobfpfbrc7;
	// path=\/"}
	private static void parseRegisterData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(REGIS_SUC);
			} else {
				handler.sendEmptyMessage(REGIS_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(REGIS_EXCEPTION);
		}
	}

	public static void forgetPwd(final Context context, final Handler handler,
			final User user, final String authCode) {

		String url = RequestUrl.HOST_URL + RequestUrl.account.forgetPwd;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("phone",
					URLEncoder.encode(user.getPhone(), "UTF-8"));
			requestJson.put("pwd",
					URLEncoder.encode(user.getPassword(), "UTF-8"));
			requestJson.put("pwd2",
					URLEncoder.encode(user.getPassword(), "UTF-8"));
			requestJson.put("authCode", URLEncoder.encode(authCode, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_forgetPwd", response.toString());
								parseForgetPwdData(response, handler);
							}

						}
					}, null);

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void parseForgetPwdData(JSONObject response, Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
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
	public static void sendAuthCode(final Context context,
			final Handler handler, final String phone, final String authType) {

		String url = RequestUrl.HOST_URL + RequestUrl.account.sendAuthCode;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("phone", URLEncoder.encode(phone, "UTF-8"));
			requestJson.put("register", URLEncoder.encode(authType, "UTF-8"));

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_sendAuthCode", response.toString());
								parseSendAuthCodeData(response, handler);
							}

						}
					}, null);

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// {"data":"770340","result":"0","msg":"","Set-Cookie":"frontend=4jpqr6krfjrmjpb3ernp8budp2;
	// path=\/"}
	// {"data":[],"result":"-1","msg":"手机号已存在","Set-Cookie":"frontend=fh0tjat9p3ik325h5vp9s7er63;
	// path=\/"}
	private static void parseSendAuthCodeData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				String authCode = response.getString(MsgResult.RESULT_DATA_TAG);
				Message message = new Message();
				message.what = SEND_AUTHCODE_SUC;
				message.obj = authCode;
				handler.sendMessage(message);
			} else {
				String msg = response.getString("msg");
				Message message = new Message();
				message.what = SEND_AUTHCODE_FAIL;
				message.obj = msg;
				handler.sendMessage(message);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(SEND_AUTHCODE_EXCEPTION);
		}
	}

	public static void setReal(final Context context, final Handler handler,
			final String realname, final String dentity,
			final String id_photo_opposite, final String id_photo_positive) {

		// String url = "http://120.55.116.206:8060/mapi/checkout/setReal";
		String url = RequestUrl.HOST_URL + RequestUrl.account.setReal;
		JSONObject requestJson = new JSONObject();
		try {
			requestJson.put("session",
					"frontend=" + UserInfoManager.getSession(context));
			requestJson.put("realname", URLEncoder.encode(realname, "UTF-8"));
			requestJson.put("dentity", URLEncoder.encode(dentity, "UTF-8"));
			requestJson.put("id_photo_opposite", id_photo_opposite);
			requestJson.put("id_photo_positive", id_photo_positive);

			CookieRequest cookieRequest = new CookieRequest(Method.POST, url,
					requestJson, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							if (null != response) {
								Log.e("xxx_test", response.toString());
								parseRealData(response, handler);
							}

						}
					}, null);

			cookieRequest.setCookie("frontend="
					+ UserInfoManager.getSession(context));

			BaseApplication.getInstanceRequestQueue().add(cookieRequest);
			BaseApplication.getInstanceRequestQueue().start();

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void parseRealData(JSONObject response, Handler handler) {
		try {
			// Log.e("xxx_login_suc", response.toString());
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				handler.sendEmptyMessage(SET_REAL_SUC);
			} else {
				handler.sendEmptyMessage(SET_REAL_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(SET_REAL_EXCEPTION);
		}
	}

}
