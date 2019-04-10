package main.java.com.mrunknown404.serdana.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.PrayInfo;

public class CommandPrayer implements CommandExecutor {
	
	private final Main main;
	
	public CommandPrayer(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		
		if ((args[0].equalsIgnoreCase("showUnsorted") || args[0].equalsIgnoreCase("showGood") || args[0].equalsIgnoreCase("showBad")) && args.length == 2) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException | NullPointerException nfe) {
				return false;
			}
		}
		
		Player p = ((Player) sender);
		
		if (args[0].equalsIgnoreCase("send") && sender.hasPermission("serdana.prayer.send") && args.length == 1) {
			if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) {
				p.sendMessage(ColorHelper.setColors("&cMust hold a written book!"));
				return false;
			}
			
			NBTItem n = new NBTItem(p.getInventory().getItemInMainHand());
			if (!n.hasKey("isPrayerBook")) {
				p.sendMessage(ColorHelper.setColors("&cHeld book must be a prayer book!"));
				return false;
			}
			
			main.getPrayerHandler().addPrayer(new PrayInfo(p.getUniqueId(), (BookMeta) p.getInventory().getItemInMainHand().getItemMeta()), "unsorted");
			p.getInventory().setItemInMainHand(null);
			return true;
		} else if (args[0].equalsIgnoreCase("book") && sender.hasPermission("serdana.prayer.book") && args.length == 1) {
			if (p.getInventory().firstEmpty() == -1) {
				p.sendMessage(ColorHelper.setColors("&cInventory is full!"));
				return false;
			}
			
			ItemStack item = new ItemStack(Material.WRITABLE_BOOK, 1);
			item.getItemMeta().setDisplayName(ColorHelper.setColors("&bPrayer Book"));
			
			NBTItem n = new NBTItem(item);
			n.setBoolean("isPrayerBook", true);
			
			p.getInventory().addItem(n.getItem());
			p.sendMessage(ColorHelper.setColors("&cPrayer book added!"));
			return true;
		} else if (args[0].equalsIgnoreCase("showUnsorted") && sender.hasPermission("serdana.prayer.showUnsorted") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getUnsortedInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) > inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if (args[0].equalsIgnoreCase("showGood") && sender.hasPermission("serdana.prayer.showGood") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getGoodInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) > inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if (args[0].equalsIgnoreCase("showBad") && sender.hasPermission("serdana.prayer.showBad") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getBadInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) > inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if (args[0].equalsIgnoreCase("setUnsorted") && sender.hasPermission("serdana.prayer.setUnsorted") && args.length == 1) {
			
		} else if (args[0].equalsIgnoreCase("setGood") && sender.hasPermission("serdana.prayer.setGood") && args.length == 1) {
			
		} else if (args[0].equalsIgnoreCase("setBad") && sender.hasPermission("serdana.prayer.setBad") && args.length == 1) {
			
		}
		
		return false;
	}
}
