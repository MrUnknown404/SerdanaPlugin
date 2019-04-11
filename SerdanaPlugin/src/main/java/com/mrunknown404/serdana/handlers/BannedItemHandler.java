package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;

public class BannedItemHandler {
	
	private final Main main;
	private final File path;
	
	private List<Material> bannedVanillaItems = new ArrayList<Material>();
	private final File file_bannedVanillaItems = new File("BannedVanillaItems");
	private final File file_exampleBannedVanillaItems = new File("ExampleBannedVanillaItems");
	
	public BannedItemHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	public List<ItemStack> getPlayersBannedItems(Player p) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null) {
				if (isBannedItem(p.getInventory().getItem(i))) {
					items.add(p.getInventory().getItem(i));
				}
			}
		}
		
		performBannedAction(p, items.size());
		return items;
	}
	
	public boolean isBannedItem(ItemStack item) {
		NBTItem n = new NBTItem(item);
		if (n.hasKey("banned") || bannedVanillaItems.contains(item.getType())) {
			return true;
		}
		
		return false;
	}
	
	private void performBannedAction(Player p, int amount) {
		int totalTime = amount * main.getRandomConfig().getJailTimeMinutes();
		String jailName = main.getRandomConfig().getJailNames().get(new Random().nextInt(main.getRandomConfig().getJailNames().size()));
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jail " + p.getName() + " " + jailName + " " + totalTime + "m");
	}
	
	public void reload() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		bannedVanillaItems = new ArrayList<Material>();
		bannedVanillaItems.add(Material.DIRT);
		bannedVanillaItems.add(Material.STONE);
		
		if (!new File(path + "/" + file_bannedVanillaItems + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_bannedVanillaItems + Main.TYPE + "! (Will be created)");
			
			try {
				fw = new FileWriter(path + "/" + file_bannedVanillaItems + Main.TYPE);
				
				g.toJson(bannedVanillaItems, fw);
				
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!new File(path + "/" + file_exampleBannedVanillaItems + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_exampleBannedVanillaItems + Main.TYPE + "! (Will be created)");
			
			bannedVanillaItems = new ArrayList<Material>();
			for (Material m : Material.values()) {
				bannedVanillaItems.add(m);
			}
			
			try {
				fw = new FileWriter(path + "/" + file_exampleBannedVanillaItems + Main.TYPE);
				
				g.toJson(bannedVanillaItems, fw);
				
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fr = new FileReader(path + "/" + file_bannedVanillaItems + Main.TYPE);
			
			bannedVanillaItems = g.fromJson(fr, new TypeToken<List<Material>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
