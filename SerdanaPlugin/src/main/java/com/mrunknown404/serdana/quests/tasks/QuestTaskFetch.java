package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;

public class QuestTaskFetch extends QuestTask implements ConfigurationSerializable {

	protected int amountNeeded;
	protected ItemStack item;
	
	public QuestTaskFetch(ItemStack item, int amountNeeded, String[] description) {
		super(EnumTaskCheckType.playerTick, description);
		this.item = item;
		this.amountNeeded = amountNeeded;
	}
	
	public QuestTaskFetch(Map<String, Object> map) {
		super(map);
		
		amountNeeded = (int) map.get("amountNeeded");
		item = (ItemStack) map.get("item");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("amountNeeded", amountNeeded);
		result.put("item", item);
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			Player p = (Player) obj;
			
			for (int i = 0; i < p.getInventory().getSize(); i++) {
				ItemStack item = p.getInventory().getItem(i);
				if (this.item.isSimilar(item) && item.getAmount() >= amountNeeded) {
					return true;
				}
			}
		}
		
		return false;
	}
}
