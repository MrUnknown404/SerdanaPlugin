package main.java.serdana.util.infos;

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
			BountyInfo info = (BountyInfo) obj;
			
			if (info.ownerUUID.equals(ownerUUID)) {
				if (info.toKillUUID.equals(toKillUUID)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
