package main.java.com.mrunknown404.serdana.entities.util;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Entity;

import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DataConverterRegistry;
import net.minecraft.server.v1_13_R2.DataConverterTypes;
import net.minecraft.server.v1_13_R2.EntityTypes;

@SuppressWarnings("unchecked")
public class EntityHandler {
	public EntityHandler() {
		for (EnumCustomEntities ent : EnumCustomEntities.values()) {
			ent.register(injectNewEntity(ent));
		}
	}
	
	public Entity spawnEntity(EnumCustomEntities type, Location loc) {
		return type.getType().a(((CraftWorld) loc.getWorld()).getHandle(), null, null, null, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()),
				true, false).getBukkitEntity();
	}
	
	private EntityTypes<?> injectNewEntity(EnumCustomEntities ent) {
		Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
		dataTypes.put("minecraft:" + ent.name().toLowerCase(), dataTypes.get("minecraft:" + ent.getVanillaName()));
		
		return EntityTypes.a(ent.name().toLowerCase(), EntityTypes.a.a(ent.getClazz(), ent.getFunction()));
	}
}
