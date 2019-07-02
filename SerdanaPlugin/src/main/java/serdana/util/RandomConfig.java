package main.java.serdana.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import main.java.serdana.magic.util.MagicShrineInfo;

public class RandomConfig implements ConfigurationSerializable {

	private List<String> jailNames, worlds;
	private int jailTimeMinutes, parasiteSpreadChance;
	private double parasiteSpreadDistance;
	private List<MagicShrineInfo> shrines;
	
	public RandomConfig(List<String> jailNames, int jailTimeMinutes, List<String> worlds, double parasiteSpreadDistance, int parasiteSpreadChance, List<MagicShrineInfo> shrines) {
		this.jailNames = jailNames;
		this.jailTimeMinutes = jailTimeMinutes;
		this.worlds = worlds;
		this.parasiteSpreadDistance = parasiteSpreadDistance;
		this.parasiteSpreadChance = parasiteSpreadChance;
		this.shrines = shrines;
	}
	
	@SuppressWarnings("unchecked")
	public RandomConfig(Map<String, Object> map) {
		this.jailNames = (List<String>) map.get("jailNames");
		this.jailTimeMinutes = (int) map.get("jailTimeMinutes");
		this.worlds = (List<String>) map.get("worlds");
		this.parasiteSpreadDistance = (double) map.get("parasiteSpreadDistance");
		this.parasiteSpreadChance = (int) map.get("parasiteSpreadChance");
		this.shrines = (List<MagicShrineInfo>) map.get("shrines");
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("jailNames", jailNames);
		result.put("jailTimeMinutes", jailTimeMinutes);
		result.put("worlds", worlds);
		result.put("parasiteSpreadDistance", parasiteSpreadDistance);
		result.put("parasiteSpreadChance", parasiteSpreadChance);
		result.put("shrines", shrines);
		return result;
	}
	
	public List<MagicShrineInfo> getMagicShrines() {
		return shrines;
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
