package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJoin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = sender.getName();
		if (args.length == 1) {
			name = args[0];
		} else if (args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments");
			return false;
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.YELLOW + name + " joined the game");
		}
		
		return true;
	}
}
