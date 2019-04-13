package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import main.java.com.mrunknown404.serdana.Main;

public class InitQuests {
	private static List<Quest> allQuests = new ArrayList<Quest>();
	
	static {
		Main main = (Main) Bukkit.getPluginManager().getPlugin("Serdana");
		
		addQuest(main.getQuestHandler().getQuestFile("DebugFetch"));
		addQuest(main.getQuestHandler().getQuestFile("DebugKill"));
		addQuest(main.getQuestHandler().getQuestFile("DebugWalk"));
	}
	
	private static void addQuest(Quest q) {
		allQuests.add(q);
	}
	
	public static List<Quest> getAllQuests() {
		return allQuests;
	}
}
