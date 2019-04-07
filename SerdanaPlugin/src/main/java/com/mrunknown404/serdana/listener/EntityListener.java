package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.itemnbtapi.NBTEntity;
import de.tr7zw.itemnbtapi.NBTItem;

public class EntityListener implements Listener {
	
	@EventHandler
	public void entitySpawn(EntitySpawnEvent e) {
		if (e.getEntity() instanceof Monster) {
			NBTEntity nEnt = new NBTEntity(e.getEntity());
			LivingEntity ent = (LivingEntity) e.getEntity();
			
			int highestTier = 0;
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (ItemStack item : p.getInventory().getArmorContents()) {
					if (item != null) {
						NBTItem armor = new NBTItem(item);
						
						if (armor.hasKey("tier")) {
							if (armor.getInteger("tier") > highestTier) {
								highestTier = armor.getInteger("tier");
							}
						}
					}
				}
				
				if (p.getInventory().getItemInMainHand() != null) {
					NBTItem item = new NBTItem(p.getInventory().getItemInMainHand());
					if (item.hasKey("tier")) {
						if (item.getInteger("tier") != 0) {
							if (item.getInteger("tier") > highestTier) {
								highestTier = item.getInteger("tier");
							}
						}
					}
				}
				
				if (p.getInventory().getItemInOffHand() != null) {
					NBTItem item = new NBTItem(p.getInventory().getItemInOffHand());
					if (item.hasKey("tier")) {
						if (item.getInteger("tier") != 0) {
							if (item.getInteger("tier") > highestTier) {
								highestTier = item.getInteger("tier");
							}
						}
					}
				}
			}
			
			nEnt.setInteger("tier", highestTier);
			double multi = 0;
			if (highestTier != 0) {
				multi = highestTier / 10f;
			}
			
			multi++;
			
			double newMaxHP = ent.getHealth() * multi;
			ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHP);;
			ent.setHealth(newMaxHP);
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e) {
		PlayerInventory inv = null;
		
		if (e.getEntity() instanceof Player) {
			inv = ((Player) e.getEntity()).getInventory();
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
			
			e.setDamage(e.getDamage() * finalMulti);
		}
	}
}
