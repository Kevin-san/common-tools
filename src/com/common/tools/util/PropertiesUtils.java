package com.common.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import com.common.tools.ex.ToolsException;

public class PropertiesUtils {
	public static Properties loadPropertiesWithSuperPath(String property) throws ToolsException {
		try (InputStream in = new FileInputStream(new File(property));) {
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		} catch (IOException e) {
			throw new ToolsException(e);
		}
	}

	public static Properties loadProperties(String property) throws ToolsException {
		try (InputStream in = Thread.class.getResourceAsStream(property);) {
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		} catch (IOException e) {
			throw new ToolsException(e);
		}
	}

	private PropertiesUtils() {
	}
}
