package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.util.enums.EnumQuestTalkType;
import main.java.com.mrunknown404.serdana.util.enums.EnumTalkType;

public class NPCListener implements Listener {
	private final Main main;
	
	public NPCListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onShopTrade(ShopkeeperTradeEvent e) {
		if (main.isComponentEnabled(Main.Components.CustomNPCs)) {
			main.getNPCHandler().talk(e.getShopkeeper(), e.getPlayer(), EnumTalkType.trade);
		}
	}
	
	@EventHandler
	public void onShopOpen(ShopkeeperOpenUIEvent e) {
		if (main.isComponentEnabled(Main.Components.CustomNPCs)) {
			if (main.isComponentEnabled(Main.Components.Quests)) {
				if (main.getQuestHandler().getQuestShopTalkType(e.getPlayer(), e.getShopkeeper()) != EnumQuestTalkType.none) {
					boolean b = main.getNPCHandler().talk(e.getShopkeeper(), e.getPlayer(),
							main.getQuestHandler().getQuestShopTalkType(e.getPlayer(), e.getShopkeeper()));
					
					e.setCancelled(b);
					
					if (b) {
						return;
					}
				}
			}
			
			if (main.isComponentEnabled(Main.Components.BannedItems)) {
				if (main.getBannedItemHandler().doesPlayerHaveBannedItems(e.getPlayer())) {
					e.setCancelled(main.getNPCHandler().talk(e.getShopkeeper(), e.getPlayer(), EnumTalkType.banned));
					return;
				}
			}
			
			e.setCancelled(main.getNPCHandler().talk(e.getShopkeeper(), e.getPlayer(), EnumTalkType.open));
		}
	}
}
