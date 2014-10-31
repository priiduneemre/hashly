package com.neemre.hashly.frontend.controller.rest.failure;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.neemre.hashly.common.misc.ResourceWrapper;
import com.neemre.hashly.common.util.ServletUtils;
import com.neemre.hashly.common.util.StringUtils;
import com.neerme.hashly.common.ErrorCodes;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ResourceWrapper resources;
	

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatus status,
			WebRequest req) {
		ResponseEntity<Object> superResponse = super.handleHttpRequestMethodNotSupported(e, 
				headers, status, req);
		String errorMsg = resources.getErrorMessage(ErrorCodes.REQUEST_UNSUPPORTED_METHOD, 
				new Object[]{e.getMethod(), new StringUtils().join(e.getSupportedHttpMethods())});
		RestErrorInfo errorInfo = toRestErrorInfo(ErrorCodes.REQUEST_UNSUPPORTED_METHOD,
				errorMsg, req, superResponse);
		return toResponseEntity(errorInfo, superResponse);
		
	}
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException e, HttpHeaders headers, HttpStatus status,
			WebRequest req) {
		ResponseEntity<Object> superResponse = super.handleHttpMediaTypeNotSupported(e, headers, 
				status, req);
		String errorMsg = resources.getErrorMessage(ErrorCodes.REQUEST_UNSUPPORTED_MEDIA_TYPE, 
				new Object[]{e.getContentType(), new StringUtils().join(e.getSupportedMediaTypes())});
		RestErrorInfo errorInfo = toRestErrorInfo(ErrorCodes.REQUEST_UNSUPPORTED_MEDIA_TYPE,
				errorMsg, req, superResponse);
		return toResponseEntity(errorInfo, superResponse);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, 
			HttpHeaders headers, HttpStatus status, WebRequest req) {
		ResponseEntity<Object> superResponse = super.handleExceptionInternal(e, body, headers,
				status, req);
		if(superResponse.getBody() == null) {
			String errorMsg = resources.getErrorMessage(ErrorCodes.UNSPECIFIED, null);
			RestErrorInfo errorInfo = toRestErrorInfo(ErrorCodes.UNSPECIFIED, errorMsg, req, 
					superResponse);
			return toResponseEntity(errorInfo, superResponse);
		} else {
			return superResponse;
		}
	}
	
	@ExceptionHandler({Exception.class})
	protected ResponseEntity<Object> handleUnspecified(Exception e, WebRequest req) 
			throws Exception {
		System.out.println("böö");
		Class<? extends Throwable>[] t = AnnotationUtils.findAnnotation(getClass()
				.getMethod("handleException", Exception.class, WebRequest.class), ExceptionHandler.class).value();
		for(int i = 0; i < t.length; i++) {
			System.out.printf("%s.-th exception class was: %s;\n", i, t[i].getSimpleName());
		}
		System.out.println("...aga siin sees olin küll...");
		return getDefaultResponse(req);
	}
		
	private <T> RestErrorInfo toRestErrorInfo(ErrorCodes error, String errorMsg, WebRequest req,
			ResponseEntity<T> superResponse) {
		Integer status = superResponse.getStatusCode().value();
		String reason = superResponse.getStatusCode().getReasonPhrase();
		String requestUrl = new ServletUtils().getRequestUrl((HttpServletRequest)req
				.resolveReference("request"));
		RestErrorInfo errorInfo = new RestErrorInfo(status.toString(), reason, requestUrl,
				error.getCode().toString(), errorMsg);
		return errorInfo;
	}
	
	private <T> ResponseEntity<T> toResponseEntity(T body, ResponseEntity<T> superResponse) {
		ResponseEntity<T> response = new ResponseEntity<T>(body, superResponse.getHeaders(), 
				superResponse.getStatusCode());
		return response;
	}
	
	private ResponseEntity<Object> getDefaultResponse(WebRequest req) {
		ResponseEntity<Object> genesisResponse = new ResponseEntity<Object>(null, new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		String errorMsg = resources.getErrorMessage(ErrorCodes.UNSPECIFIED, null);
		RestErrorInfo errorInfo = toRestErrorInfo(ErrorCodes.UNSPECIFIED, errorMsg, req, 
				genesisResponse);
		return toResponseEntity(errorInfo, genesisResponse);
	}
}