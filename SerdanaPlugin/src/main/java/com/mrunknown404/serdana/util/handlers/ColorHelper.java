package main.java.com.mrunknown404.serdana.util.handlers;

public class ColorHelper {
	public static String setColors(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '&') {
				if (str.charAt(i) == str.length() - 1) {
				} else if (str.charAt(i + 1) == ' ') {
				} else if (str.charAt(i + 1) == '0') {
					str = str.replace("&0", "\u00a70");
				} else if (str.charAt(i + 1) == '1') {
					str = str.replace("&1", "\u00a71");
				} else if (str.charAt(i + 1) == '2') {
					str = str.replace("&2", "\u00a72");
				} else if (str.charAt(i + 1) == '3') {
					str = str.replace("&3", "\u00a73");
				} else if (str.charAt(i + 1) == '4') {
					str = str.replace("&4", "\u00a74");
				} else if (str.charAt(i + 1) == '5') {
					str = str.replace("&5", "\u00a75");
				} else if (str.charAt(i + 1) == '6') {
					str = str.replace("&6", "\u00a76");
				} else if (str.charAt(i + 1) == '7') {
					str = str.replace("&7", "\u00a77");
				} else if (str.charAt(i + 1) == '8') {
					str = str.replace("&8", "\u00a78");
				} else if (str.charAt(i + 1) == '9') {
					str = str.replace("&9", "\u00a79");
				} else if (str.charAt(i + 1) == 'a') {
					str = str.replace("&a", "\u00a7a");
				} else if (str.charAt(i + 1) == 'b') {
					str = str.replace("&b", "\u00a7b");
				} else if (str.charAt(i + 1) == 'c') {
					str = str.replace("&c", "\u00a7c");
				} else if (str.charAt(i + 1) == 'd') {
					str = str.replace("&d", "\u00a7d");
				} else if (str.charAt(i + 1) == 'e') {
					str = str.replace("&e", "\u00a7e");
				} else if (str.charAt(i + 1) == 'f') {
					str = str.replace("&f", "\u00a7f");
				} else if (str.charAt(i + 1) == 'r') {
					str = str.replace("&r", "\u00a7r");
				} else if (str.charAt(i + 1) == 'l') {
					str = str.replace("&l", "\u00a7l");
				} else if (str.charAt(i + 1) == 'o') {
					str = str.replace("&o", "\u00a7o");
				} else if (str.charAt(i + 1) == 'n') {
					str = str.replace("&n", "\u00a7n");
				} else if (str.charAt(i + 1) == 'm') {
					str = str.replace("&m", "\u00a7m");
				} else if (str.charAt(i + 1) == 'k') {
					str = str.replace("&k", "\u00a7k");
				}
			}
		}
		
		return str;
	}
}
