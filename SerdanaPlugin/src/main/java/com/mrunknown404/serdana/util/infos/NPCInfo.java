package main.java.com.mrunknown404.serdana.util.infos;

import java.util.Random;

import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.enums.EnumTalkType;

public class NPCInfo {

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
	
	/** Makes a NPC talk using the possible messages written here
	 * @param p Player to check
	 * @param type EnumTalkType to check
	 * @return true if should be canceled, otherwise false
	 */
	public boolean talk(Player p, EnumTalkType type) {
		String[] list = new String[0];
		
		if (type == EnumTalkType.banned && !ignoresBannedItems) {
			list = bannedItemMessages;
		} else if (type == EnumTalkType.trade) {
			list = tradeMessages;
		} else {
			list = openMessages;
		}
		
		String msg = list[new Random().nextInt(list.length)];
		
		if (msg.contains("$player$")) {
			msg = msg.replaceAll("$player$", p.getDisplayName());
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
