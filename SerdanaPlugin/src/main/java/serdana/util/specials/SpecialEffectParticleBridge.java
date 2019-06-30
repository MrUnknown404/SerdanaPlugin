package main.java.serdana.util.specials;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import main.java.serdana.util.infos.SpecialPlayerInfo;

public class SpecialEffectParticleBridge implements ISpecialEffectBase {
	
	@Override public void start(SpecialPlayerInfo info) {}
	
	@Override
	public void tick(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		World w = p.getWorld();
		
		w.spawnParticle(Particle.ENCHANTMENT_TABLE, p.getLocation().getX(), p.getLocation().getY() - 0.07, p.getLocation().getZ(), 50, 0.25, 0, 0.25, 0);
	}
}
