package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.mrunknown404.serdana.Main;

public class InitQuests {
	private static Map<Boolean, List<Quest>> allQuests = new HashMap<Boolean, List<Quest>>();
	
	/** Registers all Quests */
	public static void register(Main main) {
		allQuests.clear();
		allQuests.put(true, new ArrayList<Quest>());
		allQuests.put(false, new ArrayList<Quest>());
		
		addQuest(true, main.getQuestHandler().getQuestFile("DebugFetch"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugKill"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugWalk"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugTalk"));
	}
	
	/** Adds the given {@link Quest} to a {@link List} and then added to a {@link Map} containing all Quests
	 * @param isActive Is the Quest active (Enabled)
	 * @param q Quest to add
	 */
	private static void addQuest(boolean isActive, Quest q) {
		allQuests.get(isActive).add(q);
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
		
		return null;
	}
	
	public static Map<Boolean, List<Quest>> getAllQuests() {
		return allQuests;
	}
}
