package main.java.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.serdana.util.ColorHelper;

public class CommandRainbow implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		
		String msg = "";
		for (int i = 0; i < args.length; i++) {
			msg += args[i] + " ";
		}
		
		StringBuilder sb = new StringBuilder(msg);
		int ti = 0;
		
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == ' ') {
				ti++;
			}
			
			sb.insert(i + (i * 2), "&" + ColorHelper.getRainbowCodes().charAt((i - ti) % ColorHelper.getRainbowCodes().length()));
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ColorHelper.addColor("<" + ((Player) sender).getDisplayName() + "&f> " + sb.toString()));
		}
		
		return true;
	}
}
