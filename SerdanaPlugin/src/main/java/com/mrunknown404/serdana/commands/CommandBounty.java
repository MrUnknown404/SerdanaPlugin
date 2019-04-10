package main.java.com.mrunknown404.serdana.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.BountyInfo;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class CommandBounty implements CommandExecutor {
	private final Main main;
	
	public CommandBounty(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		
		if (args.length != 2) {
			return false;
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			if (item.getType() == Material.AIR) {
				sender.sendMessage(ColorHelper.setColors("&cMust hold an item!"));
				return false;
			}
			
			NBTItem n = new NBTItem(item);
			if (!n.hasKey("bountyToken")) {
				sender.sendMessage(ColorHelper.setColors("&cMust hold a bounty token!"));
				return false;
			}
			
			Player toKill = null;
			if (Bukkit.getPlayer(args[1]) != null) {
				toKill = Bukkit.getPlayer(args[1]);
			} else {
				sender.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			}
			
			BountyInfo b = new BountyInfo(((Player) sender).getUniqueId(), toKill.getUniqueId(), item.getAmount());
			for (int i = 0; i < main.getBountyHandler().getBounties().size(); i++) {
				if (!main.getBountyHandler().getBounties().get(i).getToKillUUID().equals(b.getToKillUUID())) {
					main.getBountyHandler().addBounty(b);
					((Player) sender).getInventory().setItemInMainHand(null);
					
					return true;
				}
			}
			
			((Player) sender).sendMessage(ColorHelper.setColors("&cA bounty already exists on that player!"));
		} else if (args[0].equalsIgnoreCase("cancel")) {
			Player p = null;
			if (Bukkit.getPlayer(args[1]) != null) {
				p = Bukkit.getPlayer(args[1]);
			} else {
				sender.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			}
			
			main.getBountyHandler().removeBounty(new BountyInfo(((Player) sender).getUniqueId(), p.getUniqueId(), item.getAmount()), false);
			return true;
		}
		
		return false;
	}
}
