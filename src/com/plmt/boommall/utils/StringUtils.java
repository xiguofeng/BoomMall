package com.plmt.boommall.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/*-----------------------------------
	
	笨方法：String s = "你要去除的字符串";
	
	      1.去除空格：s = s.replace('\\s','');
	
	      2.去除回车：s = s.replace('\n','');
	
	这样也可以把空格和回车去掉，其他也可以照这样做。
	
	注：\n 回车(\u000a) 
	\t 水平制表符(\u0009) 
	\s 空格(\u0008) 
	\r 换行(\u000d)*/
}
