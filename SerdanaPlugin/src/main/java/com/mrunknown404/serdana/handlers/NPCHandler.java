package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import io.lumine.utils.config.file.YamlConfiguration;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.enums.EnumQuestTalkType;
import main.java.com.mrunknown404.serdana.util.enums.EnumTalkType;
import main.java.com.mrunknown404.serdana.util.infos.NPCInfo;

public class NPCHandler {

	private final Main main;
	private final File path;
	
	private final File file_NPCInfos = new File("NPCInfos");
	
	private List<NPCInfo> NPCInfos;
	
	public NPCHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	/** Reloads this class's Configs */
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		if (!new File(path + "/" + file_NPCInfos + ".yml").exists()) {
			System.out.println("Could not find file: " + file_NPCInfos + ".yml" + "! (Will be created)");
			
			NPCInfos = new ArrayList<NPCInfo>();
			NPCInfos.add(new NPCInfo(1, true, "Steve",
					new String[] {"Test 1", "Test 2"},
					new String[] {"Banned 1", "Banned 2"},
					new String[] {"Trade 1", "Trade 2"}));
			
			write(file_NPCInfos, NPCInfos);
		}
		
		read();
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	/** Makes given {@link Shopkeeper} talk to the specified {@link Player}
	 * @param shop The Shopkeeper that will talk
	 * @param p The Player the Shopkeeper that will talk to
	 * @param type The Shopkeeper's talk type
	 * @return false if the Shopkeeper ignores banned items, otherwise true
	 */
	public boolean talk(Shopkeeper shop, Player p, EnumTalkType type) {
		for (NPCInfo info : NPCInfos) {
			if (info.getNPCID() == shop.getId()) {
				boolean b = info.talk(p, type);
				
				if (b) {
					main.getBannedItemHandler().performBannedAction(p, main.getBannedItemHandler().getPlayersBannedItems(p).size());
				}
				
				return b;
			}
		}
		
		return false;
	}
	
	/** Makes given {@link Shopkeeper} talk to the specified {@link Player}
	 * @param shop The Shopkeeper that will talk
	 * @param p The Player the Shopkeeper that will talk to
	 * @param type The talk type
	 * @return true if the task was successful, otherwise false
	 */
	public boolean talk(Shopkeeper shop, Player p, EnumQuestTalkType type) {
		switch (type) {
			case start:
				//start quest
				return true;
			case finish:
				//finish quest
				return true;
			case talkTask:
				return main.getQuestHandler().checkTalkTask(p, shop);
			default:
				break;
		}
		
		return false;
	}
	
	/** Writes a new {@link List} for the given {@link File}
	 * @param f The File that will be written to
	 */
	private void write(File file, Object toWrite) {
		File f = new File(path + "/" + file + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("NPCInfo", toWrite);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Writes a new {@link List} for the given {@link File}
	 * @param f The File that will be read from
	 * @return A List containing the contents of the read File
	 */
	@SuppressWarnings("unchecked")
	private void read() {
		NPCInfos = (List<NPCInfo>) YamlConfiguration.loadConfiguration(new File(path + "/" + file_NPCInfos + ".yml")).getList("NPCInfo");
	}
}
