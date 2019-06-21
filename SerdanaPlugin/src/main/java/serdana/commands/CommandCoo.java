package main.java.serdana.commands;

import java.util.Random;

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
		
		StringBuilder sb = new StringBuilder("Coo");
		
		for (int i = 0; i < new Random().nextInt(3); i++) {
			sb.append("o");
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(sb.toString());
		}
		
		return true;
	}
}
