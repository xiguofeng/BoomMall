package com.plmt.boommall.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class JsonUtils {
	public static Object simpleJsonToObject(String jsonString, Class<?> c) {
		try {
			JSONObject obj = new JSONObject(jsonString);
			return toObject(obj, c);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object toObject(JSONObject jobj, Class<?> c) {
		if (c == null || jobj == null) {
			return null;
		}
		try {
			Object obj = c.newInstance();
			Field[] fields = c.getFields();
			for (Field f : fields) {
				String className = f.getType().getName();
				if (className.equals("int")
						|| className.equals(Integer.class.getName())) {
					f.setInt(obj, jobj.optInt(f.getName()));
				} else if (className.equals("long")
						|| className.equals(Long.class.getName())) {
					f.setLong(obj, jobj.optLong(f.getName()));
				} else if (className.equals("double")
						|| className.equals(Double.class.getName())) {
					f.setDouble(obj, jobj.optDouble(f.getName()));
				} else if (className.equals("boolean")
						|| className.equals(Boolean.class.getName())) {
					f.setBoolean(obj, jobj.optBoolean(f.getName()));
				} else if (className.equals(String.class.getName())) {
					String s = jobj.optString(f.getName());
					if (!TextUtils.isEmpty(s)) {
						f.set(obj, s);
					}
				} else if (className.startsWith("[L")) {
					JSONArray array = jobj.optJSONArray(f.getName());
					if (array != null && array.length() > 0) {
						Class<?> innerClass = Class.forName(className
								.substring(2, className.length() - 1));
						Object[] objArray = (Object[]) Array.newInstance(
								innerClass, array.length());
						for (int i = 0; i < array.length(); i++) {
							if ((array.get(i) instanceof String)
									|| (array.get(i) instanceof Long)
									|| (array.get(i) instanceof Double)
									|| (array.get(i) instanceof Boolean)) {

								objArray[i] = array.get(i);
							} else {
								if (objArray == null) {
									objArray = new Object[array.length()];
								}
								Object innerObj = toObject(
										array.getJSONObject(i), innerClass);

								if (innerObj != null) {
									objArray[i] = innerObj;
								}
							}
						}
						f.set(obj, objArray);
					}
				} else {
					JSONObject json = jobj.optJSONObject(f.getName());
					if (json != null) {
						Object innerObj = toObject(json,
								Class.forName(f.getType().getName()));
						if (innerObj != null) {
							f.set(obj, innerObj);
						}
					}

				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object fromJsonToJava(JSONObject json, Class pojo) {
		// 首先得到pojo所定义的字段
		Field[] fields = pojo.getDeclaredFields();
		// 根据传入的Class动态生成pojo对象
		Object obj = null;
		try {
			obj = pojo.newInstance();

			for (Field field : fields) {
				// 设置字段可访问（必须，否则报错）
				field.setAccessible(true);
				// 得到字段的属性名
				String name = field.getName();
				// 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
				try {
					json.get(name);
				} catch (Exception ex) {
					continue;
				}
				if (json.get(name) != null && !"".equals(json.getString(name))) {
					// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
					if (field.getType().equals(Long.class)
							|| field.getType().equals(long.class)) {
						field.set(obj, Long.parseLong(json.getString(name)));
					} else if (field.getType().equals(String.class)) {
						field.set(obj, json.getString(name));
					} else if (field.getType().equals(Double.class)
							|| field.getType().equals(double.class)) {
						field.set(obj, Double.parseDouble(json.getString(name)));
					} else if (field.getType().equals(Integer.class)
							|| field.getType().equals(int.class)) {
						field.set(obj, Integer.parseInt(json.getString(name)));
					} else if (field.getType().equals(java.util.Date.class)) {
						field.set(obj, Date.parse(json.getString(name)));
					} else {
						continue;
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj;
	}

	public static final String removeBOM(String data) {
		if (TextUtils.isEmpty(data)) {
			return data;
		}

		if (data.startsWith("\ufeff")) {
			return data.substring(1);
		} else {
			return data;
		}
	}

}
