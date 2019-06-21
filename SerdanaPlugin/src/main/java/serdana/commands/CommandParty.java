package main.java.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

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
				pl.sendMessage(ColorHelper.addColor("&cYou are already in a party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.addColor("&cParty created!"));
			main.getPartyHandler().createParty(pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("invite") && args.length == 2) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are not in a party!"));
				return false;
			} else if (!main.getPartyHandler().isPartyLeader(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are not the party leader!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.addColor("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (main.getPartyHandler().doesPlayerHaveInvite(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cPlayer already has an invite!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]).getUniqueId() == ((Player) sender).getUniqueId()) {
				pl.sendMessage(ColorHelper.addColor("&cYou cannot invite yourself!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.addColor("&cInvite sent!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.addColor("&cYou have received an invite from " + ((Player) sender).getDisplayName() + "!"));
			main.getPartyHandler().invitePlayer(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("join") && args.length == 2) {
			if (main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are already in a party!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.addColor("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (!main.getPartyHandler().doesPlayerHaveInvite(Bukkit.getPlayer(args[1]).getUniqueId(), pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou do not have an invite from that player!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.addColor("&cYou joined the party!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.addColor("&c" + pl.getDisplayName() + " has joined the party!"));
			main.getPartyHandler().joinParty(Bukkit.getPlayer(args[1]).getUniqueId(), pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("leave") && args.length == 1) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are not in a party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.addColor("&cYou left the party!"));
			main.getPartyHandler().leaveParty(pl.getUniqueId());
			return true;
		} else if (args[0].equalsIgnoreCase("kick") && args.length == 2) {
			if (!main.getPartyHandler().isPlayerInAnyParty(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are not in a party!"));
				return false;
			} else if (!main.getPartyHandler().isPartyLeader(pl.getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cYou are not the party leader!"));
				return false;
			} else if (Bukkit.getPlayer(args[1]) == null) {
				pl.sendMessage(ColorHelper.addColor("&cUnknown player : " + args[1] + "!"));
				return false;
			} else if (!main.getPartyHandler().isPlayerInCertainParty(pl.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId())) {
				pl.sendMessage(ColorHelper.addColor("&cThat player is not in your party!"));
				return false;
			}
			
			pl.sendMessage(ColorHelper.addColor("&cYou have kicked " + args[1]+ " from the party!"));
			Bukkit.getPlayer(args[1]).sendMessage(ColorHelper.addColor("&cYou have been kicked from the party!"));
			main.getPartyHandler().leaveParty(Bukkit.getPlayer(args[1]).getUniqueId());
			return true;
		}
		
		return false;
	}
}
