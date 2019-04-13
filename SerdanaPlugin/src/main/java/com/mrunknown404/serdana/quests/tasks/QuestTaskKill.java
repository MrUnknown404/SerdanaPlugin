package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;

public class QuestTaskKill extends QuestTask implements ConfigurationSerializable {

	protected EntityType entityType;
	protected int amountNeeded;
	protected int amountKilled;
	
	public QuestTaskKill(EntityType entityType, int amountNeeded, String[] description) {
		super(EnumTaskCheckType.entityDeath, description);
		this.amountNeeded = amountNeeded;
		this.entityType = entityType;
	}
	
	public QuestTaskKill(Map<String, Object> map) {
		super(map);
		
		entityType = EntityType.valueOf((String) map.get("entityType"));
		amountNeeded = (int) map.get("amountNeeded");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("entityType", entityType.toString());
		result.put("amountNeeded", amountNeeded);
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			if (((Entity) obj).getType() == entityType) {
				amountKilled++;
				
				if (amountKilled == amountNeeded) {
					return true;
				}
			}
		}
		
		return false;
	}
}
