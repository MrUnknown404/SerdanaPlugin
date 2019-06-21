package main.java.serdana.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class WorldListener implements Listener {

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		for (Entity ent : e.getChunk().getEntities()) {
			if (ent instanceof Monster) {
				ent.remove();
			}
		}
	}
}
