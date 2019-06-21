package main.java.serdana.scripts;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import main.java.serdana.util.enums.EnumScriptStartType;

public class ScriptInfo implements ConfigurationSerializable {

	private final int scriptTaskID;
	private final String scriptName;
	private final EnumScriptStartType type;
	
	ScriptInfo(int scriptTaskID, String scriptName, EnumScriptStartType type) {
		this.scriptTaskID = scriptTaskID;
		this.scriptName = scriptName;
		this.type = type;
	}
	
	public ScriptInfo(Map<String, Object> map) {
		scriptTaskID = (int) map.get("scriptTaskID");
		scriptName = (String) map.get("scriptName");
		type = EnumScriptStartType.valueOf((String) map.get("type"));
	}
	
	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("scriptTaskID", scriptTaskID);
		result.put("scriptName", scriptName);
		result.put("type", type.toString());
		return result;
	}
	
	public int getScriptTaskID() {
		return scriptTaskID;
	}
	
	public String getScriptName() {
		return scriptName;
	}
	
	public EnumScriptStartType getStartType() {
		return type;
	}
}
