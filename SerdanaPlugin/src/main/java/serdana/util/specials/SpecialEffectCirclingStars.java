package main.java.serdana.util.specials;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import main.java.serdana.util.infos.SpecialPlayerInfo;

public class SpecialEffectCirclingStars implements ISpecialEffectBase {
	
	private final int maxPos = 11, maxTick = 2;
	private Map<UUID, Integer> pos = new HashMap<UUID, Integer>(), tick = new HashMap<UUID, Integer>();
	
	@Override
	public void start(SpecialPlayerInfo info) {
		pos.put(info.getID(), 0);
		tick.put(info.getID(), 0);
	}
	
	@Override
	public void tick(SpecialPlayerInfo info) {
		Player p = Bukkit.getPlayer(info.getID());
		World w = p.getWorld();
		
		if (tick.get(info.getID()) == 0) {
			tick.put(info.getID(), maxTick);
			
			if (pos.get(info.getID()) == 0) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.5, p.getLocation().getY() + 2, p.getLocation().getZ(), 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.5, p.getLocation().getY() + 2, p.getLocation().getZ(), 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 1) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.483, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.1294, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.483, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.1294, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 2) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.433, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.25, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.433, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.25, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 3) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.354, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.354, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.354, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.354, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 4) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.25, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.433, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.25, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.433, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 5) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.1294, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.483, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.1294, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.483, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 6) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ() - 0.5, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ() + 0.5, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 7) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.1294, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.483, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.1294, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.483, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 8) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.25, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.433, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.25, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.433, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 9) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.354, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.354, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.354, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.354, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 10) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.433, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.25, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.433, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.25, 0, 0, 0, 0, 1);
			} else if (pos.get(info.getID()) == 11) {
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() + 0.483, p.getLocation().getY() + 2, p.getLocation().getZ() - 0.1294, 0, 0, 0, 0, 1);
				w.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().getX() - 0.483, p.getLocation().getY() + 2, p.getLocation().getZ() + 0.1294, 0, 0, 0, 0, 1);
			}
			
			if (pos.get(info.getID()) != maxPos) {
				pos.put(info.getID(), pos.get(info.getID()) + 1);
			} else {
				pos.put(info.getID(), 0);
			}
		} else {
			tick.put(info.getID(), tick.get(info.getID()) - 1);
		}
	}
}
