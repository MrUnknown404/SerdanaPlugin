package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.scripts.InitScripts;
import main.java.com.mrunknown404.serdana.scripts.ScriptHandler;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandTestScript implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (InitScripts.doesScriptExist(args[0])) {
				ScriptHandler.read(InitScripts.getScript(args[0]), (Player) sender);
				sender.sendMessage(ColorHelper.setColors("&cSuccessfully ran : " + args[0]));
				return true;
			} else {
				sender.sendMessage(ColorHelper.setColors("&cUnknown script : " + args[0]));
			}
		}
		
		return false;
	}
}
