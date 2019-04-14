package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.quests.EnumQuestState;

public class InventoryListener implements Listener {
	
	private final Main main;
	
	public InventoryListener(Main main) {
		this.main = main;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event) {
		for (Inventory inv : main.getPrayerHandler().getUnsortedInventories()) {
			if (event.getInventory().getName().equalsIgnoreCase(inv.getName())) {
				if (event.getAction() != InventoryAction.CLONE_STACK && event.getAction() != InventoryAction.PLACE_ALL &&
						event.getAction() != InventoryAction.PLACE_ONE && event.getAction() != InventoryAction.PLACE_SOME) {
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				}
			}
		}
		
		for (EnumQuestState state : EnumQuestState.values()) {
			for (Inventory inv : main.getQuestHandler().getPlayersQuestGUIs((Player) event.getWhoClicked(), state)) {
				if (event.getInventory().getName().equalsIgnoreCase(inv.getName())) {
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}
}
