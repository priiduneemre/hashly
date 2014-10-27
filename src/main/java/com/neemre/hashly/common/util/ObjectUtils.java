package com.neemre.hashly.common.util;

public class ObjectUtils {

	public static boolean allEqual(Object... items) {
		if(items.length < 2) {
			return true;
		} else {
			Object sampleItem = items[0];
			for(Object item : items) {
				if(!item.equals(sampleItem)) {
					return false;
				}
			}
			return true;
		}
	}
}