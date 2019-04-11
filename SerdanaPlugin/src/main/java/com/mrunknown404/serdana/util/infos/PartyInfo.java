package main.java.com.mrunknown404.serdana.util.infos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyInfo {

	private UUID leader;
	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> invites = new ArrayList<UUID>();
	
	public PartyInfo(UUID leader) {
		this.leader = leader;
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
}
