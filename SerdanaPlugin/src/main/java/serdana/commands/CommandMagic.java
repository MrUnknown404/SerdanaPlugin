package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.magic.util.MagicItemHandler;
import main.java.serdana.magic.util.MagicItemHandler.MagicItems;
import main.java.serdana.util.ColorHelper;

public class CommandMagic implements CommandExecutor {

	private final Main main;
	
	public CommandMagic(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MagicItemHandler hand = main.getMagicItemHandler();
		
		if (args.length == 1) {
			if (((Player) sender).getInventory().getItemInMainHand() == null) {
				sender.sendMessage(ColorHelper.addColor("&cMost hold an item!"));
				return false;
			} else if (hand.getItemsMagic(((Player) sender).getInventory().getItemInMainHand()) == null) {
				sender.sendMessage(ColorHelper.addColor("&cThat item is not magic!"));
				return false;
			}
			
			sender.sendMessage(ColorHelper.addColor("&cThat item's mana has been refilled!"));
			((Player) sender).getInventory().setItemInMainHand(hand.refillMagic(((Player) sender).getInventory().getItemInMainHand()));
			return true;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
			if (MagicItems.valueOf(args[1]) == null) {
				sender.sendMessage(ColorHelper.addColor("&cUnknown magic type : " + args[1]));
				return false;
			} else if (((Player) sender).getInventory().getItemInMainHand() == null) {
				sender.sendMessage(ColorHelper.addColor("&cMost hold an item!"));
				return false;
			} else if (hand.getItemsMagic(((Player) sender).getInventory().getItemInMainHand()) != null) {
				sender.sendMessage(ColorHelper.addColor("&cThat item is already magic!"));
				return false;
			}
			
			sender.sendMessage(ColorHelper.addColor("&cThat item is now magic!"));
			((Player) sender).getInventory().setItemInMainHand(hand.addMagic(((Player) sender).getInventory().getItemInMainHand(), MagicItems.valueOf(args[1])));
			return true;
		}
		
		return false;
	}
}
