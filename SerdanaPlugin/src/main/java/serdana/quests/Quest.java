package main.java.serdana.quests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.serdana.quests.tasks.QuestTask;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.enums.EnumQuestState;
import main.java.serdana.util.enums.EnumScriptStartType;

public class Quest implements ConfigurationSerializable {

	private String name;
	private List<String> description, startMessages, finishMessages, turnInMessages, readyToTurnInMessages;
	private int questID, startID = -1, finishID = -1, currentTaskID = 0;
	private boolean readyToTurnIn = false, isActive = true;
	private EnumQuestState state = EnumQuestState.unknown;
	private List<ItemStack> rewards;
	private List<Integer> requirements;
	
	private List<QuestTask> tasks = new ArrayList<QuestTask>();
	
	/**
	 * @param questID
	 * @param name
	 * @param startMessages
	 * @param finishMessages
	 * @param description
	 * @param turnInMessages
	 * @param readyToTurnInMessages
	 * @param tasks
	 * @param startID
	 * @param finishID
	 * @param rewards
	 * @param requirements
	 */
	Quest(int questID, boolean isActive, String name, List<String> startMessages, List<String> finishMessages, List<String> description, List<String> turnInMessages,
			List<String> readyToTurnInMessages, List<QuestTask> tasks, int startID, int finishID, List<ItemStack> rewards, List<Integer> requirements) {
		this.questID = questID;
		this.name = name;
		this.startMessages = startMessages;
		this.finishMessages = finishMessages;
		this.tasks = tasks;
		this.startID = startID;
		this.finishID = finishID;
		this.rewards = rewards;
		this.description = description;
		this.turnInMessages = turnInMessages;
		this.readyToTurnInMessages = readyToTurnInMessages;
		this.requirements = requirements;
		this.isActive = isActive;
	}
	
	public Quest(String name, int startID, int finishID) {
		this(InitQuests.getNewQuestID(), true, name, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
				new ArrayList<QuestTask>(), startID, finishID, new ArrayList<ItemStack>(), new ArrayList<Integer>());
	}
	
	public Quest(Quest quest) {
		this(quest.questID, quest.isActive, quest.name, quest.startMessages, quest.finishMessages, quest.description, quest.turnInMessages, quest.readyToTurnInMessages,
				quest.tasks, quest.startID, quest.finishID, quest.rewards, quest.requirements);
	}
	
	@SuppressWarnings("unchecked")
	public Quest(Map<String, Object> map) {
		questID = (int) map.get("questID");
		name = (String) map.get("name");
		
		startID = (int) map.get("startID");
		finishID = (int) map.get("finishID");
		currentTaskID = (int) map.get("currentTaskID");
		state = EnumQuestState.valueOf((String) map.get("state"));
		tasks = (List<QuestTask>) map.get("tasks");
		description = (List<String>) map.get("description");
		startMessages = (List<String>) map.get("startMessages");
		finishMessages = (List<String>) map.get("finishMessages");
		readyToTurnInMessages = (List<String>) map.get("readyToTurnInMessages");
		turnInMessages = (List<String>) map.get("turnInMessages");
		rewards = (List<ItemStack>) map.get("rewards");
		readyToTurnIn = (boolean) map.get("readyToTurnIn");
		requirements = (List<Integer>) map.get("requirements");
		isActive = (boolean) map.get("isActive");
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("questID", questID);
		result.put("isActive", isActive);
		result.put("name", name);
		result.put("description", description);
		result.put("startMessages", startMessages);
		result.put("finishMessages", finishMessages);
		result.put("turnInMessages", turnInMessages);
		result.put("readyToTurnInMessages", readyToTurnInMessages);
		result.put("startID", startID);
		result.put("finishID", finishID);
		result.put("currentTaskID", currentTaskID);
		result.put("readyToTurnIn", readyToTurnIn);
		result.put("requirements", requirements);
		result.put("state", state.toString());
		result.put("rewards", rewards);
		result.put("tasks", tasks);
		return result;
	}
	
	/** Checks the {@link Quest}'s {@link QuestTask}s
	 * @param p Player to check
	 * @return true if the task was successful, otherwise false
	 */
	public boolean check(Player p) {
		if (state == EnumQuestState.accepted) {
			boolean b = getCurrentTask().checkForTask(p);
			checkFinishedTask(p);
			return b;
		}
		
		return false;
	}
	
	/** Checks the {@link Quest}'s {@link QuestTask}s
	 * @param p Player to check
	 * @param e Entity to check
	 * @return true if the task was successful, otherwise false
	 */
	public boolean check(Player p, Entity e) {
		if (state == EnumQuestState.accepted) {
			boolean b = getCurrentTask().checkForTask(e);
			checkFinishedTask(p);
			return b;
		}
		
		return false;
	}
	
	/** Checks the {@link Quest}'s {@link QuestTask}s
	 * @param p Player to check
	 * @param shop Shopkeeper to check
	 * @return true if the task was successful, otherwise false
	 */
	public boolean check(Player p, Shopkeeper shop) {
		if (state == EnumQuestState.accepted) {
			return getCurrentTask().checkForTask(shop);
		}
		
		return false;
	}
	
	/** Checks if the {@link QuestTask} is finished
	 * @param p Player to check
	 */
	public void checkFinishedTask(Player p) {
		if (state == EnumQuestState.accepted) {
			if (getCurrentTask().checkForFinishedTask()) {
				increaseTask(p);
			}
		}
	}
	
	/** Increases the currentTaskID
	 * @param p Player to send the task completion messages to
	 */
	public void increaseTask(Player p) {
		getCurrentTask().doScript(p, EnumScriptStartType.finish, currentTaskID);
		currentTaskID++;
		
		if (currentTaskID == tasks.size()) {
			readyToTurnIn = true;
			
			for (String s : readyToTurnInMessages) {
				p.sendMessage(ColorHelper.addColor(s));
			}
		} else {
			for (String s : getCurrentTask().getCompletionMessage()) {
				p.sendMessage(ColorHelper.addColor(s));
			}
			
			getCurrentTask().doScript(p, EnumScriptStartType.start, currentTaskID);
		}
	}
	
	public void setState(Player p, EnumQuestState state) {
		if (state == EnumQuestState.accepted) {
			getCurrentTask().doScript(p, EnumScriptStartType.start, currentTaskID);
		}
		
		this.state = state;
	}
	
	public void addReward(ItemStack item) {
		rewards.add(item);
	}
	
	public void setActive(boolean active) {
		this.isActive = active;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStartID(int startID) {
		this.startID = startID;
	}
	
	public void setFinishID(int finishID) {
		this.finishID = finishID;
	}
	
	public void removeDescription(int where) {
		description.remove(where);
	}
	
	public void removeStartMessages(int where) {
		startMessages.remove(where);
	}
	
	public void removeFinishedMessages(int where) {
		finishMessages.remove(where);
	}
	
	public void removeTurnInMessages(int where) {
		turnInMessages.remove(where);
	}
	
	public void removeReadyToTurnInMessages(int where) {
		readyToTurnInMessages.remove(where);
	}
	
	public void addDescription(String value) {
		description.add(value);
	}
	
	public void addStartMessages(String value) {
		startMessages.add(value);
	}
	
	public void addFinishedMessages(String value) {
		finishMessages.add(value);
	}
	
	public void addTurnInMessages(String value) {
		turnInMessages.add(value);
	}
	
	public void addReadyToTurnInMessages(String value) {
		readyToTurnInMessages.add(value);
	}
	
	public void addRequirements(int requirement) {
		requirements.add(requirement);
	}
	
	public QuestTask getCurrentTask() {
		return tasks.get(currentTaskID);
	}
	
	public int getQuestID() {
		return questID;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getStartMessages() {
		return startMessages;
	}
	
	public List<String> getFinishMessages() {
		return finishMessages;
	}
	
	public List<String> getDescription() {
		return description;
	}
	
	public List<String> getTurnInMessages() {
		return turnInMessages;
	}
	
	public List<String> getReadyToTurnInMessages() {
		return readyToTurnInMessages;
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
	
	public EnumQuestState getState() {
		return state;
	}
	
	public List<ItemStack> getRewards() {
		return rewards;
	}
	
	public List<Integer> getRequirements() {
		return requirements;
	}
	
	public List<QuestTask> getTasks() {
		return tasks;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quest) {
			if (((Quest) obj).questID == questID) {
				return true;
			}
		}
		
		return false;
	}
}
