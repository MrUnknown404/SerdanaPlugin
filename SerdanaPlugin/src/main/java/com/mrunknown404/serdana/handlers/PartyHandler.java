package main.java.com.mrunknown404.serdana.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.util.ColorHelper;
import main.java.com.mrunknown404.serdana.util.Playerboard;
import main.java.com.mrunknown404.serdana.util.infos.PartyInfo;

public class PartyHandler {
	private List<PartyInfo> parties = new ArrayList<PartyInfo>();
	
	public void joinParty(UUID partyLeader, UUID playerJoining) {
		for (int i = 0; i < parties.size(); i++) {
			PartyInfo p = parties.get(i);
			
			if (p.getLeader().equals(partyLeader)) {
				p.getMembers().add(playerJoining);
				p.getInvites().remove(playerJoining);
				
				p.getBoard().addPlayer(Bukkit.getPlayer(playerJoining));
				return;
			}
		}
	}
	
	public void leaveParty(UUID playerLeaving) {
		for (int i = 0; i < parties.size(); i++) {
			PartyInfo p = parties.get(i);
			
			if (p.getLeader().equals(playerLeaving)) {
				p.getBoard().removePlayer(Bukkit.getPlayer(p.getLeader()));
				for (UUID id : p.getMembers()) {
					p.getBoard().removePlayer(Bukkit.getPlayer(id));
				}
				
				parties.remove(p);
				return;
			} else if (p.getMembers().contains(playerLeaving)) {
				p.getMembers().remove(playerLeaving);
				p.getBoard().removePlayer(Bukkit.getPlayer(playerLeaving));
				return;
			}
		}
	}
	
	public void createParty(UUID creator) {
		Player p = Bukkit.getPlayer(creator);
		Playerboard board = new Playerboard(p, ColorHelper.setColors("&6Party's Health"));
		board.set(p.getDisplayName() + " :", (int) p.getHealth(), false);
		
		parties.add(new PartyInfo(creator, board));
	}
	
	public void notifyDeath(UUID death) {
		for (PartyInfo p : parties) {
			Bukkit.getPlayer(p.getLeader()).sendMessage(ColorHelper.setColors("&c" + Bukkit.getPlayer(death).getDisplayName() + " has died!"));
			for (UUID id : p.getMembers()) {
				Bukkit.getPlayer(id).sendMessage(ColorHelper.setColors("&c" + Bukkit.getPlayer(death).getDisplayName() + " has died!"));
			}
		}
	}
	
	public void invitePlayer(UUID partyLeader, UUID invite) {
		for (PartyInfo p : parties) {
			if (p.getLeader().equals(partyLeader)) {
				p.getInvites().add(invite);
				return;
			}
		}
	}
	
	public PartyInfo getPlayersParty(UUID id) {
		for (PartyInfo p : parties) {
			if (p.getLeader().equals(id)) {
				return p;
			} else if (p.getMembers().contains(id)) {
				return p;
			}
		}
		
		return null;
	}
	
	public boolean doesPlayerHaveInvite(UUID partyLeader, UUID playerToCheck) {
		for (PartyInfo p : parties) {
			if (p.getLeader().equals(partyLeader)) {
				if (!p.getInvites().isEmpty() && p.getInvites().contains(playerToCheck)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isPlayerInAnyParty(UUID pl) {
		PartyInfo info = getPlayersParty(pl);
		
		if (info != null) {
			return true;
		}
		
		return false;
	}
	
	public boolean isPlayerInCertainParty(UUID leader, UUID pl) {
		if (parties.isEmpty()) {
			return false;
		}
		
		PartyInfo info = getPlayersParty(pl);
		if (info.getLeader().equals(leader)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isPartyLeader(UUID pl) {
		for (PartyInfo info : parties) {
			if (info.getLeader().equals(pl)) {
				return true;
			}
		}
		
		return false;
	}
}
