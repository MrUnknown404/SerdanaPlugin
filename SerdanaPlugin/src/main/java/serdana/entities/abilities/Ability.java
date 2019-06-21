package main.java.serdana.entities.abilities;

import main.java.serdana.entities.util.EntityMonsterBase;

public abstract class Ability {
	
	protected final EntityMonsterBase user;
	
	public Ability(EntityMonsterBase user) {
		this.user = user;
	}
	
	public abstract void perform();
}
