package com.neerme.hashly.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum Encodings {

	ASCII("ASCII"),
	UTF8("UTF-8"),
	UTF16("UTF-16"),
	UTF32("UTF-32");
	
	@Getter
	@Setter
	private String name;
}