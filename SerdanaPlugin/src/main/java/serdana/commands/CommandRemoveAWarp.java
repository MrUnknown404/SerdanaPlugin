package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CommandRemoveAWarp implements CommandExecutor {

	private final Main main;
	
	public CommandRemoveAWarp(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (!main.getAWarpHandler().getAllNames().contains(args[0])) {
				sender.sendMessage(ColorHelper.addColor("&cThat warp does not exist!"));
				return false;
			}
			
			sender.sendMessage(ColorHelper.addColor("&cYou have removed " + args[0] + "!"));
			main.getAWarpHandler().removeWarp(args[0]);
			return true;
		}
		
		return false;
	}
}
