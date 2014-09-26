package com.neemre.hashly;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.neemre.hashly.back.calculation.text.Sha256Hasher;
import com.neerme.hashly.global.GeneralConst;

public class StaticMain {
	
	public static void main (String[] args) {
		String sha256TextHash = null;
		try {
			sha256TextHash = new Sha256Hasher().calculate("blablabla", GeneralConst.ENCODING_UTF8);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println(sha256TextHash);
	}
}