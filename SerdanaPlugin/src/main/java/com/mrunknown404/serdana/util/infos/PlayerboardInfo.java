package main.java.com.mrunknown404.serdana.util.infos;

import java.util.UUID;

public class PlayerboardInfo {

	private final UUID uuid;
	private String oldName;
	private int score;
	
	public PlayerboardInfo(UUID uuid, String oldName, int score) {
		this.uuid = uuid;
		this.oldName = oldName;
		this.score = score;
	}
	
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public int getScore() {
		return score;
	}
}
