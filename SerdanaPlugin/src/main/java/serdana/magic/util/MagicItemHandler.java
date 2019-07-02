package main.java.serdana.magic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import de.tr7zw.itemnbtapi.NBTItem;
import main.java.serdana.Main;
import main.java.serdana.magic.MagicFireball;
import main.java.serdana.util.ColorHelper;
import main.java.serdana.util.IReloadable;

public class MagicItemHandler implements IReloadable {
	
	private final Main main;
	
	private Map<String, Map<Location, BoundingBox>> mainBoxes = new HashMap<String, Map<Location, BoundingBox>>();
	private Map<String, Map<Location, BoundingBox>> boxes = new HashMap<String, Map<Location, BoundingBox>>();
	private Map<String, Boolean> isCharging = new HashMap<String, Boolean>();
	private Map<String, MagicItems> charging = new HashMap<String, MagicItems>();
	
	public MagicItemHandler(Main main) {
		this.main = main;
		
		for (MagicShrineInfo info : main.getRandomConfig().getMagicShrines()) {
			charging.put(info.getName(), null);
			isCharging.put(info.getName(), false);
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				for (MagicShrineInfo info : main.getRandomConfig().getMagicShrines()) {
					if (charging.get(info.getName()) == null) {
						Iterator<Entry<Location, BoundingBox>> it = mainBoxes.get(info.getName()).entrySet().iterator();
						
						MagicItems magic = null;
						
						while (it.hasNext()) {
							Entry<Location, BoundingBox> pair = it.next();
							
							for (Entity e : pair.getKey().getWorld().getNearbyEntities(pair.getValue())) {
								if (e instanceof Item && getItemsMagic(((Item) e).getItemStack()) != null) {
									if (!isCharging.get(info.getName())) {
										isCharging.put(info.getName(), true);
										setupRecharging(info);
									}
									
									magic = getItemsMagic(((Item) e).getItemStack());
									break;
								}
							}
							
							if (magic == null && isCharging.get(info.getName())) {
								isCharging.put(info.getName(), false);
								stopRecharging(info);
							}
						}
						
						if (magic != null) {
							it = boxes.get(info.getName()).entrySet().iterator();
							Map<Material, Boolean> found = new HashMap<Material, Boolean>();
								
							for (Material mat : magic.getItems()) {
								found.put(mat, false);
							}
							
							while (it.hasNext()) {
								Entry<Location, BoundingBox> pair = it.next();
								
								for (Entity e : pair.getKey().getWorld().getNearbyEntities(pair.getValue())) {
									if (e instanceof Item) {
										if (magic.getItems().contains(((Item) e).getItemStack().getType())) {
											found.put(((Item) e).getItemStack().getType(), true);
										}
									}
								}
								
								boolean foundAll = false;
								if (!found.isEmpty()) {
									for (Material mat : magic.getItems()) {
										if (found.get(mat)) {
											foundAll = true;
										} else {
											foundAll = false;
											break;
										}
									}
								}
								
								if (foundAll && charging.get(info.getName()) == null) {
									charging.put(info.getName(), magic);
									startRecharging(magic, info);
								}
							}
						}
					} else { //-----------------------------------------------------------------------------------------------------------------//
						Iterator<Entry<Location, BoundingBox>> it = boxes.get(info.getName()).entrySet().iterator();
						
						boolean ran = false;
						
						firstLoop:
						while (it.hasNext()) {
							Entry<Location, BoundingBox> pair = it.next();
							
							for (Entity e : pair.getKey().getWorld().getNearbyEntities(pair.getValue())) {
								if (e instanceof Item && charging.get(info.getName()).getItems().contains(((Item) e).getItemStack().getType())) {
									World w = e.getWorld();
									w.playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 1, 1);
									w.spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 10, 2, 2, 2, 0);
									w.spawnParticle(Particle.FLAME, e.getLocation(), 20, 1, 1, 1, 0);
									w.spawnParticle(Particle.VILLAGER_HAPPY, e.getLocation(), 20, 1, 1, 1, 0);
									e.remove();
									ran = true;
									break firstLoop;
								}
							}
						}
						
						if (!ran) {
							charging.put(info.getName(), null);
							
							World w = info.getMainPillar().getWorld();
							
							w.playSound(info.getMainPillar(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 1, 2);
							w.spawnParticle(Particle.EXPLOSION_LARGE, info.getMainPillar(), 10, 2, 2, 2, 0);
							w.spawnParticle(Particle.VILLAGER_HAPPY, info.getMainPillar(), 30, 1, 1, 1, 0);
							w.spawnParticle(Particle.ENCHANTMENT_TABLE, info.getMainPillar(), 50, 1, 1, 1, 2);
							w.spawnParticle(Particle.DRAGON_BREATH, info.getMainPillar(), 50, 1, 1, 1, 2);
							break;
						}
					}
				}
			}
		}, 0L, 20L);
	}
	
	@Override
	public void reload() {
		for (MagicShrineInfo info : main.getRandomConfig().getMagicShrines()) {
			Map<Location, BoundingBox> map = new HashMap<Location, BoundingBox>();
			for (Location loc : info.getPillars()) {
				map.put(loc, new BoundingBox(loc.getX(), loc.getY() + 1, loc.getZ(), loc.getX() + 1, loc.getY() + 2, loc.getZ() + 1));
			}
			boxes.put(info.getName(), map);
			
			map = new HashMap<Location, BoundingBox>();
			Location loc = info.getMainPillar();
			map.put(loc, new BoundingBox(loc.getX(), loc.getY() + 1, loc.getZ(), loc.getX() + 1, loc.getY() + 2, loc.getZ() + 1));
			
			mainBoxes.put(info.getName(), map);
		}
	}
	
	private void setupRecharging(MagicShrineInfo info) {
		World w = info.getMainPillar().getWorld();
		
		w.getBlockAt(new Location(info.getMainPillar().getWorld(), info.getMainPillar().getX(), info.getMainPillar().getY() - 1, info.getMainPillar().getZ())).setType(Material.BEACON);
	}
	
	private void stopRecharging(MagicShrineInfo info) {
		World w = info.getMainPillar().getWorld();
		
		w.getBlockAt(new Location(info.getMainPillar().getWorld(), info.getMainPillar().getX(), info.getMainPillar().getY() - 1, info.getMainPillar().getZ())).setType(Material.AIR);
		w.playSound(info.getMainPillar(), Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.MASTER, 1, 1);
	}
	
	private void startRecharging(MagicItems magic, MagicShrineInfo info) {
		Iterator<Entry<Location, BoundingBox>> it = boxes.get(info.getName()).entrySet().iterator();
		while (it.hasNext()) {
			Entry<Location, BoundingBox> pair = it.next();
			
			for (Entity e : pair.getKey().getWorld().getNearbyEntities(pair.getValue())) {
				if (e instanceof Item && magic.getItems().contains(((Item) e).getItemStack().getType())) {
					((Item) e).setPickupDelay(Integer.MAX_VALUE);
				}
			}
		}
		
		it = mainBoxes.get(info.getName()).entrySet().iterator();
		while (it.hasNext()) {
			Entry<Location, BoundingBox> pair = it.next();
			
			for (Entity e : pair.getKey().getWorld().getNearbyEntities(pair.getValue())) {
				if (e instanceof Item && getItemsMagic(((Item) e).getItemStack()) != null) {
					((Item) e).setPickupDelay(magic.getItems().size() * 20); //TODO learn how many ticks!
					((Item) e).setItemStack(refillMagic(((Item) e).getItemStack()));
				}
			}
		}
	}
	
	public ItemStack addMagic(ItemStack item, MagicItems magic) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		
		lore.add(ColorHelper.addColor("&8Uses the following items for recharging :"));
		
		for (Material mat : magic.getItems()) {
			lore.add(ColorHelper.addColor("&c" + WordUtils.capitalizeFully(mat.toString().replaceAll("_", " "))));
		}
		
		lore.add("mana");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		NBTItem n = new NBTItem(item);
		n.setString("magic", magic.toString());
		n.setInteger("mana", magic.getMaxMana());
		
		return updateMagic(n.getItem());
	}
	
	public ItemStack updateMagic(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(lore.size() - 1, ColorHelper.addColor("&bMana &8: &b" + new NBTItem(item).getInteger("mana") + "&8/&c" + getItemsMagic(item).getMaxMana()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public ItemStack refillMagic(ItemStack item) {
		NBTItem n = new NBTItem(item);
		n.setInteger("mana", MagicItems.valueOf(n.getString("magic")).getMaxMana());
		
		return updateMagic(n.getItem());
	}
	
	public ItemStack useMagicItem(Player p, ItemStack item) {
		MagicItems magic = getItemsMagic(item);
		NBTItem n = new NBTItem(item);
		
		if (n.getInteger("mana") != 0) {
			try {
				magic.getClazz().newInstance().use(p);
				
				n.setInteger("mana", n.getInteger("mana") - 1);
				
				return updateMagic(n.getItem());
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		} else {
			p.sendMessage(ColorHelper.addColor("&cThis item is out of mana! You must recharge it at a recharging station!"));
		}
		
		return item;
	}
	
	public MagicItems getItemsMagic(ItemStack item) {
		NBTItem n = new NBTItem(item);
		
		if (!n.hasKey("magic")) {
			return null;
		}
		
		for (MagicItems magic : MagicItems.values()) {
			if (magic.toString().equals(n.getString("magic"))) {
				return magic;
			}
		}
		
		return null;
	}
	
	public enum MagicItems {
		fireball(MagicFireball.class, Arrays.asList(Material.BLAZE_ROD, Material.FIRE_CHARGE, Material.ENDER_PEARL, Material.LAVA_BUCKET), 32);
		
		private Class<? extends IMagicBase> clazz;
		private final List<Material> items;
		private final int maxMana;
		
		private MagicItems(Class<? extends IMagicBase> clazz, List<Material> items, int maxMana) {
			this.clazz = clazz;
			this.items = items;
			this.maxMana = maxMana;
		}
		
		public Class<? extends IMagicBase> getClazz() {
			return clazz;
		}
		
		public List<Material> getItems() {
			return items;
		}
		
		public int getMaxMana() {
			return maxMana;
		}
	}
}
