package main.java.serdana.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandUnbreakable implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 0) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments");
			return false;
		}
		
		if (((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR) {
			ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			
			if (!meta.isUnbreakable()) {
				meta.setUnbreakable(true);
			} else {
				meta.setUnbreakable(false);
			}
			
			item.setItemMeta(meta);
			
			return true;
		}
		
		return false;
	}
}
