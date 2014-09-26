package com.neemre.hashly.back.calculation.text;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.neemre.hashly.back.calculation.TextHasher;
import com.neerme.hashly.global.GeneralConst;

public class Sha256Hasher extends TextHasher {

	@Override
	public String calculate(String inputString, String encoding) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digester = MessageDigest.getInstance(GeneralConst.ALGORITHM_SHA256);
		digester.update(inputString.getBytes(encoding));
		byte[] digest = digester.digest();
		
		return Hex.encodeHexString(digest);
	}
}