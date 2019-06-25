package main.java.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.text.WordUtils;
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
import main.java.serdana.util.infos.SpecialPlayerInfo;

public class SpecialPlayerHandler extends Reloadable {

	private final Main main;
	private final File path;
	private final File file_special = new File("SpecialPlayers");
	
	private List<SpecialPlayerInfo> infos = new ArrayList<SpecialPlayerInfo>();
	
	private final ItemStack antenna_disabled, antenna_enabled, antenna_blinking;
	private final ItemStack seizureHelmet_red, seizureHelmet_blue;
	
	private final int antenna_maxStartBlink = 30, antenna_maxBlink = 10;
	private Map<UUID, Boolean> antenna_hasWalked = new HashMap<UUID, Boolean>(), antenna_isBlinking = new HashMap<UUID, Boolean>();
	private Map<UUID, Integer> antenna_startBlink = new HashMap<UUID, Integer>(), antenna_blink = new HashMap<UUID, Integer>();
	
	public SpecialPlayerHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
		
		antenna_disabled= new ItemStack(Material.LEVER);
		antenna_enabled = new ItemStack(Material.REDSTONE_TORCH);
		antenna_blinking = new ItemStack(Material.TORCH);
		
		seizureHelmet_red = new ItemStack(Material.RED_STAINED_GLASS);
		seizureHelmet_blue = new ItemStack(Material.BLUE_STAINED_GLASS);
		
		ItemMeta metaDisabled = antenna_disabled.getItemMeta();
		ItemMeta metaEnabled= antenna_enabled.getItemMeta();
		ItemMeta metaBlinking = antenna_blinking.getItemMeta();
		
		ItemMeta metaRed= seizureHelmet_red.getItemMeta();
		ItemMeta metaBlue = seizureHelmet_blue.getItemMeta();
		
		metaDisabled.setDisplayName(ColorHelper.addColor("&8Disabled Antenna"));
		metaEnabled.setDisplayName(ColorHelper.addColor("&cEnabled Antenna"));
		metaBlinking.setDisplayName(ColorHelper.addColor("&6Blinking Antenna"));
		metaDisabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metaEnabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metaBlinking.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		
		metaRed.setDisplayName(ColorHelper.addColor("&cRed Seizure Helmet"));
		metaBlue.setDisplayName(ColorHelper.addColor("&bBlue Seizure Helmet"));
		
		antenna_disabled.setItemMeta(metaDisabled);
		antenna_enabled.setItemMeta(metaEnabled);
		antenna_blinking.setItemMeta(metaBlinking);
		
		seizureHelmet_red.setItemMeta(metaRed);
		seizureHelmet_blue.setItemMeta(metaBlue);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				for (SpecialPlayerInfo info : infos) {
					Player p = Bukkit.getPlayer(info.getID());
					
					if (p != null && isPlayerEnabled(info)) {
						try {
							Method method = SpecialPlayerHandler.class.getDeclaredMethod("tick" + WordUtils.capitalize(info.getEffect().toString()), SpecialPlayerInfo.class);
							method.setAccessible(true);
							method.invoke(main.getSpecialPlayerHandler(), info);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
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
			
			g.toJson(infos, fw);
			
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
			
			infos = g.fromJson(fr, new TypeToken<List<SpecialPlayerInfo>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SpecialPlayerInfo setupNewPlayer(UUID id, boolean enabled) {
		SpecialPlayerInfo info = new SpecialPlayerInfo(id, enabled, SpecialPlayerEffect.seizureHelmet);
		infos.add(info);
		return info;
	}
	
	public void setupPlayer(UUID id) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		
		if (info.isEnabled()) {
			try {
				Method method = getClass().getDeclaredMethod("start" + WordUtils.capitalize(info.getEffect().toString()), SpecialPlayerInfo.class);
				method.setAccessible(true);
				method.invoke(main.getSpecialPlayerHandler(), info);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setEffect(UUID id, SpecialPlayerEffect effect) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		info.setEffect(effect);
		write();
		
		try {
			Method method = getClass().getDeclaredMethod("start" + WordUtils.capitalize(info.getEffect().toString()), SpecialPlayerInfo.class);
			method.setAccessible(true);
			method.invoke(main.getSpecialPlayerHandler(), info);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void togglePlayer(UUID id) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		info.toggle();
		
		write();
		
		if (info.isEnabled()) {
			try {
				Method method = getClass().getDeclaredMethod("start" + WordUtils.capitalize(info.getEffect().toString()), SpecialPlayerInfo.class);
				method.setAccessible(true);
				method.invoke(main.getSpecialPlayerHandler(), info);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startAntenna(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(antenna_enabled) && !p.getInventory().getHelmet().equals(antenna_disabled) &&
				!p.getInventory().getHelmet().equals(antenna_blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(antenna_disabled);
		
		antenna_hasWalked.put(info.getID(), false);
		antenna_isBlinking.put(info.getID(), false);
		antenna_startBlink.put(info.getID(), antenna_maxStartBlink);
		antenna_blink.put(info.getID(), antenna_maxBlink);
	}
	
	public void tickAntenna(SpecialPlayerInfo info) {
		if (antenna_hasWalked.get(info.getID())) {
			antenna_hasWalked.put(info.getID(), false);
		} else {
			Player p = Bukkit.getPlayer(info.getID());
			
			if (!p.getInventory().getHelmet().equals(antenna_disabled)) {
				if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(antenna_enabled) && !p.getInventory().getHelmet().equals(antenna_disabled) &&
						!p.getInventory().getHelmet().equals(antenna_blinking)) {
					p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
				}
				
				p.getInventory().setHelmet(antenna_disabled);
			}
			
			if (!antenna_isBlinking.get(info.getID())) {
				if (antenna_startBlink.get(info.getID()) == 0) {
					antenna_startBlink.put(info.getID(), antenna_maxStartBlink);
					antenna_isBlinking.put(info.getID(), true);
				} else {
					antenna_startBlink.put(info.getID(), antenna_startBlink.get(info.getID()) - 1);
				}
			} else {
				if (antenna_blink.get(info.getID()) == 0) {
					antenna_blink.put(info.getID(), antenna_maxBlink);
					
					if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(antenna_enabled) && !p.getInventory().getHelmet().equals(antenna_disabled) &&
							!p.getInventory().getHelmet().equals(antenna_blinking)) {
						p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
					}
					
					p.getInventory().setHelmet(antenna_blinking);
				} else {
					antenna_blink.put(info.getID(), antenna_blink.get(info.getID()) - 1);
				}
			}
		}
	}
	
	public void doAntenna(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(antenna_enabled) && !p.getInventory().getHelmet().equals(antenna_disabled) &&
				!p.getInventory().getHelmet().equals(antenna_blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(antenna_enabled);
		
		antenna_hasWalked.put(info.getID(), true);
		antenna_isBlinking.put(info.getID(), false);
		antenna_startBlink.put(info.getID(), antenna_maxStartBlink);
		antenna_blink.put(info.getID(), antenna_maxBlink);
	}
	
	public void startSeizureHelmet(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(seizureHelmet_blue) && !p.getInventory().getHelmet().equals(seizureHelmet_red)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		} else if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(seizureHelmet_red);
		}
	}
	
	public void tickSeizureHelmet(SpecialPlayerInfo info) {
		doSeizureHelmet(info);
	}
	
	public void doSeizureHelmet(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(seizureHelmet_red);
		} else if (p.getInventory().getHelmet().equals(seizureHelmet_red)) {
			p.getInventory().setHelmet(seizureHelmet_blue);
		} else if (p.getInventory().getHelmet().equals(seizureHelmet_blue)) {
			p.getInventory().setHelmet(seizureHelmet_red);
		} else {
			p.getInventory().setHelmet(seizureHelmet_red);
		}
	}
	
	public boolean isPlayerEnabled(UUID id) {
		return isPlayerEnabled(getSpecialInfo(id));
	}
	
	public boolean isPlayerEnabled(SpecialPlayerInfo info) {
		return !infos.contains(info) ? false : info.isEnabled();
	}
	
	public SpecialPlayerInfo getSpecialInfo(UUID id) {
		for (SpecialPlayerInfo info : infos) {
			if (info.getID().equals(id)) {
				return info;
			}
		}
		
		return null;
	}
	
	public enum SpecialPlayerEffect {
		antenna,
		seizureHelmet;
	}
}
