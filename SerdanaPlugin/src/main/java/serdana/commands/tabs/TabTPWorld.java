package main.java.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabTPWorld implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("" + ((Player) sender).getLocation().getBlockX());
		} else if (args.length == 2) {
			r.add("" + ((Player) sender).getLocation().getBlockY());
		} else if (args.length == 3) {
			r.add("" + ((Player) sender).getLocation().getBlockZ());
		} else if (args.length == 4) {
			for (World w : Bukkit.getWorlds()) {
				r.add(w.getName());
			}
		}
		
		List<String> finalResults = new ArrayList<String>();
		for (int i = 0; i < r.size(); i++) {
			if (r.get(i).toLowerCase().contains(args[args.length - 1].toLowerCase())) {
				finalResults.add(r.get(i));
			}
		}
		
		return finalResults;
	}
}
