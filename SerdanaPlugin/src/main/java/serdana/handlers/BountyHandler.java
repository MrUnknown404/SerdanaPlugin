package main.java.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.java.serdana.Main;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.Reloadable;
import main.java.serdana.util.infos.BountyInfo;

public class BountyHandler extends Reloadable {
	
	private final File path;
	private final File file_bounties = new File("Bounties");
	
	private List<BountyInfo> bounties = new ArrayList<BountyInfo>();
	
	public BountyHandler(Main main) {
		this.path = main.getDataFolder();
	}
	
	/** Adds the given {@link BountyInfo} to the list of bounties
	 * @param b BountyInfo to add
	 */
	public void addBounty(BountyInfo b) {
		Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.addColor("&cBounty created!"));
		
		bounties.add(b);
		writeBounties();
		readBounties();
	}
	
	/** Removed the given {@link BountyInfo} from the list of bounties
	 * @param b BountyInfo to remove
	 * @param wasCanceled Was the bounty canceled
	 */
	public void removeBounty(BountyInfo b, boolean wasCanceled) {
		if (bounties.contains(b)) {
			if (wasCanceled) {
				Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.addColor("&cBounty canceled!"));
			}
			
			bounties.remove(b);
			writeBounties();
			readBounties();
		} else {
			Bukkit.getPlayer(b.getOwnerUUID()).sendMessage(ColorHelper.addColor("&cThat player does not have a bounty created by you!"));
		}
	}
	
	/** Rewards the given {@link Player}
	 * @param info The BountyInfo that was claimed
	 * @param killer The killer
	 */
	public void rewardPlayer(BountyInfo info, Player killer) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ColorHelper.addColor("&cThe bounty on " + Bukkit.getPlayer(info.getToKillUUID()).getDisplayName() + " was claimed!"));
		}
		
		ItemStack reward = new ItemStack(Material.FLINT, 1);
		ItemMeta meta = reward.getItemMeta();
		meta.setDisplayName(ColorHelper.addColor("&cBounty Reward Token"));
		reward.setItemMeta(meta);
		
		if (killer.getInventory().firstEmpty() == -1) {
			killer.sendMessage(ColorHelper.addColor("&cInventory is full!"));
		} else {
			killer.getInventory().addItem(reward);
		}
	}
	
	@Override
	protected void reload() {
		if (!new File(path + "/" + file_bounties + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_bounties + Main.TYPE + "! (Will be created)");
			bounties = new ArrayList<BountyInfo>();
			
			writeBounties();
		}
		
		readBounties();
	}
	
	/** Writes all bounties to file  */
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
	
	/** Reads all bounties from file */
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
