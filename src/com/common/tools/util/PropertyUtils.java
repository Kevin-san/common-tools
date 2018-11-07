package com.common.tools.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import com.common.tools.ex.ToolsException;

public class PropertyUtils {

	public static <T> void setProperty(String field, Object value, T t) throws ToolsException {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(field, t.getClass());
		propertyDescriptor.setValue(field, value);
	}

	public static <T> Object getProperty(String field, T t) throws ToolsException {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(field, t.getClass());
		return propertyDescriptor.getValue(field);
	}

	public static <T> Class<?> getPropertyReturnType(String field, T t) throws ToolsException {
		return getPropertyDescriptor(field, t.getClass()).getReadMethod().getReturnType();
	}

	public static <T> T createInstance(Class<T> beanClass) throws ToolsException {
		try {
			return beanClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new ToolsException(e);
		}
	}

	public static PropertyDescriptor getPropertyDescriptor(String propertyName, Class<?> beanClass)
			throws ToolsException {
		try {
			return new PropertyDescriptor(propertyName, beanClass);
		} catch (IntrospectionException e) {
			throw new ToolsException(e);
		}
	}

	private PropertyUtils() {
	}

}
