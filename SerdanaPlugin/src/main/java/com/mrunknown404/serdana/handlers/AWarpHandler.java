
package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.com.mrunknown404.serdana.Main;

public class AWarpHandler {
	
	private final File path;
	private final File file_warps = new File("AdminWarps");
	
	private Map<String, Location> warps;
	
	public AWarpHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		if (!new File(path + "/" + file_warps + ".yml").exists()) {
			System.out.println("Could not find file: " + file_warps + ".yml" + "! (Will be created)");
			warps = new HashMap<String, Location>();
			
			writeWarps();
		}
		
		readWarps();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	public void setWarp(Location l, String name) {
		warps.put(name, l);
		
		writeWarps();
	}
	
	public void removeWarp(String name) {
		warps.remove(name);
		writeWarps();
	}
	
	public void writeWarps() {
		File f = new File(path + "/" + file_warps + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("AdminWarps", warps);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readWarps() {
		File f = new File(path + "/" + file_warps + ".yml");
		
		Map<String, ?> temp = YamlConfiguration.loadConfiguration(f).getConfigurationSection("AdminWarps").getValues(false);
		warps = (Map<String, Location>) temp;
	}
	
	public int getAmountOfWarps() {
		return warps.keySet().size();
	}
	
	public List<String> getAllNames() {
		List<String> str = new ArrayList<String>();
		for (String s : warps.keySet()) {
			str.add(s);
		}
		
		return str;
	}

	public Location getWarp(String string) {
		if (!warps.isEmpty()) {
			Iterator<Entry<String, Location>> it = warps.entrySet().iterator();
			
			while (it.hasNext()) {
				Entry<String, Location> pair = it.next();
				
				if (pair.getKey().equalsIgnoreCase(string)) {
					return pair.getValue();
				}
			}
		}
		
		return null;
	}
}
