package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCoo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments");
			return false;
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage("Cooo");
		}
		
		return true;
	}
}
