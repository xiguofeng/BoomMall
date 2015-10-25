package com.plmt.boommall.network.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import com.plmt.boommall.network.volley.NetworkResponse;
import com.plmt.boommall.network.volley.ParseError;
import com.plmt.boommall.network.volley.Response;
import com.plmt.boommall.network.volley.Response.ErrorListener;
import com.plmt.boommall.network.volley.Response.Listener;
import com.plmt.boommall.network.volley.toolbox.HttpHeaderParser;
import com.plmt.boommall.network.volley.toolbox.JsonObjectRequest;

public class JsonObjectRequestUtf extends JsonObjectRequest {

	public JsonObjectRequestUtf(String url, JSONObject jsonRequest, Listener listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	public JsonObjectRequestUtf(int method, String url, JSONObject jsonRequest, Listener listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

		try {
			JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
			return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (Exception je) {
			return Response.error(new ParseError(je));
		}
	}

}