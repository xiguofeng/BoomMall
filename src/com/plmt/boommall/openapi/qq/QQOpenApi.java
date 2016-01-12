package com.plmt.boommall.openapi.qq;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class QQOpenApi {

	private static Context mContext;

	private static Activity mActivity;

	public static String mAppid;

	public static Tencent mTencent;

	private static boolean isServerSideLogin = false;

	public static QQOpenApi sQQOpenApi;

	public static OnCompelete sOnCompelete;

	private UserInfo mInfo;

	// share
	private String title = null;
	private String imageUrl = null;
	private String targetUrl = null;
	private String audioUrl = null;
	private String summary = null;
	private String appName = null;// app名称，用于手Q显示返回
	private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	private int mExtarFlag = 0x00;


	public QQOpenApi() {

	}

	public QQOpenApi(Context context, Activity activity) {

	}

	public static synchronized QQOpenApi getInstance(Context context, Activity activity) {

		if (sQQOpenApi == null) {
			mContext = context;
			mActivity = activity;

			sQQOpenApi = new QQOpenApi();
		}
		return sQQOpenApi;
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		if (TextUtils.isEmpty(mAppid)) {
			mAppid = "222222";
		}
	}

	/**
	 * 创建Tencent
	 * 
	 */
	public Tencent getTencentInstance() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(mAppid, mContext);
		}
		return mTencent;
	}

	/**
	 * 登陆
	 */
	public void login(OnCompelete onCompelete) {
		sOnCompelete = onCompelete;

		if (!mTencent.isSessionValid()) {
			mTencent.login(mActivity, "all", loginListener);
			isServerSideLogin = false;
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
			if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
				mTencent.logout(mContext);
				mTencent.login(mActivity, "all", loginListener);
				isServerSideLogin = false;
				Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
				return;
			}
			mTencent.logout(mContext);
			updateUserInfo();
		}
	}

	
	public Bundle getShareData(){
		final Bundle params = new Bundle();
        if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
        	params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        }
        if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        }
        params.putString(shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE ? QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL
        		: QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
        if (shareType == QQShare.SHARE_TO_QQ_TYPE_AUDIO) {
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);
        }
        return params;
	}
	public void doShareToQQ(final Bundle params) {
		// QQ分享要在主线程做
		ThreadManager.getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (null != mTencent) {
					mTencent.shareToQQ(mActivity, params, qqShareListener);
				}
			}
		});
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
				QQApiUtil.toastMessage(mActivity, "onCancel: ");
			}
		}

		@Override
		public void onComplete(Object response) {
			QQApiUtil.toastMessage(mActivity, "onComplete: " + response.toString());
		}

		@Override
		public void onError(UiError e) {
			QQApiUtil.toastMessage(mActivity, "onError: " + e.errorMessage, "e");
		}
	};

	public static void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
			// TODO
			sOnCompelete.doCompelete();
		} catch (Exception e) {
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {

				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					// TODO
					// mHandler.sendMessage(msg);
					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									bitmap = QQApiUtil.getbitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								// TODO
								// mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {

				}
			};
			mInfo = new UserInfo(mContext, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// TODO
		}
	}

	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			initOpenidAndToken(values);
			updateUserInfo();
		}
	};

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				QQApiUtil.showResultDialog(mContext, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				QQApiUtil.showResultDialog(mContext, "返回为空", "登录失败");
				return;
			}
			QQApiUtil.showResultDialog(mContext, response.toString(), "登录成功");
			// 有奖分享处理
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			QQApiUtil.toastMessage(mActivity, "onError: " + e.errorDetail);
			QQApiUtil.dismissDialog();
		}

		@Override
		public void onCancel() {
			QQApiUtil.toastMessage(mActivity, "onCancel: ");
			QQApiUtil.dismissDialog();
		}
	}

	public interface OnCompelete {
		void doCompelete();
	}
}
