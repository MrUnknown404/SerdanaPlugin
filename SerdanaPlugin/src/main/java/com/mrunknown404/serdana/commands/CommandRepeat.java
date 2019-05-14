package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandRepeat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length <= 1) {
			return false;
		}
		
		try {
			Integer.parseInt(args[0]);
			
			if (Integer.parseInt(args[0]) > 1000) {
				sender.sendMessage(ColorHelper.setColors("&cThat number is too high!"));
				return false;
			}
		} catch (Exception e) {
			sender.sendMessage(ColorHelper.setColors("&cWhat you wrote isn't a number"));
			return false;
		}
		
		StringBuilder string = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			string.append(args[i]);
			
			if (i != args.length - 1) {
				string.append(" ");
			}
		}
		
		try {
			for (int i = 0; i < Integer.parseInt(args[0]); i++) {
				Bukkit.dispatchCommand(sender, string.toString());
			}
			return true;
		} catch (CommandException e) {
			sender.sendMessage(e.getMessage());
			return false;
		}
	}
}
