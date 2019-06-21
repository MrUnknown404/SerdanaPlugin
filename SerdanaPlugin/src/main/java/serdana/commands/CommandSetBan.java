package main.java.serdana.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.util.ColorHelper;

public class CommandSetBan implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			return false;
		}
		
		ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
		
		if (item.getType() == Material.AIR) {
			return false;
		}
		
		NBTItem n = new NBTItem(item);
		
		if (n.hasKey("banned")) {
			sender.sendMessage(ColorHelper.setColors("&cItem was already banned!"));
			return false;
		}
		
		n.setBoolean("banned", true);
		((Player) sender).getInventory().setItemInMainHand(n.getItem());
		
		return true;
	}
}
