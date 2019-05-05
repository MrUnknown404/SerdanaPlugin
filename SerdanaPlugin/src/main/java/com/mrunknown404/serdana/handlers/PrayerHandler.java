package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.enums.EnumPrayerType;
import main.java.com.mrunknown404.serdana.util.infos.PrayInfo;

public class PrayerHandler {
	
	private final File path;
	private final File file_unsetPrayers = new File("UnsetPrayers");
	private final File file_goodPrayers = new File("GoodPrayers");
	private final File file_badPrayers = new File("BadPrayers");
	
	private List<PrayInfo> unsetPrayers = new ArrayList<PrayInfo>();
	private List<PrayInfo> goodPrayers = new ArrayList<PrayInfo>();
	private List<PrayInfo> badPrayers = new ArrayList<PrayInfo>();
	
	public static List<Inventory> unsetPrayersInventory = new ArrayList<Inventory>();
	public static List<Inventory> goodPrayersInventory = new ArrayList<Inventory>();
	public static List<Inventory> badPrayersInventory = new ArrayList<Inventory>();
	
	public PrayerHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	/** Creates the Prayer {@link Inventory}s */
	private void createInventories() {
		unsetPrayersInventory.clear();
		goodPrayersInventory.clear();
		badPrayersInventory.clear();
		
		for (int i = 0; i < unsetPrayers.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				unsetPrayersInventory.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&cUnsorted Prayers [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
		
		for (int i = 0; i < goodPrayers.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				goodPrayersInventory.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&cGood Prayers [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
		
		for (int i = 0; i < badPrayers.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				badPrayersInventory.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&cBad Prayers [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
	}
	
	/** Adds Prayers to the Prayer {@link Inventory}s */
	private void addPrayerstoInventories() {
		for (int i = 0; i < unsetPrayers.size(); i++) {
			PrayInfo pi = unsetPrayers.get(i);
			
			unsetPrayersInventory.get((int) Math.ceil(i / 54)).setItem(i % 54, pi.getBook());
		}
		
		for (int i = 0; i < goodPrayers.size(); i++) {
			PrayInfo pi = goodPrayers.get(i);
			
			goodPrayersInventory.get((int) Math.ceil(i / 54)).setItem(i % 54, pi.getBook());
		}
		
		for (int i = 0; i < badPrayers.size(); i++) {
			PrayInfo pi = badPrayers.get(i);
			
			badPrayersInventory.get((int) Math.ceil(i / 54)).setItem(i % 54, pi.getBook());
		}
	}
	
	/** Reloads this class's Configs */
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		writeAllPrayers();
		readAllPrayers();
		createInventories();
		addPrayerstoInventories();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	/** Adds the given {@link PrayInfo} to the given {@link EnumPrayerType}'s {@link Inventory}
	 * @param info PrayInfo to add
	 * @param type Type to add to
	 */
	public void addPrayer(PrayInfo info, EnumPrayerType type) {
		if (type == EnumPrayerType.unset) {
			unsetPrayers.add(info);
			writeUnsetPrayers();
		} else if (type == EnumPrayerType.good) {
			goodPrayers.add(info);
			writeGoodPrayers();
		} else if (type == EnumPrayerType.bad) {
			badPrayers.add(info);
			writeBadPrayers();
		}
		
		reload();
	}
	
	/** Removed the given {@link PrayInfo} from the given {@link EnumPrayerType}'s {@link Inventory}
	 * @param info PrayInfo to remove
	 * @param type Type to remove from
	 */
	public void removePrayer(PrayInfo info, EnumPrayerType type) {
		if (type == EnumPrayerType.unset) {
			for (int k = 0; k < unsetPrayers.size(); k++) {
				if (unsetPrayers.get(k).equals(info)) {
					unsetPrayers.remove(info);
					writeUnsetPrayers();
				}
			}
		} else if (type == EnumPrayerType.good) {
			for (int k = 0; k < goodPrayers.size(); k++) {
				if (goodPrayers.get(k).equals(info)) {
					goodPrayers.remove(info);
					writeGoodPrayers();
				}
			}
		} else if (type == EnumPrayerType.bad) {
			for (int k = 0; k < badPrayers.size(); k++) {
				if (badPrayers.get(k).equals(info)) {
					badPrayers.remove(info);
					writeBadPrayers();
				}
			}
		}
		
		reload();
	}
	
	/** Write all Prayers to file */
	private void writeAllPrayers() {
		if (!new File(path + "/Prayers/" + file_unsetPrayers + ".yml").exists()) {
			System.out.println("Could not find file: " + file_unsetPrayers + ".yml! (Will be created)");
			writeUnsetPrayers();
		}
		
		if (!new File(path + "/Prayers/" + file_goodPrayers + ".yml").exists()) {
			System.out.println("Could not find file: " + file_goodPrayers + ".yml! (Will be created)");
			writeGoodPrayers();
		}
		
		if (!new File(path + "/Prayers/" + file_badPrayers + ".yml").exists()) {
			System.out.println("Could not find file: " + file_badPrayers + ".yml! (Will be created)");
			writeBadPrayers();
		}
	}
	
	/** Writes UnsetPrayers to file */
	private void writeUnsetPrayers() {
		File f = new File(path + "/Prayers/" + file_unsetPrayers + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("Prays", unsetPrayers);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Writes GoodPrayers to file */
	private void writeGoodPrayers() {
		File f = new File(path + "/Prayers/" + file_goodPrayers + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("Prays", goodPrayers);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Writes BadPrayers to file */
	private void writeBadPrayers() {
		File f = new File(path + "/Prayers/" + file_badPrayers + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("Prays", badPrayers);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Reads all Prayers from file */
	private void readAllPrayers() {
		File f = new File(path + "/Prayers/" + file_unsetPrayers + ".yml");
		
		unsetPrayers.clear();
		List<?> list = YamlConfiguration.loadConfiguration(f).getList("Prays");
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				unsetPrayers.add((PrayInfo) list.get(i));
			}
		}
		
		f = new File(path + "/Prayers/" + file_goodPrayers + ".yml");
		
		goodPrayers.clear();
		list = YamlConfiguration.loadConfiguration(f).getList("Prays");
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				goodPrayers.add((PrayInfo) list.get(i));
			}
		}
		
		f = new File(path + "/Prayers/" + file_badPrayers + ".yml");
		
		badPrayers.clear();
		list = YamlConfiguration.loadConfiguration(f).getList("Prays");
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				badPrayers.add((PrayInfo) list.get(i));
			}
		}
	}
	
	public List<Inventory> getUnsetInventories() {
		return unsetPrayersInventory;
	}
	
	public List<Inventory> getGoodInventories() {
		return goodPrayersInventory;
	}
	
	public List<Inventory> getBadInventories() {
		return badPrayersInventory;
	}
	
	public List<PrayInfo> getUnsetPrayers() {
		return unsetPrayers;
	}
	
	public List<PrayInfo> getGoodPrayers() {
		return goodPrayers;
	}
	
	public List<PrayInfo> getBadPrayers() {
		return badPrayers;
	}
}
