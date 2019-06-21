package main.java.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabNPC implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("dialogue");
			r.add("set");
			r.add("show");
			r.add("getAt");
			r.add("tp");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("dialogue")) {
				r.add("add");
				r.add("remove");
			} else if (args[0].equalsIgnoreCase("getAt")) {
				r.add("" + ((Player) sender).getLocation().getBlockX());
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("set")) {
				r.add("#");
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("dialogue") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("show"))) {
				r.add("#");
			} else if (args[0].equalsIgnoreCase("set")) {
				r.add("ignoresBannedItems");
				r.add("isShop");
				r.add("name");
			} else if (args[0].equalsIgnoreCase("getAt")) {
				r.add("" + ((Player) sender).getLocation().getBlockY());
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("dialogue") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
				r.add("openMessages");
				r.add("bannedItemMessages");
				r.add("tradeMessages");
			} else if (args[0].equalsIgnoreCase("set")) {
				if (args[2].equalsIgnoreCase("ignoresBannedItems")) {
					r.add("true/false");
				} else if (args[2].equalsIgnoreCase("isShop")) {
					r.add("true/false");
				} else if (args[2].equalsIgnoreCase("name")) {
					r.add("\"\"");
				}
			} else if (args[0].equalsIgnoreCase("getAt")) {
				r.add("" + ((Player) sender).getLocation().getBlockZ());
			}
		} else if (args.length == 5) {
			if (args[0].equalsIgnoreCase("dialogue") && args[1].equalsIgnoreCase("add")) {
				if (args[3].equalsIgnoreCase("openMessages")) {
					r.add("\"\"");
				} else if (args[3].equalsIgnoreCase("bannedItemMessages")) {
					r.add("\"\"");
				} else if (args[3].equalsIgnoreCase("tradeMessages")) {
					r.add("\"\"");
				}
			} else if (args[0].equalsIgnoreCase("dialogue") && args[1].equalsIgnoreCase("remove")) {
				r.add("#");
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
