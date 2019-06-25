package main.java.serdana.util.infos;

import java.util.UUID;

import main.java.serdana.handlers.SpecialPlayerHandler.SpecialPlayerEffect;

public class SpecialPlayerInfo {

	private UUID ID;
	private boolean enabled;
	private SpecialPlayerEffect effect;
	
	public SpecialPlayerInfo(UUID ID, boolean enabled, SpecialPlayerEffect effect) {
		this.ID = ID;
		this.enabled = enabled;
		this.effect = effect;
	}
	
	public void toggle() {
		enabled = !enabled;
	}
	
	public void setEffect(SpecialPlayerEffect effect) {
		this.effect = effect;
	}
	
	public UUID getID() {
		return ID;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public SpecialPlayerEffect getEffect() {
		return effect;
	}
}
