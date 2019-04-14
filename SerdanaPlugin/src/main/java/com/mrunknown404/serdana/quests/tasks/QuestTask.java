package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;

public abstract class QuestTask implements ConfigurationSerializable {
	protected EnumTaskCheckType type;
	protected String[] description;
	protected String[] completionMessage;
	protected int amount = 0, amountNeeded;
	
	public QuestTask(EnumTaskCheckType type, int amountNeeded, String[] description, String[] completionMessage) {
		this.type = type;
		this.description = description;
		this.completionMessage = completionMessage;
		this.amountNeeded = amountNeeded;
	}
	
	@SuppressWarnings("unchecked")
	public QuestTask(Map<String, Object> map) {
		type = EnumTaskCheckType.valueOf((String) map.get("type"));
		amount = (int) map.get("amount");
		amountNeeded = (int) map.get("amountNeeded");
		description = ((List<String>) map.get("description")).toArray(new String[0]);
		completionMessage = ((List<String>) map.get("completionMessage")).toArray(new String[0]);
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("type", type.toString());
		result.put("amount", amount);
		result.put("amountNeeded", amountNeeded);
		result.put("description", description);
		result.put("completionMessage", completionMessage);
		return result;
	}
	
	public abstract boolean checkForTask(Object obj);
	
	public boolean checkForFinishedTask() {
		if (amount >= amountNeeded) {
			return true;
		}
		
		return false;
	}
	
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
	}
	
	return false;
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
