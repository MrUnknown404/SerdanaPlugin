package main.java.com.mrunknown404.serdana.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandTimer implements CommandExecutor {

	private final Main main;
	private Map<UUID, Integer> players = new HashMap<UUID, Integer>();
	
	public CommandTimer(Main main) {
		this.main = main;
		startTimer();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player pl = null;
		
		if (args.length == 1 && sender.getClass().getSimpleName().equalsIgnoreCase("CraftBlockCommandSender")) { //a workaround but it works
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (pl == null) {
					pl = p;
				} else if (p.getLocation().distance(((CommandBlock) sender).getLocation()) < pl.getLocation().distance(((CommandBlock) sender).getLocation())) {
					pl = p;
				}
			}
		} else if (args.length == 2) {
			if (Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			}
			
			pl = Bukkit.getPlayer(args[1]);
		}
		
		if ((args.length == 1 && sender.getClass().getSimpleName().equalsIgnoreCase("CraftBlockCommandSender")) || args.length == 2) {
			if (args[0].equalsIgnoreCase("start")) {
				if (!players.containsKey(pl.getUniqueId())) {
					players.put(pl.getUniqueId(), 0);
					pl.sendMessage(ColorHelper.setColors("&cTimer started!"));
					return true;
				} else {
					pl.sendMessage(ColorHelper.setColors("&cTimer already started!"));
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				Iterator<Entry<UUID, Integer>> it = players.entrySet().iterator();
				
				while (it.hasNext()) {
					Entry<UUID, Integer> pair = it.next();
					
					if (pair.getKey() == pl.getUniqueId()) {
						pl.sendMessage(ColorHelper.setColors("&cTimer stopped at : " + pair.getValue() + "!"));
						players.remove(pl.getUniqueId());
						return true;
					}
				}
				
				pl.sendMessage(ColorHelper.setColors("&cThat player doesn't have a timer!"));
			}
		}
		
		return false;
	}
	
	public void startTimer() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				if (!players.isEmpty()) {
					Iterator<Entry<UUID, Integer>> it = players.entrySet().iterator();
					while (it.hasNext()) {
						Entry<UUID, Integer> pair = it.next();
						pair.setValue(pair.getValue() + 1);
					}
				}
			}
		}, 0L, 1L);
	}
	
	public Map<UUID, Integer> getPlayers() {
		return players;
	}
}
