package main.java.serdana.magic.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class MagicShrineInfo implements ConfigurationSerializable {

	private final String name;
	private final Location mainPillar;
	private final List<Location> pillars;
	
	public MagicShrineInfo(String name, Location mainPillar, List<Location> pillars) {
		this.name = name;
		this.mainPillar = mainPillar;
		this.pillars = pillars;
	}
	
	@SuppressWarnings("unchecked")
	public MagicShrineInfo(Map<String, Object> map) {
		this.name = (String) map.get("name");
		this.mainPillar = (Location) map.get("mainPillar");
		this.pillars = (List<Location>) map.get("pillars");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("mainPillar", mainPillar);
		result.put("pillars", pillars);
		return result;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getMainPillar() {
		return mainPillar;
	}
	
	public List<Location> getPillars() {
		return pillars;
	}
}
