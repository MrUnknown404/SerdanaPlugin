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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.Reloadable;

public class BannedItemHandler extends Reloadable {
	
	private final Main main;
	private final File path;
	
	private List<Material> bannedVanillaItems = new ArrayList<Material>();
	private final File file_bannedVanillaItems = new File("BannedVanillaItems");
	
	public BannedItemHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	/** Searches the given {@link Player}'s {@link Inventory} for any banned {@link ItemStack}
	 * @param p The Player to search
	 * @return A List containing any banned ItemStacks
	 */
	public List<ItemStack> getPlayersBannedItems(Player p) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null) {
				if (isBannedItem(p.getInventory().getItem(i))) {
					items.add(p.getInventory().getItem(i));
				}
			}
		}
		
		return items;
	}
	
	/** Searches the given {@link Player}'s {@link Inventory} for any banned {@link ItemStack}
	 * @param p The Player to search
	 * @return true if the given Player has any banned ItemStacks, otherwise false
	 */
	public boolean doesPlayerHaveBannedItems(Player p) {
		if (!getPlayersBannedItems(p).isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	/** Checks if the given {@link ItemStack} is banned
	 * @param item ItemStack to check
	 * @return true if the given ItemStack is banned, otherwise false
	 */
	public boolean isBannedItem(ItemStack item) {
		NBTItem n = new NBTItem(item);
		if (n.hasKey("banned") || bannedVanillaItems.contains(item.getType())) {
			return true;
		}
		
		return false;
	}
	
	/** Performs the banned action (Jails player)
	 * @param p The Player to perform the banned action on
	 * @param amount The amount of different ItemStacks the Player has on them
	 */
	public void performBannedAction(Player p, int amount) {
		int totalTime = amount * main.getRandomConfig().getJailTimeMinutes();
		String jailName = main.getRandomConfig().getJailNames().get(new Random().nextInt(main.getRandomConfig().getJailNames().size()));
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jail " + p.getName() + " " + jailName + " " + totalTime + "m");
	}
	
	@Override
	protected void reload() {
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
		
		try {
			fr = new FileReader(path + "/" + file_bannedVanillaItems + Main.TYPE);
			
			bannedVanillaItems = g.fromJson(fr, new TypeToken<List<Material>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
