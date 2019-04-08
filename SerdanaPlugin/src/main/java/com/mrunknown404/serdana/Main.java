package main.java.com.mrunknown404.serdana;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.com.mrunknown404.serdana.commands.CommandSerdana;
import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.commands.tabs.TabSerdana;
import main.java.com.mrunknown404.serdana.listener.CraftingListener;
import main.java.com.mrunknown404.serdana.listener.EntityListener;
import main.java.com.mrunknown404.serdana.listener.HealthListener;
import main.java.com.mrunknown404.serdana.listener.ShopkeeperListener;
import main.java.com.mrunknown404.serdana.util.HealthBarHandler;

public final class Main extends JavaPlugin {
	
	private ShopkeeperListener shopListen;
	private HealthBarHandler healthBarHandler;
	
	@Override
	public void onEnable() {
		File f = new File(getDataFolder() + "/");
		if(!f.exists()) {
			f.mkdir();
		}
		
		healthBarHandler = new HealthBarHandler(this);
		
		shopListen = new ShopkeeperListener(getDataFolder());
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		getServer().getPluginManager().registerEvents(shopListen, this);
		
		getCommand("setTier").setExecutor(new CommandSetTier());
		getCommand("serdana").setExecutor(new CommandSerdana(this));
		getCommand("serdana").setTabCompleter(new TabSerdana());
	}
	
	public void reload(CommandSender sender) {
		shopListen.reload(sender);
	}
	
	public HealthBarHandler getHealthBarHandler() {
		return healthBarHandler;
	}
}
