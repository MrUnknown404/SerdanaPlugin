package main.java.com.mrunknown404.serdana.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.quests.EnumQuestState;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandQuest implements CommandExecutor {

	private final Main main;
	
	public CommandQuest(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show")) {
				if (!EnumQuestState.contains(args[1])) {
					sender.sendMessage(ColorHelper.setColors("&cUnknown quest type " + args[1] + "!"));
					return false;
				}
				
				List<Inventory> invs = main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(args[1]));
				
				if (Integer.parseInt(args[2]) >= invs.size() || invs.get(Integer.parseInt(args[2])) == null) {
					sender.sendMessage(ColorHelper.setColors("&cUnknown page!"));
					return false;
				}
				
				((Player) sender).openInventory(main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(args[1])).get(Integer.parseInt(args[2])));
				return true;
			}
		}
		
		return false;
	}
}