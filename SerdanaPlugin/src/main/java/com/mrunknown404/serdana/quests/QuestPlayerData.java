package main.java.com.mrunknown404.serdana.quests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import main.java.com.mrunknown404.serdana.util.QuestState;

public class QuestPlayerData implements ConfigurationSerializable {

	private final UUID playerID;
	private List<Quest> quests;
	
	public QuestPlayerData(UUID playerID, List<Quest> quests) {
		this.playerID = playerID;
		this.quests = quests;
	}
	
	@SuppressWarnings("unchecked")
	public QuestPlayerData(Map<String, Object> map) {
		playerID = UUID.fromString((String) map.get("playerID"));
		quests = (List<Quest>) map.get("quests");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("playerID", playerID.toString());
		result.put("quests", quests);
		return result;
	}
	
	public UUID getPlayerUUID() {
		return playerID;
	}
	
	public List<Quest> getQuests() {
		return quests;
	}
	
	public List<Quest> getQuestsThatHaveState(QuestState type) {
		List<Quest> list = new ArrayList<Quest>();
		
		for (Quest q : quests) {
			if (q.getState().equals(type)) {
				list.add(q);
			}
		}
		
		return list;
	}
	
	public void addQuest(Quest quest) {
		quests.add(new Quest(quest.getName(), quest.getDescription(), quest.getTasks(), quest.getStartID(), quest.getFinishID(), quest.getRewards()));
	}
	
	public boolean hasQuest(Quest q) {
		for (Quest qs : quests) {
			if (qs.equals(q)) {
				return true;
			}
		}
		
		return false;
	}
}
