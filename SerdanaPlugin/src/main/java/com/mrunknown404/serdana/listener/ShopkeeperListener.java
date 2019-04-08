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

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.JsonShopInfo;

public class ShopkeeperListener implements Listener {

	private static final String TYPE = ".json";
	private final File path;
	
	private List<JsonShopInfo> shopInfos = new ArrayList<JsonShopInfo>();
	
	public ShopkeeperListener(File path) {
		this.path = path;
		
		reload(null);
	}

	@EventHandler
	public void onShopTrade(ShopkeeperTradeEvent e) {
		String shopName = e.getShopkeeper().getName();
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		if (shopInfos.get(1).info.containsKey(e.getShopkeeper().getId())) {
			e.getPlayer().sendMessage("[" + shopName + "] " + shopInfos.get(1).info.get(e.getShopkeeper().getId()).get(new Random().nextInt(3)));
		} else {
			e.getPlayer().sendMessage("[" + shopName + "] " + shopInfos.get(1).info.get(-1).get(new Random().nextInt(3)));
		}
	}
	
	@EventHandler
	public void onShopOpen(ShopkeeperOpenUIEvent e) {
		String shopName = e.getShopkeeper().getName();
		
		if (shopName.isEmpty()) {
			shopName = "null";
		}
		
		if (shopInfos.get(2).info.containsKey(e.getShopkeeper().getId())) {
			e.getPlayer().sendMessage("[" + shopName + "] " + shopInfos.get(2).info.get(e.getShopkeeper().getId()).get(new Random().nextInt(3)));
		} else {
			e.getPlayer().sendMessage("[" + shopName + "] " + shopInfos.get(2).info.get(-1).get(new Random().nextInt(3)));
		}
	}
	
	public void reload(CommandSender sender) {
		if (sender != null) {
			sender.sendMessage(ColorHelper.setColors("&cReloading Serdana's Configs!"));
		}
		System.out.println("Reloading Serdana's Configs!");
		
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
		} else {
			shopInfos.set(0, new JsonShopInfo(exampleMapDefault, "ExampleList"));
			shopInfos.set(1, new JsonShopInfo("TradeMessages"));
			shopInfos.set(2, new JsonShopInfo("OpenShopMessages"));
		}
		
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		FileReader fr = null;
		
		for (JsonShopInfo info : shopInfos) {
			if (!new File(path + "/" + info.fileName + TYPE).exists()) {
				System.out.println("Could not find file: " + info.fileName + TYPE + "! (Will be created)");
				
				try {
					fw = new FileWriter(path + "/" + info.fileName + TYPE);
					
					g.toJson(info, fw);
					
					fw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < shopInfos.size(); i++) {
			JsonShopInfo info = shopInfos.get(i);
			
			try {
				fr = new FileReader(path + "/" + info.fileName + TYPE);
				
				shopInfos.set(i, g.fromJson(fr, JsonShopInfo.class));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if (sender != null) {
			sender.sendMessage(ColorHelper.setColors("&cFinished Serdana's Configs!"));
		}
		System.out.println("Finished Serdana's Configs!");
	}
}
