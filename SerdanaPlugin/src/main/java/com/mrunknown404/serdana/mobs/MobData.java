package main.java.com.mrunknown404.serdana.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import main.java.com.mrunknown404.serdana.mobs.abilities.Ability;

public abstract class MobData {

	private final UUID id;
	private int minAbilityUseTime, maxAbilityUseTime;
	private boolean isBoss = false;
	
	private List<Ability> abilities = new ArrayList<>();
	
	public MobData(UUID id, int minAbilityUseTime, int maxAbilityUseTime, boolean isBoss, List<Ability> abilities) {
		this.id = id;
		this.minAbilityUseTime = minAbilityUseTime;
		this.maxAbilityUseTime = maxAbilityUseTime;
		this.isBoss = isBoss;
		this.abilities = abilities;
	}
	
	public MobData(UUID id) {
		this.id = id;
	}
	
	public void doAbilities() {
		if (!abilities.isEmpty()) {
			for (Ability ab : abilities) {
				ab.doAbility();
			}
		}
	}
	
	public UUID getUUID() {
		return id;
	}
	
	public int getMinAbilityUseTime() {
		return minAbilityUseTime;
	}
	
	public int getMaxAbilityUseTime() {
		return maxAbilityUseTime;
	}
	
	public boolean isBoss() {
		return isBoss;
	}
	
	public List<Ability> getAbilities() {
		return abilities;
	}
}
