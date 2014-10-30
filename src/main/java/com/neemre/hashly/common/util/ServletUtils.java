package com.neemre.hashly.common.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {
	
	public String getRequestUrl(HttpServletRequest req) {
		StringBuffer urlBuilder = req.getRequestURL();
		if(req.getQueryString() != null) {
			urlBuilder.append("?").append(req.getQueryString());
		}
		return urlBuilder.toString();
	}
}
