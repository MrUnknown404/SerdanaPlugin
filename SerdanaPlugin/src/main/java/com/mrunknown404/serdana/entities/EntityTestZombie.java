package main.java.com.mrunknown404.serdana.entities;

import main.java.com.mrunknown404.serdana.entities.abilities.AbilityJump;
import main.java.com.mrunknown404.serdana.entities.util.EntityMonsterBase;
import main.java.com.mrunknown404.serdana.entities.util.EnumCustomEntities;
import net.minecraft.server.v1_13_R2.World;

public class EntityTestZombie extends EntityMonsterBase {

	public EntityTestZombie(World world) {
		super(world, EnumCustomEntities.TEST_ZOMBIE, 1, 10, 20, 30);
	}
	
	@Override
	protected void setup() {
		setMaxHealth(100);
		setMoveSpeed(0.7);
		setAttackDamage(2);
		setFollowRange(32);
		setKnockbackResistance(0);
		setArmor(0);
		setArmorToughness(0);
		
		abilities.add(10, new AbilityJump(this));
		abilities.add(50, new AbilityJump(this));
	}
}
