package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.shopkeepers.api.events.ShopkeeperOpenUIEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.handlers.ShopkeeperHandler;

public class ShopkeeperListener implements Listener {
	private final Main main;
	
	public ShopkeeperListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onShopTrade(ShopkeeperTradeEvent e) {
		if (main.getComponent(Main.Components.CustomShops)) {
			main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.trade);
		}
	}
	
	@EventHandler
	public void onShopOpen(ShopkeeperOpenUIEvent e) {
		if (main.getComponent(Main.Components.CustomShops)) {
			if (main.getBannedItemHandler().doesPlayerHaveBannedItems(e.getPlayer())) {
				e.setCancelled(main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.banned));
			} else {
				main.getShopHandler().talk(e.getShopkeeper(), e.getPlayer(), ShopkeeperHandler.TalkType.open);
			}
		}
	}
}
