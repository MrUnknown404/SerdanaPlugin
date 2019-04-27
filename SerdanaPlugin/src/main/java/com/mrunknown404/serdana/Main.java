package main.java.com.mrunknown404.serdana;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.java.com.mrunknown404.serdana.commands.CommandAChatToggle;
import main.java.com.mrunknown404.serdana.commands.CommandAWarp;
import main.java.com.mrunknown404.serdana.commands.CommandAWarps;
import main.java.com.mrunknown404.serdana.commands.CommandAddAWarp;
import main.java.com.mrunknown404.serdana.commands.CommandBounty;
import main.java.com.mrunknown404.serdana.commands.CommandCoo;
import main.java.com.mrunknown404.serdana.commands.CommandDots;
import main.java.com.mrunknown404.serdana.commands.CommandJoin;
import main.java.com.mrunknown404.serdana.commands.CommandLeave;
import main.java.com.mrunknown404.serdana.commands.CommandParasite;
import main.java.com.mrunknown404.serdana.commands.CommandParty;
import main.java.com.mrunknown404.serdana.commands.CommandPray;
import main.java.com.mrunknown404.serdana.commands.CommandQuest;
import main.java.com.mrunknown404.serdana.commands.CommandRainbow;
import main.java.com.mrunknown404.serdana.commands.CommandRemoveAWarp;
import main.java.com.mrunknown404.serdana.commands.CommandSayAs;
import main.java.com.mrunknown404.serdana.commands.CommandSerdana;
import main.java.com.mrunknown404.serdana.commands.CommandSetBan;
import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.commands.CommandShowItem;
import main.java.com.mrunknown404.serdana.commands.CommandTimer;
import main.java.com.mrunknown404.serdana.commands.CommandUnbreakable;
import main.java.com.mrunknown404.serdana.commands.tabs.TabAWarp;
import main.java.com.mrunknown404.serdana.commands.tabs.TabBounty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabParasite;
import main.java.com.mrunknown404.serdana.commands.tabs.TabParty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabPray;
import main.java.com.mrunknown404.serdana.commands.tabs.TabQuest;
import main.java.com.mrunknown404.serdana.commands.tabs.TabRemoveAWarp;
import main.java.com.mrunknown404.serdana.commands.tabs.TabSerdana;
import main.java.com.mrunknown404.serdana.commands.tabs.TabTimer;
import main.java.com.mrunknown404.serdana.handlers.AChatHandler;
import main.java.com.mrunknown404.serdana.handlers.AWarpHandler;
import main.java.com.mrunknown404.serdana.handlers.BannedItemHandler;
import main.java.com.mrunknown404.serdana.handlers.BountyHandler;
import main.java.com.mrunknown404.serdana.handlers.HealthBarHandler;
import main.java.com.mrunknown404.serdana.handlers.ParasiteHandler;
import main.java.com.mrunknown404.serdana.handlers.PartyHandler;
import main.java.com.mrunknown404.serdana.handlers.PrayerHandler;
import main.java.com.mrunknown404.serdana.handlers.ShopkeeperHandler;
import main.java.com.mrunknown404.serdana.handlers.TierHandler;
import main.java.com.mrunknown404.serdana.listener.BlockListener;
import main.java.com.mrunknown404.serdana.listener.BookListener;
import main.java.com.mrunknown404.serdana.listener.CraftingListener;
import main.java.com.mrunknown404.serdana.listener.EntityListener;
import main.java.com.mrunknown404.serdana.listener.HealthListener;
import main.java.com.mrunknown404.serdana.listener.InventoryListener;
import main.java.com.mrunknown404.serdana.listener.PlayerListener;
import main.java.com.mrunknown404.serdana.listener.ShopkeeperListener;
import main.java.com.mrunknown404.serdana.quests.InitQuests;
import main.java.com.mrunknown404.serdana.quests.Quest;
import main.java.com.mrunknown404.serdana.quests.QuestHandler;
import main.java.com.mrunknown404.serdana.quests.QuestPlayerData;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTask;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskFetch;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskKill;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskTalk;
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskWalk;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.RandomConfig;
import main.java.com.mrunknown404.serdana.util.infos.PrayInfo;

public final class Main extends JavaPlugin {
	
	public static final String TYPE = ".json";
	public static final String BASE_LOCATION_TEXTURES = "/main/resources/serdana/assets/quests/";
	private final File file_randomConfig = new File("RandomConfig");
	private final File file_components = new File("Components");
	
	private ShopkeeperListener shopListen;
	
	private HealthBarHandler healthBarHandler;
	private BannedItemHandler bannedItemHandler;
	private RandomConfig randomConfig;
	private BountyHandler bountyHandler;
	private PrayerHandler prayerHandler;
	private PartyHandler partyHandler;
	private QuestHandler questHandler;
	private ParasiteHandler parasiteHandler;
	private TierHandler tierHandler;
	private AWarpHandler aWarpHandler;
	private AChatHandler aChatHandler;
	private ShopkeeperHandler shopHandler;
	
	private CommandTimer commandTimer;
	
	private Map<Components, Boolean> components = new HashMap<Components, Boolean>();
	
	@Override
	public void onEnable() {
		File f = new File(getDataFolder() + "/Quests/");
		if(!f.exists()) {
			f.mkdirs();
		}
		f = new File(getDataFolder() + "/Quests/PlayerData/");
		if(!f.exists()) {
			f.mkdirs();
		}
		f = new File(getDataFolder() + "/NPCMessages/");
		if(!f.exists()) {
			f.mkdirs();
		}
		f = new File(getDataFolder() + "/Prayers/");
		if(!f.exists()) {
			f.mkdirs();
		}
		
		setupEnabledComponents();
		
		ConfigurationSerialization.registerClass(PrayInfo.class, "PrayerInfo");
		ConfigurationSerialization.registerClass(QuestPlayerData.class, "QuestInfo");
		ConfigurationSerialization.registerClass(Quest.class, "Quest");
		ConfigurationSerialization.registerClass(QuestTask.class, "QuestTask");
		ConfigurationSerialization.registerClass(QuestTaskFetch.class, "QuestTaskFetch");
		ConfigurationSerialization.registerClass(QuestTaskKill.class, "QuestTaskKill");
		ConfigurationSerialization.registerClass(QuestTaskWalk.class, "QuestTaskWalk");
		ConfigurationSerialization.registerClass(QuestTaskTalk.class, "QuestTaskTalk");
		
		shopListen = new ShopkeeperListener(this);
		
		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(this), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		getServer().getPluginManager().registerEvents(new BookListener(this), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		
		getServer().getPluginManager().registerEvents(shopListen, this);
		
		reload(Bukkit.getConsoleSender());
	}
	
	/** Reloads all Configs */
	public void reload(CommandSender sender) {
		Bukkit.getConsoleSender().sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		if (sender instanceof Player) {
			sender.sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		}
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		if (!new File(getDataFolder() + "/" + file_randomConfig + TYPE).exists()) {
			System.out.println("Could not find file: " + file_randomConfig + TYPE + "! (Will be created)");
			
			ArrayList<String> jails = new ArrayList<String>();
			jails.add("jail1");
			jails.add("jail2");
			
			randomConfig = new RandomConfig(jails, 2, Arrays.asList("Max"), 16d, 5);
			
			try {
				fw = new FileWriter(getDataFolder() + "/" + file_randomConfig + TYPE);
				
				g.toJson(randomConfig, fw);
				
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fr = new FileReader(getDataFolder() + "/" + file_randomConfig + TYPE);
			
			randomConfig = g.fromJson(fr, RandomConfig.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Iterator<Entry<Components, Boolean>> it = components.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Components, Boolean> pair = it.next();
			
			if (pair.getValue()) {
				switch (pair.getKey()) {
					case CustomShops:
						shopHandler.reload();
						break;
					case Bounties:
						bountyHandler.reload();
						break;
					case BannedItems:
						bannedItemHandler.reload();
						break;
					case Prayers:
						prayerHandler.reload();
						break;
					case Quests:
						questHandler.reloadAll();
						InitQuests.register(this);
						break;
					case AWarp:
						aWarpHandler.reload();
						break;
					case AChat:
						aChatHandler.reload();
						break;
					default:
						break;
				}
			}
		}
		
		Bukkit.getConsoleSender().sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		if (sender instanceof Player) {
			sender.sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		}
	}
	
	/** Sets up components */
	private void setupEnabledComponents() {
		for (Components c : Components.values()) {
			components.put(c, c.defaultState);
		}
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		if (!new File(getDataFolder() + "/" + file_components + TYPE).exists()) {
			System.out.println("Could not find file: " + file_components + TYPE + "! (Will be created)");
			
			try {
				fw = new FileWriter(getDataFolder() + "/" + file_components + TYPE);
				
				g.toJson(components, fw);
				
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fr = new FileReader(getDataFolder() + "/" + file_components + TYPE);
			
			components = g.fromJson(fr, new TypeToken<Map<Components, Boolean>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Iterator<Entry<Components, Boolean>> it = components.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Components, Boolean> pair = it.next();
			
			if (pair.getValue()) {
				switch (pair.getKey()) {
					case AChat:
						aChatHandler = new AChatHandler(this);
						getCommand("adminChatToggle").setExecutor(new CommandAChatToggle(this));
						break;
					case AWarp:
						aWarpHandler = new AWarpHandler(this);
						getCommand("setAdminWarp").setExecutor(new CommandAddAWarp(this));
						getCommand("remAdminWarp").setExecutor(new CommandRemoveAWarp(this));
						getCommand("adminWarps").setExecutor(new CommandAWarps(this));
						getCommand("adminWarp").setExecutor(new CommandAWarp(this));
						
						getCommand("remAdminWarp").setTabCompleter(new TabRemoveAWarp(this));
						getCommand("adminWarp").setTabCompleter(new TabAWarp(this));
						break;
					case BannedItems:
						bannedItemHandler = new BannedItemHandler(this);
						getCommand("setBan").setExecutor(new CommandSetBan());
						break;
					case Bounties:
						bountyHandler = new BountyHandler(this);
						getCommand("bounty").setExecutor(new CommandBounty(this));
						getCommand("bounty").setTabCompleter(new TabBounty());
						break;
					case HealthBar:
						healthBarHandler = new HealthBarHandler(this);
						break;
					case Parasite:
						parasiteHandler = new ParasiteHandler(this);
						getCommand("parasite").setExecutor(new CommandParasite(this));
						getCommand("parasite").setTabCompleter(new TabParasite());
						
						parasiteHandler.start();
						break;
					case Parties:
						partyHandler = new PartyHandler();
						getCommand("party").setExecutor(new CommandParty(this));
						getCommand("party").setTabCompleter(new TabParty());
						break;
					case Prayers:
						prayerHandler = new PrayerHandler(this);
						getCommand("pray").setExecutor(new CommandPray(this));
						getCommand("pray").setTabCompleter(new TabPray());
						break;
					case Tiers:
						tierHandler = new TierHandler();
						getCommand("setTier").setExecutor(new CommandSetTier(this));
						break;
					case Quests:
						questHandler = new QuestHandler(this);
						getCommand("quest").setExecutor(new CommandQuest(this));
						getCommand("quest").setTabCompleter(new TabQuest(this));
						break;
					case Misc:
						commandTimer = new CommandTimer(this);
						
						getCommand("serdana").setExecutor(new CommandSerdana(this));
						getCommand("join").setExecutor(new CommandJoin());
						getCommand("leave").setExecutor(new CommandLeave());
						getCommand("showitem").setExecutor(new CommandShowItem());
						getCommand("dots").setExecutor(new CommandDots());
						getCommand("coo").setExecutor(new CommandCoo());
						getCommand("sayas").setExecutor(new CommandSayAs());
						getCommand("unbreakable").setExecutor(new CommandUnbreakable());
						getCommand("rainbow").setExecutor(new CommandRainbow());
						getCommand("timer").setExecutor(commandTimer);
						
						getCommand("serdana").setTabCompleter(new TabSerdana());
						getCommand("timer").setTabCompleter(new TabTimer());
						break;
					case CustomShops:
						shopHandler = new ShopkeeperHandler(this);
						break;
					default:
						break;
				}
			}
		}
	}
	
	/** Checks if the given {@link Components} is enabled or disabled
	 * @param comp The Component to check
	 * @return returns true if the given component is enabled, returns false if disabled
	 */
	public boolean isComponentEnabled(Components comp) {
		if (components.containsKey(comp)) {
			return components.get(comp);
		}
		
		return false;
	}
	
	public ShopkeeperListener getShopListener() {
		return shopListen;
	}
	
	public HealthBarHandler getHealthBarHandler() {
		return healthBarHandler;
	}
	
	public BannedItemHandler getBannedItemHandler() {
		return bannedItemHandler;
	}
	
	public RandomConfig getRandomConfig() {
		return randomConfig;
	}
	
	public BountyHandler getBountyHandler() {
		return bountyHandler;
	}
	
	public PrayerHandler getPrayerHandler() {
		return prayerHandler;
	}
	
	public PartyHandler getPartyHandler() {
		return partyHandler;
	}
	
	public QuestHandler getQuestHandler() {
		return questHandler;
	}
	
	public ParasiteHandler getParasiteHandler() {
		return parasiteHandler;
	}
	
	public TierHandler getTierHandler() {
		return tierHandler;
	}
	
	public CommandTimer getCommandTimer() {
		return commandTimer;
	}
	
	public AWarpHandler getAWarpHandler() {
		return aWarpHandler;
	}
	
	public AChatHandler getAChatHandler() {
		return aChatHandler;
	}
	
	public ShopkeeperHandler getShopHandler() {
		return shopHandler;
	}
	
	public enum Components {
		AChat           (true),
		AWarp           (true),
		BannedItems     (false),
		Bounties        (true),
		HealthBar       (true),
		Parasite        (true),
		Parties         (true),
		Prayers         (true),
		Tiers           (false),
		Quests          (false),
		CustomShops     (false),
		StopNamedItemUse(true),
		StopItemCrafting(false),
		Misc            (true);
		
		final boolean defaultState;
		
		private Components(boolean defaultState) {
			this.defaultState = defaultState;
		}
	}
}
