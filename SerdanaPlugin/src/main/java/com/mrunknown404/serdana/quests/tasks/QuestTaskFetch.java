package main.java.com.mrunknown404.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.com.mrunknown404.serdana.util.EnumTaskCheckType;
import main.java.com.mrunknown404.serdana.util.math.MathHelper;

public class QuestTaskFetch extends QuestTask implements ConfigurationSerializable {

	protected ItemStack item;
	
	public QuestTaskFetch(ItemStack item, int amountNeeded, String[] description, String[] completionMessage) {
		super(EnumTaskCheckType.playerTick, amountNeeded, description, completionMessage);
		this.item = item;
		this.amountNeeded = amountNeeded;
	}
	
	public QuestTaskFetch(Map<String, Object> map) {
		super(map);
		
		item = (ItemStack) map.get("item");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("item", item);
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			Player p = (Player) obj;
			
			for (int i = 0; i < p.getInventory().getSize(); i++) {
				ItemStack it = p.getInventory().getItem(i);
				if (this.item.isSimilar(it)) {
					if (it.getAmount() > amount) {
						amount = (int) MathHelper.clamp(it.getAmount(), 0, amountNeeded);
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
