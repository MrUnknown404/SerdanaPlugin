package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.serdana.Main;
import main.java.serdana.handlers.SpecialPlayerHandler.SpecialPlayers;
import main.java.serdana.util.ColorHelper;

public class CommandToggleSpecial implements CommandExecutor {

	private final Main main;
	
	public CommandToggleSpecial(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (SpecialPlayers player : SpecialPlayers.values()) {
			if (sender.getName().equalsIgnoreCase(player.getName())) {
				main.getSpecialPlayerHandler().togglePlayer(player);
				sender.sendMessage(ColorHelper.addColor("&cSpecial effect toggled!"));
				return true;
			}
		}
		
		sender.sendMessage(ColorHelper.addColor("&cYou aren't special!"));
		return false;
	}
}
