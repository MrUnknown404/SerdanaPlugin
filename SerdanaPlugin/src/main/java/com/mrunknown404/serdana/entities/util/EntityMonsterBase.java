package main.java.com.mrunknown404.serdana.entities.util;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Random;

import main.java.com.mrunknown404.serdana.entities.abilities.Ability;
import main.java.com.mrunknown404.serdana.util.RandomCollection;
import net.minecraft.server.v1_13_R2.EntityMonster;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_13_R2.PathfinderGoalTarget;
import net.minecraft.server.v1_13_R2.World;

public abstract class EntityMonsterBase extends EntityMonster {

	protected final int abilityChance, abilityMinTime, abilityMaxTime;
	protected int abilityTime = 0;
	
	protected RandomCollection<Ability> abilities = new RandomCollection<Ability>();
	
	/**
	 * @param world
	 * @param customEntityType
	 * @param abilityChance
	 * @param abilityMinTime
	 * @param abilityMaxTime
	 */
	public EntityMonsterBase(World world, EnumCustomEntities customEntityType, int abilityChance, int abilityMinTime, int abilityMaxTime) {
		super(customEntityType.getVanillaType(), world);
		this.abilityChance = abilityChance;
		this.abilityMinTime = abilityMinTime;
		this.abilityMaxTime = abilityMaxTime;
		
		setCustomName(ChatSerializer.a("\"" + customEntityType.getName() + "\""));
		getAttributeMap().a().clear();
		
		canPickUpLoot = false;
		
		((LinkedHashSet<?>) getPrivateField("b", PathfinderGoalSelector.class, goalSelector)).clear();
		((LinkedHashSet<?>) getPrivateField("c", PathfinderGoalSelector.class, goalSelector)).clear();
		((LinkedHashSet<?>) getPrivateField("b", PathfinderGoalSelector.class, targetSelector)).clear();
		((LinkedHashSet<?>) getPrivateField("c", PathfinderGoalSelector.class, targetSelector)).clear();
		
		setup();
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!abilities.isEmpty()) {
			attemptAbilityUse();
		}
	}
	
	/** Returns a random weighted ability
	 * @return A random weighted ability
	 */
	protected Ability getRandomAbility() {
		return abilities.getRandom();
	}
	
	/** Sets up AI/Abilities/Variables */
	protected abstract void setup();
	
	/** Uses an ability */
	protected void onAbilityUse() {
		getRandomAbility().perform();
	}
	
	/** Attempts to use an ability */
	private void attemptAbilityUse() {
		if (abilityTime == abilityMaxTime) {
			onAbilityUse();
			abilityTime = 0;
		} else if (abilityTime > abilityMinTime) {
			if (new Random().nextInt(abilityChance) == 0) {
				onAbilityUse();
				abilityTime = 0;
			} else {
				abilityTime++;
			}
		} else {
			abilityTime++;
		}
	}
	
	/** Adds the given {@link PathfinderGoal}
	 * @param goal PathfinderGoal to add
	 */
	protected void addPathfinder(PathfinderGoal goal) {
		goalSelector.a(0, goal); //no idea what the number means EDIT: after looking at the source code i still have no idea
	}
	
	/** Adds the given {@link PathfinderGoalTarget}
	 * @param target PathfinderGoalTarget to add
	 */
	protected void addPathfinderTarget(PathfinderGoalTarget target) {
		targetSelector.a(0, target); //no idea what the number means EDIT: after looking at the source code i still have no idea
	}
	
	/** Sets the {@link EntityMonsterBase}'s move speed
	 * @param value Value to set to
	 */
	protected void setMoveSpeed(double value) {
		getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s max health
	 * @param value Value to set to
	 */
	protected void setMaxHealth(double value) {
		getAttributeMap().b(GenericAttributes.maxHealth);
		getAttributeInstance(GenericAttributes.maxHealth).setValue(value);
		setHealth(100);
	}
	
	/** Sets the {@link EntityMonsterBase}'s attack damage
	 * @param value Value to set to
	 */
	protected void setAttackDamage(double value) {
		getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s follow range
	 * @param value Value to set to
	 */
	protected void setFollowRange(double value) {
		getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s knockback resistance
	 * @param value Value to set to
	 */
	protected void setKnockbackResistance(double value) {
		getAttributeMap().b(GenericAttributes.c);
		getAttributeInstance(GenericAttributes.c).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s fly speed
	 * @param value Value to set to
	 */
	protected void setFlySpeed(double value) {
		getAttributeMap().b(GenericAttributes.e);
		getAttributeInstance(GenericAttributes.e).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s attack speed
	 * @param value Value to set to
	 */
	protected void setAttackSpeed(double value) {
		getAttributeMap().b(GenericAttributes.g);
		getAttributeInstance(GenericAttributes.g).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s armor
	 * @param value Value to set to
	 */
	protected void setArmor(double value) {
		getAttributeMap().b(GenericAttributes.h);
		getAttributeInstance(GenericAttributes.h).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s armor toughness
	 * @param value Value to set to
	 */
	protected void setArmorToughness(double value) {
		getAttributeMap().b(GenericAttributes.i);
		getAttributeInstance(GenericAttributes.i).setValue(value);
	}
	
	/** Sets the {@link EntityMonsterBase}'s luck
	 * @param value Value to set to
	 */
	protected void setLuck(double value) {
		getAttributeMap().b(GenericAttributes.j);
		getAttributeInstance(GenericAttributes.j).setValue(value);
	}
	
	private Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			return field.get(object);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
