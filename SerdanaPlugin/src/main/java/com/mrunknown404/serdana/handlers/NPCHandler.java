package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.Reloadable;
import main.java.com.mrunknown404.serdana.util.enums.EnumQuestTalkType;
import main.java.com.mrunknown404.serdana.util.enums.EnumTalkType;
import main.java.com.mrunknown404.serdana.util.infos.NPCInfo;

public class NPCHandler extends Reloadable {

	private final Main main;
	private final File path;
	
	private final File file_NPCInfos = new File("NPCInfos");
	
	private List<NPCInfo> NPCInfos;
	
	public NPCHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	@Override
	protected void reload() {
		NPCInfos = read();
	}
	
	/** Makes given {@link Shopkeeper} talk to the specified {@link Player}
	 * @param shop The Shopkeeper that will talk
	 * @param p The Player the Shopkeeper that will talk to
	 * @param type The Shopkeeper's talk type
	 * @return true if should be canceled, otherwise false
	 */
	public boolean talk(Shopkeeper shop, Player p, EnumTalkType type) {
		for (NPCInfo info : NPCInfos) {
			if (info.getNPCID() == shop.getId()) {
				boolean b = info.talk(p, type);
				
				if (b && type == EnumTalkType.banned) {
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
	 * @return true if successful, otherwise false
	 */
	public boolean talk(Shopkeeper shop, Player p, EnumQuestTalkType type) {
		switch (type) {
			case start:
				return main.getQuestHandler().checkStartQuest(p, shop);
			case finish:
				return main.getQuestHandler().checkFinishQuest(p, shop);
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
	private void write() {
		File f = new File(path + "/" + file_NPCInfos + ".yml");
		
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("NPCInfos", NPCInfos);
		
		try {
			write.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Returns a new {@link List} for the given {@link File}
	 * @return A List containing the contents of the read File
	 */
	@SuppressWarnings("unchecked")
	private List<NPCInfo> read() {
		if (getClass().getResourceAsStream(Main.BASE_LOCATION + file_NPCInfos + ".yml") == null) {
			System.out.println("Could not find file inside jar: " + file_NPCInfos + ".yml!");
			
			List<?> list = YamlConfiguration.loadConfiguration(new File(path + "/" + file_NPCInfos + ".yml")).getList("NPCInfos");
			
			if (list == null) {
				System.out.println("Could not find file in config: " + file_NPCInfos + ".yml! (Will be created)");
				
				NPCInfos = new ArrayList<NPCInfo>();
				NPCInfos.add(new NPCInfo(1, true, true, "Steve",
						new String[] {"Test 1", "Test 2"},
						new String[] {"Banned 1", "Banned 2"},
						new String[] {"Trade 1", "Trade 2"}));
				
				write();
			}
			
			return (List<NPCInfo>) list;
		} else {
			InputStream s = getClass().getResourceAsStream(Main.BASE_LOCATION + file_NPCInfos + ".yml");
			return (List<NPCInfo>) YamlConfiguration.loadConfiguration(new InputStreamReader(s)).getList("NPCInfos");
		}
	}
}
