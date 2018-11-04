package com.common.tools.util;

import java.util.LinkedHashSet;
import java.util.List;

public class ToolsArray {
	public static <T> List<T> toUnique(List<T> list) {
		LinkedHashSet<T> set = new LinkedHashSet<>(list.size());
		set.addAll(list);
		list.clear();
		list.addAll(set);
		return list;
	}

	private ToolsArray() {
	}
}
