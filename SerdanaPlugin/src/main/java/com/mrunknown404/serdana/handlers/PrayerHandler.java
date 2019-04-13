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
import main.java.com.mrunknown404.serdana.util.EnumPrayerType;
import main.java.com.mrunknown404.serdana.util.infos.PrayInfo;

public class PrayerHandler {
	
	private final File path;
	private final File file_unsortedPrayers = new File("UnsetPrayers");
	private final File file_goodPrayers = new File("GoodPrayers");
	private final File file_badPrayers = new File("BadPrayers");
	
	private List<PrayInfo> unsortedPrayers = new ArrayList<PrayInfo>();
	private List<PrayInfo> goodPrayers = new ArrayList<PrayInfo>();
	private List<PrayInfo> badPrayers = new ArrayList<PrayInfo>();
	
	public static List<Inventory> unsortedPrayersInventory = new ArrayList<Inventory>();
	public static List<Inventory> goodPrayersInventory = new ArrayList<Inventory>();
	public static List<Inventory> badPrayersInventory = new ArrayList<Inventory>();
	
	public PrayerHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	private void createInventories() {
		unsortedPrayersInventory.clear();
		goodPrayersInventory.clear();
		badPrayersInventory.clear();
		
		for (int i = 0; i < unsortedPrayers.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				unsortedPrayersInventory.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&cUnsorted Prayers [" + (int) Math.ceil(i / 54) + "]")));
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
	
	private void addPrayerstoInventories() {
		for (int i = 0; i < unsortedPrayers.size(); i++) {
			PrayInfo pi = unsortedPrayers.get(i);
			
			unsortedPrayersInventory.get((int) Math.ceil(i / 54)).setItem(i % 54, pi.getBook());
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
	
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		writeAllPrayers();
		readAllPrayers();
		createInventories();
		addPrayerstoInventories();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	public void addPrayer(PrayInfo info, EnumPrayerType type) {
		if (type == EnumPrayerType.unset) {
			unsortedPrayers.add(info);
			writeUnsortedPrayers();
		} else if (type == EnumPrayerType.good) {
			goodPrayers.add(info);
			writeGoodPrayers();
		} else if (type == EnumPrayerType.bad) {
			badPrayers.add(info);
			writeBadPrayers();
		}
		
		reload();
	}
	
	public void removePrayer(PrayInfo info, EnumPrayerType type) {
		if (type == EnumPrayerType.unset) {
			for (int k = 0; k < unsortedPrayers.size(); k++) {
				if (unsortedPrayers.get(k).equals(info)) {
					unsortedPrayers.remove(info);
					writeUnsortedPrayers();
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
	
	private void writeAllPrayers() {
		if (!new File(path + "/Prayers/" + file_unsortedPrayers + ".yml").exists()) {
			System.out.println("Could not find file: " + file_unsortedPrayers + ".yml! (Will be created)");
			writeUnsortedPrayers();
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
	
	private void writeUnsortedPrayers() {
		File f = new File(path + "/Prayers/" + file_unsortedPrayers + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("Prays", unsortedPrayers);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private void readAllPrayers() {
		File f = new File(path + "/Prayers/" + file_unsortedPrayers + ".yml");
		
		unsortedPrayers.clear();
		List<?> list = YamlConfiguration.loadConfiguration(f).getList("Prays");
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				unsortedPrayers.add((PrayInfo) list.get(i));
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
	
	public List<Inventory> getUnsortedInventories() {
		return unsortedPrayersInventory;
	}
	
	public List<Inventory> getGoodInventories() {
		return goodPrayersInventory;
	}
	
	public List<Inventory> getBadInventories() {
		return badPrayersInventory;
	}
	
	public List<PrayInfo> getUnsortedPrayers() {
		return unsortedPrayers;
	}
	
	public List<PrayInfo> getGoodPrayers() {
		return goodPrayers;
	}
	
	public List<PrayInfo> getBadPrayers() {
		return badPrayers;
	}
}
