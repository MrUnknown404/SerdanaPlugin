package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandAWarp implements CommandExecutor {

	private final Main main;
	
	public CommandAWarp(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (!main.getAWarpHandler().getAllNames().contains(args[0])) {
				sender.sendMessage(ColorHelper.setColors("&cThat warp does not exist!"));
				return false;
			}
			
			sender.sendMessage(ColorHelper.setColors("&cYou have teleported to " + args[0] + "!"));
			((Player) sender).teleport(main.getAWarpHandler().getWarp(args[0]));
			return true;
		}
		
		return false;
	}
}
