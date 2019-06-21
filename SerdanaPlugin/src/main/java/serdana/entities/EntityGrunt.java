package main.java.serdana.entities;

import main.java.serdana.entities.util.EntityMonsterBase;
import main.java.serdana.entities.util.EnumCustomEntities;
import net.minecraft.server.v1_13_R2.World;

public class EntityGrunt extends EntityMonsterBase {

	public EntityGrunt(World world) {
		super(world, EnumCustomEntities.GRUNT, 1, 10, 20, 30);
	}
	
	@Override
	protected void setup() {
		
	}
}
