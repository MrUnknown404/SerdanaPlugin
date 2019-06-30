package main.java.serdana.util.specials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.infos.SpecialPlayerInfo;

public class SpecialEffectSeizureHelmet implements ISpecialEffectBase {
	
	private final ItemStack red, blue;
	
	public SpecialEffectSeizureHelmet() {
		red = new ItemStack(Material.RED_STAINED_GLASS);
		blue = new ItemStack(Material.BLUE_STAINED_GLASS);
		
		ItemMeta metared= red.getItemMeta();
		ItemMeta metablue = blue.getItemMeta();
		
		metared.setDisplayName(ColorHelper.addColor("&cred Seizure Helmet"));
		metablue.setDisplayName(ColorHelper.addColor("&bblue Seizure Helmet"));
		
		red.setItemMeta(metared);
		blue.setItemMeta(metablue);
	}
	
	@Override
	public void start(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() != null && !p.getInventory().getHelmet().equals(blue) && !p.getInventory().getHelmet().equals(red)) {
			p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getHelmet());
		} else if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(red);
		}
	}
	
	@Override
	public void tick(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		
		if (p.getInventory().getHelmet() == null) {
			p.getInventory().setHelmet(red);
		} else if (p.getInventory().getHelmet().equals(red)) {
			p.getInventory().setHelmet(blue);
		} else if (p.getInventory().getHelmet().equals(blue)) {
			p.getInventory().setHelmet(red);
		} else {
			p.getInventory().setHelmet(red);
		}
	}
}
