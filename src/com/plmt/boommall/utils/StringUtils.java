package com.plmt.boommall.utils;

public class StringUtils {

	public static String getCookieValue(String cookie) {
		String cookieValue = "";
		if (cookie.contains("=") && cookie.contains(";")) {
			int index1 = cookie.indexOf("=") + 1;
			int index2 = cookie.indexOf(";");
			cookieValue = cookie.substring(index1, index2);
		}
		return cookieValue;
	}
}
