package com.neemre.hashly.back.computation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface HashGenerator {
	
	public abstract String generate(byte[] inputData, String encoding) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	public abstract String generate(String filePath, String encoding)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException;
}