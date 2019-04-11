package main.java.com.mrunknown404.serdana.util.infos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class PrayInfo implements ConfigurationSerializable {
	private UUID ownerUUID;
	private ItemStack book;
	
	public PrayInfo(UUID ownerUUID, ItemStack book) {
		this.ownerUUID = ownerUUID;
		this.book = book;
	}
	
	public PrayInfo(Map<String, Object> map) {
		ownerUUID = UUID.fromString((String) map.get("ownerUUID"));
		book = (ItemStack) map.get("book");
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("ownerUUID", ownerUUID.toString());
		result.put("book", book);
		return result;
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public ItemStack getBook() {
		return book;
	}
	
	@Override
	public String toString() {
		return "(" + ownerUUID + ", " + book + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PrayInfo) {
			if (((PrayInfo) obj).ownerUUID.equals(ownerUUID)) {
				if (((PrayInfo) obj).book.equals(book)) {
					return true;
				}
			}
		}
		return false;
	}
}
