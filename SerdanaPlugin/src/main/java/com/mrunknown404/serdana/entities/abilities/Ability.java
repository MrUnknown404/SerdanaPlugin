package main.java.com.mrunknown404.serdana.entities.abilities;

import main.java.com.mrunknown404.serdana.entities.util.EntityMonsterBase;

public abstract class Ability {
	
	protected final EntityMonsterBase user;
	
	public Ability(EntityMonsterBase user) {
		this.user = user;
	}
	
	public abstract void perform();
}
