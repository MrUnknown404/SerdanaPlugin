package main.java.serdana.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.serdana.Main;
import main.java.serdana.handlers.NPCHandler;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.infos.NPCInfo;

public class CommandNPC implements CommandExecutor {

	private final Main main;
	
	public CommandNPC(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0].equalsIgnoreCase("set")) {
			try {
				Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ColorHelper.addColor("&c" + args[1] + " is not a number!"));
				return false;
			}
			
			String value = "";
			for (int i = 3; i < args.length; i++) {
				value += args[i];
				
				if (i != args.length - 1) {
					value += " ";
				}
			}
			
			if (args[2].equalsIgnoreCase("name") && (!value.startsWith("\"") || !value.endsWith("\""))) {
				sender.sendMessage(ColorHelper.addColor("&cValue doesn't start/end with \"!"));
				return false;
			} else if (args[2].equalsIgnoreCase("name")) {
				value = value.substring(1, value.length() - 1);
			}
			
			NPCHandler hand = main.getNPCHandler();
			NPCInfo info = hand.getNPCInfoFromID(Integer.parseInt(args[1]));
			if (info == null) {
				Shopkeeper shop = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1]));
				if (shop != null) {
					sender.sendMessage(ColorHelper.addColor("&cCould not find any dialogue for that NPC! (creating now)"));
					main.getNPCHandler().addNPCDialogue(shop);
					info = main.getNPCHandler().getNPCInfoFromID(shop.getId());
				} else {
					sender.sendMessage(ColorHelper.addColor("&cCould not find a shop with that ID!"));
					return false;
				}
			}
			
			if (args[2].equalsIgnoreCase("name")) {
				info.setName(value);
			} else if (args[2].equalsIgnoreCase("isShop")) {
				value = value.toLowerCase();
				if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					info.setShop(Boolean.parseBoolean(value));
				}
			} else if (args[2].equalsIgnoreCase("ignoresBannedItems")) {
				value = value.toLowerCase();
				if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					info.setShop(Boolean.parseBoolean(value));
				}
			} else {
				sender.sendMessage(ColorHelper.addColor("&cUnknown value: " + args[2]));
				return false;
			}
			
			sender.sendMessage(ColorHelper.addColor("&cSuccessfully added \"" + value + "\" to \"" + args[2] + "\"!"));
			hand.write();
			return true;
		} else if (args[0].equalsIgnoreCase("dialogue")) {
			if (args[1].equalsIgnoreCase("add")) {
				if (args.length >= 5) {
					try {
						Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ColorHelper.addColor("&c" + args[2] + " is not a number!"));
						return false;
					}
					
					String value = "";
					for (int i = 4; i < args.length; i++) {
						value += args[i];
						
						if (i != args.length - 1) {
							value += " ";
						}
					}
					
					if (!value.startsWith("\"") || !value.endsWith("\"")) {
						sender.sendMessage(ColorHelper.addColor("&cValue doesn't start/end with \"!"));
						return false;
					}
					value = value.substring(1, value.length() - 1);
					
					NPCHandler hand = main.getNPCHandler();
					NPCInfo info = hand.getNPCInfoFromID(Integer.parseInt(args[2]));
					if (info == null) {
						Shopkeeper shop = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[2]));
						if (shop != null) {
							sender.sendMessage(ColorHelper.addColor("&cCould not find any dialogue for that NPC! (creating now)"));
							main.getNPCHandler().addNPCDialogue(shop);
							info = main.getNPCHandler().getNPCInfoFromID(shop.getId());
						} else {
							sender.sendMessage(ColorHelper.addColor("&cCould not find a shop with that ID!"));
							return false;
						}
					}
					
					if (args[3].equalsIgnoreCase("openMessages")) {
						info.addOpenMessages(value);
					} else if (args[3].equalsIgnoreCase("bannedItemMessages")) {
						info.addBannedItemMessages(value);
					} else if (args[3].equalsIgnoreCase("tradeMessages")) {
						info.addTradeMessages(value);
					} else {
						sender.sendMessage(ColorHelper.addColor("&cUnknown value: " + args[3]));
						return false;
					}
					
					sender.sendMessage(ColorHelper.addColor("&cSuccessfully set \"" + args[3] + "\" as \"" + value + "\"!"));
					hand.write();
					return true;
				}
			} else if (args[1].equalsIgnoreCase("remove") && args.length == 5) {
				try {
					Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ColorHelper.addColor("&c" + args[2] + " is not a number!"));
					return false;
				}
				
				try {
					Integer.parseInt(args[4]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ColorHelper.addColor("&c" + args[4] + " is not a number!"));
					return false;
				}
				
				NPCHandler hand = main.getNPCHandler();
				NPCInfo info = hand.getNPCInfoFromID(Integer.parseInt(args[2]));
				if (info == null) {
					Shopkeeper shop = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[2]));
					if (shop != null) {
						sender.sendMessage(ColorHelper.addColor("&cCould not find any dialogue for that NPC! (creating now)"));
						main.getNPCHandler().addNPCDialogue(shop);
						info = main.getNPCHandler().getNPCInfoFromID(shop.getId());
					} else {
						sender.sendMessage(ColorHelper.addColor("&cCould not find a shop with that ID!"));
						return false;
					}
				}
				
				
				if (args[3].equalsIgnoreCase("openMessages")) {
					if (Integer.parseInt(args[4]) >= info.getOpenMessages().size()) {
						sender.sendMessage(ColorHelper.addColor("&cNumber too high!"));
						return false;
					}
					
					info.getOpenMessages().remove(Integer.parseInt(args[4]));
				} else if (args[3].equalsIgnoreCase("bannedItemMessages")) {
					if (Integer.parseInt(args[4]) >= info.getBannedItemMessages().size()) {
						sender.sendMessage(ColorHelper.addColor("&cNumber too high!"));
						return false;
					}
					
					info.getBannedItemMessages().remove(Integer.parseInt(args[4]));
				} else if (args[3].equalsIgnoreCase("tradeMessages")) {
					if (Integer.parseInt(args[4]) >= info.getTradeMessages().size()) {
						sender.sendMessage(ColorHelper.addColor("&cNumber too high!"));
						return false;
					}
					
					info.getTradeMessages().remove(Integer.parseInt(args[4]));
				} else {
					sender.sendMessage(ColorHelper.addColor("&cUnknown value: " + args[3]));
					return false;
				}
				
				sender.sendMessage(ColorHelper.addColor("&cSuccessfully removed line \"" + args[4] + "\" from \"" + args[3] + "\"!"));
				hand.write();
				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("tp")) {
				try {
					Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ColorHelper.addColor("&c" + args[1] + " is not a number!"));
					return false;
				}
				
				if (args[0].equalsIgnoreCase("show")) {
					if (ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1])) != null) {
						sendInfoToPlayer(ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1])), (Player) sender);
						return true;
					} else {
						sender.sendMessage(ColorHelper.addColor("&cCould not find a shopkeeper with the ID " + args[1] + "!"));
						return false;
					}
				} else if (args[0].equalsIgnoreCase("tp")) {
					if (ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1])) != null) {
						if (ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1])).getLocation() == null) {
							sender.sendMessage(ColorHelper.addColor("&cWorld does not exist!"));
							return false;
						}
						
						sender.sendMessage(ColorHelper.addColor("&eFound shop and teleporting..."));
						((Player) sender).teleport(ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperById(Integer.parseInt(args[1])).getLocation());
						return true;
					} else {
						sender.sendMessage(ColorHelper.addColor("&cCould not find a shopkeeper with the ID " + args[1] + "!"));
						return false;
					}
				}
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("getAt")) {
				int x, y, z;
				
				if (args[1].equals("~")) {
					x = ((Player) sender).getLocation().getBlockX();
				} else {
					try {
						x = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ColorHelper.addColor("&c" + args[1] + " is not a number!"));
						return false;
					}
				}
				
				if (args[2].equals("~")) {
					y = ((Player) sender).getLocation().getBlockY();
				} else {
					try {
						y = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ColorHelper.addColor("&c" + args[2] + " is not a number!"));
						return false;
					}
				}
				
				if (args[3].equals("~")) {
					z = ((Player) sender).getLocation().getBlockZ();
				} else {
					try {
						z = Integer.parseInt(args[3]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ColorHelper.addColor("&c" + args[3] + " is not a number!"));
						return false;
					}
				}
				
				List<? extends Shopkeeper> shops = ShopkeepersAPI.getShopkeeperRegistry().getShopkeepersAtLocation(new Location(((Player) sender).getLocation().getWorld(), x, y, z));
				
				if (shops.isEmpty()) {
					sender.sendMessage(ColorHelper.addColor("&cCould not find any shops!"));
					return false;
				} else if (shops.size() >= 2) {
					sender.sendMessage(ColorHelper.addColor("&cFound too many shops! (Maybe one day i'll add support for this... i probably won't)"));
					return false;
				}
				
				sender.sendMessage(ColorHelper.addColor("&eFound shop with the ID: " + shops.get(0).getId()));
				return true;
			}
		}
		
		return false;
	}
	
	private void sendInfoToPlayer(Shopkeeper shop, Player p) {
		NPCInfo info = main.getNPCHandler().getNPCInfoFromID(shop.getId());
		
		if (info == null) {
			p.sendMessage(ColorHelper.addColor("&cCould not find any dialogue for that NPC! (creating now)"));
			main.getNPCHandler().addNPCDialogue(shop);
			info = main.getNPCHandler().getNPCInfoFromID(shop.getId());
		}
		
		p.sendMessage(ColorHelper.addColor("&eNPCID: &f" + info.getNPCID()));
		p.sendMessage(ColorHelper.addColor("&eName: &f" + info.getName()));
		
		if (info.ignoresBannedItems()) {
			p.sendMessage(ColorHelper.addColor("&eignoresBannedItems: &atrue"));
		} else {
			p.sendMessage(ColorHelper.addColor("&eignoresBannedItems: &cfalse"));
		}
		
		if (info.isShop()) {
			p.sendMessage(ColorHelper.addColor("&eisShop: &atrue"));
		} else {
			p.sendMessage(ColorHelper.addColor("&eisShop: &cfalse"));
		}
		
		if (info.getOpenMessages() != null && info.getOpenMessages().size() != 0) {
			for (int i = 0; i < info.getOpenMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&eopenMessages &6" + i + "&e: &f" + info.getOpenMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&eopenMessages: &fnull"));
		}
		
		if (info.getBannedItemMessages() != null && info.getBannedItemMessages().size() != 0) {
			for (int i = 0; i < info.getBannedItemMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&ebannedItemMessages &6" + i + "&e: &f" + info.getBannedItemMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&ebannedItemMessages: &fnull"));
		}
		
		if (info.getTradeMessages() != null && info.getTradeMessages().size() != 0) {
			for (int i = 0; i < info.getTradeMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&etradeMessages &6" + i + "&e: &f" + info.getTradeMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&etradeMessages: &fnull"));
		}
	}
}
