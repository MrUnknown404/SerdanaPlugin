package main.java.com.mrunknown404.serdana.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandSetTier implements CommandExecutor {

	private static final String COLOR_CODE = "&a";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		int tier;
		
		if (args.length == 1) {
			try {
				tier = Integer.parseInt(args[0]); 
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
		
		if (tier < 0) {
			sender.sendMessage(ColorHelper.setColors("&cTier \"" + tier + "\" is too low!"));
			return false;
		}
		
		if (tier > 32) {
			sender.sendMessage(ColorHelper.setColors("&cTier \"" + tier + "\" is too high!"));
			return false;
		}
		
		if (item.getType() == Material.AIR) {
			sender.sendMessage(ColorHelper.setColors("&cMust hold an item!"));
			return false;
		}
		
		if (new NBTItem(item).hasKey("tier")) {
			sender.sendMessage(ColorHelper.setColors("&cThis item already has a tier!"));
			return true;
		}
		
		List<String> lore = new ArrayList<String>();
		if (item.getItemMeta().hasLore()) {
			lore = item.getItemMeta().getLore();
			lore.set(lore.size() - 1, lore.get(lore.size() - 1) + ColorHelper.setColors(" " + COLOR_CODE + "[Tier " + tier + "]"));
		} else {
			lore.add(ColorHelper.setColors(COLOR_CODE + "[Tier " + tier + "]"));
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		NBTItem n = new NBTItem(item);
		n.setInteger("tier", tier);
		
		((Player) sender).getInventory().setItemInMainHand(n.getItem());
		return true;
	}

}
