package com.locojoy.jmonitor.util;

import java.util.Collection;

public class StringHelper {

	public static <T> String join( Collection<T> cs, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Object s : cs)
			sb.append(s).append(delimiter);
		sb.delete(sb.lastIndexOf(delimiter), sb.length());
		return sb.toString();
	}

}
