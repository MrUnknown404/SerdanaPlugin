package main.java.com.mrunknown404.serdana.entities;

import main.java.com.mrunknown404.serdana.entities.util.EntityMonsterBase;
import main.java.com.mrunknown404.serdana.entities.util.EnumCustomEntities;
import net.minecraft.server.v1_13_R2.World;

public class EntityDraglet extends EntityMonsterBase {

	public EntityDraglet(World world) {
		super(world, EnumCustomEntities.DRAGLET, 10, 20, 30);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
	}
}