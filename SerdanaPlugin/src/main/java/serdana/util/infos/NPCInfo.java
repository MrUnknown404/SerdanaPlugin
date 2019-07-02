package main.java.serdana.util.infos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.enums.EnumTalkType;

public class NPCInfo {

	protected final int NPCID;
	protected boolean ignoresBannedItems = true, isShop = true;
	protected String name = "";
	protected List<String> openMessages = new ArrayList<String>(), bannedItemMessages = new ArrayList<String>(), tradeMessages = new ArrayList<String>();
	
	public NPCInfo(int NPCID) {
		this.NPCID = NPCID;
	}
	
	/** Makes a NPC talk using the possible messages written here
	 * @param p Player to check
	 * @param type EnumTalkType to check
	 * @return true if should be canceled, otherwise false
	 */
	public boolean talk(Player p, EnumTalkType type) {
		List<String> list = new ArrayList<String>();
		
		if (type == EnumTalkType.banned && !ignoresBannedItems) {
			list = bannedItemMessages;
		} else if (type == EnumTalkType.trade) {
			list = tradeMessages;
		} else {
			list = openMessages;
		}
		
		if (list.isEmpty()) {
			return !isShop;
		}
		
		String msg = list.get(new Random().nextInt(list.size()));
		
		if (msg.contains("$player$")) {
			msg = msg.replaceAll("\\$player\\$", p.getDisplayName());
		}
		
		p.sendMessage(ColorHelper.addColor("[" + name + "&f] &7" + msg));
		
		if (!isShop || (!ignoresBannedItems && type == EnumTalkType.banned)) {
			return true;
		}
		
		return false;
	}
	
	public void setIgnoresBannedItems(boolean ignoresBannedItems) {
		this.ignoresBannedItems = ignoresBannedItems;
	}
	
	public void setShop(boolean isShop) {
		this.isShop = isShop;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addOpenMessages(String openMessages) {
		this.openMessages.add(openMessages);
	}
	
	public void addBannedItemMessages(String bannedItemMessages) {
		this.bannedItemMessages.add(bannedItemMessages);
	}
	
	public void addTradeMessages(String tradeMessages) {
		this.tradeMessages.add(tradeMessages);
	}
	
	public int getNPCID() {
		return NPCID;
	}
	
	public boolean ignoresBannedItems() {
		return ignoresBannedItems;
	}
	
	public boolean isShop() {
		return isShop;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getOpenMessages() {
		return openMessages;
	}
	
	public List<String> getBannedItemMessages() {
		return bannedItemMessages;
	}
	
	public List<String> getTradeMessages() {
		return tradeMessages;
	}
}
