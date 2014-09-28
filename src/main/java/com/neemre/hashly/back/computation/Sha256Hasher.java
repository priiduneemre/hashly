package com.neemre.hashly.back.computation;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.neerme.hashly.global.GeneralConst;

public class Sha256Hasher implements Hasher {

	@Override
	public String calculate(byte[] inputData, String encoding) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digester = MessageDigest.getInstance(GeneralConst.ALGORITHM_SHA256);
		digester.update(inputData);
		byte[] digest = digester.digest();

		return Hex.encodeHexString(digest);
	}

	@Override
	public String calculate(String filePath, String encoding)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
}