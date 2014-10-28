package com.neemre.hashly.common.util;

public class StringUtils {

	public boolean isBlank(String str) {
		if(str != null && !str.trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}