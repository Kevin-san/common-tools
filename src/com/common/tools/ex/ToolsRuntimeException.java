package com.common.tools.ex;

public class ToolsRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ToolsRuntimeException(Exception e) {
		super(e);
	}
	
	public ToolsRuntimeException(String e) {
		super(e);
	}
}
