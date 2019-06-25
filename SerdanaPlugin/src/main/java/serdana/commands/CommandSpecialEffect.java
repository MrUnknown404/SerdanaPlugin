package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.handlers.SpecialPlayerHandler.SpecialPlayerEffect;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.infos.SpecialPlayerInfo;

public class CommandSpecialEffect implements CommandExecutor {

	private final Main main;
	
	public CommandSpecialEffect(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		SpecialPlayerInfo info = main.getSpecialPlayerHandler().getSpecialInfo(((Player) sender).getUniqueId());
		
		if (info == null) {
			info = main.getSpecialPlayerHandler().setupNewPlayer(((Player) sender).getUniqueId(), false);
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
			main.getSpecialPlayerHandler().togglePlayer(((Player) sender).getUniqueId());
			sender.sendMessage(ColorHelper.addColor("&cSpecial effect toggled!"));
			return true;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
			for (SpecialPlayerEffect effect : SpecialPlayerEffect.values()) {
				if (args[1].equalsIgnoreCase(effect.toString())) {
					main.getSpecialPlayerHandler().setEffect(info.getID(), effect);
					sender.sendMessage(ColorHelper.addColor("&cSpecial effect set to : " + args[1] + "!"));
					return true;
				}
			}
			
			sender.sendMessage(ColorHelper.addColor("&cUnknown value : " + args[1]));
			return false;
		}
		
		return false;
	}
}
