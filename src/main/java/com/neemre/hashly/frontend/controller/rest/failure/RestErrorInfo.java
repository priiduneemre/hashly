package com.neemre.hashly.frontend.controller.rest.failure;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonPropertyOrder({"status", "reason", "url", "code", "message"})
public class RestErrorInfo {
	
	private String status;
	private String reason;
	private String url;
	private String code;
	private String message;
}