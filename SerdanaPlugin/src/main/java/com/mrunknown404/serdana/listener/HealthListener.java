package main.java.com.mrunknown404.serdana.listener;


import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.infos.PartyInfo;

public class HealthListener implements Listener {
	private Main main;
	
	public HealthListener(Main main) {
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (main.getPartyHandler().isPlayerInAnyParty(p.getUniqueId())) {
				PartyInfo info = main.getPartyHandler().getPlayersParty(p.getUniqueId());
				info.getBoard().set(p.getUniqueId(), (int) Math.floor(p.getHealth() - e.getFinalDamage()), false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onHeal(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (main.getPartyHandler().isPlayerInAnyParty(p.getUniqueId())) {
				PartyInfo info = main.getPartyHandler().getPlayersParty(p.getUniqueId());
				info.getBoard().set(p.getUniqueId(), (int) Math.floor(p.getHealth() + e.getAmount()), false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		Entity damaged = e.getEntity();
		if (damaged.getType() == EntityType.ARMOR_STAND || damaged.hasMetadata("shopkeeper")) {
			return;
		}
		
		Player player = null;
		if (e.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) e.getDamager();
			
			if (projectile.getShooter() instanceof Player) {
				player = (Player) projectile.getShooter();
			}
		} else if (e.getDamager() instanceof Player) {
			player = (Player) e.getDamager();
		}
		
		if (player == null || player.getUniqueId() == damaged.getUniqueId()) {
			return;
		}
		
		if (damaged instanceof LivingEntity) {
			main.getHealthBarHandler().sendHealth(player, (LivingEntity) damaged, ((LivingEntity) damaged).getHealth() - e.getFinalDamage());
		}
	}
}
