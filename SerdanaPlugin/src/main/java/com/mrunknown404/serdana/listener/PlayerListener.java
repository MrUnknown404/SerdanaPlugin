package main.java.com.mrunknown404.serdana.listener;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.infos.BountyInfo;

public class PlayerListener implements Listener {
	private final Main main;
	
	public PlayerListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			EntityType finalType = null;
			for (EntityType type : EntityType.values()) {
				if (type.toString().contains(e.getItem().getType().toString().replace("SPAWN_EGG", ""))) {
					finalType = type;
					break;
				}
			}
			
			if (finalType != null) {
				Entity ent = e.getPlayer().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0, 1, 0), finalType);
				
				main.getEntityhandler().setupEntity((LivingEntity) ent, main.getTierHandler().getItemsTier(e.getItem()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDropItem(PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() == Material.NETHER_WART) {
			NBTItem n = new NBTItem(e.getItemDrop().getItemStack());
			
			if (n.hasKey("isParasite")) {
				e.setCancelled(true);
			}
		}
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
		
		if (main.getCommandTimer().getPlayers().containsKey(e.getPlayer().getUniqueId())) {
			main.getCommandTimer().getPlayers().remove(e.getPlayer().getUniqueId());
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
