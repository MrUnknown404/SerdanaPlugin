package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;

public class AChatHandler {

	private final File path;
	private final File file_chatStates = new File("AdminChatStates");
	
	private Map<UUID, Boolean> chatStates;
	
	public AChatHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	/** Reloads this class's Configs */
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		if (!new File(path + "/" + file_chatStates + ".yml").exists()) {
			System.out.println("Could not find file: " + file_chatStates + ".yml" + "! (Will be created)");
			chatStates = new HashMap<UUID, Boolean>();
			
			write();
		}
		
		read();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	/** Toggles the given {@link Player}'s admin chat state
	 * @param id The Player's UUID
	 */
	public void togglePlayer(UUID id) {
		if (!chatStates.isEmpty() && chatStates.containsKey(id)) {
			chatStates.put(id, !chatStates.get(id));
		} else {
			chatStates.put(id, true);
		}
		
		write();
	}
	
	/** Writes all {@link Player}'s admin chat state */
	private void write() {
		File f = new File(path + "/" + file_chatStates + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		Map<String, Boolean> stringStates = new HashMap<String, Boolean>();
		
		Iterator<Entry<UUID, Boolean>> it = chatStates.entrySet().iterator();
		while (it.hasNext()) {
			Entry<UUID, Boolean> pair = it.next();
			stringStates.put(pair.getKey().toString(), pair.getValue());
		}
		
		write.set("AdminChatStates", stringStates);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Read all {@link Player}'s admin chat states */
	@SuppressWarnings("unchecked")
	private void read() {
		File f = new File(path + "/" + file_chatStates + ".yml");
		
		Map<String, ?> temp = YamlConfiguration.loadConfiguration(f).getConfigurationSection("AdminChatStates").getValues(false);
		Iterator<?> it = temp.entrySet().iterator();
		chatStates = new HashMap<UUID, Boolean>();
		
		while (it.hasNext()) {
			Entry<String, Boolean> pair = (Entry<String, Boolean>) it.next();
			
			chatStates.put(UUID.fromString(pair.getKey()), pair.getValue());
		}
	}
	
	/** returns the given {@link Player}'s admin chat state
	 * @param id Player to check
	 * @return The given Player's admin chat state
	 */
	public boolean isPlayerEnabled(UUID id) {
		if (chatStates.containsKey(id)) {
			return chatStates.get(id);
		}
		
		return false;
	}
}
