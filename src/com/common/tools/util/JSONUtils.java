package com.common.tools.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.tools.cons.ConstantExp;
import com.common.tools.ex.ToolsException;

public class JSONUtils {
	private JSONUtils() {
	}

	private static Logger logger = Logger.getLogger(JSONUtils.class);

	public static <T> String toJSONString(List<T> list) {
		JSONArray jsonArray = new JSONArray(list);
		return toJSONString(jsonArray);
	}

	public static String toJSONString(Object object) throws JSONException {
		JSONArray jsonArray = new JSONArray(object);
		return toJSONString(jsonArray);
	}

	public static String toJSONString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

	public static String toJSONString(JSONArray jsonArray) {
		return jsonArray.toString();
	}

	public static List<Object> toArrayList(Object object) throws JSONException {
		List<Object> arrayList = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(object);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Object value = jsonObject.get(key);
				arrayList.add(value);
			}
		}
		return arrayList;
	}

	public static List<Map<String, Object>> toList(Object object) throws JSONException {
		List<Map<String, Object>> list = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(object);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Map<String, Object> map = new HashMap<>();
			Iterator<?> iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				Object value = jsonObject.get(key);
				map.put(key, value);
			}
			list.add(map);
		}
		return list;
	}

	public static <T> T toBean(String jsonString, Class<T> beanClass) throws ToolsException {
		try {
			JSONObject jsonObject = getJSONObject(jsonString);
			T t = PropertyUtils.createInstance(beanClass);
			Iterator<?> iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				Object value = jsonObject.get(key);
				PropertyUtils.setProperty(key, value, t);
			}
			return t;
		} catch (JSONException e) {
			logger.error(ConstantExp.JSON_Exception, e);
			throw new ToolsException(e.getMessage());
		}
	}

	private static JSONObject getJSONObject(String jsonString) throws ToolsException {
		try {
			return new JSONObject(jsonString);
		} catch (JSONException e) {
			logger.error(ConstantExp.JSON_Exception, e);
			throw new ToolsException(e.getMessage());
		}
	}
}
