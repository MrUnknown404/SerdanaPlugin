package main.java.serdana.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CommandSetTier implements CommandExecutor {

	private final Main main;
	
	public CommandSetTier(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		int tier;
		
		if (args.length == 1) {
			try {
				tier = Integer.parseInt(args[0]); 
			} catch (NumberFormatException e) {
				sender.sendMessage(ColorHelper.setColors("&cUnknown number : " + args[0] + "!"));
				return false;
			}
			
			if (tier < 0) {
				sender.sendMessage(ColorHelper.setColors("&cTier \"" + tier + "\" is too low!"));
				return false;
			} else if (tier > 32) {
				sender.sendMessage(ColorHelper.setColors("&cTier \"" + tier + "\" is too high!"));
				return false;
			} else if (item.getType() == Material.AIR) {
				sender.sendMessage(ColorHelper.setColors("&cMust hold an item!"));
				return false;
			} else if (main.getTierHandler().isItemTiered(item)) {
				sender.sendMessage(ColorHelper.setColors("&cThis item already has a tier!"));
				return true;
			}
			
			((Player) sender).getInventory().setItemInMainHand(main.getTierHandler().addTierToItem(item, tier));
			return true;
		}
		
		return false;
	}
}
