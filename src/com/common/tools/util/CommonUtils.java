package com.common.tools.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.common.tools.cons.Constant;
import com.common.tools.ex.ToolsException;


public class CommonUtils {

	public static boolean isOddNum(int quotaCount) {
		return quotaCount % 2 == 1;
	}

	public static String toFirstUppder(String value) {
		StringBuilder sb = new StringBuilder();
		String firstValue = value.substring(0, 1).toUpperCase();
		sb.append(firstValue);
		sb.append(value.substring(1));
		return sb.toString();
	}
	
	public static String getFileSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(Constant.SPOT)+1);
	}
	public static String toFirstLower(String value) {
		StringBuilder sb = new StringBuilder();
		String firstValue = value.substring(0, 1).toLowerCase();
		sb.append(firstValue);
		sb.append(value.substring(1));
		return sb.toString();
	}

	public static boolean isIntNumeric(String value) throws ToolsException {
		if (StringUtils.isEmpty(value)) {
			throw new ToolsException("value is not empty or null");
		}
		if (!NumberUtils.isNumber(value)) {
			throw new ToolsException("value is not numberFormat.");
		}
		Double d = Double.valueOf(value);
		return Math.floor(d) == d;
	}

}
