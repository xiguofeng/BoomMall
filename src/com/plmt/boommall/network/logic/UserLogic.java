package com.plmt.boommall.network.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.config.MsgResult;
import com.plmt.boommall.utils.JsonUtils;

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

	public static void login(final Context context, final Handler handler,
			final User user) {

	}

	private static void parseLoginData(JSONObject response, Handler handler) {
		try {
			// Log.e("xxx_login_suc", response.toString());
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {
				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);
				User user = (User) JsonUtils.fromJsonToJava(jsonObject,
						User.class);
				Message message = new Message();
				message.what = LOGIN_SUC;
				message.obj = user;
				handler.sendMessage(message);
			} else {
				handler.sendEmptyMessage(LOGIN_FAIL);
			}
		} catch (JSONException e) {
			handler.sendEmptyMessage(LOGIN_EXCEPTION);
		}
	}

	public static void modifyPwd(final Context context, final Handler handler,
			final User user, final String authCode) {}

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
	public static void sendAuthCode(final Context context,
			final Handler handler, final String phone, final String authType) {}

	// {"datas":{"authCode":750152},"message":"操作成功","result":"0"}
	private static void parseSendAuthCodeData(JSONObject response,
			Handler handler) {
		try {
			String sucResult = response.getString(MsgResult.RESULT_TAG).trim();
			if (sucResult.equals(MsgResult.RESULT_SUCCESS)) {

				JSONObject jsonObject = response
						.getJSONObject(MsgResult.RESULT_DATAS_TAG);

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
