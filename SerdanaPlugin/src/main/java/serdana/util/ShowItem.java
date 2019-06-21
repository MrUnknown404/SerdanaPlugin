package main.java.serdana.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class ShowItem {
	
	/** Shows all {@link Player}s the given Player's {@link ItemStack} in their hand
	 * @param p Player to get the ItemStack from
	 */
	public ShowItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		
		StringBuilder msg = new StringBuilder();
		StringBuilder metaBuilder = new StringBuilder();
		
		String itemName = "" + item.getType().toString().toLowerCase();
		String itemDisplayName = WordUtils.capitalize(itemName.replaceAll("_", " "));
		String itemLore = "";
		String itemEnchants = "";
		String itemStoredEnchants = "";
		String itemBookInfo = "";
		String itemPotionInfo = "";
		
		if (item.getItemMeta().hasDisplayName()) {
			itemDisplayName = item.getItemMeta().getDisplayName();
		}
		
		if (item.getItemMeta().getLore() != null) {
			List<String> itemLoreList = item.getItemMeta().getLore();
			metaBuilder.append(",\\\"Lore\\\": [\\\"");
			
			for (int i = 0; i < itemLoreList.size(); i++) {
				metaBuilder.append(itemLoreList.get(i));
				if (i != itemLoreList.size() - 1) {
					metaBuilder.append(",");
				}
			}
			
			metaBuilder.append("\\\"]");
			itemLore = metaBuilder.toString();
		}
		
		if (item.getEnchantments().size() > 0) {
			Map<Enchantment, Integer> itemEnchantsMap = item.getItemMeta().getEnchants();
			metaBuilder = new StringBuilder();
			metaBuilder.append(",\\\"Enchantments\\\":[");
			
			Iterator<Map.Entry<Enchantment, Integer>> it = itemEnchantsMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) it.next();
				
				metaBuilder.append("{\\\"id\\\": \\\"" + pair.getKey().getKey() + "\\\",\\\"lvl\\\": " + pair.getValue() + "}");
				if (it.hasNext()) {
					metaBuilder.append(",");
				}
			}
			
			metaBuilder.append("]");
			itemEnchants = metaBuilder.toString();
		}
		
		if (item.getType() == Material.ENCHANTED_BOOK) {
			Set<Entry<Enchantment, Integer>> itemStoredEnchantsMap = ((EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants().entrySet();
			metaBuilder = new StringBuilder();
			metaBuilder.append(",\\\"Enchantments\\\":[");
			
			Iterator<Map.Entry<Enchantment, Integer>> it = itemStoredEnchantsMap.iterator();
			
			while (it.hasNext()) {
				Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) it.next();
				
				metaBuilder.append("{\\\"id\\\": \\\"" + pair.getKey().getKey() + "\\\",\\\"lvl\\\": " + pair.getValue() + "}");
				if (it.hasNext()) {
					metaBuilder.append(",");
				}
			}
			
			metaBuilder.append("]");
			itemStoredEnchants = metaBuilder.toString();
		} else if (item.getType() == Material.WRITTEN_BOOK) {
			BookMeta bookMeta = (BookMeta) item.getItemMeta();
			
			metaBuilder = new StringBuilder();
			metaBuilder.append(",\\\"author\\\": \\\"" + bookMeta.getAuthor() + "\\\"");
			
			itemDisplayName = bookMeta.getTitle();
			itemBookInfo = metaBuilder.toString();
		} else if (item.getType() == Material.POTION) {
			PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
			
			for(int i =0; i< potionMeta.getCustomEffects().size(); i++) {
				Object potionEffect = potionMeta.getCustomEffects().get(i);
				append(potionEffect);
			};
			
			itemPotionInfo = metaBuilder.toString();
		} else if (item.getType() == Material.LINGERING_POTION) {
			PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
			
			for(int i =0; i< potionMeta.getCustomEffects().size(); i++) {
				Object potionEffect = potionMeta.getCustomEffects().get(i);
				append(potionEffect);
			};
			
			itemPotionInfo = metaBuilder.toString();
		} else if (item.getType() == Material.SPLASH_POTION) {
			PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
			
			for(int i =0; i< potionMeta.getCustomEffects().size(); i++) {
				Object potionEffect = potionMeta.getCustomEffects().get(i);
				append(potionEffect);
			};
			
			itemPotionInfo = metaBuilder.toString();
		}
		
		if (itemDisplayName.contains("\"")) {
			itemDisplayName = itemDisplayName.replaceAll("\"", "''");
		}
		
		msg.append("{\"text\": \"" + p.getDisplayName() + " has shared [" + itemDisplayName + "\u00a7f]\",\"hoverEvent\": {\"action\": \"show_item\",\"value\":\""
				+ "{\\\"id\\\": \\\"" + itemName + "\\\","
				+ "\\\"Count\\\": 1,"
				+ "\\\"tag\\\":{\\\"display\\\": {\\\"Name\\\":\\\"\\\\\\\"" + itemDisplayName + "\\\\\\\"\\\""
				+ itemLore
				+ "}"
				+ itemEnchants
				+ itemStoredEnchants
				+ itemBookInfo
				+ itemPotionInfo
				+ "}}\"}}"
				);
		
		p.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a " + msg.toString());
	}

	private void append(Object potionEffect) {
		// TODO Auto-generated method stub
		
	}
}
