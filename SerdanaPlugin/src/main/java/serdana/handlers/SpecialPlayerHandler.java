package main.java.serdana.handlers;

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

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.Reloadable;

public class SpecialPlayerHandler extends Reloadable {

	private final File path;
	private final File file_special = new File("SpecialPlayers");
	
	private Map<String, Boolean> enabledPlayers = new HashMap<String, Boolean>();
	
	/* -Unknown- */
	private final ItemStack u_disabled, u_enabled, u_blinking;
	private boolean u_hasWalked, u_isBlinking;
	private final int u_maxStartBlink = 30, u_maxBlink = 10;
	private int u_startBlink = u_maxStartBlink, u_blink = u_maxBlink;
	
	/* Hoodie */
	private final ItemStack h_red, h_blue;
	
	public SpecialPlayerHandler(Main main) {
		path = main.getDataFolder();
		
		u_disabled= new ItemStack(Material.LEVER);
		u_enabled = new ItemStack(Material.REDSTONE_TORCH);
		u_blinking = new ItemStack(Material.TORCH);
		
		h_red = new ItemStack(Material.RED_STAINED_GLASS);
		h_blue = new ItemStack(Material.BLUE_STAINED_GLASS);
		
		ItemMeta metaDisabled = u_disabled.getItemMeta();
		ItemMeta metaEnabled= u_enabled.getItemMeta();
		ItemMeta metaBlinking = u_blinking.getItemMeta();
		
		ItemMeta metaRed= h_red.getItemMeta();
		ItemMeta metaBlue = h_blue.getItemMeta();
		
		metaDisabled.setDisplayName(ColorHelper.addColor("&8Disabled Antenna"));
		metaEnabled.setDisplayName(ColorHelper.addColor("&cEnabled Antenna"));
		metaBlinking.setDisplayName(ColorHelper.addColor("&6Blinking Antenna"));
		metaDisabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metaEnabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metaBlinking.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		
		metaRed.setDisplayName(ColorHelper.addColor("&cRed Seizure Helmet"));
		metaBlue.setDisplayName(ColorHelper.addColor("&bBlue Seizure Helmet"));
		
		u_disabled.setItemMeta(metaDisabled);
		u_enabled.setItemMeta(metaEnabled);
		u_blinking.setItemMeta(metaBlinking);
		
		h_red.setItemMeta(metaRed);
		h_blue.setItemMeta(metaBlue);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getPlayer(SpecialPlayers.Unknown.getName()) != null && isPlayerEnabled(SpecialPlayers.Unknown)) {
					if (u_hasWalked) {
						u_hasWalked = false;
					} else {
						Player p = Bukkit.getPlayer(SpecialPlayers.Unknown.getName());
						
						if (!p.getInventory().getHelmet().equals(u_disabled)) {
							if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(u_enabled) && !p.getInventory().getHelmet().equals(u_disabled) &&
									!p.getInventory().getHelmet().equals(u_blinking)) {
								p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
							}
							
							p.getInventory().setHelmet(u_disabled);
						}
						
						if (!u_isBlinking) {
							if (u_startBlink == 0) {
								u_startBlink = u_maxStartBlink;
								u_isBlinking = true;
							} else {
								u_startBlink--;
							}
						} else {
							if (u_blink == 0) {
								u_blink = u_maxBlink;
								
								if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(u_enabled) && !p.getInventory().getHelmet().equals(u_disabled) &&
										!p.getInventory().getHelmet().equals(u_blinking)) {
									p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
								}
								
								p.getInventory().setHelmet(u_blinking);
							} else {
								u_blink--;
							}
						}
					}
				}
				
				if (Bukkit.getPlayer(SpecialPlayers.Hoodie.getName()) != null && isPlayerEnabled(SpecialPlayers.Hoodie)) {
					doHoodie();
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
		if (!isPlayerEnabled(SpecialPlayers.Unknown)) {
			return;
		}
		
		Player p = Bukkit.getPlayer(SpecialPlayers.Unknown.getName());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(u_enabled) && !p.getInventory().getHelmet().equals(u_disabled) &&
				!p.getInventory().getHelmet().equals(u_blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(u_enabled);
		
		u_hasWalked = true;
		u_isBlinking = false;
		u_blink = u_maxBlink;
		u_startBlink = u_maxStartBlink;
	}
	
	private void doHoodie() {
		if (!isPlayerEnabled(SpecialPlayers.Hoodie)) {
			return;
		}
		
		Player p = Bukkit.getPlayer(SpecialPlayers.Hoodie.getName());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(h_blue) && !p.getInventory().getHelmet().equals(h_red)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		} else if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(h_red);
		}
		
		if (p.getInventory().getHelmet().equals(h_red)) {
			p.getInventory().setHelmet(h_blue);
		} else if (p.getInventory().getHelmet().equals(h_blue)) {
			p.getInventory().setHelmet(h_red);
		} else {
			p.getInventory().setHelmet(h_red);
		}
	}
	
	public void togglePlayer(SpecialPlayers player) {
		if (enabledPlayers.containsKey(player.getName())) {
			enabledPlayers.put(player.getName(), !enabledPlayers.get(player.getName()));
		} else {
			enabledPlayers.put(player.getName(), true);
		}
		
		if (player == SpecialPlayers.Unknown) {
			Player p = Bukkit.getPlayer(player.getName());
			
			if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(u_enabled) && !p.getInventory().getHelmet().equals(u_disabled) &&
					!p.getInventory().getHelmet().equals(u_blinking)) {
				p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
			}
			
			p.getInventory().setHelmet(u_disabled);
		}
		
		write();
	}
	
	private boolean isPlayerEnabled(SpecialPlayers player) {
		if (!enabledPlayers.containsKey(player.getName())) {
			return false;
		}
		
		return enabledPlayers.get(player.getName());
	}
	
	public enum SpecialPlayers {
		Unknown("MrUnknown404"),
		Hoodie ("Hoodsworth");
		
		private final String name;
		
		private SpecialPlayers(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
