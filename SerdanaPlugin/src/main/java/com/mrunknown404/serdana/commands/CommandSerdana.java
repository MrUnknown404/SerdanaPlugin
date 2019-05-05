package main.java.com.mrunknown404.serdana.commands;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.Main.Components;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandSerdana implements CommandExecutor {

	private Main main;
	
	public CommandSerdana(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				main.reload(sender);
				return true;
			} else if (args[0].equalsIgnoreCase("components")) {
				Iterator<Entry<Components, Boolean>> it = main.getComponents().entrySet().iterator();
				
				while (it.hasNext()) {
					Entry<Components, Boolean> pair = it.next();
					
					String code = "&c";
					if (pair.getValue()) {
						code = "&a";
					}
					
					sender.sendMessage(ColorHelper.setColors(pair.getKey() + ":" + code + pair.getValue()));
				}
				
				return true;
			}
		}
		
		return false;
	}
}
