package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandParty implements CommandExecutor {

	private final Main main;
	
	public CommandParty(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 3 || args.length == 0) {
			return false;
		}
		
		Player pl = (Player) sender;
		
		if (args[0].equalsIgnoreCase("create") && args.length == 1) {
			if (main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are already in a party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.setColors("&cParty created!"));
			main.getPartyHandler().createParty(pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("invite") && args.length == 2) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are not in a party!"));
				return false;
			} else if (!main.getPartyHandler().isPartyLeader(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are not the party leader!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (main.getPartyHandler().doesPlayerHaveInvite(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cPlayer already has an invite!"));
				return false;
			} else if (main.getPartyHandler().isPartyLeader(Bukkit.getPlayer(args[1]).getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou cannot invite yourself!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.setColors("&cInvite sent!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.setColors("&cYou have received an invite from " + ((Player) sender).getDisplayName() + "!"));
			main.getPartyHandler().invitePlayer(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("join") && args.length == 2) {
			if (main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are already in a party!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (!main.getPartyHandler().doesPlayerHaveInvite(Bukkit.getPlayer(args[1]).getUniqueId(), pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou do not have an invite"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.setColors("&cYou joined the party!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.setColors("&c" + pl + " has joined the party!"));
			main.getPartyHandler().joinParty(Bukkit.getPlayer(args[1]).getUniqueId(), pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("leave") && args.length == 1) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are not in a party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.setColors("&cYou left the party!"));
			main.getPartyHandler().leaveParty(pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("kick") && args.length == 2) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are not in a party!"));
				return false;
			} else if (!main.getPartyHandler().isPartyLeader(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cYou are not the party leader!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (!main.getPartyHandler().isPlayerInCertainParty(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId())) {
				pl.sendMessage(ColorHelper.setColors("&cThat player is not in your party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.setColors("&cYou have kicked " + args[1]+ " from the party!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.setColors("&cYou have been kicked from the party!"));
			main.getPartyHandler().leaveParty(Bukkit.getPlayer(args[1]).getUniqueId());
			return true;
		}
		
		return false;
	}
}
