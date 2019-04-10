package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
			if (event.getInventory().getName().equalsIgnoreCase(inv.getName())) {
				if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR || event.getAction() == InventoryAction.PICKUP_ALL ||
						event.getAction() == InventoryAction.PICKUP_HALF || event.getAction() == InventoryAction.PICKUP_ONE ||
						event.getAction() == InventoryAction.PICKUP_SOME) {
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}
	
	@EventHandler
	public void inventory(InventoryEvent e) {
		System.out.println("open");
	}
	
	@EventHandler
	public void inventory(InventoryOpenEvent e) {
		System.out.println("open22");
	}
}
