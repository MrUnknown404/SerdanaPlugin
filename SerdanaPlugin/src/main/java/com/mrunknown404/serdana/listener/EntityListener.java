package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;

import de.tr7zw.itemnbtapi.NBTEntity;
import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.math.MathHelper;

public class EntityListener implements Listener {
	
	private final Main main;
	
	public EntityListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void entityDeath(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			main.getQuestHandler().checkEntityDeathTask(e.getEntity().getKiller(), e.getEntity());
		}
	}
	
	@EventHandler
	public void entitySpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
			NBTEntity nEnt = new NBTEntity(e.getEntity());
			LivingEntity ent = (LivingEntity) e.getEntity();
			
			int tier = -1;
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getInventory().getItemInMainHand().getItemMeta() instanceof SpawnEggMeta) {
					NBTItem nItem = new NBTItem(p.getInventory().getItemInMainHand());
					if (nItem.hasKey("tier")) {
						tier = nItem.getInteger("tier");
					}
				} else if (p.getInventory().getItemInOffHand().getItemMeta() instanceof SpawnEggMeta) {
					NBTItem nItem = new NBTItem(p.getInventory().getItemInOffHand());
					if (nItem.hasKey("tier")) {
						tier = nItem.getInteger("tier");
					}
				}
			}
			
			if (tier >= 0) {
				nEnt.setInteger("tier", tier);
				
				double multi = MathHelper.clamp(tier / 10f, 1, Integer.MAX_VALUE);
				
				double newMaxHP = MathHelper.clamp((float) (ent.getHealth() * multi), 1, 2000);
				ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHP);;
				ent.setHealth(newMaxHP);
			}
		}
		
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
			
			double newMaxHP = MathHelper.clamp((float) (ent.getHealth() * multi), 1, 2000);
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
