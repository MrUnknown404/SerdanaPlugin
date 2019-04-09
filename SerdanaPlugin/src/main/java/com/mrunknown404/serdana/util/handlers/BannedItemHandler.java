package main.java.com.mrunknown404.serdana.util.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;

public class BannedItemHandler {
	
	public static List<ItemStack> getPlayersBannedItems(Player p, List<Material> vanillaBanned) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null) {
				if (isBannedItem(p.getInventory().getItem(i)) || isBannedMaterial(p.getInventory().getItem(i).getType(), vanillaBanned)) {
					items.add(p.getInventory().getItem(i));
				}
			}
		}
		
		performBannedAction(p, items.size());
		return items;
	}
	
	public static boolean isBannedItem(ItemStack item) {
		NBTItem n = new NBTItem(item);
		if (n.hasKey("banned")) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isBannedMaterial(Material m, List<Material> vanillaBanned) {
		if (vanillaBanned.contains(m)) {
			return true;
		}
		
		return false;
	}
	
	private static void performBannedAction(Player p, int amount) {
		int totalTime = amount * Main.getRandomConfig().getJailTimeMinutes();
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jail " + p.getName() + " " + Main.getRandomConfig().getJailName() + " " + totalTime + "m");
	}
}
