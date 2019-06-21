package main.java.serdana.commands;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.Main;
import main.java.serdana.Main.Components;
import main.java.serdana.entities.util.EnumCustomEntities;
import main.java.serdana.util.ColorHelper;

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
			} else if (args[0].equalsIgnoreCase("test")) {
				if (main.isComponentEnabled(Main.Components.CustomEntities)) {
					main.getEntityHandler().spawnEntity(EnumCustomEntities.TEST_ZOMBIE, ((Player) sender).getLocation());
				}
			}
		}
		
		return false;
	}
}
