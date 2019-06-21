package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CommandToggleSpecial implements CommandExecutor {

	private final Main main;
	
	public CommandToggleSpecial(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.getName().equalsIgnoreCase("MrUnknown404")) {
			main.getSpecialPlayerHandler().togglePlayer("MrUnknown404");
			sender.sendMessage(ColorHelper.setColors("&cSpecial effect toggled!"));
			return true;
		} else {
			sender.sendMessage(ColorHelper.setColors("&cYou aren't special!"));
			return false;
		}
	}
}
