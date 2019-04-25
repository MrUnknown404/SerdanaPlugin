package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandAddAWarp implements CommandExecutor {

	private final Main main;
	
	public CommandAddAWarp(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(ColorHelper.setColors("&cYou have set " + args[0] + "!"));
			main.getAWarpHandler().setWarp(((Player) sender).getLocation(), args[0]);
			return true;
		}
		
		return false;
	}
}
