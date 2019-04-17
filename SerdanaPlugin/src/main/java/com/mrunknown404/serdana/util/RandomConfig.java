package main.java.com.mrunknown404.serdana.util;

import java.util.List;

public class RandomConfig {

	private List<String> jailNames;
	private int jailTimeMinutes, parasiteSpreadChance;
	private List<String> worlds;
	private double parasiteSpreadDistance;
	
	public RandomConfig(List<String> jailNames, int jailTimeMinutes, List<String> worlds, double parasiteSpreadDistance, int parasiteSpreadChance) {
		this.jailNames = jailNames;
		this.jailTimeMinutes = jailTimeMinutes;
		this.worlds = worlds;
		this.parasiteSpreadDistance = parasiteSpreadDistance;
		this.parasiteSpreadChance = parasiteSpreadChance;
	}
	
	public List<String> getJailNames() {
		return jailNames;
	}
	
	public int getJailTimeMinutes() {
		return jailTimeMinutes;
	}
	
	public List<String> getWorlds() {
		return worlds;
	}
	
	public double getParasiteSpreadDistance() {
		return parasiteSpreadDistance;
	}
	
	public int getParasiteSpreadChance() {
		return parasiteSpreadChance;
	}
}
