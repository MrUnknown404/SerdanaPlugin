package main.java.com.mrunknown404.serdana;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.mrunknown404.serdana.commands.CommandBounty;
import main.java.com.mrunknown404.serdana.commands.CommandCoo;
import main.java.com.mrunknown404.serdana.commands.CommandDots;
import main.java.com.mrunknown404.serdana.commands.CommandJoin;
import main.java.com.mrunknown404.serdana.commands.CommandLeave;
import main.java.com.mrunknown404.serdana.commands.CommandParasite;
import main.java.com.mrunknown404.serdana.commands.CommandParty;
import main.java.com.mrunknown404.serdana.commands.CommandPray;
import main.java.com.mrunknown404.serdana.commands.CommandQuest;
import main.java.com.mrunknown404.serdana.commands.CommandSayAs;
import main.java.com.mrunknown404.serdana.commands.CommandSerdana;
import main.java.com.mrunknown404.serdana.commands.CommandSetBan;
import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.commands.CommandShowItem;
import main.java.com.mrunknown404.serdana.commands.CommandTimer;
import main.java.com.mrunknown404.serdana.commands.CommandUnbreakable;
import main.java.com.mrunknown404.serdana.commands.tabs.TabBounty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabParasite;
import main.java.com.mrunknown404.serdana.commands.tabs.TabParty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabPray;
import main.java.com.mrunknown404.serdana.commands.tabs.TabQuest;
import main.java.com.mrunknown404.serdana.commands.tabs.TabSerdana;
import main.java.com.mrunknown404.serdana.commands.tabs.TabTimer;
import main.java.com.mrunknown404.serdana.handlers.BannedItemHandler;
import main.java.com.mrunknown404.serdana.handlers.BountyHandler;
import main.java.com.mrunknown404.serdana.handlers.EntityHandler;
import main.java.com.mrunknown404.serdana.handlers.HealthBarHandler;
import main.java.com.mrunknown404.serdana.handlers.ParasiteHandler;
import main.java.com.mrunknown404.serdana.handlers.PartyHandler;
import main.java.com.mrunknown404.serdana.handlers.PrayerHandler;
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
import main.java.com.mrunknown404.serdana.quests.tasks.QuestTaskWalk;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.RandomConfig;
import main.java.com.mrunknown404.serdana.util.infos.PrayInfo;

public final class Main extends JavaPlugin {
	
	public static final String TYPE = ".json";
	public static final String BASE_LOCATION_TEXTURES = "/main/resources/serdana/assets/quests/";
	private final File file_randomConfig = new File("RandomConfig");
	
	private ShopkeeperListener shopListen;
	
	private HealthBarHandler healthBarHandler;
	private BannedItemHandler bannedItemHandler;
	private RandomConfig randomConfig;
	private BountyHandler bountyHandler;
	private PrayerHandler prayerHandler;
	private PartyHandler partyHandler;
	private QuestHandler questHandler;
	private ParasiteHandler parasiteHandler;
	private EntityHandler entityhandler;
	private TierHandler tierHandler;
	
	private CommandTimer commandTimer;
	
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
		
		ConfigurationSerialization.registerClass(PrayInfo.class, "PrayerInfo");
		ConfigurationSerialization.registerClass(QuestPlayerData.class, "QuestInfo");
		ConfigurationSerialization.registerClass(Quest.class, "Quest");
		ConfigurationSerialization.registerClass(QuestTask.class, "QuestTask");
		ConfigurationSerialization.registerClass(QuestTaskFetch.class, "QuestTaskFetch");
		ConfigurationSerialization.registerClass(QuestTaskKill.class, "QuestTaskKill");
		ConfigurationSerialization.registerClass(QuestTaskWalk.class, "QuestTaskWalk");
		
		commandTimer = new CommandTimer(this);
		
		healthBarHandler = new HealthBarHandler(this);
		bountyHandler = new BountyHandler(this);
		bannedItemHandler = new BannedItemHandler(this);
		prayerHandler = new PrayerHandler(this);
		partyHandler = new PartyHandler();
		questHandler = new QuestHandler(this);
		parasiteHandler = new ParasiteHandler(this);
		entityhandler = new EntityHandler(this);
		tierHandler = new TierHandler();
		
		shopListen = new ShopkeeperListener(this);
		
		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new BookListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		
		getServer().getPluginManager().registerEvents(shopListen, this);
		
		getCommand("setTier").setExecutor(new CommandSetTier(this));
		getCommand("serdana").setExecutor(new CommandSerdana(this));
		getCommand("setBan").setExecutor(new CommandSetBan());
		getCommand("bounty").setExecutor(new CommandBounty(this));
		getCommand("join").setExecutor(new CommandJoin());
		getCommand("leave").setExecutor(new CommandLeave());
		getCommand("showitem").setExecutor(new CommandShowItem());
		getCommand("dots").setExecutor(new CommandDots());
		getCommand("sayas").setExecutor(new CommandSayAs());
		getCommand("coo").setExecutor(new CommandCoo());
		getCommand("pray").setExecutor(new CommandPray(this));
		getCommand("party").setExecutor(new CommandParty(this));
		getCommand("quest").setExecutor(new CommandQuest(this));
		getCommand("unbreakable").setExecutor(new CommandUnbreakable());
		getCommand("parasite").setExecutor(new CommandParasite(this));
		getCommand("timer").setExecutor(commandTimer);
		
		getCommand("serdana").setTabCompleter(new TabSerdana());
		getCommand("bounty").setTabCompleter(new TabBounty());
		getCommand("pray").setTabCompleter(new TabPray());
		getCommand("party").setTabCompleter(new TabParty());
		getCommand("quest").setTabCompleter(new TabQuest());
		getCommand("parasite").setTabCompleter(new TabParasite());
		getCommand("timer").setTabCompleter(new TabTimer());
		
		reload(Bukkit.getConsoleSender());
		parasiteHandler.start();
	}
	
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
		
		shopListen.reload();
		bountyHandler.reload();
		bannedItemHandler.reload();
		prayerHandler.reload();
		questHandler.reloadAll();
		
		InitQuests.register(this);
		
		Bukkit.getConsoleSender().sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		if (sender instanceof Player) {
			sender.sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		}
	}
	
	public ShopkeeperListener getShopListen() {
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
	
	public EntityHandler getEntityhandler() {
		return entityhandler;
	}
	
	public TierHandler getTierHandler() {
		return tierHandler;
	}
	
	public CommandTimer getCommandTimer() {
		return commandTimer;
	}
}
