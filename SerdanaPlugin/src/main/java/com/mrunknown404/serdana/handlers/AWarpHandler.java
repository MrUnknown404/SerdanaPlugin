
package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.Reloadable;

public class AWarpHandler extends Reloadable {
	
	private final File path;
	private final File file_warps = new File("AdminWarps");
	
	private Map<String, Location> warps;
	
	public AWarpHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	@Override
	protected void reload() {
		if (!new File(path + "/" + file_warps + ".yml").exists()) {
			System.out.println("Could not find file: " + file_warps + ".yml" + "! (Will be created)");
			warps = new HashMap<String, Location>();
			
			writeWarps();
		}
		
		readWarps();
	}
	
	/** Sets a warp at the give {@link Location} with the given name
	 * @param l The Location to set the warp at
	 * @param name The name of the warp
	 */
	public void setWarp(Location l, String name) {
		warps.put(name, l);
		
		writeWarps();
	}
	
	/** Removes the warp with the given name
	 * @param name The name of the warp to remove
	 */
	public void removeWarp(String name) {
		warps.remove(name);
		writeWarps();
	}
	
	/** Writes all warps */
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
	
	/** Reads all warps */
	@SuppressWarnings("unchecked")
	public void readWarps() {
		File f = new File(path + "/" + file_warps + ".yml");
		
		Map<String, ?> temp = YamlConfiguration.loadConfiguration(f).getConfigurationSection("AdminWarps").getValues(false);
		warps = (Map<String, Location>) temp;
	}
	
	/** Returns the amount of warps
	 * @return The amount of warps
	 */
	public int getAmountOfWarps() {
		return warps.keySet().size();
	}
	
	/** Returns all warp names
	 * @return Returns a List of all warp names
	 */
	public List<String> getAllNames() {
		List<String> str = new ArrayList<String>();
		for (String s : warps.keySet()) {
			str.add(s);
		}
		
		return str;
	}

	/** Gets the warp with the specified name and returns the warp's {@link Location}
	 * @param string The warp to find
	 * @return The warps Location
	 */
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
