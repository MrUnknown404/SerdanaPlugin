package main.java.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CommandParasite implements CommandExecutor {

	private final Main main;
	
	public CommandParasite(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("give")) {
				main.getParasiteHandler().giveParasite((Player) sender, 1);
				return true;
			} else if (args[0].equalsIgnoreCase("stop")) {
				main.getParasiteHandler().killAll();
				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("give")) {
				if (Bukkit.getPlayer(args[1]) == null) {
					sender.sendMessage(ColorHelper.addColor("&cUnknown player : " + args[1]));
					return false;
				}
				
				main.getParasiteHandler().giveParasite(Bukkit.getPlayer(args[1]), 1);
				return true;
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (Bukkit.getPlayer(args[1]) == null) {
					sender.sendMessage(ColorHelper.addColor("&cUnknown player : " + args[1]));
					return false;
				}
				
				main.getParasiteHandler().kill(Bukkit.getPlayer(args[1]));
				return true;
			}
		}
		
		return false;
	}
}
