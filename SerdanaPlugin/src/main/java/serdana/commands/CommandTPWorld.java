package main.java.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import main.java.serdana.util.ColorHelper;

public class CommandTPWorld implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		float x, y, z;
		World w = Bukkit.getWorld(args[3]);
		
		try {
			x = Float.parseFloat(args[0]);
			y = Float.parseFloat(args[1]);
			z = Float.parseFloat(args[2]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ColorHelper.addColor("&cOne of the provided coords is not a number!"));
			return false;
		}
		
		if (w != null) {
			if (sender instanceof BlockCommandSender) {
				Location loc = ((BlockCommandSender) sender).getBlock().getLocation();
				
				for (Entity p : w.getNearbyEntities(new BoundingBox(loc.getX() - 2, loc.getY() - 2, loc.getZ() - 2, loc.getX() + 3, loc.getY() + 3, loc.getZ() + 3))) {
					if (p instanceof Player) {
						p.teleport(new Location(w, x, y, z));
					}
				}
			} else {
				((Player) sender).teleport(new Location(w, x, y, z));
			}
			return true;
		} else {
			sender.sendMessage(ColorHelper.addColor("&cUnknown world : " + args[3]));
			return false;
		}
	}
}
