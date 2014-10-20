package com.neemre.hashly.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.neemre.hashly.backend.computation.Md2Generator;
import com.neemre.hashly.backend.computation.Md5Generator;
import com.neemre.hashly.backend.computation.Sha1Generator;
import com.neemre.hashly.backend.computation.Sha256Generator;
import com.neemre.hashly.backend.computation.Sha384Generator;
import com.neemre.hashly.backend.computation.Sha512Generator;
import com.neerme.hashly.common.Encodings;
import com.neerme.hashly.common.GeneralConst;

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
					Encodings.UTF8.getName()));
			md5TextHash = new Md5Generator().generate(new String("blablabla").getBytes(
					Encodings.UTF8.getName()));
			sha1TextHash = new Sha1Generator().generate(new String("blablabla").getBytes(
					Encodings.UTF8.getName()));
			sha256TextHash = new Sha256Generator().generate(new String("blablabla").getBytes(
					Encodings.UTF8.getName()));
			sha384TextHash = new Sha384Generator().generate(new String("blablabla").getBytes(
					Encodings.UTF8.getName()));
			sha512TextHash = new Sha512Generator().generate(new String("blablabla").getBytes(
					Encodings.UTF8.getName()));
			
			md2FileHash = new Md2Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
			md5FileHash = new Md5Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
			sha1FileHash = new Sha1Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
			sha256FileHash = new Sha256Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
			sha384FileHash = new Sha384Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
			sha512FileHash = new Sha512Generator().generate(GeneralConst.TEST_DATA_FILE_PATH);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Oopsie, something went wrong: " + e.getMessage());
		}

		System.out.println("MD2 hash (hex, text): " + md2TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");	
		System.out.println("MD5 hash (hex, text): " + md5TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");	
		System.out.println("SHA-1 hash (hex, text): " + sha1TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-256 hash (hex, text): " + sha256TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-384 hash (hex, text): " + sha384TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-512 hash (hex, text): " + sha512TextHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")\n");
		
		System.out.println("MD2 hash (hex, file): " + md2FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("MD5 hash (hex, file): " + md5FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");	
		System.out.println("SHA-1 hash (hex, file): " + sha1FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-256 hash (hex, file): " + sha256FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-384 hash (hex, file): " + sha384FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")");
		System.out.println("SHA-512 hash (hex, file): " + sha512FileHash + " (encoding: \"" + Encodings.UTF8.getName() + "\")\n");
	}
}