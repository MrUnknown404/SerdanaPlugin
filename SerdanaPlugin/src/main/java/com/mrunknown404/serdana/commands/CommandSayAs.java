package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandSayAs implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = args[0];
		if (args.length >= 2) {
			name = ColorHelper.setColors(name);
			
			String msg = "";
			for (int i = 1; i < args.length; i++) {
				msg += args[i] + " ";
			}
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage("<" + name + "\u00a7f> " + msg);
			}
			
			return true;
		}
		
		return false;
	}
}
