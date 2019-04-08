package main.java.com.mrunknown404.serdana.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class TargetHelper {
	public static List<LivingEntity> getLivingTargets(LivingEntity source, double range) {
		List<Entity> list = source.getNearbyEntities(range, range, range);
		List<LivingEntity> targets = new ArrayList<LivingEntity>();
		
		Vector facing = source.getLocation().getDirection();
		double fLengthSq = facing.lengthSquared();
		
		for (Entity entity : list) {
			Vector facing1 = source.getLocation().getDirection();
			Vector relative1 = entity.getLocation().subtract(entity.getLocation()).toVector();
			
			if (!(facing1.dot(relative1) >= 0) || !(entity instanceof LivingEntity)) {
				continue;
			}
			
			Vector relative = entity.getLocation().subtract(source.getLocation()).toVector();
			double dot = relative.dot(facing);
			double rLengthSq = relative.lengthSquared();
			double cosSquared = (dot * dot) / (rLengthSq * fLengthSq);
			double sinSquared = 1 - cosSquared;
			double dSquared = rLengthSq * sinSquared;
			
			if (dSquared < 4) {
				targets.add((LivingEntity) entity);
			}
		}
		
		return targets;
	}
	
	public static Block getTarget(Location from, int distance, Set<Byte> transparentTypeIds) {
		BlockIterator itr = new BlockIterator(from, 0, distance);
		while (itr.hasNext()) {
			Block block = itr.next();
			
			@SuppressWarnings("deprecation")
			int id = block.getType().getId();
			
			if (transparentTypeIds == null) {
				if (id == 0) {
					continue;
				}
			} else if (transparentTypeIds.contains((byte) id)) {
				continue;
			}
			return block;
		}
		return null;
	}
}
