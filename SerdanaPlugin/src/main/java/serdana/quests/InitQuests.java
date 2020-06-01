package main.java.serdana.quests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import main.java.serdana.Main;

public class InitQuests {
	private static Main main;
	private static Map<Boolean, List<Quest>> allQuests = new HashMap<Boolean, List<Quest>>();
	
	/** Registers all Quests */
	public static void register(@Nullable Main main) {
		InitQuests.main = main;
		
		allQuests.clear();
		allQuests.put(true, new ArrayList<Quest>());
		allQuests.put(false, new ArrayList<Quest>());
		
		List<Quest> quests = getQuestsInFolder(new File(main.getDataFolder() + "/Quests/"));
		for (Quest q : quests) {
			addQuest(q.isActive(), q);
		}
		
		//*
		addQuest(false, main.getQuestHandler().getQuestFile("Debug Fetch!.yml"));
		addQuest(false, main.getQuestHandler().getQuestFile("Debug Kill!.yml"));
		addQuest(false, main.getQuestHandler().getQuestFile("Debug Walk!.yml"));
		addQuest(false, main.getQuestHandler().getQuestFile("Debug Talk!.yml"));
		//*/
	}
	
	public static List<Quest> getQuestsInFolder(File folder) {
		List<Quest> quests = new ArrayList<>();
		
		for (File entry : folder.listFiles()) {
			if (!entry.isDirectory()) {
				System.out.println(entry.getName() + " was found!");
				quests.add(main.getQuestHandler().getQuestFile(entry.getName()));
			}
		}
		
		return quests;
	}
	
	/** Adds the given {@link Quest} to a {@link List} and then added to a {@link Map} containing all Quests
	 * @param isActive Is the Quest active (Enabled)
	 * @param q Quest to add
	 */
	private static void addQuest(boolean isActive, Quest q) {
		if (!allQuests.get(isActive).contains(q)) {
			allQuests.get(isActive).add(q);
		}
	}
	
	public static void addNewQuest(Quest q) {
		File f = new File(main.getDataFolder() + "/Quests/" + q.getName().replaceAll(" ", "_") + ".yml");
		YamlConfiguration write = YamlConfiguration.loadConfiguration(f);
		write.set("Quest", q);
		
		try {
			write.save(f);
			
			if (allQuests.get(true).contains(q)) {
				allQuests.get(true).remove(q);
			} else if (allQuests.get(false).contains(q)) {
				allQuests.get(false).remove(q);
			}
			addQuest(true, q);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				main.getQuestHandler().reloadPlayer(p);
			}
			
			System.out.println("Successfully added new quest : " + q.getName().replaceAll(" ", "_"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void replaceQuest(Quest oldQuest, Quest newQuest) {
		File f = new File(main.getDataFolder() + "/Quests/" + oldQuest.getName().replaceAll(" ", "_") + ".yml");
		if (f.exists()) {
			f.delete();
		}
		
		addNewQuest(newQuest);
	}
	
	public static int getNewQuestID () {
		int id = 0;
		for (Quest q : allQuests.get(true)) {
			if (q.getQuestID() > id) {
				id = q.getQuestID();
			}
		}
		
		for (Quest q : allQuests.get(false)) {
			if (q.getQuestID() > id) {
				id = q.getQuestID();
			}
		}
		
		return id + 1;
	}
	
	/** Checks if a {@link Quest} with the given name exists
	 * @param name Name of the Quest to check for
	 * @return true if the there is a Quest with the given name, otherwise false
	 */
	public static boolean doesQuestExist(String name) {
		if (getQuest(name) != null) {
			return true;
		}
		
		return false;
	}
	
	/** Gets a {@link Quest} from the given name
	 * @param name Name of the Quest to get
	 * @return The Quest with the given name
	 */
	public static Quest getQuest(String name) {
		for (Quest q : allQuests.get(true)) {
			if (q.getName().equalsIgnoreCase(name)) {
				return q;
			}
		}
		
		for (Quest q : allQuests.get(false)) {
			if (q.getName().equalsIgnoreCase(name)) {
				return q;
			}
		}
		
		return null;
	}
	
	public static Map<Boolean, List<Quest>> getAllQuests() {
		return allQuests;
	}
}
