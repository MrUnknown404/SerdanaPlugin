package main.java.com.mrunknown404.serdana.util.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.BountyInfo;
import main.java.com.mrunknown404.serdana.util.ColorHelper;

public class BountyHandler {
	
	private final File path;
	private final File file_bounties = new File("Bounties");
	
	private List<BountyInfo> bounties = new ArrayList<BountyInfo>();
	
	public BountyHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	public void addBounty(BountyInfo b) {
		Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.setColors("&cBounty created!"));
		
		bounties.add(b);
		writeBounties();
		readBounties();
	}
	
	public void removeBounty(BountyInfo b, boolean wasDeath) {
		if (bounties.contains(b)) {
			if (!wasDeath) {
				Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.setColors("&cBounty canceled!"));
			}
			
			bounties.remove(b);
			writeBounties();
			readBounties();
		} else {
			Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.setColors("&cThat player does not have a bounty created by you!"));
		}
	}
	
	public void rewardPlayer(BountyInfo info) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ColorHelper.setColors("&cThe bounty on " + Bukkit.getPlayer(info.getToKillUUID()).getDisplayName() + " was claimed!"));
		}
		
		//give reward
	}
	
	public void reload() {
		if (!new File(path + "/" + file_bounties + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_bounties + Main.TYPE + "! (Will be created)");
			bounties = new ArrayList<BountyInfo>();
			
			writeBounties();
		}
		
		readBounties();
	}
	
	public void writeBounties() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path + "/" + file_bounties + Main.TYPE);
			
			g.toJson(bounties, fw);
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readBounties() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		
		try {
			fr = new FileReader(path + "/" + file_bounties + Main.TYPE);
			
			bounties = g.fromJson(fr, new TypeToken<List<BountyInfo>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<BountyInfo> getBounties() {
		return bounties;
	}
}
