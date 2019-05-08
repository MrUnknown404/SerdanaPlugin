package main.java.com.mrunknown404.serdana.entities.util;

import java.util.Map;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Entity;

import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DataConverterRegistry;
import net.minecraft.server.v1_13_R2.DataConverterTypes;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

@SuppressWarnings("unchecked")
public class EntityHandler {
	//private Map<UUID, EntityTypes> entities = new HashMap<UUID, EntityTypes>();
	
	public EntityHandler() {
		for (EnumCustomEntities ent : EnumCustomEntities.values()) {
			ent.register(injectNewEntity(ent.name().toLowerCase(), ent.toString().toLowerCase(), ent.getClazz(), ent.getFunction()));
		}
	}
	
	public Entity spawnEntity(EntityTypes<?> entityTypes, Location loc) {
		net.minecraft.server.v1_13_R2.Entity nmsEntity = entityTypes.a(((CraftWorld) loc.getWorld()).getHandle(), null, null, null,
				new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), true, false);
		
		//entities.put(nmsEntity.getUniqueID(), entityTypes);
		
		return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
	}
	
	static EntityTypes<?> injectNewEntity(String name, String extend_from, Class<? extends net.minecraft.server.v1_13_R2.Entity> clazz, Function<? super World, ? extends net.minecraft.server.v1_13_R2.Entity> function) {
		Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
		dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
		
		return EntityTypes.a(name, EntityTypes.a.a(clazz, function));
	}
}
