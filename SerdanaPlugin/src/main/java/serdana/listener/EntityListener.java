package main.java.serdana.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import main.java.serdana.Main;
import main.java.serdana.entities.util.EntityMonsterBase;
import main.java.serdana.util.ColorHelper;

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
		if (main.isComponentEnabled(Main.Components.Tiers)) {
			if (e.getEntity() instanceof Player && e.getDamager() instanceof EntityMonsterBase) {
				/* LittleJDude wrote the maths */
				e.setDamage(e.getDamage() * Math.pow(((((EntityMonsterBase) e.getDamager()).getTier() + 10000) /
						(main.getTierHandler().getAverageTierFromPlayer((Player) e.getEntity()) + 10000)), 5000));
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Parties)) {
			if (e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
				if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
					if (main.getPartyHandler().arePlayersInSameParty(((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId(), e.getDamager().getUniqueId())) {
						e.setCancelled(true);
						e.getDamager().sendMessage(ColorHelper.setColors("&cYou cannot hurt that person!"));
						return;
					}
				}
			} else if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
				if (main.getPartyHandler().arePlayersInSameParty(e.getEntity().getUniqueId(), e.getDamager().getUniqueId())) {
					e.setCancelled(true);
					e.getDamager().sendMessage(ColorHelper.setColors("&cYou cannot hurt that person!"));
					return;
				}
			}
		}
	}
}
