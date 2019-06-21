package main.java.serdana.entities;

import main.java.serdana.entities.util.EntityMonsterBase;
import main.java.serdana.entities.util.EnumCustomEntities;
import net.minecraft.server.v1_13_R2.World;

public class EntitySwamper extends EntityMonsterBase {

	public EntitySwamper(World world) {
		super(world, EnumCustomEntities.SWAMPER, 1, 10, 20, 30);
	}
	
	@Override
	protected void setup() {
		
	}
}
