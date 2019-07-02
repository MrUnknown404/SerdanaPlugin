package main.java.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import main.java.serdana.entities.util.EntityMonsterBase;
import main.java.serdana.entities.util.EnumCustomEntities;
import main.java.serdana.util.enums.EnumTaskCheckType;

public class QuestTaskKill extends QuestTask implements ConfigurationSerializable {

	protected String entityType;
	
	public QuestTaskKill(EntityType entityType, int amountNeeded, String[] description, String[] completionMessage, String[] scriptNames) {
		super(EnumTaskCheckType.entityDeath, amountNeeded, description, completionMessage, scriptNames);
		this.amountNeeded = amountNeeded;
		this.entityType = entityType.toString();
	}
	
	public QuestTaskKill(EnumCustomEntities entityType, int amountNeeded, String[] description, String[] completionMessage, String[] scriptNames) {
		super(EnumTaskCheckType.entityDeath, amountNeeded, description, completionMessage, scriptNames);
		this.amountNeeded = amountNeeded;
		this.entityType = entityType.toString();
	}
	
	public QuestTaskKill(Map<String, Object> map) {
		super(map);
		
		entityType = (String) map.get("entityType");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("entityType", entityType.toString());
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			if (((Entity) obj).getType().toString().equalsIgnoreCase(entityType)) {
				amount++;
				return true;
			} else {
				net.minecraft.server.v1_13_R2.Entity ent = ((CraftEntity) obj).getHandle();
				
				if (ent instanceof EntityMonsterBase) {
					if (((EntityMonsterBase) ent).getCustomEntityType().toString().equalsIgnoreCase(entityType)) {
						amount++;
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
