package com.github.lutzblox.engine.debugging;

public class DebugConfig {

	private static boolean enabled = false, printSettings = false, printResolutions = false;
	
	public static void enable(boolean enabled){
		
		DebugConfig.enabled = enabled;
	}
	
	public static boolean isEnabled(){
		
		return enabled;
	}
	
	public static void setPrintSettings(boolean printSet){
		
		DebugConfig.printSettings = printSet;
	}
	
	public static boolean getPrintSettings(){
		
		return printSettings;
	}
	
	public static void setPrintResolutions(boolean printRes){
		
		DebugConfig.printResolutions = printRes;
	}
	
	public static boolean getPrintResolutions(){
		
		return printResolutions;
	}
}
