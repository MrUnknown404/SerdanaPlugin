package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.ArrayList;
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
	
	public QuestTask(EnumTaskCheckType type, String[] description) {
		this.type = type;
		this.description = description;
	}
	
	@SuppressWarnings("unchecked")
	public QuestTask(Map<String, Object> map) {
		type = EnumTaskCheckType.valueOf((String) map.get("type"));
		
		List<String> list = (List<String>) map.get("description");
		description = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			description[i] = list.get(i);
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("type", type.toString());
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < description.length; i++) {
			list.add(description[i]);
		}
		
		result.put("description", list);
		return result;
	}
	
	public abstract boolean checkForTask(Object obj);
	
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
	
	public String[] getDescription() {
		return description;
	}
	
	public EnumTaskCheckType getTaskCheckType() {
		return type;
	}
}
