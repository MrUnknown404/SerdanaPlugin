package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.com.mrunknown404.serdana.quests.tasks.QuestTask;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class Quest implements ConfigurationSerializable {

	private String name;
	private String[] description, completionMessage, turnInMessage;
	private int questID, startID = -1, finishID = -1, currentTaskID = 0;
	private boolean readyToTurnIn = false;
	private EnumQuestState state = EnumQuestState.unknown;
	private ItemStack[] rewards;
	private int[] requirements;
	
	private List<QuestTask> tasks = new ArrayList<QuestTask>();
	
	Quest(int questID, String name, String[] description, String[] completionMessage, String[] turnInMessage, List<QuestTask> tasks, int startID, int finishID,
			ItemStack[] rewards, int[] requirements) {
		this.questID = questID;
		this.name = name;
		this.tasks = tasks;
		this.startID = startID;
		this.finishID = finishID;
		this.rewards = rewards;
		this.description = description;
		this.completionMessage = completionMessage;
		this.turnInMessage = turnInMessage;
		this.requirements = requirements;
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
		description = ((List<String>) map.get("description")).toArray(new String[0]);
		completionMessage = ((List<String>) map.get("completionMessage")).toArray(new String[0]);
		turnInMessage = ((List<String>) map.get("turnInMessage")).toArray(new String[0]);
		rewards = ((List<ItemStack>) map.get("rewards")).toArray(new ItemStack[0]);
		readyToTurnIn = (boolean) map.get("readyToTurnIn");
		requirements = ((List<Integer>) map.get("requirements")).stream().mapToInt(i -> i).toArray();
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("questID", questID);
		result.put("name", name);
		result.put("description", description);
		result.put("completionMessage", completionMessage);
		result.put("turnInMessage", turnInMessage);
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
				for (String s : getCurrentTask().getCompletionMessage()) {
					p.sendMessage(ColorHelper.setColors(s));
				}
				
				increaseTask(p);
			}
		}
	}
	
	/** Increases the currentTaskID
	 * @param p Player to send the task completion messages to
	 */
	public void increaseTask(Player p) {
		currentTaskID++;
		
		if (currentTaskID == tasks.size()) {
			readyToTurnIn = true;
			
			for (String s : completionMessage) {
				p.sendMessage(ColorHelper.setColors(s));
			}
		}
	}
	
	public void setState(EnumQuestState state) {
		this.state = state;
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
	
	public String[] getCompletionMessage() {
		return completionMessage;
	}
	
	public String[] getTurnInMessage() {
		return turnInMessage;
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
	
	public ItemStack[] getRewards() {
		return rewards;
	}
	
	public int[] getRequirements() {
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
