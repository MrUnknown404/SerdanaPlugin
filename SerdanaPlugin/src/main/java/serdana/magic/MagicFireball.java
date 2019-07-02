package main.java.serdana.magic;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import main.java.serdana.magic.util.IMagicBase;

public class MagicFireball implements IMagicBase {

	@Override
	public void use(Player p) {
		Fireball e = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().toVector().add(p.getLocation().getDirection().multiply(2)).toLocation(
				p.getWorld(), p.getLocation().getYaw(), p.getLocation().getPitch()), EntityType.FIREBALL);
		
		e.setInvulnerable(true);
		e.setShooter(p);
		e.setYield(2.22f);
	}
}
