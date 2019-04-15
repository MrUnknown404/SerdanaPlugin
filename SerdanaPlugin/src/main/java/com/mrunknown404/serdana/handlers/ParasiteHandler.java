package main.java.com.mrunknown404.serdana.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class ParasiteHandler {

	private final Main main;
	private final ItemStack parasite;
	
	public ParasiteHandler(Main main) {
		this.main = main;
		ItemStack item = new ItemStack(Material.NETHER_WART, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ColorHelper.setColors("&cParasite"));
		meta.setLore(new ArrayList<String>(Arrays.asList(ColorHelper.setColors("&4It appears to be eating you"), ColorHelper.setColors("&4You're unsure if you should be worried"))));
		item.setItemMeta(meta);
		
		NBTItem nItem = new NBTItem(item);
		nItem.setBoolean("isParasite", true);
		
		parasite = nItem.getItem();
	}
	
	public void start() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					int amount = 0;
					boolean has = false;
					
					for (ItemStack item : p.getInventory().getContents()) {
						if (item != null) {
							NBTItem nItem = new NBTItem(item);
							
							if (nItem.hasKey("isParasite")) {
								has = true;
								amount += item.getAmount();
							}
						}
					}
					
					if (has) {
						giveParasite(p, (int) Math.floor(1 + amount / 128));
						spread(p, amount);
						
						if (amount >= 512) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
						} else if (amount >= 384) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
						} else if (amount >= 256) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
						} else if (amount >= 128) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
						} else if (amount >= 64) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
						}
						if (amount >= 896) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 9));
						} else if (amount >= 768) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 9));
						} else if (amount >= 640) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 9));
						}
						
						if (amount >= 512) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
						}
						
						if (amount >= 768) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
						}
						
						if (amount >= 1024) {
							kill(p);
							p.damage(p.getHealth());
						}
					}
				}
			}
		}, 0L, 200L);
	}
	
	private void spread(Player source, int amount) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getLocation().distance(source.getLocation()) <= main.getRandomConfig().getParasiteSpreadDistance()) {
				if (new Random().nextInt(100) <= main.getRandomConfig().getParasiteSpreadChance() * Math.floor(1 + amount / 128)) {
					giveParasite(p, 1);
				}
			}
		}
	}
	
	public void killAll() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			kill(p);
		}
	}
	
	public void kill(Player p) {
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			ItemStack item = p.getInventory().getItem(i);
			
			if (item != null) {
				NBTItem nItem = new NBTItem(item);
				
				if (nItem.hasKey("isParasite")) {
					p.getInventory().remove(item);
					item = null;
				}
			}
		}
	}
	
	public void giveParasite(Player p, int amount) {
		for (int i = 0; i < amount; i++) {
			p.getInventory().addItem(parasite);
		}
	}
}
