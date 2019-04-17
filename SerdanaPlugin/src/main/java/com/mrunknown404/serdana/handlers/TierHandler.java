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
	
	public int getHighestTierFromPlayer(Player p) {
		int highest = 0;
		
		List<ItemStack> itemsToCheck = new ArrayList<ItemStack>();
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item != null) {
				itemsToCheck.add(item);
			}
		}
		
		if (p.getInventory().getItemInMainHand() != null) {
			itemsToCheck.add(p.getInventory().getItemInMainHand());
		}
		
		if (p.getInventory().getItemInOffHand() != null) {
			itemsToCheck.add(p.getInventory().getItemInOffHand());
		}
		
		for (ItemStack item : itemsToCheck) {
			int tier = getItemsTier(item);
			if (tier > highest) {
				highest = tier;
			}
		}
		
		return highest;
	}
	
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
	
	public int getTiersOnPlayer(Player p) {
		int tiers = 0;
		
		List<ItemStack> itemsToCheck = new ArrayList<ItemStack>();
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item != null) {
				itemsToCheck.add(item);
			}
		}
		
		if (p.getInventory().getItemInMainHand() != null) {
			itemsToCheck.add(p.getInventory().getItemInMainHand());
		}
		
		if (p.getInventory().getItemInOffHand() != null) {
			itemsToCheck.add(p.getInventory().getItemInOffHand());
		}
		
		for (ItemStack item : itemsToCheck) {
			if (isItemTiered(item)) {
				tiers += getItemsTier(item);
			}
		}
		
		return tiers;
	}
	
	public int getItemsTier(ItemStack item) {
		NBTItem n = new NBTItem(item);
		
		if (n.hasKey("tier")) {
			return n.getInteger("tier");
		}
		
		return -1;
	}
	
	public boolean isItemTiered(ItemStack item) {
		if (new NBTItem(item).hasKey("tier")) {
			return true;
		}
		
		return false;
	}
}
