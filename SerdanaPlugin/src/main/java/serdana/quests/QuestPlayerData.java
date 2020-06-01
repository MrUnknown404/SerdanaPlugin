package main.java.serdana.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.serdana.util.enums.EnumQuestState;

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
	
	/** Gets all {@link Quest}s that have the given {@link EnumQuestState}
	 * @param type Type to check for
	 * @return All Quests that have the given EnumQuestState
	 */
	public List<Quest> getQuestsThatHaveState(EnumQuestState type) {
		List<Quest> list = new ArrayList<Quest>();
		
		for (Quest q : quests) {
			if (q.getState().equals(type)) {
				list.add(q);
			}
		}
		
		return list;
	}
	
	/** Gets all {@link Quest}s that are not the given {@link EnumQuestState}
	 * @param type Type to check for
	 * @return All Quests that do not have the given EnumQuestState
	 */
	public List<Quest> getQuestsThatAreNotState(EnumQuestState type) {
		List<Quest> list = new ArrayList<Quest>();
		
		for (Quest q : quests) {
			if (!q.getState().equals(type)) {
				list.add(q);
			}
		}
		
		return list;
	}
	
	/** Sets the given {@link Quest}'s state to the given {@link EnumQuestState}
	 * @param p Player to potentially start Script for
	 * @param quest Quest who's state will be edited
	 * @param state New EnumQuestState
	 */
	public void setQuestState(Player p, Quest quest, EnumQuestState state) {
		for (int i = 0; i < quests.size(); i++) {
			Quest q = quests.get(i);
			
			if (q.equals(quest)) {
				if (state == EnumQuestState.unknown) {
					removeQuest(q);
					addQuest(InitQuests.getQuest(quest.getName()));
				} else {
					q.setState(p, state);
				}
			}
		}
	}
	
	/** Adds the given {@link Quest}
	 * @param quest Quest to add
	 */
	public void addQuest(Quest quest) {
		quests.add(new Quest(quest));
	}
	
	/** Removes the given {@link Quest}
	 * @param quest Quest to remove
	 */
	public void removeQuest(Quest quest) {
		for (int i = 0; i < quests.size(); i++) {
			Quest q = quests.get(i);
			
			if (q.equals(quest)) {
				quests.remove(q);
			}
		}
	}
	
	/** Checks if this {@link QuestPlayerData} contains the given {@link Quest}
	 * @param q Quest to check for
	 * @return true if this contains the given Quest, otherwise false
	 */
	public boolean hasQuest(Quest q) {
		for (Quest qs : quests) {
			if (qs.equals(q)) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Checks if the given {@link Quest} is accepted
	 * @param q Quest to check if accepted
	 * @return true of the given Quest is accepted, otherwise false
	 */
	public boolean hasAcceptedQuest(Quest q) {
		for (Quest qs : quests) {
			if (qs.equals(q) && qs.getState() == EnumQuestState.accepted) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Checks if the given {@link Quest} is finished
	 * @param q Quest to check if finished
	 * @return true of the given Quest is finished, otherwise false
	 */
	public boolean hasFinishedQuest(Quest q) {
		for (Quest qs : quests) {
			if (qs.equals(q) && qs.getState() == EnumQuestState.finished) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Checks if this QuestPlayerData has an old Quest
	 * @param q Quest to check
	 * @return true if this QuestPlayerData has an old Quest, otherwise false
	 */
	public boolean isOldQuest(Quest q) {
		for (Quest oldQ : quests) {
			if (oldQ.getQuestID() == q.getQuestID()) {
				return oldQ.isActive() == q.isActive() &&
						oldQ.getName().equals(q.getName()) &&
						areEqual(oldQ.getDescription().toArray(), q.getDescription().toArray()) &&
						areEqual(oldQ.getReadyToTurnInMessages().toArray(), q.getReadyToTurnInMessages().toArray()) &&
						areEqual(oldQ.getTurnInMessages().toArray(), q.getTurnInMessages().toArray()) &&
						areEqual(oldQ.getStartMessages().toArray(), q.getStartMessages().toArray()) &&
						areEqual(oldQ.getFinishMessages().toArray(), q.getFinishMessages().toArray()) &&
						areEqual(oldQ.getRewards(), q.getRewards()) &&
						areEqual(oldQ.getRequirements().toArray(), q.getRequirements().toArray()) &&
						oldQ.getStartID() == q.getStartID() &&
						oldQ.getFinishID() == q.getFinishID() &&
						oldQ.getTasks().equals(q.getTasks()) ? false : true;
			}
		}
		
		return true;
	}
	
	private boolean areEqual(List<ItemStack> list, List<ItemStack> list2) {
		return areEqual(list.toArray(), list2.toArray());
	}
	
	private boolean areEqual(Object[] arr1, Object[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		
		Arrays.sort(arr1);
		Arrays.sort(arr2);
		
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[0].getClass().isPrimitive()) {
				if (arr1[i] != arr2[i]) {
					System.out.println("These are out of date : " + arr1[i] + ":" + arr2[i]);
					return false;
				}
			}
			
			if (!arr1[i].equals(arr2[i])) {
				System.out.println("These are out of date : " + arr1[i] + ":" + arr2[i]);
				return false;
			}
		}
		return true;
	}
	
	public UUID getPlayerUUID() {
		return playerID;
	}
	
	public List<Quest> getQuests() {
		return quests;
	}
}
