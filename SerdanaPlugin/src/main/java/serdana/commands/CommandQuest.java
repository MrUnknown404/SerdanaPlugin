package main.java.serdana.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import main.java.serdana.Main;
import main.java.serdana.quests.InitQuests;
import main.java.serdana.quests.Quest;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.enums.EnumQuestState;

public class CommandQuest implements CommandExecutor {

	private final Main main;
	
	public CommandQuest(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("show")) {
				return show(sender, args[1], 0);
			} else if (args[0].equalsIgnoreCase("display")) {
				String quest = args[1].replaceAll("_", " ");
				
				if (!InitQuests.doesQuestExist(quest)) {
					sender.sendMessage(ColorHelper.addColor("&cUnknown quest " + args[1] + "!"));
					return false;
				}
				
				sendInfoToPlayer(InitQuests.getQuest(quest), (Player) sender);
				return true;
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("show")) {
				try {
					int page = Integer.parseInt(args[2]);
					return show(sender, args[1], page);
				} catch (NumberFormatException e) {
					return false;
				}
			}
			
			if (sender.hasPermission("serdana.quest.admin")) {
				if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("finish")) {
					String quest = args[2];
					
					if (quest.contains("_")) {
						quest = quest.replaceAll("_", " ");
					}
					
					if (!InitQuests.doesQuestExist(quest)) {
						sender.sendMessage(ColorHelper.addColor("&cUnknown quest " + args[2] + "!"));
						return false;
					} else if (Bukkit.getPlayer(args[1]) == null) {
						sender.sendMessage(ColorHelper.addColor("&cUnknown player " + args[1] + "!"));
						return false;
					}
					
					if (args[0].equalsIgnoreCase("give")) {
						if (!main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasAcceptedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.addColor("&cYou have given " + args[1] + " the quest " + quest + "!"));
							main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.accepted);
							return true;
						} else {
							sender.sendMessage(ColorHelper.addColor("&cPlayer already has that quest!"));
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasAcceptedQuest(InitQuests.getQuest(quest)) ||
								main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasFinishedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.addColor("&cYou have removed " + quest + " from " + args[1] + "!"));
							main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.unknown);
							return true;
						} else {
							sender.sendMessage(ColorHelper.addColor("&cPlayer does not have that quest!"));
						}
					} else if (args[0].equalsIgnoreCase("finish")) {
						if (main.getQuestHandler().getQuestPlayersData(Bukkit.getPlayer(args[1])).hasFinishedQuest(InitQuests.getQuest(quest))) {
							sender.sendMessage(ColorHelper.addColor("&cPlayer has already finished that quest!"));
							return false;
						}
						
						sender.sendMessage(ColorHelper.addColor("&cYou have finished " + quest + " for " + args[1] + "!"));
						main.getQuestHandler().setPlayersQuestState(Bukkit.getPlayer(args[1]), InitQuests.getQuest(quest), EnumQuestState.finished);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("edit") && args[2].equalsIgnoreCase("rewards")) {
					if (((Player) sender).getInventory().getItemInMainHand().getType() == Material.AIR) {
						sender.sendMessage(ColorHelper.addColor("&cMost hold an item!"));
						return false;
					} else if (!InitQuests.doesQuestExist(args[1])) {
						sender.sendMessage(ColorHelper.addColor("&cQuest does not exist!"));
						return false;
					}
					
					Quest q = new Quest(InitQuests.getQuest(args[1]));
					q.addReward(((Player) sender).getInventory().getItemInMainHand());
					
					sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
					InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
					return true;
				}
			}
		} else if (args.length >= 4) {
			if (sender.hasPermission("serdana.quest.admin")) {
				if (args[0].equalsIgnoreCase("create")) {
					String value = "";
					for (int i = 3; i < args.length; i++) {
						value += args[i];
						
						if (i != args.length - 1) {
							value += " ";
						}
					}
					
					if (!value.startsWith("\"") || !value.endsWith("\"")) {
						sender.sendMessage(ColorHelper.addColor("&cName doesn't start/end with \"!"));
						return false;
					}
					value = value.substring(1, value.length() - 1).replaceAll("_", " ");
					
					if (InitQuests.doesQuestExist(value)) {
						sender.sendMessage(ColorHelper.addColor("&cQuest already exist!"));
						return false;
					}
					
					int startID, finishID;
					
					try {
						startID = Integer.parseInt(args[1]);
						finishID = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ColorHelper.addColor("&c" + args[1] + " or " + args[2] + " is not a number!"));
						return false;
					}
					
					InitQuests.addNewQuest(new Quest(value, startID, finishID));
					sender.sendMessage(ColorHelper.addColor("&cSuccessfully added a new quest named : " + value));
					return true;
				} else if (args[0].equalsIgnoreCase("edit")) {
					if (!InitQuests.doesQuestExist(args[1])) {
						sender.sendMessage(ColorHelper.addColor("&cQuest does not exist!"));
						return false;
					}
					
					if (args[2].equalsIgnoreCase("description") || args[2].equalsIgnoreCase("startMessages") || args[2].equalsIgnoreCase("finishMessages") ||
							args[2].equalsIgnoreCase("turnInMessages") || args[2].equalsIgnoreCase("readyToTurnInMessages")) {
						if (args[3].equalsIgnoreCase("add")) {
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							
							String value = "";
							for (int i = 4; i < args.length; i++) {
								value += args[i];
								
								if (i != args.length - 1) {
									value += " ";
								}
							}
							
							if (!value.startsWith("\"") || !value.endsWith("\"")) {
								sender.sendMessage(ColorHelper.addColor("&cName doesn't start/end with \"!"));
								return false;
							}
							value = value.substring(1, value.length() - 1);
							
							if (args[2].equalsIgnoreCase("description")) {
								q.addDescription(value);
							} else if (args[2].equalsIgnoreCase("startMessages")) {
								q.addStartMessages(value);
							} else if (args[2].equalsIgnoreCase("finishMessages")) {
								q.addFinishedMessages(value);
							} else if (args[2].equalsIgnoreCase("turnInMessages")) {
								q.addTurnInMessages(value);
							} else if (args[2].equalsIgnoreCase("readyToTurnInMessages")) {
								q.addReadyToTurnInMessages(value);
							}
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						} else if (args[3].equalsIgnoreCase("remove")) {
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							
							int where;
							
							try {
								where = Integer.parseInt(args[4]);
							} catch (NumberFormatException e) {
								sender.sendMessage(ColorHelper.addColor("&cUnknown number : " + args[4]));
								return false;
							}
							
							if (args[2].equalsIgnoreCase("description")) {
								q.removeDescription(where);
							} else if (args[2].equalsIgnoreCase("startMessages")) {
								q.removeStartMessages(where);
							} else if (args[2].equalsIgnoreCase("finishMessages")) {
								q.removeFinishedMessages(where);
							} else if (args[2].equalsIgnoreCase("turnInMessages")) {
								q.removeTurnInMessages(where);
							} else if (args[2].equalsIgnoreCase("readyToTurnInMessages")) {
								q.removeReadyToTurnInMessages(where);
							}
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						}
					} else {
						if (args[2].equalsIgnoreCase("isActive")) {
							if (args[3].equalsIgnoreCase("true") || args[3].equalsIgnoreCase("false")) {
								Quest q = new Quest(InitQuests.getQuest(args[1]));
								q.setActive(Boolean.parseBoolean(args[3]));
								
								sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
								InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
								return true;
							}
						} else if (args[2].equalsIgnoreCase("name")) {
							String value = "";
							for (int i = 3; i < args.length; i++) {
								value += args[i];
								
								if (i != args.length - 1) {
									value += " ";
								}
							}
							
							if (!value.startsWith("\"") || !value.endsWith("\"")) {
								sender.sendMessage(ColorHelper.addColor("&cName doesn't start/end with \"!"));
								return false;
							}
							value = value.substring(1, value.length() - 1);
							
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							q.setName(value);
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						} else if (args[2].equalsIgnoreCase("startID")) {
							int startID;
							try {
								startID = Integer.parseInt(args[3]);
							} catch (NumberFormatException e) {
								return false;
							}
							
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							q.setStartID(startID);
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						} else if (args[2].equalsIgnoreCase("finishID")) {
							int finishID;
							try {
								finishID = Integer.parseInt(args[3]);
							} catch (NumberFormatException e) {
								return false;
							}
							
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							q.setFinishID(finishID);
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						} else if (args[2].equalsIgnoreCase("requirements")) {
							int requirements;
							try {
								requirements = Integer.parseInt(args[3]);
							} catch (NumberFormatException e) {
								return false;
							}
							
							Quest q = new Quest(InitQuests.getQuest(args[1]));
							q.addRequirements(requirements);
							
							sender.sendMessage(ColorHelper.addColor("&cSuccessfully added edited quest : " + args[1]));
							InitQuests.replaceQuest(InitQuests.getQuest(args[1]), q);
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private void sendInfoToPlayer(Quest q, Player p) {
		p.sendMessage(ColorHelper.addColor("&eQuestID: &f" + q.getQuestID()));
		
		if (q.isActive()) {
			p.sendMessage(ColorHelper.addColor("&eIsActive: &atrue"));
		} else {
			p.sendMessage(ColorHelper.addColor("&eIsActive: &cfalse"));
		}
		
		p.sendMessage(ColorHelper.addColor("&eName: &f" + q.getName()));
		p.sendMessage(ColorHelper.addColor("&eStartID: &f" + q.getStartID()));
		p.sendMessage(ColorHelper.addColor("&eFinishID: &f" + q.getFinishID()));
		
		if (q.getDescription() != null && q.getDescription().size() != 0) {
			for (int i = 0; i < q.getDescription().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&eopenMessages &6" + i + "&e: &f" + q.getDescription().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&eopenMessages: &fnull"));
		}
		
		if (q.getStartMessages() != null && q.getStartMessages().size() != 0) {
			for (int i = 0; i < q.getStartMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&estartMessages &6" + i + "&e: &f" + q.getStartMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&estartMessages: &fnull"));
		}
		
		if (q.getFinishMessages() != null && q.getFinishMessages().size() != 0) {
			for (int i = 0; i < q.getFinishMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&efinishMessages &6" + i + "&e: &f" + q.getFinishMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&efinishMessages: &fnull"));
		}
		
		if (q.getTurnInMessages() != null && q.getTurnInMessages().size() != 0) {
			for (int i = 0; i < q.getTurnInMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&eturnInMessages &6" + i + "&e: &f" + q.getTurnInMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&eturnInMessages: &fnull"));
		}
		
		if (q.getReadyToTurnInMessages() != null && q.getReadyToTurnInMessages().size() != 0) {
			for (int i = 0; i < q.getReadyToTurnInMessages().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&ereadyToTurnInMessages &6" + i + "&e: &f" + q.getReadyToTurnInMessages().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&ereadyToTurnInMessages: &fnull"));
		}
		
		if (q.getRequirements() != null && q.getRequirements().size() != 0) {
			for (int i = 0; i < q.getRequirements().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&erequirements &6" + i + "&e: &f" + q.getRequirements().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&erequirements: &fnull"));
		}
		
		if (q.getRewards() != null && q.getRewards().size() != 0) {
			for (int i = 0; i < q.getRewards().size(); i++) {
				p.sendMessage(ColorHelper.addColor("&erewards &6" + i + "&e: &f" + q.getRewards().get(i)));
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&erewards: &fnull"));
		}
	}
	
	private boolean show(CommandSender sender, String questState, int page) {
		if (!EnumQuestState.contains(questState)) {
			sender.sendMessage(ColorHelper.addColor("&cUnknown quest type " + questState + "!"));
			return false;
		}
		
		List<Inventory> invs = main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(questState));
		
		if (invs.size() == 0) {
			sender.sendMessage(ColorHelper.addColor("&cYou don't have any quests in that category!"));
			return false;
		} else if (page >= invs.size()) {
			sender.sendMessage(ColorHelper.addColor("&cUnknown page!"));
			return false;
		}
		
		((Player) sender).openInventory(main.getQuestHandler().getPlayersQuestGUIs((Player) sender, EnumQuestState.valueOf(questState)).get(page));
		return true;
	}
}
