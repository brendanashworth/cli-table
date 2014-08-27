package org.lasti.cli_table;

public class TextUtil {
	public static String repeatCh(char ch, int times) {
		StringBuilder outputSb = new StringBuilder();
		for (int i = 0; i < times; i++) 
			outputSb.append(ch);
		return outputSb.toString();
	} // repeatCh
} // class