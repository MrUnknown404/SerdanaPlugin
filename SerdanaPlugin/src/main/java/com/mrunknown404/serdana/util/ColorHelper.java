package main.java.com.mrunknown404.serdana.util;

public class ColorHelper {
	public static final String ALL_COLOR_CODES = "0123456789abcdef";
	public static final String ALL_MODIFIER_CODES = "klmnor";
	public static final String ALL_CODES = ALL_COLOR_CODES + ALL_MODIFIER_CODES;
	public static final String RAINBOW_COLOR_CODES = "4c6e2ab319d5";
	
	/** Sets the given String's colors
	 * @param str String to modify
	 * @return The given String with colors
	 */
	public static String setColors(String str) {
		char[] b = str.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == '&' && ALL_CODES.indexOf(b[i + 1]) > -1) {
				b[i] = '\u00A7';
			}
		}
		return new String(b);
		
	}
}
