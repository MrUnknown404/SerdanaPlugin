package main.java.com.mrunknown404.serdana.scripts;

import java.util.ArrayList;
import java.util.List;

import main.java.com.mrunknown404.serdana.util.enums.EnumScriptStartType;

public class InitScripts {
	private static List<ScriptInfo> allScripts = new ArrayList<ScriptInfo>();
	
	/** Registers all {@link ScriptInfo} */
	public static void register() {
		allScripts.clear();
		
		addScript(new ScriptInfo(0, "TestScript", EnumScriptStartType.start));
	}
	
	/** Adds the given {@link ScriptInfo} to a {@link List} that containing all Scripts
	 * @param s Script to add
	 */
	private static void addScript(ScriptInfo s) {
		allScripts.add(s);
	}
	
	/** Gets all {@link ScriptInfo} from the given names
	 * @param name Name of the ScriptInfo to get
	 * @return The ScriptInfo with the given name
	 */
	public static ScriptInfo[] getScripts(String[] names) {
		List<ScriptInfo> scripts = new ArrayList<ScriptInfo>();
		
		for (ScriptInfo s : allScripts) {
			for (String name : names) {
				if (s.getScriptName().equalsIgnoreCase(name)) {
					scripts.add(s);
				}
			}
		}
		
		return scripts.toArray(new ScriptInfo[scripts.size()]);
	}
	
	/** Checks if a {@link ScriptInfo} with the given name exists
	 * @param name Name of the ScriptInfo to check for
	 * @return true if the there is a ScriptInfo with the given name, otherwise false
	 */
	public static boolean doesScriptExist(String name) {
		if (getScript(name) != null) {
			return true;
		}
		
		return false;
	}
	
	/** Gets a {@link ScriptInfo} from the given name
	 * @param name Name of the ScriptInfo to get
	 * @return The ScriptInfo with the given name
	 */
	public static ScriptInfo getScript(String name) {
		for (ScriptInfo s : allScripts) {
			if (s.getScriptName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		
		return null;
	}
	
	public static List<ScriptInfo> getAllScripts() {
		return allScripts;
	}
}
