package main.java.com.mrunknown404.serdana;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
import main.java.com.mrunknown404.serdana.commands.CommandPray;
import main.java.com.mrunknown404.serdana.commands.CommandSayAs;
import main.java.com.mrunknown404.serdana.commands.CommandSerdana;
import main.java.com.mrunknown404.serdana.commands.CommandSetBan;
import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.commands.CommandShowItem;
import main.java.com.mrunknown404.serdana.commands.tabs.TabBounty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabPray;
import main.java.com.mrunknown404.serdana.commands.tabs.TabSerdana;
import main.java.com.mrunknown404.serdana.listener.BlockListener;
import main.java.com.mrunknown404.serdana.listener.BookListener;
import main.java.com.mrunknown404.serdana.listener.CraftingListener;
import main.java.com.mrunknown404.serdana.listener.EntityListener;
import main.java.com.mrunknown404.serdana.listener.HealthListener;
import main.java.com.mrunknown404.serdana.listener.InventoryListener;
import main.java.com.mrunknown404.serdana.listener.ShopkeeperListener;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.PrayInfo;
import main.java.com.mrunknown404.serdana.util.RandomConfig;
import main.java.com.mrunknown404.serdana.util.handlers.BannedItemHandler;
import main.java.com.mrunknown404.serdana.util.handlers.BountyHandler;
import main.java.com.mrunknown404.serdana.util.handlers.HealthBarHandler;
import main.java.com.mrunknown404.serdana.util.handlers.PrayerHandler;

public final class Main extends JavaPlugin {
	
	public static final String TYPE = ".json";
	
	private ShopkeeperListener shopListen;
	private HealthBarHandler healthBarHandler;
	private BannedItemHandler bannedItemHandler;
	private RandomConfig randomConfig;
	private BountyHandler bountyHandler;
	private PrayerHandler prayerHandler;
	
	private final File file_randomConfig = new File("RandomConfig");
	
	@Override
	public void onEnable() {
		File f = new File(getDataFolder() + "/");
		if(!f.exists()) {
			f.mkdir();
		}
		
		ConfigurationSerialization.registerClass(PrayInfo.class, "PrayerInfo");
		
		healthBarHandler = new HealthBarHandler(this);
		bountyHandler = new BountyHandler(this);
		bannedItemHandler = new BannedItemHandler(this);
		prayerHandler = new PrayerHandler(this);
		
		shopListen = new ShopkeeperListener(this);
		
		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new BookListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		
		getServer().getPluginManager().registerEvents(shopListen, this);
		
		getCommand("setTier").setExecutor(new CommandSetTier());
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
		
		getCommand("serdana").setTabCompleter(new TabSerdana());
		getCommand("bounty").setTabCompleter(new TabBounty());
		getCommand("pray").setTabCompleter(new TabPray());
		
		reload(Bukkit.getConsoleSender());
	}
	
	public void reload(CommandSender sender) {
		Bukkit.getConsoleSender().sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		if (sender instanceof Player) {
			sender.sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		}
		
		shopListen.reload();
		bountyHandler.reload();
		bannedItemHandler.reload();
		prayerHandler.reload();
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		if (!new File(getDataFolder() + "/" + file_randomConfig + TYPE).exists()) {
			System.out.println("Could not find file: " + file_randomConfig + TYPE + "! (Will be created)");
			
			ArrayList<String> jails = new ArrayList<String>();
			jails.add("test1");
			jails.add("test2");
			
			randomConfig = new RandomConfig(jails, 2);
			
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
		
		Bukkit.getConsoleSender().sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		if (sender instanceof Player) {
			sender.sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		}
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
}
