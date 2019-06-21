package main.java.serdana.util.infos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import main.java.serdana.util.Playerboard;

public class PartyInfo {

	private UUID leader;
	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> invites = new ArrayList<UUID>();
	private Playerboard board;
	
	public PartyInfo(UUID leader, Playerboard board) {
		this.leader = leader;
		this.board = board;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public List<UUID> getMembers() {
		return members;
	}
	
	public List<UUID> getInvites() {
		return invites;
	}
	
	public Playerboard getBoard() {
		return board;
	}
}
