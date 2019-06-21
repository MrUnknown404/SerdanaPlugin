package main.java.serdana.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.Main;
import main.java.serdana.handlers.PrayerHandler;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.enums.EnumPrayerType;
import main.java.serdana.util.infos.PrayInfo;

public class CommandPray implements CommandExecutor {
	
	private final Main main;
	
	public CommandPray(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		
		int page = 0;
		if ((args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("showBad")) && args.length == 3) {
			try {
				page = Integer.parseInt(args[2]);
			} catch (NumberFormatException | NullPointerException nfe) {
				return false;
			}
		}
		
		Player p = ((Player) sender);
		PrayerHandler handler = main.getPrayerHandler();
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("send") && sender.hasPermission("serdana.pray.send")) {
				if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) {
					p.sendMessage(ColorHelper.setColors("&cMust hold a written book!"));
					return false;
				}
				
				NBTItem n = new NBTItem(p.getInventory().getItemInMainHand());
				if (!n.hasKey("isPrayerBook")) {
					p.sendMessage(ColorHelper.setColors("&cHeld book must be a prayer book!"));
					return false;
				}
				
				handler.addPrayer(new PrayInfo(p.getUniqueId(), p.getInventory().getItemInMainHand()), EnumPrayerType.unset);
				p.sendMessage(ColorHelper.setColors("&cPrayer sent!"));
				p.getInventory().setItemInMainHand(null);
				return true;
			} else if (args[0].equalsIgnoreCase("book") && sender.hasPermission("serdana.pray.book")) {
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
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set") && sender.hasPermission("serdana.pray.set")) {
				if (!EnumPrayerType.contains(args[1])) {
					sender.sendMessage(ColorHelper.setColors("&cUnknown prayer type " + args[1] + "!"));
					return false;
				}
				
				EnumPrayerType newType = EnumPrayerType.valueOf(args[1]);
				
				if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) {
					p.sendMessage(ColorHelper.setColors("&cMust hold a prayer book!"));
					return false;
				}
				
				PrayInfo info = search(newType, p.getInventory().getItemInMainHand());
				if (info != null) {
					p.sendMessage(ColorHelper.setColors("&cThat prayer is already set to that!"));
					return false;
				}
				
				List<EnumPrayerType> notThese = new ArrayList<EnumPrayerType>();
				for (EnumPrayerType t : EnumPrayerType.values()) {
					if (!t.name().equals(newType.name())) {
						notThese.add(t);
					}
				}
				
				for (EnumPrayerType oldType : notThese) {
					info = search(oldType, p.getInventory().getItemInMainHand());
					
					if (info != null) {
						main.getPrayerHandler().removePrayer(info, oldType);
						main.getPrayerHandler().addPrayer(info, newType);
						p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
						return true;
					}
				}
				
				p.sendMessage(ColorHelper.setColors("&cCould not find that prayer!"));
				return false;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show") && sender.hasPermission("serdana.pray.show")) {
				if (!EnumPrayerType.contains(args[1])) {
					sender.sendMessage(ColorHelper.setColors("&cUnknown prayer type " + args[1] + "!"));
					return false;
				}
				
				EnumPrayerType type = EnumPrayerType.valueOf(args[1]);
				
				List<Inventory> inv = null;
				if (type == EnumPrayerType.unset) {
					inv = main.getPrayerHandler().getUnsetInventories();
				} else if (type == EnumPrayerType.good) {
					inv = main.getPrayerHandler().getGoodInventories();
				} else if (type == EnumPrayerType.bad) {
					inv = main.getPrayerHandler().getBadInventories();
				}
				
				if (inv == null) {
					return false;
				} else if (inv.isEmpty()) {
					p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
					return false;
				} else if (page >= inv.size() || inv.get(page) == null) {
					p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
					return false;
				}
				
				p.openInventory(inv.get(page));
				return true;
			}
		}
		
		return false;
	}
	
	/** Searches in the given {@link EnumPrayerType}'s {@link Inventory} for the given {@link ItemStack}
	 * @param type Prayer type to search in
	 * @param itemToFind The ItemStack to find
	 * @return The PrayerInfo the given ItemStack is in
	 */
	private PrayInfo search(EnumPrayerType type, ItemStack itemToFind) {
		List<Inventory> inv = main.getPrayerHandler().getUnsetInventories();
		
		if (type == EnumPrayerType.unset) {
			inv = main.getPrayerHandler().getUnsetInventories();
		} else if (type == EnumPrayerType.good) {
			inv = main.getPrayerHandler().getGoodInventories();
		} else if (type == EnumPrayerType.bad) {
			inv = main.getPrayerHandler().getBadInventories();
		}
		
		for (int i = 0; i < inv.size(); i++) {
			for (int j = 0; j < inv.get(i).getSize(); j++) {
				ItemStack item = inv.get(i).getItem(j);
				
				if (item != null) {
					if (item.isSimilar(itemToFind)) {
						if (type == EnumPrayerType.unset) {
							return main.getPrayerHandler().getUnsetPrayers().get(i);
						} else if (type == EnumPrayerType.good) {
							return main.getPrayerHandler().getGoodPrayers().get(i);
						} else if (type == EnumPrayerType.bad) {
							return main.getPrayerHandler().getBadPrayers().get(i);
						}
					}
				}
			}
		}
		
		return null;
	}
}
