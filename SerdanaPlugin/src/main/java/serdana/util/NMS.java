package main.java.serdana.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMS {
	private static String packageName;
	
	public static final Field PLAYER_SCORES;
	
	public static final Constructor<?> PACKET_SCORE_REMOVE;
	public static final Constructor<?> PACKET_SCORE;
	
	public static final Object ENUM_SCORE_ACTION_CHANGE;
	public static final Object ENUM_SCORE_ACTION_REMOVE;
	
	public static final Constructor<?> SB_SCORE;
	public static final Method SB_SCORE_SET;
	
	public static final Constructor<?> PACKET_OBJ;
	public static final Constructor<?> PACKET_DISPLAY;
	
	public static final Field PLAYER_CONNECTION;
	public static final Method SEND_PACKET;
	
	private static Map<Class<?>, Method> handles = new HashMap<>();
	
	static {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		
		packageName = "net.minecraft.server." + name.substring(name.lastIndexOf('.') + 1);
		
		Field playerScores = null;
		
		Constructor<?> packetScoreRemove = null;
		Constructor<?> packetScore = null;
		
		Object enumScoreActionChange = null;
		Object enumScoreActionRemove = null;
		
		Constructor<?> sbScore = null;
		Method sbScoreSet = null;
		
		Constructor<?> packetObj = null;
		Constructor<?> packetDisplay = null;
		
		Field playerConnection = null;
		Method sendPacket = null;
		
		try {
			Class<?> packetScoreClass = getClass("PacketPlayOutScoreboardScore");
			Class<?> packetDisplayClass = getClass("PacketPlayOutScoreboardDisplayObjective");
			Class<?> packetObjClass = getClass("PacketPlayOutScoreboardObjective");
			
			Class<?> scoreClass = getClass("ScoreboardScore");
			Class<?> scoreActionClass = getClass("ScoreboardServer$Action");
			
			Class<?> sbClass = getClass("Scoreboard");
			Class<?> objClass = getClass("ScoreboardObjective");
			
			Class<?> playerClass = getClass("EntityPlayer");
			Class<?> playerConnectionClass = getClass("PlayerConnection");
			Class<?> packetClass = getClass("Packet");
			
			playerScores = sbClass.getDeclaredField("playerScores");
			playerScores.setAccessible(true);
			
			sbScore = scoreClass.getConstructor(sbClass, objClass, String.class);
			sbScoreSet = scoreClass.getMethod("setScore", int.class);
			
			packetScore = packetScoreClass.getConstructor(scoreActionClass, String.class, String.class, int.class);
			
			enumScoreActionChange = scoreActionClass.getEnumConstants()[0];
			enumScoreActionRemove = scoreActionClass.getEnumConstants()[1];
			
			packetObj = packetObjClass.getConstructor(objClass, int.class);
			
			packetDisplay = packetDisplayClass.getConstructor(int.class, objClass);
			
			playerConnection = playerClass.getField("playerConnection");
			sendPacket = playerConnectionClass.getMethod("sendPacket", packetClass);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		PLAYER_SCORES = playerScores;
		
		PACKET_SCORE_REMOVE = packetScoreRemove;
		PACKET_SCORE = packetScore;
		
		ENUM_SCORE_ACTION_CHANGE = enumScoreActionChange;
		ENUM_SCORE_ACTION_REMOVE = enumScoreActionRemove;
		
		SB_SCORE = sbScore;
		SB_SCORE_SET = sbScoreSet;
		
		PACKET_OBJ = packetObj;
		PACKET_DISPLAY = packetDisplay;
		
		PLAYER_CONNECTION = playerConnection;
		SEND_PACKET = sendPacket;
	}
	
	public static Class<?> getClass(String name) {
		try {
			return Class.forName(packageName + "." + name);
		} catch(ClassNotFoundException e) {
			return null;
		}
	}
	
	public static Object getHandle(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<?> clazz = obj.getClass();
		
		if (!handles.containsKey(clazz)) {
			Method method = clazz.getDeclaredMethod("getHandle");
			
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			
			handles.put(clazz, method);
		}
		
		return handles.get(clazz).invoke(obj);
	}
	
	public static void sendPacket(Object packet, Player... players) {
		for (Player p : players) {
			try {
				Object playerConnection = PLAYER_CONNECTION.get(getHandle(p));
				SEND_PACKET.invoke(playerConnection, packet);
			} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				System.err.println(e);
			}
		}
	}
}
