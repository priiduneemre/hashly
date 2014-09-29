package com.neemre.hashly;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.neemre.hashly.back.computation.Md2Generator;
import com.neemre.hashly.back.computation.Md5Generator;
import com.neemre.hashly.back.computation.Sha1Generator;
import com.neemre.hashly.back.computation.Sha256Generator;
import com.neemre.hashly.back.computation.Sha384Generator;
import com.neemre.hashly.back.computation.Sha512Generator;
import com.neerme.hashly.global.GeneralConst;

public class ExampleMain {

	public static void main (String[] args) {
		String md2TextHash = null;
		String md5TextHash = null;
		String sha1TextHash = null;
		String sha256TextHash = null;
		String sha384TextHash = null;
		String sha512TextHash = null;

		String md2FileHash = null;
		String md5FileHash = null;
		String sha1FileHash = null;
		String sha256FileHash = null;
		String sha384FileHash = null;
		String sha512FileHash = null;
		
		try {
			md2TextHash = new Md2Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			md5TextHash = new Md5Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha1TextHash = new Sha1Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha256TextHash = new Sha256Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha384TextHash = new Sha384Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			sha512TextHash = new Sha512Generator().generate(new String("blablabla").getBytes(
					GeneralConst.ENCODING_UTF8), GeneralConst.ENCODING_UTF8);
			
			md2FileHash = new Md2Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);
			md5FileHash = new Md5Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);
			sha1FileHash = new Sha1Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);
			sha256FileHash = new Sha256Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);
			sha384FileHash = new Sha384Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);
			sha512FileHash = new Sha512Generator().generate(GeneralConst.TEST_DATA_FILE_PATH, 
					GeneralConst.ENCODING_UTF8);			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		}

		System.out.println("MD2 hash (hex, text): " + md2TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");	
		System.out.println("MD5 hash (hex, text): " + md5TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");	
		System.out.println("SHA-1 hash (hex, text): " + sha1TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-256 hash (hex, text): " + sha256TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-384 hash (hex, text): " + sha384TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-512 hash (hex, text): " + sha512TextHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")\n");
		
		System.out.println("MD2 hash (hex, file): " + md2FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("MD5 hash (hex, file): " + md5FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");	
		System.out.println("SHA-1 hash (hex, file): " + sha1FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-256 hash (hex, file): " + sha256FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-384 hash (hex, file): " + sha384FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")");
		System.out.println("SHA-512 hash (hex, file): " + sha512FileHash + " (encoding: \"" + GeneralConst.ENCODING_UTF8 + "\")\n");
	}
}