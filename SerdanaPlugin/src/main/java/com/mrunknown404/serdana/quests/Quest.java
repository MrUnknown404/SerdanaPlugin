package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.com.mrunknown404.serdana.quests.tasks.QuestTask;
import main.java.com.mrunknown404.serdana.util.QuestState;

public class Quest implements ConfigurationSerializable {

	private int questID;
	private String name;
	private String[] description;
	private int startID, finishID, currentTaskID = 0;
	private boolean readyToTurnIn = false;
	private QuestState state = QuestState.unknown;
	private ItemStack[] rewards;
	
	private List<QuestTask> tasks = new ArrayList<>();
	
	Quest(int questID, String name, String[] description, List<QuestTask> tasks, int startID, int finishID, ItemStack[] rewards) {
		this.questID = questID;
		this.name = name;
		this.tasks = tasks;
		this.startID = startID;
		this.finishID = finishID;
		this.rewards = rewards;
		this.description = description;
	}
	
	@SuppressWarnings("unchecked")
	public Quest(Map<String, Object> map) {
		questID = (int) map.get("questID");
		name = (String) map.get("name");
		
		List<String> list1 = (List<String>) map.get("description");
		description = new String[list1.size()];
		for (int i = 0; i < list1.size(); i++) {
			description[i] = list1.get(i);
		}
		
		startID = (int) map.get("startID");
		finishID = (int) map.get("finishID");
		state = QuestState.valueOf((String) map.get("state"));
		tasks = (List<QuestTask>) map.get("tasks");
		
		List<ItemStack> list2 = (List<ItemStack>) map.get("rewards");
		rewards = new ItemStack[list2.size()];
		for (int i = 0; i < list2.size(); i++) {
			rewards[i] = list2.get(i);
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("questID", questID);
		result.put("name", name);
		
		List<String> list1 = new ArrayList<String>();
		if (description.length != 0) {
			for (int i = 0; i < description.length; i++) {
				list1.add(description[i]);
			}
		}
		
		result.put("description", list1);
		result.put("startID", startID);
		result.put("finishID", finishID);
		result.put("state", state.toString());
		
		List<ItemStack> list2 = new ArrayList<ItemStack>();
		if (rewards.length != 0) {
			for (int i = 0; i < rewards.length; i++) {
				list2.add(rewards[i]);
			}
		}
		
		result.put("rewards", list2);
		result.put("tasks", tasks);
		return result;
	}
	
	public void check(Player p) {
		if (state == QuestState.accepted) {
			if (tasks.get(currentTaskID).checkForTask(p)) {
				increaseTask();
			}
		}
	}
	
	public void increaseTask() {
		currentTaskID++;
		
		if (currentTaskID > tasks.size()) {
			readyToTurnIn = true;
		}
	}
	
	public QuestTask getCurrentTask() {
		return tasks.get(currentTaskID);
	}
	
	public int getQuestID() {
		return questID;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public int getStartID() {
		return startID;
	}
	
	public int getFinishID() {
		return finishID;
	}
	
	public int getCurrentTaskID() {
		return currentTaskID;
	}
	
	public boolean isReadyToTurnIn() {
		return readyToTurnIn;
	}
	
	public QuestState getState() {
		return state;
	}
	
	public ItemStack[] getRewards() {
		return rewards;
	}
	
	public List<QuestTask> getTasks() {
		return tasks;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Quest)) {
			return false;
		}
		if (((Quest) obj).questID == questID) {
			return true;
		}
		
		return false;
	}
}
