package main.java.com.mrunknown404.serdana.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import main.java.com.mrunknown404.serdana.Main;

public class WorldListener implements Listener {

	private final Main main;
	
	public WorldListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		//for (Entity ent : e.getChunk().getEntities()) {
		//	if (!(ent instanceof Player || ent instanceof Shopkeeper)) {
		//		ent.remove();
		//	}
		//}
	}
}
