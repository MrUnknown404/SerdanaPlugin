package main.java.com.mrunknown404.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabPrayer implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> results = new ArrayList<String>();
		
		if (args.length == 1) {
			results.add("send");
			results.add("book");
			results.add("showUnsorted");
			results.add("showGood");
			results.add("showBad");
			results.add("setUnsorted");
			results.add("setGood");
			results.add("setBad");
			
			return results;
		}
		
		return null;
	}
}
