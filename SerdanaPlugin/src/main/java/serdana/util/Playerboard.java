package main.java.serdana.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import main.java.serdana.util.infos.PlayerboardInfo;

public class Playerboard {
	
	private List<Player> players = new ArrayList<Player>();
	private Scoreboard scoreboard;
	
	private Objective objective;
	private Objective buffer;
	
	private List<PlayerboardInfo> lines = new ArrayList<PlayerboardInfo>();

	public Playerboard(Player player, String name) {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		String subName = player.getName().length() <= 14 ? player.getName() : player.getName().substring(0, 14);
		
		objective = scoreboard.registerNewObjective("sb" + subName, "dummy", name);
		buffer = scoreboard.registerNewObjective("bf" + subName, "dummy", name);
		
		addPlayer(player);
	}
	
	/** Adds the given {@link Player}
	 * @param p Player to add
	 */
	public void addPlayer(Player p) {
		players.add(p);
		
		sendObjective(objective, ObjectiveMode.CREATE);
		sendObjectiveDisplay(objective);
		sendObjective(buffer, ObjectiveMode.CREATE);
		
		p.setScoreboard(scoreboard);
		
		for (Player pl : players) {
			set(pl.getUniqueId(), (int) Math.floor(pl.getHealth()), false);
		}
	}
	
	/** Removes the given {@link Player}
	 * @param p Player to remove
	 */
	public void removePlayer(Player p) {
		sendObjective(objective, ObjectiveMode.REMOVE);
		sendObjective(buffer, ObjectiveMode.REMOVE);
		
		set(p.getUniqueId(), (int) Math.floor(p.getHealth()), true);
		players.remove(p);
		
		sendObjective(objective, ObjectiveMode.CREATE);
		sendObjectiveDisplay(objective);
		sendObjective(buffer, ObjectiveMode.CREATE);
		
		for (Player pl : players) {
			set(pl.getUniqueId(), (int) Math.floor(pl.getHealth()), false);
		}
	}
	
	/** Sets the {@link Scoreboard}'s info
	 * @param id Player UUID to update
	 * @param score Player's health
	 * @param remove Should remove
	 */
	public void set(UUID id, int score, boolean remove) {
		String oldName = null;
		
		for (PlayerboardInfo info : lines) {
			if (info.getUUID().equals(id)) {
				if (Bukkit.getPlayer(id).getDisplayName() != info.getOldName()) {
					oldName = info.getOldName();
				}
			}
		}
		
		if (!remove) {
			if (oldName != null) {
				sendScore(buffer, oldName, score, true);
				sendScore(buffer, Bukkit.getPlayer(id).getDisplayName(), score, false);
				
				swapBuffers();
				
				sendScore(buffer, oldName, score, true);
				sendScore(buffer, Bukkit.getPlayer(id).getDisplayName(), score, false);
				
				for (PlayerboardInfo info : lines) {
					if (info.getUUID().equals(id)) {
						info.setOldName(Bukkit.getPlayer(id).getDisplayName());
					}
				}
			} else {
				sendScore(objective, Bukkit.getPlayer(id).getDisplayName(), score, false);
				sendScore(buffer, Bukkit.getPlayer(id).getDisplayName(), score, false);
				
				lines.add(new PlayerboardInfo(id, Bukkit.getPlayer(id).getDisplayName(), score));
			}
		} else {
			for (int i = 0; i < lines.size(); i++) {
				PlayerboardInfo info = lines.get(i);
				
				if (info.getUUID().equals(id)) {
					lines.remove(info);
				}
			}
		}
	}
	
	/** Swaps buffers */
	private void swapBuffers() {
		sendObjectiveDisplay(buffer);
		
		Objective temp = buffer;
		
		buffer = objective;
		objective = temp;
	}
	
	/** Sends the given {@link Objective} with the given {@link ObjectiveMode}
	 * @param obj Objective to send
	 * @param mode Type to send
	 */
	private void sendObjective(Objective obj, ObjectiveMode mode) {
		try {
			Object objHandle = NMS.getHandle(obj);
			Object packetObj = NMS.PACKET_OBJ.newInstance(objHandle, mode.ordinal());
			
			for (Player player : players) {
				NMS.sendPacket(packetObj, player);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.err.println(e);
		}
	}
	
	/** Sends the given {@link Objective} to display
	 * @param obj Objective to send to display
	 */
	private void sendObjectiveDisplay(Objective obj) {
		try {
			Object objHandle = NMS.getHandle(obj);
			Object packet = NMS.PACKET_DISPLAY.newInstance(1, objHandle);
			
			for (Player player : players) {
				NMS.sendPacket(packet, player);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.err.println(e);
		}
	}
	
	/** Sends the given Score
	 * @param obj Objective to send
	 * @param name Name to send
	 * @param score Score to send
	 * @param remove Should remove
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
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
			System.err.println(e);
		}
	}
	
	private enum ObjectiveMode {
		CREATE,
		REMOVE,
		UPDATE
	}
}
