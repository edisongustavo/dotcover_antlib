package com.egmas.ant.tasks.dotcover.utils;

public class StringUtils {

	/**
	 * Repeats <code>string</code> N times
	 * 
	 * @param string
	 * @param times
	 * @return
	 */
	public static String repeat(String string, int times) {
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < times; i++)
			ss.append(string);
		return ss.toString();
	}

}
