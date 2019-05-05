package main.java.com.mrunknown404.serdana.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import main.java.com.mrunknown404.serdana.Main;
import main.java.com.mrunknown404.serdana.scripts.IncorrectScriptFormatException.IncorrectScriptFormatType;

public class ScriptHandler {
	public static final String SCRIPT_FILE_TYPE = ".txt";
	
	private static final File path = Bukkit.getPluginManager().getPlugin("Serdana").getDataFolder();
	private static Map<UUID, List<String>> lines = new HashMap<UUID, List<String>>(), force = new HashMap<UUID, List<String>>();
	
	private static int waitTime, lineCounter;
	
	/** Reads the given {@link ScriptInfo}
	 * @param info The ScriptInfo to read
	 * @param p Player to run off of
	 */
	public static void read(ScriptInfo info, Player p) {
		if (getScriptFile(info.getScriptName()) != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(getScriptFile(info.getScriptName())));
			
			String st;
			List<String> strs = new ArrayList<String>();
			try {
				while ((st = br.readLine()) != null) {
					strs.add(st);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			lines.put(p.getUniqueId(), strs);
		}
	}
	
	/** Runs the Script's lines every Tick */
	public static void run(Main main) {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				if (waitTime == 0) {
					if (!lines.isEmpty()) {
						Iterator<Entry<UUID, List<String>>> it = lines.entrySet().iterator();
						boolean found = false;
						
						while (it.hasNext()) {
							Entry<UUID, List<String>> pair = it.next();
							
							if (lineCounter < pair.getValue().size()) {
								try {
									runLine(pair.getValue().get(lineCounter), Bukkit.getPlayer(pair.getKey()), lineCounter, false);
								} catch (IncorrectScriptFormatException e) {
									e.printStackTrace();
								}
								
								found = true;
								lineCounter++;
							}
						}
						
						if (!found) {
							lines.clear();
							lineCounter = 0;
						}
					}
				} else {
					waitTime--;
				}
				
				if (lines.isEmpty() && !force.isEmpty()) {
					force.clear();
				}
				
				if (!force.isEmpty()) {
					Iterator<Entry<UUID, List<String>>> it = force.entrySet().iterator();
					
					while (it.hasNext()) {
						Entry<UUID, List<String>> pair = it.next();
						
						if (!lines.containsKey(pair.getKey())) {
							force.remove(pair.getKey());
						}
						
						if (force.containsKey(pair.getKey())) {
							for (int i = 0; i < pair.getValue().size(); i++) {
								String line = pair.getValue().get(i);
								
								try {
									runLine(line, Bukkit.getPlayer(pair.getKey()), i, true);
								} catch (IncorrectScriptFormatException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}, 0L, 1L);
	}
	
	/** Runs the given line
	 * @param line Line to run
	 * @param p Player to run off of
	 * @param wasForced Should add to force list
	 * @throws IncorrectScriptFormatException 
	 */
	private static void runLine(String line, Player p, int lineNumber, boolean wasForced) throws IncorrectScriptFormatException {
		if (line.isEmpty()) {
			return;
		} else if (line.startsWith("//") || line.startsWith("#")) {
			return;
		}
		
		int charCount = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ':') {
				charCount++;
			}
		}
		
		if (line.startsWith(Commands.force.toString()) && charCount != 2) {
			throw new IncorrectScriptFormatException(IncorrectScriptFormatType.incorrectCommandFormat, lineNumber);
		} else if (!line.startsWith(Commands.force.toString()) && charCount != 1) {
			throw new IncorrectScriptFormatException(IncorrectScriptFormatType.incorrectCommandFormat, lineNumber);
		}
		
		boolean isForce = false;
		String cmd, args;
		int space = 1;
		
		if (line.startsWith(Commands.force.toString())) {
			String tCmd = line.substring(line.indexOf(':') + 1, line.length());
			cmd = tCmd.substring(0, tCmd.indexOf(':'));
			
			if (tCmd.charAt(tCmd.indexOf(':') + 1) == ' ') {
				space++;
			}
			
			args = Pattern.quote(line.substring(line.length() - tCmd.length() + cmd.length() + space));
			
			isForce = true;
		} else {
			cmd = line.substring(0, line.indexOf(':'));
			
			if (line.charAt(line.indexOf(':') + 1) == ' ') {
				space++;
			}
			
			args = Pattern.quote(line.substring(cmd.length() + space));
		}
		
		args = args.substring(2, args.length() - 2);
		
		if (!Commands.contains(cmd)) {
			throw new IncorrectScriptFormatException(IncorrectScriptFormatType.unknownCommand, lineNumber);
		}
		
		switch(Commands.valueOf(cmd)) {
			case print:
				if (args.startsWith("(") && args.endsWith(")")) {
					args = args.replaceAll("\\(", "").replaceAll("\\)", "");
					
					if (args.startsWith("\"") && args.endsWith("\"")) {
						args = args.replaceAll("\"", "");
						System.out.println(args);
						
						if (isForce && !wasForced) {
							addForce(p.getUniqueId(), line);
						}
						
						return;
					}
				}
				
				throw new IncorrectScriptFormatException(IncorrectScriptFormatType.commandError, lineNumber);
			case cmd:
				if (args.startsWith("(") && args.endsWith(")")) {
					args = args.replaceAll("\\(", "").replaceAll("\\)", "");
					
					if (args.startsWith("\"") && args.endsWith("\"")) {
						args = args.replaceAll("\"", "");
						
						if (args.contains("{player}")) {
							args = args.replaceAll("\\{player\\}", p.getName());
						}
						
						args = args.replaceAll("\"", "");
						
						try {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args);
							
							if (isForce && !wasForced) {
								addForce(p.getUniqueId(), line);
							}
							
							return;
						} catch (CommandException e) {
							throw new IncorrectScriptFormatException(IncorrectScriptFormatType.incorrectCMDArguments, lineNumber);
						}
					}
				}
				
				throw new IncorrectScriptFormatException(IncorrectScriptFormatType.commandError, lineNumber);
			case wait:
				try {
					if (isForce && !wasForced) {
						throw new IncorrectScriptFormatException(IncorrectScriptFormatType.forcedWait, lineNumber);
					}
					
					waitTime = Integer.parseInt(args.replaceAll("\\(", "").replaceAll("\\)", ""));
					return;
				} catch (NumberFormatException e) {
					throw new IncorrectScriptFormatException(IncorrectScriptFormatType.incorrectCommandArguments, lineNumber);
				}
			default:
				throw new IncorrectScriptFormatException(IncorrectScriptFormatType.unknownCommand, lineNumber);
		}
	}
	
	/** Adds given {@link UUID} & line to the force {@link Map}
	 * @param id UUID to add
	 * @param line Line to add
	 */
	private static void addForce(UUID id, String line) {
		List<String> tList = new ArrayList<String>();
		
		if (force.containsKey(id)) {
			tList = force.get(id);
		}
		
		tList.add(line);
		force.put(id, tList);
	}
	
	/** Gets a {@link InputStream} for the given {@link File} name
	 * @param file File name to find
	 * @return An InputStream based around the given File name
	 */
	private static InputStream getScriptFile(String file) {
		if (ScriptHandler.class.getResourceAsStream(Main.BASE_LOCATION_SCRIPTS + file + SCRIPT_FILE_TYPE) == null) {
			System.out.println("Could not find file inside jar: " + file + SCRIPT_FILE_TYPE + "!");
			File f = new File(path + "/Scripts/" + file + SCRIPT_FILE_TYPE);
			
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				System.out.println("Could not find file in config: " + file + SCRIPT_FILE_TYPE + "! (Will be created)");
				
				try {
					f.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			return ScriptHandler.class.getResourceAsStream(Main.BASE_LOCATION_SCRIPTS + file + SCRIPT_FILE_TYPE);
		}
		
		return null;
	}
	
	private enum Commands {
		print,
		cmd,
		wait,
		force;
		
		public static boolean contains(String str) {
			for (Commands type : Commands.values()) {
				if (type.name().equals(str)) {
					return true;
				}
			}
			
			return false;
		}
	}
}
