package main.java.serdana.quests.tasks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.serdana.util.enums.EnumTaskCheckType;

public class QuestTaskTalk extends QuestTask implements ConfigurationSerializable {

	protected int talkID;
	protected String[] messages;
	
	public QuestTaskTalk(String[] description, String[] completionMessage, int talkID, String[] messages, String[] scriptNames) {
		super(EnumTaskCheckType.shopTalk, messages.length, description, completionMessage, scriptNames);
		this.talkID = talkID;
		this.messages = messages;
	}
	
	@SuppressWarnings("unchecked")
	public QuestTaskTalk(Map<String, Object> map) {
		super(map);
		
		talkID = (int) map.get("talkID");
		messages = ((List<String>) map.get("messages")).toArray(new String[0]);
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) super.serialize();
		result.put("talkID", talkID);
		result.put("messages", messages);
		return result;
	}
	
	@Override
	public boolean checkForTask(Object obj) {
		if (checkType(obj)) {
			if (talkID == ((Shopkeeper) obj).getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getTalkID() {
		return talkID;
	}
	
	public String getCurrentMessage() {
		return messages[amount];
	}
}
