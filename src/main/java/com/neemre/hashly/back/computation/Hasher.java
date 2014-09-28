package com.neemre.hashly.back.computation;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface Hasher {
	
	public abstract String calculate(byte[] inputData, String encoding) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public abstract String calculate(String filePath, String encoding)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;
}