package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.handlers.ShopkeeperHandler;
import main.java.com.mrunknown404.serdana.quests.QuestHandler;

public class ShopkeeperListener implements Listener {
	private final Main main;
	
	public ShopkeeperListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onShopTrade(ShopkeeperTradeEvent e) {
		if (main.isComponentEnabled(Main.Components.CustomShops)) {
			main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.trade);
		}
	}
	
	@EventHandler
	public void onShopOpen(ShopkeeperOpenUIEvent e) {
		if (main.isComponentEnabled(Main.Components.CustomShops)) {
			if (main.isComponentEnabled(Main.Components.Quests)) {
				if (main.getQuestHandler().getQuestShopTalkType(e.getPlayer(), e.getShopkeeper()) != QuestHandler.QuestTalkType.none) {
					boolean b = main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(),
							main.getQuestHandler().getQuestShopTalkType(e.getPlayer(), e.getShopkeeper()));
					
					e.setCancelled(b);
					
					if (b) {
						return;
					}
				}
			}
			
			if (main.isComponentEnabled(Main.Components.BannedItems)) {
				if (main.getBannedItemHandler().doesPlayerHaveBannedItems(e.getPlayer())) {
					e.setCancelled(main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.banned));
					return;
				}
			}
			
			main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.open);
		}
	}
}
