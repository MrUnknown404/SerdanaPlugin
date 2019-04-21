package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.mrunknown404.serdana.Main;

public class InitQuests {
	private static Map<Boolean, List<Quest>> allQuests = new HashMap<Boolean, List<Quest>>();
	
	public static void register(Main main) {
		allQuests.clear();
		allQuests.put(true, new ArrayList<Quest>());
		allQuests.put(false, new ArrayList<Quest>());
		
		addQuest(true, main.getQuestHandler().getQuestFile("DebugFetch"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugKill"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugWalk"));
	}
	
	private static void addQuest(boolean isActive, Quest q) {
		allQuests.get(isActive).add(q);
	}
	
	public static boolean doesQuestExist(String name) {
		if (getQuest(name) != null) {
			return true;
		}
		
		return false;
	}
	
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
