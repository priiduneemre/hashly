package com.neemre.hashly.util;

import java.io.PrintStream;

public class ByteUtils {

	public void printAsHex(byte[] srcData, PrintStream targetStream) {
		for(int i = 0; i < srcData.length; i++) {
			targetStream.printf("0x%02X", srcData[i]);
		}
		System.out.println();
	}
}