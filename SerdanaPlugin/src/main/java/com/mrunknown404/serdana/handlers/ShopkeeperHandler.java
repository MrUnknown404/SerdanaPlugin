package main.java.com.mrunknown404.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;

import main.java.com.mrunknown404.serdana.Main;

public class ShopkeeperHandler {

	private final Main main;
	private final File path;
	
	private final File file_openMessages = new File("OpenMessages");
	private final File file_tradeMessages = new File("TradeMessages");
	private final File file_bannedItemMessages = new File("BannedItemMessages");
	
	private Map<Integer, List<String>> openMessages;
	private Map<Integer, List<String>> tradeMessages;
	private Map<Integer, List<String>> bannedItemMessages;
	
	public ShopkeeperHandler(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}
	
	/** Reloads this class's Configs */
	public void reload() {
		Bukkit.getConsoleSender().sendMessage("Reloading " + getClass().getSimpleName() + "'s Configs!");
		
		if (!new File(path + "/NPCMessages/" + file_openMessages + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_openMessages + Main.TYPE + "! (Will be created)");
			write(file_openMessages);
		}
		
		if (!new File(path + "/NPCMessages/" + file_tradeMessages + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_tradeMessages + Main.TYPE + "! (Will be created)");
			write(file_tradeMessages);
		}
		
		if (!new File(path + "/NPCMessages/" + file_bannedItemMessages + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_bannedItemMessages + Main.TYPE + "! (Will be created)");
			write(file_bannedItemMessages);
		}
		
		openMessages = read(file_openMessages);
		tradeMessages = read(file_tradeMessages);
		bannedItemMessages = read(file_bannedItemMessages);
		
		Bukkit.getConsoleSender().sendMessage("Finished " + getClass().getSimpleName() + "'s Configs!");
	}
	
	/** Makes given {@link Shopkeeper} talk to the specified {@link Player}
	 * @param shop The Shopkeeper that will talk
	 * @param p The Player the Shopkeeper that will talk to
	 * @param type The Shopkeeper's talk type
	 * @return false if the Shopkeeper ignores banned items, otherwise true
	 */
	public boolean talk(Shopkeeper shop, Player p, TalkType type) {
		String shopName = shop.getName();
		int r;
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		Map<Integer, List<String>> map = null;
		
		if (type == TalkType.open) {
			map = openMessages;
		} else if (type == TalkType.trade) {
			map = tradeMessages;
		} else if (type == TalkType.banned) {
			map = bannedItemMessages;
		}
		
		if (map.get(shop.getId()) != null && !map.get(shop.getId()).isEmpty()) {
			r = new Random().nextInt(map.get(shop.getId()).size());
		} else {
			r = new Random().nextInt(map.get(-1).size());
		}
		
		if (map.containsKey(shop.getId())) {
			if (!map.get(shop.getId()).isEmpty()) {
				p.sendMessage("[" + shopName + "] " + map.get(shop.getId()).get(r));
			}
		} else {
			p.sendMessage("[" + shopName + "] " + map.get(-1).get(r));
		}
		
		if (type == TalkType.banned) {
			if (map.containsKey(shop.getId())) {
				if (!map.get(shop.getId()).isEmpty()) {
					main.getBannedItemHandler().performBannedAction(p, main.getBannedItemHandler().getPlayersBannedItems(p).size());
					return true;
				}
				
				talk(shop, p, TalkType.open);
				return false;
			}
			
			main.getBannedItemHandler().performBannedAction(p, main.getBannedItemHandler().getPlayersBannedItems(p).size());
		}
		
		return true;
	}
	
	/** Writes a new {@link HashMap} for the given {@link File}
	 * @param f The File that will be written to
	 */
	private void write(File f) {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path + "/NPCMessages/" + f + Main.TYPE);
			
			g.toJson(new HashMap<Integer, List<String>>(), fw);
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Writes a new {@link HashMap} for the given {@link File}
	 * @param f The File that will be read from
	 * @return A HashMap containing the contents of the read File
	 */
	private Map<Integer, List<String>> read(File f) {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		
		try {
			fr = new FileReader(path + "/NPCMessages/" + f + Main.TYPE);
			
			return g.fromJson(fr, new TypeToken<Map<Integer, List<String>>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public enum TalkType {
		open,
		trade,
		banned;
	}
}
