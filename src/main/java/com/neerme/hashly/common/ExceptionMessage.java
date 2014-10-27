package com.neerme.hashly.common;

public class ExceptionMessage {

	public static final String RECORD_UPDATE_INCORRECT_RESULT_SIZE = "Expected %d record(s) (of " 
			+ "type \"%s\") to be updated in \"%s\", but instead got: %d records.";
	public static final String RECORD_DELETE_INCORRECT_RESULT_SIZE = "Expected %d record(s) (of " 
			+ "type \"%s\") to be deleted in \"%s\", but instead got: %d records.";

	public static final String ARGUMENTS_INCOMPATIBLE_ITEM_COUNT = "Incompatible (unequal) item "
			+ "count for one or more pairs of [%s]!";
	public static final String METHOD_UNEXPECTED_NULL_RESULT = "The executing method (%s) "
			+ "attempted to return a 'null' result where a non-null value was expected!";
}