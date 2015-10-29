package com.plmt.boommall;

import cn.jpush.android.api.JPushInterface;

import com.plmt.boommall.config.Constants;
import com.plmt.boommall.network.volley.RequestQueue;
import com.plmt.boommall.network.volley.toolbox.Volley;
import com.plmt.boommall.push.Easemob;
import com.plmt.boommall.utils.image.ImageLoaderConfig;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class BaseApplication extends Application {

	private static Context context;

	private static RequestQueue sQueue;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		sQueue = Volley.newRequestQueue(getApplicationContext());
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);

		//Easemob.getInstance().init(context);

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(context);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static RequestQueue getInstanceRequestQueue() {
		if (null == sQueue) {
			sQueue = Volley.newRequestQueue(getContext());
		}
		return sQueue;
	}

	public static Context getContext() {
		return context;
	}

}
