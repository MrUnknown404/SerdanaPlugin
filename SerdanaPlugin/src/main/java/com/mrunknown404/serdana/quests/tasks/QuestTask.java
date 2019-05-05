package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.com.mrunknown404.serdana.scripts.ScriptHandler;
import main.java.com.mrunknown404.serdana.scripts.ScriptInfo;
import main.java.com.mrunknown404.serdana.scripts.ScriptInfo.ScriptStartType;
import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;

public abstract class QuestTask implements ConfigurationSerializable {
	protected EnumTaskCheckType type;
	protected String[] description, completionMessage;
	protected ScriptInfo[] scripts;
	protected int amount = 0, amountNeeded;
	
	public QuestTask(EnumTaskCheckType type, int amountNeeded, String[] description, String[] completionMessage, ScriptInfo[] scripts) {
		this.type = type;
		this.description = description;
		this.completionMessage = completionMessage;
		this.amountNeeded = amountNeeded;
		this.scripts = scripts;
	}
	
	@SuppressWarnings("unchecked")
	public QuestTask(Map<String, Object> map) {
		type = EnumTaskCheckType.valueOf((String) map.get("type"));
		amount = (int) map.get("amount");
		amountNeeded = (int) map.get("amountNeeded");
		description = ((List<String>) map.get("description")).toArray(new String[0]);
		completionMessage = ((List<String>) map.get("completionMessage")).toArray(new String[0]);
		scripts = ((List<ScriptInfo>) map.get("scripts")).toArray(new ScriptInfo[0]);
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("type", type.toString());
		result.put("amount", amount);
		result.put("amountNeeded", amountNeeded);
		result.put("description", description);
		result.put("completionMessage", completionMessage);
		result.put("scripts", scripts);
		return result;
	}
	
	/** Runs a Script found using the given variables
	 * @param p Player thats running the Script
	 * @param type Script start Type
	 * @param id Script Task ID to run
	 */
	public void doScript(Player p, ScriptStartType type, int id) {
		for (ScriptInfo scr : scripts) {
			if (scr.getScriptTaskID() == id && scr.getStartType() == type) {
				ScriptHandler.read(scr, p);
			}
		}
	}
	
	/** Checks if the {@link QuestTask} was successful
	 * @param obj Object to check
	 * @return true if task was successful, otherwise false
	 */
	public abstract boolean checkForTask(Object obj);
	
	/** Checks if the {@link QuestTask} is finished
	 * @return true if the task is finished, otherwise false
	 */
	public boolean checkForFinishedTask() {
		if (amount >= amountNeeded) {
			return true;
		}
		
		return false;
	}
	
	/** Checks if the given {@link Object} is the right type
	 * @param obj Object to check
	 * @return true if the given Object is the right type, otherwise false
	 */
	protected boolean checkType(Object obj) {
		switch (type) {
			case entityDeath:
				if (obj instanceof Entity) {
					return true;
				}
			case playerTick:
				if (obj instanceof Player) {
					return true;
				}
			case shopTalk:
				if (obj instanceof Shopkeeper) {
					return true;
				}
		}
		
		return false;
	}
	
	public void increaseAmmount() {
		amount++;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getAmountNeeded() {
		return amountNeeded;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public String[] getCompletionMessage() {
		return completionMessage;
	}
	
	public EnumTaskCheckType getTaskCheckType() {
		return type;
	}
}
