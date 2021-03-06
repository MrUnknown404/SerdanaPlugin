package main.java.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import main.java.serdana.handlers.SpecialPlayerHandler.SpecialPlayerEffect;

public class TabSpecialEffect implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("toggle");
			r.add("set");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
			for (SpecialPlayerEffect effect : SpecialPlayerEffect.values()) {
				r.add(effect.toString());
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
