package main.java.serdana.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.quests.InitQuests;
import main.java.serdana.quests.Quest;
import main.java.serdana.util.enums.EnumQuestState;

public class TabQuest implements TabCompleter {
	
	private final Main main;
	
	public TabQuest(Main main) {
		this.main = main;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> r = new ArrayList<String>();
		
		if (args.length == 1) {
			r.add("show");
			if (sender.hasPermission("serdana.quest.admin")) {
				r.add("give");
				r.add("remove");
				r.add("finish");
				r.add("create");
				r.add("edit");
				r.add("display");
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show")) {
				for (EnumQuestState s : EnumQuestState.values()) {
					r.add(s.toString());
				}
			} else if (args[0].equalsIgnoreCase("display")) {
				for (Quest q : InitQuests.getAllQuests().get(true)) {
					r.add(q.getName());
				}
				
				for (Quest q : InitQuests.getAllQuests().get(false)) {
					r.add(q.getName().replaceAll(" ", "_"));
				}
			}
			
			if (sender.hasPermission("serdana.quest.admin")) {
				if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
					for (Player p :Bukkit.getOnlinePlayers()) {
						r.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("create")) {
					r.add("startID");
				} else if (args[0].equalsIgnoreCase("edit")) {
					for (Quest q : InitQuests.getAllQuests().get(true)) {
						r.add(q.getName());
					}
					for (Quest q : InitQuests.getAllQuests().get(false)) {
						r.add(q.getName());
					}
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show")) {
				r.add("#");
			}
			
			if (sender.hasPermission("serdana.quest.admin")) {
				if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
					List<Quest> quests = null;
					
					if (args[0].equalsIgnoreCase("give")) {
						quests = main.getQuestHandler().getQuestPlayersData((Player) sender).getQuestsThatAreNotState(EnumQuestState.accepted);
					} else if (args[0].equalsIgnoreCase("remove")) {
						quests = main.getQuestHandler().getQuestPlayersData((Player) sender).getQuestsThatAreNotState(EnumQuestState.unknown);
					} else if (args[0].equalsIgnoreCase("finish")) {
						quests = main.getQuestHandler().getQuestPlayersData((Player) sender).getQuestsThatAreNotState(EnumQuestState.finished);
					}
					
					for (Quest q : quests) {
						String txt = q.getName();
						
						if (txt.contains(" ")) {
							txt = txt.replaceAll(" ", "_");
						}
						
						r.add(txt);
					}
				} else if (args[0].equalsIgnoreCase("edit")) {
					r.add("description");
					r.add("startMessages");
					r.add("finishMessages");
					r.add("turnInMessages");
					r.add("readyToTurnInMessages");
					r.add("requirements");
					r.add("rewards");
					r.add("startID");
					r.add("finishID");
					r.add("name");
					r.add("isActive");
				} else if (args[0].equalsIgnoreCase("create")) {
					r.add("finishID");
				}
			}
		} else if (args.length == 4 && sender.hasPermission("serdana.quest.admin")) {
			if (args[0].equalsIgnoreCase("create")) {
				r.add("\"name\"");
			} else if (args[0].equalsIgnoreCase("edit")) {
				if (args[2].equalsIgnoreCase("name")) {
					r.add("\"\"");
				} else if (args[2].equalsIgnoreCase("startID") || args[2].equalsIgnoreCase("finishID") || args[2].equalsIgnoreCase("requirements")) {
					r.add("#");
				} else if (args[2].equalsIgnoreCase("isActive")) {
					r.add("true/false");
				} else if (args[2].equalsIgnoreCase("description") || args[2].equalsIgnoreCase("startMessages") || args[2].equalsIgnoreCase("finishMessages") ||
						args[2].equalsIgnoreCase("turnInMessages") || args[2].equalsIgnoreCase("readyToTurnInMessages")) {
					r.add("add");
					r.add("remove");
				}
			}
		} else if (args.length == 5 && sender.hasPermission("serdana.quest.admin")) {
			if (args[0].equalsIgnoreCase("edit") && args[3].equalsIgnoreCase("add")) {
				if (args[2].equalsIgnoreCase("description") || args[2].equalsIgnoreCase("startMessages") || args[2].equalsIgnoreCase("finishMessages") ||
						args[2].equalsIgnoreCase("turnInMessages") || args[2].equalsIgnoreCase("readyToTurnInMessages")) {
					r.add("\"\"");
				}
			} else if (args[0].equalsIgnoreCase("edit") && args[3].equalsIgnoreCase("remove")) {
				if (args[2].equalsIgnoreCase("description") || args[2].equalsIgnoreCase("startMessages") || args[2].equalsIgnoreCase("finishMessages") ||
						args[2].equalsIgnoreCase("turnInMessages") || args[2].equalsIgnoreCase("readyToTurnInMessages")) {
					r.add("#");
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
