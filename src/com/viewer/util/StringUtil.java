package com.viewer.util;

public class StringUtil {
	public static boolean notNullEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	public static boolean notNullEmpty(String... values) {
		for (String s : values) {
			if (s == null || s.isEmpty())
				return false;
		}
		return true;
	}
	
	public static String emptyIfNull(String value) {
		if (value == null) return "";
		return value;
	}
}
