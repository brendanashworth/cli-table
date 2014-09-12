package org.lasti.cli_table;

public class TextUtil {

	/**
	 * Repeats a character ({@link ch}) multiple times until it hits {@link times}.
	 * @param  ch    the character to repeat
	 * @param  times amount of times to repeat said character
	 * @return       concatenated characters in string form
	 */
	public static String repeatCh(char ch, int times) {
		StringBuilder outputSb = new StringBuilder();
		for (int i = 0; i < times; i++) 
			outputSb.append(ch);
		
		return outputSb.toString();
	}
}