package com.common.tools.ex;

public class ToolsException extends Exception {
	private static final long serialVersionUID = 1L;

	public ToolsException(Exception e) {
		super(e);
	}

	public ToolsException(String e) {
		super(e);
	}

}
