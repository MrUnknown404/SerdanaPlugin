package main.java.com.mrunknown404.serdana.util;

import org.bukkit.Bukkit;

public abstract class Reloadable {

	/** Reloads this class's Configs */
	public void reloadAll() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		reload();
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	/** Reloads this class's Configs */
	protected abstract void reload();
}
