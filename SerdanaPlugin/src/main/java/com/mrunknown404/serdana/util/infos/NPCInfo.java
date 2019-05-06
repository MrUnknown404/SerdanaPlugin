package main.java.com.mrunknown404.serdana.util.infos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.enums.EnumTalkType;

public class NPCInfo implements ConfigurationSerializable {

	protected int NPCID;
	protected boolean ignoresBannedItems, isShop;
	protected String name;
	protected String[] openMessages, bannedItemMessages, tradeMessages;
	
	public NPCInfo(int NPCID, boolean ignoresBannedItems, boolean isShop, String name, String[] openMessages, String[] bannedItemMessages, String[] tradeMessages) {
		this.NPCID = NPCID;
		this.ignoresBannedItems = ignoresBannedItems;
		this.isShop = isShop;
		this.name = name;
		this.openMessages = openMessages;
		this.bannedItemMessages = bannedItemMessages;
		this.tradeMessages = tradeMessages;
	}
	
	@SuppressWarnings("unchecked")
	public NPCInfo(Map<String, Object> map) {
		NPCID = (int) map.get("NPCID");
		ignoresBannedItems = (boolean) map.get("ignoresBannedItems");
		isShop = (boolean) map.get("isShop");
		name = (String) map.get("name");
		openMessages = ((List<String>) map.get("openMessages")).toArray(new String[0]);
		bannedItemMessages = ((List<String>) map.get("bannedItemMessages")).toArray(new String[0]);
		tradeMessages = ((List<String>) map.get("tradeMessages")).toArray(new String[0]);
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("NPCID", NPCID);
		result.put("ignoresBannedItems", ignoresBannedItems);
		result.put("isShop", isShop);
		result.put("name", name);
		result.put("openMessages", openMessages);
		result.put("bannedItemMessages", bannedItemMessages);
		result.put("tradeMessages", tradeMessages);
		return result;
	}
	
	/** Makes a NPC talk using the possible messages written here
	 * @param p Player to check
	 * @param type EnumTalkType to check
	 * @return true if should be canceled, otherwise false
	 */
	public boolean talk(Player p, EnumTalkType type) {
		String[] list = new String[0];
		
		if (type == EnumTalkType.open) {
			list = openMessages;
		} else if (type == EnumTalkType.banned && ignoresBannedItems) {
			list = bannedItemMessages;
		} else if (type == EnumTalkType.trade) {
			list = tradeMessages;
		}
		
		String msg = Pattern.quote(list[new Random().nextInt(list.length)]);
		msg = msg.substring(2, msg.length() - 2);
		
		if (msg.contains("{player}")) {
			msg = msg.replaceAll("\\{player\\}", p.getDisplayName());
		}
		
		p.sendMessage(ColorHelper.setColors("[" + name + "&f] " + msg));
		
		if (!isShop || !ignoresBannedItems) {
			return true;
		}
		
		return false;
	}
	
	public int getNPCID() {
		return NPCID;
	}
	
	public boolean isIgnoresBannedItems() {
		return ignoresBannedItems;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getOpenMessages() {
		return openMessages;
	}
	
	public String[] getBannedItemMessages() {
		return bannedItemMessages;
	}
	
	public String[] getTradeMessages() {
		return tradeMessages;
	}
}
