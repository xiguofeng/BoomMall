package com.plmt.boommall.config;

import android.os.Environment;

public class Constants {
	/**
	 ******************************************* 参数设置信息开始 ******************************************
	 */

	// 应用名称
	public static String APP_NAME = "boommall";

	// 保存参数文件夹名称
	public static final String SHARED_PREFERENCE_NAME = "boommall_prefs";

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/boommall/";

	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";

	// asset下栏目图片路径
	public static final String IMAGE_COLUMNS_PATH = "image/home_columns";

	// 需要分享的图片
	public static final String SHARE_FILE = BASE_PATH + "QrShareImage.png";

	// 手机IMEI号码
	public static String IMEI = "";

	// 手机号码
	public static String TEL = "";

	// 屏幕高度
	public static int SCREEN_HEIGHT = 800;

	// 屏幕宽度
	public static int SCREEN_WIDTH = 480;

	// 屏幕密度
	public static float SCREEN_DENSITY = 1.5f;

	// 分享成功
	public static final int SHARE_SUCCESS = 0X1000;

	// 分享取消
	public static final int SHARE_CANCEL = 0X2000;

	// 分享失败
	public static final int SHARE_ERROR = 0X3000;

	// 开始执行
	public static final int EXECUTE_LOADING = 0X4000;

	// 正在执行
	public static final int EXECUTE_SUCCESS = 0X5000;

	// 执行完成
	public static final int EXECUTE_FAILED = 0X6000;

	// 加载数据成功
	public static final int LOAD_DATA_SUCCESS = 0X7000;

	// 加载数据失败
	public static final int LOAD_DATA_ERROR = 0X8000;

	// 动态加载数据
	public static final int SET_DATA = 0X9000;

	// 未登录
	public static final int NONE_LOGIN = 0X10000;

	// UTF-8
	public static final String UTF8 = "UTF-8";

	public static final String HTTP_SCHEME = "http://";
	public static final String HTTPS_SCHEME = "https://";
	public static final String FILE_SCHEME = "file://";

	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";
	public static final String LT = "<";
	public static final String GT = ">";

	public static final String TRUE = "true";
	public static final String FALSE = "false";

	public static final String ENCLOSURE_SEPARATOR = "[@]";

	public static final String HTML_QUOT = "&quot;";
	public static final String QUOT = "\"";
	public static final String HTML_APOSTROPHE = "&#39;";
	public static final String APOSTROPHE = "'";
	public static final String AMP = "&";
	public static final String AMP_SG = "&amp;";
	public static final String SLASH = "/";
	public static final String COMMA_SPACE = ", ";

	public static final String FETCH_PICTURE_MODE_NEVER_PRELOAD = "NEVER_PRELOAD";
	public static final String FETCH_PICTURE_MODE_WIFI_ONLY_PRELOAD = "WIFI_ONLY_PRELOAD";
	public static final String FETCH_PICTURE_MODE_ALWAYS_PRELOAD = "ALWAYS_PRELOAD";

	public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
	
}
