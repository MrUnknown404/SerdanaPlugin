package main.java.serdana.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.Playerboard;
import main.java.serdana.util.infos.PartyInfo;

public class PartyHandler {
	private List<PartyInfo> parties = new ArrayList<PartyInfo>();
	
	/** Makes the given {@link Player} join the given Party leader's party
	 * @param partyLeader The Party leader the given player is joining
	 * @param playerJoining The Player joining
	 */
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
	
	/** Makes the given {@link Player} leave their Party
	 * @param playerLeaving Player thats leaving their Party
	 */
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
	
	/** Creates a Party
	 * @param creator Party's creator
	 */
	public void createParty(UUID creator) {
		Player p = Bukkit.getPlayer(creator);
		Playerboard board = new Playerboard(p, ColorHelper.addColor("&6Party's Health"));
		board.addPlayer(p);
		
		parties.add(new PartyInfo(creator, board));
	}
	
	/** Notifies the Party of the given {@link Player}'s Death
	 * @param death The player that died
	 */
	public void notifyDeath(UUID death) {
		for (PartyInfo p : parties) {
			Bukkit.getPlayer(p.getLeader()).sendMessage(ColorHelper.addColor("&c" + Bukkit.getPlayer(death).getDisplayName() + " has died!"));
			for (UUID id : p.getMembers()) {
				Bukkit.getPlayer(id).sendMessage(ColorHelper.addColor("&c" + Bukkit.getPlayer(death).getDisplayName() + " has died!"));
			}
		}
	}
	
	/** Invites the given {@link Player} to the given Party leader's Party
	 * @param partyLeader The Party Leader
	 * @param invite The Player who's getting invited
	 */
	public void invitePlayer(UUID partyLeader, UUID invite) {
		for (PartyInfo p : parties) {
			if (p.getLeader().equals(partyLeader)) {
				p.getInvites().add(invite);
				return;
			}
		}
	}
	
	/** Gets the {@link Player}'s {@link PartyInfo}
	 * @param id The Player to check
	 * @return Returns the given Player's PartyInfo
	 */
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
	
	/** Checks if the given {@link Player} has an invite from the given Party leader
	 * @param partyLeader The Party leader to check
	 * @param playerToCheck The Player to check
	 * @return true if the given Player has an invite from the given Party leader, otherwise false
	 */
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
	
	/** Checks if the given {@link Player} is in any Party
	 * @param pl Player to check
	 * @return true if the given Player is in any Party, otherwise false
	 */
	public boolean isPlayerInAnyParty(UUID pl) {
		if (getPlayersParty(pl) != null) {
			return true;
		}
		
		return false;
	}
	
	/** Checks if the given {@link Player} is in the given Party leader's Party
	 * @param leader Party leader to check
	 * @param pl Player to check
	 * @return true if the given Player is in the given Party leader's Party, otherwise false
	 */
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
	
	/** Checks if the given {@link Player} is a Party leader
	 * @param pl Player to check
	 * @return true if the given Player is the Party leader, otherwise false
	 */
	public boolean isPartyLeader(UUID pl) {
		for (PartyInfo info : parties) {
			if (info.getLeader().equals(pl)) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Checks if the given {@link Player}s are in the same Party
	 * @param player1 Player1 to check
	 * @param player2 Player2 to check
	 * @return true if the given Player's are in the same Party, otherwise false
	 */
	public boolean arePlayersInSameParty(UUID player1, UUID player2) {
		if (!isPlayerInAnyParty(player1) || !isPlayerInAnyParty(player2)) {
			return false;
		}
		
		PartyInfo info = getPlayersParty(player1);
		if (info.getLeader() == player2) {
			return true;
		} else if (info.getMembers().contains(player2)) {
			return true;
		}
		
		return false;
	}
}
