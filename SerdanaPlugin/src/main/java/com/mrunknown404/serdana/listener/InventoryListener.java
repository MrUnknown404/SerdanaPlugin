package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import main.java.com.mrunknown404.serdana.Main;

public class InventoryListener implements Listener {
	
	private final Main main;
	
	public InventoryListener(Main main) {
		this.main = main;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		for (Inventory inv : main.getPrayerHandler().getUnsortedInventories()) {
			if (event.getInventory().getName().equalsIgnoreCase(inv.getName())) { //switch to !=
				if (event.getAction() != InventoryAction.CLONE_STACK && event.getAction() != InventoryAction.PLACE_ALL &&
						event.getAction() != InventoryAction.PLACE_ONE && event.getAction() != InventoryAction.PLACE_SOME) {
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}
}
