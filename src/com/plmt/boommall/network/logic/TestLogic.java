package com.plmt.boommall.network.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.plmt.boommall.BaseApplication;
import com.plmt.boommall.network.utils.CookieRequest;
import com.plmt.boommall.network.volley.Request.Method;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.utils.UserInfoManager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class TestLogic {

	public static final int NET_ERROR = 0;

	public static final int TEST_SUC = NET_ERROR + 1;

	public static final int TEST_FAIL = TEST_SUC + 1;

	public static final int TEST_EXCEPTION = TEST_FAIL + 1;

	

}
