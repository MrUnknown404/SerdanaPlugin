package main.java.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.serdana.Main;
import main.java.serdana.util.Reloadable;
import main.java.serdana.util.enums.EnumQuestTalkType;
import main.java.serdana.util.enums.EnumTalkType;
import main.java.serdana.util.infos.NPCInfo;

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
	
	/** Adds dialogue for the given {@link Shopkeeper}
	 * @param shop The shopkeeper to base the info around
	 */
	public void addNPCDialogue(Shopkeeper shop) {
		NPCInfos.add(new NPCInfo(shop.getId()));
		write();
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
				
				if (b && type == EnumTalkType.banned && !info.ignoresBannedItems()) {
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
	public void write() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path + "/" + file_NPCInfos + Main.TYPE);
			
			g.toJson(NPCInfos, fw);
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Returns a new {@link List} for the given {@link File}
	 * @return A List containing the contents of the read File
	 */
	private List<NPCInfo> read() {
		if (!new File(path + "/" + file_NPCInfos + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_NPCInfos + Main.TYPE + "! (Will be created)");
			
			NPCInfos = new ArrayList<NPCInfo>();
			NPCInfos.add(new NPCInfo(1));
			
			write();
		}
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		
		try {
			fr = new FileReader(path + "/" + file_NPCInfos + Main.TYPE);
			
			return g.fromJson(fr, new TypeToken<List<NPCInfo>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Returns a NPCInfo using the given ID
	 * @param id ID to search for
	 * @return An NPCInfo using the given ID
	 */
	public NPCInfo getNPCInfoFromID(int id) {
		for (NPCInfo info : NPCInfos) {
			if (info.getNPCID() == id) {
				return info;
			}
		}
		
		return null;
	}
}
