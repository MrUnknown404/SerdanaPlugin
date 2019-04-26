package main.java.com.mrunknown404.serdana.quests;

public enum EnumQuestState {
	unknown,
	accepted,
	finished;
	
	/** Checks if the given String is a {@link EnumQuestState}
	 * @param str String to check
	 * @return true if the given String matches any enum names, otherwise false
	 */
	public static boolean contains(String str) {
		for (EnumQuestState type : EnumQuestState.values()) {
			if (type.name().equals(str)) {
				return true;
			}
		}
		
		return false;
	}
}
