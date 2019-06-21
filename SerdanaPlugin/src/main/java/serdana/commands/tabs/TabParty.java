package main.java.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabParty implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("create");
			r.add("invite");
			r.add("join");
			r.add("leave");
			r.add("kick");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("kick")) {
				for (Player p :Bukkit.getOnlinePlayers()) {
					r.add(p.getName());
				}
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
