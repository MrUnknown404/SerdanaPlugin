package main.java.serdana.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.math.MathH;

public class TierHandler {
	
	private static final String COLOR_CODE = "&a";
	
	/** Adds the given tier to the given {@link ItemStack}
	 * @param item The ItemStack to give the given tier
	 * @param tier The tier to given the ItemStack
	 * @return The given ItemStack with the given tier
	 */
	public ItemStack addTierToItem(ItemStack item, int tier) {
		if (new NBTItem(item).hasKey("tier")) {
			List<String> lore = new ArrayList<String>();
			
			for (String str : item.getItemMeta().getLore()) {
				lore.add(str);
			}
			
			lore.set(lore.size() - 1, lore.get(lore.size() - 1).substring(0, lore.get(lore.size() - 1).length() - 8 - String.valueOf(tier).length()));
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		List<String> lore = new ArrayList<String>();
		if (item.getItemMeta().hasLore()) {
			lore = item.getItemMeta().getLore();
			lore.set(lore.size() - 1, lore.get(lore.size() - 1) + ColorHelper.addColor(" " + COLOR_CODE + "[Tier " + tier + "]"));
		} else {
			lore.add(ColorHelper.addColor(COLOR_CODE + "[Tier " + tier + "]"));
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		NBTItem n = new NBTItem(item);
		n.setInteger("tier", tier);
		return n.getItem();
	}
	
	/** Gets the given {@link Player}'s average tier
	 * @param p The Player to check
	 * @return returns the the given Player's tier
	 */
	public float getAverageTierFromPlayer(Player p) {
		List<Integer> tiers = new ArrayList<Integer>();
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (isItemTiered(item)) {
				tiers.add(getItemsTier(item));
			} else {
				tiers.add(0);
			}
		}
		
		return MathH.roundTo(MathH.calculateAverage(tiers), 1);
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
