package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.com.mrunknown404.serdana.Main;

public class CommandSerdana implements CommandExecutor {

	private Main main;
	
	public CommandSerdana(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0 || args.length >= 2) {
			return false;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			main.reload(sender);
			return true;
		} else {
			return false;
		}
	}
}
