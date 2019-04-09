package main.java.com.mrunknown404.serdana.util;

public class ColorHelper {
	public static String setColors(String str) {
		char[] b = str.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == '&' && "0123456789abcdefklmnor".indexOf(b[i + 1]) > -1) {
				b[i] = '\u00A7';
			}
		}
		return new String(b);
		
	}
}
