package main.java.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.util.ColorHelper;

public class CommandTPWorld implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int x, y, z;
		World w = Bukkit.getWorld(args[3]);
		
		try {
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ColorHelper.addColor("&cOne of the provided coords is not a number!"));
			return false;
		}
		
		if (w != null) {
			((Player) sender).teleport(new Location(w, x, y, z));
			return true;
		}
		
		return false;
	}
}
