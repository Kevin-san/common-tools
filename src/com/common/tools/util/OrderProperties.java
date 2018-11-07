package com.common.tools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.common.tools.ex.ToolsException;

public class OrderProperties {
	private Map<String, List<String>> keyLists = new HashMap<>();
	private Map<String, Set<String>> keySet = new HashMap<>();
	private Map<String, Map<String, String>> keyMaps = new HashMap<>();
	private List<String> lines = new ArrayList<>();
	private BufferedReader reader;

	public void readLines() throws ToolsException {
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			throw new ToolsException(e);
		}
	}

	public OrderProperties(String path) throws ToolsException {
		try (InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream(path);) {
			this.reader = new BufferedReader(new InputStreamReader(inputStream));
			readLines();
			generate2KeyLists(lines);
			generate2KeyMaps(lines);
		} catch (IOException e) {
			throw new ToolsException(e);
		}

	}

	public void generate2KeyLists(List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (isValid(line)) {
				continue;
			}
			String[] keyValue = line.split("=", 2);
			String key = keyValue[0];
			String value = keyValue[1];
			String[] results = value.split(",");
			if (keyLists.containsKey(key)) {
				keyLists.get(key).add(value);
				keySet.get(key).add(results[1]);
			} else {
				keyLists.put(key, new ArrayList<String>());
				keyLists.get(key).add(value);
				keySet.put(key, new HashSet<>());
				keySet.get(key).add(results[1]);
			}
		}
	}

	private boolean isValid(String line) {
		return "".equals(line) || line.contains("=");
	}

	public void generate2KeyMaps(List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (isValid(line)) {
				continue;
			}
			String[] keyValue = line.split("=", 2);
			String key = keyValue[0];
			String value = keyValue[1];
			String[] valueMaps = value.split(",", 2);
			if (keyMaps.containsKey(key)) {
				keyMaps.get(key).put(valueMaps[0], valueMaps[1]);
			} else {
				keyMaps.put(key, new HashMap<>());
				keyMaps.get(key).put(valueMaps[0], valueMaps[1]);
			}
		}
	}

	public Map<String, List<String>> getKeyLists() {
		return keyLists;
	}

	public List<String> getKeyListsKey(String key) {
		return keyLists.get(key);
	}

	public Map<String, Map<String, String>> getKeyMaps() {
		return keyMaps;
	}

	public Map<String, String> getKeyMapsKey(String key) {
		return keyMaps.get(key);
	}

	public List<String> getLines() {
		return lines;
	}

	public static void main(String[] args) throws ToolsException {
		OrderProperties prop = new OrderProperties("/common/types/types.properties");
		System.out.println(prop.keyMaps);
	}
}
