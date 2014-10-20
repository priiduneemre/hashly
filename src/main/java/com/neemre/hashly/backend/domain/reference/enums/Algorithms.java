package com.neemre.hashly.backend.domain.reference.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum Algorithms {

	MD2("MD2"),
	MD4("MD4"),
	MD5("MD5"),
	SHA1("SHA-1"),
	SHA224("SHA-224"),
	SHA256("SHA-256"),
	SHA384("SHA-384"),
	SHA512("SHA-512");
	
	@Getter 
	@Setter
	private String name;
}