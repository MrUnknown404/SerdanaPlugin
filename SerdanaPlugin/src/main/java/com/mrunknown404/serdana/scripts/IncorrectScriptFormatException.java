package main.java.com.mrunknown404.serdana.scripts;

public class IncorrectScriptFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public IncorrectScriptFormatException(IncorrectScriptFormatType type, int line) {
		super("Incorrect Script Format l:" + (line + 1));
		line += 1;
		
		switch (type) {
			case unknownCommand:
				System.err.println("Unknown command at line l:" + line);
				break;
			case incorrectCommandFormat:
				System.err.println("Incorrect command format on line l:" + line);
				break;
			case incorrectCommandArguments:
				System.err.println("Incorrect command argument on line l:" + line);
				break;
			case incorrectCMDArguments:
				System.err.println("Incorrect CMD arguments on line l:" + line);
				break;
			case forcedWait:
				System.err.println("force:wait on line l:" + line + " is not allowed as it would do nothing");
				break;
			case commandError:
				System.err.println("Something is written incorrectly on line l:" + line);
				break;
		}
	}
	
	public IncorrectScriptFormatException(int line) {
		super("Incorrect Script Format l:" + line);
	}
	
	public enum IncorrectScriptFormatType {
		unknownCommand,
		incorrectCommandFormat,
		incorrectCommandArguments,
		incorrectCMDArguments,
		forcedWait,
		commandError;
	}
}
