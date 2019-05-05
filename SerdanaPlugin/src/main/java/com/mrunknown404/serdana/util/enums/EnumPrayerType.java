package main.java.com.mrunknown404.serdana.util.enums;

public enum EnumPrayerType {
	unset,
	good,
	bad;
	
	public static boolean contains(String str) {
		for (EnumPrayerType type : EnumPrayerType.values()) {
			if (type.name().equals(str)) {
				return true;
			}
		}
		
		return false;
	}
}
