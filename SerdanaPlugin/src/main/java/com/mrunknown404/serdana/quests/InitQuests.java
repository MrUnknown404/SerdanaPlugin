package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import main.java.com.mrunknown404.serdana.Main;

public class InitQuests {
	private static Map<Boolean, List<Quest>> allQuests = new HashMap<Boolean, List<Quest>>();
	
	static {
		Main main = (Main) Bukkit.getPluginManager().getPlugin("Serdana");
		allQuests.put(true, new ArrayList<Quest>());
		allQuests.put(false, new ArrayList<Quest>());
		
		addQuest(true, main.getQuestHandler().getQuestFile("DebugFetch"));
		addQuest(true, main.getQuestHandler().getQuestFile("DebugKill"));
		addQuest(false, main.getQuestHandler().getQuestFile("DebugWalk"));
	}
	
	private static void addQuest(boolean isActive, Quest q) {
		allQuests.get(isActive).add(q);
	}
	
	public static Map<Boolean, List<Quest>> getAllQuests() {
		return allQuests;
	}
}
