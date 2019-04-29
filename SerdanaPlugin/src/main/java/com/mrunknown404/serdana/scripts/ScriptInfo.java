package main.java.com.mrunknown404.serdana.scripts;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ScriptInfo implements ConfigurationSerializable {

	private int scriptTaskID;
	private String scriptName;
	private ScriptStartType type;
	
	public ScriptInfo(int scriptTaskID, String scriptName, ScriptStartType type) {
		this.scriptTaskID = scriptTaskID;
		this.scriptName = scriptName;
		this.type = type;
	}
	
	public ScriptInfo(Map<String, Object> map) {
		scriptTaskID = (int) map.get("scriptTaskID");
		scriptName = (String) map.get("scriptName");
		type = ScriptStartType.valueOf((String) map.get("type"));
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
	
	public ScriptStartType getStartType() {
		return type;
	}
	
	public enum ScriptStartType {
		start,
		finish;
	}
}
