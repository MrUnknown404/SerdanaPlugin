package main.java.com.mrunknown404.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabPray implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			List<String> r = new ArrayList<String>();
			List<String> finalResults = new ArrayList<String>();
			
			r.add("send");
			r.add("book");
			if (sender.hasPermission("serdana.pray.show")) {
				r.add("showUnset");
				r.add("showGood");
				r.add("showBad");
			}
			if (sender.hasPermission("serdana.pray.set")) {
				r.add("setUnset");
				r.add("setGood");
				r.add("setBad");
			}
			
			for (int i = 0; i < r.size(); i++) {
				if (r.get(i).toLowerCase().contains(args[0].toLowerCase())) {
					finalResults.add(r.get(i));
				}
			}
			return finalResults;
		}
		
		return null;
	}
}
