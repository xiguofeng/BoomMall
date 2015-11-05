package com.plmt.boommall.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.baidu.location.BDLocation;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.utils.UserInfoManager;

public class AccountService extends Service {

	public static final int TIME_UPDATE = 1;

	private Context mContext;

	public static interface LoginCallback {

		void onLoginSuc();
	}

	public static LoginCallback mLoginCallback;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGIN_SUC: {
				if (null != msg.obj) {
					String session = (String) msg.obj;
					UserInfoManager.setSession(mContext, session);
					if (null != mLoginCallback) {
						mLoginCallback.onLoginSuc();
					}
				}

				break;
			}
			case UserLogic.LOGIN_FAIL: {
				break;
			}
			case UserLogic.LOGIN_EXCEPTION: {
				break;
			}

			case UserLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}
		}

	};

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("xxx_login", "onStartCommand");
		flags = START_STICKY;
		UserInfoManager.setUserInfo(mContext);
		User user = new User();
		if (!TextUtils.isEmpty(UserInfoManager.userInfo.getAccount())
				&& !TextUtils.isEmpty(UserInfoManager.userInfo.getPassword())) {
			user.setAccount(UserInfoManager.userInfo.getAccount());
			user.setPassword(UserInfoManager.userInfo.getPassword());
			UserLogic.login(mContext, mHandler, user);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
