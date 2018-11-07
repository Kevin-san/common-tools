package com.common.tools.date;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ToolDate {
	private Map<Integer, List<Integer>> dateMaps = new LinkedHashMap<>();

	public List<Integer> get(Integer key) {
		return dateMaps.get(key);
	}

	public void put(Integer key, List<Integer> dateList) {
		this.dateMaps.put(key, dateList);
	}

	public Map<Integer, List<Integer>> getDateMaps() {
		return dateMaps;
	}

	public void setDateMaps(Map<Integer, List<Integer>> dateMaps) {
		this.dateMaps = dateMaps;
	}

}
