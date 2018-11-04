package com.common.tools.cons;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	public static final char QUOTA = '"';
	
	public static final String ENTER = "\n\r";
	public static final String TAB = "\t";
	public static final String ENTER_STRING = "\n";
	
	public static final String UNDER_LINE = "_";
	public static final String EMPTY = " ";
	public static final String BLANK = "";
	public static final String SPOT = ".";
	public static final String VERTICAL = "|";
	public static final String STAR = "*";
	public static final String SLANT = "\\\\";
	public static final String  PLUS = "+";
	
	public static final String CONV_SPOT = "\\.";
	public static final String CONV_VERTICAL = "\\|";
	public static final String CONV_STAR= "\\*";
	public static final String CONV_PLUS = "\\+";
	
	public static final Map<String, String> SPEC_MAPS_CONVS = new HashMap<>();
	static {
		SPEC_MAPS_CONVS.put(SPOT, CONV_SPOT);
		SPEC_MAPS_CONVS.put(VERTICAL, CONV_VERTICAL);
		SPEC_MAPS_CONVS.put(STAR, CONV_STAR);
		SPEC_MAPS_CONVS.put(PLUS, CONV_PLUS);
	}
}
