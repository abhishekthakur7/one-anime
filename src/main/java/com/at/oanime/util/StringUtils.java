package com.at.oanime.util;

public class StringUtils {
	
	private StringUtils() {
		super();
	}

	public static String trimString(String input) {
		return input != null ? input.trim() : "";
	}
	
}
