package com.neemre.hashly.frontend.controller.rest.failure;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neemre.hashly.common.util.ServletUtils;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public RestErrorInfo handleDefault(HttpServletRequest req, Exception e) {
		String errorUrl = new ServletUtils().getRequestUrl(req);
		String errorMessage = e.getMessage();
		return new RestErrorInfo(null, null, errorUrl, null, errorMessage);
	}
}