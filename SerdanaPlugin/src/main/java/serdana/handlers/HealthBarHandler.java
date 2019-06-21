package main.java.serdana.handlers;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.math.MathH;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HealthBarHandler {
	
	private final Main main;
	
	public HealthBarHandler(Main main) {
		this.main = main;
	}
	
	/** Sends the given {@link LivingEntity} to the given {@link Player}'s Actionbar
	 * @param receiver The Player to send the Actionbar to
	 * @param entity The Entity that took damage
	 * @param health The Entities health
	 */
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
	
	/** Returns a formated String based around the given Entities health
	 * @param entity The Entity to format off of
	 * @return The formated String based around the given Entities health
	 */
	private String getOutput(LivingEntity entity) {
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		String name = ChatColor.stripColor(entity.getName());
		if (entity.getCustomName() != null) {
			name = ChatColor.stripColor(entity.getCustomName());
		}
		
		double health = MathH.clamp((float) entity.getHealth(), 0, Integer.MAX_VALUE);
		if (entity.isDead()) {
			health = 0;
		}
		
		return "&7&l" + name + ": &r&c" + MathH.roundTo((float) health, 2) + "/" + maxHealth + " &4\u2764";
	}
	
	private BukkitTask b = null;
	
	/** Sends an Actionbar to the given {@link Player} with the given text
	 * @param player Player to send to
	 * @param message String to send
	 */
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
