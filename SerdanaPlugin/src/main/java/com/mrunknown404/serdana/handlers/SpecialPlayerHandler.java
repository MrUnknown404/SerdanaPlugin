package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.Reloadable;

public class SpecialPlayerHandler extends Reloadable {

	private final File path;
	private final File file_special = new File("SpecialPlayers");
	
	private Map<String, Boolean> enabledPlayers = new HashMap<String, Boolean>();
	
	/* -Unknown- */
	private final ItemStack disabled, enabled, blinking;
	private boolean hasWalked, isBlinking;
	private final int maxStartBlink = 60, maxBlink = 20;
	private int startBlink = maxStartBlink, blink = maxBlink;
	
	public SpecialPlayerHandler(Main main) {
		path = main.getDataFolder();
		
		disabled= new ItemStack(Material.LEVER);
		enabled = new ItemStack(Material.REDSTONE_TORCH);
		blinking = new ItemStack(Material.TORCH);
		
		ItemMeta metaDisabled = disabled.getItemMeta();
		ItemMeta metaEnabled= enabled.getItemMeta();
		ItemMeta metaBlinking = blinking.getItemMeta();
		
		metaDisabled.setDisplayName(ColorHelper.setColors("&8Disabled Antenna"));
		metaEnabled.setDisplayName(ColorHelper.setColors("&cEnabled Antenna"));
		metaBlinking.setDisplayName(ColorHelper.setColors("&6Blinking Antenna"));
		metaDisabled.setLore(Arrays.asList(ColorHelper.setColors("&8...")));
		metaEnabled.setLore(Arrays.asList(ColorHelper.setColors("&8...")));
		metaBlinking.setLore(Arrays.asList(ColorHelper.setColors("&8...")));
		
		disabled.setItemMeta(metaDisabled);
		enabled.setItemMeta(metaEnabled);
		blinking.setItemMeta(metaBlinking);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				if (isPlayerEnabled("MrUnknown404")) {
					if (hasWalked) {
						hasWalked = false;
					} else {
						Player p1 = Bukkit.getPlayer("MrUnknown404");
						
						if (p1 != null) {
							if (!p1.getInventory().getHelmet().equals(disabled)) {
								if (p1.getInventory().getHelmet() != null && !p1.getInventory().getHelmet().equals(enabled) && !p1.getInventory().getHelmet().equals(disabled) &&
										!p1.getInventory().getHelmet().equals(blinking)) {
									p1.getWorld().dropItemNaturally(p1.getLocation(), p1.getInventory().getHelmet());
								}
								
								p1.getInventory().setHelmet(disabled);
							}
							
							if (!isBlinking) {
								if (startBlink == 0) {
									startBlink = maxStartBlink;
									isBlinking = true;
								} else {
									startBlink--;
								}
							} else {
								if (blink == 0) {
									blink = maxBlink;
									
									if (p1.getInventory().getHelmet() != null && !p1.getInventory().getHelmet().equals(enabled) && !p1.getInventory().getHelmet().equals(disabled) &&
											!p1.getInventory().getHelmet().equals(blinking)) {
										p1.getWorld().dropItemNaturally(p1.getLocation(), p1.getInventory().getHelmet());
									}
									
									p1.getInventory().setHelmet(blinking);
								} else {
									blink--;
								}
							}
						}
					}
				}
			}
		}, 0L, 2L);
	}
	
	@Override
	protected void reload() {
		if (!new File(path + "/" + file_special + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_special + Main.TYPE + " (Will be created)");
			write();
		}
		
		read();
	}
	
	private void write() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path + "/" + file_special +  Main.TYPE);
			
			g.toJson(enabledPlayers, fw);
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		
		try {
			fr = new FileReader(path + "/" + file_special + Main.TYPE);
			
			enabledPlayers = g.fromJson(fr, new TypeToken<Map<String, Boolean>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void doUnknown() {
		if (!isPlayerEnabled("MrUnknown404")) {
			return;
		}
		
		Player p = Bukkit.getPlayer("MrUnknown404");
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
				!p.getInventory().getHelmet().equals(blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(enabled);
		
		hasWalked = true;
		isBlinking = false;
		blink = maxBlink;
		startBlink = maxStartBlink;
	}
	
	public void togglePlayer(String name) {
		if (enabledPlayers.containsKey(name)) {
			enabledPlayers.put(name, !enabledPlayers.get(name));
		} else {
			enabledPlayers.put(name, true);
		}
		
		if (name.equalsIgnoreCase("MrUnknown404")) {
			Player p = Bukkit.getPlayer("MrUnknown404");
			
			if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
					!p.getInventory().getHelmet().equals(blinking)) {
				p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
			}
			
			p.getInventory().setHelmet(disabled);
		}
		
		write();
	}
	
	private boolean isPlayerEnabled(String name) {
		if (!enabledPlayers.containsKey(name)) {
			return false;
		}
		
		return enabledPlayers.get(name);
	}
}
