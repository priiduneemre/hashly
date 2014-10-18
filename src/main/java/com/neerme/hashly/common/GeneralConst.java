package com.neerme.hashly.common;

import java.io.File;

public class GeneralConst {
	
	public static final String ENCODING_ASCII = "ASCII";
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_UTF16 = "UTF-16";
	public static final String ENCODING_UTF32 = "UTF-32";
	
	public static final String ALGORITHM_MD2 = "MD2";
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_SHA1 ="SHA-1";
	public static final String ALGORITHM_SHA224 ="SHA-224";
	public static final String ALGORITHM_SHA256 = "SHA-256";
	public static final String ALGORITHM_SHA384 = "SHA-384";
	public static final String ALGORITHM_SHA512 = "SHA-512";
	
	public static final String TEST_DATA_FILE_PATH = "misc" + File.separator + "test_data" 
			+ File.separator + "lorem_ipsum_20140928.txt";
	
	public static final int END_OF_STREAM_COUNT = -1;
}