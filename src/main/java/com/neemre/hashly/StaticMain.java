package com.neemre.hashly;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.neemre.hashly.back.computation.Md2Hasher;
import com.neemre.hashly.back.computation.Md5Hasher;
import com.neemre.hashly.back.computation.Sha1Hasher;
import com.neemre.hashly.back.computation.Sha256Hasher;
import com.neemre.hashly.back.computation.Sha384Hasher;
import com.neemre.hashly.back.computation.Sha512Hasher;
import com.neerme.hashly.global.GeneralConst;

public class StaticMain {

	public static void main (String[] args) {
		String md2TextHash = null;
		String md5TextHash = null;
		String sha1TextHash = null;
		String sha256TextHash = null;
		String sha384TextHash = null;
		String sha512TextHash = null;

		try {
			md2TextHash = new Md2Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			md5TextHash = new Md5Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha1TextHash = new Sha1Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha256TextHash = new Sha256Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha384TextHash = new Sha384Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha512TextHash = new Sha512Hasher().calculate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("MD2 hash (hex, text): " + md2TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");	
		System.out.println("MD5 hash (hex, text): " + md5TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");	
		System.out.println("SHA-1 hash (hex, text): " + sha1TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-256 hash (hex, text): " + sha256TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-384 hash (hex, text): " + sha384TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-512 hash (hex, text): " + sha512TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
	}
}