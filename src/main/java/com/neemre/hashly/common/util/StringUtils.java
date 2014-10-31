package com.neemre.hashly.common.util;

import java.util.Collection;
import java.util.Iterator;

import com.neerme.hashly.common.GeneralConst;

public class StringUtils {

	public boolean isBlank(String str) {
		if(str != null && !str.trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public <T> String join(Collection<T> items) {
		StringBuilder result = new StringBuilder();
		Iterator<T> i = items.iterator();
		while(i.hasNext()) {
			T item = i.next();
			if(item == null) {
				result.append(GeneralConst.EMPTY);
			} else {
				result.append(item.toString());
			}
			if(i.hasNext()) {
				result.append(GeneralConst.ITEM_SEPARATOR).append(GeneralConst.WHITESPACE);
			}
		}
		if(result.toString() == null) {
			return GeneralConst.EMPTY;
		} else {
			return result.toString();
		}
	}
}