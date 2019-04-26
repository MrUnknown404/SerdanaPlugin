package main.java.com.mrunknown404.serdana.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class TierHandler {
	
	private static final String COLOR_CODE = "&a";
	
	/** Adds the given tier to the given {@link ItemStack}
	 * @param item The ItemStack to give the given tier
	 * @param tier The tier to given the ItemStack
	 * @return The given ItemStack with the given tier
	 */
	public ItemStack addTierToItem(ItemStack item, int tier) {
		List<String> lore = new ArrayList<String>();
		if (item.getItemMeta().hasLore()) {
			lore = item.getItemMeta().getLore();
			lore.set(lore.size() - 1, lore.get(lore.size() - 1) + ColorHelper.setColors(" " + COLOR_CODE + "[Tier " + tier + "]"));
		} else {
			lore.add(ColorHelper.setColors(COLOR_CODE + "[Tier " + tier + "]"));
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		NBTItem n = new NBTItem(item);
		
		if (n.hasKey("tier")) {
			System.out.println("Item already has a tier!");
			return null;
		}
		
		n.setInteger("tier", tier);
		return n.getItem();
	}
	
	/** Gets the highest tier from the given {@link Player}
	 * @param p The Player to check
	 * @return The highest tier from the given Player
	 */
	public int getHighestTierFromPlayer(Player p) {
		int highest = 0;
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item != null) {
				int tier = getItemsTier(item);
				if (tier > highest) {
					highest = tier;
				}
			}
		}
		
		return highest;
	}
	
	/** Gets the highest tier from all {@link Player}s
	 * @return the highest tier from all Players
	 */
	public int getHighestTierFromAllPlayers() {
		int highest = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			int tier = getHighestTierFromPlayer(p);
			
			if (tier > highest) {
				highest = tier;
			}
		}
		
		return highest;
	}
	
	/** Gets the given {@link Player}'s total tiers
	 * @param p The player to get tiers from
	 * @return The given Player's total tiers
	 */
	public int getTiersOnPlayer(Player p) {
		int tiers = 0;
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item != null) {
				if (isItemTiered(item)) {
					tiers += getItemsTier(item);
				}
			}
		}
		
		return tiers;
	}
	
	/** Gets the given {@link ItemStack}'s tier
	 * @param item The ItemStack to check
	 * @return The given ItemStack's tier
	 */
	public int getItemsTier(ItemStack item) {
		NBTItem n = new NBTItem(item);
		
		if (n.hasKey("tier")) {
			return n.getInteger("tier");
		}
		
		return -1;
	}
	
	/** Check if the given {@link ItemStack} is tiered
	 * @param item The ItemStack to check
	 * @return true if the given ItemStack is tiered, otherwise false
	 */
	public boolean isItemTiered(ItemStack item) {
		if (new NBTItem(item).hasKey("tier")) {
			return true;
		}
		
		return false;
	}
}
