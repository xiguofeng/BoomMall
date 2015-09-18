package com.plmt.boommall;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.plmt.boommall.config.Constants;
import com.plmt.boommall.network.volley.RequestQueue;
import com.plmt.boommall.network.volley.toolbox.Volley;
import com.plmt.boommall.utils.image.ImageLoaderConfig;

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
