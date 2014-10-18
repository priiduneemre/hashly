package com.neemre.hashly.back.computation;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface HashGenerator {
	
	String generate(byte[] inputData) throws NoSuchAlgorithmException;
	
	String generate(String filePath) throws NoSuchAlgorithmException, IOException;
}