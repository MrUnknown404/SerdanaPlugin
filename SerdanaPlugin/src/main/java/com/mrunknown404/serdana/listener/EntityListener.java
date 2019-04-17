package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

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
	public void entityDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				if (main.getPartyHandler().arePlayersInSameParty(e.getEntity().getUniqueId(), e.getDamager().getUniqueId())) {
					e.setCancelled(true);
					e.getDamager().sendMessage(ColorHelper.setColors("&cYou cannot hurt that person!"));
					return;
				}
			}
			
			e.setDamage(e.getDamage() * (1 + main.getTierHandler().getTiersOnPlayer((Player) e.getEntity()) / 10));
		}
	}
}
