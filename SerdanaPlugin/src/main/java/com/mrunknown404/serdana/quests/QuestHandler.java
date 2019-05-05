package main.java.com.mrunknown404.serdana.quests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTask;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskFetch;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskKill;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskTalk;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskWalk;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.enums.EnumQuestState;
import main.java.com.mrunknown404.serdana.util.enums.EnumQuestTalkType;
import main.java.com.mrunknown404.serdana.util.enums.EnumTaskCheckType;

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
	
	/** Reloads the given {@link Player}'s {@link QuestPlayerData}
	 * @param p Player to reload
	 */
	public void reloadPlayer(Player p) {
		if (playersQuests.isEmpty()) {
			return;
		}
		
		setupPlayer(p);
	}
	
	/** Reloads this class's Configs/{@link QuestPlayerData} */
	public void reloadAll() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			setupPlayer(p);
		}
		
		checkTickTask();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	private BukkitScheduler scheduler;
	/** Checks tick {@link QuestTask} */
	private void checkTickTask() {
		scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				for (QuestPlayerData info : playersQuests) {
					for (Quest q : info.getQuests()) {
						if (!q.isReadyToTurnIn()) {
							if (q.getState() == EnumQuestState.accepted && q.getCurrentTask().getTaskCheckType() == EnumTaskCheckType.playerTick) {
								Player p = Bukkit.getPlayer(info.getPlayerUUID());
								if (q.check(p)) {
									writeQuestPlayerData(p);
									setupQuestChestGUI(p);
								}
							}
						}
					}
				}
			}
		}, 0L, 1L);
	}
	
	/** Checks the given {@link Player}'s Talk {@link QuestTask}
	 * @param p Player to check
	 * @param shop Shopkeeper that'll be checked
	 * @return true if the check was successful, otherwise false
	 */
	public boolean checkTalkTask(Player p, Shopkeeper shop) {
		String shopName = shop.getName();
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		for (Quest q : getQuestPlayersData(p).getQuestsThatHaveState(EnumQuestState.accepted)) {
			QuestTask tempTask = q.getCurrentTask();
			
			if (tempTask.getTaskCheckType() == EnumTaskCheckType.shopTalk) {
				QuestTaskTalk t = (QuestTaskTalk) tempTask;
				
				if (q.check(p, shop)) {
					p.sendMessage(ColorHelper.setColors(shopName + ": " + t.getCurrentMessage()));
					
					q.getCurrentTask().increaseAmmount();
					q.checkFinishedTask(p);
					
					writeQuestPlayerData(p);
					setupQuestChestGUI(p);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/** Checks the given {@link Player}'s {@link Entity} {@link QuestTask}
	 * @param p Player to check
	 * @param e Entity that'll be checked
	 */
	public void checkEntityDeathTask(Player p, Entity e) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		for (Quest q : info.getQuests()) {
			if (!q.isReadyToTurnIn() && q.getCurrentTask().getTaskCheckType() == EnumTaskCheckType.entityDeath) {
				if (q.check(p, e)) {
					writeQuestPlayerData(p);
					setupQuestChestGUI(p);
				}
			}
		}
	}
	
	/** Sets up the given {@link Player}
	 * @param p Player to setup
	 */
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
	
	/** Closes the given {@link Player}
	 * @param p Player to close
	 */
	public void closePlayer(Player p) {
		playersQuests.remove(getQuestPlayersData(p));
		questInventoriesAccepted.remove(p.getUniqueId());
		questInventoriesUnknown.remove(p.getUniqueId());
		questInventoriesFinished.remove(p.getUniqueId());
	}
	
	/** Gives the given {@link Player} the given {@link Quest}
	 * @param p Player to add Quest to
	 * @param q Quest to add
	 */
	private void addQuestToPlayer(Player p, Quest quest) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		if (info != null) {
			info.addQuest(quest);
			writeQuestPlayerData(p);
		} else {
			System.err.println("Player " + p.getDisplayName() + " does not have a quest playerdata");
		}
	}
	
	/** Removes the given {@link Quest} from the given {@link Player}
	 * @param p Player to remove Quest from
	 * @param q Quest to remove
	 */
	private void removeQuestFromPlayer(Player p, Quest quest) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		if (info != null) {
			info.removeQuest(quest);
			writeQuestPlayerData(p);
		} else {
			System.err.println("Player " + p.getDisplayName() + " does not have a quest playerdata");
		}
	}
	
	/** Set the given {@link Player}'s {@link Quest} {@link EnumQuestState}
	 * @param p Player to modify
	 * @param quest Quest to modify
	 * @param state New EnumQuestState
	 */
	public void setPlayersQuestState(Player p, Quest quest, EnumQuestState state) {
		QuestPlayerData info = getQuestPlayersData(p);
		
		if (info != null) {
			info.setQuestState(p, quest, state);
			writeQuestPlayerData(p);
			setupQuestChestGUI(p);
		} else {
			System.err.println("Player " + p.getDisplayName() + " does not have a quest playerdata");
		}
	}
	
	/** Sets up the {@link Quest} chest GUI
	 * @param p Player to setup a chest GUI for
	 */
	private void setupQuestChestGUI(Player p) {
		List<Inventory> invUnknown = new ArrayList<Inventory>(); //paper
		List<Inventory> invAccepted = new ArrayList<Inventory>(); //writable book
		List<Inventory> invFinished = new ArrayList<Inventory>(); //written book
		
		List<Quest> unknownQuests = getQuestPlayersData(p).getQuestsThatHaveState(EnumQuestState.unknown);
		List<Quest> acceptedQuests = getQuestPlayersData(p).getQuestsThatHaveState(EnumQuestState.accepted);
		List<Quest> FinishedQuests = getQuestPlayersData(p).getQuestsThatHaveState(EnumQuestState.finished);
		
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
				newLore.add(ColorHelper.setColors("&c???"));
				
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
				if (!q.isReadyToTurnIn()) {
					for (String s : q.getCurrentTask().getDescription()) {
						newLore.add(ColorHelper.setColors("&a" + s));
					}
					
					newLore.add(ColorHelper.setColors("&a---"));
					newLore.add(ColorHelper.setColors("&a" + q.getCurrentTask().getAmount() + "/" + q.getCurrentTask().getAmountNeeded()));
				} else {
					for (String s : q.getTurnInMessage()) {
						newLore.add(ColorHelper.setColors("&a" + s));
					}
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
				
				newLore.add(ColorHelper.setColors("&b---"));
				newLore.add(ColorHelper.setColors("&bCOMPLETED"));
				
				meta.setLore(newLore);
				item.setItemMeta(meta);
				
				invFinished.get((int) Math.ceil(i / 54)).setItem(i % 54, item);
			}
		}
		
		questInventoriesAccepted.put(p.getUniqueId(), invAccepted);
		questInventoriesFinished.put(p.getUniqueId(), invFinished);
		questInventoriesUnknown.put(p.getUniqueId(), invUnknown);
	}
	
	/** Write the given {@link Player}'s {@link QuestPlayerData}
	 * @param p Player to setup
	 */
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
	
	/** Reads the given {@link Player}'s {@link QuestPlayerData}
	 * @param p The Player's QuestPlayerData to read
	 * @return The given Player's QuestPlayerData
	 */
	private QuestPlayerData readQuestPlayerData(Player p) {
		File f = new File(path + "/Quests/PlayerData/" + p.getUniqueId().toString() + ".yml");
		return (QuestPlayerData) YamlConfiguration.loadConfiguration(f).getObject("PlayerData", QuestPlayerData.class);
	}
	
	/** Writes the given default {@link Quest}
	 * @param questFileName Quest File name
	 * @return A default Quest
	 */
	private Quest writeDefaultQuest(String questFileName) {
		File f = new File(path + "/Quests/" + questFileName + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		Quest q = null;
		
		if (questFileName == "DebugFetch") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskFetch(new ItemStack(Material.ROTTEN_FLESH), 3, new String[] {
					"Get 3 Rotten Flesh"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			tasks.add(new QuestTaskFetch(new ItemStack(Material.BONE), 3, new String[] {
					"Get 3 Bones"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			
			q = new Quest(0, "Debug Fetch!", new String[] {
					"Description Fetch"
			}, new String[] {
					"Complete Quest Message 1",
					"Complete Quest Message 2"
			}, new String[] {
					"Turn in Message 1",
					"Turn in Message 2"
			}, tasks, -1, -1, new ItemStack[] {
					new ItemStack(Material.DIAMOND, 2)
			}, new int[] {});
			
			write.set("Quest", q);
		} else if (questFileName == "DebugKill") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskKill(EntityType.ZOMBIE, 3, new String[] {
					"Kill 3 Zombies"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			tasks.add(new QuestTaskKill(EntityType.SKELETON, 3, new String[] {
					"Kill 3 Skeletons"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			
			q = new Quest(1, "Debug Kill!", new String[] {
					"Description Kill"
			}, new String[] {
					"Complete Quest Message 1",
					"Complete Quest Message 2"
			}, new String[] {
					"Turn in Message 1",
					"Turn in Message 2"
			}, tasks, -1, -1, new ItemStack[] {
					new ItemStack(Material.BONE, 5)
			}, new int[] {});
			
			write.set("Quest", q);
		} else if (questFileName == "DebugWalk") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskWalk(new Location(Bukkit.getServer().getWorld(main.getRandomConfig().getWorlds().get(0)), 0, 0, 0), new String[] {
					"Walk to (0, 0, 0)"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			tasks.add(new QuestTaskWalk(new Location(Bukkit.getServer().getWorld(main.getRandomConfig().getWorlds().get(0)), 10, 10, 10), new String[] {
					"Now walk to (10, 10, 10)"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, new String[] {}));
			
			q = new Quest(2, "Debug Walk!", new String[] {
					"Description Walk"
			}, new String[] {
					"Complete Quest Message 1",
					"Complete Quest Message 2"
			}, new String[] {
					"Turn in Message 1",
					"Turn in Message 2"
			}, tasks, -1, -1, new ItemStack[] {
					new ItemStack(Material.FEATHER, 2)
			}, new int[] {});
			
			write.set("Quest", q);
		} else if (questFileName == "DebugTalk") {
			List<QuestTask> tasks = new ArrayList<QuestTask>();
			
			tasks.add(new QuestTaskTalk(new String[] {
					"Talk to Shopkeeper 1"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, 1, new String[] {
					"Message 1-1",
					"Message 1-2",
					"Message 1-3"
			}, new String[] {}));
			tasks.add(new QuestTaskTalk(new String[] {
					"Talk to Shopkeeper 3"
			}, new String[] {
					"Complete Task Message 1",
					"Complete Task Message 2"
			}, 3, new String[] {
					"Message 2-1",
					"Message 2-2",
					"Message 2-3"
			}, new String[] {}));
			
			q = new Quest(3, "Debug Talk!", new String[] {
					"Description Talk"
			}, new String[] {
					"Complete Quest Message 1",
					"Complete Quest Message 2"
			}, new String[] {
					"Turn in Message 1",
					"Turn in Message 2"
			}, tasks, -1, -1, new ItemStack[] {
					new ItemStack(Material.CLAY, 23)
			}, new int[] {});
			
			write.set("Quest", q);
		} else {
			q = new Quest(3, "Unfinished Quest!", new String[] {
					"Unfinished description 1",
					"Unfinished description 2"
			}, new String[] {
					"Complete Quest Message 1",
					"Complete Quest Message 2"
			}, new String[] {
					"Turn in Message 1",
					"Turn in Message 2"
			}, new ArrayList<QuestTask>(), -1, -1, new ItemStack[] {}, new int[] {});
			
			write.set("Quest", q);
		}
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return q;
	}
	
	/** Gets a {@link Quest} from the given String
	 * @param questFileName String to use to find a Quest
	 * @return A Quest found from the given String
	 */
	public Quest getQuestFile(String questFileName) {
		if (getClass().getResourceAsStream(Main.BASE_LOCATION_QUESTS + questFileName + ".yml") == null) {
			System.out.println("Could not find file inside jar: " + questFileName + ".yml!");
			
			File f = new File(path + "/Quests/" + questFileName + ".yml");
			Quest q = YamlConfiguration.loadConfiguration(f).getObject("Quest", Quest.class);
			
			if (q == null) {
				System.out.println("Could not find file in config: " + questFileName + ".yml! (Will be created)");
				return writeDefaultQuest(questFileName);
			}
			
			return q;
		} else {
			InputStream s = getClass().getResourceAsStream(Main.BASE_LOCATION_QUESTS + questFileName + ".yml");
			return YamlConfiguration.loadConfiguration(new InputStreamReader(s)).getObject("Quest", Quest.class);
		}
	}
	
	/** Checks if the given {@link Player} is setup
	 * @param p Player to check
	 * @return true if the given Player is setup, otherwise false
	 */
	public boolean isPlayerSetup(Player p) {
		for (QuestPlayerData info : playersQuests) {
			if (info.getPlayerUUID().equals(p.getUniqueId())) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Gets the given {@link Player}'s {@link QuestPlayerData}
	 * @param p Player to get their QuestPlayerData from
	 * @return The given Player's QuestPlayerData
	 */
	public QuestPlayerData getQuestPlayersData(Player p) {
		for (QuestPlayerData info : playersQuests) {
			if (info.getPlayerUUID().equals(p.getUniqueId())) {
				return info;
			}
		}
		
		return null;
	}
	
	/** Gets the given {@link Player}'s {@link Quest} GUI
	 * @param p Player to get Quest GUI from
	 * @param state Quest GUI type
	 * @return A Inventory from the given Player & state
	 */
	public List<Inventory> getPlayersQuestGUIs(Player p, EnumQuestState state) {
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
	
	/** Searches the {@link Player}'s {@link Quest}s for {@link QuestTalkType}
	 * @param p Player to check
	 * @param shop Shopkeeper to check
	 * @return A QuestTalkType based off the given Player's Quests & Shop
	 */
	public EnumQuestTalkType getQuestShopTalkType(Player p, Shopkeeper shop) {
		QuestPlayerData data = getQuestPlayersData(p);
		
		for (Quest q : data.getQuestsThatHaveState(EnumQuestState.unknown)) {
			if (q.getStartID() == shop.getId()) {
				return EnumQuestTalkType.start;
			}
		}
		
		for (Quest q : data.getQuestsThatHaveState(EnumQuestState.accepted)) {
			if (q.getFinishID() == shop.getId()) {
				return EnumQuestTalkType.finish;
			}
			
			for (QuestTask t : q.getTasks()) {
				if (t.getTaskCheckType() == EnumTaskCheckType.shopTalk) {
					return EnumQuestTalkType.talkTask;
				}
			}
		}
		
		return EnumQuestTalkType.none;
	}
}
