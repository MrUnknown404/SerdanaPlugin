package main.java.serdana.util.specials;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.infos.SpecialPlayerInfo;

public class SpecialEffectAntenna implements ISpecialEffectBase {

	private final ItemStack disabled, enabled, blinking;
	
	protected final int maxStartBlink = 60, maxBlink = 20;
	private Map<UUID, Boolean> hasWalked = new HashMap<UUID, Boolean>(), isblinking = new HashMap<UUID, Boolean>();
	private Map<UUID, Integer> startBlink = new HashMap<UUID, Integer>(), blink = new HashMap<UUID, Integer>();
	
	public SpecialEffectAntenna() {
		disabled = new ItemStack(Material.LEVER);
		enabled = new ItemStack(Material.REDSTONE_TORCH);
		blinking = new ItemStack(Material.TORCH);
		
		ItemMeta metadisabled = disabled.getItemMeta();
		ItemMeta metaenabled= enabled.getItemMeta();
		ItemMeta metablinking = blinking.getItemMeta();
		
		metadisabled.setDisplayName(ColorHelper.addColor("&8disabled Antenna"));
		metaenabled.setDisplayName(ColorHelper.addColor("&cenabled Antenna"));
		metablinking.setDisplayName(ColorHelper.addColor("&6blinking Antenna"));
		metadisabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metaenabled.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		metablinking.setLore(Arrays.asList(ColorHelper.addColor("&8...")));
		
		disabled.setItemMeta(metadisabled);
		enabled.setItemMeta(metaenabled);
		blinking.setItemMeta(metablinking);
	}
	
	@Override
	public void start(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
				!p.getInventory().getHelmet().equals(blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(disabled);
		
		hasWalked.put(info.getID(), false);
		isblinking.put(info.getID(), false);
		startBlink.put(info.getID(), maxStartBlink);
		blink.put(info.getID(), maxBlink);
	}
	
	@Override
	public void tick(SpecialPlayerInfo info) {
		if (hasWalked.get(info.getID())) {
			hasWalked.put(info.getID(), false);
		} else {
			Player p = Bukkit.getPlayer(info.getID());
			
			if (!p.getInventory().getHelmet().equals(disabled)) {
				if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
						!p.getInventory().getHelmet().equals(blinking)) {
					p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
				}
				
				p.getInventory().setHelmet(disabled);
			}
			
			if (!isblinking.get(info.getID())) {
				if (startBlink.get(info.getID()) == 0) {
					startBlink.put(info.getID(), maxStartBlink);
					isblinking.put(info.getID(), true);
				} else {
					startBlink.put(info.getID(), startBlink.get(info.getID()) - 1);
				}
			} else {
				if (blink.get(info.getID()) == 0) {
					blink.put(info.getID(), maxBlink);
					
					if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
							!p.getInventory().getHelmet().equals(blinking)) {
						p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
					}
					
					p.getInventory().setHelmet(blinking);
				} else {
					blink.put(info.getID(), blink.get(info.getID()) - 1);
				}
			}
		}
	}
	
	public void doEffect(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(enabled) && !p.getInventory().getHelmet().equals(disabled) &&
				!p.getInventory().getHelmet().equals(blinking)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		}
		
		p.getInventory().setHelmet(enabled);
		
		hasWalked.put(info.getID(), true);
		isblinking.put(info.getID(), false);
		startBlink.put(info.getID(), maxStartBlink);
		blink.put(info.getID(), maxBlink);
	}
}
