package main.java.com.mrunknown404.serdana.util;

import java.util.List;

public class RandomConfig {

	private List<String> jailNames;
	private int jailTimeMinutes;

	public RandomConfig(List<String> jailNames, int jailTimeMinutes) {
		this.jailNames = jailNames;
		this.jailTimeMinutes = jailTimeMinutes;
	}
	
	public List<String> getJailNames() {
		return jailNames;
	}
	
	public int getJailTimeMinutes() {
		return jailTimeMinutes;
	}
}
