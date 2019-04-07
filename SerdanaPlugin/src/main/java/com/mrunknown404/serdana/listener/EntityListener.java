package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.itemnbtapi.NBTItem;

public class EntityListener implements Listener {
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e) {
		
		PlayerInventory inv = null;
		if (e.getEntity() instanceof Player) {
			inv = ((Player) e.getEntity()).getInventory();
		} else {
			inv = ((Player) e.getDamager()).getInventory();
		}
		float finalMulti = 1;
		
		for (ItemStack item : inv.getArmorContents()) {
			if (item != null) {
				NBTItem armor = new NBTItem(item);
				
				if (armor.hasKey("tier")) {
					if (armor.getInteger("tier") != 0) {
						finalMulti += armor.getInteger("tier") / 10;
					}
				}
			}
		}
		
		if (inv.getItemInMainHand() != null) {
			NBTItem item = new NBTItem(inv.getItemInMainHand());
			if (item.hasKey("tier")) {
				if (item.getInteger("tier") != 0) {
					finalMulti += item.getInteger("tier") / 10;
				}
			}
		}
		
		if (inv.getItemInOffHand() != null) {
			NBTItem item = new NBTItem(inv.getItemInOffHand());
			if (item.hasKey("tier")) {
				if (item.getInteger("tier") != 0) {
					finalMulti += item.getInteger("tier") / 10;
				}
			}
		}
		
		if (e.getEntity() instanceof Player) {
			e.setDamage(e.getDamage() * finalMulti);
		} else {
			e.setDamage(e.getDamage() / finalMulti);
		}
	}
}
