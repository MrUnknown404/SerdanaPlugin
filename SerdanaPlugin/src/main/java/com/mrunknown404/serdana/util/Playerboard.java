package main.java.com.mrunknown404.serdana.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Playerboard {
	
	private List<Player> players = new ArrayList<Player>();
	private Scoreboard scoreboard;
	
	private Objective objective;
	private Objective buffer;
	
	private Map<String, Integer> lines = new HashMap<>();

	public Playerboard(Player player, String name) {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		String subName = player.getName().length() <= 14 ? player.getName() : player.getName().substring(0, 14);
		
		objective = scoreboard.registerNewObjective("sb" + subName, "dummy", name);
		buffer = scoreboard.registerNewObjective("bf" + subName, "dummy", name);
		
		addPlayer(player);
	}
	
	public void addPlayer(Player p) {
		players.add(p);
		
		sendObjective(objective, ObjectiveMode.CREATE);
		sendObjectiveDisplay(objective);
		sendObjective(buffer, ObjectiveMode.CREATE);
		
		p.setScoreboard(scoreboard);
		
		for (Player pl : players) {
			set(pl.getDisplayName() + " :", (int) Math.floor(pl.getHealth()), false);
		}
	}
	
	public void removePlayer(Player p) {
		sendObjective(objective, ObjectiveMode.REMOVE);
		sendObjective(buffer, ObjectiveMode.REMOVE);
		
		set(p.getDisplayName() + " :", (int) Math.floor(p.getHealth()), true);
		players.remove(p);
		
		sendObjective(objective, ObjectiveMode.CREATE);
		sendObjectiveDisplay(objective);
		sendObjective(buffer, ObjectiveMode.CREATE);
		
		for (Player pl : players) {
			set(pl.getDisplayName() + " :", (int) Math.floor(pl.getHealth()), false);
		}
	}
	
	public void set(String name, int score, boolean remove) {
		String oldName = null;
		
		Iterator<Entry<String, Integer>> it = lines.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> pair = it.next();
			
			if (pair.getKey().equals(name)) {
				oldName = name;
			}
		}
		
		if (!remove) {
			if (oldName != null) {
				sendScore(buffer, oldName, score, true);
				sendScore(buffer, name, score, false);
				
				swapBuffers();
				
				sendScore(buffer, oldName, score, true);
				sendScore(buffer, name, score, false);
			} else {
				sendScore(objective, name, score, false);
				sendScore(buffer, name, score, false);
				
				lines.put(name, score);
			}
		} else {
			sendScore(buffer, oldName, score, true);
			swapBuffers();
			sendScore(buffer, oldName, score, true);
			
			lines.remove(name);
		}
	}
	
	private void swapBuffers() {
		sendObjectiveDisplay(buffer);
		
		Objective temp = buffer;
		
		buffer = objective;
		objective = temp;
	}
	
	private void sendObjective(Objective obj, ObjectiveMode mode) {
		try {
			Object objHandle = NMS.getHandle(obj);
			Object packetObj = NMS.PACKET_OBJ.newInstance(objHandle, mode.ordinal());
			
			for (Player player : players) {
				NMS.sendPacket(packetObj, player);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.out.println(e);
		}
	}
	
	private void sendObjectiveDisplay(Objective obj) {
		try {
			Object objHandle = NMS.getHandle(obj);
			Object packet = NMS.PACKET_DISPLAY.newInstance(1, objHandle);
			
			for (Player player : players) {
				NMS.sendPacket(packet, player);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.out.println(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendScore(Objective obj, String name, int score, boolean remove) {
		try {
			Object sbHandle = NMS.getHandle(scoreboard);
			Object objHandle = NMS.getHandle(obj);
			Object sbScore = NMS.SB_SCORE.newInstance(sbHandle, objHandle, name);
			
			NMS.SB_SCORE_SET.invoke(sbScore, score);
			Map scores = (Map) NMS.PLAYER_SCORES.get(sbHandle);
			
			if (remove) {
				if (scores.containsKey(name)) {
					((Map) scores.get(name)).remove(objHandle);
				}
			} else {
				if (!scores.containsKey(name)) {
					scores.put(name, new HashMap());
				}
				
				((Map) scores.get(name)).put(objHandle, sbScore);
			}
			
			Object packet = NMS.PACKET_SCORE.newInstance(remove ? NMS.ENUM_SCORE_ACTION_REMOVE : NMS.ENUM_SCORE_ACTION_CHANGE, obj.getName(), name, score);
			
			for (Player player : players) {
				NMS.sendPacket(packet, player);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.out.println(e);
		}
	}
	
	private enum ObjectiveMode {
		CREATE,
		REMOVE,
		UPDATE
	}
}
