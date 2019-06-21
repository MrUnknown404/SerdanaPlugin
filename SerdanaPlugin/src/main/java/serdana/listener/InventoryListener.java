package main.java.serdana.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.enums.EnumQuestState;

public class InventoryListener implements Listener {
	
	private final Main main;
	
	public InventoryListener(Main main) {
		this.main = main;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() != null) {
			if (main.isComponentEnabled(Main.Components.StopNamedItemUse) && main.isComponentEnabled(Main.Components.Tiers)) {
				if (e.getInventory() instanceof AnvilInventory || e.getInventory() instanceof EnchantingInventory) {
					if (main.getTierHandler().isItemTiered(e.getCurrentItem())) {
						e.getWhoClicked().sendMessage(ColorHelper.setColors("&cYou cannot put that in an anvil!"));
						e.setCancelled(true);
						e.setResult(Event.Result.DENY);
					}
				}
			}
			
			if (main.isComponentEnabled(Main.Components.Parasite)) {
				if (e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
					NBTItem n = new NBTItem(e.getCurrentItem());
					
					if (n.hasKey("isParasite")) {
						e.setCancelled(true);
						e.setResult(Event.Result.DENY);
					}
				}
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Prayers)) {
			for (Inventory inv : main.getPrayerHandler().getUnsetInventories()) {
				if (e.getInventory().getName().equalsIgnoreCase(inv.getName())) {
					if (e.getAction() != InventoryAction.CLONE_STACK && e.getAction() != InventoryAction.PLACE_ALL &&
							e.getAction() != InventoryAction.PLACE_ONE && e.getAction() != InventoryAction.PLACE_SOME) {
						e.setCancelled(true);
						e.setResult(Event.Result.DENY);
					}
				}
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Quests)) {
			for (EnumQuestState state : EnumQuestState.values()) {
				for (Inventory inv : main.getQuestHandler().getPlayersQuestGUIs((Player) e.getWhoClicked(), state)) {
					if (e.getInventory().getName().equalsIgnoreCase(inv.getName())) {
						e.setCancelled(true);
						e.setResult(Event.Result.DENY);
					}
				}
			}
		}
	}
}
