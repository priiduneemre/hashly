package com.neemre.hashly.back.calculation;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public abstract class TextHasher implements Hasher {
	
	public abstract String calculate(String inputString, String encoding) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException;
}		