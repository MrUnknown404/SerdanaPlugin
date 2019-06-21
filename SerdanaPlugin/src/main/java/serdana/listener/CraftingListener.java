package main.java.serdana.listener;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CraftingListener implements Listener {

	private final Main main;
	
	public CraftingListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		ItemStack item = e.getRecipe().getResult();
		
		if (item != null) {
			if (main.isComponentEnabled(Main.Components.StopNamedItemUse)) {
				for (int i = 0; i < e.getInventory().getMatrix().length; i++) {
					if (e.getInventory().getMatrix()[i] != null) {
						if (e.getInventory().getMatrix()[i].hasItemMeta() && e.getInventory().getMatrix()[i].getItemMeta().hasDisplayName()) {
							e.setCancelled(true);
							e.setResult(Result.DENY);
							e.getWhoClicked().sendMessage(ColorHelper.setColors("&cYou cannot use named items for crafting!"));
							return;
						}
					}
				}
			}
			
			if (main.isComponentEnabled(Main.Components.StopItemCrafting)) {
				if (item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS ||
						item.getType() == Material.IRON_HELMET || item.getType() == Material.IRON_CHESTPLATE || item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.IRON_BOOTS ||
						item.getType() == Material.GOLDEN_HELMET || item.getType() == Material.GOLDEN_CHESTPLATE || item.getType() == Material.GOLDEN_LEGGINGS || item.getType() == Material.GOLDEN_BOOTS ||
						item.getType() == Material.DIAMOND_HELMET || item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.DIAMOND_BOOTS ||
						item.getType() == Material.WOODEN_SWORD || item.getType() == Material.STONE_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.GOLDEN_SWORD || item.getType() == Material.DIAMOND_SWORD ||
						item.getType() == Material.IRON_AXE || item.getType() == Material.GOLDEN_AXE || item.getType() == Material.DIAMOND_AXE || item.getType() == Material.BOW || item.getType() == Material.SHIELD) {
					e.setCancelled(true);
					e.setResult(Result.DENY);
					e.getWhoClicked().sendMessage(ColorHelper.setColors("&cYou cannot craft " + WordUtils.capitalize(item.getType().toString().toLowerCase()) + "!"));
					return;
				}
			}
		}
	}
}
