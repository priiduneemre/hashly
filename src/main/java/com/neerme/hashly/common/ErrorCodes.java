package com.neerme.hashly.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum ErrorCodes {
	
	RECORD_UPDATE_INCORRECT_RESULT_SIZE(1001, "error.data.update.result.size"),
	RECORD_DELETE_INCORRECT_RESULT_SIZE(1002, "error.data.delete.result.size"),
	
	ARGUMENTS_INCOMPATIBLE_ITEM_COUNT(6001, "error.general.arg.itemcount"),
	METHOD_UNEXPECTED_NULL_RESULT(6002, "error.general.result.null");
	
	@Getter
	@Setter
	private int code;
	@Getter
	@Setter
	private String messageKey;
}