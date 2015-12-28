package com.plmt.boommall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.plmt.boommall.entity.User;

/**
 * 用户信息管理类
 */
public class UserInfoManager {

	public static final String USER_INFO_PREFERNCE_KEY = "userinfo";

	public static final String USER_FIRST_USE_KEY = "ISFIRSTUSE";

	public static final String USER_ID_KEY = "id";

	public static final String USER_NAME_KEY = "name";

	public static final String USER_ACCOUNT_KEY = "account";

	public static final String USER_PWD_KEY = "password";

	public static final String USER_SEX_KEY = "sex";

	public static final String USER_PHONE_KEY = "phone";

	public static final String USER_PHOTO_KEY = "photo";

	public static final String USER_IS_AUTHENTICATION = "is_authentication";

	public static final String USER_ADDRESS_KEY = "address";

	public static final String USER_INVOICE_KEY = "invoice";

	public static final String USER_SIGNATURE_KEY = "signature";

	public static final String USER_LAST_LOGINIP_KEY = "last_login_ip";

	public static final String USER_REMEMBER_PSW = "is_remember_pwd";

	public static final String USER_LOGIN_IN = "is_login_in";

	public static final String USER_LOGIN_IN_IS_AUTO = "is_login_in_auto";

	public static final String USER_GESTURUE_PWD = "gesturue_pwd";

	public static final String USER_GESTURUE_PWD_IS_HAS = "is_has_gesturue_pwd";

	public static final String USER_GESTURUE_PWD_IS_OPEN = "is_open_gesturue_pwd";

	public static final String USER_AUTH_IS_MUST = "is_must_auth";

	public static final String USER_SESSION = "user_session";
	
	public static final String IS_FIRST_USE = "is_first_use";

	public static User userInfo = new User();

	/**
	 * 保存用户登录返回信息
	 * 
	 * @param context
	 */
	public static void setUserInfo(Context context) {
		SharedPreferences userInfoPreferences = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		UserInfoManager.userInfo.setId(userInfoPreferences.getString(
				USER_ID_KEY, ""));
		UserInfoManager.userInfo.setUsername(userInfoPreferences.getString(
				USER_NAME_KEY, ""));

		UserInfoManager.userInfo.setUser_photo(userInfoPreferences.getString(
				USER_PHOTO_KEY, ""));
		UserInfoManager.userInfo.setIs_authentication(userInfoPreferences
				.getString(USER_IS_AUTHENTICATION, ""));

		UserInfoManager.userInfo.setAccount(userInfoPreferences.getString(
				USER_ACCOUNT_KEY, ""));
		UserInfoManager.userInfo.setPassword(userInfoPreferences.getString(
				USER_PWD_KEY, ""));

		UserInfoManager.userInfo.setSex(userInfoPreferences.getString(
				USER_SEX_KEY, ""));
		UserInfoManager.userInfo.setSignature(userInfoPreferences.getString(
				USER_SIGNATURE_KEY, ""));
	}

	/**
	 * 保存用户登录返回信息到配置文件
	 * 
	 * @param context
	 * @param userName
	 * @param userPass
	 * @param user
	 *            登录成功后，服务器返回的用户信息
	 */
	public static void saveUserInfo(Context context, User user) {
		if (null != user) {
			SharedPreferences.Editor userInfoSp = context.getSharedPreferences(
					USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

			userInfoSp.putString(USER_ID_KEY,
					null == user.getId() ? "" : user.getId());
			userInfoSp.putString(USER_NAME_KEY, user.getUsername());

			userInfoSp.putString(USER_PHOTO_KEY, user.getUser_photo());
			userInfoSp.putString(USER_IS_AUTHENTICATION, user.getIs_authentication());

			userInfoSp.putString(USER_ACCOUNT_KEY, user.getAccount());
			userInfoSp.putString(USER_PWD_KEY, user.getPassword());

			userInfoSp.putString(USER_SEX_KEY, null == user.getSex() ? ""
					: user.getSex());
			userInfoSp.putString(USER_SIGNATURE_KEY,
					null == user.getSignature() ? "" : user.getSignature());

			userInfoSp.commit();
		}

	}

	public static void clearUserInfo(Context context) {
		SharedPreferences.Editor userInfoSp = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		userInfoSp.putString(USER_ID_KEY, "");
		userInfoSp.putString(USER_NAME_KEY, "");

		userInfoSp.putString(USER_PHOTO_KEY, "");
		userInfoSp.putString(USER_IS_AUTHENTICATION, "");

		userInfoSp.putString(USER_ACCOUNT_KEY, "");
		userInfoSp.putString(USER_PWD_KEY, "");

		userInfoSp.putString(USER_SEX_KEY, "");
		userInfoSp.putString(USER_SIGNATURE_KEY, "");

		userInfoSp.putBoolean(USER_LOGIN_IN_IS_AUTO, false);
		userInfoSp.putBoolean(USER_REMEMBER_PSW, false);
		userInfoSp.commit();

		UserInfoManager.userInfo.setId("");

		UserInfoManager.userInfo.setUsername("");
		UserInfoManager.userInfo.setAccount("");
		UserInfoManager.userInfo.setPassword("");
		UserInfoManager.userInfo.setUser_photo("");
		UserInfoManager.userInfo.setSex("");
		UserInfoManager.userInfo.setSignature("");
		UserInfoManager.userInfo.setTimestamp("");
	}

	/**
	 * 保存是否记住密码到配置文件
	 * 
	 * @param context
	 * @param isRemember
	 *            是否记住密码
	 */
	public static void setRememberPwd(Context context, Boolean isRemember) {

		SharedPreferences.Editor userInfoSp = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		userInfoSp.putBoolean(USER_REMEMBER_PSW, isRemember);
		userInfoSp.commit();

	}

	/**
	 * 从配置文件获取是否记住密码
	 * 
	 * @param context
	 * @param USER_REMEMBER_PWD
	 *            是否记住密码
	 */
	public static boolean getRememberPwd(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_REMEMBER_PSW, false);

	}

	/**
	 * 保存是否自动登录
	 * 
	 * @param context
	 * @param isAutoLoginIn
	 */
	public static void setLoginInAuto(Context context, Boolean isAutoLoginIn) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		userInfo.putBoolean(USER_LOGIN_IN_IS_AUTO, isAutoLoginIn);
		userInfo.commit();
	}

	public static boolean getLoginInAuto(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_LOGIN_IN_IS_AUTO, false);
	}

	/**
	 * 保存登录状态到配置文件
	 * 
	 * @param context
	 * @param isLoginIn
	 *            是否登录
	 */

	public static void setLoginIn(Context context, Boolean isLoginIn) {

		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		userInfo.putBoolean(USER_LOGIN_IN, isLoginIn);
		userInfo.commit();
	}

	/**
	 * 从配置文件获取登录状态
	 * 
	 * @param context
	 * @param isLoginIn
	 *            是否登录
	 */
	public static boolean getLoginIn(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_LOGIN_IN, false);

	}

	/**
	 * 保存手势密码
	 * 
	 * @param context
	 * @param pwdStr
	 *            手势密码
	 */
	public static void setGesturuePwd(Context context, String pwdStr) {

		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		if (!TextUtils.isEmpty(pwdStr)) {
			userInfo.putBoolean(USER_GESTURUE_PWD_IS_HAS, true);
			userInfo.putString(USER_GESTURUE_PWD, pwdStr);
		} else {
			userInfo.putBoolean(USER_GESTURUE_PWD_IS_HAS, false);
			userInfo.putString(USER_GESTURUE_PWD, "");
		}
		userInfo.commit();

	}

	/**
	 * 加入手势密码关闭功能
	 * 
	 * @param context
	 * @param isClose
	 */
	public static void setOpenOrCloseGesturuePwd(Context context,
			boolean isClose) {

		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		userInfo.putBoolean(USER_GESTURUE_PWD_IS_OPEN, isClose);

		userInfo.commit();
	}

	public static boolean getIsOpenGesturuePwd(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_GESTURUE_PWD_IS_OPEN, true);
	}

	/**
	 * 从配置文件获取是有手势密码
	 * 
	 * @param context
	 * @param USER_GESTURUE_PSW_IS_HAS
	 *            是否有手势密码
	 */
	public static boolean getIsHasGesturuePwd(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_GESTURUE_PWD_IS_HAS, false);

	}

	/**
	 * 从配置文件获取是有手势密码
	 * 
	 * @param context
	 * @param USER_GESTURUE_PSW_IS_HAS
	 *            是否有手势密码
	 */
	public static String getGesturuePwd(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getString(USER_GESTURUE_PWD, "");

	}

	public static boolean getIsMustAuth(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(USER_AUTH_IS_MUST, true);

	}

	public static void setIsMustAuth(Context context, boolean isMust) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();
		userInfo.putBoolean(USER_AUTH_IS_MUST, isMust);
		userInfo.commit();
	}

	public static String getPhone(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);
		return userInfo.getString(USER_PHONE_KEY, "");

	}

	public static void setPhone(Context context, String phone) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		if (!TextUtils.isEmpty(phone)) {
			userInfo.putString(USER_PHONE_KEY, phone);
		} else {
			userInfo.putString(USER_PHONE_KEY, "");
		}
		userInfo.commit();

	}

	public static String getSession(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);
		return userInfo.getString(USER_SESSION, "");

	}

	public static void setSession(Context context, String session) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		if (!TextUtils.isEmpty(session)) {
			userInfo.putString(USER_SESSION, session);
		} else {
			userInfo.putString(USER_SESSION, "");
		}
		userInfo.commit();

	}

	public static String getAddress(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);
		return userInfo.getString(USER_ADDRESS_KEY, "");

	}

	public static void setAddress(Context context, String address) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();

		if (!TextUtils.isEmpty(address)) {
			userInfo.putString(USER_ADDRESS_KEY, address);
		} else {
			userInfo.putString(USER_ADDRESS_KEY, "");
		}
		userInfo.commit();

	}

	public static void saveAddress(Context context, String address) {
		String tempString = "";
		String string = getAddressHistory(context);
		if (!TextUtils.isEmpty(string)) {
			String[] strings = string.substring(0, string.length()).split(";");
			int size = 5;
			if (strings.length < 5) {
				size = strings.length;
			}
			for (int i = 0; i < size; i++) {
				if (!address.equals(strings[i])) {
					if (!TextUtils.isEmpty(tempString)) {
						tempString = tempString + ";" + strings[i];
					} else {
						tempString = strings[i];
					}
				}
			}
		}
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();
		if (!TextUtils.isEmpty(tempString)) {
			userInfo.putString(USER_ADDRESS_KEY, address + ";" + tempString);
		} else {
			userInfo.putString(USER_ADDRESS_KEY, address);
		}
		userInfo.commit();
	}

	public static String getAddressHistory(Context context) {
		String result = "";
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);
		result = userInfo.getString(USER_ADDRESS_KEY, "");

		return result;
	}

	public static boolean isHasAddress(Context context, String address) {
		boolean isHas = false;
		String string = getAddressHistory(context);
		String[] strings = string.substring(0, string.length() - 1).split(";");
		for (String s : strings) {
			if (address.equals(s)) {
				isHas = true;
				break;
			}
		}
		return isHas;
	}

	public static void saveInvoice(Context context, String invoice) {
		String tempString = "";
		String string = getInvoiceHistory(context);
		if (!TextUtils.isEmpty(string)) {
			String[] strings = string.substring(0, string.length()).split(";");
			int size = 5;
			if (strings.length < 5) {
				size = strings.length;
			}
			for (int i = 0; i < size; i++) {
				if (!invoice.equals(strings[i])) {
					if (!TextUtils.isEmpty(tempString)) {
						tempString = tempString + ";" + strings[i];
					} else {
						tempString = strings[i];
					}
				}
			}
		}
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();
		if (!TextUtils.isEmpty(tempString)) {
			userInfo.putString(USER_INVOICE_KEY, invoice + ";" + tempString);
		} else {
			userInfo.putString(USER_INVOICE_KEY, invoice);
		}
		userInfo.commit();
	}

	public static String getInvoiceHistory(Context context) {
		String result = "";
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);
		result = userInfo.getString(USER_INVOICE_KEY, "");

		return result;
	}
	
	
	public static boolean getIsFirstUse(Context context) {
		SharedPreferences userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE);

		return userInfo.getBoolean(IS_FIRST_USE, true);

	}

	public static void setIsFirstUse(Context context, boolean isFirstUse) {
		SharedPreferences.Editor userInfo = context.getSharedPreferences(
				USER_INFO_PREFERNCE_KEY, Context.MODE_PRIVATE).edit();
		userInfo.putBoolean(IS_FIRST_USE, isFirstUse);
		userInfo.commit();
	}
}
