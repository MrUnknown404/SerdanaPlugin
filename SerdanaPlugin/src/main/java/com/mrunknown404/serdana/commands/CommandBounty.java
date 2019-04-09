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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		
		if (args.length != 2) {
			return false;
		}
		
		if (item.getType() == Material.AIR) {
			sender.sendMessage(ColorHelper.setColors("&cMust hold an item!"));
			return false;
		}
		
		NBTItem n = new NBTItem(item);
		if (!n.hasKey("bountyToken")) {
			sender.sendMessage(ColorHelper.setColors("&cMust hold a bounty token!"));
			return false;
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			Player p = null;
			if (Bukkit.getPlayer(args[1]) != null) {
				p = Bukkit.getPlayer(args[1]);
			} else {
				sender.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			}
			
			BountyInfo b = new BountyInfo(((Player) sender).getUniqueId(), p.getUniqueId(), item.getAmount());
			if (!Main.getBountyHandler().getBounties().contains(b)) {
				Main.getBountyHandler().addBounty(b);
				((Player) sender).getInventory().remove(((Player) sender).getInventory().getItemInMainHand());
			}
			return true;
		} else if (args[0].equalsIgnoreCase("cancel")) {
			Player p = null;
			if (Bukkit.getPlayer(args[1]) != null) {
				p = Bukkit.getPlayer(args[1]);
			} else {
				sender.sendMessage(ColorHelper.setColors("&cUnknown player : " + args[1] + "!"));
				return false;
			}
			
			Main.getBountyHandler().removeBounty(new BountyInfo(((Player) sender).getUniqueId(), p.getUniqueId(), item.getAmount()), false);
			return true;
		}
		
		return false;
	}
}
