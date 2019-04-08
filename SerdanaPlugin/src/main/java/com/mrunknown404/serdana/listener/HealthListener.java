package main.java.com.mrunknown404.serdana.listener;


import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import main.java.com.mrunknown404.serdana.Main;

public class HealthListener implements Listener {
	private Main plugin;
	
	public HealthListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Entity damaged = event.getEntity();
		if (damaged.getType().name().equals("ARMOR_STAND")) return;
		
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			
			if (projectile.getShooter() instanceof Player) {
				Player player = (Player) projectile.getShooter();
				
				if (player.getUniqueId() == damaged.getUniqueId()) {
					return;
				}
				
				if (damaged instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) damaged;
					plugin.getHealthBarHandler().sendHealth(player, livingEntity, livingEntity.getHealth() - event.getFinalDamage());
				}
			}
		}
		
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			
			if (player.getUniqueId() == damaged.getUniqueId()) {
				return;
			}
			
			if (damaged instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) damaged;
				plugin.getHealthBarHandler().sendHealth(player, livingEntity, livingEntity.getHealth() - event.getFinalDamage());
			}
		}
	}
}
