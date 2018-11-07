package com.common.tools.coder;

import java.nio.charset.Charset;
import java.util.Base64;

public class BaseCoder {
	private String str;
	private Charset charset;

	public BaseCoder(String str, Charset charset) {
		this.str = str;
		this.charset = charset;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getBasicEncStr() {
		return Base64.getEncoder().encodeToString(str.getBytes(charset));
	}

	public String getMimeEncStr() {
		return Base64.getMimeEncoder().encodeToString(str.getBytes(charset));
	}

	public String getUrlEncStr() {
		return Base64.getUrlEncoder().encodeToString(str.getBytes(charset));
	}

}
