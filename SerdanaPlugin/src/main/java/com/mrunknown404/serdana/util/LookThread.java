package main.java.com.mrunknown404.serdana.util;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.mrunknown404.serdana.Main;

public class LookThread extends BukkitRunnable {
	private Main plugin;
	private Set<Byte> transparentTypeIds;
	
	public LookThread(Main plugin) {
		this.plugin = plugin;
		this.transparentTypeIds = new TreeSet<>();
		
		transparentTypeIds.add((byte) 0);
		transparentTypeIds.add((byte) 20);
		transparentTypeIds.add((byte) 95);
		transparentTypeIds.add((byte) 102);
		transparentTypeIds.add((byte) 160);
		transparentTypeIds.add((byte) 8);
		transparentTypeIds.add((byte) 9);
	}
	
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			List<LivingEntity> entities = TargetHelper.getLivingTargets(player, 10);
			if (!entities.isEmpty()) {
				for (LivingEntity livingEntity : entities) {
					if (livingEntity.getType() == EntityType.ARMOR_STAND) {
						continue;
					}
					
					if (player.getWorld() != livingEntity.getWorld()) {
						continue;
					}
					
					if (TargetHelper.getTarget(player.getEyeLocation(), (int) Math.ceil(player.getLocation().distance(livingEntity.getLocation())), transparentTypeIds) == null && !livingEntity.hasMetadata("NPC")) {
						plugin.getHealthBarHandler().sendHealth(player, livingEntity, livingEntity.getHealth());
						break;
					}
				}
			}
		}
	}
}
