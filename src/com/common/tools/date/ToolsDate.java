package com.common.tools.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.common.tools.util.ToolsArray;

public class ToolsDate {
	private static LinkedHashMap<String, List<Integer>> holidays = new LinkedHashMap<>();
	private static LinkedHashMap<String, List<Integer>> bizdates = new LinkedHashMap<>();
	private static Map<String, ToolDate> allMonthGroupDates = new LinkedHashMap<>();
	private static Map<String, ToolDate> allYearGroupDates = new LinkedHashMap<>();
	private static Map<String, ToolDate> allWeekGroupDates = new LinkedHashMap<>();
	private static Map<String, ToolDate> allQuarterGroupDates = new LinkedHashMap<>();
	private static WeekFields fields = WeekFields.ISO;

	public static void init(String region, List<Integer> holidays) {
		addHolidays(region, holidays);
		loadBizDays(region);
		loadMonthGroupDates(region);
		loadYearGroupDates(region);
		loadWeekGroupDates(region);
		loadQuarterGroupDates(region);
	}

	public static void addHolidays(String region, List<Integer> hollys) {
		if (region == null || hollys.isEmpty()) {
			return;
		}
		List<Integer> holldays = ToolsArray.toUnique(hollys);
		if (holidays.containsKey(region)) {
			holidays.get(region).addAll(holldays);
		} else {
			holidays.put(region, holldays);
		}
	}

	public static void loadBizDays(String region) {
		List<Integer> hollys = holidays.get(region);
		Integer beginHoliday = Collections.min(hollys);
		Integer endHoliday = Collections.max(hollys);
		Integer beginDay = convertToSpecDate(beginHoliday, "0101");
		Integer endDay = convertToSpecDate(endHoliday, "1231");
		long days = getDays(beginDay, endDay);
		if (!bizdates.containsKey(region)) {
			List<Integer> bizDates = new ArrayList<>();
			LocalDate localDate = parse(beginDay);
			for (long i=0; i<days; i++) {
				if (!isHollyDay(region, localDate)) {
					bizDates.add(format(localDate));
				}
				localDate = localDate.plusDays(i);
			}
			bizdates.put(region, bizDates);
		}
	}

	public static void loadMonthGroupDates(String region) {
		List<Integer> bizDates = getBizDays(region);
		ToolDate toolDate = new ToolDate();
		List<Integer> monthKeys = getUniqueMonth(region);
		for (int i = 0; i < monthKeys.size(); i++) {
			String month = String.valueOf(monthKeys.get(i));
			List<Integer> matchDates = bizDates.stream().filter((Integer d) -> month.equals(getMonthString(d)))
					.collect(Collectors.toList());
			toolDate.put(monthKeys.get(i), matchDates);
		}
		allMonthGroupDates.put(region, toolDate);
	}

	public static void loadYearGroupDates(String region) {
		List<Integer> bizDates = getBizDays(region);
		ToolDate toolDate = new ToolDate();
		List<Integer> yearKeys = getUniqueYear(region);
		for (int i = 0; i < yearKeys.size(); i++) {
			String year = String.valueOf(yearKeys.get(i));
			List<Integer> matchDates = bizDates.stream().filter((Integer d) -> year.equals(getYearString(d)))
					.collect(Collectors.toList());
			toolDate.put(yearKeys.get(i), matchDates);
		}
		allYearGroupDates.put(region, toolDate);
	}

	public static void loadQuarterGroupDates(String region) {
		ToolDate date = getMonthDates(region);
		List<Integer> months = getUniqueMonth(region);
		ToolDate newDate = new ToolDate();
		for (int i = 0; i < months.size(); i += 3) {
			Integer monthKey = months.get(i);
			Integer monthKey2 = months.get(i + 1);
			Integer monthKey3 = months.get(i + 2);
			List<Integer> matchDates = date.get(monthKey);
			matchDates.addAll(date.get(monthKey2));
			matchDates.addAll(date.get(monthKey3));
			newDate.put(monthKey, matchDates);
		}
		allQuarterGroupDates.put(region, newDate);
	}

	public static void loadWeekGroupDates(String region) {
		List<Integer> bizDates = getBizDays(region);
		ToolDate toolDate = new ToolDate();
		List<Integer> weekKeys = getUniqueWeek(region);
		for (int i = 0; i < weekKeys.size(); i++) {
			Integer weekKey = weekKeys.get(i);
			String year = getYearString(weekKey);
			Integer week = weekKey % 1000;
			List<Integer> matchDates = bizDates.stream()
					.filter((Integer d) -> year.equals(getYearString(d)) && getWeekOfYear(d) == week)
					.collect(Collectors.toList());
			toolDate.put(weekKey, matchDates);
		}
		allWeekGroupDates.put(region, toolDate);
	}

	public static String getYearString(Integer weekKey) {
		return String.valueOf(weekKey).substring(0, 4);
	}

	public static String getMonthString(Integer weekKey) {
		return String.valueOf(weekKey).substring(0, 6);
	}

	public static Integer withMonthBizDay(String region, String monthStr, int index) {
		return getMonthDate(region, monthStr).get(index - 1);
	}

	public static Integer withYearBizDay(String region, String yearStr, int index) {
		return getYearDate(region, yearStr).get(index - 1);
	}

	public static Integer withWeekBizDay(String region, String dateStr, int index) {
		return getWeekDate(region, dateStr).get(index - 1);
	}

	private static List<Integer> getUniqueWeek(String region) {
		List<Integer> bizDates = getBizDays(region);
		List<Integer> weeks = new ArrayList<>();
		for (int i = 0; i < bizDates.size(); i++) {
			Integer dateInt = bizDates.get(i);
			LocalDate date = parse(dateInt);
			Integer year = dateInt / 10000;
			Integer key = year*10000 + getWeekOfYear(date);
			weeks.add(key);
		}
		return ToolsArray.toUnique(weeks);
	}

	public static List<Integer> getUniqueYear(String region) {
		return getUniqueByIndex(region, 4);
	}

	public static List<Integer> getUniqueMonth(String region) {
		return getUniqueByIndex(region, 6);
	}

	public static List<Integer> getUniqueByIndex(String region, int endIndex) {
		List<Integer> bizDates = getBizDays(region);
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < bizDates.size(); i++) {
			String value = String.valueOf(bizDates.get(i)).substring(0, endIndex);
			list.add(Integer.parseInt(value));
		}
		return ToolsArray.toUnique(list);
	}

	public static List<Integer> getBizDays(String region) {
		return bizdates.get(region);
	}

	public static List<Integer> getHolidays(String region) {
		return holidays.get(region);
	}

	public static ToolDate getMonthDates(String region) {
		return allMonthGroupDates.get(region);
	}

	public static List<Integer> getMonthDate(String region, String month) {
		ToolDate dates = getMonthDates(region);
		return dates.get(Integer.parseInt(month));
	}

	public static ToolDate getYearDates(String region) {
		return allYearGroupDates.get(region);
	}

	public static List<Integer> getYearDate(String region, String year) {
		ToolDate dates = getYearDates(region);
		return dates.get(Integer.parseInt(year));
	}

	public static ToolDate getWeekDates(String region) {
		return allWeekGroupDates.get(region);
	}

	public static List<Integer> getWeekDate(String region, String dateStr) {
		ToolDate date = getWeekDates(region);
		Integer weekKey = Integer.parseInt(dateStr) / 1000 + getWeekOfYear(dateStr);
		return date.get(weekKey);
	}

	public static Integer getWeekOfYear(String dateStr) {
		return getWeekOfYear(parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE));
	}

	public static Integer getWeekOfYear(LocalDate date) {
		return date.get(fields.weekOfYear());
	}

	public static Integer getWeekOfYear(Integer dateInt) {
		return getWeekOfYear(parse(dateInt));
	}

	public static ToolDate getQuarterDates(String region) {
		return allQuarterGroupDates.get(region);
	}

	public static List<Integer> getQuarterDate(String region, String year) {
		ToolDate dates = getYearDates(region);
		return dates.get(Integer.parseInt(year));
	}

	public static long getDays(Integer dateInt) {
		return parse(dateInt).toEpochDay();
	}

	public static long getDays(Integer dateBegin, Integer dateEnd) {
		return getDays(dateEnd) - getDays(dateBegin);
	}

	public static String getDayOfWeek(Integer dateInt) {
		return parse(dateInt).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	}

	public static String getMonthOfYear(Integer dateInt) {
		return parse(dateInt).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
	}

	public static int getDayOfWeekValue(LocalDate date) {
		return date.getDayOfWeek().getValue();
	}

	public static boolean isWeekend(Integer dateInt) {
		return isWeekend(parse(dateInt));
	}

	public static boolean isWeekend(LocalDate date) {
		return getDayOfWeekValue(date) == 6 || getDayOfWeekValue(date) == 7;
	}

	public static boolean isHoliday(String region, Integer dateInt) {
		return isHoliday(region, parse(dateInt));
	}

	public static boolean isHoliday(String region, LocalDate date) {
		if (region == null || !holidays.containsKey(region)) {
			return false;
		}
		return holidays.get(region).contains(format(date));
	}

	public static Integer rollDays(String region, Integer dateInt, Integer rollDays) {
		LocalDate date = parse(dateInt);
		int count = 0;
		while (Math.abs(rollDays) != count) {
			date = plusDays(date, rollDays);
			if (isHollyDay(region, date)) {
				continue;
			}
			count++;
		}
		return format(date);
	}

	private static boolean isHollyDay(String region, LocalDate date) {
		return isHoliday(region, date) || isWeekend(date);
	}

	public static Integer getNextBizDate(String region, Integer dateInt) {
		return rollDays(region, dateInt, 1);
	}

	public static Integer getPrevBizDate(String region, Integer dateInt) {
		return rollDays(region, dateInt, -1);
	}

	public static Integer getFirstBizDateOfMonth(String region, Integer dateInt) {
		LocalDate firstDate = firstDayOfMonth(dateInt);
		if (isHollyDay(region, firstDate)) {
			return format(firstDate);
		}
		return getNextBizDate(region, format(firstDate));
	}

	public static Integer getLastBizDateOfMonth(String region, Integer dateInt) {
		LocalDate lastDate = lastDayOfMonth(dateInt);
		if (isHollyDay(region, lastDate)) {
			return format(lastDate);
		}
		return getPrevBizDate(region, format(lastDate));
	}

	public static LocalDate plusDays(LocalDate date, Integer rollDays) {
		return rollDays > 0 ? date.plusDays(1) : date.plusDays(-1);
	}

	public static boolean isLeapYear(Integer dateInt) {
		return parse(dateInt).isLeapYear();
	}

	public static LocalDate firstDayOfMonth(Integer dateInt) {
		return parse(dateInt).with(TemporalAdjusters.firstDayOfMonth());
	}

	public static LocalDate lastDayOfMonth(Integer dateInt) {
		return parse(dateInt).with(TemporalAdjusters.lastDayOfMonth());
	}

	public static LocalDate withMonthDay(LocalDate date, int day) {
		return date.withDayOfMonth(day);
	}

	private ToolsDate() {
	}

	public static Integer convertToSpecDate(Integer orgDate, String date) {
		return Integer.parseInt(String.valueOf(orgDate).substring(0, 4) + date);
	}

	public static LocalDate parse(String dateStr, String pattern) {
		return parse(dateStr, DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDate parse(Integer dateInt) {
		return parse(String.valueOf(dateInt), DateTimeFormatter.BASIC_ISO_DATE);
	}

	public static LocalDate parse(String dateStr, DateTimeFormatter formatter) {
		return LocalDate.parse(dateStr, formatter);
	}

	public static String format(Integer dateInt, String pattern) {
		return parse(dateInt).format(DateTimeFormatter.ofPattern(pattern));
	}

	public static Integer format(LocalDate date) {
		return Integer.parseInt(formatDate(date));
	}

	public static String formatDate(LocalDate date, DateTimeFormatter formatter) {
		return date.format(formatter);
	}

	public static String formatDate(LocalDate date) {
		return formatDate(date, DateTimeFormatter.BASIC_ISO_DATE);
	}

	public static String format(LocalDate date, String pattern) {
		return formatDate(date, DateTimeFormatter.ofPattern(pattern));
	}

	public static String format(String dateStr, String srcPattern, String targetPattern) {
		return parse(dateStr, srcPattern).format(DateTimeFormatter.ofPattern(targetPattern));
	}
	
	public static String nowTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
	}
}
