package main.java.com.mrunknown404.serdana.entities;

import net.minecraft.server.v1_13_R2.DifficultyDamageScaler;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.GroupDataEntity;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.World;

public class EntityTestZombie extends EntityZombie {

	public EntityTestZombie(World world) {
		super(world);
	}
	
	@Override
	public void tick() {
		super.tick();
		System.out.println("Zombie Tick");
	}
	
	@Override
	public GroupDataEntity prepare(DifficultyDamageScaler scale, GroupDataEntity gde, NBTTagCompound nbt) {
		gde = super.prepare(scale, gde, nbt);
		
		return gde;
	}
	
	@Override
	protected void initAttributes() {
		super.initAttributes();
		
	}
}
