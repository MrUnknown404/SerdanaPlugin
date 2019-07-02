package main.java.serdana.listener;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import main.java.serdana.Main;
import main.java.serdana.entities.util.EntityMonsterBase;
import main.java.serdana.util.ColorHelper;
import net.minecraft.server.v1_13_R2.Entity;

public class EntityListener implements Listener {
	
	private final Main main;
	
	public EntityListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void entityDeath(EntityDeathEvent e) {
		if (main.isComponentEnabled(Main.Components.Quests)) {
			if (e.getEntity().getKiller() instanceof Player) {
				main.getQuestHandler().checkEntityDeathTask(e.getEntity().getKiller(), e.getEntity());
			}
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e) {
		Entity c_damager = ((CraftEntity) e.getDamager()).getHandle();
		Entity c_gotHurt = ((CraftEntity) e.getEntity()).getHandle();
		
		/* LittleJDude wrote the maths */
		if (main.isComponentEnabled(Main.Components.Tiers)) {
			if (e.getEntity() instanceof Player && c_damager instanceof EntityMonsterBase) {
				EntityMonsterBase m_damager = ((EntityMonsterBase) c_damager);
				double tier = main.getTierHandler().getAverageTierFromPlayer((Player) e.getEntity());
				
				e.setDamage(e.getDamage() * Math.pow(((double) m_damager.getTier() + 10000d) / (tier + 10000d), 5000));
			} else if (c_gotHurt instanceof EntityMonsterBase && e.getDamager() instanceof Player) {
				EntityMonsterBase m_gotHurt = ((EntityMonsterBase) c_gotHurt);
				double tier = main.getTierHandler().getItemsTier(((Player) e.getDamager()).getInventory().getItemInMainHand());
				
				e.setDamage(e.getDamage() * Math.pow((tier + 10000d) / ((double) m_gotHurt.getTier() + 10000d), 5000));
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Parties)) {
			if (e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
				if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
					if (main.getPartyHandler().arePlayersInSameParty(((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId(), e.getEntity().getUniqueId())) {
						e.setCancelled(true);
						((Player) ((Projectile) e.getDamager()).getShooter()).sendMessage(ColorHelper.addColor("&cYou cannot hurt that person!"));
						return;
					}
				}
			} else if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
				if (main.getPartyHandler().arePlayersInSameParty(e.getEntity().getUniqueId(), e.getDamager().getUniqueId())) {
					e.setCancelled(true);
					e.getDamager().sendMessage(ColorHelper.addColor("&cYou cannot hurt that person!"));
					return;
				}
			}
		}
	}
}
