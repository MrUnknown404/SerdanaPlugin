package main.java.com.mrunknown404.serdana.handlers;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.tr7zw.itemnbtapi.NBTEntity;
import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.mobs.MobData;
import main.java.com.mrunknown404.serdana.util.Nullable;
import main.java.com.mrunknown404.serdana.util.math.MathHelper;

public class EntityHandler {
	
	private final Main main;
	
	//private List<MobData> entityData = new ArrayList<MobData>(); //save this data?
	
	public EntityHandler(Main main) {
		this.main = main;
	}
	
	/** Setup the provided entity using the provided tier & health values */
	public void setupEntity(LivingEntity entity, int tier, float newMaxHealth, @Nullable MobData data) {
		double newMaxHP = MathHelper.clamp(newMaxHealth, 1, 2000);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHP);;
		entity.setHealth(newMaxHP);
		
		/* setup MobData
		if (data == null) {
			entityData.add(new MobDataNormal(entity.getUniqueId()));
		} else {
			entityData.add(data);
		}*/
	}
	
	/** Setup the provided entity using the provided tier */
	public void setupEntity(LivingEntity entity, int tier) {
		if (tier == -1) {
			tier = 0;
		}
		
		float multi = 1;
		if (tier != 0) {
			multi += tier / 10f;
		}
		
		setupEntity(entity, tier, 20f * multi, null);
	}
	
	/** Setup the provided entity using the highest tier */
	public void setupEntity(LivingEntity entity) {
		int tier = main.getTierHandler().getHighestTierFromAllPlayers();
		float multi = 1;
		if (tier != 0) {
			multi += tier / 10f;
		}
		
		setupEntity(entity, tier, 20f * multi, null);
	}
	
	public int getEntitiesTier(Entity entity) {
		NBTEntity n = new NBTEntity(entity);
		
		if (n.hasKey("tier")) {
			return n.getInteger("tier");
		}
		
		return -1;
	}
	
	public boolean isEntitySetup(Entity entity) {
		if (new NBTEntity(entity).hasKey("tier")) {
			return true;
		}
		
		return false;
	}
}
