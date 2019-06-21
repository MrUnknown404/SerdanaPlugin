package main.java.serdana.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;

public class CommandAWarps implements CommandExecutor {

	private final Main main;
	
	public CommandAWarps(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (main.getAWarpHandler().getAmountOfWarps() == 0) {
				sender.sendMessage(ColorHelper.setColors("&cThere are no warps!"));
				return false;
			}
			
			StringBuilder sb = new StringBuilder();
			List<String> names = main.getAWarpHandler().getAllNames();
			for (int i = 0; i < names.size(); i++) {
				sb.append(names.get(i));
				
				if (i != names.size() - 1) {
					sb.append(", ");
				}
			}
			
			sender.sendMessage(sb.toString());
			return true;
		}
		
		return false;
	}
}
