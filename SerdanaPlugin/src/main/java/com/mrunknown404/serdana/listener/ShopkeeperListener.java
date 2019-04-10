package main.java.com.mrunknown404.serdana.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.JsonShopInfo;

public class ShopkeeperListener implements Listener {
	private final Main main;
	private final File path;
	
	private List<JsonShopInfo> shopInfos = new ArrayList<JsonShopInfo>();
	
	public ShopkeeperListener(Main main) {
		this.main = main;
		this.path = main.getDataFolder();
	}

	@EventHandler
	public void onShopTrade(ShopkeeperTradeEvent e) {
		String shopName = e.getShopkeeper().getName();
		JsonShopInfo info = shopInfos.get(1);
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		int r1;
		
		if (info.info.get(e.getShopkeeper().getId()) != null && !info.info.get(e.getShopkeeper().getId()).isEmpty()) {
			r1 = new Random().nextInt(info.info.get(e.getShopkeeper().getId()).size());
		} else {
			r1 = new Random().nextInt(info.info.get(-1).size());
		}
		
		if (info.info.containsKey(e.getShopkeeper().getId())) {
			if (!info.info.get(e.getShopkeeper().getId()).isEmpty()) {
				e.getPlayer().sendMessage("[" + shopName + "] " + info.info.get(e.getShopkeeper().getId()).get(r1));
			}
		} else {
			e.getPlayer().sendMessage("[" + shopName + "] " + info.info.get(-1).get(r1));
		}
	}
	
	@EventHandler
	public void onShopOpen(ShopkeeperOpenUIEvent e) {
		String shopName = e.getShopkeeper().getName();
		JsonShopInfo info = shopInfos.get(2);
		JsonShopInfo banInfo = shopInfos.get(3);
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		int r1;
		int r2;
		
		if (info.info.get(e.getShopkeeper().getId()) != null && !info.info.get(e.getShopkeeper().getId()).isEmpty()) {
			r1 = new Random().nextInt(info.info.get(e.getShopkeeper().getId()).size());
		} else {
			r1 = new Random().nextInt(info.info.get(-1).size());
		}
		
		if (banInfo.info.get(e.getShopkeeper().getId()) != null && !banInfo.info.get(e.getShopkeeper().getId()).isEmpty()) {
			r2 = new Random().nextInt(banInfo.info.get(e.getShopkeeper().getId()).size());
		} else {
			r2 = new Random().nextInt(banInfo.info.get(-1).size());
		}
		
		List<ItemStack> bannedItems = main.getBannedItemHandler().getPlayersBannedItems(e.getPlayer());
		
		if (!bannedItems.isEmpty()) {
			if (banInfo.info.containsKey(e.getShopkeeper().getId())) {
				if (!banInfo.info.get(e.getShopkeeper().getId()).isEmpty()) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("[" + shopName + "] " + banInfo.info.get(e.getShopkeeper().getId()).get(r2));
				}
			} else {
				e.setCancelled(true);
				e.getPlayer().sendMessage("[" + shopName + "] " + banInfo.info.get(-1).get(r2));
			}
		} else {
			if (info.info.containsKey(e.getShopkeeper().getId())) {
				if (!info.info.get(e.getShopkeeper().getId()).isEmpty()) {
					e.getPlayer().sendMessage("[" + shopName + "] " + info.info.get(e.getShopkeeper().getId()).get(r1));
				}
			} else {
				e.getPlayer().sendMessage("[" + shopName + "] " + info.info.get(-1).get(r1));
			}
		}
	}
	
	public void reload() {
		Map<Integer, List<String>> exampleMapDefault = new HashMap<Integer, List<String>>();
		List<String> exampleListDefault = new ArrayList<>();
		
		exampleListDefault.add("example 1");
		exampleListDefault.add("example 2");
		exampleMapDefault.put(-1, exampleListDefault);
		exampleMapDefault.put(1, exampleListDefault);
		
		if (shopInfos.isEmpty()) {
			shopInfos.add(new JsonShopInfo(exampleMapDefault, "ExampleList"));
			shopInfos.add(new JsonShopInfo("TradeMessages"));
			shopInfos.add(new JsonShopInfo("OpenShopMessages"));
			shopInfos.add(new JsonShopInfo("BannedItemMessages"));
		} else {
			shopInfos.set(0, new JsonShopInfo(exampleMapDefault, "ExampleList"));
			shopInfos.set(1, new JsonShopInfo("TradeMessages"));
			shopInfos.set(2, new JsonShopInfo("OpenShopMessages"));
			shopInfos.set(3, new JsonShopInfo("BannedItemMessages"));
		}
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		for (JsonShopInfo info : shopInfos) {
			if (!new File(path + "/" + info.fileName + Main.TYPE).exists()) {
				System.out.println("Could not find file: " + info.fileName + Main.TYPE + "! (Will be created)");
				
				try {
					fw = new FileWriter(path + "/" + info.fileName + Main.TYPE);
					
					g.toJson(info, fw);
					
					fw.flush();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < shopInfos.size(); i++) {
			JsonShopInfo info = shopInfos.get(i);
			
			try {
				fr = new FileReader(path + "/" + info.fileName + Main.TYPE);
				
				shopInfos.set(i, g.fromJson(fr, JsonShopInfo.class));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
