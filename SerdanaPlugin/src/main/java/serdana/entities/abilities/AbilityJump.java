package main.java.serdana.entities.abilities;

import main.java.serdana.entities.util.EntityMonsterBase;

public class AbilityJump extends Ability {
	
	public AbilityJump(EntityMonsterBase user) {
		super(user);
	}
	
	@Override
	public void perform() {
		user.motY += 1;
	}
}
