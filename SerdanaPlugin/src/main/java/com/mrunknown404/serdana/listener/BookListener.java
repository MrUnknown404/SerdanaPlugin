package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;

public class BookListener implements Listener {

	private final Main main;
	
	public BookListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBookEdit(PlayerEditBookEvent e) {
		if (main.getComponent(Main.Components.Prayers)) {
			if (e.isSigning()) {
				ItemStack main = e.getPlayer().getInventory().getItemInMainHand();
				ItemStack off = e.getPlayer().getInventory().getItemInOffHand();
				
				NBTItem n = null;
				if (main.getType() == Material.WRITABLE_BOOK) {
					n = new NBTItem(main);
				} else if (off.getType() == Material.WRITABLE_BOOK) {
					n = new NBTItem(off);
				}
				
				if (n.hasKey("isPrayerBook")) {
					NBTItem nb = null;
					if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.WRITABLE_BOOK) {
						nb = new NBTItem(e.getPlayer().getInventory().getItemInMainHand());
					} else if (e.getPlayer().getInventory().getItemInOffHand().getType() == Material.WRITABLE_BOOK) {
						nb = new NBTItem(e.getPlayer().getInventory().getItemInOffHand());
					}
					
					nb.getItem().setItemMeta(e.getNewBookMeta());
					nb.setBoolean("isPrayerBook", true);
					
					e.setNewBookMeta((BookMeta) nb.getItem().getItemMeta());
				}
			}
		}
	}
}
