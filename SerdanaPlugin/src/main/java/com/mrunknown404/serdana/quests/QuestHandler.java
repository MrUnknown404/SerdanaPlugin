package main.java.com.mrunknown404.serdana.quests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTask;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskFetch;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskKill;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskWalk;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;
import main.java.com.mrunknown404.serdana.util.QuestState;

public class QuestHandler {

	private final Main main;
	private final File path;
	
	private List<QuestPlayerData> playersQuests = new ArrayList<QuestPlayerData>();
	private Map<UUID, List<Inventory>> questInventoriesUnknown = new HashMap<UUID, List<Inventory>>();
	private Map<UUID, List<Inventory>> questInventoriesAccepted = new HashMap<UUID, List<Inventory>>();
	private Map<UUID, List<Inventory>> questInventoriesFinished = new HashMap<UUID, List<Inventory>>();
	
	public QuestHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	public void reloadPlayer(Player p) {
		if (playersQuests.isEmpty()) {
			return;
		}
		
		setupPlayer(p);
	}
	
	public void reloadAll() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			setupPlayer(p);
		}
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	public void checkAllTask(EnumTaskCheckType type) { int runthis;
		for (Player p : Bukkit.getOnlinePlayers()) {
			QuestPlayerData info = getQuestPlayersData(p);
			
			for (Quest q : info.getQuests()) {
				if (q.getCurrentTask().getTaskCheckType() == type) {
					q.check(Bukkit.getPlayer(info.getPlayerUUID()));
				}
			}
		}
	}
	
	public void checkTask(Player p, EnumTaskCheckType type) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		for (Quest q : info.getQuests()) {
			if (q.getCurrentTask().getTaskCheckType() == type) {
				q.check(Bukkit.getPlayer(info.getPlayerUUID()));
			}
		}
	}
	
	public void setupPlayer(Player p) {
		if (playersQuests.contains(getQuestPlayersData(p))) {
			playersQuests.remove(getQuestPlayersData(p));
		}
		
		if (readQuestPlayerData(p) == null) {
			playersQuests.add(new QuestPlayerData(p.getUniqueId(), new ArrayList<Quest>()));
			writeQuestPlayerData(p);
		} else {
			playersQuests.add(readQuestPlayerData(p));
		}
		
		Iterator<Entry<Boolean, List<Quest>>> it = InitQuests.getAllQuests().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Boolean, List<Quest>> pair = it.next();
			
			if (pair.getKey()) {
				for (Quest q : pair.getValue()) {
					if (!getQuestPlayersData(p).hasQuest(q)) {
						System.out.println(p.getDisplayName() + " does not have the quest " + q.getName() + "! (adding now)");
						addQuestToPlayer(p, q);
					}
				}
			} else {
				for (Quest q : pair.getValue()) {
					if (getQuestPlayersData(p).hasQuest(q)) {
						System.out.println(p.getDisplayName() + " has a removed (inactive) quest " + q.getName() + "! (removing now)");
						removeQuestFromPlayer(p, q);
					}
				}
			}
		}
		
		setupQuestChestGUI(p);
	}
	
	public void unsetupPlayer(Player p) {
		playersQuests.remove(getQuestPlayersData(p));
		questInventoriesAccepted.remove(p.getUniqueId());
		questInventoriesUnknown.remove(p.getUniqueId());
		questInventoriesFinished.remove(p.getUniqueId());
	}
	
	private void addQuestToPlayer(Player p, Quest quest) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		if (info != null) {
			info.addQuest(quest);
			writeQuestPlayerData(p);
		} else {
			System.err.println("Player " + p.getDisplayName() + " does not have a quest playerdata");
		}
	}
	
	private void removeQuestFromPlayer(Player p, Quest quest) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		if (info != null) {
			info.removeQuest(quest);
			writeQuestPlayerData(p);
		} else {
			System.err.println("Player " + p.getDisplayName() + " does not have a quest playerdata");
		}
	}
	
	public void setupQuestChestGUI(Player p) {
		List<Inventory> invUnknown = new ArrayList<Inventory>(); //paper
		List<Inventory> invAccepted = new ArrayList<Inventory>(); //writable book
		List<Inventory> invFinished = new ArrayList<Inventory>(); //written book
		
		List<Quest> unknownQuests = getQuestPlayersData(p).getQuestsThatHaveState(QuestState.unknown);
		List<Quest> acceptedQuests = getQuestPlayersData(p).getQuestsThatHaveState(QuestState.accepted);
		List<Quest> FinishedQuests = getQuestPlayersData(p).getQuestsThatHaveState(QuestState.finished);
		
		for (int i = 0; i < unknownQuests.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				invUnknown.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&6Unknown quests [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
		
		for (int i = 0; i < acceptedQuests.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				invAccepted.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&6Accepted quests [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
		
		for (int i = 0; i < FinishedQuests.size(); i++) {
			if (i % 54 == 0 || i == 0) {
				invFinished.add(Bukkit.createInventory(null, 54, ColorHelper.setColors("&6Finished quests [" + (int) Math.ceil(i / 54) + "]")));
			}
		}
		
		if (!unknownQuests.isEmpty()) {
			for (int i = 0; i < unknownQuests.size(); i++) {
				Quest q = unknownQuests.get(i);
				ItemStack item = new ItemStack(Material.PAPER);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ColorHelper.setColors("&4" + q.getName()));
				
				List<String> newLore = new ArrayList<String>();
				for (String s : q.getDescription()) {
					newLore.add(ColorHelper.setColors("&c" + s));
				}
				
				meta.setLore(newLore);
				item.setItemMeta(meta);
				
				invUnknown.get((int) Math.ceil(i / 54)).setItem(i % 54, item);
			}
		}
		
		if (!acceptedQuests.isEmpty()) {
			for (int i = 0; i < acceptedQuests.size(); i++) {
				Quest q = acceptedQuests.get(i);
				ItemStack item = new ItemStack(Material.PAPER);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ColorHelper.setColors("&2" + q.getName()));
				
				List<String> newLore = new ArrayList<String>();
				for (String s : q.getDescription()) {
					newLore.add(ColorHelper.setColors("&a" + s));
				}
				
				newLore.add(ColorHelper.setColors("&a---"));
				for (String s : q.getCurrentTask().getDescription()) {
					newLore.add(ColorHelper.setColors("&a" + s));
				}
				
				
				meta.setLore(newLore);
				item.setItemMeta(meta);
				
				invAccepted.get((int) Math.ceil(i / 54)).setItem(i % 54, item);
			}
		}
		
		if (!FinishedQuests.isEmpty()) {
			for (int i = 0; i < FinishedQuests.size(); i++) {
				Quest q = FinishedQuests.get(i);
				ItemStack item = new ItemStack(Material.PAPER);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ColorHelper.setColors("&3" + q.getName()));
				
				List<String> newLore = new ArrayList<String>();
				for (String s : q.getDescription()) {
					newLore.add(ColorHelper.setColors("&b" + s));
				}
				
				meta.setLore(newLore);
				item.setItemMeta(meta);
				
				invFinished.get((int) Math.ceil(i / 54)).setItem(i % 54, item);
			}
		}
		
		questInventoriesAccepted.put(p.getUniqueId(), invAccepted);
		questInventoriesFinished.put(p.getUniqueId(), invFinished);
		questInventoriesUnknown.put(p.getUniqueId(), invUnknown);
	}
	
	private void writeQuestPlayerData(Player p) {
		File f = new File(path + "/Quests/PlayerData/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		
		write.set("PlayerData", getQuestPlayersData(p));
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private QuestPlayerData readQuestPlayerData(Player p) {
		File f = new File(path + "/Quests/PlayerData/" + p.getUniqueId().toString() + ".yml");
		return (QuestPlayerData) YamlConfiguration.loadConfiguration(f).getObject("PlayerData", QuestPlayerData.class);
	}
	
	private Quest writeDefaultQuest(String questFileName) {
		File f = new File(path + "/Quests/" + questFileName + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		Quest q = null;
		
		if (questFileName == "DebugFetch") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskFetch(new ItemStack(Material.ROTTEN_FLESH), 3, new String[] {
					"Get 3 Rotten Flesh",
					"String 1"
			}));
			tasks.add(new QuestTaskFetch(new ItemStack(Material.BONE), 3, new String[] {
					"Get 3 Bones",
					"String 2"
			}));
			
			q = new Quest(0, "Debug Fetch!", new String[] {
					"Description Fetch"
			}, tasks, 0, 0, new ItemStack[] {
					new ItemStack(Material.DIAMOND, 2)
			});
			write.set("Quest", q);
		} else if (questFileName == "DebugKill") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskKill(EntityType.ZOMBIE, 3, new String[] {
					"Kill 3 Zombies",
					"String 1"
			}));
			tasks.add(new QuestTaskKill(EntityType.SKELETON, 3, new String[] {
					"Kill 3 Skeletons",
					"String 2"
			}));
			
			q = new Quest(1, "Debug Kill!", new String[] {
					"Description Kill"
			}, tasks, 0, 0, new ItemStack[] {
					new ItemStack(Material.BONE, 5)
			});
			write.set("Quest", q);
		} else if (questFileName == "DebugWalk") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskWalk(new Location(Bukkit.getServer().getWorld(main.getRandomConfig().getMainWorld()), 0, 0, 0), new String[] {
					"Walk to (0, 0, 0)",
					"String 1"
			}));
			tasks.add(new QuestTaskWalk(new Location(Bukkit.getServer().getWorld(main.getRandomConfig().getMainWorld()), 0, 0, 0), new String[] {
					"Now walk to (0, 0, 0)",
					"String 2"
			}));
			
			q = new Quest(2, "Debug Walk!", new String[] {
					"Description Walk"
			}, tasks, 0, 0, new ItemStack[] {
					new ItemStack(Material.FEATHER, 2)
			});
			write.set("Quest", q);
		} else {
			q = new Quest(3, "Unfinished Quest!", new String[] {
					"Unfinished description"
			}, new ArrayList<QuestTask>(), 0, 0, new ItemStack[] {});
			write.set("Quest", q);
		}
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return q;
	}
	
	public Quest getQuestFile(String questFileName) {
		File f = new File(path + "/Quests/" + questFileName + ".yml");
		
		Quest q = (Quest) YamlConfiguration.loadConfiguration(f).getObject("Quest", Quest.class);
		
		if (q == null) {
			System.out.println("Could not find file: " + questFileName + ".yml! (Will be created)");
			return writeDefaultQuest(questFileName);
		}
		
		return q;
	}
	
	public boolean isPlayerSetup(Player p) {
		for (QuestPlayerData info : playersQuests) {
			if (info.getPlayerUUID().equals(p.getUniqueId())) {
				return true;
			}
		}
		
		return false;
	}
	
	private QuestPlayerData getQuestPlayersData(Player p) {
		for (QuestPlayerData info : playersQuests) {
			if (info.getPlayerUUID().equals(p.getUniqueId())) {
				return info;
			}
		}
		
		return null;
	}
	
	public List<Inventory> getPlayersQuestGUIs(Player p, QuestState state) {
		Iterator<Entry<UUID, List<Inventory>>> it = null;
		switch (state) {
			case unknown:
				it = questInventoriesUnknown.entrySet().iterator();
				break;
			case accepted:
				it = questInventoriesAccepted.entrySet().iterator();
				break;
			case finished:
				it = questInventoriesFinished.entrySet().iterator();
				break;
		}
		
		while (it.hasNext()) {
			Entry<UUID, List<Inventory>> pair = it.next();
			if (pair.getKey().equals(p.getUniqueId())) {
				return pair.getValue();
			}
		}
		
		return null;
	}
}
