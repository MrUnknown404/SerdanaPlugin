package main.java.serdana.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.scripts.InitScripts;
import main.java.serdana.scripts.ScriptHandler;
import main.java.serdana.util.ColorHelper;

public class CommandTestScript implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (InitScripts.doesScriptExist(args[0])) {
				ScriptHandler.read(InitScripts.getScript(args[0]), (Player) sender);
				sender.sendMessage(ColorHelper.addColor("&cSuccessfully ran : " + args[0]));
				return true;
			} else {
				sender.sendMessage(ColorHelper.addColor("&cUnknown script : " + args[0]));
			}
		}
		
		return false;
	}
}
