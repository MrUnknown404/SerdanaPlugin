package main.java.com.mrunknown404.serdana.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonShopInfo {

	public final transient String fileName;
	public final Map<Integer, List<String>> info;
	
	public JsonShopInfo(Map<Integer, List<String>> info, String fileName) {
		this.fileName = fileName;
		this.info = info;
	}
	
	public JsonShopInfo(String fileName) {
		this.fileName = fileName;
		this.info = new HashMap<Integer, List<String>>();
	}
	
	@Override
	public String toString() {
		return "(" + info + ")";
	}
}
