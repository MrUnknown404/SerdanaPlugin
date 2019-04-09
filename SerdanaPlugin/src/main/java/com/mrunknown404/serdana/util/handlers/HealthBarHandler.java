package main.java.com.mrunknown404.serdana.util.handlers;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.math.MathHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HealthBarHandler {
	
	private Main plugin;
	
	public HealthBarHandler(Main plugin) {
		this.plugin = plugin;
	}
	
	public void sendHealth(Player receiver, LivingEntity entity, double health) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			
			if (!receiver.canSee(player)) {
				return;
			}
			
			if (player.getGameMode() != null && player.getGameMode() == GameMode.SPECTATOR) {
				return;
			}
		}
		
		if (entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			return;
		}
		
		new BukkitRunnable() {
			public void run() {
				String output = getOutput(entity.getHealth(), receiver, entity);
				
				if (output != null) {
					sendActionBar(receiver, output);
				}
			}
		}.runTaskLater(plugin, 1L);
	}
	
	private String getOutput(double health, Player receiver, LivingEntity entity) {
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		if (health < 0 || entity.isDead()) {
			health = 0;
		}
		
		String name = ChatColor.stripColor(entity.getName());
		if (entity.getCustomName() != null) {
			name = ChatColor.stripColor(entity.getCustomName());
		}
		
		StringBuilder sb = new StringBuilder();
		int left = (int) MathHelper.clamp((float) maxHealth / 2, 1, 40);
		double heart = maxHealth / (int) MathHelper.clamp((float) maxHealth / 2, 1, 40);
		double tempHealth = health;
		
		if (maxHealth != health && health >= 0 && !entity.isDead()) {
			for (int i = 0; i < (int) MathHelper.clamp((float) maxHealth / 2, 1, 40); i++) {
				if (tempHealth - heart > 0) {
					tempHealth = tempHealth - heart;
					
					sb.append("&4\u2764");
					left--;
				} else {
					break;
				}
			}
			
			if (tempHealth > heart / 2) {
				sb.append("&4\u2764");
				left--;
			} else if (tempHealth > 0 && tempHealth <= heart / 2) {
				sb.append("&c\u2764");
				left--;
			}
		}

		if (maxHealth != health) {
			for (int i = 0; i < left; i++) {
				sb.append("&7\u2764");
			}
		} else {
			for (int i = 0; i < left; i++) {
				sb.append("&4\u2764");
			}
		}
		
		return "&7&l" + name + ": " + sb.toString();
	}
	
	private BukkitTask b = null;
	
	public void sendActionBar(Player player, String message) {
		if (b != null) {
			b.cancel();
		}
		
		b = new BukkitRunnable() {
			
			@Override
			public void run() {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorHelper.setColors(message)));
			}
			
		}.runTaskLater(this.plugin, 30);
		
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorHelper.setColors(message)));
	}
}
