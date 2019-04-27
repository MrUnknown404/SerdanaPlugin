package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;

public class QuestTaskWalk extends QuestTask implements ConfigurationSerializable {

	protected Location whereTo;
	
	public QuestTaskWalk(Location whereTo, String[] description, String[] completionMessage) {
		super(EnumTaskCheckType.playerTick, 1, description, completionMessage);
		this.whereTo = whereTo;
	}
	
	public QuestTaskWalk(Map<String, Object> map) {
		super(map);
		
		whereTo = (Location) map.get("whereTo");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("whereTo", whereTo);
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			Player p = (Player) obj;
			if (p.getLocation().distance(whereTo) <= 5d) {
				amount = amountNeeded;
				return true;
			}
		}
		
		return false;
	}
}
