package main.java.serdana.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.Main;
import main.java.serdana.handlers.SpecialPlayerHandler.SpecialPlayerEffect;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.infos.BountyInfo;

public class PlayerListener implements Listener {
	
	private final Main main;
	
	public PlayerListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (main.isComponentEnabled(Main.Components.AChat)) {
			if (main.getAChatHandler().isPlayerEnabled(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				Bukkit.getConsoleSender().sendMessage(ColorHelper.addColor("&4[AC] " + e.getPlayer().getDisplayName() + "&f: " + e.getMessage()));
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission("serdana.adminChat")) {
						p.sendMessage(ColorHelper.addColor("&4[AC] " + e.getPlayer().getDisplayName() + "&f: " + e.getMessage()));
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDropItem(PlayerDropItemEvent e) {
		if (main.isComponentEnabled(Main.Components.Parasite)) {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if (e.getItemDrop().getItemStack().getType() == Material.NETHER_WART) {
					NBTItem n = new NBTItem(e.getItemDrop().getItemStack());
					
					if (n.hasKey("isParasite")) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		if (main.isComponentEnabled(Main.Components.Misc)) {
			if (!e.getPlayer().hasPlayedBefore()) {
				e.getPlayer().setDisplayName(ColorHelper.addColor("&7" + e.getPlayer().getName()));
			}
			
			if (main.getSpecialPlayerHandler().getSpecialInfo(e.getPlayer().getUniqueId()) != null) {
				main.getSpecialPlayerHandler().setupPlayer(e.getPlayer().getUniqueId());
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Quests)) {
			main.getQuestHandler().setupPlayer(e.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent e) {
		if (main.isComponentEnabled(Main.Components.Parties)) {
			if (main.getPartyHandler().isPlayerInAnyParty(e.getPlayer().getUniqueId()) ) {
				main.getPartyHandler().leaveParty(e.getPlayer().getUniqueId());
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Quests)) {
			if ((main.getQuestHandler().isPlayerSetup(e.getPlayer()))) {
				main.getQuestHandler().closePlayer(e.getPlayer());
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Misc)) {
			if (main.getCommandTimer().getPlayers().containsKey(e.getPlayer().getUniqueId())) {
				main.getCommandTimer().getPlayers().remove(e.getPlayer().getUniqueId());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (main.isComponentEnabled(Main.Components.Parties)) {
			if (main.getPartyHandler().isPlayerInAnyParty(e.getEntity().getUniqueId()) ) {
				main.getPartyHandler().notifyDeath(e.getEntity().getUniqueId());
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Misc)) {
			if (main.getCommandTimer().getPlayers().containsKey(e.getEntity().getUniqueId())) {
				main.getCommandTimer().getPlayers().remove(e.getEntity().getUniqueId());
			}
		}
		
		if (main.isComponentEnabled(Main.Components.Bounties)) {
			if (e.getEntity().getKiller() instanceof Player) {
				List<BountyInfo> bounties = main.getBountyHandler().getBounties();
				
				for (int i = 0; i < bounties.size(); i++) {
					BountyInfo b = bounties.get(i);
					if (e.getEntity().getUniqueId().equals(b.getToKillUUID())) {
						main.getBountyHandler().rewardPlayer(b, e.getEntity().getKiller());
						main.getBountyHandler().removeBounty(b, false);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (main.isComponentEnabled(Main.Components.Misc)) {
			if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
				if (main.getSpecialPlayerHandler().isPlayerEnabled(e.getPlayer().getUniqueId()) && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
					if (main.getSpecialPlayerHandler().getSpecialInfo(e.getPlayer().getUniqueId()).getEffect() == SpecialPlayerEffect.antenna) {
						main.getSpecialPlayerHandler().doAntenna(main.getSpecialPlayerHandler().getSpecialInfo(e.getPlayer().getUniqueId()));
					}
				}
			}
		}
	}
}
