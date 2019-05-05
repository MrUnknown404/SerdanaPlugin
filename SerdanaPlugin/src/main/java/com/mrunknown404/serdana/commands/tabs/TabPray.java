package main.java.com.mrunknown404.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import main.java.com.mrunknown404.serdana.util.enums.EnumPrayerType;

public class TabPray implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("send");
			r.add("book");
			if (sender.hasPermission("serdana.pray.show")) {
				r.add("show");
			}
			if (sender.hasPermission("serdana.pray.set")) {
				r.add("set");
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("set")) {
				for (EnumPrayerType s : EnumPrayerType.values()) {
					r.add(s.toString());
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show") && sender.hasPermission("serdana.pray.show")) {
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
