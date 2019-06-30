package main.java.serdana.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import main.java.serdana.Main;
import main.java.serdana.util.Reloadable;
import main.java.serdana.util.infos.SpecialPlayerInfo;
import main.java.serdana.util.specials.ISpecialEffectBase;
import main.java.serdana.util.specials.SpecialEffectAntenna;
import main.java.serdana.util.specials.SpecialEffectParticleBridge;
import main.java.serdana.util.specials.SpecialEffectSeizureHelmet;

public class SpecialPlayerHandler extends Reloadable {

	private final File path;
	private final File file_special = new File("SpecialPlayers");
	
	private List<SpecialPlayerInfo> infos = new ArrayList<SpecialPlayerInfo>();
	
	public SpecialPlayerHandler(Main main) {
		this.path = main.getDataFolder();
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				for (SpecialPlayerInfo info : infos) {
					Player p = Bukkit.getPlayer(info.getID());
					
					if (p != null && isPlayerEnabled(info)) {
						info.getEffect().getEffectBase().tick(info);
					}
				}
			}
		}, 0L, 1L);
	}
	
	@Override
	protected void reload() {
		if (!new File(path + "/" + file_special + Main.TYPE).exists()) {
			System.out.println("Could not find file: " + file_special + Main.TYPE + " (Will be created)");
			write();
		}
		
		read();
	}
	
	private void write() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path + "/" + file_special +  Main.TYPE);
			
			g.toJson(infos, fw);
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		
		try {
			fr = new FileReader(path + "/" + file_special + Main.TYPE);
			
			infos = g.fromJson(fr, new TypeToken<List<SpecialPlayerInfo>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SpecialPlayerInfo setupNewPlayer(UUID id, boolean enabled) {
		SpecialPlayerInfo info = new SpecialPlayerInfo(id, enabled, SpecialPlayerEffect.seizureHelmet);
		infos.add(info);
		return info;
	}
	
	public void setupPlayer(UUID id) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		
		if (info.isEnabled()) {
			info.getEffect().getEffectBase().start(info);
		}
	}
	
	public void setEffect(UUID id, SpecialPlayerEffect effect) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		info.setEffect(effect);
		write();
		
		if (info.isEnabled()) {
			info.getEffect().getEffectBase().start(info);
		}
	}
	
	public void togglePlayer(UUID id) {
		SpecialPlayerInfo info = getSpecialInfo(id);
		info.toggle();
		
		write();
		
		if (info.isEnabled()) {
			info.getEffect().getEffectBase().start(info);
		}
	}
	
	public boolean isPlayerEnabled(UUID id) {
		return isPlayerEnabled(getSpecialInfo(id));
	}
	
	public boolean isPlayerEnabled(SpecialPlayerInfo info) {
		return !infos.contains(info) ? false : info.isEnabled();
	}
	
	public SpecialPlayerInfo getSpecialInfo(UUID id) {
		for (SpecialPlayerInfo info : infos) {
			if (info.getID().equals(id)) {
				return info;
			}
		}
		
		return null;
	}
	
	public enum SpecialPlayerEffect {
		antenna       (new SpecialEffectAntenna()),
		seizureHelmet (new SpecialEffectSeizureHelmet()),
		particleBridge(new SpecialEffectParticleBridge());
		
		private final ISpecialEffectBase effectBase;
		
		private SpecialPlayerEffect(ISpecialEffectBase effectBase) {
			this.effectBase = effectBase;
		}
		
		public ISpecialEffectBase getEffectBase() {
			return effectBase;
		}
	}
}
