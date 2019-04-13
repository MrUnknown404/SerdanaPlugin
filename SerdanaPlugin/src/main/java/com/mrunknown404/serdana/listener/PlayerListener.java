package main.java.com.mrunknown404.serdana.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.infos.BountyInfo;

public class PlayerListener implements Listener {
	private final Main main;
	
	public PlayerListener(Main main) {
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		main.getQuestHandler().setupPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent e) {
		if (main.getPartyHandler().isPlayerInAnyParty(e.getPlayer().getUniqueId()) ) {
			main.getPartyHandler().leaveParty(e.getPlayer().getUniqueId());
		}
		
		if ((main.getQuestHandler().isPlayerSetup(e.getPlayer()))) {
			main.getQuestHandler().unsetupPlayer(e.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (main.getPartyHandler().isPlayerInAnyParty(e.getEntity().getUniqueId()) ) {
			main.getPartyHandler().notifyDeath(e.getEntity().getUniqueId());
		}
		
		if (e.getEntity().getKiller() instanceof Player) {
			List<BountyInfo> bounties = main.getBountyHandler().getBounties();
			
			for (int i = 0; i < bounties.size(); i++) {
				BountyInfo b = bounties.get(i);
				if (e.getEntity().getUniqueId().equals(b.getToKillUUID())) {
					main.getBountyHandler().rewardPlayer(b, e.getEntity().getKiller());
					main.getBountyHandler().removeBounty(b, true);
					return;
				}
			}
		}
	}
}
