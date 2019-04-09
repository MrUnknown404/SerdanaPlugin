package main.java.com.mrunknown404.serdana;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.mrunknown404.serdana.commands.CommandBounty;
import main.java.com.mrunknown404.serdana.commands.CommandCoo;
import main.java.com.mrunknown404.serdana.commands.CommandDots;
import main.java.com.mrunknown404.serdana.commands.CommandJoin;
import main.java.com.mrunknown404.serdana.commands.CommandLeave;
import main.java.com.mrunknown404.serdana.commands.CommandSayAs;
import main.java.com.mrunknown404.serdana.commands.CommandSerdana;
import main.java.com.mrunknown404.serdana.commands.CommandSetBan;
import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.commands.CommandShowItem;
import main.java.com.mrunknown404.serdana.commands.tabs.TabBounty;
import main.java.com.mrunknown404.serdana.commands.tabs.TabSerdana;
import main.java.com.mrunknown404.serdana.listener.BlockListener;
import main.java.com.mrunknown404.serdana.listener.CraftingListener;
import main.java.com.mrunknown404.serdana.listener.EntityListener;
import main.java.com.mrunknown404.serdana.listener.HealthListener;
import main.java.com.mrunknown404.serdana.listener.ShopkeeperListener;
import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.RandomConfig;
import main.java.com.mrunknown404.serdana.util.handlers.BountyHandler;
import main.java.com.mrunknown404.serdana.util.handlers.HealthBarHandler;

public final class Main extends JavaPlugin {
	
	public static final String TYPE = ".json";
	
	private ShopkeeperListener shopListen;
	private HealthBarHandler healthBarHandler;
	
	private static RandomConfig randomConfig;
	private static BountyHandler bountyHandler;
	
	private final File file_randomConfig = new File("RandomConfig");
	
	@Override
	public void onEnable() {
		File f = new File(getDataFolder() + "/");
		if(!f.exists()) {
			f.mkdir();
		}
		
		healthBarHandler = new HealthBarHandler(this);
		bountyHandler = new BountyHandler(this);
		
		shopListen = new ShopkeeperListener(getDataFolder());
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(shopListen, this);
		
		getCommand("setTier").setExecutor(new CommandSetTier());
		getCommand("serdana").setExecutor(new CommandSerdana(this));
		getCommand("setBan").setExecutor(new CommandSetBan());
		getCommand("serdana").setTabCompleter(new TabSerdana());
		getCommand("bounty").setExecutor(new CommandBounty());
		getCommand("bounty").setTabCompleter(new TabBounty());
		getCommand("join").setExecutor(new CommandJoin());
		getCommand("leave").setExecutor(new CommandLeave());
		getCommand("showitem").setExecutor(new CommandShowItem());
		getCommand("dots").setExecutor(new CommandDots());
		getCommand("sayas").setExecutor(new CommandSayAs());
		getCommand("coo").setExecutor(new CommandCoo());
		
		reload(null);
	}
	
	public void reload(CommandSender sender) {
		if (sender != null) {
			sender.sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		}
		System.out.println("Reloading Serdana's Configs!");
		
		shopListen.reload(sender);
		bountyHandler.reload();
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		if (!new File(getDataFolder() + "/" + file_randomConfig + TYPE).exists()) {
			System.out.println("Could not find file: " + file_randomConfig + TYPE + "! (Will be created)");
			
			randomConfig = new RandomConfig("name", 2);
			
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
		
		if (sender != null) {
			sender.sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		}
		System.out.println("Finished Serdana's Configs!");
	}
	
	public HealthBarHandler getHealthBarHandler() {
		return healthBarHandler;
	}
	
	public static RandomConfig getRandomConfig() {
		return randomConfig;
	}
	
	public static BountyHandler getBountyHandler() {
		return bountyHandler;
	}
}
