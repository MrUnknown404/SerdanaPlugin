package main.java.com.mrunknown404.serdana.util;

import java.util.List;

public class RandomConfig {

	private List<String> jailNames;
	private int jailTimeMinutes, parasiteSpreadChance;
	private String mainWorld;
	private double parasiteSpreadDistance;
	
	public RandomConfig(List<String> jailNames, int jailTimeMinutes, String mainWorld, double parasiteSpreadDistance, int parasiteSpreadChance) {
		this.jailNames = jailNames;
		this.jailTimeMinutes = jailTimeMinutes;
		this.mainWorld = mainWorld;
		this.parasiteSpreadDistance = parasiteSpreadDistance;
		this.parasiteSpreadChance = parasiteSpreadChance;
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
	
	public double getParasiteSpreadDistance() {
		return parasiteSpreadDistance;
	}
	
	public int getParasiteSpreadChance() {
		return parasiteSpreadChance;
	}
}
