package main.java.com.mrunknown404.serdana.util.infos;

import java.util.UUID;

public class BountyInfo {

	private UUID ownerUUID;
	private UUID toKillUUID;
	private int amountOfTokens;
	
	public BountyInfo(UUID ownerUUID, UUID toKillUUID, int amountOfTokens) {
		this.ownerUUID = ownerUUID;
		this.toKillUUID = toKillUUID;
		this.amountOfTokens = amountOfTokens;
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public UUID getToKillUUID() {
		return toKillUUID;
	}
	
	public int getAmountOfTokens() {
		return amountOfTokens;
	}
	
	@Override
	public String toString() {
		return "(" + ownerUUID + ", " + toKillUUID + ", " + amountOfTokens + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BountyInfo) {
			if (((BountyInfo) obj).ownerUUID.equals(ownerUUID)) {
				if (((BountyInfo) obj).toKillUUID.equals(toKillUUID)) {
					if (((BountyInfo) obj).amountOfTokens == amountOfTokens) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
