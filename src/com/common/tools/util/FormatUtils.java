package com.common.tools.util;

import com.common.tools.cons.Constant;

public class FormatUtils {
	public static boolean isOddStartQuota(String dataCell) {
		int quotaCount = 0;
		if (dataCell.charAt(0) == Constant.QUOTA) {
			quotaCount++;
		}
		return CommonUtils.isOddNum(quotaCount);
	}

	public static boolean isOddEndQuota(String dataCell) {
		int quotaCount = 0;
		int lastIndex = dataCell.length() - 1;
		if (dataCell.charAt(lastIndex) == Constant.QUOTA) {
			quotaCount++;
		}
		return CommonUtils.isOddNum(quotaCount);
	}

	public static boolean isOddQuotaCount(String dataLine) {
		int quotaCount = getQuotaCount(dataLine);
		return CommonUtils.isOddNum(quotaCount);
	}

	public static int getQuotaCount(String dataLine) {
		int quotaCount = 0;
		for (int i = 0; i < dataLine.length(); i++) {
			if (dataLine.charAt(i) == Constant.QUOTA) {
				quotaCount++;
			}
		}
		return quotaCount;
	}

	public static boolean isZeroQuotaCount(String dataLine) {
		return getQuotaCount(dataLine) == 0;
	}

	public static boolean isOddQuotaCount(String dataLine, char seperator) {
		int quotaCount = 0;
		for (int i = 0; i < dataLine.length(); i++) {
			if (seperator == dataLine.charAt(i)) {
				quotaCount++;
			}
		}
		return CommonUtils.isOddNum(quotaCount);
	}
}
