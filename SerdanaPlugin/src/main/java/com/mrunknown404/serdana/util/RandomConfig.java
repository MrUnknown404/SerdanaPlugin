package main.java.com.mrunknown404.serdana.util;

import java.util.List;

public class RandomConfig {

	private List<String> jailNames;
	private int jailTimeMinutes;
	private String mainWorld;
	
	public RandomConfig(List<String> jailNames, int jailTimeMinutes, String mainWorld) {
		this.jailNames = jailNames;
		this.jailTimeMinutes = jailTimeMinutes;
		this.mainWorld = mainWorld;
	}
	
	public List<String> getJailNames() {
		return jailNames;
	}
	
	public int getJailTimeMinutes() {
		return jailTimeMinutes;
	}
	
	public String getMainWorld() {
		return mainWorld;
	}
}
