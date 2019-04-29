package main.java.com.mrunknown404.serdana.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.quests.EnumQuestState;
import main.java.com.mrunknown404.serdana.quests.InitQuests;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandQuest implements CommandExecutor {

	private final Main main;
	
	public CommandQuest(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show")) {
				try {
					return show(sender, args[1], 0);
				} catch (NumberFormatException e) {
					return false;
				}
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show")) {
				try {
					int page = Integer.parseInt(args[2]);
					return show(sender, args[1], page);
				} catch (NumberFormatException e) {
					return false;
				}
			}
			
			if (sender.hasPermission("serdana.quest.admin")) {
				if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
					String quest = args[2];
					
					if (quest.contains("_")) {
						quest = quest.replaceAll("_", " ");
					}
					
					if (!InitQuests.doesQuestExist(quest)) {
						sender.sendMessage(ColorHelper.setColors("&cUnknown quest " + args[2] + "!"));
						return false;
					} else if (Bukkit.getPlayer(args[1]) == null) {
						sender.sendMessage(ColorHelper.setColors("&cUnknown player " + args[1] + "!"));
						return false;
					}
					
					if (args[0].equalsIgnoreCase("give")) {
						if (!main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasAcceptedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.setColors("&cYou have given " + args[1] + " the quest " + quest + "!"));
							main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.accepted);
							return true;
						} else {
							sender.sendMessage(ColorHelper.setColors("&cPlayer already has that quest!"));
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasAcceptedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.setColors("&cYou have removed " + quest + " from " + args[1] + "!"));
							main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.unknown);
							return true;
						} else {
							sender.sendMessage(ColorHelper.setColors("&cPlayer does not have that quest!"));
						}
					} else if (args[0].equalsIgnoreCase("finish")) {
						if (main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasFinishedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.setColors("&cPlayer has already finished that quest!"));
							return false;
						}
						
						sender.sendMessage(ColorHelper.setColors("&cYou have finished " + quest + " for " + args[1] + "!"));
						main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.finished);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean show(CommandSender sender, String questState, int page) {
		if (!EnumQuestState.contains(questState)) {
			sender.sendMessage(ColorHelper.setColors("&cUnknown quest type " + questState + "!"));
			return false;
		}
		
		List<Inventory> invs = main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(questState));
		
		if (invs.size() == 0) {
			sender.sendMessage(ColorHelper.setColors("&cYou don't have any quests in that category!"));
			return false;
		} else if (page >= invs.size()) {
			sender.sendMessage(ColorHelper.setColors("&cUnknown page!"));
			return false;
		}
		
		((Player) sender).openInventory(main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(questState)).get(page));
		return true;
	}
}
