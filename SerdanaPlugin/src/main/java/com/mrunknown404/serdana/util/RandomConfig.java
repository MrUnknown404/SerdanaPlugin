package main.java.com.mrunknown404.serdana.util;

public class RandomConfig {

	private String jailName;
	private int jailTimeMinutes;

	public RandomConfig(String jailName, int jailTimeMinutes) {
		this.jailName = jailName;
		this.jailTimeMinutes = jailTimeMinutes;
	}
	
	public String getJailName() {
		return jailName;
	}
	
	public int getJailTimeMinutes() {
		return jailTimeMinutes;
	}
}
