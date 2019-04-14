package main.java.com.mrunknown404.serdana.quests;

public enum EnumQuestState {
	unknown,
	accepted,
	finished;
	
	public static boolean contains(String str) {
		for (EnumQuestState type : EnumQuestState.values()) {
			if (type.name().equals(str)) {
				return true;
			}
		}
		
		return false;
	}
}
