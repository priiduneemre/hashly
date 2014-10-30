package com.neemre.hashly.frontend.controller.rest.failure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.neemre.hashly.common.misc.ResourceWrapper;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ResourceWrapper resources;

}