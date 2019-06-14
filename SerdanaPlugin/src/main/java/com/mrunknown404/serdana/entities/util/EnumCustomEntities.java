package main.java.com.mrunknown404.serdana.entities.util;

import java.util.function.Function;

import main.java.com.mrunknown404.serdana.entities.EntityDraglet;
import main.java.com.mrunknown404.serdana.entities.EntityGrunt;
import main.java.com.mrunknown404.serdana.entities.EntitySwamper;
import main.java.com.mrunknown404.serdana.entities.EntityTestZombie;
import main.java.com.mrunknown404.serdana.entities.EntityUndead;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

public enum EnumCustomEntities {
	TEST_ZOMBIE(EntityTypes.ZOMBIE, "Test Zombie", EntityTestZombie.class, EntityTestZombie::new),
	GRUNT(EntityTypes.ZOMBIE_PIGMAN, "Grunt", EntityGrunt.class, EntityGrunt::new),
	DRAGLET(EntityTypes.PHANTOM, "Draglet", EntityDraglet.class, EntityDraglet::new),
	SWAMPER(EntityTypes.DROWNED, "Swamper", EntitySwamper.class, EntitySwamper::new),
	UNDEAD(EntityTypes.ZOMBIE, "Undead", EntityUndead.class, EntityUndead::new);
	
	private EntityTypes<?> type, vanillaType;
	private Class<? extends EntityMonsterBase> clazz;
	private Function<? super World, ? extends Entity> function;
	private String name;
	
	private EnumCustomEntities(EntityTypes<?> vanillaType, String name, Class<? extends EntityMonsterBase> clazz, Function<? super World, ? extends Entity> function) {
		this.vanillaType = vanillaType;
		this.clazz = clazz;
		this.function = function;
		this.name = name;
	}
	
	public void register(EntityTypes<?> type) {
		this.type = type;
	}
	
	public EntityTypes<?> getType() {
		return type;
	}
	
	public EntityTypes<?> getVanillaType() {
		return vanillaType;
	}
	
	public String getVanillaName() {
		return EntityTypes.getName(vanillaType).toString().replaceAll("minecraft:", "");
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends Entity> getClazz() {
		return clazz;
	}
	
	public Function<? super World, ? extends Entity> getFunction() {
		return function;
	}
}
