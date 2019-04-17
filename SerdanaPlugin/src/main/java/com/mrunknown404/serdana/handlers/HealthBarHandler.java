package main.java.com.mrunknown404.serdana.handlers;

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
	
	private final Main main;
	
	public HealthBarHandler(Main main) {
		this.main = main;
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
				String output = getOutput(entity);
				
				if (output != null) {
					sendActionBar(receiver, output);
				}
			}
		}.runTaskLater(main, 1L);
	}
	
	private String getOutput(LivingEntity entity) {
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		String name = ChatColor.stripColor(entity.getName());
		if (entity.getCustomName() != null) {
			name = ChatColor.stripColor(entity.getCustomName());
		}
		
		double health = entity.getHealth();
		if (health < 0 || entity.isDead()) {
			health = 0;
		}
		
		return "&7&l" + name + ": &r&c" + MathHelper.roundTo((float) health, 2) + "/" + maxHealth + " &4\u2764";
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
			
		}.runTaskLater(main, 30);
		
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorHelper.setColors(message)));
	}
}
