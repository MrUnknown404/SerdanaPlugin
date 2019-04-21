package main.java.com.mrunknown404.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.quests.EnumQuestState;
import main.java.com.mrunknown404.serdana.quests.InitQuests;
import main.java.com.mrunknown404.serdana.quests.Quest;

public class TabQuest implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("show");
			r.add("give");
			r.add("remove");
			r.add("finish");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show")) {
				for (EnumQuestState s : EnumQuestState.values()) {
					r.add(s.toString());
				}
			} else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
				for (Player p :Bukkit.getOnlinePlayers()) {
					r.add(p.getName());
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show")) {
				r.add("#");
			} else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
				for (Quest q : InitQuests.getAllQuests().get(true)) {
					String txt = q.getName();
					
					if (txt.contains(" ")) {
						txt = txt.replaceAll(" ", "_");
					}
					
					r.add(txt);
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
