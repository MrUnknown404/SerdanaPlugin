package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class BlockListener implements Listener {

	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		if (e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName()) {
			e.getPlayer().sendMessage(ColorHelper.setColors("&cYou cannot place named blocks!"));
			e.setCancelled(true);
		}
	}
}
