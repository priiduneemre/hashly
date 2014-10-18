package com.neemre.hashly.back.computation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.neerme.hashly.global.GeneralConst;

public class Md2Generator implements HashGenerator {

	@Override
	public String generate(byte[] inputData) throws NoSuchAlgorithmException {
		MessageDigest digester = MessageDigest.getInstance(GeneralConst.ALGORITHM_MD2);
		digester.update(inputData);
		byte[] digest = digester.digest();
		
		return Hex.encodeHexString(digest);
	}

	@Override
	public String generate(String filePath) throws NoSuchAlgorithmException, IOException {
		InputStream fileInStream = new FileInputStream(new File(filePath));
		MessageDigest digester = MessageDigest.getInstance(GeneralConst.ALGORITHM_MD2);
		DigestInputStream digestInStream = new DigestInputStream(fileInStream, digester);
		
		byte[] buffer = new byte[1024];
		
		while(digestInStream.read(buffer) != GeneralConst.END_OF_STREAM_COUNT);
		digestInStream.close();
		
		byte[] digest = digester.digest();
		return Hex.encodeHexString(digest);
	}
}