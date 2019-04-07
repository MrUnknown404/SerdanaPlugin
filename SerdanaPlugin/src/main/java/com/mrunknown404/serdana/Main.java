package main.java.com.mrunknown404.serdana;

import org.bukkit.plugin.java.JavaPlugin;

import main.java.com.mrunknown404.serdana.commands.CommandSetTier;
import main.java.com.mrunknown404.serdana.listener.CraftingListener;
import main.java.com.mrunknown404.serdana.listener.EntityListener;

public final class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new CraftingListener(), this);
		
		getCommand("setTier").setExecutor(new CommandSetTier());
	}
	
	@Override
	public void onDisable() {
		
	}
}
