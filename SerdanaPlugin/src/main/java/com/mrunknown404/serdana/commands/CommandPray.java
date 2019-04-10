package main.java.com.mrunknown404.serdana.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.EnumPrayerType;
import main.java.com.mrunknown404.serdana.util.PrayInfo;

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
		
		if ((args[0].equalsIgnoreCase("showUnset") || args[0].equalsIgnoreCase("showGood") || args[0].equalsIgnoreCase("showBad")) && args.length == 2) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException | NullPointerException nfe) {
				return false;
			}
		}
		
		Player p = ((Player) sender);
		
		if (args[0].equalsIgnoreCase("send") && sender.hasPermission("serdana.pray.send") && args.length == 1) {
			if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) {
				p.sendMessage(ColorHelper.setColors("&cMust hold a written book!"));
				return false;
			}
			
			NBTItem n = new NBTItem(p.getInventory().getItemInMainHand());
			if (!n.hasKey("isPrayerBook")) {
				p.sendMessage(ColorHelper.setColors("&cHeld book must be a prayer book!"));
				return false;
			}
			
			main.getPrayerHandler().addPrayer(new PrayInfo(p.getUniqueId(), p.getInventory().getItemInMainHand()), EnumPrayerType.unset);
			p.sendMessage(ColorHelper.setColors("&cPrayer sent!"));
			p.getInventory().setItemInMainHand(null);
			return true;
		} else if (args[0].equalsIgnoreCase("book") && sender.hasPermission("serdana.pray.book") && args.length == 1) {
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
		} else if (args[0].equalsIgnoreCase("showUnset") && sender.hasPermission("serdana.pray.showUnset") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getUnsortedInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) >= inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if (args[0].equalsIgnoreCase("showGood") && sender.hasPermission("serdana.pray.showGood") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getGoodInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) >= inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if (args[0].equalsIgnoreCase("showBad") && sender.hasPermission("serdana.pray.showBad") && args.length == 2) {
			List<Inventory> inv = main.getPrayerHandler().getBadInventories();
			if (inv.isEmpty()) {
				p.sendMessage(ColorHelper.setColors("&cThere are no prayers!"));
				return false;
			} else if (Integer.parseInt(args[1]) >= inv.size() || inv.get(Integer.parseInt(args[1])) == null) {
				p.sendMessage(ColorHelper.setColors("&cUnknown page!"));
				return false;
			}
			
			p.openInventory(inv.get(Integer.parseInt(args[1])));
			return true;
		} else if ((args[0].equalsIgnoreCase("setUnset") || args[0].equalsIgnoreCase("setGood") || args[0].equalsIgnoreCase("setBad")) && args.length == 1) {
			if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) {
				p.sendMessage(ColorHelper.setColors("&cMust hold a prayer book!"));
				return false;
			}
			
			if (args[0].equalsIgnoreCase("setUnset") && sender.hasPermission("serdana.pray.setUnset")) {
				PrayInfo info = search(EnumPrayerType.unset, p.getInventory().getItemInMainHand());
				if (info != null) {
					p.sendMessage(ColorHelper.setColors("&cThat prayer is already set to that!"));
					return false;
				}
				
				info = search(EnumPrayerType.good, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.good);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.unset);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
				
				info = search(EnumPrayerType.bad, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.bad);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.unset);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
			} else if (args[0].equalsIgnoreCase("setGood") && sender.hasPermission("serdana.pray.setGood")) {
				PrayInfo info = search(EnumPrayerType.good, p.getInventory().getItemInMainHand());
				if (info != null) {
					p.sendMessage(ColorHelper.setColors("&cThat prayer is already set to that!"));
					return false;
				}
				
				info = search(EnumPrayerType.unset, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.unset);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.good);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
				
				info = search(EnumPrayerType.bad, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.bad);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.good);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
			} else if (args[0].equalsIgnoreCase("setBad") && sender.hasPermission("serdana.pray.setBad")) {
				PrayInfo info = search(EnumPrayerType.bad, p.getInventory().getItemInMainHand());
				if (info != null) {
					p.sendMessage(ColorHelper.setColors("&cThat prayer is already set to that!"));
					return false;
				}
				
				info = search(EnumPrayerType.unset, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.unset);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.bad);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
				
				info = search(EnumPrayerType.good, p.getInventory().getItemInMainHand());
				if (info != null) {
					main.getPrayerHandler().removePrayer(info, EnumPrayerType.good);
					main.getPrayerHandler().addPrayer(info, EnumPrayerType.bad);
					p.sendMessage(ColorHelper.setColors("&cPrayer successfully moved!"));
					return true;
				}
			}
			
			p.sendMessage(ColorHelper.setColors("&cCould not find that prayer!"));
			return false;
		}
		
		return false;
	}
	
	public PrayInfo search(EnumPrayerType type, ItemStack item) {
		List<Inventory> inv = main.getPrayerHandler().getUnsortedInventories();
		
		if (type == EnumPrayerType.unset) {
			inv = main.getPrayerHandler().getUnsortedInventories();
		} else if (type == EnumPrayerType.good) {
			inv = main.getPrayerHandler().getGoodInventories();
		} else if (type == EnumPrayerType.bad) {
			inv = main.getPrayerHandler().getBadInventories();
		}
		
		for (int i = 0; i < inv.size(); i++) {
			for (int j = 0; j < inv.get(i).getSize(); j++) {
				ItemStack itm = inv.get(i).getItem(j);
				
				if (itm != null) {
					if (itm.isSimilar(item)) {
						if (type == EnumPrayerType.unset) {
							return main.getPrayerHandler().getUnsortedPrayers().get(i);
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
