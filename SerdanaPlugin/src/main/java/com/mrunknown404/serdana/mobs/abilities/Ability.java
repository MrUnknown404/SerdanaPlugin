package main.java.com.mrunknown404.serdana.mobs.abilities;

public abstract class Ability {

	private int cooldown, maxCooldown;
	
	public Ability(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}
	
	public void doAbility() {
		cooldown = maxCooldown;
	}
	
	protected boolean canDoAbility() {
		if (cooldown > 0) {
			cooldown--;
			return false;
		}
		
		return true;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public int getMaxCooldown() {
		return maxCooldown;
	}
}
