package main.java.serdana.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.util.ShowItem;

public class CommandShowItem implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments");
			return false;
		}
		
		if (((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR) {
			new ShowItem((Player) sender);
			return true;
		}
		
		return false;
	}
}
