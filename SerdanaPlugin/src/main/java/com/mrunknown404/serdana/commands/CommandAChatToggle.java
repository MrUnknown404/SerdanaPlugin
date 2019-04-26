package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandAChatToggle implements CommandExecutor {

	private final Main main;
	
	public CommandAChatToggle(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(ColorHelper.setColors("&cAdmin chat toggled!"));
		main.getAChatHandler().togglePlayer(((Player) sender).getUniqueId());
		return true;
	}
}
