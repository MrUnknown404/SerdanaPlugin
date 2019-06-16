package main.java.com.mrunknown404.serdana.entities.abilities;

import main.java.com.mrunknown404.serdana.entities.util.EntityMonsterBase;

public class AbilityJump extends Ability {
	
	public AbilityJump(EntityMonsterBase user) {
		super(user);
	}
	
	@Override
	public void perform() {
		user.motY += 1;
	}
}
